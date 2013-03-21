package org.padacore.core.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.padacore.core.Activator;
import org.padacore.core.utils.ErrorLog;

public class BuildingJob extends Job {
	private IProject launchedProject;
	private IPath absoluteExecPath;

	public BuildingJob(IPath absoluteExecPath, IProject launchedProject) {
		super("Building before launching " + absoluteExecPath);

		this.absoluteExecPath = absoluteExecPath;
		this.launchedProject = launchedProject;
	}

	@Override
	/**
	 * @pre Executable does not exist yet.
	 */
	protected IStatus run(IProgressMonitor monitor) {
		Assert.isLegal(!absoluteExecPath.toFile().exists());

		IStatus returnStatus = Status.OK_STATUS;

		try {
			launchedProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
			returnStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Build failed for " + launchedProject.getName());
		}

		Assert.isTrue(!returnStatus.isOK()
				|| absoluteExecPath.toFile().exists());

		return returnStatus;
	}
};
