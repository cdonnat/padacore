package org.padacore.core.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;

public class ExternalProcess {

	/**
	 * Monitor an external process in order to stop it when required by the
	 * monitor.
	 */
	private class Monitor extends Thread {

		private ExternalProcess process;
		private IProgressMonitor monitor;

		public Monitor(ExternalProcess process, IProgressMonitor monitor) {
			this.process = process;
			this.monitor = monitor;
		}

		@Override
		public void run() {
			while (!this.process.isFinished()) {
				if (this.monitor.isCanceled()) {
					this.process.stop();
				}
			}
		}
	}

	private Process process;
	private Thread outReader;
	private Thread errReader;
	private Observer[] outStreamObservers;
	private Observer[] errStreamObservers;
	private Monitor processMonitor;
	private long startTimeInMs;
	private String processName;

	private static final DateFormat Format = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Default constructor.
	 */
	public ExternalProcess(String processName) {
		this(processName, new Observer[0], new Observer[0]);
	}

	/**
	 * Attach given output stream observers to the external process. No observer
	 * is attached to the error stream.
	 * 
	 * @param outStreamObservers
	 *            Output stream observers.
	 */
	public ExternalProcess(String processName, Observer[] outStreamObservers) {
		this(processName, outStreamObservers, new Observer[0]);
	}

	/**
	 * Attach given output and error stream observers to the external process.
	 * 
	 * @param outStreamObservers
	 *            Output stream observers.
	 * @param errStreamObservers
	 *            Error stream observers.
	 */
	public ExternalProcess(String processName, Observer[] outStreamObservers,
			Observer[] errStreamObservers) {
		this.processName = processName;
		this.outStreamObservers = outStreamObservers;
		this.errStreamObservers = errStreamObservers;
	}

	/**
	 * Run the command given in parameter and attach to observers given to
	 * constructors. Observers (output stream and error stream) are notify when
	 * a new entry is written in the stream.
	 * 
	 * @param cmdWithArgs
	 *            Command to run
	 * @param monitor
	 *            Progress monitor to display external process progress
	 */
	public void run(String[] cmdWithArgs, IProgressMonitor monitor) {
		ProcessBuilder processBuilder = new ProcessBuilder(cmdWithArgs);

		try {
			displayRunCmd(cmdWithArgs);
			runCmd(monitor, processBuilder);
			computeAndDisplayElapsedTime();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void runCmd(IProgressMonitor monitor, ProcessBuilder processBuilder) throws IOException {
		process = processBuilder.start();
		startTimeInMs = System.currentTimeMillis();

		outReader = new Thread(new StreamReader(process.getInputStream(), outStreamObservers));
		errReader = new Thread(new StreamReader(process.getErrorStream(), errStreamObservers));
		processMonitor = new Monitor(this, monitor);

		outReader.start();
		errReader.start();
		processMonitor.start();

		try {
			outReader.join();
			errReader.join();
			processMonitor.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void displayRunCmd(String[] cmdWithArgs) {
		StringBuilder cmd = new StringBuilder();
		for (int i = 0; i < cmdWithArgs.length; i++) {
			cmd.append(cmdWithArgs[i]);
			cmd.append(" ");
		}
		Console.Print(cmd.toString());
	}

	/**
	 * @return True is returned if the external process is finished.
	 */
	private boolean isFinished() {
		boolean isFinished = true;
		try {
			this.process.exitValue();
		} catch (IllegalThreadStateException e) {
			isFinished = false;
		}
		return isFinished;
	}

	/**
	 * Stop the external process.
	 */
	private void stop() {
		this.process.destroy();
	}
	
	private void computeAndDisplayElapsedTime() {
		Assert.isLegal(isFinished());

		final double elapsedTimeInS = (System.currentTimeMillis() - startTimeInMs) / 1000.0;

		StringBuilder message = new StringBuilder(Format.format(new Date()));
		message.append(" ");
		message.append(processName);

		if (process.exitValue() == 0) {
			message.append(" successful in ");
		} else {
			message.append(" failed in ");
		}
		message.append(elapsedTimeInS);
		message.append(" seconds");

		Console.Print(message.toString());
	}
}
