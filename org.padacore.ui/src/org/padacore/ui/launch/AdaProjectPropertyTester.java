package org.padacore.ui.launch;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.padacore.core.AdaProjectNature;

public class AdaProjectPropertyTester extends PropertyTester {

	private final static String BELONGS_TO_ADA_PROJECT = "belongsToAdaProject";
	private final static String IS_ADA_PROJECT = "isAdaProject";
	
	//TODO check that project is indeed executable and open

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		boolean testPassed = false;

		if (property.equals(BELONGS_TO_ADA_PROJECT)) {
			assert(receiver instanceof IFile);
			testPassed = this.checkFileBelongstoAdaProject((IFile) receiver);
		} else if (property.equals(IS_ADA_PROJECT)) {
			assert(receiver instanceof IProject);
			testPassed = this
					.checkProjectIsAnAdaProject((IProject) receiver);
		}

		return testPassed;

	}

	/**
	 * Checks if the given project has an Ada nature.
	 * @param selectedProject the selected project for which nature is tested.
	 * @return True if the given project has an Ada nature, False otherwise.
	 */
	private boolean checkProjectIsAnAdaProject(
			IProject selectedProject) {
		boolean hasAdaNature;

		try {
			hasAdaNature = selectedProject
					.hasNature(AdaProjectNature.NATURE_ID);
		} catch (CoreException e) {
			hasAdaNature = false;
		}

		return hasAdaNature;
	}

	/**
	 * Checks if the given file belongs to a project which has an Ada nature.
	 * @param selectedFile the selected file to check.
	 * @return True if the given file belongs to a project with Ada nature, False otherwise.
	 */
	private boolean checkFileBelongstoAdaProject(IFile selectedFile) {

		boolean belongsToAdaProject;

		try {
			belongsToAdaProject = selectedFile.getProject().hasNature(
					AdaProjectNature.NATURE_ID);
		} catch (CoreException e) {
			belongsToAdaProject = false;
		}

		return belongsToAdaProject;
	}

}
