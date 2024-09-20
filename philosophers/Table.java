package philosophers;


import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Table {

	private int nbrOfChopsticks;
	private boolean chopstick[]; // true if chopstick[i] is available

	public Table(int nbrOfSticks) {
		nbrOfChopsticks = nbrOfSticks;
		chopstick = new boolean[nbrOfChopsticks];
		for (int i = 0; i < nbrOfChopsticks; i++) {
			chopstick[i] = true;
		}
	}

	public synchronized void getLeftChopstick(int n) {
		int pos = n + 1;
		if (pos == nbrOfChopsticks)
			pos = 0;

		try {
			while (!chopstick[n] && !chopstick[pos]) {
				wait();
			}

			chopstick[n] = false; //chopstick is taken

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void getRightChopstick(int n) {
		int pos = n + 1;
		if (pos == nbrOfChopsticks)
			pos = 0;

		try {
			while (!chopstick[pos]) {
				wait();
			}

			chopstick[n] = false; //chopstick is taken

		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
	}

	public synchronized void releaseLeftChopstick(int n) {
		chopstick[n] = true;
		notifyAll();
	}

	public synchronized void releaseRightChopstick(int n) {
		int pos = n + 1;
		if (pos == nbrOfChopsticks)
			pos = 0;
		chopstick[pos] = true;
		notifyAll();
	}
}
