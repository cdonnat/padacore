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

public class NewAdaProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage projectCreationPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
	
	private NullProgressMonitor createMonitor() {
		return new NullProgressMonitor();
	}

	@Override
	public boolean performFinish() {
		IProject            project     = projectCreationPage.getProjectHandle();
		IProjectDescription description = project.getWorkspace().newProjectDescription(project.getName());
		
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
		projectCreationPage = new WizardNewProjectCreationPage("New Ada Project Creation");
		projectCreationPage.setTitle("Create an Ada Project");
		projectCreationPage.setDescription("Enter a project name");
		addPage(projectCreationPage);
	}
}
