package org.padacore.ui.wizards;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.padacore.ui.Messages;

public class AdaProjectFromGprCreationPage extends WizardPage {

	private Text projectNameField;
	private Text locationPathField;
	private Button browseButton;

	private Listener locationModifyListener = new Listener() {
		public void handleEvent(Event e) {
			setPageComplete(validatePage());
			if (!isPageComplete()) {
				projectNameField.setText (""); //$NON-NLS-1$
			}
		}
	};

	// constants
	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	/**
	 * Creates a new project creation wizard page.
	 * 
	 */
	public AdaProjectFromGprCreationPage() {
		super("wizardExternalProjectPage"); //$NON-NLS-1$
		setPageComplete(false);
		setTitle(Messages.AdaProjectFromGprCreationPage_Title);
		setDescription(Messages.AdaProjectFromGprCreationPage_Description);

	}

	@Override
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NULL);

		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());

		createProjectNameGroup(composite);
		createProjectLocationGroup(composite);
		validatePage();
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
	}

	/**
	 * Creates the project location specification controls.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private final void createProjectLocationGroup(Composite parent) {

		// project specification group
		Composite projectGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectGroup.setFont(parent.getFont());

		// new project label
		Label projectContentsLabel = new Label(projectGroup, SWT.NONE);
		projectContentsLabel.setText(Messages.AdaProjectFromGprCreationPage_GPRProject);
		projectContentsLabel.setFont(parent.getFont());

		createUserSpecifiedProjectLocationGroup(projectGroup);
	}

	/**
	 * Creates the project name specification controls.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private final void createProjectNameGroup(Composite parent) {

		Font dialogFont = parent.getFont();

		// project specification group
		Composite projectGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		projectGroup.setFont(dialogFont);
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new project label
		Label projectLabel = new Label(projectGroup, SWT.NONE);
		projectLabel.setText(Messages.AdaProjectFromGprCreationPage_Project);
		projectLabel.setFont(dialogFont);

		// new project name entry field
		projectNameField = new Text(projectGroup, SWT.BORDER | SWT.READ_ONLY);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		projectNameField.setLayoutData(data);
		projectNameField.setFont(dialogFont);
		projectNameField.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
	}

	/**
	 * Creates the project location specification controls.
	 * 
	 * @param projectGroup
	 *            the parent composite
	 */
	private void createUserSpecifiedProjectLocationGroup(Composite projectGroup) {

		Font dialogFont = projectGroup.getFont();

		// project location entry field
		this.locationPathField = new Text(projectGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		this.locationPathField.setLayoutData(data);
		this.locationPathField.setFont(dialogFont);

		// browse button
		this.browseButton = new Button(projectGroup, SWT.PUSH);
		this.browseButton.setText(Messages.AdaProjectFromGprCreationPage_Browse);
		this.browseButton.setFont(dialogFont);
		setButtonLayoutData(this.browseButton);

		this.browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handleLocationBrowseButtonPressed();
			}
		});

		locationPathField.addListener(SWT.Modify, locationModifyListener);
	}

	/**
	 * Returns the value of the project location field with leading and trailing
	 * spaces removed.
	 * 
	 * @return the project location directory in the field
	 */
	private String getProjectLocationFieldValue() {
		return locationPathField.getText().trim();
	}

	/**
	 * Open an appropriate directory browser
	 */
	private void handleLocationBrowseButtonPressed() {
		FileDialog dialog = new FileDialog(locationPathField.getShell(), SWT.SHEET);
		dialog.setFilterExtensions(new String[] { "*.gpr" }); //$NON-NLS-1$

		String selectedGprProject = dialog.open();
		if (selectedGprProject != null) {
			locationPathField.setText(selectedGprProject);
			this.projectNameField.setText(new File(selectedGprProject).getName()
					.replace(".gpr", "")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Returns whether this page's controls currently all contain valid values.
	 * 
	 * @return <code>true</code> if all controls are valid, and
	 *         <code>false</code> if at least one is invalid
	 */
	private boolean validatePage() {

		String locationFieldContents = getProjectLocationFieldValue();

		if (locationFieldContents.equals("")) { //$NON-NLS-1$
			setErrorMessage(null);
			return false;
		}

		IPath path = new Path(""); //$NON-NLS-1$
		if (!path.isValidPath(locationFieldContents)) {
			setErrorMessage(Messages.AdaProjectFromGprCreationPage_BadPath);
			return false;
		}

		File file = new File(locationFieldContents);

		if (!file.exists() || file.isDirectory()
				|| (file.isFile() && !file.getName().contains(".gpr"))) { //$NON-NLS-1$
			setErrorMessage(Messages.AdaProjectFromGprCreationPage_NoGPR);
			return false;
		}
		// FIXME : Handle the case when the project is already in the workspace.

		setErrorMessage(null);
		setMessage(null);
		return true;
	}

	/*
	 * see @DialogPage.setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			this.locationPathField.setFocus();
		}
	}

	public String getGprProjectPath () {
		Assert.isLegal(isPageComplete());
		return locationPathField.getText();
	}
}
