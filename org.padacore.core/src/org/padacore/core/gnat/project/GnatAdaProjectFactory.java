package org.padacore.core.gnat.project;

import org.eclipse.core.runtime.IPath;
import org.padacore.core.IAdaProject;
import org.padacore.core.IAdaProjectFactory;

public class GnatAdaProjectFactory implements IAdaProjectFactory {

	private GprProject gprProject;

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