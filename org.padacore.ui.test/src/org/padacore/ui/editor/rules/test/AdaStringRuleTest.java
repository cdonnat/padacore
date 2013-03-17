package org.padacore.ui.editor.rules.test;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.junit.Test;
import org.padacore.ui.editor.rules.AdaStringRule;

public class AdaStringRuleTest {

	private FixtureRule fixture = new FixtureRule() {
		public IRule createSut(IToken token) {
			return new AdaStringRule(token);
		}
	};

	@Test
	public void testRule() {
		fixture.performTestCase("\"test\"", true);
		fixture.performTestCase("\"test", false);
		fixture.performTestCase("test\"", false);
		fixture.performTestCase("test", false);
		fixture.performTestCase("\"", false);
		fixture.performTestCase("\"\"", true);
		fixture.performTestCase("\"\n", true);
		fixture.performTestCase("\"\r", true);
		fixture.performTestCase("\n", false);
		fixture.performTestCase("\r", false);
	}
}
