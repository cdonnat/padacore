
package padacore.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Detects comments and comment-based annotations such as those of SPARK.
 * 
 */
public class AdaCommentRule implements IPredicateRule {

	private final IToken commentToken;
	private final IToken annotationToken;
	
	private boolean isAnnotation;

	public AdaCommentRule(IToken tokenComment, IToken tokenAnnotation) {
		commentToken = tokenComment;
		annotationToken = tokenAnnotation;
	}

	public IToken getSuccessToken() {
		if (isAnnotation) {
			return annotationToken;
		} else {
			return commentToken;
		} // if
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return doEvaluation(scanner);
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return doEvaluation(scanner);
	}
	
	/**
	 * Return proper token if the scanner is on a basic comment or 
	 * on an annotation, UNDEFINED otherwise.
	 * 
	 * Annotations are comments starting with "--" as usual,
	 * but followed immediately by some non-alphabetic character, such
	 * as #|$ etc.  These will cover SPARK annotations in particular,
	 * but anything else as well.
	 * 
	 * @param scanner
	 * @return
	 */
	public IToken doEvaluation(ICharacterScanner scanner) {
		if (scanner.read() != '-') {
			scanner.unread();
			return Token.UNDEFINED;
		}
		if (scanner.read() != '-') {
			scanner.unread();
			return Token.UNDEFINED;
		}
		
		isAnnotation = false;
		
		int c = scanner.read();
		
		if (endOfLine(c, scanner)) {
			return getSuccessToken();
		}

		if (!isAlphanumeric(c) && c != '-') {
			// we test for a third dash so that a line of dashes is 
			// treated as a comment
			isAnnotation = true;
		} // if
		
		while (!endOfLine(c, scanner)) {
			c = scanner.read();
		}

		return getSuccessToken();
	}
	
	
	protected boolean isAlphanumeric(int c) {
		char C = (char)c;
		return Character.isLetterOrDigit(C) || Character.isWhitespace(C);
	}
	
	protected boolean endOfLine(int c, ICharacterScanner scanner) {
		return (c == '\n' || c == '\r' || c == ICharacterScanner.EOF
				|| scanner.getColumn() == -1);
	}
	
	
}