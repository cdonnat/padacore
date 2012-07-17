package org.padacore.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.padacore.Messages;

public class NewAdaProjectWizard extends Wizard implements INewWizard {

	private static final String PAGE_NAME = Messages.NewAdaProjectWizard_PageName;
	private static final String WIZARD_NAME = Messages.NewAdaProjectWizard_WizardName;
	private static final String TITLE = Messages.NewAdaProjectWizard_Title;
	private static final String NEW_PROJECT_DESCRIPTION = Messages.NewAdaProjectWizard_Description;

	private WizardNewProjectCreationPage projectCreationPage;

	public NewAdaProjectWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	private NullProgressMonitor createMonitor() {
		return new NullProgressMonitor();
	}

	@Override
	public boolean performFinish() {
		IProject project = projectCreationPage.getProjectHandle();
		IProjectDescription description = project.getWorkspace()
				.newProjectDescription(project.getName());

		try {
			project.create(description, createMonitor());
			project.open(createMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public void addPages() {
		projectCreationPage = new WizardNewProjectCreationPage(PAGE_NAME);
		projectCreationPage.setTitle(TITLE);
		projectCreationPage.setDescription(NEW_PROJECT_DESCRIPTION);
		addPage(projectCreationPage);
	}
}
