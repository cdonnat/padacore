package org.padacore.core.gnat.project;

import org.padacore.core.IAdaProject;
import org.padacore.core.IAdaProjectFactory;

public class DefaultGnatAdaProjectFactory implements IAdaProjectFactory {

	private GprProject defaultGpr;

	public DefaultGnatAdaProjectFactory(String projectName,
			boolean addMainProcedure, String executableName) {
		this.defaultGpr = GprProjectFactory.CreateDefaultGprProject(
				projectName, addMainProcedure, executableName);

	}

	@Override
	public IAdaProject createAdaProject() {
		return new GnatAdaProject(this.defaultGpr);
	}
	
	public GprProject getGprProject() {
		return this.defaultGpr;
	}
}
