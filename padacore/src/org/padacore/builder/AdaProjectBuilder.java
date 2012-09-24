package org.padacore.builder;

import java.util.Map;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.padacore.utils.ExternalProcess;
import org.padacore.utils.ExternalProcessJob;
import org.padacore.utils.ExternalProcessOutput;

public class AdaProjectBuilder extends IncrementalProjectBuilder {

	private static final String RUNNING_GPRBUILD = "Running GPRbuild...";
	private static final String RUNNING_GPRCLEAN = "Running GPRclean...";

	public AdaProjectBuilder() {

	}

	/**
	 * Returns the path of GPR (GNAT Project) file.
	 * 
	 * @return the absolute path of GPR file.
	 */
	private String getGprFullPath() {
		return this.getProject().getLocation() + "/" + this.getProject().getName() + ".gpr";
	}

	/**
	 * Returns the build command to use with its arguments.
	 * 
	 * @return the build command with its arguments as an array of String
	 *         (command followed by arguments).
	 */
	private String[] buildCommand() {
		return new String[] { "gprbuild", "-d", "-p", "-P", getGprFullPath() };
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

		Job job = new Job(RUNNING_GPRBUILD) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				ExternalProcess process = new ExternalProcess(new Observer[] {
						new ExternalProcessOutput(), new GprbuildObserver(monitor) },
						new Observer[] { new ExternalProcessOutput() });

				process.run(buildCommand(), monitor);

				return Status.OK_STATUS;
			}
		};
		job.schedule();

	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {

		this.build(kind);

		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		ExternalProcessJob.runWithDefaultOutput(RUNNING_GPRCLEAN, cleanCommand());
	}

}
