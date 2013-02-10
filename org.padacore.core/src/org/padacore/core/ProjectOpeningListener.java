package org.padacore.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.padacore.core.utils.ErrorLogger;

public class ProjectOpeningListener implements IResourceChangeListener {

	private AbstractAdaProjectAssociationManager adaProjectAssociationManager;

	public ProjectOpeningListener(
			AbstractAdaProjectAssociationManager adaProjectAssociationManager) {
		this.adaProjectAssociationManager = adaProjectAssociationManager;
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
			ErrorLogger.appendExceptionToErrorLog(e);
		}
	}

	/**
	 * Visits the resource delta to find all the projects that have just been opened.
	 * @param delta the resource delta to examine.
	 * @throws CoreException if visiting could not be performed.
	 */
	private void findAllProjectsWhichHaveBeenOpened(IResourceDelta delta)
			throws CoreException {
		IResourceDeltaVisitor projectVisitor = new ProjectOpeningVisitor(
				this.adaProjectAssociationManager);

		delta.accept(projectVisitor);
	}

}
