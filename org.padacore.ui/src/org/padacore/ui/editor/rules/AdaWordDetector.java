package org.padacore.ui.editor.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class AdaWordDetector implements IWordDetector {
				
	public boolean isWordStart(char c) {
		return Character.isLetter(c);
	} // isWordStart
		
	public boolean isWordPart(char c) {
		return Character.isLetterOrDigit(c) || c == '_';
	} // isWordPart
		
} // AdaWordDetector
