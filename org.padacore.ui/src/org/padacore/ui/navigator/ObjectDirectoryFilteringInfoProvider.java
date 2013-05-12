package org.padacore.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.padacore.core.project.IAdaProject;
import org.padacore.ui.preferences.IPreferenceConstants;

/**
 * This class defines implementation of ProjectDirectoryInfoProvider for Ada
 * project object directory.
 * 
 * @author RS
 * 
 */
public final class ObjectDirectoryFilteringInfoProvider extends ProjectDirectoryFilteringInfoProvider {

	public ObjectDirectoryFilteringInfoProvider(IAdaProject adaProject) {
		super(adaProject);
	}

	@Override
	protected List<IPath> getProjectDirectoryPaths() {
		List<IPath> projectDir = new ArrayList<>(1);
		projectDir.add(this.adaProject.getObjectDirectoryPath());

		return projectDir;
	}

	@Override
	protected String[] getDisplayableFileExtensions() {
		return this
				.retrieveExtensionsFromPreferences(IPreferenceConstants.NAVIGATOR_OBJECT_EXTENSIONS);
	}

}