package org.padacore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AdaImplementationFile implements IAdaSourceFile {

	@Override
	public String getExtension() {
		return "adb"; //$NON-NLS-1$
	}

	@Override
	public String getFileTypeDescription() {
		return Messages.AdaImplementationFile_Description;
	}

	@Override
	public InputStream getTemplate() {
		//TODO get template from file
		return new ByteArrayInputStream("-- Adb template".getBytes()); //$NON-NLS-1$
	}

}
