package org.padacore.ui.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.launch.AdaLaunchConstants;

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
			this.getLaunchConfigurationFor(selectedFile).launch(
					ILaunchManager.RUN_MODE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the Ada launch configuration type.
	 * 
	 * @return an ILaunchConfigurationType corresponding to Ada launch
	 *         configuration.
	 */
	private ILaunchConfigurationType getAdaLaunchConfigType() {
		return DebugPlugin
				.getDefault()
				.getLaunchManager()
				.getLaunchConfigurationType(
						AdaLaunchConstants.ID_LAUNCH_ADA_APP);
	}

	/**
	 * Returns a launch configuration for given file. It can be an existing
	 * launch configuration if it already exists or a newly created one.
	 * 
	 * @param selectedFile
	 *            the file for which a launch configuration is returned.
	 * @return a launch configuration useable for given file.
	 */
	private ILaunchConfiguration getLaunchConfigurationFor(IFile selectedFile) {
		ILaunchConfiguration launchConfigForFile = this
				.findExistingLaunchConfigurationFor(selectedFile);

		boolean existingConfigWasFoundForFile = launchConfigForFile != null;

		if (!existingConfigWasFoundForFile) {
			try {
				launchConfigForFile = createNewLaunchConfigurationFor(selectedFile);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		assert(launchConfigForFile != null);

		return launchConfigForFile;
	}

	/**
	 * Creates a new Ada launch configuration for given file.
	 * 
	 * @param selectedFile
	 *            the file for which an Ada launch configuration shall be
	 *            created.
	 * @return the new Ada launch configuration for given file.
	 * @throws CoreException
	 *             if a new Ada launch configuration could not be created
	 */
	private ILaunchConfiguration createNewLaunchConfigurationFor(
			IFile selectedFile) throws CoreException {

		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();

		String newAdaConfigName = launchManager
				.generateLaunchConfigurationName(selectedFile.getName());

		ILaunchConfigurationWorkingCopy newAdaConfigWc = this
				.getAdaLaunchConfigType().newInstance(null, newAdaConfigName);

		newAdaConfigWc.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH,
				selectedFile.getRawLocation().toOSString());

		ILaunchConfiguration newAdaConfig = newAdaConfigWc.doSave();

		return newAdaConfig;
	}

	/**
	 * Finds an existing Ada launch configuration for given file.
	 * 
	 * @param selectedFile
	 *            the file for which a launch configuration has to be found.
	 * @return a launch configuration for given file it one exists, null
	 *         otherwise (or if an error occurred while retrieving the existing
	 *         launch configuration)
	 */
	private ILaunchConfiguration findExistingLaunchConfigurationFor(
			IFile selectedFile) {

		ILaunchConfiguration matchingConfig;

		try {
			ILaunchConfiguration[] existingAdaConfigs = this
					.retrieveExistingAdaLaunchConfigurations();

			matchingConfig = this.findMatchingLaunchConfiguration(
					existingAdaConfigs, selectedFile);
		} catch (CoreException e) {
			// in case of error, fail silently so that a new launch
			// configuration can be created
			matchingConfig = null;
		}

		return matchingConfig;
	}

	/**
	 * Retrieves the existing Ada launch configurations.
	 * 
	 * @return an array containing all the existing Ada launch configurations
	 *         (possibly empty if none exists).
	 */
	private ILaunchConfiguration[] retrieveExistingAdaLaunchConfigurations()
			throws CoreException {

		ILaunchConfigurationType adaConfigType = this.getAdaLaunchConfigType();

		ILaunchConfiguration[] existingAdaConfigs = DebugPlugin.getDefault()
				.getLaunchManager().getLaunchConfigurations(adaConfigType);

		return existingAdaConfigs;
	}

	/**
	 * Tries to find a launch configuration corresponding to the given file
	 * among given launch configurations.
	 * 
	 * @param selectedFile
	 *            the currently selected file.
	 * @param existingAdaLaunchConfigurations
	 *            the existing Ada launch configurations.
	 * 
	 * @return a launch configuration corresponding to the given file or null if
	 *         none exists.
	 */
	private ILaunchConfiguration findMatchingLaunchConfiguration(
			ILaunchConfiguration[] existingAdaLaunchConfigurations,
			IFile selectedFile) throws CoreException {

		ILaunchConfiguration matchingLaunchConfig = null;

		int configIndex = 0;

		while (matchingLaunchConfig == null
				&& configIndex < existingAdaLaunchConfigurations.length) {

			ILaunchConfiguration currentConfig = existingAdaLaunchConfigurations[configIndex];

			if (this.doesLaunchConfigurationMatchFile(currentConfig,
					selectedFile)) {
				matchingLaunchConfig = currentConfig;
			}

			configIndex++;
		}

		return matchingLaunchConfig;
	}

	/**
	 * Checks if the given launch configuration corresponds to given file.
	 * 
	 * @param config
	 *            a launch configuration to match to a file.
	 * @param file
	 *            a file to match to a launch configuration.
	 * @return true if launch configurations corresponds to given file, false
	 *         otherwise.
	 */
	private boolean doesLaunchConfigurationMatchFile(
			ILaunchConfiguration config, IFile file) throws CoreException {

		String fileLocation = file.getRawLocation().toOSString();

		String configExecPath = config.getAttribute(
				AdaLaunchConstants.EXECUTABLE_PATH, "");

		return configExecPath.equals(fileLocation);
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub

	}

}
