package org.padacore.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class ProjectOpeningVisitor implements IResourceDeltaVisitor {

	private AbstractAdaProjectAssociationManager adaProjectAssociationManager;

	/**
	 * Builds a new project opening visitor.
	 * 
	 * @param adaProjectAssociationManager
	 *            the Ada project association manager to notify when an opened
	 *            project is found.
	 */
	public ProjectOpeningVisitor(
			AbstractAdaProjectAssociationManager adaProjectAssociationManager) {
		this.adaProjectAssociationManager = adaProjectAssociationManager;
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		boolean processChildren;

		switch (delta.getKind()) {
			case IResourceDelta.CHANGED:
				if (deltaIndicatesProjectOpening(delta)) {
					IProject concernedProject = delta.getResource()
							.getProject();

					if (concernedProject.hasNature(AdaProjectNature.NATURE_ID)) {
						adaProjectAssociationManager
								.associateToAdaProject(concernedProject);
					}

					processChildren = false;
				} else {
					processChildren = true;
				}

				break;

			default:
				processChildren = false;
				break;
		}

		return processChildren;
	}

	/**
	 * Returns True if the given resource delta contains a project opening
	 * event.
	 * 
	 * @param delta
	 *            the resource delta to examine.
	 * @return true if a project opening event exists in resource delta, false
	 *         otherwise.
	 */
	private boolean deltaIndicatesProjectOpening(IResourceDelta delta) {
		boolean deltaIndicatesProjectOpening = false;
		boolean projectOpeningOrClosure = (delta.getFlags() & IResourceDelta.OPEN) != 0;

		if (projectOpeningOrClosure) {
			IProject concernedProject = delta.getResource().getProject();
			if (concernedProject.isOpen()) {
				deltaIndicatesProjectOpening = true;
			}
		}

		return deltaIndicatesProjectOpening;
	}
}
