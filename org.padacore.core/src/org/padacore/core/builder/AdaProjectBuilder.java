package org.padacore.core.builder;

import java.util.Map;
import java.util.Observer;

import org.padacore.core.Activator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.project.PropertiesManager;
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
	 * Returns the build command to use with its arguments.
	 * 
	 * @return the build command with its arguments as an array of String
	 *         (command followed by arguments).
	 */
	private String[] buildCommand() {
		AdaProjectBuilderCmds cmd = new AdaProjectBuilderCmds(Activator.getDefault().getScenario(),
				(GnatAdaProject) new PropertiesManager(this.getProject()).getAdaProject());
		return cmd.build();
	}

	/**
	 * Returns the clean command to use with its arguments.
	 * 
	 * @return the clean command with its arguments as an array of String
	 *         (command followed by arguments).
	 */
	private String[] cleanCommand() {
		AdaProjectBuilderCmds cmd = new AdaProjectBuilderCmds(Activator.getDefault().getScenario(),
				(GnatAdaProject) new PropertiesManager(this.getProject()).getAdaProject());
		return cmd.clean();
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
		Assert.isLegal(kind == FULL_BUILD || kind == INCREMENTAL_BUILD || kind == AUTO_BUILD);

		Console console = new Console();
		SubMonitor submonitor = SubMonitor.convert(monitor);

		String message = "Building of " + getProject().getName();

		ExternalProcess process = new ExternalProcess(message, console,
				new Observer[] { new GprbuildObserver(submonitor, console) }, new Observer[] {
						new GprbuildErrObserver(this.getProject(), new GprbuildOutput()),
						new ExternalProcessOutput(console) });

		submonitor.beginTask(message, 100);
		process.run(buildCommand(), monitor);
		submonitor.done();
		refreshBuiltProject();
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {

		this.build(kind, monitor);

		return this.getProject().getReferencedProjects();
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
			getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}
}
