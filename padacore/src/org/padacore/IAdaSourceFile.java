package org.padacore;

import java.io.InputStream;

public interface IAdaSourceFile {
	
	/**
	 * Provides the extension associated to Ada file.
	 * @return the extension associated to Ada file (without leading dot).
	 */
	public String getExtension();
	
	/**
	 * Provides a textual description of the type of Ada file.
	 * @return a textual description of the type of Ada file.
	 */
	public String getFileTypeDescription();
	
	/**
	 * Provides the template to use for Ada file.
	 * @return the template that will be used to fill an empty Ada file as an InputStream.
	 */
	public InputStream getTemplate();
	
	
}
