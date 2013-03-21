package org.padacore.ui.editor.rules.test;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.junit.Test;
import org.padacore.ui.editor.rules.AdaCharacterRule;

public class AdaCharacterRuleTest {

	private FixtureRule fixture = new FixtureRule() {
		public IRule createSut(IToken token) {
			return new AdaCharacterRule(token);
		}
	};

	@Test
	public void testRule() {
		this.fixture.performTestCase("\'c\'", true);
		this.fixture.performTestCase("\'\'\'", true);
		this.fixture.performTestCase("\'c", false);
		this.fixture.performTestCase("c\'", false);
		this.fixture.performTestCase("\'\'", false);
		this.fixture.performTestCase("\'test\'", false);
	}
}
