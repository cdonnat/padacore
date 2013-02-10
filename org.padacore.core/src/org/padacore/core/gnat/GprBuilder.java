package org.padacore.core.gnat;

import org.eclipse.core.runtime.IPath;


public class GprBuilder {

	private final static String EXECUTABLE_DIRECTORY_ATTRIBUTE = "Exec_Dir";
	private final static String OBJECT_DIRECTORY_ATTRIBUTE = "Object_Dir";
	private final static String MAIN_ATTRIBUTE = "Main";
	private final static String SOURCE_DIRECTORIES_ATTRIBUTE = "Source_Dirs";

	private IPropertiesProvider referenceProject;
	private IPath referencePath;

	public GprBuilder(IPropertiesProvider project, IPath gprFilePath) {
		this.referenceProject = project;
		this.referencePath = gprFilePath;
	}

	public GprProject build() {
		GprProject res = new GprProject(this.referenceProject.getName(), this.referencePath.removeLastSegments(1));
		this.addSourceDirs(res);
		this.addExecDir(res);
		this.addObjectDir(res);
		this.addExecutables(res);
		return res;
	}

	private void addExecutables(GprProject gprProject) {
		if (this.referenceProject.attributeIsDefined(MAIN_ATTRIBUTE)) {

			gprProject.setExecutable(true);

			for (String execName : this.referenceProject.getAttribute(
					MAIN_ATTRIBUTE).getAsStringList()) {
				gprProject.addExecutableName(execName);
			}
		}
	}

	private void addObjectDir(GprProject gprProject) {
		if (this.referenceProject
				.attributeIsDefined(OBJECT_DIRECTORY_ATTRIBUTE)) {
			gprProject.setObjectDir(this.referenceProject.getAttribute(
					OBJECT_DIRECTORY_ATTRIBUTE).getAsString());
		}
	}

	private void addExecDir(GprProject gprProject) {
		if (this.referenceProject
				.attributeIsDefined(EXECUTABLE_DIRECTORY_ATTRIBUTE)) {
			gprProject.setExecutable(true);
			gprProject.setExecutableDir(this.referenceProject.getAttribute(
					EXECUTABLE_DIRECTORY_ATTRIBUTE).getAsString());
		}
	}

	private void addSourceDirs(GprProject gprProject) {
		if (this.referenceProject
				.attributeIsDefined(SOURCE_DIRECTORIES_ATTRIBUTE)) {
			for (String sourceDir : this.referenceProject.getAttribute(
					SOURCE_DIRECTORIES_ATTRIBUTE).getAsStringList()) {
				gprProject.addSourceDir(sourceDir);
			}
		}
	}

}
