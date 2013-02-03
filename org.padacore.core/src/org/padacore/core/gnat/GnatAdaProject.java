package org.padacore.core.gnat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.padacore.core.IAdaProject;

public class GnatAdaProject implements IAdaProject {

	private GprProject gprProject;

	public GnatAdaProject(GprProject gprProject) {
		this.gprProject = gprProject;
	}

	@Override
	public List<String> getExecutableNames() {
		Assert.isTrue(this.isExecutable());

		List<String> execNames = new ArrayList<String>(this.gprProject
				.getExecutableSourceNames().size());
		boolean isWindowsSystem = isWindowsSystem();
		List<String> execSourceNames = this.gprProject
				.getExecutableSourceNames();

		for (String execSourceName : execSourceNames) {

			String execName = removeExtensionFromFilename(execSourceName);

			// TODO we should take into account the Executable_Suffix GPR
			// attribute
			if (isWindowsSystem) {
				execName = execName + ".exe";
			}

			execNames.add(execName);
		}

		return execNames;
	}

	@Override
	public String getExecutableDirectoryPath() {
		Assert.isTrue(this.isExecutable());

		String realExecDir = ".";

		if (this.gprProject.getExecutableDir() != null) {
			realExecDir = this.gprProject.getExecutableDir();
		} else if (this.gprProject.getObjectDir() != null) {
			realExecDir = this.gprProject.getObjectDir();
		}

		return realExecDir;
	}

	@Override
	public boolean isExecutable() {
		return this.gprProject.isExecutable();
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

	@Override
	public List<String> getSourcesDir() {
		return this.gprProject.getSourcesDir();
	}

	@Override
	public String getObjectDirectoryPath() {
		String definedObjectDirectory = this.gprProject.getObjectDir();
		String realObjectDirectory = definedObjectDirectory;

		if (definedObjectDirectory == null) {
			realObjectDirectory = ".";
		}

		return realObjectDirectory;
	}

}
