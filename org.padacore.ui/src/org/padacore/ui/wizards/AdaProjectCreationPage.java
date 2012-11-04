package org.padacore.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.padacore.ui.Messages;

public class AdaProjectCreationPage extends WizardNewProjectCreationPage {
	
	//FIXME project name shall be validated to make sure it's not an Ada keyword

	private static final String ADD_MAIN_PROCEDURE = Messages.AdaProjectCreationPage_AddMainProcedure;
	
	private Button checkBox;

	public AdaProjectCreationPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		addMainCheckBox(parent);
	}

	/**
	 * Add checkbox for main procedure.
	 * @param parent
	 */
	private void addMainCheckBox(Composite parent) {
		// project specification group
		Composite group = new Composite((Composite) getControl(), SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		checkBox = new Button (group, SWT.CHECK);
		checkBox.setText(ADD_MAIN_PROCEDURE);		
	}

	/**
	 * Returns the value of the diagram type field with leading and trailing
	 * spaces removed.
	 * 
	 * @return True if the main procedure shall be add the project.
	 */
	public boolean addMainProcedure () {
		return checkBox.getSelection();
	}
}
