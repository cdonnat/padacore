package org.padacore.core.gnat;

import org.gpr4j.api.Gpr;

/**
 * Abstract class for a GPR project factory.
 * @author RS
 *
 */
public abstract class AbstractGprProjectFactory {
	
	private static final String GPR_PROJECT_FILE_EXTENSION = ".gpr";

	/**
	 * Creates a new GPR project.
	 * 
	 * @return the newly created GPR project.
	 */
	public abstract Gpr createGprProject();
	
	/**
	 * Returns the file extension for GPR project files.
	 * 
	 * @return a String corresponding to the file extension for GPR project files.
	 */
	public static String GetGprProjectFileExtension() {
		return GPR_PROJECT_FILE_EXTENSION;
	}

}
