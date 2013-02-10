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

			String execName = RemoveExtensionFromFilenameIfPresent(execSourceName);

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

		String relativeExecDir = ".";

		if (this.gprProject.getExecutableDir() != null) {
			relativeExecDir = this.gprProject.getExecutableDir();
		} else if (this.gprProject.getObjectDir() != null) {
			relativeExecDir = this.gprProject.getObjectDir();
		}
		
		IPath absoluteExecDir = this.convertToAbsolutePath(relativeExecDir);

		return absoluteExecDir.toOSString();
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
	 * Removes the extension from given filename if there is one, does nothing
	 * otherwise.
	 * 
	 * @param filename
	 *            the filename with extension.
	 * @return the filename without extension.
	 */
	private static String RemoveExtensionFromFilenameIfPresent(String filename) {
		String fileNameWithoutExtension = filename;

		if (filename.indexOf('.') != -1) {
			fileNameWithoutExtension = filename.substring(0,
					filename.lastIndexOf('.'));
		}

		return fileNameWithoutExtension;
	}

	@Override
	public List<String> getSourcesDir() {
		return this.gprProject.getSourcesDir();
	}

	@Override
	public String getObjectDirectoryPath() {
		String relativeObjectDirectory = this.gprProject.getObjectDir();
		
		if(relativeObjectDirectory == null) {
			relativeObjectDirectory = ".";
		}
		IPath absoluteObjectDirectory = this.convertToAbsolutePath(relativeObjectDirectory);

		return absoluteObjectDirectory.toOSString();
	}
	
	private IPath convertToAbsolutePath(String projectRelativePath) {
		return this.gprProject.getRootDirPath().append(projectRelativePath);
	}
}
