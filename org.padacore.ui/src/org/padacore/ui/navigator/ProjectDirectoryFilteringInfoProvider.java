package org.padacore.ui.navigator;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.IPreferenceStore;
import org.padacore.core.project.IAdaProject;
import org.padacore.ui.Activator;

/**
 * This class enables to retrieve filtering information for Ada project
 * directory (i.e. source, object or executable directory).
 * 
 * @author RS
 * 
 */
public abstract class ProjectDirectoryFilteringInfoProvider {

	protected IAdaProject adaProject;
	protected IPreferenceStore preferenceStore;

	private final static String EXTENSION_WILDCARD = "*";

	public ProjectDirectoryFilteringInfoProvider(IAdaProject adaProject) {
		this.adaProject = adaProject;
		this.preferenceStore = Activator.getDefault().getPreferenceStore();
	}

	/**
	 * Checks if the given file extension shall be displayed in the project
	 * directory according to user preferences.
	 * 
	 * @pre fileExtension is not null
	 * @param fileExtension
	 *            the extension of file
	 * @return true if and only if the given file extension shall be displayed.
	 */
	private boolean doesfileExtensionShallBeDisplayedForProjectDirectory(
			String fileExtension) {
		Assert.isNotNull(fileExtension);
		String[] displayableFileExtensions = this
				.getDisplayableFileExtensions();

		boolean extensionShallBeDisplayed = false;
		int extIndex = 0;

		while (extIndex < displayableFileExtensions.length
				&& !extensionShallBeDisplayed) {
			extensionShallBeDisplayed = fileExtension
					.equals(displayableFileExtensions[extIndex])
					|| displayableFileExtensions[extIndex]
							.equals(EXTENSION_WILDCARD);
			extIndex++;
		}

		return extensionShallBeDisplayed;
	}

	/**
	 * Checks if the given file is contained in the project directory.
	 * 
	 * @param file
	 *            the file we want to check
	 * @return true if and only if the file is contained in the project
	 *         directory.
	 */
	private boolean isFileContainedInProjectDirectory(IFile file) {
		Iterator<IPath> projectDirIt = this.getProjectDirectoryPaths()
				.iterator();
		boolean fileIsContainedInProjectDirectory = false;
		IPath projectDir;

		while (!fileIsContainedInProjectDirectory && projectDirIt.hasNext()) {
			projectDir = projectDirIt.next();

			fileIsContainedInProjectDirectory = file.getParent().getLocation()
					.equals(projectDir);
		}

		return fileIsContainedInProjectDirectory;
	}

	/**
	 * Checks if the file belongs to project directory and shall be displayed
	 * according to its extension.
	 * 
	 * @pre file has an extension
	 * @param file
	 *            the file to check
	 * @return true if and only if the file belongs to project directory and
	 *         shall be displayed.
	 */
	public boolean isFileADisplayableFileOfProjectDirectory(IFile file) {
		String fileExtension = file.getFileExtension();
		Assert.isNotNull(fileExtension);

		return this.isFileContainedInProjectDirectory(file)
				&& this.doesfileExtensionShallBeDisplayedForProjectDirectory(fileExtension);
	}

	/**
	 * Checks if the given folder is an ancestor of the project directory
	 * (either itself or a parent).
	 * 
	 * @param folder
	 *            the folder to check
	 * @return true if and only if the folder is an ancestor of project
	 *         directory.
	 */
	public boolean isFolderAnAncestorOfProjectDirectory(IFolder folder) {
		Iterator<IPath> projectDirectoryIt = this.getProjectDirectoryPaths()
				.iterator();
		boolean folderIsAncestorOfProjectDir = false;
		IPath projectDirectory;

		while (!folderIsAncestorOfProjectDir && projectDirectoryIt.hasNext()) {
			projectDirectory = projectDirectoryIt.next();

			folderIsAncestorOfProjectDir = folder.getLocation().isPrefixOf(
					projectDirectory);
		}

		return folderIsAncestorOfProjectDir;
	}

	/**
	 * Returns the absolute paths of project directories.
	 * 
	 * @return the absolute paths of project directories.
	 */
	protected abstract List<IPath> getProjectDirectoryPaths();

	/**
	 * Return the list of file extensions that shall be displayed for these
	 * project directories.
	 * 
	 * @return the list of file extensions that shall be displayed for these
	 *         project directories.
	 */
	protected abstract String[] getDisplayableFileExtensions();

	/**
	 * Retrieve the list of user-defined file extensions according to given
	 * preference.
	 * 
	 * @param preferenceName
	 *            identifier of the preference in which file extensions are
	 *            stored.
	 * @return an array of String corresponding to user-defined file extensions
	 *         for the given preference.
	 */
	protected String[] retrieveExtensionsFromPreferences(String preferenceName) {
		String listDelimiter = ";";

		return preferenceStore.getString(preferenceName).split(listDelimiter);
	}
}
