package readerwriter;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLock {
    private int writersQueue;
    private int readersQueue;
    private boolean writing;
    private ReentrantReadWriteLock rwl;


    public RWLock(){
        rwl = new ReentrantReadWriteLock();
        writersQueue = 0;
        readersQueue = 0;
        writing = false;

    }

    public void acquireRead(){
        rwl.readLock().lock();

    }

    public void acquireWrite(){
        rwl.writeLock().lock();

    }

    public void releaseRead(){
        rwl.readLock().unlock();

    }

    public void releaseWrite(){
        rwl.writeLock().unlock();

    }




    // I actually managed to do get a solution without starvation using a monitor:

    public synchronized void acquireReadSynchronized(){
        // only if a writer is writing or waiting
        try {
            while (writersQueue > 0 || writing) {
                wait();
            }
            readersQueue++;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void acquireWriteSynchronized(){
        try {
            writersQueue++;

            // only if a writer is writing and at least one reader is reading
            while (writing || readersQueue > 0) {
                wait();
            }
            writersQueue--;
            writing = true;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void releaseReadSynchronized(){
        readersQueue--;
        notifyAll();
    }

    public synchronized void releaseWriteSynchronized(){
        writing = false;
        notifyAll();
    }

}
