package org.padacore.core.launch;

import java.io.File;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.padacore.core.utils.ErrorLog;

/**
 * This class provides a launch configuration based on the executable path.
 * Depending on conditions, a new launch configuration may be created or an
 * existing one retrieved.
 * 
 * @author RS
 * 
 */
public class AdaLaunchConfigurationProvider implements ILaunchConfigurationProvider {

	/**
	 * Returns the Ada launch configuration type.
	 * 
	 * @return an ILaunchConfigurationType corresponding to Ada launch
	 *         configuration.
	 */
	private static ILaunchConfigurationType GetAdaLaunchConfigType() {
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
	private static ILaunchConfiguration[] RetrieveExistingAdaLaunchConfigurations()
			throws CoreException {

		ILaunchConfigurationType adaConfigType = GetAdaLaunchConfigType();

		ILaunchConfiguration[] existingAdaConfigs = DebugPlugin.getDefault()
				.getLaunchManager().getLaunchConfigurations(adaConfigType);

		return existingAdaConfigs;
	}

	/**
	 * Finds an existing Ada launch configuration for given file.
	 * 
	 * @param executableFilePath
	 *            the file for which a launch configuration has to be found.
	 * @return a launch configuration for given file it one exists, null
	 *         otherwise (or if an error occurred while retrieving the existing
	 *         launch configuration)
	 */
	private static ILaunchConfiguration FindExistingLaunchConfigurationFor(
			IPath executableFilePath) {

		ILaunchConfiguration matchingConfig;

		try {
			ILaunchConfiguration[] existingAdaConfigs = RetrieveExistingAdaLaunchConfigurations();

			matchingConfig = FindMatchingLaunchConfiguration(
					existingAdaConfigs, executableFilePath);
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
	 * @param executableFilePath
	 *            the currently selected file.
	 * @param existingAdaLaunchConfigurations
	 *            the existing Ada launch configurations.
	 * 
	 * @return a launch configuration corresponding to the given file or null if
	 *         none exists.
	 */
	private static ILaunchConfiguration FindMatchingLaunchConfiguration(
			ILaunchConfiguration[] existingAdaLaunchConfigurations,
			IPath executableFilePath) throws CoreException {

		ILaunchConfiguration matchingLaunchConfig = null;

		int configIndex = 0;

		while (matchingLaunchConfig == null
				&& configIndex < existingAdaLaunchConfigurations.length) {

			ILaunchConfiguration currentConfig = existingAdaLaunchConfigurations[configIndex];

			if (DoesLaunchConfigurationMatchFile(currentConfig,
					executableFilePath)) {
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
	 * @param executableFilePath
	 *            absolute path of a file to match to a launch configuration.
	 * @return true if launch configurations corresponds to given file, false
	 *         otherwise.
	 */
	private static boolean DoesLaunchConfigurationMatchFile(
			ILaunchConfiguration config, IPath executableFilePath)
			throws CoreException {

		String configExecPath = config.getAttribute(
				AdaLaunchConstants.EXECUTABLE_PATH, "");

		return configExecPath.equals(executableFilePath.toOSString());
	}

	/**
	 * Creates a new Ada launch configuration for given file.
	 * 
	 * @param executableFilePath
	 *            absolute path of the file for which an Ada launch
	 *            configuration shall be created.
	 * @return the new Ada launch configuration for given file.
	 * @throws CoreException
	 *             if a new Ada launch configuration could not be created
	 */
	private static ILaunchConfiguration CreateNewLaunchConfigurationFor(
			IPath executableFilePath) throws CoreException {

		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();

		String newAdaConfigName = launchManager
				.generateLaunchConfigurationName(new File(executableFilePath
						.toOSString()).getName());

		ILaunchConfigurationWorkingCopy newAdaConfigWc = GetAdaLaunchConfigType()
				.newInstance(null, newAdaConfigName);

		newAdaConfigWc.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH,
				executableFilePath.toOSString());

		ILaunchConfiguration newAdaConfig = newAdaConfigWc.doSave();

		return newAdaConfig;
	}

	/* (non-Javadoc)
	 * @see org.padacore.core.launch.ILaunchConfigurationProvider#getLaunchConfigurationFor(org.eclipse.core.runtime.IPath)
	 */
	@Override
	public ILaunchConfiguration getLaunchConfigurationFor(
			IPath executableFilePath) {
		ILaunchConfiguration launchConfigForFile = FindExistingLaunchConfigurationFor(executableFilePath);

		boolean existingConfigWasFoundForFile = launchConfigForFile != null;

		if (!existingConfigWasFoundForFile) {
			try {
				launchConfigForFile = CreateNewLaunchConfigurationFor(executableFilePath);
			} catch (CoreException e) {
				ErrorLog.appendException(e);
			}
		}

		Assert.isNotNull(launchConfigForFile);

		return launchConfigForFile;
	}

}
