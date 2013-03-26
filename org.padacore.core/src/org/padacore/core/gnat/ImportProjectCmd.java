package org.padacore.core.gnat;

import java.nio.file.Paths;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.gpr4j.core.Factory;
import org.gpr4j.core.ILoader;
import org.gpr4j.core.IProjectUnit;
import org.padacore.core.project.ProjectBuilder;
import org.padacore.core.utils.CancelableJob;
import org.padacore.core.utils.ErrorLog;

public class ImportProjectCmd extends CancelableJob {

	private IPath absoluteGprPath;
	private ILoader loader;

	public ImportProjectCmd(IPath absoluteGprPath) {
		super("Import project " + absoluteGprPath.removeFileExtension().lastSegment());
		this.absoluteGprPath = absoluteGprPath;
		this.loader = Factory.CreateLoader();
	}

	@Override
	protected void execute(IProgressMonitor monitor) throws OperationCanceledException {
		try {
			this.loader.load(Paths.get(this.absoluteGprPath.toOSString()));
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.importLoadedProjects(monitor);
		this.setReferencesBetweenEclipseProjects(monitor);
	}

	@Override
	protected void executeWhenCanceled() {
		IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
		for (IProjectUnit project : this.loader.getLoadedProjects()) {
			try {
				IProject eclipseProject = workspace.getProject(project.getName());
				if (eclipseProject.exists()) {
					eclipseProject.delete(true, null);
				}
			} catch (CoreException e) {
				ErrorLog.appendException(e);
			}
		}
	}

	/**
	 * Import all the projects in eclipse.
	 * 
	 * @pre Projects are loaded.
	 * @param monitor
	 *            Monitor
	 * 
	 */
	private void importLoadedProjects(IProgressMonitor monitor) {
		for (IProjectUnit project : this.loader.getLoadedProjects()) {
			this.checkCanceled(monitor);

			ProjectBuilder eclipseAdaProjectBuilder = new ProjectBuilder(project.getName());

			eclipseAdaProjectBuilder.importProject(new Path(project.getPath().toString()),
					new GnatAdaProject(Factory.CreateGpr(project)));
		}
	}

	/**
	 * Set the references between the eclipse projects imported.
	 * 
	 * @pre Projects are imported in eclipse.
	 * @param monitor
	 */
	private void setReferencesBetweenEclipseProjects(IProgressMonitor monitor) {
		IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
		for (IProjectUnit project : this.loader.getLoadedProjects()) {
			this.checkCanceled(monitor);

			IProject[] referencedProjects = new IProject[project.getReferenceProjects().size()];
			for (int i = 0; i < project.getReferenceProjects().size(); i++) {
				referencedProjects[i] = workspace.getProject(project.getReferenceProjects().get(i)
						.getName());
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
	}
}
