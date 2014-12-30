package edu.udel.cis.vsl.sarl.util;

/**
 * Utility class providing means to control processes.
 * 
 * @author siegel
 *
 */
public class ProcessControl {

	/**
	 * Given an existing process and time limit, this method will wait until
	 * either the process has terminated, or the time limit has been reached,
	 * whichever comes first. It will not modify the process.
	 * 
	 * @param process
	 *            a non-null process
	 * @param timeout
	 *            a time limit, in seconds
	 * @return <code>true</code> if the process terminated, <code>false</code>
	 *         if the timeout was reached
	 */
	public static boolean waitForProcess(Process process, double timeout) {
		ProcessTimer pt = new ProcessTimer(Thread.currentThread(), timeout);

		try {
			pt.start();
			process.waitFor();
			pt.setTerminated(true);
			return true;
		} catch (InterruptedException e) {
			// time is up
		}
		return false;
	}
}

class ProcessTimer extends Thread {

	private Thread mainThread;

	private double timeout;

	private long stopTime;

	private boolean terminated = false;

	public ProcessTimer(Thread mainThread, double timeout) {
		this.mainThread = mainThread;
		this.timeout = timeout;
	}

	public synchronized boolean getTerminated() {
		return terminated;
	}

	public synchronized void setTerminated(boolean value) {
		terminated = value;
	}

	@Override
	public void run() {
		stopTime = System.currentTimeMillis() + (long) (timeout * 1000);
		while (!getTerminated()) {
			if (System.currentTimeMillis() > stopTime) {
				mainThread.interrupt();
				break;
			}
			try {
				sleep(100); // 100 milliseconds = 1/10 sec.
			} catch (InterruptedException e) {
				// shouldn't happen, but just continue
			}
		}
	}

}
