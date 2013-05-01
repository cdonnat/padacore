package org.padacore.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.padacore.ui.Activator;
import org.padacore.ui.preferences.IPreferenceConstants;

public class FileExtensionFilter extends ViewerFilter {

	private IPreferenceStore preferenceStore;
	private final static String GNAT_PROJECT_EXTENSION = "gpr";

	public FileExtensionFilter() {
		this.preferenceStore = Activator.getDefault().getPreferenceStore();
	}

	/**
	 * Retrieve the list of user-defined file extensions that filter shall
	 * display.
	 * 
	 * @return an array of String corresponding to user-defined file extensions.
	 */
	private String[] retrieveExtensionsFromPreferences() {
		String listDelimiter = ";";

		return preferenceStore.getString(
				IPreferenceConstants.NAVIGATOR_EXTENSIONS_PREF).split(
				listDelimiter);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		boolean selectElt = false;

		if (element instanceof IResource) {
			if (element instanceof IFile) {
				IFile file = (IFile) element;
				String fileExtension = file.getFileExtension();

				if (fileExtension != null) {
					selectElt = fileExtension.equals(GNAT_PROJECT_EXTENSION)
							|| this.fileExtensionMatchesOneOfUserDefined(fileExtension);
				}
			} else {
				selectElt = true;
			}
		}

		return selectElt;
	}

	/**
	 * Returns True if and only if given file extension matches one of the
	 * user-defined ones.
	 * 
	 * @param fileExtension
	 *            the file extension to verify
	 * @return True if and only if given file extension matches one of the
	 *         user-defined ones.
	 */
	private boolean fileExtensionMatchesOneOfUserDefined(String fileExtension) {
		String[] userDefinedExtensions = this
				.retrieveExtensionsFromPreferences();

		boolean extensionIsDefinedByUser = false;
		int extIndex = 0;

		while (extIndex < userDefinedExtensions.length
				&& !extensionIsDefinedByUser) {
			extensionIsDefinedByUser = fileExtension
					.equals(userDefinedExtensions[extIndex]);
			extIndex++;
		}

		return extensionIsDefinedByUser;
	}
}
