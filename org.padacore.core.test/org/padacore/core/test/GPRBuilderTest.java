package org.padacore.core.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.padacore.core.GprProject;
import org.padacore.core.gnat.project.GprBuilder;
import org.padacore.core.gnat.project.GPRV2Lexer;
import org.padacore.core.gnat.project.GPRV2Parser;

public class GPRBuilderTest {

	@Test
	public void test() {
		GPRV2Lexer lexer;
		try {
			lexer = new GPRV2Lexer(new ANTLRFileStream(getClass().getResource("sample_project.gpr")
					.getPath()));
			GPRV2Parser parser = new GPRV2Parser(new CommonTokenStream(lexer));

			GprBuilder builder = parser.project();
			GprProject gpr = builder.build();

			assertEquals("Toto", gpr.getName());
			assertEquals("new_exe", gpr.getExecutableDir());
			assertEquals("objects", gpr.getObjectDir());
			assertEquals(3, gpr.getSourcesDir().size());
			assertEquals("src", gpr.getSourcesDir().get(1));
			assertEquals("include", gpr.getSourcesDir().get(2));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
