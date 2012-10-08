package org.padacore.ui.test.utils;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.GprProject;
import org.padacore.core.NewAdaProject;
import org.padacore.core.launch.AdaLaunchConstants;

public class TestUtils {

	private static int cpt = 0;

	public static IProject createAdaProject() {
		cpt++;
		return createAdaProject("TestProject" + cpt);
	}

	public static IProject createAdaProject(String projectName) {
		return NewAdaProject.Create(projectName, null, false);
	}

	private static ILaunchConfigurationType getAdaLaunchConfigurationType() {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

		ILaunchConfigurationType adaConfigType = launchManager
				.getLaunchConfigurationType(AdaLaunchConstants.ID_LAUNCH_ADA_APP);

		return adaConfigType;
	}

	public static ILaunchConfiguration[] retrieveAdaLaunchConfigurations() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

		return launchManager.getLaunchConfigurations(getAdaLaunchConfigurationType());
	}

	public static ILaunchConfiguration createAdaLaunchConfigurationFor(String launchConfigName,
			IProject project, String executableName) throws CoreException {

		ILaunchConfigurationType adaConfigType = getAdaLaunchConfigurationType();

		ILaunchConfiguration launchConfig = null;

		ILaunchConfigurationWorkingCopy configWc = adaConfigType
				.newInstance(null, launchConfigName);

		String fileAbsolutePath = getFileAbsolutePath(project, executableName);

		configWc.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH, fileAbsolutePath);

		launchConfig = configWc.doSave();

		return launchConfig;
	}

	public static String getWorkspaceAbsolutePath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocationURI().getPath();
	}

	public static String getFileAbsolutePath(IProject project, String filename) {
		String res = project.getWorkspace().getRoot().getRawLocation().toOSString()
				+ project.getFullPath().toOSString() + System.getProperty("file.separator")
				+ filename;

		try {
			IProjectDescription desc = project.getDescription();
			if (desc.getLocationURI() != null) {
				return desc.getLocationURI().getPath();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static GprProject checkAGprIsAssociatedToProject(IProject createdProject) {

		GprProject associatedGpr = null;

		try {
			Object associatedProperty = createdProject
					.getSessionProperty(new QualifiedName(
							NewAdaProject.GPR_PROJECT_SESSION_PROPERTY_QUALIFIER, createdProject
									.getName()));

			assertTrue("GprProject shall be associated", associatedProperty instanceof GprProject);

			if (associatedProperty instanceof GprProject) {
				associatedGpr = (GprProject) associatedProperty;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return associatedGpr;
	}

	public static void checkDefaultGprContents(GprProject gprToCheck,
			boolean mainProcedureHasBeenGenerated) {
		assertTrue("GprProject shall be executable: " + mainProcedureHasBeenGenerated,
				gprToCheck.isExecutable() == mainProcedureHasBeenGenerated);
		assertTrue("GprProject shall have " + (mainProcedureHasBeenGenerated ? "1" : "0")
				+ " executable",
				gprToCheck.getExecutableSourceNames().size() == (mainProcedureHasBeenGenerated ? 1
						: 0));
		if (mainProcedureHasBeenGenerated) {
			assertTrue("GprProject executable shall be called main.adb", gprToCheck
					.getExecutableSourceNames().get(0).equals("main.adb"));
		}

	}

}
