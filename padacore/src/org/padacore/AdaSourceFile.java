package org.padacore;

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
	 *         dashes and starts with a letter or digit), False otherwise.
	 */
	// FIXME Correct the rule: Ada identifiers: they must start with a
	// letter, and be followed by zero or more letters, digits or underscore
	// characters; it is also illegal to have two underscores next to each
	// other. Identifiers are always case-insensitive ("Name" is the same as
	// "name").
	public final boolean isFileNameValid(String fileNameWithExtension) {
		int extensionLength = this.getExtension().length() + 1; // add leading
																// dot
		int fileNameLastCharIndex = fileNameWithExtension.length()
				- extensionLength;

		String fileNameWithoutExtension = fileNameWithExtension.substring(0,
				fileNameLastCharIndex);
		boolean isValid = fileNameWithoutExtension
				.matches("\\A\\p{Alnum}{1}[\\p{Alnum}-_]*");

		return isValid;
	}

}
