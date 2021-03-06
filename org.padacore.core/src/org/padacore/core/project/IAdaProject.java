package org.padacore.core.project;

import java.util.List;

import org.eclipse.core.runtime.IPath;

public interface IAdaProject {

	/**
	 * Return the name of the Ada project.
	 * 
	 * @return The name of the Ada project
	 */
	public abstract String getName();

	/**
	 * Returns the names of executables of the given project.
	 * 
	 * @return a list of String corresponding to the names of executables of the
	 *         project.
	 * @pre the project is indeed executable
	 */
	public abstract List<String> getExecutableNames();

	/**
	 * Returns the names of "main" source files of the given project (the source
	 * files used to produce executables of the project).
	 * 
	 * @return a list of String corresponding to the names of "main" source
	 *         files of the project.
	 * @pre the project is indeed executable
	 */
	public abstract List<String> getExecutableSourceNames();

	/**
	 * Returns whether this Ada project is executable.
	 * 
	 * @return true if the Ada project is executable, false otherwise.
	 */
	public abstract boolean isExecutable();

	/**
	 * Returns the absolute path of the directory in which executables will
	 * reside.
	 * 
	 * @return the absolute executable directory path of the project
	 * 
	 */
	public abstract IPath getExecutableDirectoryPath();

	/**
	 * Returns the list of sources directory.
	 * 
	 * @return a list of paths corresponding to the sources directories.
	 */
	public abstract List<IPath> getSourceDirectoriesPaths();

	/**
	 * Returns the absolute path of the directory in which object files will
	 * reside.
	 * 
	 * @return the absolute object directory path of the project.
	 */
	public abstract IPath getObjectDirectoryPath();

	/**
	 * Returns the absolute path of the project root.
	 * 
	 * @return the absolute path of the project root.
	 */
	public abstract IPath getRootPath();

}
