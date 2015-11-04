package org.padacore.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class AdaOutlinePage extends ContentOutlinePage {

	private IDocument input;
	private ITreeContentProvider treeContentProvider;
	private ILabelProvider labelProvider;

	public AdaOutlinePage(IDocumentProvider documentProvider, TextEditor editor) {
		super();
		this.treeContentProvider = new AdaOutlineContentProvider(editor.getEditorInput());
		this.labelProvider = new AdaOutlineLabelProvider();
		this.input = (documentProvider.getDocument(editor.getEditorInput()));
		if (this.getTreeViewer() != null) {
			this.getTreeViewer().setInput(input);
		}
	}

	@Override
	public void createControl(Composite parent) {

		super.createControl(parent);

		this.getTreeViewer().setContentProvider(this.treeContentProvider);
		this.getTreeViewer().setLabelProvider(this.labelProvider);

		this.getTreeViewer().setInput(this.input);

		this.getSite().setSelectionProvider(this.getTreeViewer());
	}
}
