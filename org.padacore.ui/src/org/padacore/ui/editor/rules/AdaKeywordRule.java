/*******************************************************************************
 * Copyright (c) 2005 AdaCore and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     AdaCore - Initial API and implementation
 *******************************************************************************/

package org.padacore.ui.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import org.padacore.ui.editor.rules.SyntaxUtils;

public class AdaKeywordRule implements IPredicateRule {

	private IToken fToken;

	public AdaKeywordRule(IToken token) {
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
	 * Evaluates the current word on the scanner, return the proper token if 
	 * it's a reserved word, false otherwise.
	 * 
	 * @param scanner
	 * @return
	 */
	public IToken doEvaluation(ICharacterScanner scanner) {		
		String word = "";
		
		scanner.unread();

		int c = scanner.read();
		
		if ((c >= 'A' && c <= 'Z') ||
				(c >= 'a' && c <= 'z') ||
				(c >= '0' && c <= '9') ||
				c == '_' ||
				c == '\'') {
			return Token.UNDEFINED;
		}
		
		while (true) {
			c = scanner.read();
			
			if ((c >= 'A' && c <= 'Z') ||
				(c >= 'a' && c <= 'z') ||
				(c >= '0' && c <= '9') || 
				c == '_') {
				word += (char) c;
			}
			else {
				scanner.unread();
				break;
			}
		}
		
		if (SyntaxUtils.isAdaKeyword(word)) {
			return fToken;
		}
		else {
			for (int i = 0; i < word.length(); ++i) {
				scanner.unread();
			}

			return Token.UNDEFINED;
		}
	}
}