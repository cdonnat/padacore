package org.padacore.core.builder;

import java.util.Map;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.padacore.core.utils.ExternalProcess;
import org.padacore.core.utils.ExternalProcessOutput;

public class AdaProjectBuilder extends IncrementalProjectBuilder {

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

		final String message = "Building of " + getProject().getName();
		Job job = new Job(message) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				ExternalProcess process = new ExternalProcess(message,
						new Observer[] { new GprbuildObserver(monitor) },
						new Observer[] { new GprbuildErrObserver(getProject()) });

				process.run(buildCommand(), monitor);
				refresh();
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
		final String message = "Cleaning of " + getProject().getName();
		Job job = new Job(message) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				ExternalProcess process = new ExternalProcess(message,
						new Observer[] { new ExternalProcessOutput() },
						new Observer[] { new ExternalProcessOutput() });

				process.run(cleanCommand(), monitor);
				refresh();
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private void refresh() {
		try {
			getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
