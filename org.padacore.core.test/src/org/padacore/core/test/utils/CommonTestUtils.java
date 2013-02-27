package org.padacore.core.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.GprProject;
import org.padacore.core.launch.AdaLaunchConstants;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.utils.ErrorLog;

public class CommonTestUtils {

	public static String SESSION_PROPERTY_QUALIFIED_NAME_PREFIX = "org.padacore";

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

	public static IProject CreateNonAdaProject(boolean openProject) {
		cpt++;
		return CreateNonAdaProject("TestProject" + cpt, openProject);
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

	public static GprProject CreateGprProject(String name, IPath rootDir,
			boolean isExecutable) {
		GprProject result = new GprProject(name, rootDir);

		result.setExecutable(isExecutable);

		return result;
	}

	public static GprProject CreateGprProject(String name, boolean isExecutable) {
		return CreateGprProject(name, ResourcesPlugin.getWorkspace().getRoot()
				.getLocation(), isExecutable);
	}

	public static IProject CreateAdaProject() {
		return CreateAdaProject(true, false);
	}

	public static IProject CreateAdaProject(boolean openProject,
			boolean isExecutable) {
		return CreateAdaProjectAt(null, openProject, isExecutable);
	}

	public static IProject CreateAdaProjectAt(IPath location) {
		return CreateAdaProjectAt(location, true, false);
	}

	public static IProject CreateAdaProjectAt(IPath location,
			boolean openProject, boolean isExecutable) {
		cpt++;
		return CreateAdaProjectAt(location, "TestProject" + cpt, openProject,
				isExecutable);
	}

	public static IProject CreateAdaProjectAt(IPath location,
			String projectName, boolean openProject, boolean isExecutable) {
		IProject eclipseProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		IAdaProject adaProject = new GnatAdaProject(CreateGprProject(
				eclipseProject.getName(), location, isExecutable));

		FileWriter filewriter = null;

		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace()
					.newProjectDescription(projectName);

			description.setLocation(location);
			description
					.setNatureIds(new String[] { AdaProjectNature.NATURE_ID });
			eclipseProject.create(description, null);

			GprProject gpr = new GprProject(projectName,
					eclipseProject.getRawLocation());
			filewriter = new FileWriter(new File(eclipseProject.getLocation()
					.toOSString() + IPath.SEPARATOR + projectName + ".gpr"));
			filewriter.write(gpr.toString());
			filewriter.close();

			eclipseProject.open(null);

			eclipseProject.setSessionProperty(new QualifiedName(
					SESSION_PROPERTY_QUALIFIED_NAME_PREFIX, "adaProject"),
					adaProject);

			if (!openProject) {
				eclipseProject.close(null);
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

		return eclipseProject;
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
			sessionProperty = createdProject
					.getSessionProperty(new QualifiedName(
							SESSION_PROPERTY_QUALIFIED_NAME_PREFIX,
							"adaProject"));
		} catch (CoreException e) {
			e.printStackTrace();
		}

		assertTrue("GprProject shall be associated",
				sessionProperty != null == shallBeAssociated);

		return (IAdaProject) sessionProperty;
	}

	public static void RemoveAssociationToAdaProject(IProject project) {
		try {
			project.setSessionProperty(new QualifiedName(
					SESSION_PROPERTY_QUALIFIED_NAME_PREFIX, "adaProject"), null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static void CheckDefaultAdaProjectContents(IAdaProject adaProject,
			boolean mainProcedureHasBeenGenerated) {
		assertTrue("AdaProject shall be executable: "
				+ mainProcedureHasBeenGenerated,
				adaProject.isExecutable() == mainProcedureHasBeenGenerated);
		assertTrue(
				"AdaProject shall have "
						+ (mainProcedureHasBeenGenerated ? "1" : "0")
						+ " executable",
				adaProject.getExecutableNames().size() == (mainProcedureHasBeenGenerated ? 1
						: 0));
		if (mainProcedureHasBeenGenerated) {
			assertTrue("AdaProject executable shall be called main.adb",
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

	public static IPath GetPathToTestProject() {
		return new Path(System.getProperty("user.dir")
				+ "/src/org/padacore/core/gnat/test/gpr/");
	}

	public final static String IMPORTED_PROJECT = "imported";
	public final static String CREATED_PROJECT = "created";

	private static Object GetSessionPropertyName(IProject project, String name) {
		Object property = null;
		try {
			property = project.getSessionProperty(new QualifiedName(
					"org.padacore", name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
		return property;
	}

	private static Object GetPersistentPropertyName(IProject project,
			String name) {
		Object property = null;
		try {
			property = project.getPersistentProperty(new QualifiedName(
					"org.padacore", name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
		return property;
	}

	public static void CheckAdaProjectIsSetInProperties(IProject project,
			IAdaProject expectedAdaProject) {
		assertEquals("AdaProject should be set", expectedAdaProject,
				GetSessionPropertyName(project, "adaProject"));
	}

	public static void CheckProjectKindIsSetInProperties(IProject project,
			String expectedKind) {
		assertEquals("Project kind should be set", expectedKind,
				GetPersistentPropertyName(project, "projectKind"));
	}
}
