package org.padacore.ui.launch;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.padacore.core.project.AbstractAdaProjectAssociationManager;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;

public class AdaProjectPropertyTester extends PropertyTester {

	private final static String BELONGS_TO_ADA_PROJECT = "belongsToAdaProject";
	private final static String IS_ADA_PROJECT = "isAdaProject";

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		boolean testPassed = false;

		if (property.equals(BELONGS_TO_ADA_PROJECT)) {
			Assert.isLegal(receiver instanceof IFile);
			testPassed = this
					.checkFileIsAnExecutableInAdaProject((IFile) receiver);
		} else if (property.equals(IS_ADA_PROJECT)) {
			Assert.isLegal(receiver instanceof IProject);
			testPassed = this
					.checkProjectIsAnExecutableAdaProject((IProject) receiver);
		}

		return testPassed;

	}

	/**
	 * Checks if the given project: + has an Ada nature. + is open. + is
	 * executable.
	 * 
	 * @param selectedProject
	 *            the selected project which is tested.
	 * @return True if the given project is open, has an Ada nature and is
	 *         executable, False otherwise.
	 */
	private boolean checkProjectIsAnExecutableAdaProject(
			IProject selectedProject) {
		boolean hasAdaNatureAndIsOpen = false;
		boolean isAnExecutableProject = false;

		IAdaProject associatedAdaProject;

		try {
			hasAdaNatureAndIsOpen = selectedProject.isOpen()
					&& selectedProject.hasNature(AdaProjectNature.NATURE_ID);

			if (hasAdaNatureAndIsOpen) {
				associatedAdaProject = AbstractAdaProjectAssociationManager
						.GetAssociatedAdaProject(selectedProject);
				isAnExecutableProject = associatedAdaProject.isExecutable();
			}
		} catch (CoreException e) {
		}

		return hasAdaNatureAndIsOpen && isAnExecutableProject;
	}

	/**
	 * Checks if the given file correspond to an executable of a project with
	 * Ada nature.
	 * 
	 * @param selectedFile
	 *            the selected file to check.
	 * @return True if the given file correspond to an executable of a project
	 *         with Ada nature, False otherwise.
	 */
	private boolean checkFileIsAnExecutableInAdaProject(IFile selectedFile) {

		boolean belongsToExecutableAdaProject = this
				.checkProjectIsAnExecutableAdaProject(selectedFile.getProject());
		boolean selectedFileIsAnExecutableOfProject = false;

		if (belongsToExecutableAdaProject) {
			PropertiesManager propertiesManager = new PropertiesManager(
					selectedFile.getProject());
			IAdaProject adaProject = propertiesManager.getAdaProject();

			selectedFileIsAnExecutableOfProject = adaProject
					.getExecutableSourceNames()
					.contains(selectedFile.getName())
					|| adaProject.getExecutableNames().contains(
							selectedFile.getName());
		}

		return selectedFileIsAnExecutableOfProject;
	}

}
