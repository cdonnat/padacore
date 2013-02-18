package org.padacore.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.Project;
import org.padacore.core.project.ProjectBuilder;
import org.padacore.core.utils.ErrorLog;

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
		final IPath gprProjectAbsolutePath = new Path(this.page.getGprProjectPath());

		Job job = new Job("Importing project") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				GprLoader loader = new GprLoader();
				loader.load(gprProjectAbsolutePath);

				// TODO clean this mess!
				for (Project project : loader.getLoadedProjects()) {
					eclipseAdaProjectBuilder.createProjectWithAdaNatureAt(project.getName(), null,
							false, project.getPath());
				}

				for (Project project : loader.getLoadedProjects()) {
					IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
					IProject[] referencedProjects = new IProject[project.getReferenceProjects()
							.size()];
					for (int i = 0; i < project.getReferenceProjects().size(); i++) {
						referencedProjects[i] = workspace.getProject(project.getReferenceProjects()
								.get(i).getName());
					}

					IProject eclipseProject = workspace.getProject(project.getName());
					try {
						IProjectDescription description = eclipseProject.getDescription();
						description.setReferencedProjects(referencedProjects);
						eclipseProject.setDescription(description, null);
					} catch (CoreException e) {
						ErrorLog.appendException(e);
					}
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();

	}

	@Override
	public void addPages() {
		page = new AdaProjectFromGprCreationPage();
		addPage(page);
	}
}
