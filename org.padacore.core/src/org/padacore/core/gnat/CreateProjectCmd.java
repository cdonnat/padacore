package org.padacore.core.gnat;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.gpr4j.core.Gpr;
import org.padacore.core.project.ProjectBuilder;
import org.padacore.core.utils.CancelableJob;

public class CreateProjectCmd extends CancelableJob {

	private IPath projectPath;
	private String projectName;
	private boolean mainIsRequired;

	public CreateProjectCmd (IPath projectPath, String projectName, boolean mainIsRequired) {
		super("Create project " + projectName);
		this.projectPath = projectPath;
		this.projectName = projectName;
		this.mainIsRequired = mainIsRequired;
	} 

	@Override
	protected void execute(IProgressMonitor monitor) throws OperationCanceledException {
		IPath eclipseProjectPath = ProjectBuilder
				.GetProjectPath(this.projectName, this.projectPath);

		DefaultGprProjectFactory gprFactory = new DefaultGprProjectFactory(this.projectName,
				this.mainIsRequired, eclipseProjectPath);
		Gpr gprProject = gprFactory.createGprProject();

		ProjectBuilder eclipseAdaProjectBuilder = new ProjectBuilder(this.projectName);
		eclipseAdaProjectBuilder.createNewProject(new GnatAdaProject(gprProject), this.projectPath,
				this.mainIsRequired);
	}

	@Override
	protected void executeWhenCanceled() {
		// TODO Auto-generated method stub
		
	}

}
