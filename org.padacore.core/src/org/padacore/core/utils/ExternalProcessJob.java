package org.padacore.core.utils;

import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ExternalProcessJob extends PadacoreJob {

	private ExternalProcess externalProcess;
	private String[] cmdWithArgs;

	private ExternalProcessJob(String name, String[] cmdWithArgs,
			Observer[] outputObservers, Observer[] errObservers) {
		super(name);
		this.cmdWithArgs = cmdWithArgs;
		this.externalProcess = new ExternalProcess(name, new Console(),
				outputObservers, errObservers);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStatus returnStatus = Status.OK_STATUS;

		try {
			this.externalProcess.run(cmdWithArgs, monitor);
		} catch (ProgramNotFoundException e) {
			returnStatus = Status.CANCEL_STATUS;
		}

		return returnStatus;
	}

	public static void run(String name, String cmd) {
		run(name, cmd, new Observer[] {}, new Observer[] {});
	}

	public static void run(String name, String cmd, Observer[] outputObservers) {
		run(name, cmd, outputObservers, new Observer[] {});
	}

	public static void run(String name, String cmd, Observer[] outputObservers,
			Observer[] errObservers) {
		run(name, new String[] { cmd }, outputObservers, errObservers);
	}

	public static void run(String name, String[] cmdWithArgs) {
		run(name, cmdWithArgs, new Observer[] {}, new Observer[] {});
	}

	public static void run(String name, String[] cmdWithArgs,
			Observer[] outputObservers) {
		run(name, cmdWithArgs, outputObservers, new Observer[] {});
	}

	public static void run(String name, String[] cmdWithArgs,
			Observer[] outputObservers, Observer[] errObservers) {
		ExternalProcessJob job = new ExternalProcessJob(name, cmdWithArgs,
				outputObservers, errObservers);
		job.schedule();
	}

	public static void runWithDefaultOutput(String name, String cmd) {
		Console console = new Console();
		run(name, new String[] { cmd },
				new Observer[] { new ExternalProcessOutput(console) },
				new Observer[] { new ExternalProcessOutput(console) });
	}
}
