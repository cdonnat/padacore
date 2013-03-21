package org.padacore.ui.editor.rules.test;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.junit.Test;
import org.padacore.ui.editor.rules.AdaCommentRule;

public class AdaCommentRuleTest {

	private FixtureRule fixture = new FixtureRule() {
		public IRule createSut(IToken token) {
			return new AdaCommentRule(token);
		}
	};

	@Test
	public void testRule() {
		fixture.performTestCase("--", true);
		fixture.performTestCase("test--", false);
		fixture.performTestCase("--test", true);
		fixture.performTestCase("---", true);
		fixture.performTestCase("\"", false);
	}

}
