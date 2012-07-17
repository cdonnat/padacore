package org.padacore.preferences;

import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.padacore.Messages;

public class AdaPreferences implements IWorkbenchPreferencePage {

	private String description;
	private String title;
	private Control control;

	@Override
	public Point computeSize() {
		return this.control.getSize();
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public boolean okToLeave() {
		return true;
	}

	@Override
	public boolean performCancel() {
		return true;
	}

	@Override
	public boolean performOk() {
		return true;
	}

	@Override
	public void setContainer(IPreferencePageContainer preferencePageContainer) {
	}

	@Override
	public void setSize(Point size) {
		this.control.setSize(size);
	}

	@Override
	public void createControl(Composite parent) {
		GridLayout layout = new GridLayout();
		Composite adaPreferences = new Composite(parent, SWT.NONE);
		Label adaInfo = new Label(adaPreferences, SWT.NONE);
		
		adaPreferences.setLayout(layout);
		adaInfo.setText(Messages.AdaPreferences_Description);
		
		this.control = adaPreferences;
	}

	@Override
	public void dispose() {
		this.control.dispose();
	}

	@Override
	public Control getControl() {
		return this.control;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public String getMessage() {
		return Messages.AdaPreferences_Title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void performHelp() {

	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setImageDescriptor(ImageDescriptor image) {

	}

	@Override
	public void setTitle(String title) {
		this.title = title;

	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.control.pack();
		}
		this.control.setVisible(visible);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
