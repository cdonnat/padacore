package org.padacore.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.padacore.ui.messages"; //$NON-NLS-1$
	public static String AdaImplementationFile_Description;
	public static String AdaPreferences_Description;
	public static String AdaPreferences_Title;
	public static String AdaSpecificationFile_Description;
	public static String NewAdaProjectWizard_Description;
	public static String NewAdaProjectWizard_PageName;
	public static String NewAdaProjectWizard_Title;
	public static String NewAdaProjectWizard_WizardName;
	public static String AdaProjectCreationPage_AddMainProcedure;
	public static String AdaProjectFromGprCreationPage_BadPath;
	public static String AdaProjectFromGprCreationPage_Browse;
	public static String AdaProjectFromGprCreationPage_Description;
	public static String AdaProjectFromGprCreationPage_GPRProject;
	public static String AdaProjectFromGprCreationPage_NoGPR;
	public static String AdaProjectFromGprCreationPage_Project;
	public static String AdaProjectFromGprCreationPage_Title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
