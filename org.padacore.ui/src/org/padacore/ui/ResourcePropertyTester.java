package org.padacore.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.padacore.core.project.AbstractAdaProjectAssociationManager;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;

public class ResourcePropertyTester extends PropertyTester {

	private final static String IS_EXECUTABLE_IN_ADA_PROJECT = "isExecutableInAdaProject";
	private final static String IS_EXECUTABLE_ADA_PROJECT = "isExecutableAdaProject";
	private final static String IS_ADA_EDITOR = "isAdaExecutableEditor";
	private final static String IS_IN_ADA_PROJECT = "isInAdaProject";

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		boolean testPassed = false;

		if (property.equals(IS_EXECUTABLE_IN_ADA_PROJECT)) {
			Assert.isLegal(receiver instanceof IFile);
			testPassed = this.isFileAnExecutableInAdaProject((IFile) receiver);
		} else if (property.equals(IS_EXECUTABLE_ADA_PROJECT)) {
			Assert.isLegal(receiver instanceof IProject);
			testPassed = this
					.isProjectAnExecutableAdaProject((IProject) receiver);
		} else if (property.equals(IS_ADA_EDITOR)) {
			Assert.isLegal(receiver instanceof IEditorPart);
			testPassed = this
					.doesEditorPointsToAnExecutableInAdaProject((IEditorPart) receiver);
		} else if (property.equals(IS_IN_ADA_PROJECT)) {
			Assert.isLegal(receiver instanceof IResource);
			testPassed = this.isResourceInAdaProject((IResource) receiver);
		}

		return testPassed;

	}

	/**
	 * Checks if the given resource belongs to a project with Ada nature.
	 * 
	 * @param resource
	 *            the selected resource to check.
	 * @return True if resource belongs to a project with Ada nature, False
	 *         otherwise.
	 */
	private boolean isResourceInAdaProject(IResource resource) {
		IProject enclosingProject = resource.getProject();
		boolean isInAdaProject = false;

		if (enclosingProject.isOpen()) {
			try {
				isInAdaProject = enclosingProject
						.hasNature(AdaProjectNature.NATURE_ID);
			} catch (CoreException e) {
				isInAdaProject = false;
			}
		}

		return isInAdaProject;

	}

	/**
	 * Checks if the given editor points to an executable of a project with Ada
	 * nature.
	 * 
	 * @param selectedEditor
	 *            the selected editor to check.
	 * @return True if the given editor points to an executable of a project
	 *         with Ada nature, False otherwise.
	 */
	private boolean doesEditorPointsToAnExecutableInAdaProject(
			IEditorPart selectedEditor) {
		IEditorInput editorInput = selectedEditor.getEditorInput();
		boolean testPassed = false;

		if (editorInput instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
			IFile editedFile = fileEditorInput.getFile();

			testPassed = this.isFileAnExecutableInAdaProject(editedFile);
		}
		return testPassed;
	}

	/**
	 * Checks if the given project has an Ada nature, is open and is executable.
	 * 
	 * @param selectedProject
	 *            the selected project which is tested.
	 * @return True if the given project is open, has an Ada nature and is
	 *         executable, False otherwise.
	 */
	private boolean isProjectAnExecutableAdaProject(IProject selectedProject) {
		boolean isAnExecutableOpenAdaProject = false;

		IAdaProject associatedAdaProject;

		if (selectedProject.isOpen()) {
			try {
				if (selectedProject.hasNature(AdaProjectNature.NATURE_ID)) {

					associatedAdaProject = AbstractAdaProjectAssociationManager
							.GetAssociatedAdaProject(selectedProject);
					isAnExecutableOpenAdaProject = associatedAdaProject
							.isExecutable();
				}
			} catch (CoreException e) {
				isAnExecutableOpenAdaProject = false;
			}
		}

		return isAnExecutableOpenAdaProject;
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
	private boolean isFileAnExecutableInAdaProject(IFile selectedFile) {

		boolean belongsToExecutableAdaProject = this
				.isProjectAnExecutableAdaProject(selectedFile.getProject());
		boolean selectedFileIsAnExecutableOfProject = false;

		if (belongsToExecutableAdaProject) {
			PropertiesManager propertiesManager = new PropertiesManager(
					selectedFile.getProject());
			IAdaProject adaProject = propertiesManager.getAdaProject();

			selectedFileIsAnExecutableOfProject = adaProject
					.getExecutableNames().contains(selectedFile.getName())
					|| adaProject.getExecutableSourceNames().contains(
							selectedFile.getName());
		}

		return selectedFileIsAnExecutableOfProject;
	}
}
