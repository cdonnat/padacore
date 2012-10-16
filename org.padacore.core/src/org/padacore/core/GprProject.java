package org.padacore.core;

import java.util.ArrayList;
import java.util.List;

public class GprProject {

	private String name;
	private List<String> sourcesDir = new ArrayList<String>();
	private String objectDir = null;
	private boolean isExecutable = false;
	private String execDir = null;
	private List<String> execSourceNames = new ArrayList<String>(1);
	private List<String> withedProjects = new ArrayList<String>();

	/**
	 * Create a default GPR project with given name.
	 * 
	 * @param name
	 *            GPR name
	 */
	public GprProject(String name) {
		assert !name.isEmpty();

		this.name = name;
	}

	/**
	 * Add a new source directory
	 * 
	 * @param dirName
	 *            Source directory to add.
	 */
	public void addSourceDir(String dirName) {
		this.sourcesDir.add(dirName);
	}

	/**
	 * Return the list of sources directory.
	 * 
	 * @return The list of sources directory.
	 */
	public List<String> getSourcesDir() {
		return sourcesDir;
	}

	/**
	 * Return the object directory or null if none was specified.
	 * 
	 * @return Object directory.
	 */
	public String getObjectDir() {
		return objectDir;
	}

	/**
	 * Set the new object directory.
	 * 
	 * @param objectDir
	 *            New object directory.
	 */
	public void setObjectDir(String objectDir) {
		this.objectDir = objectDir;
	}

	/**
	 * Set if the project is an executable project.
	 * 
	 * @param isExecutable
	 */
	public void setExecutable(boolean isExecutable) {
		this.isExecutable = isExecutable;
	}

	/**
	 * Add an executable to the project.
	 * 
	 * @pre GPR is an executable project.
	 * @param execName
	 */
	public void addExecutableName(String execName) {
		assert isExecutable;

		this.execSourceNames.add(execName);
	}

	/**
	 * Return executable directory or null if none was specified.
	 * 
	 * @pre GPR is an executable project.
	 * @return The executable directory
	 */
	public String getExecutableDir() {
		assert isExecutable;

		return execDir;
	}

	/**
	 * Return true if the GPR is an executable project, false otherwise.
	 * 
	 * @return true if the GPR is an executable project, false otherwise.
	 */
	public boolean isExecutable() {
		return this.isExecutable;
	}

	/**
	 * Set the executable directory.
	 * 
	 * @pre GPR is an executable project.
	 */
	public void setExecutableDir(String execDir) {
		assert isExecutable;

		this.execDir = execDir;
	}

	/**
	 * Add a new withed project
	 * 
	 * @param projectPath
	 *            Path of the withed project.
	 */
	public void addWithedProject(String projectPath) {
		this.withedProjects.add(projectPath);
	}

	/**
	 * The project name is returned.
	 * 
	 * @return The project name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the name of the GPR file associated.
	 * 
	 * @return The name of the GPR file associated.
	 */
	public String getFileName() {
		return this.getName() + ".gpr";
	}

	/**
	 * Returns the names of the executable source files of the project.
	 * 
	 * @return a list of String corresponding to the names of executable source
	 *         files.
	 */
	public List<String> getExecutableSourceNames() {
		return this.execSourceNames;
	}

	/**
	 * Returns the String corresponding to the list of executable names.
	 * 
	 * Precondition: the project is executable.
	 * 
	 * @return the String corresponding to the list of executable names under
	 *         the form ("exe1", "exe2",...)
	 */
	private String executableNamesAsString() {
		assert (this.isExecutable);

		String listOfExecutablesAsString = "(";

		for (int exec = 0; exec < this.execSourceNames.size(); exec++) {

			listOfExecutablesAsString = listOfExecutablesAsString + "\""
					+ this.execSourceNames.get(exec) + "\"";

			if (exec != this.execSourceNames.size() - 1) {
				listOfExecutablesAsString = listOfExecutablesAsString + ", ";
			}
		}

		listOfExecutablesAsString = listOfExecutablesAsString + ")";

		return listOfExecutablesAsString;

	}

	/**
	 * Content of the GPR project is returned.
	 * 
	 * @return Content of the GPR project.
	 */
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("project " + this.getName() + " is\n");

		this.appendSourceDirsIfAny(stringBuilder);

		this.appendObjectDirIfDefined(stringBuilder);

		this.appendExecSourceNamesIfAny(stringBuilder);

		stringBuilder.append("end " + this.getName() + ";");

		return stringBuilder.toString();
	}

	private void appendExecSourceNamesIfAny(StringBuilder stringBuilder) {
		if (isExecutable) {
			if (this.getExecutableDir() != null) {
				stringBuilder.append("\tfor Exec_Dir use \""
						+ this.getExecutableDir() + "\";\n");
			}
			stringBuilder.append("\tfor Main use "
					+ this.executableNamesAsString() + ";\n");
		}
	}

	private void appendObjectDirIfDefined(StringBuilder stringBuilder) {
		if (this.getObjectDir() != null) {
			stringBuilder.append("\tfor Object_Dir use \""
					+ this.getObjectDir() + "\";\n");
		}
	}

	private void appendSourceDirsIfAny(StringBuilder stringBuilder) {
		if (this.getSourcesDir().size() != 0) {
			stringBuilder.append("\tfor Source_Dirs use (");

			for (int i = 0; i < this.getSourcesDir().size() - 1; i++) {
				stringBuilder.append("\"" + this.getSourcesDir().get(i)
						+ "\",\n");
			}
			stringBuilder.append("\""
					+ this.getSourcesDir().get(this.getSourcesDir().size() - 1)
					+ "\");\n");
		}
	}
}
