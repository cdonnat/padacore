package org.padacore.ui.editor.rules;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;

/**
 * Detects Ada comments.
 * 
 */
public class AdaCommentRule implements IPredicateRule {

	private IRule rule;
	private IToken token;

	public AdaCommentRule(IToken token) {
		this.token = token;
		this.rule = new EndOfLineRule("--", token);
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return this.rule.evaluate(scanner);
	}

	@Override
	public IToken getSuccessToken() {
		return this.token;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return this.evaluate(scanner);
	}
}