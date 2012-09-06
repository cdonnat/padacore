package org.padacore.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.padacore.utils.ProcessManagement;

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
	 * Returns the build command to use with its arguments.
	 * 
	 * @return the build command with its arguments as an array of String
	 *         (command followed by arguments).
	 */
	private String[] buildCommand() {
		return new String[] { "gprbuild", "-p", "-P", getGprFullPath() };
	}

	/**
	 * Returns the clean command to use with its arguments.
	 * 
	 * @return the clean command with its arguments as an array of String
	 *         (command followed by arguments).
	 */
	private String[] cleanCommand() {
		return new String[] { "gprclean", "-P", getGprFullPath() };
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

		ProcessManagement.executeExternalCommandWithArgs(buildCommand());
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {

		this.build(kind);
		
		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		ProcessManagement.executeExternalCommandWithArgs(cleanCommand());
	}
	
}
