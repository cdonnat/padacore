package org.padacore.core.launch;

import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;

public interface ILaunchConfigurationProvider {

	/**
	 * Returns a launch configuration for given file. It can be an existing
	 * launch configuration if it already exists or a newly created one.
	 * 
	 * @param executableFilePath
	 *            absolute path of the file for which a launch configuration is
	 *            returned.
	 * @return a launch configuration useable for given file.
	 */
	public abstract ILaunchConfiguration getLaunchConfigurationFor(
			IPath executableFilePath);

}