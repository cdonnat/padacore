package org.padacore.ui.views;

import java.util.ArrayList;
import java.util.Set;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.gnat.ScenarioItem;

public class ExternalVariablesEditingSupport extends EditingSupport {

	private final TableViewer viewer;
	private final Scenario scenario;

	public ExternalVariablesEditingSupport(TableViewer viewer, Scenario scenario) {
		super(viewer);
		this.viewer = viewer;
		this.scenario = scenario;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		Composite parent = (Composite) this.viewer.getTable();
		ScenarioItem var = (ScenarioItem) element;
		if (var.isTyped()) {
			Set<String> values = var.getType().getValues();
			String[] res = new String[values.size()];
			values.toArray(res);
			return new ComboBoxCellEditor(parent, res, SWT.READ_ONLY | SWT.BORDER);
		} else {
			return new TextCellEditor(parent);
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ScenarioItem var = (ScenarioItem) element;
		if (var.isTyped()) {
			return new ArrayList<>(var.getType().getValues()).indexOf(var.getValue());
		} else {
			return ((ScenarioItem) element).getValue();
		}

	}

	@Override
	protected void setValue(Object element, Object value) {
		ScenarioItem var = (ScenarioItem) element;
		String valueToSet;

		if (var.isTyped()) {
			valueToSet = (String) var.getType().getValues().toArray()[(int) value];
		} else {
			valueToSet = (String) value;
		}
		this.scenario.setExternalVariableValueFor((GnatAdaProject) this.viewer.getInput(),
				var.getName(), valueToSet);

		this.viewer.refresh();
		//this.viewer.update(element, null);
	}
}