package org.padacore.ui.wizards;

import org.padacore.ui.AdaSpecificationFile;

public class NewAdaSpecificationWizard extends NewAdaSourceFileWizard {
	
	public NewAdaSpecificationWizard() {
		super(new AdaSpecificationFile());
	}
}
