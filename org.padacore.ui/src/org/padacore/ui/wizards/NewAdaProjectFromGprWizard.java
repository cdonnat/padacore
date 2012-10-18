package org.padacore.ui.wizards;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.NewAdaProject;

public class NewAdaProjectFromGprWizard extends Wizard implements INewWizard {

	private AdaProjectFromGprCreationPage page;
	
	public NewAdaProjectFromGprWizard() {
		setWindowTitle("New Ada Project from a GPR Project");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		NewAdaProject.CreateFrom(new Path(page.getGprProjectPath()));
		return true;
	}

	@Override
	public void addPages() {
		page = new AdaProjectFromGprCreationPage();
		addPage(page);
	}
}
