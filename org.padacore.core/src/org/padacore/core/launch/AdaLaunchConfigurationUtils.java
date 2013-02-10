package org.padacore.core.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.utils.ErrorLogger;

public class AdaLaunchConfigurationUtils {
	
	/**
	 * Returns the Ada launch configuration type.
	 * 
	 * @return an ILaunchConfigurationType corresponding to Ada launch
	 *         configuration.
	 */
	private static ILaunchConfigurationType getAdaLaunchConfigType() {
		return DebugPlugin
				.getDefault()
				.getLaunchManager()
				.getLaunchConfigurationType(
						AdaLaunchConstants.ID_LAUNCH_ADA_APP);
	}
	
	/**
	 * Retrieves the existing Ada launch configurations.
	 * 
	 * @return an array containing all the existing Ada launch configurations
	 *         (possibly empty if none exists).
	 */
	private static ILaunchConfiguration[] retrieveExistingAdaLaunchConfigurations()
			throws CoreException {

		ILaunchConfigurationType adaConfigType = getAdaLaunchConfigType();

		ILaunchConfiguration[] existingAdaConfigs = DebugPlugin.getDefault()
				.getLaunchManager().getLaunchConfigurations(adaConfigType);

		return existingAdaConfigs;
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
	private static ILaunchConfiguration findExistingLaunchConfigurationFor(
			IFile selectedFile) {

		ILaunchConfiguration matchingConfig;

		try {
			ILaunchConfiguration[] existingAdaConfigs = AdaLaunchConfigurationUtils
					.retrieveExistingAdaLaunchConfigurations();

			matchingConfig = findMatchingLaunchConfiguration(
					existingAdaConfigs, selectedFile);
		} catch (CoreException e) {
			// in case of error, fail silently so that a new launch
			// configuration can be created
			matchingConfig = null;
		}

		return matchingConfig;
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
	private static ILaunchConfiguration findMatchingLaunchConfiguration(
			ILaunchConfiguration[] existingAdaLaunchConfigurations,
			IFile selectedFile) throws CoreException {

		ILaunchConfiguration matchingLaunchConfig = null;

		int configIndex = 0;

		while (matchingLaunchConfig == null
				&& configIndex < existingAdaLaunchConfigurations.length) {

			ILaunchConfiguration currentConfig = existingAdaLaunchConfigurations[configIndex];

			if (doesLaunchConfigurationMatchFile(currentConfig,
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
	private static boolean doesLaunchConfigurationMatchFile(
			ILaunchConfiguration config, IFile file) throws CoreException {

		String absoluteFileLocation = file.getRawLocation().toOSString();

		String configExecPath = config.getAttribute(
				AdaLaunchConstants.EXECUTABLE_PATH, "");

		return configExecPath.equals(absoluteFileLocation);
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
	private static ILaunchConfiguration createNewLaunchConfigurationFor(
			IFile selectedFile) throws CoreException {

		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();

		String newAdaConfigName = launchManager
				.generateLaunchConfigurationName(selectedFile.getName());

		ILaunchConfigurationWorkingCopy newAdaConfigWc = getAdaLaunchConfigType().newInstance(null, newAdaConfigName);

		newAdaConfigWc.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH,
				selectedFile.getRawLocation().toOSString());

		ILaunchConfiguration newAdaConfig = newAdaConfigWc.doSave();

		return newAdaConfig;
	}
	
	/**
	 * Returns a launch configuration for given file. It can be an existing
	 * launch configuration if it already exists or a newly created one.
	 * 
	 * @param selectedFile
	 *            the file for which a launch configuration is returned.
	 * @return a launch configuration useable for given file.
	 */
	public static ILaunchConfiguration getLaunchConfigurationFor(IFile selectedFile) {
		ILaunchConfiguration launchConfigForFile = findExistingLaunchConfigurationFor(selectedFile);

		boolean existingConfigWasFoundForFile = launchConfigForFile != null;

		if (!existingConfigWasFoundForFile) {
			try {
				launchConfigForFile = createNewLaunchConfigurationFor(selectedFile);
			} catch (CoreException e) {
				ErrorLogger.appendExceptionToErrorLog(e);
			}
		}
		
		assert(launchConfigForFile != null);

		return launchConfigForFile;
	}

}
