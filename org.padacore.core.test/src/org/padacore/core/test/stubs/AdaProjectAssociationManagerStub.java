package org.padacore.core.test.stubs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.padacore.core.AbstractAdaProjectAssociationManager;

public class AdaProjectAssociationManagerStub extends
		AbstractAdaProjectAssociationManager {

	private List<IProject> associatedProjects;

	public AdaProjectAssociationManagerStub() {
		this.associatedProjects = new ArrayList<IProject>();
	}

	@Override
	public void associateToAdaProject(IProject eclipseProject) {
		if (!this.associatedProjects.contains(eclipseProject)) {
			this.associatedProjects.add(eclipseProject);
		}
	}

	public boolean isEclipseProjectAssociatedToAdaProject(
			IProject eclipseProject) {
		return this.associatedProjects.contains(eclipseProject);
	}

}
