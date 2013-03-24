package org.padacore.core.launch;

import org.eclipse.core.resources.IFile;

public interface IApplicationLauncher {

	/**
	 * Perfom launch of the application for the given file. File may be a source
	 * file used to produce an executable file or an executable file.
	 * 
	 * @param file
	 *            the file for which application shall be launched.
	 */
	public void performLaunchFromFile(IFile file);
}
