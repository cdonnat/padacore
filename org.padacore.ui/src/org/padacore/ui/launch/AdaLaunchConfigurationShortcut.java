package org.padacore.ui.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.padacore.core.AbstractAdaProjectAssociationManager;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.IAdaProject;
import org.padacore.core.launch.AdaLaunchConfigurationUtils;
import org.padacore.core.utils.ErrorLogger;

public class AdaLaunchConfigurationShortcut implements ILaunchShortcut {

	/**
	 * Checks if the given selection contains only 1 item.
	 * 
	 * @param selection
	 *            the selection for which the number of items is checked.
	 * @return true if the given selection contains only 1 item, false
	 *         otherwise.
	 */
	private static boolean IsOnlyOneElementSelected(
			IStructuredSelection selection) {
		return selection.size() == 1;
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			if (IsOnlyOneElementSelected(structuredSelection)) {
				Object selectedElt = structuredSelection.getFirstElement();

				if (selectedElt instanceof IFile) {
					IFile selectedFile = (IFile) selectedElt;
					LaunchFromFile(selectedFile);

				} else if (selectedElt instanceof IProject) {
					IProject selectedProject = (IProject) selectedElt;
					LaunchFromProject(selectedProject);

				}
			}
		}
	}

	/**
	 * Perform program launch from a selected Ada project.
	 * 
	 * @param selectedProject
	 *            the selected project.
	 * @pre selectedProject is an Ada project and has an associated GPR project.
	 */
	private static void LaunchFromProject(IProject selectedProject) {
		try {
			Assert.isLegal(selectedProject
					.hasNature(AdaProjectNature.NATURE_ID));
			Assert.isLegal(AbstractAdaProjectAssociationManager
					.GetAssociatedAdaProject(selectedProject) != null);

			IAdaProject associatedAdaProject = AbstractAdaProjectAssociationManager
					.GetAssociatedAdaProject(selectedProject);

			if (associatedAdaProject.getExecutableNames().size() == 1) {

				IPath executablePath = new Path(
						associatedAdaProject.getExecutableDirectoryPath()
								+ System.getProperty("file.separator")
								+ associatedAdaProject.getExecutableNames()
										.get(0));

				// TODO what shall be do when executable does not exist: request
				// build or display error ?
				LaunchExecutableAtPath(executablePath);

			} else {
				ErrorLogger
						.appendMessageToErrorLog(
								"Launching a project with multiple executables is not implemented yet",
								IStatus.WARNING);
			}

		} catch (CoreException e) {
			ErrorLogger.appendExceptionToErrorLog(e);
		}
	}

	/**
	 * Perfom program launch from a selected file.
	 * 
	 * @param selectedFile
	 *            the selected file.
	 * @pre selected file belongs to Ada project
	 * 
	 */
	private static void LaunchFromFile(IFile selectedFile) {
		try {
			Assert.isLegal(selectedFile.getProject().hasNature(
					AdaProjectNature.NATURE_ID));

		} catch (CoreException e) {
			ErrorLogger.appendExceptionToErrorLog(e);
		}

		LaunchExecutableAtPath(selectedFile.getRawLocation());
	}

	/**
	 * Launches the executable found at given absolute path.
	 * 
	 * @param absoluteExecPath
	 *            absolute path of the executable to launch.
	 */
	private static void LaunchExecutableAtPath(IPath absoluteExecPath) {
		ILaunchConfiguration configForFile = AdaLaunchConfigurationUtils
				.GetLaunchConfigurationFor(absoluteExecPath);

		try {
			configForFile.launch(ILaunchManager.RUN_MODE, null);
		} catch (CoreException e) {
			ErrorLogger.appendExceptionToErrorLog(e);
		}

	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO implement launch from editor
	}

}
