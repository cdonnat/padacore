package org.padacore.core.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.utils.ErrorLog;

public class LaunchingJob extends Job {

	private ILaunchConfiguration launchConfig;

	public LaunchingJob(IPath absoluteExecPath,
			ILaunchConfiguration launchConfig) {
		super("Launching " + absoluteExecPath);
		this.launchConfig = launchConfig;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStatus launchStatus = Status.OK_STATUS;

		try {
			this.launchConfig.launch(ILaunchManager.RUN_MODE, monitor);

		} catch (CoreException e) {
			ErrorLog.appendException(e);
			launchStatus = e.getStatus();
		}

		return launchStatus;
	}

}