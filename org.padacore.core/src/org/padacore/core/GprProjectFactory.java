package org.padacore.core;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;
import org.padacore.core.gnat.project.GPRLexer;
import org.padacore.core.gnat.project.GPRParser;
import org.padacore.core.gnat.project.GprBuilder;

public class GprProjectFactory {

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
	public static GprProject CreateGprProjectFromFile(IPath gprFilePath)
			throws IOException, RecognitionException {
		GPRLexer lexer = new GPRLexer(new ANTLRFileStream(
				gprFilePath.toOSString()));
		GPRParser parser = new GPRParser(new CommonTokenStream(lexer));

		GprBuilder builder = parser.project();

		return builder.build();
	}

}
