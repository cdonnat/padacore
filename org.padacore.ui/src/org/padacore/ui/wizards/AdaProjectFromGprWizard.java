package org.padacore.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.ProjectBuilder;
import org.padacore.core.gnat.Context;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;
import org.padacore.core.gnat.GprBuilder;
import org.padacore.core.gnat.GprLoader;
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
		GprLoader loader = new GprLoader(gprProjectAbsolutePath);
		loader.load();
		IProject createdProject;
		//
		// GprLoader.Load load = loader.getLoadedProject().get(0);
		// GprBuilder builder = new GprBuilder(load.getProject());
		// GprProject gprFromFile = builder.build();
		//
		// IPath projectLocation = new Path(new
		// File(load.getPath().toOSString()).getParent());
		// eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(gprFromFile.getName(),
		// projectLocation, false);

		/* Multiple projects creation attempt... */

		for (GprLoader.Load load : loader.getLoadedProjects()) {
			GprBuilder builder = new GprBuilder(load.getProject());
			GprProject gprFromFile = builder.build();

			createdProject = eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(
					gprFromFile.getName(), null, false, load.getPath());
		}

		for (GprLoader.Load load : loader.getLoadedProjects()) {
			Context current = load.getProject();
			IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
			IProject[] referencedProjects = new IProject[current
					.getReferences().size()];
			for (int i = 0; i < current.getReferences().size(); i++) {
				referencedProjects[i] = workspace.getProject(current
						.getReferences().get(i).getName());
			}

			IProject eclipseProject = workspace.getProject(current.getName());
			try {
				IProjectDescription description = eclipseProject
						.getDescription();
				description.setReferencedProjects(referencedProjects);
				eclipseProject.setDescription(description, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addPages() {
		page = new AdaProjectFromGprCreationPage();
		addPage(page);
	}
}
