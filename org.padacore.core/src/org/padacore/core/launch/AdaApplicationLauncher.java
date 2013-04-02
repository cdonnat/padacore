package org.padacore.core.launch;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.padacore.core.project.IAdaProject;

/**
 * This class provides various methods for launching Ada application (from a
 * file, a project...).
 * 
 * @author RS
 * 
 */
public class AdaApplicationLauncher implements IApplicationLauncher {

	private IAdaProject adaProject;
	private ILaunchConfigurationProvider launchConfigProvider;
	private ApplicationLauncherJobFactory factory;

	public AdaApplicationLauncher(IAdaProject adaProject,
			ILaunchConfigurationProvider launchConfigProvider,
			ApplicationLauncherJobFactory factory) {
		this.adaProject = adaProject;
		this.launchConfigProvider = launchConfigProvider;
		this.factory = factory;
	}

	@Override
	public void performLaunchFromFile(IPath filePath) {
		Assert.isLegal(this.adaProject.getExecutableNames().contains(
				filePath.lastSegment())
				|| this.adaProject.getExecutableSourceNames().contains(
						filePath.lastSegment()));

		boolean fileIsAnExecutable = this.adaProject.getExecutableNames()
				.contains(filePath.lastSegment());
		IPath executablePath;

		if (fileIsAnExecutable) {
			executablePath = filePath;
		} else {
			String matchingExecutableName = this
					.findExecutableNameCorrespondingToSource(filePath);

			executablePath = filePath.removeLastSegments(1).append(
					matchingExecutableName);
		}

		this.launchExecutableAtPath(executablePath);
	}

	private String findExecutableNameCorrespondingToSource(IPath filePath) {
		boolean matchingExecutableFound = false;
		List<String> executableNames = this.adaProject.getExecutableNames();
		int execIndex = 0;
		String executableNameWithoutExtension;

		while (!matchingExecutableFound && execIndex < executableNames.size()) {
			executableNameWithoutExtension = new Path(
					executableNames.get(execIndex)).removeFileExtension()
					.toString();

			if (filePath.lastSegment().startsWith(
					executableNameWithoutExtension)) {
				matchingExecutableFound = true;
			} else {
				execIndex++;
			}
		}

		Assert.isTrue(matchingExecutableFound);
		return executableNames.get(execIndex);
	}

	/**
	 * Returns true if the executable at given path exists, false otherwise.
	 * 
	 * @return true if the executable designed by given path exists, false
	 *         otherwise
	 */
	private static boolean DoesExecutableExist(IPath absoluteExecPath) {
		return absoluteExecPath.toFile().exists();
	}

	/**
	 * Launches the executable found at given absolute path.
	 * 
	 * @param absoluteExecPath
	 *            absolute path of the executable to launch.
	 */
	private void launchExecutableAtPath(final IPath absoluteExecPath) {
		ILaunchConfiguration configForFile = this.launchConfigProvider
				.getLaunchConfigurationFor(absoluteExecPath);
		final Job launchingJob = this.factory.createLaunchingJobFor(
				absoluteExecPath, configForFile);

		if (DoesExecutableExist(absoluteExecPath)) {
			launchingJob.schedule();
		} else {
			Job buildingJob = this.factory
					.createBuildingJobFor(absoluteExecPath);
			buildingJob.schedule();

			buildingJob.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					if (DoesExecutableExist(absoluteExecPath)) {
						launchingJob.schedule();
					}
				}
			});

		}
	}
}
