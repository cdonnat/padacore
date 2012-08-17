package org.padacore.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class AdaProjectBuilder extends IncrementalProjectBuilder {

	public AdaProjectBuilder() {

	}

	private String getGprFullPath() {
		return this.getProject().getLocation() + "/" + this.getProject().getName() + ".gpr";
	}

	private boolean isAGprFile(IResource resource) {
		return (resource.getType() == IResource.FILE) && resource.getFileExtension().equals("gpr");
	}

	private void displayErrors(Process process) throws IOException {
		BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String errorLine;

		while ((errorLine = error.readLine()) != null) {
			System.err.println(errorLine);
		}
	}

	private void displayWarnings(Process process) throws IOException {
		BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String outputLine;

		while ((outputLine = output.readLine()) != null) {
			System.out.println(outputLine);
		}
	}

	private String[] buildCommand(int kind) {
		if (kind == FULL_BUILD || kind == INCREMENTAL_BUILD) {
			return new String[] { "gprbuild", "-p", "-P", getGprFullPath() };
		} else {
			return new String[] { "gprclean", "-P", getGprFullPath() };
		}
	}

	private void build (int kind) throws CoreException {
		IResource[] resources = this.getProject().members();
		for (IResource iResource : resources) {
			if (isAGprFile(iResource)) {

				Runtime rt = Runtime.getRuntime();
				try {
					Process process = rt.exec(buildCommand(kind));
					displayErrors(process);
					displayWarnings(process);
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

		build (kind);

		return null;
	}
	
	protected void clean(IProgressMonitor monitor) throws CoreException {
		build (CLEAN_BUILD);
	}
}
