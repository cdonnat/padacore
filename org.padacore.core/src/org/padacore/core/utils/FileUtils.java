package org.padacore.core.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;

public class FileUtils {

	/**
	 * Create a new file with the specified contents.
	 * 
	 * @param absolutefileName
	 *            absolute file name of the file to add.
	 * @param contents
	 *            contents of the file to add.
	 * @throws IOException
	 *             if an error occurred during file creation.
	 */
	public static void CreateNewFileWithContents(IPath absoluteFilename,
			String fileContents) throws IOException {

		FileWriter writer = null;

		try {
			String osAbsoluteFilepath = absoluteFilename.toOSString();
			File addedFile = new File(osAbsoluteFilepath);
			addedFile.createNewFile();
			writer = new FileWriter(addedFile);
			writer.write(fileContents);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
