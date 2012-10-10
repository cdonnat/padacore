package org.padacore.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.padacore.core"; //$NON-NLS-1$

	// The shared instance
	@SuppressWarnings("unused")
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new ProjectOpeningListener(), IResourceChangeEvent.POST_CHANGE);
	}
	
}
