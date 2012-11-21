package org.padacore.core.gnat.project;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;

public class GprProjectFactory {

	private final static String GPR_PROJECT_FILE_EXTENSION = ".gpr";

	/**
	 * Creates a GprProject from a GPR file.
	 * 
	 * @param gprFilePath
	 *            the absolute path of the GPR file (filename included).
	 * @return the GprProject corresponding to given GPR file.
	 * 
	 * @throws IOException
	 *             if GPR file could not be opened.
	 * @throws RecognitionException
	 *             if the syntax of GPR file is incorrect.
	 */
	public static GprProject CreateGprProjectFromFile(IPath gprFilePath) {
		GprLexer lexer;
		GprProject createdGpr = null;

		try {
			lexer = new GprLexer(new ANTLRFileStream(gprFilePath.toOSString()));

			GprParser parser = new GprParser(new CommonTokenStream(lexer));

			createdGpr = parser.project();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		return createdGpr;
	}

	/**
	 * Creates and returns a default GPR project according to given project
	 * information.
	 * 
	 * @param projectName
	 *            the name of the project.
	 * @param addMainProcedure
	 *            true if a main procedure should be added to GPR project, false
	 *            otherwise.
	 * @param executableName
	 *            the name of the source file corresponding to the executable of
	 *            the project.
	 * @return a newly created GPR project.
	 */
	public static GprProject CreateDefaultGprProject(String projectName,
			boolean addMainProcedure, String executableName) {
		GprProject gprProject = new GprProject(projectName);
		
		if (addMainProcedure) {
			gprProject.setExecutable(true);
			gprProject.addExecutableName(executableName);
		}

		return gprProject;
	}

	/**
	 * Returns the file extension used for a GPR project file.
	 * 
	 * @return
	 */
	public static String GetGprProjectFileExtension() {
		return GPR_PROJECT_FILE_EXTENSION;
	}
}
