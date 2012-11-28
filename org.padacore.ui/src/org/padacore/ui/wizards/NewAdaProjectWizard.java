package org.padacore.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.EclipseAdaProjectBuilder;
import org.padacore.core.gnat.DefaultGprProjectFactory;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;
import org.padacore.ui.Messages;

/**
 * This class defines a wizard which enables user to create a new empty Eclipse
 * project with Ada nature. A sample main procedure can be added to the project
 * upon user request.
 * 
 * @author rs
 * 
 */
public class NewAdaProjectWizard extends Wizard implements INewWizard {

	private static final String PAGE_NAME = Messages.NewAdaProjectWizard_PageName;
	private static final String WIZARD_NAME = Messages.NewAdaProjectWizard_WizardName;
	private static final String TITLE = Messages.NewAdaProjectWizard_Title;
	private static final String NEW_PROJECT_DESCRIPTION = Messages.NewAdaProjectWizard_Description;

	private AdaProjectCreationPage projectCreationPage;
	private EclipseAdaProjectBuilder eclipseAdaProjectBuilder;

	public NewAdaProjectWizard() {
		setWindowTitle(WIZARD_NAME);
		this.eclipseAdaProjectBuilder = new EclipseAdaProjectBuilder(
				new GnatAdaProjectAssociationManager());
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {

		this.createNewDefaultProjectWithAdaNature();

		return true;
	}

	/**
	 * Creates a new default project with Ada nature.
	 */
	private void createNewDefaultProjectWithAdaNature() {
		IPath projectLocation = projectCreationPage.useDefaults() ? null
				: projectCreationPage.getLocationPath();

		IProject projectHandle = projectCreationPage.getProjectHandle();

		String eclipseProjectPath = EclipseAdaProjectBuilder.GetProjectPath(
				projectHandle.getName(), projectLocation);

		DefaultGprProjectFactory gprFactory = new DefaultGprProjectFactory(
				projectHandle.getName(),
				projectCreationPage.addMainProcedure(), eclipseProjectPath);
		gprFactory.createGprProject();

		eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(
				projectHandle.getName(), projectLocation,
				projectCreationPage.addMainProcedure());
	}

	@Override
	public void addPages() {
		projectCreationPage = new AdaProjectCreationPage(PAGE_NAME);
		projectCreationPage.setTitle(TITLE);
		projectCreationPage.setDescription(NEW_PROJECT_DESCRIPTION);
		addPage(projectCreationPage);
	}
}
