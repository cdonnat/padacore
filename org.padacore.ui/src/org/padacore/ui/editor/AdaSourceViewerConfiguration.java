package org.padacore.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import org.padacore.ui.Activator;
import org.padacore.ui.editor.scanners.AdaPartitionScanner;

public class AdaSourceViewerConfiguration extends SourceViewerConfiguration {

	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return AdaPartitionScanner.ADA_PARTITIONNING;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return AdaPartitionScanner.PARTITION_TYPES;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr;

		dr = new DefaultDamagerRepairer(Activator.getDefault()
				.getAdaCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(Activator.getDefault()
				.getAdaCommentScanner());
		reconciler.setDamager(dr, AdaPartitionScanner.ADA_COMMENT_PARTITION);
		reconciler.setRepairer(dr, AdaPartitionScanner.ADA_COMMENT_PARTITION);

		return reconciler;
	}

}
