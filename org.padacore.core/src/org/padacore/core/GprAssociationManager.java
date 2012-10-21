package org.padacore.core;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class GprAssociationManager implements IGprAssociationManager,
		IResourceChangeListener {

	@Override
	public void performAssociationToGprProject(IProject adaProject,
			IPath gprFilePath) {
		try {
			if (NewAdaProject.GetAssociatedGprProject(adaProject) == null) {
				NewAdaProject.AssociateIProjectToGprProject(adaProject,
						gprFilePath);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		try {
			Assert.isTrue(NewAdaProject.GetAssociatedGprProject(adaProject) != null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Associate a new GPR project (created from given GPR file) to all Ada
	 * projects found in workspace.
	 * 
	 * @param workspace the workspace in which Ada projects are looked for.
	 **/
	public void performAssociationToGprProjectForAllAdaProjectsOf(
			IWorkspace workspace) {

		IProject[] workspaceProjects = workspace.getRoot().getProjects();
		IProject currentProject;

		for (int project = 0; project < workspaceProjects.length; project++) {
			currentProject = workspaceProjects[project];

			try {
				if (currentProject.isOpen() && currentProject.hasNature(AdaProjectNature.NATURE_ID)) {

					this.performAssociationToGprProject(currentProject,
							NewAdaProject.GetGprAbsolutePath(currentProject));
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @pre Event type is POST_CHANGE
	 */
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		Assert.isLegal(event.getType() == IResourceChangeEvent.POST_CHANGE);

		IResourceDelta rootDelta = event.getDelta();

		try {
			this.findAllProjectsWhichHaveBeenOpened(rootDelta);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void findAllProjectsWhichHaveBeenOpened(IResourceDelta delta)
			throws CoreException {
		IResourceDeltaVisitor projectVisitor = new ProjectOpeningVisitor(this);

		delta.accept(projectVisitor);
	}
}
