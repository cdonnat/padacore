package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.padacore.core.gnat.GprLexer;
import org.padacore.core.gnat.GprParser;
import org.padacore.core.gnat.GprProject;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprBuilderTest {

	@Test
	public void test() {
		GprLexer lexer;
		try {
			lexer = new GprLexer(new ANTLRFileStream(CommonTestUtils.GetPathToSampleProject()));
			GprParser parser = new GprParser(new CommonTokenStream(lexer));

			GprProject gpr = parser.project();

			assertEquals("Toto", gpr.getName());
			assertEquals("new_exe", gpr.getExecutableDir());
			assertEquals("objects", gpr.getObjectDir());
			assertEquals(2, gpr.getSourcesDir().size());
			assertEquals("src", gpr.getSourcesDir().get(0));
			assertEquals("include", gpr.getSourcesDir().get(1));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
	}

}
