package org.padacore.editor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

import org.padacore.Activator;
import org.padacore.editor.scanners.AdaPartitionScanner;

public class AdaDocumentSetupParticipant implements IDocumentSetupParticipant {

	public void setup(IDocument document) {
		IDocumentPartitioner partitioner = new FastPartitioner(Activator
				.getDefault().getAdaPartitionScanner(),
				AdaPartitionScanner.PARTITION_TYPES);
		
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			extension3.setDocumentPartitioner(
					AdaPartitionScanner.ADA_PARTITIONNING, partitioner);
		}
		
		partitioner.connect(document);
	}

}