package org.padacore.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.utils.ErrorLog;
import org.padacore.ui.Activator;
import org.padacore.ui.preferences.IPreferenceConstants;

/**
 * This class defines a filter which displays only source, object, executable
 * directories and GNAT project file of an Ada project.
 * 
 * @author RS
 * 
 */
public class ProjectAndDirectoriesFilter extends ViewerFilter {

	private final static String GNAT_PROJECT_EXTENSION = "gpr";
	private IPreferenceStore preferenceStore;

	public ProjectAndDirectoriesFilter() {
		super();
		this.preferenceStore = Activator.getDefault().getPreferenceStore();
	}

	/**
	 * Returns the IAdaProject for the project which contains the given
	 * resource.
	 * 
	 * @param resource
	 *            the resource for which we want to retrieve the IAdaProject.
	 * @return the IAdaProject for the project which contains the given
	 *         resource.
	 */
	private IAdaProject getAdaProjectFor(IResource resource) {
		IProject enclosingProject = resource.getProject();
		PropertiesManager propertiesManager = new PropertiesManager(
				enclosingProject);

		return propertiesManager.getAdaProject();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Assert.isLegal(element instanceof IResource);
		boolean elementShallBeDisplayed = false;

		IResource resource = (IResource) element;

		if (resource.getType() == IResource.PROJECT) {
			elementShallBeDisplayed = this
					.isProjectAnAdaProject((IProject) resource);
		} else {
			IAdaProject adaProject = this.getAdaProjectFor(resource);
			ProjectDirectoryFilteringInfoProvider sourceDirInfoProvider = new SourceDirectoryFilteringInfoProvider(
					adaProject);
			ProjectDirectoryFilteringInfoProvider objectDirInfoProvider = new ObjectDirectoryFilteringInfoProvider(
					adaProject);
			ProjectDirectoryFilteringInfoProvider executableDirInfoProvider = new ExecutableDirectoryFilteringInfoProvider(
					adaProject);

			switch (resource.getType()) {
				case IResource.FOLDER:
					IFolder folder = (IFolder) resource;

					elementShallBeDisplayed = this.shallFolderBeDisplayed(
							sourceDirInfoProvider, objectDirInfoProvider,
							executableDirInfoProvider, folder);
					break;

				case IResource.FILE:
					IFile file = (IFile) resource;

					elementShallBeDisplayed = this.shallFileBeDisplayed(
							sourceDirInfoProvider, objectDirInfoProvider,
							executableDirInfoProvider, file);
					break;
			}
		}

		return elementShallBeDisplayed;
	}

	/**
	 * Checks if the given file shall be displayed.
	 * 
	 * @param sourceDirInfoProvider
	 *            filtering information provider for source directories
	 * @param objectDirInfoProvider
	 *            filtering information provider for object directory
	 * @param executableDirInfoProvider
	 *            filtering information provider for executable directory
	 * @param file
	 *            the file to check
	 * @return true if and only if given file shall be displayed
	 */
	private boolean shallFileBeDisplayed(
			ProjectDirectoryFilteringInfoProvider sourceDirInfoProvider,
			ProjectDirectoryFilteringInfoProvider objectDirInfoProvider,
			ProjectDirectoryFilteringInfoProvider executableDirInfoProvider,
			IFile file) {

		boolean fileShallBeDisplayed;
		String fileExtension = file.getFileExtension();

		if (fileExtension != null) {
			fileShallBeDisplayed = sourceDirInfoProvider
					.isFileADisplayableFileOfProjectDirectory(file)
					|| objectDirInfoProvider
							.isFileADisplayableFileOfProjectDirectory(file)
					|| executableDirInfoProvider
							.isFileADisplayableFileOfProjectDirectory(file)
					|| this.isFileTheGnatProjectFile(file);

		} else {
			fileShallBeDisplayed = this
					.doesUserWantsToDisplayFilesWithoutExtensions();
		}
		return fileShallBeDisplayed;
	}

	/**
	 * Checks if the given folder shall be displayed.
	 * 
	 * @param sourceDirInfoProvider
	 *            filtering information provider for source directories
	 * @param objectDirInfoProvider
	 *            filtering information provider for object directory
	 * @param executableDirInfoProvider
	 *            filtering information provider for executable directory
	 * @param folder
	 *            the folder to check
	 * @return true if and only if folder shall be displayed.
	 */
	private boolean shallFolderBeDisplayed(
			ProjectDirectoryFilteringInfoProvider sourceDirInfoProvider,
			ProjectDirectoryFilteringInfoProvider objectDirInfoProvider,
			ProjectDirectoryFilteringInfoProvider executableDirInfoProvider,
			IFolder folder) {

		return sourceDirInfoProvider
				.isFolderAnAncestorOfProjectDirectory(folder)
				|| objectDirInfoProvider
						.isFolderAnAncestorOfProjectDirectory(folder)
				|| executableDirInfoProvider
						.isFolderAnAncestorOfProjectDirectory(folder);
	}

	/**
	 * Checks if the given file corresponds to the GNAT project file of project.
	 * 
	 * @param file
	 *            the file to check
	 * @return true if and only if given file corresponds to the GNAT project
	 *         file (i.e. it has a .gpr extension and is at the root of the
	 *         project).
	 */
	private boolean isFileTheGnatProjectFile(IFile file) {
		IAdaProject adaProject = this.getAdaProjectFor(file);

		return file.getParent().getLocation().equals(adaProject.getRootPath())
				&& GNAT_PROJECT_EXTENSION.equals(file.getFileExtension());
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

	/**
	 * Returns true if and only if user chose to display files without
	 * extensions.
	 * 
	 * @return true if and only if user chose to display files without
	 *         extensions.
	 */
	private boolean doesUserWantsToDisplayFilesWithoutExtensions() {
		return this.preferenceStore
				.getBoolean(IPreferenceConstants.NAVIGATOR_FILE_WITHOUT_EXTENSION);
	}

}
