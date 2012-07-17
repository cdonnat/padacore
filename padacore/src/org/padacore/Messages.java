package org.padacore;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.padacore.messages"; //$NON-NLS-1$
	public static String AdaImplementationFile_Description;
	public static String AdaSpecificationFile_Description;
	public static String NewAdaProjectWizard_Description;
	public static String NewAdaProjectWizard_PageName;
	public static String NewAdaProjectWizard_Title;
	public static String NewAdaProjectWizard_WizardName;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
