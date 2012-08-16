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

	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {

		IResource[] resources = this.getProject().members();
		for (IResource iResource : resources) {
			if (iResource.getType() == IResource.FILE) {

				if (iResource.getFileExtension().equals("gpr")) {

					Runtime rt = Runtime.getRuntime();
					try {

						Process pr = rt.exec("gprbuild -p -P "
								+ this.getProject().getLocation() + "/" 
								+ this.getProject().getName() + ".gpr");

						BufferedReader error = new BufferedReader(
								new InputStreamReader(pr.getErrorStream()));
						String errorLine;

						while ((errorLine = error.readLine()) != null) {
							System.err.println(errorLine);
						}

						BufferedReader output = new BufferedReader(
								new InputStreamReader(pr.getInputStream()));
						String outputLine;

						while ((outputLine = output.readLine()) != null) {
							System.out.println(outputLine);
						}

					} catch (IOException e) {
						System.err.println("gnatmake execution failed on "
								+ iResource.getName());
						e.printStackTrace();
					}
					System.out.println("Found a file " + iResource.getName());
				} else {
					System.out.println("Found something different from a file");
				}
			}
		}
		return null;
	}
}
