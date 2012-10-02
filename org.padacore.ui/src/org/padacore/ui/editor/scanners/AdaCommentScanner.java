package org.padacore.ui.editor.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class AdaCommentScanner extends RuleBasedScanner {

	public AdaCommentScanner() {

		setDefaultReturnToken(new Token(new TextAttribute(new Color(
				Display.getCurrent(), new RGB(0, 0, 200)))));
	}
}