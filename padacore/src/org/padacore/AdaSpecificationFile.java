package org.padacore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AdaSpecificationFile extends AdaSourceFile {

	@Override
	public String getExtension() {
		return "ads";
	}

	@Override
	public String getFileTypeDescription() {
		return "Ada Specification file";
	}

	@Override
	public InputStream getTemplate() {
		//TODO get template from preferences
		return new ByteArrayInputStream("-- Ads template".getBytes());
	}

}
