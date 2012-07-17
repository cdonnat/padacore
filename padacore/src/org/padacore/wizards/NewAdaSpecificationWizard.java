package org.padacore.wizards;

import org.padacore.AdaSpecificationFile;

public class NewAdaSpecificationWizard extends NewAdaSourceFileWizard {
	
	public NewAdaSpecificationWizard() {
		super(new AdaSpecificationFile());
	}
}
