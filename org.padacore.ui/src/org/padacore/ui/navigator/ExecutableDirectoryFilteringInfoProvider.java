package org.padacore.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.padacore.core.project.IAdaProject;
import org.padacore.ui.preferences.IPreferenceConstants;

/**
 * This class defines implementation of ProjectDirectoryInfoProvider for Ada
 * project executable directory.
 * 
 * @author RS
 * 
 */
public class ExecutableDirectoryFilteringInfoProvider extends
		ProjectDirectoryFilteringInfoProvider {

	public ExecutableDirectoryFilteringInfoProvider(IAdaProject adaProject) {
		super(adaProject);
	}

	@Override
	protected List<IPath> getProjectDirectoryPaths() {
		List<IPath> execDir = new ArrayList<>();

		if (this.adaProject.isExecutable()) {
			execDir.add(this.adaProject.getExecutableDirectoryPath());
		}

		return execDir;
	}

	@Override
	protected String[] getDisplayableFileExtensions() {
		return this
				.retrieveExtensionsFromPreferences(IPreferenceConstants.NAVIGATOR_EXEC_EXTENSIONS);
	}

}