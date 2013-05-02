package org.padacore.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.padacore.core.gnat.ScenarioItem;

final class ExternalVariablesLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		ScenarioItem item = (ScenarioItem) element;
		return Capitalize(item.getName());
	}

	static private String Capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
}