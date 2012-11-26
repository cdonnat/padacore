package org.padacore.core.gnat.project;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;

/**
 * This class allows to create a GPR project by parsing an existing .gpr file.
 *   
 * @author RS
 *
 */
public class FileGprProjectFactory extends AbstractGprProjectFactory {
	
	private IPath gprFilePath;

	public FileGprProjectFactory(IPath gprFilePath) {
		this.gprFilePath = gprFilePath;
	}

	@Override
	public GprProject createGprProject() {
		GprLexer lexer;
		GprProject createdGpr = null;

		try {
			lexer = new GprLexer(new ANTLRFileStream(this.gprFilePath.toOSString()));

			GprParser parser = new GprParser(new CommonTokenStream(lexer));

			createdGpr = parser.project();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		return createdGpr;
	}

}
