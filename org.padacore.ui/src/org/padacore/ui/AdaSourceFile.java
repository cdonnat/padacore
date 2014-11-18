package org.padacore.ui;

import java.io.InputStream;

public abstract class AdaSourceFile {

	/**
	 * Provides the extension associated to Ada file.
	 * 
	 * @return the extension associated to Ada file (without leading dot).
	 */
	public abstract String getExtension();

	/**
	 * Provides a textual description of the type of Ada file.
	 * 
	 * @return a textual description of the type of Ada file.
	 */
	public abstract String getFileTypeDescription();

	/**
	 * Provides the template to use for Ada file.
	 * 
	 * @return the template that will be used to fill an empty Ada file as an
	 *         InputStream.
	 */
	public abstract InputStream getTemplate();

	/**
	 * Checks if the given file name is a valid Ada source file name.
	 * 
	 * @param fileNameWithExtension
	 *            the file name to check (including extension)
	 * @return true if the file name (excluding extension) is a valid Ada source
	 *         file name (i.e. contains only letters, digits, underscores and
	 *         dashes, starts with a letter, does not contain consecutive
	 *         underscores or dashes and does not end with underscore or dash),
	 *         False otherwise.
	 */
	public final boolean isFileNameValid(String fileNameWithExtension) {
		int extensionLength = this.getExtension().length() + 1; // add leading
																// dot
		int fileNameLastCharIndex = fileNameWithExtension.length()
				- extensionLength;

		String fileNameWithoutExtension = fileNameWithExtension.substring(0,
				fileNameLastCharIndex);
		boolean isValid = fileNameWithoutExtension
				.matches("\\A\\p{Alpha}{1}([_-]?\\p{Alnum}{1})*\\z");

		return isValid;
	}

}
