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

	/**
	 * Returns the path of GPR (GNAT Project) file.
	 * 
	 * @return the absolute path of GPR file.
	 */
	private String getGprFullPath() {
		return this.getProject().getLocation() + "/"
				+ this.getProject().getName() + ".gpr";
	}

	/**
	 * Checks if the given resource is a GPR file.
	 * 
	 * @param resource
	 *            the resource to check.
	 * @return true if the given resource is a file with gpr extension, false
	 *         otherwise.
	 */
	private boolean isAGprFile(IResource resource) {
		return (resource.getType() == IResource.FILE)
				&& resource.getFileExtension().equals("gpr");
	}

	/**
	 * Returns the build command to use with its arguments.
	 * 
	 * @return the build command with its arguments as an array of String
	 *         (command followed by arguments).
	 */
	private String[] buildCommand() {
		return new String[] { "gprbuild", "-p", "-P", getGprFullPath() };
	}

	/**
	 * Performs the build according to the kind of build requested.
	 * 
	 * @param kind
	 *            the type of build requested (valid values are FULL_BUILD,
	 *            INCREMENTAL_BUILD or AUTO_BUILD).
	 * @throws CoreException
	 *             if the resources of the project cannot be retrieved.
	 */
	private void build(int kind) throws CoreException {
		assert (kind == FULL_BUILD || kind == INCREMENTAL_BUILD || kind == AUTO_BUILD);

		// FIXME a-t-on vraiment besoin de boucler sur toutes les ressources du
		// projet ? (on peut pas simplement appeler gprbuild avec getGprFullPath
		// puisqu'on suppose qu'un projet a TOUJOURS un GPR associé ?
		IResource[] resources = this.getProject().members();
		for (IResource iResource : resources) {
			if (isAGprFile(iResource)) {

				ProcessBuilder buildProcessBuilder = new ProcessBuilder(
						buildCommand());

				try {
					Process buildProcess = buildProcessBuilder.start();
					ProcessDisplay.DisplayErrors(buildProcess);
					ProcessDisplay.DisplayWarnings(buildProcess);
				} catch (IOException e) {
					System.err.println("gprbuild execution failed on "
							+ iResource.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {

		build(kind);
		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		String[] cleanCmdWithArgs = new String[] { "gprclean", "-P",
				getGprFullPath() };

		ProcessBuilder cleanProcessBuilder = new ProcessBuilder(
				cleanCmdWithArgs);

		try {
			Process cleanProcess = cleanProcessBuilder.start();
			ProcessDisplay.DisplayErrors(cleanProcess);
			ProcessDisplay.DisplayWarnings(cleanProcess);
		} catch (IOException e) {
			System.err.println("gprclean execution failed on "
					+ getGprFullPath());
			e.printStackTrace();
		}
	}
}
