package org.padacore.core.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.gpr4j.core.Gpr;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.launch.AdaLaunchConstants;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.utils.ErrorLog;

public class CommonTestUtils {

	public static String SESSION_PROPERTY_QUALIFIED_NAME_PREFIX = "org.padacore";

	private static int cpt = 0;

	public static void CreateGprFileIn(IPath gprContainingFolder, String gprProjectName) {
		File gprFile = new File(gprContainingFolder + System.getProperty("file.separator")
				+ gprProjectName + ".gpr");

		FileWriter gprWriter = null;
		try {

			gprWriter = new FileWriter(gprFile);

			gprWriter.write("project " + gprProjectName + " is " + "end " + gprProjectName + ";");
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

	public static IPath GetExecutablePathFor(IProject project, String executableSourceName) {
		String executableName = GetExecutableNameFromExecSourceName(executableSourceName);

		return project.getFile(executableName).getLocation();
	}

	public static IProject CreateNonAdaProject(boolean openProject) {
		cpt++;
		return CreateNonAdaProject("TestProject" + cpt, openProject);
	}

	public static IProject CreateNonAdaProject(String projectName, boolean openProject) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
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
	
	public static Gpr CreateGprProject(String name, IPath rootDir, boolean isExecutable,
			String[] executableNames) {
		Gpr result = new Gpr(name, Paths.get(rootDir.toOSString()));

		result.setExecutable(isExecutable);

		if (isExecutable) {
			for (int exec = 0; exec < executableNames.length; exec++) {
				result.addSourceExecutableName(executableNames[exec]);
			}
		}

		return result;
	}

	public static Gpr CreateGprProject(String name, boolean isExecutable,
			String[] executableNames) {
		return CreateGprProject(name, ResourcesPlugin.getWorkspace().getRoot().getLocation(),
				isExecutable, executableNames);
	}

	public static IProject CreateAdaProject() {
		return CreateAdaProject(true, false, null);
	}

	public static IProject CreateAdaProject(boolean openProject, boolean isExecutable,
			String[] executableNames) {
		return CreateAdaProject(openProject, isExecutable, executableNames, false);
	}

	public static IProject CreateAdaProject(boolean openProject, boolean isExecutable,
			String[] executableNames, boolean preventCorrectBuild) {
		return CreateAdaProjectAt(null, openProject, isExecutable, executableNames,
				preventCorrectBuild);
	}

	public static IProject CreateAdaProject(boolean openProject) {
		return CreateAdaProject(openProject, false, null);
	}

	public static IProject CreateAdaProjectAt(IPath location) {
		return CreateAdaProjectAt(location, true, false, null, false);
	}

	public static IProject CreateAdaProjectAt(IPath location, boolean openProject,
			boolean isExecutable, String[] executableNames, boolean preventCorrectBuild) {
		cpt++;
		return CreateAdaProjectAt(location, "TestProject" + cpt, openProject, isExecutable,
				executableNames, preventCorrectBuild);
	}

	private static void CreateFakeExecutableSource(IProject project, String executableSourceName,
			boolean preventCorrectCompiling) {
		FileWriter filewriter = null;
		String sourceContents;

		if (preventCorrectCompiling) {
			sourceContents = "failed";
		} else {
			sourceContents = "procedure " + new Path(executableSourceName).removeFileExtension()
					+ " is begin null; end;";
		}

		try {
			filewriter = new FileWriter(new File(project.getLocation().toOSString()
					+ IPath.SEPARATOR + executableSourceName));
			filewriter.write(sourceContents);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (filewriter != null) {
					filewriter.close();
				}
			} catch (IOException e) {

			}
		}
	}

	public static void CreateFileIfNotExisting(IFile fileHandle) {
		if (!fileHandle.exists()) {
			try {
				fileHandle.create(new ByteArrayInputStream(new byte[] { 'e' }), false, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private static void DumpGprToFile(IProject project, Gpr gprProject) {
		FileWriter filewriter = null;
		try {
			filewriter = new FileWriter(new File(project.getLocation().toOSString()
					+ IPath.SEPARATOR + project.getName() + ".gpr"));
			filewriter.write(gprProject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (filewriter != null) {
					filewriter.close();
				}
			} catch (IOException e) {

			}
		}

	}

	public static void ScheduleJobAndWaitForIt(Job job) {
		job.schedule();
		try {
			job.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static IProject CreateAdaProjectAt(IPath location, String projectName,
			boolean openProject, boolean isExecutable, String[] executableNames,
			boolean preventCorrectBuild) {
		IProject eclipseProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
					projectName);

			description.setLocation(location);
			description.setNatureIds(new String[] { AdaProjectNature.NATURE_ID });
			eclipseProject.create(description, null);

			Gpr gpr = CreateGprProject(projectName, isExecutable, executableNames);
			DumpGprToFile(eclipseProject, gpr);

			if (executableNames != null) {
				for (int execIdx = 0; execIdx < executableNames.length; execIdx++) {
					CreateFakeExecutableSource(eclipseProject, executableNames[execIdx],
							preventCorrectBuild);
				}
			}

			eclipseProject.open(null);
			eclipseProject.setDescription(description, null);

			IAdaProject adaProject = new GnatAdaProject(CreateGprProject(eclipseProject.getName(),
					eclipseProject.getLocation(), isExecutable, executableNames));

			eclipseProject.setSessionProperty(new QualifiedName(
					SESSION_PROPERTY_QUALIFIED_NAME_PREFIX, "adaProject"), adaProject);
			eclipseProject.setPersistentProperty(new QualifiedName(
					SESSION_PROPERTY_QUALIFIED_NAME_PREFIX, "projectKind"), "created");

			if (!openProject) {
				eclipseProject.close(null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return eclipseProject;
	}

	public static String GetWorkspaceAbsolutePath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocationURI().getPath();
	}

	public static String GetFileAbsolutePath(IProject project, String filename) {
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

	public static IAdaProject CheckAdaProjectAssociationToProject(IProject createdProject,
			boolean shallBeAssociated) {

		Object sessionProperty = null;
		try {
			sessionProperty = createdProject.getSessionProperty(new QualifiedName(
					SESSION_PROPERTY_QUALIFIED_NAME_PREFIX, "adaProject"));
		} catch (CoreException e) {
			e.printStackTrace();
		}

		assertTrue("GprProject shall be associated", sessionProperty != null == shallBeAssociated);

		return (IAdaProject) sessionProperty;
	}
	
	public static void RemoveAssociationToAdaProject(IProject project) {
		SetAssociatedAdaProject(project, null);
	}

	public static void SetAssociatedAdaProject(IProject project, IAdaProject adaProject) {
		try {
			project.setSessionProperty(new QualifiedName(SESSION_PROPERTY_QUALIFIED_NAME_PREFIX,
					"adaProject"), adaProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static void CheckDefaultAdaProjectContents(IAdaProject adaProject,
			boolean mainProcedureHasBeenGenerated) {
		assertTrue("AdaProject shall be executable: " + mainProcedureHasBeenGenerated,
				adaProject.isExecutable() == mainProcedureHasBeenGenerated);
		assertTrue("AdaProject shall have " + (mainProcedureHasBeenGenerated ? "1" : "0")
				+ " executable",
				adaProject.getExecutableNames().size() == (mainProcedureHasBeenGenerated ? 1 : 0));
		if (mainProcedureHasBeenGenerated) {
			assertTrue("AdaProject executable shall be called main.adb", adaProject
					.getExecutableNames().get(0).equals("main.adb"));
		}

	}

	public static ILaunchConfiguration[] RetrieveAdaLaunchConfigurations() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

		return launchManager.getLaunchConfigurations(GetAdaLaunchConfigurationType());
	}

	private static ILaunchConfigurationType GetAdaLaunchConfigurationType() {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

		ILaunchConfigurationType adaConfigType = launchManager
				.getLaunchConfigurationType(AdaLaunchConstants.ID_LAUNCH_ADA_APP);

		return adaConfigType;
	}

	public static String GetExecutableNameFromExecSourceName(String execSourceName) {
		boolean osIsWindows = System.getProperty("os.name").contains("win")
				|| System.getProperty("os.name").contains("Win");
		String rootExecName = new Path(execSourceName).removeFileExtension().toString();

		return osIsWindows ? rootExecName + ".exe" : rootExecName;
	}

	public static ILaunchConfiguration CreateAdaLaunchConfigurationFor(String launchConfigName,
			IProject project, String executableName) throws CoreException {

		ILaunchConfigurationType adaConfigType = GetAdaLaunchConfigurationType();

		ILaunchConfiguration launchConfig = null;

		ILaunchConfigurationWorkingCopy configWc = adaConfigType
				.newInstance(null, launchConfigName);

		String fileAbsolutePath = GetFileAbsolutePath(project, executableName);

		configWc.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH, fileAbsolutePath);

		launchConfig = configWc.doSave();

		return launchConfig;
	}

	public static IPath GetPathToTestProject() {
		return new Path(System.getProperty("user.dir") + "/src/org/padacore/core/gnat/test/gpr/");
	}

	public final static String IMPORTED_PROJECT = "imported";
	public final static String CREATED_PROJECT = "created";

	private static Object GetSessionPropertyNamed(IProject project, String name) {
		Object property = null;
		try {
			property = project.getSessionProperty(new QualifiedName("org.padacore", name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
		return property;
	}

	private static Object GetPersistentPropertyNamed(IProject project, String name) {
		Object property = null;
		try {
			property = project.getPersistentProperty(new QualifiedName("org.padacore", name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
		return property;
	}

	public static void CheckAdaProjectIsSetInProperties(IProject project,
			IAdaProject expectedAdaProject) {
		assertEquals("AdaProject should be set", expectedAdaProject,
				GetSessionPropertyNamed(project, "adaProject"));
	}

	public static void CheckProjectKindIsSetInProperties(IProject project, String expectedKind) {
		assertEquals("Project kind should be set", expectedKind,
				GetPersistentPropertyNamed(project, "projectKind"));
	}
}
