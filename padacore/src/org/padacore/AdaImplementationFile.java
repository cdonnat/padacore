package org.padacore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AdaImplementationFile extends AdaSourceFile {

	@Override
	public String getExtension() {
		return "adb";
	}

	@Override
	public String getFileTypeDescription() {
		return "Ada Implementation file";
	}

	@Override
	public InputStream getTemplate() {
		//TODO get template from file
		return new ByteArrayInputStream("-- Adb template".getBytes());
	}

}
