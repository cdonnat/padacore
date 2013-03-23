package org.padacore.core.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * This class defines a factory which provides the jobs needed for launching an
 * application.
 * 
 * @author RS
 * 
 */
public class ApplicationLauncherJobFactory {

	private IProject project;

	public ApplicationLauncherJobFactory(IProject project) {
		this.project = project;
	}

	/**
	 * Creates a Job in charge of running the given launch configuration.
	 * 
	 * @param absoluteExecutablePath
	 *            absolute path of the executable corresponding to launch
	 *            configuration.
	 * @param launchConfig
	 *            the launch configuration to run
	 * @return the Job to use for running the launch configuration.
	 */
	public Job createLaunchingJobFor(IPath absoluteExecutablePath,
			ILaunchConfiguration launchConfig) {
		return new LaunchingJob(absoluteExecutablePath, launchConfig);
	}

	/**
	 * Creates a Job in charge of building the project so that one of its
	 * executable can be run afterwards.
	 * 
	 * @param absoluteExecutablePath
	 *            absolute path of the executable that will be run afterwards.
	 * @return the Job to use for building the project.
	 */
	public Job createBuildingJobFor(IPath absoluteExecutablePath) {
		return new BuildingJob(absoluteExecutablePath, this.project);
	}

}
