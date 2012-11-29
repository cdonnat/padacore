package org.padacore.core.gnat.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.padacore.core.gnat.Context;
import org.padacore.core.gnat.Symbol;

public class ContextTest {

	@Test
	public void test() {
		Context a = new Context("A");
		a.addAttribute("Exec_dir", Symbol.CreateString("exe"));
		a.addVar("Var", Symbol.CreateString("VarValue"));
		a.addVar("Compiler.C_switches", Symbol.CreateString("-inA"));

		Context b = new Context("B");
		b.addReference(a);
		b.addVar("bVar", b.getVariable("a.var"));
		b.addVar("Compiler.C_switches", Symbol.CreateString("-inB"));

		assertEquals(a.getVariable("Var").getAsString(), b.getVariable("bVar")
				.getAsString());
		assertEquals("-inA", a.getVariable("Compiler.C_switches").getAsString());
		assertEquals("-inB", b.getVariable("Compiler.C_switches").getAsString());
		assertEquals("-inA", b.getVariable("A.Compiler.C_switches")
				.getAsString());
		assertEquals("exe", b.getAttribute("A'Exec_dir").getAsString());
	}

}
