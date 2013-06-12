package org.padacore.ui.preferences;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.padacore.ui.Activator;

import com.google.common.base.Joiner;

public class NavigatorPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private class ExtensionsListEditor extends ListEditor {

		private final static String LIST_DELIMITER = ";";
		private Shell shell;

		public ExtensionsListEditor(Composite parent,
				IPreferenceStore preferenceStore, String filesDescription,
				String preferenceName) {
			super(preferenceName, filesDescription + " extensions", parent);
			this.setPreferenceStore(preferenceStore);
		}

		@Override
		protected String createList(String[] items) {
			Joiner joiner = Joiner.on(LIST_DELIMITER);

			return joiner.join(items);
		}

		@Override
		protected String getNewInputObject() {
			String valueToAddInList = "";

			InputDialog inputDialog = new InputDialog(this.shell,
					"Add a new file extension",
					"File extension (without leading dot)", "",
					new IInputValidator() {

						@Override
						public String isValid(String newText) {
							String errorMsg = null;

							if (newText.length() == 0) {
								errorMsg = "Empty extension is not allowed. Please use * as a wildcard for any non-empty extension";
							} else if (newText.indexOf('.') != -1) {
								errorMsg = "Please enter a file extension without leading dot";
							}

							return errorMsg;
						}
					});

			int returnCode = inputDialog.open();

			if (returnCode == InputDialog.OK) {
				valueToAddInList = inputDialog.getValue();
			}

			return valueToAddInList;
		}

		@Override
		protected String[] parseString(String stringList) {
			return stringList.split(LIST_DELIMITER);
		}

	}

	public NavigatorPreferencePage() {
		super(FieldEditorPreferencePage.FLAT);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();

		ListEditor sourceExtensions = new ExtensionsListEditor(
				this.getFieldEditorParent(), preferenceStore, "Source files",
				IPreferenceConstants.NAVIGATOR_SOURCE_EXTENSIONS);

		ListEditor objectExtensions = new ExtensionsListEditor(
				this.getFieldEditorParent(), preferenceStore, "Object files",
				IPreferenceConstants.NAVIGATOR_OBJECT_EXTENSIONS);

		ListEditor execExtensions = new ExtensionsListEditor(
				this.getFieldEditorParent(), preferenceStore,
				"Executable files",
				IPreferenceConstants.NAVIGATOR_EXEC_EXTENSIONS);

		BooleanFieldEditor filesWithoutExtensions = new BooleanFieldEditor(
				IPreferenceConstants.NAVIGATOR_FILE_WITHOUT_EXTENSION,
				"Display files without extension", this.getFieldEditorParent());

		this.addField(sourceExtensions);
		this.addField(objectExtensions);
		this.addField(execExtensions);
		this.addField(filesWithoutExtensions);

	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
