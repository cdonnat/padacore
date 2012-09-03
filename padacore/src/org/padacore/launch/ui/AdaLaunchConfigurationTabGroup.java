package org.padacore.launch.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class AdaLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	public AdaLaunchConfigurationTabGroup() {
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new AdaLaunchConfigurationMainTab(), new CommonTab() };
		setTabs(tabs);
	}

}
