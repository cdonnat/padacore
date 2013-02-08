package org.padacore.core.gnat.test;

import java.io.IOException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.padacore.core.gnat.GprLexer;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.GprParser;

public class GprGrammarTestUtils {
	
	public static GprParser CreateParser(GprLexer lexer, GprLoader loader)
			throws IOException {
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GprParser parser = new GprParser(loader, tokens);

		return parser;
	}

	public static GprLexer CreateLexer(String testString) throws IOException {
		CharStream stream = new ANTLRStringStream(testString);
		GprLexer lexer = new GprLexer(stream);

		return lexer;
	}

	public static boolean NoRecognitionExceptionOccurred(GprParser parser,
			GprLexer lexer) {
		return parser.getExceptions().isEmpty()
				&& lexer.getExceptions().isEmpty();
	}

}