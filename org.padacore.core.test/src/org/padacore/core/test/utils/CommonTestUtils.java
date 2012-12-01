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
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.IAdaProject;
import org.padacore.core.gnat.GprProject;
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

	public static IProject CreateNonAdaProject(String projectName,
			boolean openProject) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		try {
			project.create(null);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		if (openProject) {
			try {
				project.open(null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return project;
	}

	public static IProject CreateAdaProject() {
		cpt++;
		return CreateAdaProject("TestProject" + cpt);
	}

	public static IProject CreateAdaProject(String projectName) {
		return CreateAdaProject(projectName, true);
	}

	public static IProject CreateAdaProject(String projectName,
			boolean openProject) {

		IProject adaProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		FileWriter filewriter = null;

		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace()
					.newProjectDescription(projectName);
			
			description.setLocation(null);
			adaProject.create(description, null);
			
			GprProject gpr = new GprProject(projectName);
			filewriter = new FileWriter(new File(adaProject.getLocation()
					.toOSString() + IPath.SEPARATOR + projectName + ".gpr"));
			filewriter.write(gpr.toString());
			filewriter.close();
			
			adaProject.open(null);

			description
					.setNatureIds(new String[] { AdaProjectNature.NATURE_ID });
			adaProject.setDescription(description, null);

			if (!openProject) {
				adaProject.close(null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (filewriter != null) {
				try {
					filewriter.close();
				} catch (IOException e) {

				}
			}

		}

		return adaProject;
	}

	public static IProject CreateAndOpenNonAdaProject(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.newProjectDescription(projectName);
		try {
			project.create(description, null);
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return project;

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

	public static IAdaProject CheckAdaProjectAssociationToProject(
			IProject createdProject, boolean shallBeAssociated) {

		Object sessionProperty = null;
		try {
			 sessionProperty = createdProject.getSessionProperty(new QualifiedName("org.padacore", createdProject.getName()));
		} catch (CoreException e) {
			e.printStackTrace();
		} 
				
		assertTrue("GprProject shall be associated",
				sessionProperty != null == shallBeAssociated);

		return (IAdaProject)sessionProperty;
	}

	public static void RemoveAssociationToAdaProject(IProject project) {
		try {
			project.setSessionProperty(new QualifiedName("org.padacore",
					project.getName()), null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static void CheckDefaultAdaProjectContents(IAdaProject adaProject,
			boolean mainProcedureHasBeenGenerated) {
		assertTrue("GprProject shall be executable: "
				+ mainProcedureHasBeenGenerated,
				adaProject.isExecutable() == mainProcedureHasBeenGenerated);
		assertTrue(
				"GprProject shall have "
						+ (mainProcedureHasBeenGenerated ? "1" : "0")
						+ " executable",
				adaProject.getExecutableNames().size() == (mainProcedureHasBeenGenerated ? 1
						: 0));
		if (mainProcedureHasBeenGenerated) {
			assertTrue("GprProject executable shall be called main.adb",
					adaProject.getExecutableNames().get(0).equals("main.adb"));
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

	public static String GetPathToTestProject() {
		return System.getProperty("user.dir")
				+ "/src/org/padacore/core/gnat/test/";
	}
}
