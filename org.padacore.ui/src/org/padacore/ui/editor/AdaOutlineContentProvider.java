package org.padacore.ui.editor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.ada4j.api.Factory;
import org.ada4j.api.model.ICompilationUnit;
import org.ada4j.api.model.IPackage;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

public class AdaOutlineContentProvider implements ITreeContentProvider {

	private TreeViewer viewer;
	private IDocument document;
	private IFileEditorInput editorInput;

	public AdaOutlineContentProvider(IEditorInput editorInput) {
		if (editorInput instanceof IFileEditorInput) {
			this.editorInput = (IFileEditorInput) editorInput;
		}
	}

	private IDocumentListener documentListener = new IDocumentListener() {
		@Override
		public void documentChanged(DocumentEvent event) {
			if (!AdaOutlineContentProvider.this.viewer.getControl().isDisposed()) {
				AdaOutlineContentProvider.this.viewer.refresh();
			}
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}
	};

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;

		if (oldInput instanceof IDocument) {
			this.document.removeDocumentListener(this.documentListener);
		}

		if (newInput instanceof IDocument) {
			this.document = (IDocument) newInput;
			this.document.addDocumentListener(this.documentListener);
		}
	}

	private IDocument getDocument(Object inputElement) {
		IDocument document = null;

		if (inputElement instanceof IDocument) {
			document = (IDocument) inputElement;
		}

		return document;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object[] elements = {};
		IDocument document = this.getDocument(inputElement);

		if (document != null && this.editorInput != null) {
			Path editedFilePath = this.editorInput.getFile().getRawLocation()
					.toFile().toPath();
			elements = new ICompilationUnit[1];
			elements[0] = Factory.Create_Compilation_Unit(editedFilePath);
		}

		return elements;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		List<Object> children = new ArrayList<Object>();

		if (IPackage.class.isAssignableFrom(parentElement.getClass())) {
			IPackage parentPkg = (IPackage) parentElement;
			children.addAll(parentPkg.getPackages());
			children.addAll(parentPkg.getSubprograms());

		} else if (ICompilationUnit.class
				.isAssignableFrom(parentElement.getClass())) {
			ICompilationUnit parentUnit = (ICompilationUnit) parentElement;
			children.addAll(parentUnit.getPackages());
			children.addAll(parentUnit.getSubprograms());
		}

		return children.toArray();
	}

	@Override
	public Object getParent(Object element) {
		Object parent = null;

		if (IPackage.class.isAssignableFrom(element.getClass())) {
			IPackage childPkg = (IPackage) element;
			parent = childPkg.getParent();
		}

		return parent;
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length > 0;
	}

}
