package org.padacore.core;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.padacore.core.utils.FileUtils;

public class EclipseAdaProjectBuilder {

	private static final String[] NATURES = { AdaProjectNature.NATURE_ID };
	public static final String DEFAULT_EXECUTABLE_NAME = "main.adb";

	private AbstractAdaProjectAssociationManager adaProjectAssociationManager;

	public EclipseAdaProjectBuilder(
			AbstractAdaProjectAssociationManager adaProjectAssociationManager) {
		this.adaProjectAssociationManager = adaProjectAssociationManager;
	}

	/**
	 * Create a new Eclipse project with Ada nature at given location with given
	 * name. A main procedure is added if requested (addMainProcedure is set to
	 * true).
	 * 
	 * @param projectName
	 *            name of the Eclipse project to create.
	 * @param location
	 *            location of the Eclipse project to create.
	 * @param addMainProcedure
	 *            if true, a default generated main procedure is added to
	 *            project.
	 */
	public IProject createProjectWithAdaNatureAt(String projectName,
			IPath location, boolean addMainProcedure) {

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		this.addMainProcedureIfRequired(projectName, location, addMainProcedure);

		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace()
					.newProjectDescription(projectName);

			description.setLocation(location);
			project.create(description, null);
			project.open(null);
			this.addAdaNature(project);
			this.adaProjectAssociationManager.associateToAdaProject(project);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return project;
	}

	private void addMainProcedureIfRequired(String projectName, IPath location,
			boolean addMainProcedure) {

		if (addMainProcedure) {
			try {
				FileUtils.CreateNewFileWithContents(
						GetProjectPath(projectName, location)
								+ System.getProperty("file.separator")
								+ DEFAULT_EXECUTABLE_NAME,
						this.defaultMainContents());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Add the Ada nature to the given IProject.
	 * 
	 * @param project
	 *            Project to which the Ada nature shall be added.
	 * @throws CoreException
	 */
	private void addAdaNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		description.setNatureIds(NATURES);
		project.setDescription(description, null);
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
	public static String GetProjectPath(String projectName, IPath location) {
		if (location == null) {
			return ResourcesPlugin.getWorkspace().getRoot().getLocation()
					+ System.getProperty("file.separator") + projectName;
		} else {
			return location.toOSString();
		}
	}

	/**
	 * Return the default contents for the file containing the main procedure.
	 * 
	 * @return the default contents for main file as String.
	 */
	private String defaultMainContents() {
		StringBuilder mainContents = new StringBuilder();
		mainContents.append("with GNAT.IO;\n");
		mainContents.append("procedure Main is\n");
		mainContents.append("begin\n");
		mainContents.append("GNAT.IO.Put_Line(\"Hello world\");\n");
		mainContents.append("end Main;");

		return mainContents.toString();
	}
}
