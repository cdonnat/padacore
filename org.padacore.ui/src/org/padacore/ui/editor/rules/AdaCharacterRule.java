package org.padacore.ui.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Analyzes a code from a scanner and determines if there is an Ada character
 * litteral.
 */
public class AdaCharacterRule implements IRule {

	private IToken successToken;

	public AdaCharacterRule(IToken successToken) {
		this.successToken = successToken;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		SmartCharacterScanner smartScanner = new SmartCharacterScanner(scanner);
		IToken result = Token.UNDEFINED;

		if (smartScanner.read() == '\'') {
			smartScanner.read();
			if (smartScanner.read() == '\'') {
				return this.successToken;
			}
		}
		smartScanner.reset(); 
		return result;
	}

}