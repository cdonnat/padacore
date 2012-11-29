package org.padacore.core.gnat;


public class GprBuilder {

	private final static String EXECUTABLE_DIRECTORY_ATTRIBUTE = "Exec_Dir";
	private final static String OBJECT_DIRECTORY_ATTRIBUTE = "Object_Dir";
	private final static String MAIN_ATTRIBUTE = "Main";
	private final static String SOURCE_DIRECTORIES_ATTRIBUTE = "Source_Dirs";

	private Context referenceContext;

	public GprBuilder(Context context) {
		this.referenceContext = context;
	}

	public GprProject build() {
		GprProject res = new GprProject(this.referenceContext.getName());
		this.addSourceDirs(res);
		this.addExecDir(res);
		this.addObjectDir(res);
		this.addExecutables(res);
		return res;
	}

	private void addExecutables(GprProject gprProject) {
		if (this.referenceContext.attributeIsDefined(MAIN_ATTRIBUTE)) {

			gprProject.setExecutable(true);

			for (String execName : this.referenceContext.getAttribute(
					MAIN_ATTRIBUTE).getAsStringList()) {
				gprProject.addExecutableName(execName);
			}
		}
	}

	private void addObjectDir(GprProject gprProject) {
		if (this.referenceContext
				.attributeIsDefined(OBJECT_DIRECTORY_ATTRIBUTE)) {
			gprProject.setObjectDir(this.referenceContext.getAttribute(
					OBJECT_DIRECTORY_ATTRIBUTE).getAsString());
		}
	}

	private void addExecDir(GprProject gprProject) {
		if (this.referenceContext
				.attributeIsDefined(EXECUTABLE_DIRECTORY_ATTRIBUTE)) {
			gprProject.setExecutable(true);
			gprProject.setExecutableDir(this.referenceContext.getAttribute(
					EXECUTABLE_DIRECTORY_ATTRIBUTE).getAsString());
		}
	}

	private void addSourceDirs(GprProject gprProject) {
		if (this.referenceContext
				.attributeIsDefined(SOURCE_DIRECTORIES_ATTRIBUTE)) {
			for (String sourceDir : this.referenceContext.getAttribute(
					SOURCE_DIRECTORIES_ATTRIBUTE).getAsStringList()) {
				gprProject.addSourceDir(sourceDir);
			}
		}
	}

}
