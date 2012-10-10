package org.padacore.core;

import java.util.ArrayList;
import java.util.List;

public class GprProjectInterpreter {

	/**
	 * Returns the executable filenames of the given project.
	 * 
	 * @return a list of String corresponding to the names of executable files
	 *         of the project.
	 * @pre given GPR project is executable
	 */
	public static List<String> getExecutableNames(GprProject gpr) {
		assert (gpr.isExecutable());

		List<String> execNames = new ArrayList<String>(gpr
				.getExecutableSourceNames().size());
		boolean isWindowsSystem = isWindowsSystem();
		List<String> execSourceNames = gpr.getExecutableSourceNames();

		for (String execSourceName : execSourceNames) {

			String execName = removeExtensionFromFilename(execSourceName);

			if (isWindowsSystem) {
				execName = execName + ".exe";
			}

			execNames.add(execName);
		}

		return execNames;
	}

	/**
	 * Returns the "true" executable directory path of the project relative to
	 * project file directory (depending on existence of "Exec_Dir" and/or
	 * "Object_Dir" attributes in GPR project.
	 */
	public static String getExecutableDirectoryPath(GprProject gpr) {
		String realExecDir = ".";

		if (gpr.getExecutableDir() != null) {
			realExecDir = gpr.getExecutableDir();
		} else if (gpr.getObjectDir() != null) {
			realExecDir = gpr.getObjectDir();
		}

		return realExecDir;
	}

	/**
	 * Returns true if and only if current system is Windows.
	 * 
	 * @return true if current system is Windows, false otherwise.
	 */
	private static boolean isWindowsSystem() {
		String osName = System.getProperty("os.name");

		return osName.contains("win") || osName.contains("Win");
	}

	/**
	 * Removes the extension from given filename.
	 * 
	 * @param filename
	 *            the filename with extension.
	 * @return the filename without extension.
	 */
	private static String removeExtensionFromFilename(String filename) {
		final int EXTENSION_LENGTH = 3;

		return filename.substring(0, filename.length() - EXTENSION_LENGTH - 1);
	}

}
