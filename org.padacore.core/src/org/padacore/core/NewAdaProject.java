package org.padacore.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

public class NewAdaProject {

	public static final String GPR_PROJECT_SESSION_PROPERTY_QUALIFIER = "org.padacore";

	private IProject project;
	private IProjectDescription description;

	private static final String[] NATURES = { AdaProjectNature.NATURE_ID };
	private static final String DEFAULT_EXECUTABLE_NAME = "main.adb";

	/**
	 * Create a new NewAdaProject instance.
	 * 
	 * @param project
	 *            Project handle on the project to create.
	 * @param location
	 *            Project location.
	 */
	public NewAdaProject(String projectName, URI location) {

		this.project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		this.description = project.getWorkspace().newProjectDescription(
				project.getName());
		this.description.setNatureIds(NATURES);
		this.description.setLocationURI(location);
	}

	/**
	 * Create and return an Eclipse Ada project or return existing one if it has
	 * already been created.
	 */
	public IProject create(boolean addMainProcedure) {

		boolean newProjectCreated = this.createEclipseProjectIfNotExisting();

		try {
			if (newProjectCreated) {
				GprProject defaultGprProject = this
						.createDefaultGprProject(addMainProcedure);
				this.associateGprProjectTo(defaultGprProject);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return this.project;
	}

	public IProject createFrom(GprProject gprProject) {
		boolean newProjectCreated = this.createEclipseProjectIfNotExisting();

		try {
			if (newProjectCreated) {
				this.associateGprProjectTo(gprProject);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return this.project;
	}

	/**
	 * Creates the associated Eclipse project if it does not already exist, does
	 * nothing otherwise.
	 * 
	 * @return True if project has been created, false otherwise.
	 */
	private boolean createEclipseProjectIfNotExisting() {
		boolean newProjectShallBeCreated = false;
		
		if (!this.project.exists()) {
			
			newProjectShallBeCreated = true;
			
			try {
				this.project.create(this.description, null);
				this.project.open(null);

			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return newProjectShallBeCreated;
	}

	private QualifiedName getGprPropertyQualifiedName() {
		return new QualifiedName(GPR_PROJECT_SESSION_PROPERTY_QUALIFIER,
				this.project.getName());
	}

	private void associateGprProjectTo(GprProject associatedGpr)
			throws CoreException {
		QualifiedName qualifiedName = this.getGprPropertyQualifiedName();

		this.project.setSessionProperty(qualifiedName, associatedGpr);
	}

	/**
	 * Return the default contents for the file containing the main procedure.
	 * 
	 * @return the default contents for main file as InputStream.
	 */
	private InputStream defaultMainContents() {
		final String content = "procedure Main is\n" + "begin\n" + "\tnull;\n"
				+ "end Main;\n";
		return new ByteArrayInputStream(content.getBytes());
	}

	/**
	 * Creates and returns a default GPR project.
	 * 
	 * @throws CoreException
	 */
	private GprProject createDefaultGprProject(boolean addMainProcedure)
			throws CoreException {
		GprProject gprProject = new GprProject(project.getName());
		IFile gprHandle = project.getFile(gprProject.fileName());

		if (addMainProcedure) {
			gprProject.setExecutable(true);
			gprProject.addExecutableName(DEFAULT_EXECUTABLE_NAME);

			this.createDefaultExecutable();
		}

		gprHandle.create(new ByteArrayInputStream(gprProject.toString()
				.getBytes()), false, null);

		return gprProject;
	}

	private void createDefaultExecutable() throws CoreException {
		IFile mainHandle = this.project.getFile(DEFAULT_EXECUTABLE_NAME);
		mainHandle.create(defaultMainContents(), false, null);
	}
}
