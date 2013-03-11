package org.padacore.ui.wizards;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.gnat.ImportProjectCmd;

/**
 * This class defines a wizard which enables user to import an existing GPR
 * project file as an Eclipse project.
 * 
 * @author rs
 * 
 */
public class AdaProjectFromGprWizard extends Wizard implements IImportWizard {

	private AdaProjectFromGprCreationPage page;

	public AdaProjectFromGprWizard() {
		setWindowTitle("Import existing GPR Project");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		this.createProjectFromGprProjectFileWithAdaNature();

		return true;
	}

	/**
	 * Creates a new Eclipse project with Ada nature based on the GPR project
	 * file found in user-defined directory.
	 */
	private void createProjectFromGprProjectFileWithAdaNature() {
		Job job = new ImportProjectCmd(new Path(this.page.getGprProjectPath()));
		job.schedule();
	}

	@Override
	public void addPages() {
		page = new AdaProjectFromGprCreationPage();
		addPage(page);
	}
}
