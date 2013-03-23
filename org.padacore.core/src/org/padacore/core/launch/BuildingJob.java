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

/**
 * This class defines a job whose purpose is to build a project so that one of
 * its executables can be run afterwards.
 * 
 * @author RS
 * 
 */
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
			this.launchedProject.build(
					IncrementalProjectBuilder.INCREMENTAL_BUILD,
					"org.padacore.core.builder.AdaProjectBuilder", null,
					monitor);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
			returnStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Build failed for " + launchedProject.getName());
		}

		return returnStatus;
	}
};
