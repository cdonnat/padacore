package org.padacore.core.gnat;

import org.eclipse.core.runtime.IPath;
import org.gpr4j.api.IGpr;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.IAdaProjectFactory;

public class GnatAdaProjectFactory implements IAdaProjectFactory {

	private IGpr gprProject;

	public GnatAdaProjectFactory(IPath gprFilePath) {
		FileGprProjectFactory gprFactory = new FileGprProjectFactory(
				gprFilePath);
		this.gprProject = gprFactory.createGprProject();
	}

	@Override
	public IAdaProject createAdaProject() {
		return new GnatAdaProject(this.gprProject);
	}
}