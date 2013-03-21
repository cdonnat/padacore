package org.padacore.ui.editor.scanners;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.padacore.ui.editor.rules.AdaCommentRule;

public class AdaPartitionScanner extends RuleBasedPartitionScanner {

	public final static String ADA_COMMENT_PARTITION = "ADA_COMMENT_PARTITION";

	public final static String[] PARTITION_TYPES = new String[] { IDocument.DEFAULT_CONTENT_TYPE,
			ADA_COMMENT_PARTITION };

	public final static String ADA_PARTITIONNING = "ADA_PARTITIONNING";

	public AdaPartitionScanner() {
		super();
		IToken token = new Token(ADA_COMMENT_PARTITION);
		IPredicateRule[] rules = { new AdaCommentRule(token) };
		this.setPredicateRules(rules);
	}
}
