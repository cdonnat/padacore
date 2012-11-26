package org.padacore.core.gnat.project;

/**
 * This class allows to create a default GPR project. 
 * @author RS
 *
 */
public class DefaultGprProjectFactory extends AbstractGprProjectFactory {

	private final static String DEFAULT_EXECUTABLE_NAME = "main.adb";
	private boolean addMainProcedure;
	private String projectName;

	public DefaultGprProjectFactory(String projectName, boolean addMainProcedure) {
		this.projectName = projectName;
		this.addMainProcedure = addMainProcedure;
	}

	@Override
	public GprProject createGprProject() {
		GprProject gprProject = new GprProject(this.projectName);

		if (this.addMainProcedure) {
			gprProject.setExecutable(true);
			gprProject.addExecutableName(DEFAULT_EXECUTABLE_NAME);
		}

		return gprProject;
	}

}
