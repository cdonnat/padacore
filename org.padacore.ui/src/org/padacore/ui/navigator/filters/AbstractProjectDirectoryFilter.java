package org.padacore.ui.navigator.filters;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ViewerFilter;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.utils.ErrorLog;

/**
 * This class is the base class for filters related to Ada project directories.
 * 
 * @author RS
 * 
 */
public abstract class AbstractProjectDirectoryFilter extends ViewerFilter {

	/**
	 * Returns the IAdaProject for the project which contains the given
	 * resource.
	 * 
	 * @pre resource belongs to an Ada project
	 * @param resource
	 *            the resource for which we want to retrieve the IAdaProject.
	 * @return the IAdaProject for the project which contains the given
	 *         resource.
	 */
	protected IAdaProject getAdaProjectFor(IResource resource) {
		Assert.isLegal(this.isResourceMemberOfAdaProject(resource));

		IProject enclosingProject = resource.getProject();
		PropertiesManager propertiesManager = new PropertiesManager(
				enclosingProject);

		return propertiesManager.getAdaProject();

	}

	/**
	 * Checks if the given resource belongs to an open Ada project.
	 * 
	 * @param resource
	 *            the resource to check
	 * @return true if and only if the resource belongs to an open project with
	 *         Ada nature.
	 */
	protected boolean isResourceMemberOfAdaProject(IResource resource) {
		boolean inAdaProject = false;

		IProject enclosingProject = resource.getProject();

		if (enclosingProject.isOpen()) {
			try {
				inAdaProject = enclosingProject
						.hasNature(AdaProjectNature.NATURE_ID);
			} catch (CoreException e) {
				ErrorLog.appendException(e);
			}
		}

		return inAdaProject;
	}
}
