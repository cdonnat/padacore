package org.padacore.ui.navigator.filters;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.Viewer;
import org.padacore.core.project.IAdaProject;

/**
 * This class defines a filter which hides executable object directory of Ada
 * project.
 * 
 * @author RS
 * 
 */
public class ExecutableDirectoryFilter extends AbstractProjectDirectoryFilter {

	public ExecutableDirectoryFilter() {
		super();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Assert.isLegal(element instanceof IResource);
		boolean elementShallBeHidden = false;

		IResource resource = (IResource) element;

		if (this.isResourceMemberOfAdaProject(resource)
				&& resource.getType() == IResource.FOLDER) {
			IAdaProject adaProject = this.getAdaProjectFor(resource);
			ProjectDirectoryFilteringInfoProvider sourceDirInfoProvider = new SourceDirectoriesFilteringInfoProvider(
					adaProject);
			ProjectDirectoryFilteringInfoProvider objectDirInfoProvider = new ObjectDirectoryFilteringInfoProvider(
					adaProject);
			ProjectDirectoryFilteringInfoProvider executableDirInfoProvider = new ExecutableDirectoryFilteringInfoProvider(
					adaProject);

			IFolder folder = (IFolder) resource;

			elementShallBeHidden = this.shallFolderBeHidden(
					sourceDirInfoProvider, objectDirInfoProvider,
					executableDirInfoProvider, folder);
		}

		return !elementShallBeHidden;
	}

	/**
	 * Checks if the given folder shall be hidden.
	 * 
	 * @param sourceDirInfoProvider
	 *            filtering information provider for source directories
	 * @param objectDirInfoProvider
	 *            filtering information provider for object directory
	 * @param executableDirInfoProvider
	 *            filtering information provider for executable directory
	 * @param folder
	 *            the folder to check
	 * @return true if and only if folder shall be hidden.
	 */
	private boolean shallFolderBeHidden(
			ProjectDirectoryFilteringInfoProvider sourceDirInfoProvider,
			ProjectDirectoryFilteringInfoProvider objectDirInfoProvider,
			ProjectDirectoryFilteringInfoProvider executableDirInfoProvider,
			IFolder folder) {

		return executableDirInfoProvider
				.isFolderAnAncestorOfProjectDirectory(folder)
				&& !sourceDirInfoProvider
						.isFolderAnAncestorOfProjectDirectory(folder)
				&& !objectDirInfoProvider
						.isFolderAnAncestorOfProjectDirectory(folder);
	}
}
