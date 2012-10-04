package org.padacore.ui.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.launch.AdaLaunchConfigurationUtils;

public class AdaLaunchConfigurationShortcut implements ILaunchShortcut {

	/**
	 * Checks if the given selection contains only 1 item.
	 * 
	 * @param selection
	 *            the selection for which the number of items is checked.
	 * @return true if the given selection contains only 1 item, false
	 *         otherwise.
	 */
	private boolean isOnlyOneElementSelected(IStructuredSelection selection) {
		return selection.size() == 1;
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			if (this.isOnlyOneElementSelected(structuredSelection)) {
				Object selectedElt = structuredSelection.getFirstElement();

				if (selectedElt instanceof IFile) {
					IFile selectedFile = (IFile) selectedElt;
					this.launchFromFile(selectedFile);

				} else if (selectedElt instanceof IProject) {
					IProject selectedProject = (IProject) selectedElt;
					this.launchFromProject(selectedProject);

				}
			}
		}
	}

	/**
	 * Perform program launch from a selected Ada project.
	 * 
	 * @param selectedProject
	 *            the selected project.
	 */
	// Precondition: selected project is an Ada project
	private void launchFromProject(IProject selectedProject) {
		// TODO perform launch from a project
	}

	/**
	 * Perfom program launch from a selected file.
	 * 
	 * @param selectedFile
	 *            the selected file.
	 * 
	 */
	// Precondition: selected file belongs to Ada project
	private void launchFromFile(IFile selectedFile) {
		try {
			assert (selectedFile.getProject()
					.hasNature(AdaProjectNature.NATURE_ID));
			ILaunchConfiguration configForFile = AdaLaunchConfigurationUtils
					.getLaunchConfigurationFor(selectedFile);
			configForFile.launch(ILaunchManager.RUN_MODE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub

	}

}
