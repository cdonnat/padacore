package org.padacore.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	public static final String PLUGIN_ID = "org.padacore.core"; //$NON-NLS-1$

	@SuppressWarnings("unused")
	private static Activator plugin;
	private GprAssociationManager gprAssociationManager;

	public Activator() {

		this.gprAssociationManager = new GprAssociationManager();
		IResourceChangeListener projectOpeningListener = new ProjectOpeningListener(
				this.gprAssociationManager);

		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				projectOpeningListener, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		this.gprAssociationManager
				.performAssociationToGprProjectForAllAdaProjectsOf(ResourcesPlugin
						.getWorkspace());
	}
}
