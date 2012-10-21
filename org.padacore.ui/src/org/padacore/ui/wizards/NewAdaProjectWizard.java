package org.padacore.ui.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.NewAdaProject;
import org.padacore.ui.Messages;

public class NewAdaProjectWizard extends Wizard implements INewWizard {

	private static final String PAGE_NAME = Messages.NewAdaProjectWizard_PageName;
	private static final String WIZARD_NAME = Messages.NewAdaProjectWizard_WizardName;
	private static final String TITLE = Messages.NewAdaProjectWizard_Title;
	private static final String NEW_PROJECT_DESCRIPTION = Messages.NewAdaProjectWizard_Description;

	private AdaProjectCreationPage projectCreationPage;

	public NewAdaProjectWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		IPath projectLocation = projectCreationPage.useDefaults() ? null : projectCreationPage
				.getLocationPath();

		NewAdaProject.Create(projectCreationPage.getProjectHandle().getName(), projectLocation,
				projectCreationPage.addMainProcedure());

		return true;
	}

	@Override
	public void addPages() {
		projectCreationPage = new AdaProjectCreationPage(PAGE_NAME);
		projectCreationPage.setTitle(TITLE);
		projectCreationPage.setDescription(NEW_PROJECT_DESCRIPTION);
		addPage(projectCreationPage);
	}
}
