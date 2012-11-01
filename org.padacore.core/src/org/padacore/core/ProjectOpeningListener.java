package org.padacore.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

public class ProjectOpeningListener implements IResourceChangeListener {

	private IGprAssociationManager gprAssociationManager;

	public ProjectOpeningListener(IGprAssociationManager gprAssociationManager) {
		this.gprAssociationManager = gprAssociationManager;
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
		IResourceDeltaVisitor projectVisitor = new ProjectOpeningVisitor(
				this.gprAssociationManager);

		delta.accept(projectVisitor);
	}

}
