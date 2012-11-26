package org.padacore.ui.wizards;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.EclipseAdaProjectBuilder;
import org.padacore.core.gnat.project.AbstractGprProjectFactory;
import org.padacore.core.gnat.project.DefaultGprProjectFactory;
import org.padacore.core.gnat.project.GnatAdaProjectAssociationManager;
import org.padacore.core.gnat.project.GprProject;
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

		this.addDefaultGprProjectFileToProject(projectHandle, projectLocation);

		eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(
				projectHandle.getName(), projectLocation,
				projectCreationPage.addMainProcedure());
	}

	/**
	 * Adds a default GPR project file to the Eclipse project.
	 * 
	 * @param projectHandle
	 *            handle to the Eclipse project.
	 * @param projectLocation
	 *            location of the Eclipse project.
	 */
	private void addDefaultGprProjectFileToProject(IProject projectHandle,
			IPath projectLocation) {

		String eclipseProjectPath = EclipseAdaProjectBuilder.GetProjectPath(
				projectHandle.getName(), projectLocation);

		this.createProjectDirectory(eclipseProjectPath);

		this.createAndWriteDefaultGprProjectFile(projectHandle,
				eclipseProjectPath);

	}

	/**
	 * Creates and write to Eclipse project directory a default GPR project
	 * file.
	 * 
	 * @param projectHandle
	 *            handle to the Eclipse project.
	 * @param projectDirectoryPath
	 *            absolute path of the Eclipse project.
	 */
	private void createAndWriteDefaultGprProjectFile(IProject projectHandle,
			String projectDirectoryPath) {

		AbstractGprProjectFactory gprFactory = new DefaultGprProjectFactory(
				projectHandle.getName(), projectCreationPage.addMainProcedure());

		GprProject defaultGpr = gprFactory.createGprProject();

		IPath gprProjectFilePath = this.getGprAbsolutePath(
				projectHandle.getName(), projectDirectoryPath);

		EclipseAdaProjectBuilder.AddFileToProject(
				gprProjectFilePath.toOSString(), defaultGpr.toString());

	}

	private void createProjectDirectory(String projectDirectory) {
		File projectFolder = new File(projectDirectory);
		projectFolder.mkdirs();

	}

	private IPath getGprAbsolutePath(String projectName, String projectPath) {

		StringBuilder pathBuilder = new StringBuilder(projectPath);

		pathBuilder.append(IPath.SEPARATOR);
		pathBuilder.append(projectName);
		pathBuilder.append(AbstractGprProjectFactory.GetGprProjectFileExtension());

		return new Path(pathBuilder.toString());
	}

	@Override
	public void addPages() {
		projectCreationPage = new AdaProjectCreationPage(PAGE_NAME);
		projectCreationPage.setTitle(TITLE);
		projectCreationPage.setDescription(NEW_PROJECT_DESCRIPTION);
		addPage(projectCreationPage);
	}
}
