/*******************************************************************************
 * Copyright (c) 2006 AdaCore
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


/**
 * Dectects a numeric literal, both integers and reals, including based literals.
 * 
 */
public class AdaNumberRule implements IPredicateRule {

	private IToken fToken;

	public AdaNumberRule(IToken token) {
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
	
	
	final protected int nextChar(ICharacterScanner scanner) {
		int result = scanner.read();
		scanner.unread();
		return result;
	} // nextChar
	
	
	final protected boolean inRange(int c, int low, int high) {
		return (c >= low) && (c <= high);
	} // inRange
	
	
	final protected boolean isIdentifierChar(int c) {
		return (inRange(c,'a','z') || inRange(c,'A','Z') || 
				inRange(c,'0','9') || (c == '_'));
	} // isIdentifierChar
	
		
	protected boolean scanInteger(CountingScanner scanner) {
		
		int C;
		// the next character scanned

		C = scanner.read();
		
		if (!inRange(C, '0', '9')) { 
			return false;
		} // if

		// Loop through remaining digits (allowing underlines)
		
		for (;;) {		
			C = scanner.read();

			if (C == '_') {
				
				if (nextChar(scanner) == '_') {
					return false;
				} // if
				
				C = scanner.read();

//				for (;;) {
//					C = scanner.read();
//					if (C != '_') {
//						break;
//					} // if
//					// error: no double underline allowed
//					return false;
//				} // loop

				if (!inRange(C, '0', '9')) {
					// error : digit expected
					return false;
				} // if

			} else {
				if (!inRange(C, '0', '9')) {
					// normal exit
					scanner.unread();
					break;
				} // if
			} // if
		} // loop
		return true;
	} // scanInteger
	

	public IToken doEvaluation(ICharacterScanner basicScanner) {
		
		CountingScanner scanner = new CountingScanner(basicScanner);

		int C;
		// Current source program character

		int Base_Char;
		// Either # or : (character at start of based number)
		
	    boolean Point_Scanned;
	    // Flag for decimal point scanned in numeric literal
	    
		
		if (!scanInteger(scanner)) {
			scanner.unwind();
			return Token.UNDEFINED;
		} // if

		Point_Scanned = false;
		
		// Various possibilities now, for continuing the literal, are period,
		// E/e (for exponent), or :/# (for based literal).
		
		C = scanner.read();
		
		if (C == '.') {
						
			// Scan out point, but do not scan past .. 

			while ((C == '.') && (nextChar(scanner) != '.')) {
				
				Point_Scanned  = true;
				C = scanner.read();

				if (!inRange(C, '0', '9')) {
					// Error_Msg("real literal cannot end with point", Scan_Ptr - 1);
					scanner.unwind();
					return Token.UNDEFINED;					
				} else {
					scanner.unread();
					if (!scanInteger(scanner)) {
						return Token.UNDEFINED;
					} // if
				} // if
				
			} // while

		// Based literal case. The base is the value we already scanned.
		// In the case of colon, we insist that the following character
		// is indeed an extended digit or a period. This catches a number
		// of common errors, as well as catching the well known tricky
		// bug otherwise arising from "x : integer range 1 .. 10:= 6;"

		} else if ((C == '#') ||
			       ((C == ':') &&
			    	((nextChar(scanner) == '.')              ||
			 	     (inRange(nextChar(scanner),'0','9'))    ||
				     (inRange(nextChar(scanner),'A','Z'))    ||
				     (inRange(nextChar(scanner),'a','z')))))     { 
			
			Base_Char = C;
			
			// Scan out extended integer [. integer]

			C = scanner.read();

			for (;;) {

				if (!inRange(C,'0','9') &&
				    !inRange(C,'A','F') &&
                    !inRange(C,'a','f')) {
					// Error_Msg_S ("extended digit expected");
					scanner.unwind();
					return Token.UNDEFINED;			
				} // if

				C = scanner.read();

				if (C == '_') {
					
					C = scanner.read();
					if (C == '_') {
						scanner.unwind();
						return Token.UNDEFINED;
					} // if					
					
//					for (;;) {
//						C = scanner.read();
//						if (C != '_') {
//							break;
//						} // if
//                		// Error_No_Double_Underline;
//						scanner.unwind();
//						return Token.UNDEFINED;
//					} // loop

				} else if (C == '.') {
					
					C = scanner.read();
					Point_Scanned = true;
					
				} else if (C == Base_Char) {
					
					// we need to do a read because at the end of the function we do an unread
					// and this way the trailing # is thus retained as part of the number and colored.
					// the problem with doing the read here, though, is that it eats any E after the #
					// so we have to prevent that happening.
					if ((nextChar(scanner) != 'e') && (nextChar(scanner) != 'E')) { 
						scanner.read();
					} // if
					break;
					
				} else if ((C=='#') || (C==':')) {	
					
					// Error_Msg_S ("based number delimiters must match");
					scanner.unwind();
					return Token.UNDEFINED;
					
				} else if (!isIdentifierChar(C)) {	
					
					// Error_Msg_S ("missing '#");
					// Error_Msg_S ("missing ':");
					scanner.unwind();
					return Token.UNDEFINED;
					
				} // if
				
			} // for

		} // if

		// Scan out exponent, if any
		
		if ((nextChar(scanner) == 'e') || (nextChar(scanner) == 'E')) { 
			
			scanner.read();  // consume the 'e'|'E'

			if (nextChar(scanner) == '+') {
			
				scanner.read();  // consume the '+'				
			
			} else if (nextChar(scanner) == '-') {
							
				if (!Point_Scanned) {
             		// Error_Msg_S ("negative exponent not allowed for integer literal");
					scanner.unwind();
					return Token.UNDEFINED;
				} // if
			
				scanner.read();  // consume the '-'

			} // if

			if (inRange(nextChar(scanner),'0','9')) {
				if (!scanInteger(scanner)) {
					scanner.unwind();
					return Token.UNDEFINED;
				} // if
			} else {
				// Error_Digit_Expected;
				scanner.unwind();
				return Token.UNDEFINED;
			} // if

		} // if
		
		// it is a numeric literal.
		// if we wanted to distinguish reals from integers we could just
		// check Point_Scanned
		
		scanner.unread(); // to unconsume whatever ended our scan
		return fToken;
	} // doEvaluation


	/**
	 * Evaluates the current word on the scanner, return the proper token if 
	 * it's a numeric literal, Token.UNDEFINED otherwise.
	 * 
	 * @param scanner
	 * @return
	 */
//	public IToken doEvaluation(ICharacterScanner scanner) {
//
//		int C;
//		// Current source program character
//
//		int Base_Char;
//		// Either # or : (character at start of based number)
//		
//	    boolean Point_Scanned;
//	    // Flag for decimal point scanned in numeric literal
//	    
//		
//		if (!scanInteger(scanner)) {
//			return Token.UNDEFINED;
//		} // if
//
//		Point_Scanned = false;
//		
//		// Various possibilities now, for continuing the literal, are period,
//		// E/e (for exponent), or :/# (for based literal).
//		
//		C = scanner.read();
//
//		if (C == '.') {
//						
//			// Scan out point, but do not scan past .. 
//
//			while ((C == '.') && (nextChar(scanner) != '.')) {
//				
//				Point_Scanned  = true;
//				C = scanner.read();
//
//				if (!inRange(C, '0', '9')) {
//					// Error_Msg("real literal cannot end with point", Scan_Ptr - 1);
//					scanner.unread();
//					return Token.UNDEFINED;					
//				} else {
//					scanner.unread();
//					if (!scanInteger(scanner)) {
//						return Token.UNDEFINED;
//					} // if
//				} // if
//				
//			} // while
//
//		// Based literal case. The base is the value we already scanned.
//		// In the case of colon, we insist that the following character
//		// is indeed an extended digit or a period. This catches a number
//		// of common errors, as well as catching the well known tricky
//		// bug otherwise arising from "x : integer range 1 .. 10:= 6;"
//
//		} else if ((C == '#') ||
//			       ((C == ':') &&
//			    	((nextChar(scanner) == '.')              ||
//			 	     (inRange(nextChar(scanner),'0','9'))    ||
//				     (inRange(nextChar(scanner),'A','Z'))    ||
//				     (inRange(nextChar(scanner),'a','z')))))     { 
//			
//			Base_Char = C;
//			
//			// Scan out extended integer [. integer]
//
//			C = scanner.read();
//
//			for (;;) {
//
//				if (!inRange(C,'0','9') &&
//				    !inRange(C,'A','F') &&
//                    !inRange(C,'a','f')) {
//					// Error_Msg_S ("extended digit expected");
//					scanner.unread();
//					return Token.UNDEFINED;			
//				} // if
//
//				C = scanner.read();
//
//				if (C == '_') {
//					
//					for (;;) {
//						C = scanner.read();
//						if (C != '_') {
//							break;
//						} // if
//                		// Error_No_Double_Underline;
//						scanner.unread();
//						return Token.UNDEFINED;
//					} // loop
//
//				} else if (C == '.') {
//					
//					C = scanner.read();
//					Point_Scanned = true;
//					
//				} else if (C == Base_Char) {					
//             		// Scan_Ptr := Scan_Ptr + 1;
//             		// exit;
//					break;
//					
//				} else if ((C=='#') || (C==':')) {					
//					// Error_Msg_S ("based number delimiters must match");
//             		// Scan_Ptr := Scan_Ptr + 1;
//             		// exit;
//					return Token.UNDEFINED;
//					
//				} else if (!isIdentifierChar(C)) {
//					
//					// Error_Msg_S ("missing '#");
//					// Error_Msg_S ("missing ':");
//					scanner.unread();
//					return Token.UNDEFINED;
//					
//				} // if
//			} // for
//
//		} // if
//
//		// Scan out exponent, if any
//		
//		if ((nextChar(scanner) == 'e') || (nextChar(scanner) == 'E')) { 
//			// Scan_Ptr := Scan_Ptr + 1;
//			scanner.read();  // consume the 'e'|'E'
//
//			if (nextChar(scanner) == '+') {
//			
//				// Scan_Ptr := Scan_Ptr + 1;
//				scanner.read();  // consume the '+'				
//			
//			} else if (nextChar(scanner) == '-') {
//							
//				if (!Point_Scanned) {
//             		// Error_Msg_S ("negative exponent not allowed for integer literal");
//					return Token.UNDEFINED;
//				} // if
//			
//				// Scan_Ptr := Scan_Ptr + 1;
//				scanner.read();  // consume the '-'
//
//			} // if
//
//			if (inRange(nextChar(scanner),'0','9')) {
//				if (!scanInteger(scanner)) {
//					return Token.UNDEFINED;
//				} // if
//			} else {
//				// Error_Digit_Expected;
//				scanner.unread();
//				return Token.UNDEFINED;
//			} // if
//
//		} // if
//		
//		// it is a numeric literal.
//		// if we wanted to distinguish reals from integers we could just
//		// check Point_Scanned
//		
//		return fToken;
//	} // doEvaluation
	
	
	public class CountingScanner implements ICharacterScanner {
		
		public CountingScanner (ICharacterScanner unwrappedScanner) {
			scanner = unwrappedScanner;
			count = 0;
		}
		
		public void unwind() {
			for (int k = count; k > 0; k--) {
				scanner.unread();				
			} // for
		}

		public int getColumn() {
			return scanner.getColumn();
		}

		public char[][] getLegalLineDelimiters() {
			return scanner.getLegalLineDelimiters();
		}

		public int read() {
			++count;
			return scanner.read();
		}

		public void unread() {
			--count;
			scanner.unread();
		}

		protected ICharacterScanner scanner;
		
		protected int count;
		
	} // CountingScanner
	
	
} // AdaNumberRule