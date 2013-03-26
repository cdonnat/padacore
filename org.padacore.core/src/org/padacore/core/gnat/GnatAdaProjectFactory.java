package org.padacore.core.gnat;

import org.eclipse.core.runtime.IPath;
import org.gpr4j.core.Gpr;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.IAdaProjectFactory;

public class GnatAdaProjectFactory implements IAdaProjectFactory {

	private Gpr gprProject;

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