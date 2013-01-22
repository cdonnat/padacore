package org.padacore.ui.wizards;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.ProjectBuilder;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;
import org.padacore.core.gnat.GprBuilder;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.GprProject;

/**
 * This class defines a wizard which enables user to create a new Eclipse
 * project with Ada nature from an existing GPR project file.
 * 
 * @author rs
 * 
 */
public class NewAdaProjectFromGprWizard extends Wizard implements INewWizard {

	private AdaProjectFromGprCreationPage page;
	private ProjectBuilder eclipseAdaProjectBuilder;

	public NewAdaProjectFromGprWizard() {
		setWindowTitle("New Ada Project from a GPR Project");
		this.eclipseAdaProjectBuilder = new ProjectBuilder(new GnatAdaProjectAssociationManager());
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
		
		GprLoader.Load load = loader.getLoadedProject().get(0);
		GprBuilder builder = new GprBuilder(load.getProject());
		GprProject gprFromFile = builder.build();
		
		IPath projectLocation = new Path(new File(load.getPath().toOSString()).getParent());
		eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(gprFromFile.getName(),
				projectLocation, false);
		
		/* Multiple projects creation attempt... 
		 
		for (GprLoader.Load load : loader.getLoadedProject()) {
			GprBuilder builder = new GprBuilder(load.getProject());
			GprProject gprFromFile = builder.build();
			
			IPath projectLocation = new Path(new File(load.getPath().toOSString()).getParent());
			eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(gprFromFile.getName(),
					projectLocation, false);
		}

		for (GprLoader.Load load : loader.getLoadedProject()) {
			Context current = load.getProject();
			IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
			IProject[] referencedProjects = new IProject[current.getReferences()
			                         					.size()];
			for (int i = 0; i < current.getReferences()
					.size(); i++) {
				referencedProjects[i] = workspace.getProject(current.getReferences().get(i).getName());
			}

			IProject eclipseProject = workspace.getProject(current.getName());
			try {
				IProjectDescription description = eclipseProject.getDescription();
				description.setReferencedProjects(referencedProjects);
				eclipseProject.setDescription(description, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
	}

	@Override
	public void addPages() {
		page = new AdaProjectFromGprCreationPage();
		addPage(page);
	}
}
