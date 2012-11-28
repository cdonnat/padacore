package org.padacore.core.gnat;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.padacore.core.ProjectBuilder;
import org.padacore.core.utils.FileUtils;

/**
 * This class allows to create a default GPR project.
 * 
 * @author RS
 * 
 */
public class DefaultGprProjectFactory extends AbstractGprProjectFactory {

	private boolean addMainProcedure;
	private String projectName;
	private String projectDirectory;

	public DefaultGprProjectFactory(String projectName,
			boolean addMainProcedure, String projectDirectory) {
		this.projectName = projectName;
		this.addMainProcedure = addMainProcedure;
		this.projectDirectory = projectDirectory;
	}

	@Override
	public GprProject createGprProject() {

		GprProject defaultGpr = this.createProjectFile();
		this.createProjectDirectory();
		this.writeProjectFileToDisk(defaultGpr);

		return defaultGpr;

	}

	/**
	 * Creates a GPR project file with parameters given in constructor.
	 * 
	 * @return a newly create GPR project file.
	 */
	private GprProject createProjectFile() {
		GprProject gprProject = new GprProject(this.projectName);

		if (this.addMainProcedure) {
			gprProject.setExecutable(true);
			gprProject
					.addExecutableName(ProjectBuilder.DEFAULT_EXECUTABLE_NAME);
		}

		return gprProject;
	}

	/**
	 * Write the GPR project file as a new .gpr file on disk.
	 * 
	 * @param gprProject
	 *            the project file to write to disk.
	 */
	private void writeProjectFileToDisk(GprProject gprProject) {
		try {
			FileUtils.CreateNewFileWithContents(this.getGprAbsolutePath()
					.toOSString(), gprProject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the directory which will hold the GPR project file.
	 */
	private void createProjectDirectory() {
		File projectFolder = new File(this.projectDirectory);
		projectFolder.mkdirs();
	}

	/**
	 * Returns the absolute path of the GPR project file.
	 * 
	 * @return the absolute path of the GPR project file.
	 */
	private IPath getGprAbsolutePath() {

		StringBuilder pathBuilder = new StringBuilder(this.projectDirectory);

		pathBuilder.append(IPath.SEPARATOR);
		pathBuilder.append(this.projectName);
		pathBuilder.append(AbstractGprProjectFactory
				.GetGprProjectFileExtension());

		return new Path(pathBuilder.toString());
	}

}
