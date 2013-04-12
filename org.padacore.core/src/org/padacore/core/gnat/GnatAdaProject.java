package org.padacore.core.gnat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.gpr4j.api.Gpr;
import org.padacore.core.project.IAdaProject;

public class GnatAdaProject implements IAdaProject {

	private Gpr gprProject;

	public GnatAdaProject(Gpr gprProject) {
		this.gprProject = gprProject;
	}

	@Override
	public List<String> getExecutableNames() {
		Assert.isLegal(this.isExecutable());

		List<String> execNames = new ArrayList<String>(this.gprProject.getExecutableSourceNames()
				.size());
		boolean isWindowsSystem = isWindowsSystem();
		List<String> execSourceNames = this.gprProject.getExecutableSourceNames();

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
	public IPath getExecutableDirectoryPath() {
		Assert.isTrue(this.isExecutable());

		String relativeExecDir = ".";

		if (this.gprProject.getExecutableDir() != null) {
			relativeExecDir = this.gprProject.getExecutableDir();
		} else if (this.gprProject.getObjectDir() != null) {
			relativeExecDir = this.gprProject.getObjectDir();
		}

		IPath absoluteExecDir = this.convertToAbsolutePath(relativeExecDir);

		return absoluteExecDir;
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
			fileNameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));
		}

		return fileNameWithoutExtension;
	}

	@Override
	public List<IPath> getSourceDirectoriesPaths() {
		List<String> relativeSourceDirs = this.gprProject.getSourcesDir();
		List<IPath> absoluteSourceDirs = new ArrayList<IPath>();

		if (relativeSourceDirs.size() == 0) {
			absoluteSourceDirs.add(this.getRootPath());
		} else {
			for (String srcDirs : relativeSourceDirs) {
				absoluteSourceDirs.add(this.getRootPath().append(srcDirs));
			}
		}

		return absoluteSourceDirs;
	}

	@Override
	public IPath getObjectDirectoryPath() {
		String relativeObjectDirectory = this.gprProject.getObjectDir();

		if (relativeObjectDirectory == null) {
			relativeObjectDirectory = ".";
		}
		IPath absoluteObjectDirectory = this.convertToAbsolutePath(relativeObjectDirectory);

		return absoluteObjectDirectory;
	}

	/**
	 * Converts the given path relative to the project root to an absolute path.
	 * 
	 * @param projectRelativePath
	 *            a path relative to the project root
	 * @return an absolute path equivalent to given relative path.
	 */
	private IPath convertToAbsolutePath(String projectRelativePath) {
		return new Path(this.gprProject.getRootDirPath().toString()).append(projectRelativePath);
	}

	@Override
	public IPath getRootPath() {
		return new Path(this.gprProject.getRootDirPath().toString());
	}

	@Override
	public List<String> getExecutableSourceNames() {
		return this.gprProject.getExecutableSourceNames();
	}
}
