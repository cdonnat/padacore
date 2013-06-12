package org.padacore.ui.navigator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.utils.ErrorLog;

public class AdaProjectDirectoryPropertyTester extends PropertyTester {

	private final static String SOURCE_DIRECTORY_PROPERTY = "isASourceDirectory";

	private boolean isASourceDirectoryOfAdaProject(IFolder folder) {
		IProject project = folder.getProject();
		boolean isASourceDirOfAdaProject = false;

		try {
			if (project.hasNature(AdaProjectNature.NATURE_ID)) {

				PropertiesManager propertiesManager = new PropertiesManager(
						project);
				IAdaProject adaProject = propertiesManager.getAdaProject();

				isASourceDirOfAdaProject = adaProject
						.getSourceDirectoriesPaths().contains(
								folder.getLocation());

			}
		} catch (CoreException e) {
			isASourceDirOfAdaProject = false;
			ErrorLog.appendException(e);
		}

		return isASourceDirOfAdaProject;
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		boolean testPassed = false;

		if (property.equals(SOURCE_DIRECTORY_PROPERTY)) {
			Assert.isLegal(receiver instanceof IFolder);
			testPassed = this
					.isASourceDirectoryOfAdaProject((IFolder) receiver);
		}

		return testPassed;
	}
}
