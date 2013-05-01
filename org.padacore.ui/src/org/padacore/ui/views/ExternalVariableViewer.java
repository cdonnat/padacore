package org.padacore.ui.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.gnat.ScenarioItem;

public class ExternalVariableViewer implements Comparable<ExternalVariableViewer> {

	private Scenario scenario;
	private ScenarioItem item;
	private Label label;
	private ComboViewer viewer;

	public ExternalVariableViewer(Composite parent, Scenario scenario, ScenarioItem item) {
		this.scenario = scenario;
		this.item = item;
		this.label = new Label(parent, SWT.NONE);
		this.label.setText(item.getName());

		this.viewer = new ComboViewer(parent, SWT.READ_ONLY);
		this.viewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.viewer.setContentProvider(ArrayContentProvider.getInstance());

		if (item.isTyped()) {
			this.viewer.setInput(item.getType().getValues());
		}
		this.viewer.setSelection(new StructuredSelection(item.getValue()));
	}

	public void dispose() {
		this.label.dispose();
		this.viewer.getCombo().dispose();
	}

	@Override
	public int compareTo(ExternalVariableViewer o) {
		return this.item.getName().compareTo(o.item.getName());
	}
}
