package org.padacore.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.BundleContext;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.project.ProjectOpeningListener;
import org.padacore.core.utils.PadacoreJob;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	public static final String PLUGIN_ID = "org.padacore.core"; //$NON-NLS-1$

	private static Activator plugin;
	private GnatAdaProjectAssociationManager gnatAdaProjectAssociationManager;
	private IResourceChangeListener projectOpeningListener;
	private Scenario scenario;

	public Activator() {

		this.gnatAdaProjectAssociationManager = new GnatAdaProjectAssociationManager();
		this.projectOpeningListener = new ProjectOpeningListener(
				this.gnatAdaProjectAssociationManager);

		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectOpeningListener,
				IResourceChangeEvent.POST_CHANGE);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.gnatAdaProjectAssociationManager
				.performAssociationToAdaProjectForAllProjectsWithAdaNatureOf(ResourcesPlugin
						.getWorkspace());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this.projectOpeningListener);
		IJobManager jobManager = Job.getJobManager();
		jobManager.cancel(PadacoreJob.PADACORE_JOB_FAMILY);
		jobManager.join(PadacoreJob.PADACORE_JOB_FAMILY, null);

		super.stop(context);
	}

	public Scenario getScenario() {
		if (this.scenario == null) {
			this.scenario = new Scenario();
		}
		return this.scenario;
	}
}
