package org.padacore.core.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	public static void CreateNewFileWithContents(String absoluteFilename,
			String fileContents) throws IOException {

		FileWriter writer = null;

		try {
			File addedFile = new File(absoluteFilename);
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
