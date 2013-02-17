package org.padacore.core.project;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.padacore.core.utils.ErrorLog;
import org.padacore.core.utils.FileUtils;

public class ProjectBuilder {

	private static final String[] NATURES = { AdaProjectNature.NATURE_ID };
	private static final String GNAT_PROJECT_EXTENSION = ".gpr";
	public static final String DEFAULT_EXECUTABLE_NAME = "main.adb";

	private AbstractAdaProjectAssociationManager adaProjectAssociationManager;

	public ProjectBuilder(AbstractAdaProjectAssociationManager adaProjectAssociationManager) {
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
	 * @return the created project.
	 */
	public IProject createProjectWithAdaNatureAt(String projectName, IPath location,
			boolean addMainProcedure) {

		return this.createProjectWithAdaNatureAt(projectName, location, addMainProcedure, null);
	}

	/**
	 * Create a new Eclipse project with Ada nature at given location with given
	 * name. A main procedure is added if requested (addMainProcedure is set to
	 * true). Link between a project folder and the GPR file is created if
	 * pathToLinkedGprProject is not null.
	 * 
	 * @param projectName
	 *            name of the Eclipse project to create.
	 * @param location
	 *            location of the Eclipse project to create.
	 * @param addMainProcedure
	 *            if true, a default generated main procedure is added to
	 *            project.
	 * @param pathToLinkedGprProject
	 *            the absolute path of the GPR file to which the project should
	 *            be linked.
	 * @return the created project.
	 */
	public IProject createProjectWithAdaNatureAt(String projectName, IPath location,
			boolean addMainProcedure, IPath pathToLinkedGprProject) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		this.addMainProcedureIfRequired(projectName, location, addMainProcedure);

		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
					projectName);

			description.setLocation(location);
			project.create(description, null);
			project.open(null);

			if (pathToLinkedGprProject != null) {
				this.linkProjectInWorkspaceTo(project, pathToLinkedGprProject);
			}

			this.addAdaNature(project);
			this.adaProjectAssociationManager.associateToAdaProject(project);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		return project;

	}

	/**
	 * Links the given project to the existing GPR project file (it creates both
	 * a direct link to the GPR file and a folder linked to the folder in which
	 * GPR file is contained).
	 * 
	 * @param project
	 *            the project to link to the GPR file.
	 * @param gprFileAbsolutePath
	 *            the absolute path of the GPR file.
	 */
	private void linkProjectInWorkspaceTo(IProject project, IPath gprFileAbsolutePath) {

		this.createAFolderLinkedToGpFileParentFolder(project, gprFileAbsolutePath);

		this.createALinkToGprFile(project, gprFileAbsolutePath);

	}

	/**
	 * Creates a shortcut to the GPR file at given path at the root of the given
	 * project
	 * 
	 * @param project
	 *            the project in which shortcut is created.
	 * @param gprFileAbsolutePath
	 *            the GPR file to which shortcut points.
	 */
	private void createALinkToGprFile(IProject project, IPath gprFileAbsolutePath) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IFile shortcutToGprFile = project.getFile(project.getName() + GNAT_PROJECT_EXTENSION);
		IStatus linkedGprFileStatus = workspace.validateLinkLocation(shortcutToGprFile,
				gprFileAbsolutePath);

		try {
			if (linkedGprFileStatus.isOK()
					|| linkedGprFileStatus.matches(IStatus.INFO | IStatus.WARNING)) {
				shortcutToGprFile.createLink(gprFileAbsolutePath, IResource.NONE, null);
			} else {
				ErrorLog.appendMessage("Invalid link for GPR file of " + project.getName()
						+ " project", IStatus.ERROR);
			}
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Creates a folder in project which points to the parent folder of the GPR
	 * file which path is given.
	 * 
	 * @param project
	 *            the project in which linked folder is created.
	 * @param gprFileAbsolutePath
	 *            path of the GPR file.
	 */
	private void createAFolderLinkedToGpFileParentFolder(IProject project, IPath gprFileAbsolutePath) {
		File gprProjectParentFolder = new File(gprFileAbsolutePath.toOSString()).getParentFile();
		IPath gprFileParentFolderAbsolutePath = new Path(gprProjectParentFolder.getAbsolutePath());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		// TODO use the same name as GPR parent folder for linked folder
		IFolder linkedFolder = project.getFolder("toto");

		IStatus linkedFolderStatus = workspace.validateLinkLocation(linkedFolder,
				gprFileParentFolderAbsolutePath);
		try {
			if (linkedFolderStatus.isOK()
					|| linkedFolderStatus.matches(IStatus.INFO | IStatus.WARNING)) {
				linkedFolder.createLink(gprFileParentFolderAbsolutePath, IResource.NONE, null);
			} else {
				ErrorLog.appendMessage("Invalid link for folder of " + project.getName()
						+ " project", IStatus.ERROR);
			}
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Adds a default main procedure to the procedure if required.
	 * 
	 * @param projectName
	 *            the name of the project for which the main procedure is
	 *            created.
	 * @param location
	 *            the location of the project.
	 * @param addMainProcedure
	 *            if true, the main procedure is added, nothing is done
	 *            otherwise.
	 */
	private void addMainProcedureIfRequired(String projectName, IPath location,
			boolean addMainProcedure) {

		IPath filePath = GetProjectPath(projectName, location).append(
				new Path(IPath.SEPARATOR + DEFAULT_EXECUTABLE_NAME));

		if (addMainProcedure) {
			try {
				FileUtils.CreateNewFileWithContents(filePath, this.defaultMainContents());
			} catch (IOException e) {
				ErrorLog.appendMessage(
						"Error while creating main procedure in " + filePath.toOSString(),
						IStatus.ERROR);
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
	public static IPath GetProjectPath(String projectName, IPath location) {
		if (location == null) {
			return new Path(ResourcesPlugin.getWorkspace().getRoot().getLocation()
					.toPortableString()
					+ IPath.SEPARATOR + projectName);
		} else {
			return location;
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
