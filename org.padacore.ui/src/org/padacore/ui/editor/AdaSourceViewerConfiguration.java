package org.padacore.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
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

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		addDamagerRepairer(reconciler, Activator.getDefault().getAdaCodeScanner(),
				IDocument.DEFAULT_CONTENT_TYPE);
		addDamagerRepairer(reconciler, Activator.getDefault().getAdaCommentScanner(),
				AdaPartitionScanner.ADA_COMMENT_PARTITION);

		return reconciler;
	}

	private void addDamagerRepairer(PresentationReconciler reconciler, ITokenScanner scanner,
			String contentType) {
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		reconciler.setDamager(dr, contentType);
		reconciler.setRepairer(dr, contentType);
	}

	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new DefaultAnnotationHover();
	}
}
