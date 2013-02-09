package org.padacore.core.gnat.test;

import java.io.IOException;

import org.padacore.core.gnat.GprLexer;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.GprParser;
import org.padacore.core.test.stubs.GprLoaderStub;

public class GprGrammarFixture {

	public GprLexer lexer;
	public GprParser parser;
	public GprLoader loader;

	public GprGrammarFixture(String testString) {
		try {
			this.loader = new GprLoaderStub();
			this.lexer = GprGrammarTestUtils.CreateLexer(testString);
			this.parser = GprGrammarTestUtils.CreateParser(this.lexer,
					this.loader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
