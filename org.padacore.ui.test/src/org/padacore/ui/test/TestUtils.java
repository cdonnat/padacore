package org.padacore.ui.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.launch.AdaLaunchConstants;
import org.padacore.ui.wizards.NewAdaProject;

public class TestUtils {

	public static IProject createAdaProject() {
		return createAdaProject("Test project");
	}

	public static IProject createAdaProject(String projectName) {
		NewAdaProject adaProject = new NewAdaProject(projectName, true, null);

		return adaProject.create(false);
	}
	
	private static ILaunchConfigurationType getAdaLaunchConfigurationType() {
		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();
		
		ILaunchConfigurationType adaConfigType = launchManager
				.getLaunchConfigurationType(AdaLaunchConstants.ID_LAUNCH_ADA_APP);
		
		return adaConfigType;
	}
	
	public static ILaunchConfiguration[] retrieveAdaLaunchConfigurations() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();
		
		return launchManager.getLaunchConfigurations(getAdaLaunchConfigurationType());		
	}
	
	public static ILaunchConfiguration createAdaLaunchConfigurationFor(
			String launchConfigName, IProject project, String executableName)
			throws CoreException {

		ILaunchConfigurationType adaConfigType = getAdaLaunchConfigurationType();

		ILaunchConfiguration launchConfig = null;

		ILaunchConfigurationWorkingCopy configWc = adaConfigType.newInstance(
				null, launchConfigName);

		String fileAbsolutePath = getFileAbsolutePath(project, executableName);

		configWc.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH,
				fileAbsolutePath);

		launchConfig = configWc.doSave();

		return launchConfig;
	}

	public static String getFileAbsolutePath(IProject project, String filename) {
		String workspaceAbsolutePath = project.getWorkspace().getRoot()
				.getRawLocation().toOSString();

		return workspaceAbsolutePath + project.getFullPath().toOSString()
				+ System.getProperty("file.separator") + filename;
	}

}
