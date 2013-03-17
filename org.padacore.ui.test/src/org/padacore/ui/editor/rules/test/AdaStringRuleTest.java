package org.padacore.ui.editor.rules.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.padacore.ui.editor.rules.AdaStringRule;

public class AdaStringRuleTest {

	private IToken token;
	private RuleBasedScanner scanner;
	private Document document;
	private AdaStringRule sut;

	@Before
	public void setUp() {
		this.token = Mockito.mock(IToken.class);
		this.scanner = new RuleBasedScanner();
		this.sut = new AdaStringRule(this.token);
		this.scanner.setRules(new IRule[] { this.sut });
		this.document = new Document();
	}

	@Test
	public void testRule() {
		test("\"test\"", true);
		test("\"test", false);
		test("test\"", false);
		test("test", false);
		test("\"", false);
		test("\"\"", true);
		test("\"\n", true);
		test("\"\r", true);
		test("\n", false);
		test("\r", false);
	}

	private void test(String input, boolean expectedMatch) {
		this.document.set(input);
		this.scanner.setRange(this.document, 0, this.document.getLength());

		IToken computedToken = this.scanner.nextToken();
		assertEquals(expectedMatch, this.token == computedToken);
	}

}
