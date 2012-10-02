package org.padacore.ui.editor.scanners;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;


public class AdaPartitionScanner extends RuleBasedPartitionScanner {

	public final static String ADA_COMMENT_PARTITION = "ADA_COMMENT_PARTITION";
	
	public final static String[] PARTITION_TYPES = new String[] {
			IDocument.DEFAULT_CONTENT_TYPE, ADA_COMMENT_PARTITION};

	public final static String ADA_PARTITIONNING = "ADA_PARTITIONNING";

	public AdaPartitionScanner() {
		super();
		// Create the various tokens used for the partition
		IToken comment = new Token(ADA_COMMENT_PARTITION);

		// Create rules associating token to patterns
		IPredicateRule[] rules = {
				new EndOfLineRule("--", comment)				
				};

		setPredicateRules(rules);
	}
}
