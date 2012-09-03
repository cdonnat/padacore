package org.padacore.launch;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.padacore.utils.ProcessDisplay;

public class AdaLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor monitor) throws CoreException {

		String cmd = configuration.getAttribute(AdaLaunchConstants.ExecutablePath, "");
		Runtime rt = Runtime.getRuntime();

		try {
			Process process = rt.exec(cmd);
			ProcessDisplay.DisplayErrors(process);
			ProcessDisplay.DisplayWarnings(process);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
