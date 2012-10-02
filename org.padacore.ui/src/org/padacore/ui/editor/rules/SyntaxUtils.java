/******************************************************************************
 * Copyright (C) 2006-2009, AdaCore
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     AdaCore - Initial API and implementation
 *****************************************************************************/

package org.padacore.ui.editor.rules;

public class SyntaxUtils {

	/**
	 * Returns true if the word given in parameter is a legal Ada identifer.
	 *
	 * @param word
	 * @return
	 */
	public static boolean isAdaIdentifier(String s) {
		final int first = 0;
		final int last = s.length() - 1;
        if (s.length() == 0 ||
        	// must start with a letter
            !Character.isLetter(s.charAt(first)) ||
            // must end with a letter or digit
            !Character.isLetterOrDigit(s.charAt(last)))
        {
            return false;
        } // if
        for (int k=first+1; k < last; k++) {
            if (Character.isLetterOrDigit(s.charAt(k))) {
                continue;
            } // if
            if (s.charAt(k) == '_') {
            	if (s.charAt(k-1) == '_') {
            		return false;
            	} else {
                	continue;
            	} // if
            } // if
            // not a valid letter, digit, or underscore...
            return false;
        } // for
        return true;
    } // isAdaIdentifier


	/**
	 * Returns true if the word given in parameter is an Ada reserved word.
	 *
	 * @param word
	 * @return
	 */
	public static boolean isAdaKeyword(String word) {
		if (word.equals ("")) {
			return false;
		} // if
		for (int i = 0; i < fgAdaKeywords.length; i++) {
			if (fgAdaKeywords[i].equals(word.toLowerCase())) {
				return true;
			} // if
		} // for
		return false;
	} // isAdaKeyword


	public static String[] fgAdaKeywords = { "abort", "abs", "abstract",
		"accept", "access", "aliased", "all", "and", "array", "at",
		"begin", "body", "case", "constant", "declare", "delay", "delta",
		"digits", "do", "else", "elsif", "end", "entry", "exception",
		"exit", "for", "function", "generic", "goto", "if", "in", "is",
		"interface", "limited", "loop", "mod", "new", "not", "null", "of",
		"or", "others", "out", "overriding", "package", "pragma",
		"private", "procedure", "protected", "raise", "range", "record",
		"rem", "renames", "requeue", "return", "reverse", "select",
		"separate", "subtype", "synchronized", "tagged", "task", "terminate",
		"then", "type", "until", "use", "when", "while", "with", "xor" };

} // SyntaxUtils
