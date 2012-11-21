package org.padacore.ui.wizards;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.EclipseAdaProjectBuilder;
import org.padacore.core.gnat.project.GnatAdaProjectAssociationManager;
import org.padacore.core.gnat.project.GprProject;
import org.padacore.core.gnat.project.GprProjectFactory;

/**
 * This class defines a wizard which enables user to create a new Eclipse
 * project with Ada nature from an existing GPR project file.
 * 
 * @author rs
 * 
 */
public class NewAdaProjectFromGprWizard extends Wizard implements INewWizard {

	private AdaProjectFromGprCreationPage page;
	private EclipseAdaProjectBuilder eclipseAdaProjectBuilder;

	public NewAdaProjectFromGprWizard() {
		setWindowTitle("New Ada Project from a GPR Project");
		this.eclipseAdaProjectBuilder = new EclipseAdaProjectBuilder(
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

		GprProject gprFromFile = GprProjectFactory
				.CreateGprProjectFromFile(gprProjectAbsolutePath);

		IPath projectLocation = new Path(new File(
				gprProjectAbsolutePath.toOSString()).getParent());

		eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(gprFromFile.getName(),
				projectLocation, false);
	}

	@Override
	public void addPages() {
		page = new AdaProjectFromGprCreationPage();
		addPage(page);
	}
}
