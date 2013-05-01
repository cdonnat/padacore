package org.padacore.ui.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.padacore.ui.Activator;

public class NavigatorPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private class ExtensionsListEditor extends ListEditor {

		private final static String LIST_DELIMITER = ";";
		private Shell shell;

		public ExtensionsListEditor(Composite parent,
				IPreferenceStore preferenceStore) {
			super(IPreferenceConstants.NAVIGATOR_EXTENSIONS_PREF,
					"Extensions of files to display", parent);
			this.setPreferenceStore(preferenceStore);
		}

		@Override
		protected String createList(String[] items) {
			StringBuilder stringBuilder = new StringBuilder();

			for (String item : items) {
				stringBuilder.append(item);
				stringBuilder.append(LIST_DELIMITER);
			}

			stringBuilder.deleteCharAt(stringBuilder.length() - 1);

			return stringBuilder.toString();
		}

		@Override
		protected String getNewInputObject() {
			String valueToAddInList = "";

			InputDialog inputDialog = new InputDialog(this.shell,
					"Add a new file extension",
					"File extension (without leading dot)", "", null);

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
		ListEditor extensionsList = new ExtensionsListEditor(
				this.getFieldEditorParent(), Activator.getDefault()
						.getPreferenceStore());
		this.addField(extensionsList);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
