package org.padacore.builder;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.padacore.utils.ProcessDisplay;

public class AdaProjectBuilder extends IncrementalProjectBuilder {

	public AdaProjectBuilder() {

	}

	private String getGprFullPath() {
		return this.getProject().getLocation() + "/" + this.getProject().getName() + ".gpr";
	}

	private boolean isAGprFile(IResource resource) {
		return (resource.getType() == IResource.FILE) && resource.getFileExtension().equals("gpr");
	}

	

	private String[] buildCommand(int kind) {
		switch (kind) {
		case FULL_BUILD:
		case INCREMENTAL_BUILD:
		case AUTO_BUILD:
			return new String[] { "gprbuild", "-p", "-P", getGprFullPath() };
		case CLEAN_BUILD:
			return new String[] { "gprclean", "-P", getGprFullPath() };
		default:
			return new String[] {};
		}
	}

	private void build(int kind) throws CoreException {
		IResource[] resources = this.getProject().members();
		for (IResource iResource : resources) {
			if (isAGprFile(iResource)) {

				Runtime rt = Runtime.getRuntime();
				try {
					Process process = rt.exec(buildCommand(kind));
					ProcessDisplay.DisplayErrors(process);
					ProcessDisplay.DisplayWarnings(process);
				} catch (IOException e) {
					System.err.println("gnatmake execution failed on " + iResource.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {

		build(kind);
		return null;
	}

	protected void clean(IProgressMonitor monitor) throws CoreException {
		build(CLEAN_BUILD);
	}
}
