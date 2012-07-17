package org.padacore.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.padacore.AdaSourceFile;

public class NewAdaSourceFileWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;
	private AdaSourceFileCreationPage fileCreationPage;
	private IWorkbench workbench;
	private AdaSourceFile sourceFileType;

	public NewAdaSourceFileWizard(AdaSourceFile sourceFileType) {
		super();
		this.sourceFileType = sourceFileType;
		super.setWindowTitle(sourceFileType.getFileTypeDescription());
	}
	
	@Override
	public boolean performFinish() {
		IFile createdFile = this.fileCreationPage.createNewFile();
		IWorkbenchPage activePage = this.workbench.getActiveWorkbenchWindow()
				.getActivePage();
		boolean performFinish = false;

		if (createdFile != null) {

			try {
				IDE.openEditor(activePage, createdFile);
			}

			catch (PartInitException e) {
				// simply don't open editor
			}

			finally {
				performFinish = true;
			}
		}

		return performFinish;
	}

	@Override
	public void addPages() {
		this.fileCreationPage = new AdaSourceFileCreationPage(
				"General information", selection, this.sourceFileType);
		super.addPage(this.fileCreationPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}
}
