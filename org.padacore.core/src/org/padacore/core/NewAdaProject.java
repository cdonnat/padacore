package org.padacore.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

public class NewAdaProject {

	public static final String GPR_PROJECT_SESSION_PROPERTY_QUALIFIER = "org.padacore";

	private static final String[] NATURES = { AdaProjectNature.NATURE_ID };
	private static final String DEFAULT_EXECUTABLE_NAME = "main.adb";

	/**
	 * Create and return an Eclipse Ada project. A default GPR project is
	 * created as well and associated to the IProject returned.
	 * 
	 * @param projectName
	 *            Name of the project to create.
	 * @param location
	 *            Location where the project should be create or null to create
	 *            the project in the workspace default location
	 * @param addMainProjedure
	 *            If true, a default main procedure will be added to the
	 *            project.
	 * @return The new created project.
	 * 
	 */
	public static IProject Create(String projectName, URI location, boolean addMainProcedure) {

		GprProject defaultGprProject = CreateDefaultGprProject(projectName,
				GetProjectPath(projectName, location), addMainProcedure);

		return CreateFrom(defaultGprProject, location);
	}

	/**
	 * Create a project from GPR project at the specified location.
	 * 
	 * @param gprProject
	 *            Base GPR project use to create the corresponding IProject
	 * @param location
	 *            Location of the GPR project.
	 * @return The IProject corresponding to the GPR project.
	 */
	public static IProject CreateFrom(GprProject gprProject, URI location) {

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(gprProject.getName());
		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
					gprProject.getName());

			description.setLocationURI(EvaluateProjectLocation(gprProject.getName(), location));
			project.create(description, null);
			project.open(null);
			AddAdaNature(project);
			AssociateIProjectToGprProject(project, gprProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return project;
	}

	/**
	 * Add the Ada nature to the given IProject.
	 * 
	 * @param project
	 *            Project to which the Ada nature shall be added.
	 * @throws CoreException
	 */
	private static void AddAdaNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		description.setNatureIds(NATURES);
		project.setDescription(description, null);
	}

	/**
	 * Associate a GPR project to an Ada project (IProject) using the
	 * SessionProjerty of the IProject class.
	 * 
	 * @param project
	 *            IProject project
	 * @param associatedGpr
	 *            GPR project
	 * @throws CoreException
	 */
	private static void AssociateIProjectToGprProject(IProject project, GprProject associatedGpr)
			throws CoreException {
		QualifiedName qualifiedName = new QualifiedName(GPR_PROJECT_SESSION_PROPERTY_QUALIFIER,
				project.getName());

		project.setSessionProperty(qualifiedName, associatedGpr);
	}

	private static URI EvaluateProjectLocation(String projectName, URI location) {
		URI res = location;

		if (location != null && !location.toString().contains(projectName)) {
			res = URI.create(location.getPath() + System.getProperty("file.separator")
					+ projectName);
		}
		return res;
	}

	/**
	 * Returns the project full path as a String.
	 * 
	 * @param projectName
	 *            Name of the project
	 * @param location
	 *            A location or null for the default project location.
	 * @return The project full path.
	 */
	private static String GetProjectPath(String projectName, URI location) {
		String path = (location == null) ? ResourcesPlugin.getWorkspace().getRoot()
				.getLocationURI().getPath() : location.getPath();

		return path + System.getProperty("file.separator") + projectName
				+ System.getProperty("file.separator");
	}

	/**
	 * Creates and returns a default GPR project.
	 * 
	 * @throws CoreException
	 * @throws IOException
	 */
	private static GprProject CreateDefaultGprProject(String projectName, String path,
			boolean addMainProcedure) {

		GprProject gprProject = new GprProject(projectName);

		new File(path).mkdir();

		if (addMainProcedure) {
			gprProject.setExecutable(true);
			gprProject.addExecutableName(DEFAULT_EXECUTABLE_NAME);
			AddFileToProject(path + DEFAULT_EXECUTABLE_NAME, DefaultMainContents());
		}
		AddFileToProject(path + projectName + ".gpr", gprProject.toString());

		return gprProject;
	}

	/**
	 * Add a file to the project with the specified content.
	 * 
	 * @param absolutefileName
	 *            Absolute file name of the file to add.
	 * @param content
	 *            Content of the file to add.
	 */
	private static void AddFileToProject(String absolutefileName, String content) {
		try {
			File gprFile = new File(absolutefileName);
			gprFile.createNewFile();
			FileWriter writer = new FileWriter(gprFile);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the default contents for the file containing the main procedure.
	 * 
	 * @return the default contents for main file as InputStream.
	 */
	private static String DefaultMainContents() {
		return "procedure Main is\n" + "begin\n" + "\tnull;\n" + "end Main;\n";
	}
}
