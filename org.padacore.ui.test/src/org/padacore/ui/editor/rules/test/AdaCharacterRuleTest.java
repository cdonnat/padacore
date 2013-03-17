package org.padacore.ui.editor.rules.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.padacore.ui.editor.rules.AdaCharacterRule;

public class AdaCharacterRuleTest {

	private IToken token;
	private RuleBasedScanner scanner;
	private Document document;
	private AdaCharacterRule sut;

	@Before
	public void setUp() {
		this.token = Mockito.mock(IToken.class);
		this.scanner = new RuleBasedScanner();
		this.sut = new AdaCharacterRule(this.token);
		this.scanner.setRules(new IRule[] { this.sut });
		this.document = new Document();
	}

	@Test
	public void testRule() {
		test ("\'c\'", true);
		test ("\'c", false);
		test ("c\'", false);
		test ("\'\'", false);
		test ("\'test\'", false);		
	}


	private void test(String input, boolean expectedMatch) {
		this.document.set(input);
		this.scanner.setRange(this.document, 0, this.document.getLength());

		IToken computedToken = this.scanner.nextToken();
		assertEquals(this.token == computedToken, expectedMatch);
		assertEquals(this.scanner.getTokenLength() == 3, expectedMatch);
	}

}
