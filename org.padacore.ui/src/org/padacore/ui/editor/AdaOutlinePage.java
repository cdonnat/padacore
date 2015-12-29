package org.padacore.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * This class implements an Ada outline page corresponding to the standard
 * "Outline" view used with Ada editor.
 * 
 * @author RS
 *
 */
public class AdaOutlinePage extends ContentOutlinePage {

	private IDocument input;
	private ITreeContentProvider treeContentProvider;
	private ILabelProvider labelProvider;

	public AdaOutlinePage(IDocumentProvider documentProvider,
			TextEditor editor) {
		super();
		AdaOutlineContentProvider contentProvider = new AdaOutlineContentProvider(
				editor.getEditorInput());
		this.treeContentProvider = contentProvider;
		this.labelProvider = new AdaOutlineLabelProvider();
		this.input = (documentProvider.getDocument(editor.getEditorInput()));
		this.input.addDocumentListener(contentProvider);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		this.getTreeViewer().setContentProvider(this.treeContentProvider);
		this.getTreeViewer().setLabelProvider(this.labelProvider);
		this.getTreeViewer().setInput(this.input);
		this.getSite().setSelectionProvider(this.getTreeViewer());
	}

	@Override
	public void dispose() {
		if (this.getTreeViewer().getContentProvider() != null) {
			this.getTreeViewer().getContentProvider().dispose();
		}
		if (this.getTreeViewer().getContentProvider() != null) {
			this.getTreeViewer().getLabelProvider().dispose();
		}
		super.dispose();
	}

	public void refresh() {
		this.getTreeViewer().refresh();
		this.getTreeViewer().setInput(this.input);
	}
}
