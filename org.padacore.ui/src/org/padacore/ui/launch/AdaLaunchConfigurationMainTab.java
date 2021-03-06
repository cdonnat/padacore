package org.padacore.ui.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.padacore.core.launch.AdaLaunchConstants;
import org.padacore.core.utils.ErrorLog;

public class AdaLaunchConfigurationMainTab extends AbstractLaunchConfigurationTab {

	private Label progLabel;
	private Text progText;
	private Composite controlsComposite;

	@Override
	public void createControl(Composite parent) {
		controlsComposite = new Composite(parent, SWT.NONE);
		controlsComposite.setLayout(new GridLayout());
		controlsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		setControl(controlsComposite);
		
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		progLabel = new Label(controlsComposite, SWT.NONE);
		progLabel.setText("Executable path");
		progLabel.setLayoutData(gd);
		progText = new Text(controlsComposite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		progText.setLayoutData(gd);
		progText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			progText.setText(configuration.getAttribute(AdaLaunchConstants.EXECUTABLE_PATH, ""));

		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(AdaLaunchConstants.EXECUTABLE_PATH, progText.getText());
	}

	@Override
	public String getName() {
		return "Main";
	}

}
