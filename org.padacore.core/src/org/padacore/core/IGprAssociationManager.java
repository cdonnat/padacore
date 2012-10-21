package org.padacore.core;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public interface IGprAssociationManager {

	/**
	 * Associate a new GPR project (created from given GPR file) to an Ada
	 * project.
	 * 
	 * @param project
	 *            the project to which the GPR project will be associated.
	 * @param gprFilePath
	 *            the absolute path of GPR file (filename included)
	 * @throws IOException
	 *             if the GPR file could not be opened.
	 * @throws RecognitionException
	 *             if the GPR file format is invalid.
	 * @throws CoreException
	 *             if GPR project could not be associated to Ada project.
	 * @post a GPR project is associated to Ada project.
	 */
	public void performAssociationToGprProject(IProject adaProject,
			IPath gprFilePath);

}
