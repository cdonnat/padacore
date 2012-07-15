package org.padacore.wizards;

import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.padacore.IAdaSourceFile;

public class AdaSourceFileCreationPage extends WizardNewFileCreationPage {

	private IAdaSourceFile sourceFileType;

	/**
	 * Creates a new wizard page for Ada source file creation.
	 * @param pageName the name of the page
	 * @param selection the current selection
	 * @param sourceFileType the type of Ada source file to create
	 */
	public AdaSourceFileCreationPage(String pageName,
			IStructuredSelection selection, IAdaSourceFile sourceFileType) {
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
	
	/**
	 * Displays an error message if the input file name is considered as incorrect.
	 */
	private void displayErrorMsgForIncorrectFileName() {
		String errorMsgIfDotInFileName = "Filename cannot contain any dot.";

			if (this.getErrorMessage() == null) {
				this.setErrorMessage(errorMsgIfDotInFileName);
			} else {
				this.setErrorMessage(this.getErrorMessage()
						+ "\n" + errorMsgIfDotInFileName);
			}
	}
	
	/**
	 * Checks if the input file name is correct.
	 * @return True if the file name is correct, False otherwise.
	 */
	private boolean isFileNameCorrect() {
		boolean fileNameIsEmpty = this.getFileName() == null || this.getFileName().length() == 0;
		boolean fileNameContainsNoDot = fileNameIsEmpty
				|| (this.getFileName().indexOf(".") == this.getFileName()
						.length() - this.getFileExtension().length() - 1);
		
		return fileNameIsEmpty || fileNameContainsNoDot;
	}

	@Override
	protected boolean validatePage() {
		boolean pageIsValid = super.validatePage();
		
		if (!isFileNameCorrect()) {
			displayErrorMsgForIncorrectFileName();
		}

		pageIsValid = pageIsValid && isFileNameCorrect();

		return pageIsValid;
	}
}
