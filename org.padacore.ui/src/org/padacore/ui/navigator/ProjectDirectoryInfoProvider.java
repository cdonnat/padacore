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
 * This class enables to retrieve information for Ada project directory (which
 * type is either source, object or executable).
 * 
 * @author RS
 * 
 */
public abstract class ProjectDirectoryInfoProvider {

	protected IAdaProject adaProject;
	protected IPreferenceStore preferenceStore;

	private final static String EXTENSION_WILDCARD = "*";

	public ProjectDirectoryInfoProvider(IAdaProject adaProject) {
		this.adaProject = adaProject;
		this.preferenceStore = Activator.getDefault().getPreferenceStore();
	}

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

	public boolean shallFileBeDisplayedInProjectDirectory(IFile file) {
		String fileExtension = file.getFileExtension();
		Assert.isNotNull(fileExtension);

		return this.isFileContainedInProjectDirectory(file)
				&& this.doesfileExtensionShallBeDisplayedForProjectDirectory(fileExtension);
	}

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
