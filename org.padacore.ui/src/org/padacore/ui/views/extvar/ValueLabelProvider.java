package org.padacore.ui.views.extvar;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.padacore.core.gnat.ScenarioItem;

final class ValueLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		ScenarioItem item = (ScenarioItem) element;
		return item.getValue();
	}

}