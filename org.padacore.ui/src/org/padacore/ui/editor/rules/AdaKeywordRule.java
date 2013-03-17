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
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;

public class AdaKeywordRule implements IRule {

	private WordRule rule;

	private static String[] adaKeywords = { "abort", "abs", "abstract", "accept", "access",
			"aliased", "all", "and", "array", "at", "begin", "body", "case", "constant", "declare",
			"delay", "delta", "digits", "do", "else", "elsif", "end", "entry", "exception", "exit",
			"for", "function", "generic", "goto", "if", "in", "is", "interface", "limited", "loop",
			"mod", "new", "not", "null", "of", "or", "others", "out", "overriding", "package",
			"pragma", "private", "procedure", "protected", "raise", "range", "record", "rem",
			"renames", "requeue", "return", "reverse", "select", "separate", "subtype",
			"synchronized", "tagged", "task", "terminate", "then", "type", "until", "use", "when",
			"while", "with", "xor" };

	public AdaKeywordRule(IToken token) {
		this.rule = new WordRule(new AdaWordDetector());
		for (String keyword : adaKeywords) {
			this.rule.addWord(keyword, token);
		}
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return this.rule.evaluate(scanner);
	}

}