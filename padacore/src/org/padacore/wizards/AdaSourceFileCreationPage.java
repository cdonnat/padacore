package org.padacore.wizards;

import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.padacore.AdaSourceFile;

public class AdaSourceFileCreationPage extends WizardNewFileCreationPage {

	private AdaSourceFile sourceFileType;

	/**
	 * Creates a new wizard page for Ada source file creation.
	 * @param pageName the name of the page
	 * @param selection the current selection
	 * @param sourceFileType the type of Ada source file to create
	 */
	public AdaSourceFileCreationPage(String pageName,
			IStructuredSelection selection, AdaSourceFile sourceFileType) {
		super(pageName, selection);
		this.sourceFileType = sourceFileType;
		super.setFileExtension(sourceFileType.getExtension());
		super.setDescription("Create a new "
				+ sourceFileType.getFileTypeDescription());
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
	}

	@Override
	protected InputStream getInitialContents() {
		return this.sourceFileType.getTemplate();
	}
	
	@Override
	public void setFileName(String value) {
		super.setFileName(value);
	}
	
	/**
	 * Displays an error message if the input file name is considered as incorrect.
	 */
	private void displayErrorMsgForIncorrectFileName() {
		String errorMsgIfInvalidFileName = "Filename is invalid (it shall contain only letters, digits, underscores and dashes and shall shart with a letter or digit.";

		this.setErrorMessage(errorMsgIfInvalidFileName);
	}
	
	@Override
	protected boolean validatePage() {
		boolean pageIsValid = super.validatePage();
		boolean fileNameIsValid = false;
		
		if (pageIsValid) {
			fileNameIsValid = this.sourceFileType.isFileNameValid(this.getFileName()); 
			
			if (!fileNameIsValid){
				displayErrorMsgForIncorrectFileName();
			}
		}
		
		pageIsValid = pageIsValid && fileNameIsValid;

		return pageIsValid;
	}
}
