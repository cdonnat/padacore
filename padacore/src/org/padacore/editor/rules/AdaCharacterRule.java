
package org.padacore.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Analyzes a code from a scanner and determines if there is an ada character
 * litteral.
 * 
 * @author ochem
 */
public class AdaCharacterRule implements IPredicateRule {

	private IToken fToken;

	public AdaCharacterRule(IToken token) {
		fToken = token;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	public IToken getSuccessToken() {
		return fToken;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
	 */
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return doEvaluation(scanner);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {
		return doEvaluation(scanner);
	}

	/**
	 * Return proper token if the scanner is on a character, UNDEFINED 
	 * otherwise. 
	 * 
	 * @param scanner
	 * @return
	 */
	public IToken doEvaluation(ICharacterScanner scanner) {
		if (scanner.read() != '\'') {
			scanner.unread();
			return Token.UNDEFINED;
		}

		if (scanner.read() == ICharacterScanner.EOF) {
			scanner.unread();
			scanner.unread();
			return Token.UNDEFINED;
		}

		if (scanner.read() != '\'') {
			scanner.unread();
			scanner.unread();
			scanner.unread();
			return Token.UNDEFINED;
		}

		return fToken;
	}

}