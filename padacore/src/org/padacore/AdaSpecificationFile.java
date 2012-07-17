package org.padacore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AdaSpecificationFile extends AdaSourceFile {

	@Override
	public String getExtension() {
		return "ads"; //$NON-NLS-1$
	}

	@Override
	public String getFileTypeDescription() {
		return Messages.AdaSpecificationFile_Description;
	}

	@Override
	public InputStream getTemplate() {
		//TODO get template from preferences
		return new ByteArrayInputStream("-- Ads template".getBytes()); //$NON-NLS-1$
	}

}
