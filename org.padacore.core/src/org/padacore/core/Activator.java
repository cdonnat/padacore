package org.padacore.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	public static final String PLUGIN_ID = "org.padacore.core"; //$NON-NLS-1$

	@SuppressWarnings("unused")
	private static Activator plugin;
	private GnatAdaProjectAssociationManager gnatAdaProjectAssociationManager;
	private IResourceChangeListener projectOpeningListener;

	public Activator() {

		this.gnatAdaProjectAssociationManager = new GnatAdaProjectAssociationManager();
		this.projectOpeningListener = new ProjectOpeningListener(
				this.gnatAdaProjectAssociationManager);

		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				projectOpeningListener, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		this.gnatAdaProjectAssociationManager
				.performAssociationToAdaProjectForAllProjectsWithAdaNatureOf(ResourcesPlugin
						.getWorkspace());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
				this.projectOpeningListener);

		super.stop(context);
	}
}
