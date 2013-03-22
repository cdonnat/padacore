package org.padacore.core.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;

public class LauncherFactory {

	private IProject project;

	public LauncherFactory(IProject project) {
		this.project = project;
	}

	public Job createLaunchingJobFor(IPath absoluteExecutablePath,
			ILaunchConfiguration launchConfig) {
		return new LaunchingJob(absoluteExecutablePath, launchConfig);
	}

	public Job createBuildingJobFor(IPath absoluteExecutablePath) {
		return new BuildingJob(absoluteExecutablePath, this.project);
	}

}
