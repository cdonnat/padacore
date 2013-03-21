package org.padacore.core.launch;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.utils.ErrorLog;

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

	private class LaunchingJob extends Job {

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

	private class BuildingJob extends Job {
		private IProject launchedProject;

		public BuildingJob(IPath absoluteExecPath, IProject launchedProject)
				throws CoreException {
			super("Building before launching " + absoluteExecPath);

			this.launchedProject = launchedProject;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			IStatus returnStatus = Status.OK_STATUS;

			try {
				ResourcesPlugin.getWorkspace().build(
						launchedProject.getBuildConfigs(),
						IncrementalProjectBuilder.INCREMENTAL_BUILD, false,
						monitor);
			} catch (CoreException e) {
				ErrorLog.appendException(e);
				returnStatus = e.getStatus();
			}

			return returnStatus;
		}
	};

	public AdaApplicationLauncher(IAdaProject adaProject,
			ILaunchConfigurationProvider launchConfigProvider) {
		this.adaProject = adaProject;
		this.launchConfigProvider = launchConfigProvider;
	}

	@Override
	public void performLaunchFromFile(IFile file) {
		Assert.isLegal(this.adaProject.getExecutableNames().contains(
				file.getName())
				|| this.adaProject.getExecutableSourceNames().contains(
						file.getName()));

		boolean fileIsAnExecutable = this.adaProject.getExecutableNames()
				.contains(file.getName());
		IPath executablePath;

		if (fileIsAnExecutable) {
			executablePath = file.getLocation();
		} else {
			String matchingExecutableName = this
					.findExecutableNameCorrespondingToSource(file);

			executablePath = file.getLocation().removeLastSegments(1)
					.append(matchingExecutableName);
		}

		this.launchExecutableAtPath(executablePath, file.getProject());
	}

	private String findExecutableNameCorrespondingToSource(IFile file) {
		boolean matchingExecutableFound = false;
		List<String> executableNames = this.adaProject.getExecutableNames();
		int execIndex = 0;
		String executableNameWithoutExtension;

		while (!matchingExecutableFound && execIndex < executableNames.size()) {
			executableNameWithoutExtension = new Path(
					executableNames.get(execIndex)).removeFileExtension()
					.toString();

			if (file.getName().startsWith(executableNameWithoutExtension)) {
				matchingExecutableFound = true;
			} else {
				execIndex++;
			}
		}

		Assert.isTrue(matchingExecutableFound);
		return executableNames.get(execIndex);
	}

	@Override
	public void performLaunchFromProject(IProject project) {
		if (this.adaProject.getExecutableNames().size() == 1) {

			IPath executablePath = new Path(
					this.adaProject.getExecutableDirectoryPath()
							+ System.getProperty("file.separator")
							+ this.adaProject.getExecutableNames().get(0));

			this.launchExecutableAtPath(executablePath, project);

		} else {
			ErrorLog.appendMessage(
					"Launching a project with multiple executables is not implemented yet",
					IStatus.WARNING);
		}

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
	private void launchExecutableAtPath(final IPath absoluteExecPath,
			final IProject execProject) {
		ILaunchConfiguration configForFile = this.launchConfigProvider
				.getLaunchConfigurationFor(absoluteExecPath);
		final LaunchingJob launchingJob = new LaunchingJob(absoluteExecPath,
				configForFile);

		try {
			if (DoesExecutableExist(absoluteExecPath)) {
				launchingJob.schedule();
			} else {
				Job buildingJob = new BuildingJob(absoluteExecPath, execProject);
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

		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}
}
