package org.padacore.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

public class ProjectOpeningListener implements IResourceChangeListener {

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

	private class ProjectOpeningVisitor implements IResourceDeltaVisitor {

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			boolean processChildren;

			switch (delta.getKind()) {
			case IResourceDelta.CHANGED:
				if (deltaIndicatesProjectOpening(delta)) {
					IProject concernedProject = delta.getResource()
							.getProject();

					System.out.println("Opened project "
							+ concernedProject.getName());

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

	private void findAllProjectsWhichHaveBeenOpened(IResourceDelta delta)
			throws CoreException {
		IResourceDeltaVisitor projectVisitor = new ProjectOpeningVisitor();

		delta.accept(projectVisitor);
	}
}
