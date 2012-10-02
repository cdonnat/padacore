package org.padacore.core.utils;

import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ExternalProcessJob extends Job {

	private ExternalProcess externalProcess;
	private String[] cmdWithArgs;

	private ExternalProcessJob(String name, String[] cmdWithArgs, Observer[] outputObservers,
			Observer[] errObservers) {
		super(name);
		this.cmdWithArgs = cmdWithArgs;
		this.externalProcess = new ExternalProcess(outputObservers, errObservers);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		this.externalProcess.run(cmdWithArgs, monitor);

		return Status.OK_STATUS;
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

	public static void run(String name, String[] cmdWithArgs, Observer[] outputObservers) {
		run(name, cmdWithArgs, outputObservers, new Observer[] {});
	}

	public static void run(String name, String[] cmdWithArgs, Observer[] outputObservers,
			Observer[] errObservers) {
		ExternalProcessJob job = new ExternalProcessJob(name, cmdWithArgs, outputObservers,
				errObservers);
		job.schedule();
	}

	public static void runWithDefaultOutput(String name, String cmd) {
		run(name, new String[] { cmd }, new Observer[] { new ExternalProcessOutput() },
				new Observer[] { new ExternalProcessOutput() });
	}

	public static void runWithDefaultOutput(String name, String[] cmdWithArgs) {
		run(name, cmdWithArgs, new Observer[] { new ExternalProcessOutput() },
				new Observer[] { new ExternalProcessOutput() });
	}
}
