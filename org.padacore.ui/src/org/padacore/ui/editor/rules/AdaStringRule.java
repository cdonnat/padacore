package org.padacore.ui.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

public class AdaStringRule implements IRule {

	private IRule rule;

	public AdaStringRule(IToken successToken) {
		this.rule = new SingleLineRule("\"", "\"", successToken, '\\');
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return this.rule.evaluate(scanner);
	}
}