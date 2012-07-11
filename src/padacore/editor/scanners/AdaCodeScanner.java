package padacore.editor.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import padacore.editor.rules.AdaCharacterRule;
import padacore.editor.rules.AdaKeywordRule;
import padacore.editor.rules.AdaNumberRule;
import padacore.editor.rules.AdaStringRule;
import padacore.editor.rules.AdaWordDetector;

public class AdaCodeScanner extends RuleBasedScanner {

	
    public final static String DEFAULT = "ADA_DEFAULT";
    public final static String STRING = "ADA_STRING";
 
    public final static String[] ALL = new String[] {
        DEFAULT, STRING
        };
    
    public AdaCodeScanner() {
    	Token keyword = new Token(new TextAttribute(new Color(Display
				.getCurrent(), new RGB (127,0,85)), null,
				SWT.BOLD));		

		IToken string = new Token(new TextAttribute(new Color(Display
				.getCurrent(), new RGB(42,0,255))));

		IToken numericLiteral = new Token(new TextAttribute(new Color(Display
				.getCurrent(), new RGB(0,0,0))));

		IToken wordDefault = new Token(new TextAttribute(new Color(Display
				.getCurrent(), new RGB(0,0,0))));
			
		
        IRule[] rules = {
        		new AdaCharacterRule(string),
        		new AdaStringRule(string),
        		new AdaKeywordRule(keyword),
        		new WordRule(new AdaWordDetector(), wordDefault),
        		new AdaNumberRule(numericLiteral)
        };

        setRules(rules);  
    }
}
