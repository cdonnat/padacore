package org.padacore.ui.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.padacore.core.launch.AdaApplicationLauncher;
import org.padacore.core.launch.AdaLaunchConfigurationProvider;
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

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			if (IsOnlyOneElementSelected(structuredSelection)) {
				Object selectedElt = structuredSelection.getFirstElement();
				IApplicationLauncher adaAppLauncher;
				PropertiesManager propertiesManager;

				if (selectedElt instanceof IFile) {
					IFile selectedFile = (IFile) selectedElt;
					propertiesManager = new PropertiesManager(
							selectedFile.getProject());
					adaAppLauncher = new AdaApplicationLauncher(
							propertiesManager.getAdaProject(),
							new AdaLaunchConfigurationProvider());

					adaAppLauncher.performLaunchFromFile(selectedFile);

				} else if (selectedElt instanceof IProject) {
					IProject selectedProject = (IProject) selectedElt;
					propertiesManager = new PropertiesManager(selectedProject);
					adaAppLauncher = new AdaApplicationLauncher(
							propertiesManager.getAdaProject(),
							new AdaLaunchConfigurationProvider());

					adaAppLauncher.performLaunchFromProject(selectedProject);

				}
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO implement launch from editor
	}

}
