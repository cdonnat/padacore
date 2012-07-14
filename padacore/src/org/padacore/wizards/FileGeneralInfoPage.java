package org.padacore.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class FileGeneralInfoPage extends WizardNewFileCreationPage {

	protected FileGeneralInfoPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		super.setFileExtension("ads");
		super.setDescription("Create a new Ada specification file");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
	}
	
	@Override
	protected InputStream getInitialContents() { 
		ByteArrayInputStream initialContents = new ByteArrayInputStream("Ads template".getBytes());
		return initialContents;
	}
}
