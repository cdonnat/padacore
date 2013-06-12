package org.padacore.ui.navigator;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.padacore.core.project.IAdaProject;
import org.padacore.ui.preferences.IPreferenceConstants;

/**
 * This class defines implementation of ProjectDirectoryInfoProvider for Ada
 * project source directories.
 * 
 * @author RS
 * 
 */
public class SourceDirectoryFilteringInfoProvider extends
		ProjectDirectoryFilteringInfoProvider {

	public SourceDirectoryFilteringInfoProvider(IAdaProject adaProject) {
		super(adaProject);
	}

	@Override
	protected List<IPath> getProjectDirectoryPaths() {
		return this.adaProject.getSourceDirectoriesPaths();
	}

	@Override
	protected String[] getDisplayableFileExtensions() {
		return this
				.retrieveExtensionsFromPreferences(IPreferenceConstants.NAVIGATOR_SOURCE_EXTENSIONS);
	}

}
