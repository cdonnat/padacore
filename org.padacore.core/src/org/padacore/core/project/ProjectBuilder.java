package org.padacore.core.project;

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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.padacore.core.project.PropertiesManager.ProjectKind;
import org.padacore.core.utils.ErrorLog;
import org.padacore.core.utils.FileUtils;

public class ProjectBuilder {

	private static final String[] NATURES = { AdaProjectNature.NATURE_ID };
	public static final String DEFAULT_EXECUTABLE_NAME = "main.adb";

	private String projectName;
	private IProject eclipseProject;

	/**
	 * Default constructor.
	 * 
	 * @param projectName
	 *            Name of the project to build.
	 */
	public ProjectBuilder(String projectName) {
		this.projectName = projectName;
		this.eclipseProject = GetEclipseProject(projectName);
	}

	/**
	 * Create a new eclipse project from an Ada project.
	 * 
	 * @param adaProject
	 *            Ada project
	 * @param location
	 *            Location of the eclipse project.
	 * @param addMainProcedure
	 *            True if a default main procedure must be created.
	 */
	public void createNewProject(IAdaProject adaProject, IPath location, boolean addMainProcedure) {
		this.createAndOpen(location);
		this.addAdaNature();
		this.setProjectProperties(adaProject, ProjectKind.CREATED);
		if (addMainProcedure) {
			this.addMainProcedure(location);
		}
		this.refreshProject();
	}

	/**
	 * Create and open an eclipse project at the given location.
	 * 
	 * @param location
	 *            Location of the eclipse project.
	 */
	private void createAndOpen(IPath location) {
		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
					this.projectName);

			description.setLocation(location);
			this.eclipseProject.create(description, null);
			this.eclipseProject.open(null);
		} catch (CoreException e) {
			ErrorLog.appendMessage("Error while creating the project " + this.projectName,
					IStatus.ERROR);
		}
	}

	/**
	 * Add the Ada nature to the eclipse project.
	 * 
	 */
	private void addAdaNature() {
		try {
			IProjectDescription description = this.eclipseProject.getDescription();
			description.setNatureIds(NATURES);
			this.eclipseProject.setDescription(description, null);
		} catch (CoreException e) {
			ErrorLog.appendMessage("Error while adding Ada project nature" + this.projectName,
					IStatus.ERROR);
		}
	}

	/**
	 * Set the session and persistent project properties.
	 * 
	 * @param adaProject
	 *            Ada project corresponding to the eclipse project to build.
	 * @param kind
	 *            Kind of the eclipse project.
	 */
	private void setProjectProperties(IAdaProject adaProject, ProjectKind kind) {
		PropertiesManager propertiesManager = new PropertiesManager(this.eclipseProject);
		propertiesManager.setAdaProject(adaProject);
		propertiesManager.setProjectKind(kind);
	}

	/**
	 * Add a default main procedure to the eclipse project.
	 * 
	 * @param location
	 *            Location of the eclipse project
	 */
	private void addMainProcedure(IPath location) {
		IPath filePath = GetProjectPath(this.projectName, location).addTrailingSeparator().append(
				DEFAULT_EXECUTABLE_NAME);
		try {
			FileUtils.CreateNewFileWithContents(filePath, this.defaultMainContents());
		} catch (IOException e) {
			ErrorLog.appendMessage(
					"Error while creating main procedure in " + filePath.toOSString(),
					IStatus.ERROR);
		}
	}

	/**
	 * Refresh the content of the eclipse project.
	 */
	private void refreshProject() {
		try {
			this.eclipseProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Return the eclipse project corresponding to the given name.
	 * 
	 * @param name
	 *            Name of the project.
	 * @return Eclipse project corresponding.
	 */
	private static IProject GetEclipseProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	/**
	 * Import an Ada project into an eclipse project.
	 * 
	 * @param importedProjectFilePath
	 *            Location of the Ada project file.
	 * @param adaProject
	 *            Ada project corresponding.
	 */
	public void importProject(IPath importedProjectFilePath, IAdaProject adaProject) {
		this.createAndOpen(null);
		this.addAdaNature();
		this.setProjectProperties(adaProject, ProjectKind.IMPORTED);
		this.createLinks(importedProjectFilePath);
	}

	/**
	 * Create the links on the project to import into the workspace.
	 * 
	 * @param importedProjectFilePath
	 *            Path to the Ada project to import.
	 */
	private void createLinks(IPath importedProjectFilePath) {
		this.createLinkToProjectFileParentFolder(importedProjectFilePath);
		this.createLinkToProjectFile(importedProjectFilePath);
	}

	/**
	 * Create a link to the folder containing the Ada project.
	 * 
	 * @param importedProjectFilePath
	 *            Path to the Ada project file.
	 */
	private void createLinkToProjectFileParentFolder(IPath importedProjectFilePath) {
		IPath projectFileParentFolderPath = importedProjectFilePath.removeLastSegments(1);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		// TODO use the same name as GPR parent folder for linked folder
		IFolder linkedFolder = this.eclipseProject.getFolder("toto");

		IStatus linkedFolderStatus = workspace.validateLinkLocation(linkedFolder,
				projectFileParentFolderPath);
		try {
			if (linkedFolderStatus.isOK()
					|| linkedFolderStatus.matches(IStatus.INFO | IStatus.WARNING)) {
				linkedFolder.createLink(projectFileParentFolderPath, IResource.NONE, null);
			} else {
				ErrorLog.appendMessage("Invalid link for folder of " + this.projectName
						+ " project", IStatus.ERROR);
			}
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Create a link to the Ada project file.
	 * 
	 * @param importedProjectFilePath
	 *            Path to the Ada project file.
	 */
	private void createLinkToProjectFile(IPath importedProjectFilePath) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IFile shortcutToProjectFile = this.eclipseProject.getFile(importedProjectFilePath
				.lastSegment());
		IStatus linkedGprFileStatus = workspace.validateLinkLocation(shortcutToProjectFile,
				importedProjectFilePath);

		try {
			if (linkedGprFileStatus.isOK()
					|| linkedGprFileStatus.matches(IStatus.INFO | IStatus.WARNING)) {
				shortcutToProjectFile.createLink(importedProjectFilePath, IResource.NONE, null);
			} else {
				ErrorLog.appendMessage("Invalid link for GPR file of " + this.projectName
						+ " project", IStatus.ERROR);
			}
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
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
			return ResourcesPlugin.getWorkspace().getRoot().getLocation().addTrailingSeparator()
					.append(projectName);
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
