package org.padacore.ui.editor;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class AdaEditor extends TextEditor {

	private AdaOutlinePage outlinePage;

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new AdaSourceViewerConfiguration());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class required) {

		Object adapter;

		if (IContentOutlinePage.class.equals(required)) {
			if (this.outlinePage == null) {
				this.outlinePage = new AdaOutlinePage(getDocumentProvider(),
						this);
			}
			adapter = this.outlinePage;
		} else {
			adapter = super.getAdapter(required);
		}

		return adapter;
	}
}
