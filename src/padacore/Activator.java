package padacore;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import padacore.editor.scanners.AdaCodeScanner;
import padacore.editor.scanners.AdaCommentScanner;
import padacore.editor.scanners.AdaPartitionScanner;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "padacore"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private AdaPartitionScanner adaPartitionScanner;
	private AdaCodeScanner adaScanner;
	private AdaCommentScanner adaCommentScanner;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public AdaPartitionScanner getAdaPartitionScanner() {
		if (adaPartitionScanner == null) {
			adaPartitionScanner = new AdaPartitionScanner();
		}
		return adaPartitionScanner;
	}

	public AdaCodeScanner getAdaCodeScanner() {
		if (adaScanner == null) {
			adaScanner = new AdaCodeScanner();
		}
		return adaScanner;
	}
	
	public AdaCommentScanner getAdaCommentScanner() {
		if (adaCommentScanner == null) {
			adaCommentScanner = new AdaCommentScanner();
		}
		return adaCommentScanner;
	}
	
}
