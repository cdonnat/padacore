package org.padacore.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.padacore.utils.ExternalProcessJob;

public class AdaLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor monitor) throws CoreException {

		String cmd = configuration.getAttribute(AdaLaunchConstants.EXECUTABLE_PATH, "");

		ExternalProcessJob.runWithDefaultOutput(cmd, cmd);
	}

}
