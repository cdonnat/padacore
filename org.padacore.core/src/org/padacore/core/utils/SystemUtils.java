package org.padacore.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Defines utilities methods for querying operating system type and environment
 * (system path...).
 * 
 * @author RS
 * 
 */
public class SystemUtils {

	private final static String WINDOWS_PATH_DELIMITER = ";";
	private final static String UNIX_BASED_OS_PATH_DELIMITER = ":";
	private static final String WINDOWS_EXECUTABLE_EXTENSION = "exe";

	/**
	 * Returns true if and only if current system is Windows.
	 * 
	 * @return true if current system is Windows, false otherwise.
	 */
	public static boolean IsWindowsSystem() {
		String osName = System.getProperty("os.name");

		return osName.contains("win") || osName.contains("Win");
	}

	private static List<IPath> GetSystemPath() {
		String rawSystemPath = System.getenv("PATH");
		String[] systemPathAsArrayOfString;

		if (IsWindowsSystem()) {
			systemPathAsArrayOfString = rawSystemPath
					.split(WINDOWS_PATH_DELIMITER);
		} else {
			systemPathAsArrayOfString = rawSystemPath
					.split(UNIX_BASED_OS_PATH_DELIMITER);
		}

		List<IPath> systemPath = new ArrayList<IPath>(
				systemPathAsArrayOfString.length);
		for (String pathItem : systemPathAsArrayOfString) {
			systemPath.add(new Path(pathItem));
		}

		return systemPath;
	}

	/**
	 * Checks if the given executable exists in system path and that the user is
	 * allows to execute it.
	 * 
	 * @param executableName
	 *            name of the executable to check
	 * @return true if and only if given executable exists in system path and
	 *         can be executed.
	 */
	public static boolean DoesExecutableExistInPathAndCanBeExecuted(
			String executableName) {
		List<IPath> systemPath = SystemUtils.GetSystemPath();
		boolean executableInPathAndRunnable = false;
		Iterator<IPath> it = systemPath.iterator();
		IPath executableFullPath;

		while (!executableInPathAndRunnable && it.hasNext()) {

			executableFullPath = it.next().append(executableName);
			if (IsWindowsSystem()) {
				executableFullPath = executableFullPath
						.addFileExtension(WINDOWS_EXECUTABLE_EXTENSION);
			}
			executableInPathAndRunnable = executableFullPath.toFile()
					.canExecute();
		}

		return executableInPathAndRunnable;

	}
}
