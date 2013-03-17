package org.padacore.ui.editor.rules.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.mockito.Mockito;

public abstract class FixtureRule {

	private IToken token;
	private RuleBasedScanner scanner;
	private Document document;
	private IRule sut;

	public FixtureRule() {
		this.token = Mockito.mock(IToken.class);
		this.scanner = new RuleBasedScanner();
		this.sut = this.createSut(this.token);
		this.scanner.setRules(new IRule[] { this.sut });
		this.document = new Document();
	}

	public abstract IRule createSut(IToken token);

	public void performTestCase(String input, boolean expectedMatch) {
		this.document.set(input);
		this.scanner.setRange(this.document, 0, this.document.getLength());

		IToken computedToken = this.scanner.nextToken();
		assertEquals(expectedMatch, this.token == computedToken);
	}
}
