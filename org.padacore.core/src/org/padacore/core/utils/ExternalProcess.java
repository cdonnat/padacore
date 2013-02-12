package org.padacore.core.utils;

import java.io.IOException;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public class ExternalProcess implements IExternalProcess {

	private Process process;
	private Thread outReader;
	private Thread errReader;
	private Observer[] outStreamObservers;
	private Observer[] errStreamObservers;
	private ExternalProcessCanceler canceler;
	private ExternalProcessInfo info;

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
		this.info = new ExternalProcessInfo(processName);
		this.outStreamObservers = outStreamObservers;
		this.errStreamObservers = errStreamObservers;
	}

	@Override
	public void run(String[] cmdWithArgs, IProgressMonitor monitor) {
		ProcessBuilder processBuilder = new ProcessBuilder(cmdWithArgs);

		try {
			info.start(cmdWithArgs);
			runCmd(monitor, processBuilder);
			info.finish(isSuccessful());
		} catch (IOException e) {
			ErrorLog.appendMessage("Error while launching external command: "
					+ cmdWithArgs[0], IStatus.ERROR);
		}
	}

	private void runCmd(IProgressMonitor monitor, ProcessBuilder processBuilder)
			throws IOException {
		process = processBuilder.start();

		outReader = new Thread(new StreamReader(process.getInputStream(),
				outStreamObservers));
		errReader = new Thread(new StreamReader(process.getErrorStream(),
				errStreamObservers));
		canceler = new ExternalProcessCanceler(this, monitor);

		outReader.start();
		errReader.start();
		canceler.start();

		try {
			outReader.join();
			errReader.join();
			canceler.join();
		} catch (InterruptedException e) {
			ErrorLog.appendException(e, IStatus.WARNING);
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
		return process.exitValue() == 0;
	}
}
