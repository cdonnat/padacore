package org.padacore.editor;

import org.eclipse.ui.editors.text.TextEditor;

public class AdaEditor extends TextEditor {

	protected void initializeEditor() {
        super.initializeEditor();
        setSourceViewerConfiguration(new AdaSourceViewerConfiguration());
    }   
}