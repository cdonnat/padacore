package org.padacore.ui.wizards;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard;
import org.padacore.core.ProjectBuilder;
import org.padacore.core.gnat.FileGprProjectFactory;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;
import org.padacore.core.gnat.GprProject;

/**
 * This class defines a wizard which enables user to import an existing GPR
 * project file as an Eclipse project.
 * 
 * @author rs
 * 
 */
public class AdaProjectFromGprWizard extends Wizard implements IImportWizard {

	private AdaProjectFromGprCreationPage page;
	private ProjectBuilder eclipseAdaProjectBuilder;

	public AdaProjectFromGprWizard() {
		setWindowTitle("Import existing GPR Project");
		this.eclipseAdaProjectBuilder = new ProjectBuilder(
				new GnatAdaProjectAssociationManager());
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
		IPath gprProjectAbsolutePath = new Path(this.page.getGprProjectPath());

		FileGprProjectFactory gprFactory = new FileGprProjectFactory(
				gprProjectAbsolutePath);

		GprProject gprFromFile = gprFactory.createGprProject();

		IPath projectLocation = new Path(new File(
				gprProjectAbsolutePath.toOSString()).getParent());

		eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(
				gprFromFile.getName(), projectLocation, false);
	}

	@Override
	public void addPages() {
		page = new AdaProjectFromGprCreationPage();
		addPage(page);
	}
}
