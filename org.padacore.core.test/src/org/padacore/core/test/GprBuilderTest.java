package org.padacore.core.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.padacore.core.GprProject;
import org.padacore.core.gnat.project.GPRLexer;
import org.padacore.core.gnat.project.GPRParser;
import org.padacore.core.gnat.project.GprBuilder;

public class GprBuilderTest {

	@Test
	public void testBuildingFromGprFile() {
		GPRLexer lexer;

		try {
			lexer = new GPRLexer(new ANTLRFileStream(System.getProperty("user.dir") + 
					"/src/org/padacore/core/test/sample_project.gpr"));
			GPRParser parser = new GPRParser(new CommonTokenStream(lexer));

			GprBuilder builder = parser.project();
			GprProject gpr = builder.build();

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
