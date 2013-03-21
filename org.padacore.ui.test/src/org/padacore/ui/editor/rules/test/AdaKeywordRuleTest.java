package org.padacore.ui.editor.rules.test;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.junit.Test;
import org.padacore.ui.editor.rules.AdaKeywordRule;

public class AdaKeywordRuleTest {

	private FixtureRule fixture = new FixtureRule() {
		public IRule createSut(IToken token) {
			return new AdaKeywordRule(token);
		}
	};

	public static String[] adaKeywords = { "abort", "abs", "abstract", "accept", "access",
			"aliased", "all", "and", "array", "at", "begin", "body", "case", "constant", "declare",
			"delay", "delta", "digits", "do", "else", "elsif", "end", "entry", "exception", "exit",
			"for", "function", "generic", "goto", "if", "in", "is", "interface", "limited", "loop",
			"mod", "new", "not", "null", "of", "or", "others", "out", "overriding", "package",
			"pragma", "private", "procedure", "protected", "raise", "range", "record", "rem",
			"renames", "requeue", "return", "reverse", "select", "separate", "subtype",
			"synchronized", "tagged", "task", "terminate", "then", "type", "until", "use", "when",
			"while", "with", "xor" };

	@Test
	public void testRule() {
		for (String keyword : adaKeywords) {
			fixture.performTestCase(keyword, true);
		}	
		fixture.performTestCase("test", false);	
	}

}
