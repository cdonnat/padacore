package org.padacore.ui.navigator.filters;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.utils.ErrorLog;

/**
 * This class defines a filter which hides non-Ada projects.
 * 
 * @author RS
 * 
 */
public class AdaProjectsFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Assert.isLegal(element instanceof IResource);
		boolean elementShallBeDisplayed = true;

		IResource resource = (IResource) element;

		if (resource.getType() == IResource.PROJECT) {
			elementShallBeDisplayed = this
					.isProjectAnAdaProject((IProject) resource);
		}

		return elementShallBeDisplayed;
	}

	/**
	 * Checks if the given project shall be considered as an Ada project.
	 * 
	 * @param project
	 *            the project ot check
	 * @return true if and only if the project is open and has an Ada nature or
	 *         the project is closed.
	 */
	private boolean isProjectAnAdaProject(IProject project) {
		boolean isAnAdaProject = false;

		if (project.isOpen()) {
			try {
				isAnAdaProject = project.hasNature(AdaProjectNature.NATURE_ID);
			} catch (CoreException e) {
				ErrorLog.appendException(e);
			}
		} else {
			// since we don't know the nature of project, assume it
			// might be an Ada project
			isAnAdaProject = true;
		}

		return isAnAdaProject;
	}
}