package org.padacore.core.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public class ExternalProcess implements IExternalProcess {

	private Process process;
	private List<Thread> threads;
	private Observer[] outStreamObservers;
	private Observer[] errStreamObservers;
	private ExternalProcessInfo info;

	/**
	 * Attach given output and error stream observers to the external process.
	 * 
	 * @param outStreamObservers
	 *            Output stream observers.
	 * @param errStreamObservers
	 *            Error stream observers.
	 */
	public ExternalProcess(String processName, IConsole console,
			Observer[] outStreamObservers, Observer[] errStreamObservers) {
		this.info = new ExternalProcessInfo(processName, console);
		this.threads = new ArrayList<Thread>(3);
		this.outStreamObservers = outStreamObservers;
		this.errStreamObservers = errStreamObservers;
	}

	@Override
	public void run(String[] cmdWithArgs, IProgressMonitor monitor) {
		ProcessBuilder processBuilder = new ProcessBuilder(cmdWithArgs);

		this.info.start(cmdWithArgs);
		try {
			this.run(monitor, processBuilder);
		} catch (IOException e) {
			ErrorLog.appendException(e, IStatus.ERROR);
		} finally {
			this.info.finish(this.isSuccessful());
		}
	}

	private void runCmd(IProgressMonitor monitor, ProcessBuilder processBuilder)
			throws IOException {
		try {
			this.process = processBuilder.start();

			this.threads.add(new Thread(new StreamReader(process
					.getInputStream(), outStreamObservers)));
			this.threads.add(new Thread(new StreamReader(process
					.getErrorStream(), errStreamObservers)));
			this.threads.add(new ExternalProcessCanceler(this, monitor));
		} catch (IOException e) {
			throw e;
		}
	}

	private void startThreads() {
		for (Thread thread : this.threads) {
			thread.start();
		}
	}

	private void waitUntilTheEndOfTheCmd() {
		try {
			this.process.waitFor();
			for (Thread thread : this.threads) {
				thread.join();
			}
		} catch (InterruptedException e) {
			ErrorLog.appendException(e, IStatus.WARNING);
		}
	}

	private void run(IProgressMonitor monitor, ProcessBuilder processBuilder)
			throws IOException {
		try {
			this.runCmd(monitor, processBuilder);
			this.startThreads();
			this.waitUntilTheEndOfTheCmd();
		} catch (IOException e) {
			throw e;
		}
	}

	@Override
	public boolean isFinished() {
		boolean isFinished = true;
		try {
			this.process.exitValue();
		} catch (IllegalThreadStateException e) {
			isFinished = false;
		}
		return isFinished;
	}

	@Override
	public void stop() {
		this.process.destroy();
	}

	private boolean isSuccessful() {
		return this.process != null && this.process.exitValue() == 0;
	}
}
