package org.padacore.core;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class GprAssociationManager implements IGprAssociationManager {

	@Override
	public void performAssociationToGprProject(IProject adaProject,
			IPath gprFilePath) {
		try {
			Assert.isLegal(adaProject.hasNature(AdaProjectNature.NATURE_ID) && adaProject.isOpen());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		try {
			if (NewAdaProject.GetAssociatedGprProject(adaProject) == null) {
				NewAdaProject.AssociateIProjectToGprProject(adaProject,
						gprFilePath);
			}

			Assert.isTrue(NewAdaProject.GetAssociatedGprProject(adaProject) != null);
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Associate a new GPR project (created from given GPR file) to all Ada
	 * projects found in workspace.
	 * 
	 * @param workspace
	 *            the workspace in which Ada projects are looked for.
	 **/
	public void performAssociationToGprProjectForAllAdaProjectsOf(
			IWorkspace workspace) {

		IProject[] workspaceProjects = workspace.getRoot().getProjects();
		IProject currentProject;

		for (int project = 0; project < workspaceProjects.length; project++) {
			currentProject = workspaceProjects[project];

			try {
				if (currentProject.isOpen()
						&& currentProject.hasNature(AdaProjectNature.NATURE_ID)) {

					this.performAssociationToGprProject(currentProject,
							NewAdaProject.GetGprAbsolutePath(currentProject));
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
}
