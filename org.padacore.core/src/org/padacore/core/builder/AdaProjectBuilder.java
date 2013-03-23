package org.padacore.core.builder;

import java.util.Map;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.padacore.core.utils.Console;
import org.padacore.core.utils.ErrorLog;
import org.padacore.core.utils.ExternalProcess;
import org.padacore.core.utils.ExternalProcessOutput;

/**
 * This class implements an Ada incremental project build using gprbuild.
 * 
 * @author RS
 * 
 */
public class AdaProjectBuilder extends IncrementalProjectBuilder {

	public AdaProjectBuilder() {
	}

	/**
	 * Returns the path of GPR (GNAT Project) file.
	 * 
	 * @return the absolute path of GPR file.
	 */
	private String getGprFullPath() {
		String gprProjectFullPath = this.getProject()
				.getFile(this.getProject().getName() + ".gpr").getRawLocation()
				.toOSString();

		return gprProjectFullPath;
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
	private void build(int kind, IProgressMonitor monitor) throws CoreException {
		Assert.isLegal(kind == FULL_BUILD || kind == INCREMENTAL_BUILD
				|| kind == AUTO_BUILD);

		Console console = new Console();
		SubMonitor submonitor = SubMonitor.convert(monitor);

		String message = "Building of " + getProject().getName();

		ExternalProcess process = new ExternalProcess(message, console,
				new Observer[] { new GprbuildObserver(submonitor, console) },
				new Observer[] {
						new GprbuildErrObserver(this.getProject(),
								new GprbuildOutput()),
						new ExternalProcessOutput(console) });

		submonitor.beginTask(message, 100);
		process.run(buildCommand(), monitor);
		submonitor.done();
		refreshBuiltProject();
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {

		if (kind == FULL_BUILD) {
			this.build(kind, monitor);
		} else {
			if (this.isIncrementalBuildRequired(this.getProject())) {
				this.build(kind, monitor);
			}
		}

		return this.getProject().getReferencedProjects();
	}

	/**
	 * Returns true if an incremental build is required for the given project,
	 * false otherwise.
	 * 
	 * @param project
	 *            the project for which incremental build is requested.
	 * @return if incremental build shall be performed, False otherwise.
	 */
	private boolean isIncrementalBuildRequired(IProject project) {
		boolean buildIsRequired = true;
		IResourceDelta delta = this.getDelta(project);

		if (delta != null) {
			IResource concernedResource = delta.getResource();
			Assert.isTrue(concernedResource.getType() == IResource.PROJECT);

			buildIsRequired = this
					.doesChangeInProjectAffectsAtLeastOneNonDerivedFile(delta);
		}

		return buildIsRequired;
	}

	private class NonDerivedResourcesFinder implements IResourceDeltaVisitor {

		private boolean nonDerivedFileFound;

		public NonDerivedResourcesFinder() {
			this.nonDerivedFileFound = false;
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();

			boolean resourceIsNotADerivedFolder = !(resource.getType() == IResource.FOLDER && resource
					.isDerived());

			this.nonDerivedFileFound = resource.getType() == IResource.FILE
					&& !resource.isDerived();

			return !this.nonDerivedFileFound && resourceIsNotADerivedFolder;
		}

		public boolean hasNonDerivedFileBeenFound() {
			return this.nonDerivedFileFound;
		}

	}

	/**
	 * Checks if the given delta contains at least one change to a non-derived
	 * file.
	 * 
	 * @param delta
	 *            the delta to examine.
	 * @return true if the given delta contains at least one change to a
	 *         non-derived file, false otherwise.
	 */
	// TODO This method can easily be improved by taking into account
	// information from GPR project: non derived files could be searched only in
	// source directories.
	private boolean doesChangeInProjectAffectsAtLeastOneNonDerivedFile(
			IResourceDelta delta) {
		NonDerivedResourcesFinder derivedResourcesFinder = new NonDerivedResourcesFinder();

		try {
			delta.accept(derivedResourcesFinder);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
		return derivedResourcesFinder.hasNonDerivedFileBeenFound();
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {

		Console console = new Console();
		final String message = "Cleaning " + getProject().getName();

		ExternalProcess process = new ExternalProcess(message, console,
				new Observer[] { new ExternalProcessOutput(console) },
				new Observer[] { new ExternalProcessOutput(console) });

		process.run(cleanCommand(), monitor);
		refreshBuiltProject();
	}

	/**
	 * Refreshes the build project.
	 */
	private void refreshBuiltProject() {
		try {
			getProject().refreshLocal(IResource.DEPTH_INFINITE,
					new NullProgressMonitor());
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}
}
