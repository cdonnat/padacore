package org.padacore;

import java.util.ArrayList;
import java.util.List;

public class GprProject {

	private String name;
	private List<String> sourcesDir = new ArrayList<String>();
	private String objectDir = "obj";
	private boolean isExecutable = false;
	private String execDir = "exe";
	private List<String> execNames = new ArrayList<String>(1);
	private List<String> withedProjects = new ArrayList<String>();

	/**
	 * Create a default GPR project with a default sources directory (".") and a
	 * default object directory ("obj").
	 * 
	 * @param name
	 *            GPR name
	 */
	public GprProject(String name) {
		assert !name.isEmpty();

		this.name = name;
		this.addSourceDir(".");
		this.execNames.add("main.adb");
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
	public List<String> sourcesDir() {
		return sourcesDir;
	}

	/**
	 * Return the object directory.
	 * 
	 * @return Object directory.
	 */
	public String objectDir() {
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

		this.execNames.add(execName);
	}

	/**
	 * Return executable directory.
	 * 
	 * @pre GPR is an executable project.
	 * @return The executable directory
	 */
	public String executableDir() {
		assert isExecutable;

		return execDir;
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
	public String name() {
		return name;
	}

	/**
	 * Return the name of the GPR file associated.
	 * 
	 * @return The name of the GPR file associated.
	 */
	public String fileName() {
		return name() + ".gpr";
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

		for (int exec = 0; exec < this.execNames.size(); exec++) {

			listOfExecutablesAsString = listOfExecutablesAsString + "\"" + this.execNames.get(exec) + "\"";

			if (exec != this.execNames.size() - 1) {
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
		String res = "project " + name() + " is\n" + "\tfor Source_Dirs use (";

		for (int i = 0; i < sourcesDir().size() - 1; i++) {
			res += "\"" + sourcesDir().get(i) + "\",\n";
		}
		res += "\"" + sourcesDir().get(sourcesDir().size() - 1) + "\");\n";

		res += "\tfor Object_Dir use \"" + objectDir() + "\";\n";

		if (isExecutable) {
			res += "\tfor Exec_Dir use \"" + executableDir() + "\";\n";
			res += "\tfor Main use " + this.executableNamesAsString() + ";\n";
		}
		res += "end " + name() + ";";

		return res;
	}
}
