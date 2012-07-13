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

package org.padacore.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Basic string decection.
 * 
 * @author ochem
 */
public class AdaStringRule implements IPredicateRule {

	private IToken fToken;

	public AdaStringRule(IToken token) {
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
	 * Return proper token if the scanner is on a string, UNDEFINED 
	 * otherwise. 
	 * 
	 * @param scanner
	 * @return
	 */
	public IToken doEvaluation(ICharacterScanner scanner) {

		if (scanner.read() != '\"') {
			scanner.unread();
			return Token.UNDEFINED;
		}

		while (true) {
			int c = scanner.read();

			// TODO : there seems to be a bug on getColumn (), which
			// is not supposed to return -1, but does at the end of a
			// line. 
			if (c == '\n' || c == '\r' || c == ICharacterScanner.EOF
					|| scanner.getColumn() == -1) {
				
				return fToken;
			}

			if (c == '\"') {
				c = scanner.read();

				if (c != '\"') {
					scanner.unread();
					break;
				}
			}
		}

		return fToken;
	}
}