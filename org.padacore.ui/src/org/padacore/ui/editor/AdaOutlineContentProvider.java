package org.padacore.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.ada4j.api.Factory;
import org.ada4j.api.model.ICompilationUnit;
import org.ada4j.api.model.IPackage;
import org.ada4j.api.model.ISubprogram;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

/**
 * This class provides contents for the standard "Outline" view used with Ada
 * editor (packages, procedures...).
 * 
 * @author RS
 *
 */
public class AdaOutlineContentProvider
		implements ITreeContentProvider, IDocumentListener {

	private TreeViewer viewer;
	private IDocument document;
	private IFileEditorInput editorInput;

	public AdaOutlineContentProvider(IEditorInput editorInput) {
		if (editorInput instanceof IFileEditorInput) {
			this.editorInput = (IFileEditorInput) editorInput;
		}
	}

	@Override
	public void dispose() {
		if (this.viewer != null && !this.viewer.getControl().isDisposed()) {
			this.viewer.getControl().dispose();
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		this.viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		if (oldInput instanceof IDocument) {
			this.document.removeDocumentListener(this);
		}

		if (newInput instanceof IDocument) {
			this.document = (IDocument) newInput;
			this.document.addDocumentListener(this);
		}

		if (!this.viewer.getControl().isDisposed()) {
			this.viewer.refresh();
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
			String editedFileName = this.editorInput.getFile().getRawLocation()
					.toFile().getName();

			ICompilationUnit compilationUnit = Factory
					.Create_Compilation_Unit(document.get(), editedFileName);
			if (compilationUnit.getMainSubprogram() != null) {
				elements = new ISubprogram[1];
				elements[0] = compilationUnit.getMainSubprogram();
			} else {
				// it's a package
				elements = new IPackage[1];
				elements[0] = compilationUnit.getRootPackage();
			}
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
			if (parentUnit.getMainSubprogram() != null) {
				children.add(parentUnit.getMainSubprogram());
			} else {
				children.add(parentUnit.getRootPackage());
			}
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

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	@Override
	public void documentChanged(DocumentEvent event) {

	}

}
