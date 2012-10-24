package org.padacore.core.test.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.GprProject;
import org.padacore.core.NewAdaProject;
import org.padacore.core.launch.AdaLaunchConstants;

public class CommonTestUtils {

	private static int cpt = 0;

	public static void CreateGprFileIn(IPath gprContainingFolder,
			String gprProjectName) {
		File gprFile = new File(gprContainingFolder
				+ System.getProperty("file.separator") + gprProjectName
				+ ".gpr");

		FileWriter gprWriter = null;
		try {

			gprWriter = new FileWriter(gprFile);

			gprWriter.write("project " + gprProjectName + " is " + "end "
					+ gprProjectName + ";");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (gprWriter != null) {
					gprWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static IProject CreateAdaProject() {
		cpt++;
		return CreateAdaProject("TestProject" + cpt);
	}

	public static IProject CreateAdaProject(String projectName) {
		return NewAdaProject.Create(projectName, null, false);
	}

	public static String GetWorkspaceAbsolutePath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocationURI()
				.getPath();
	}

	public static String GetFileAbsolutePath(IProject project, String filename) {
		String res = project.getWorkspace().getRoot().getRawLocation()
				.toOSString()
				+ project.getFullPath().toOSString()
				+ System.getProperty("file.separator") + filename;

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

	public static GprProject CheckAGprIsAssociatedToProject(
			IProject createdProject) {

		GprProject associatedGpr = null;

		try {
			associatedGpr = NewAdaProject
					.GetAssociatedGprProject(createdProject);

			assertTrue("GprProject shall be associated", associatedGpr != null);

		} catch (CoreException e) {
			e.printStackTrace();
		}

		return associatedGpr;
	}

	public static void CheckDefaultGprContents(GprProject gprToCheck,
			boolean mainProcedureHasBeenGenerated) {
		assertTrue("GprProject shall be executable: "
				+ mainProcedureHasBeenGenerated,
				gprToCheck.isExecutable() == mainProcedureHasBeenGenerated);
		assertTrue(
				"GprProject shall have "
						+ (mainProcedureHasBeenGenerated ? "1" : "0")
						+ " executable",
				gprToCheck.getExecutableSourceNames().size() == (mainProcedureHasBeenGenerated ? 1
						: 0));
		if (mainProcedureHasBeenGenerated) {
			assertTrue(
					"GprProject executable shall be called main.adb",
					gprToCheck.getExecutableSourceNames().get(0)
							.equals("main.adb"));
		}

	}

	public static ILaunchConfiguration[] RetrieveAdaLaunchConfigurations()
			throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();

		return launchManager
				.getLaunchConfigurations(GetAdaLaunchConfigurationType());
	}

	private static ILaunchConfigurationType GetAdaLaunchConfigurationType() {
		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();

		ILaunchConfigurationType adaConfigType = launchManager
				.getLaunchConfigurationType(AdaLaunchConstants.ID_LAUNCH_ADA_APP);

		return adaConfigType;
	}

	public static ILaunchConfiguration CreateAdaLaunchConfigurationFor(
			String launchConfigName, IProject project, String executableName)
			throws CoreException {

		ILaunchConfigurationType adaConfigType = GetAdaLaunchConfigurationType();

		ILaunchConfiguration launchConfig = null;

		ILaunchConfigurationWorkingCopy configWc = adaConfigType.newInstance(
				null, launchConfigName);

		String fileAbsolutePath = GetFileAbsolutePath(project, executableName);

		configWc.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH,
				fileAbsolutePath);

		launchConfig = configWc.doSave();

		return launchConfig;
	}

}
