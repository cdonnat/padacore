package org.padacore.ui.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.padacore.core.launch.AdaApplicationLauncher;
import org.padacore.core.launch.AdaLaunchConfigurationProvider;
import org.padacore.core.launch.ApplicationLauncherJobFactory;
import org.padacore.core.launch.IApplicationLauncher;
import org.padacore.core.project.PropertiesManager;

/**
 * This class determines the proper Ada application launcher to invoke according
 * to selected element.
 * 
 * @author RS
 * 
 */
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

	/**
	 * Returns the application launcher corresponding to given project.
	 * 
	 * @param project
	 *            the project for which an application launcher is requested.
	 * @return an application launcher for the project.
	 */
	private IApplicationLauncher getApplicationLauncherFor(IProject project) {
		PropertiesManager propertiesManager = new PropertiesManager(project);
		IApplicationLauncher appLauncher = new AdaApplicationLauncher(
				propertiesManager.getAdaProject(),
				new AdaLaunchConfigurationProvider(),
				new ApplicationLauncherJobFactory(project));

		return appLauncher;

	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			if (IsOnlyOneElementSelected(structuredSelection)) {
				Object selectedElt = structuredSelection.getFirstElement();
				IApplicationLauncher adaAppLauncher;

				if (selectedElt instanceof IFile) {
					IFile selectedFile = (IFile) selectedElt;

					adaAppLauncher = this
							.getApplicationLauncherFor(selectedFile
									.getProject());
					adaAppLauncher.performLaunchFromFile(selectedFile.getLocation());

				} else if (selectedElt instanceof IProject) {
					final IProject selectedProject = (IProject) selectedElt;

					ExecutableSelector execSelector = new ExecutableSelector(
							selectedProject);

					if (execSelector.isExecutableSelected()) {
						adaAppLauncher = this
								.getApplicationLauncherFor(selectedProject);

						adaAppLauncher.performLaunchFromFile(execSelector
								.getSelectedExecutable());
					}
				}
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		Assert.isLegal(editor.getEditorInput() instanceof IFileEditorInput);

		IFileEditorInput fileEditorInput = (IFileEditorInput) editor
				.getEditorInput();
		IFile editedFile = fileEditorInput.getFile();

		IApplicationLauncher adaAppLauncher = this
				.getApplicationLauncherFor(editedFile.getProject());
		adaAppLauncher.performLaunchFromFile(editedFile.getLocation());

	}

}
