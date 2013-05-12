package org.padacore.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.padacore.ui.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();
		preferenceStore.setDefault(
				IPreferenceConstants.NAVIGATOR_SOURCE_EXTENSIONS, "ads;adb");
		preferenceStore.setDefault(
				IPreferenceConstants.NAVIGATOR_OBJECT_EXTENSIONS, "o;ali");
		preferenceStore.setDefault(
				IPreferenceConstants.NAVIGATOR_EXEC_EXTENSIONS, "exe");
		preferenceStore.setDefault(
				IPreferenceConstants.NAVIGATOR_FILE_WITHOUT_EXTENSION, false);
	}

}
