package org.padacore.ui.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.gnat.ScenarioItem;

import com.google.common.base.Preconditions;

public class ExternalVariableViewer implements ISelectionChangedListener, FocusListener {

	private GnatAdaProject project;
	private Scenario scenario;
	private ScenarioItem item;
	private Label label;

	private ComboViewer comboBox;
	private Text text;

	public ExternalVariableViewer(Composite parent, GnatAdaProject project, Scenario scenario,
			ScenarioItem item) {
		this.project = project;
		this.scenario = scenario;
		this.item = item;
		this.label = new Label(parent, SWT.NONE);
		this.label.setText(Capitalize(item.getName()));

		if (item.isTyped()) {
			this.addComboBox(parent);
		} else {
			this.addTextBox(parent);
		}
	}

	private void addTextBox(Composite parent) {
		Preconditions.checkState(!this.item.isTyped());

		this.text = new Text(parent, SWT.BORDER);
		this.text.setText(this.item.getValue());
		this.text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.text.addFocusListener(this);
	}

	private void addComboBox(Composite parent) {
		Preconditions.checkState(this.item.isTyped());
		this.comboBox = new ComboViewer(parent, SWT.READ_ONLY);
		this.comboBox.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.comboBox.setContentProvider(ArrayContentProvider.getInstance());
		this.comboBox.setInput(item.getType().getValues());
		this.comboBox.setSelection(new StructuredSelection(item.getValue()));
		this.comboBox.addSelectionChangedListener(this);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		this.setValue((String) selection.getFirstElement());
	}

	public void dispose() {
		this.label.dispose();
		if (this.item.isTyped()) {
			this.comboBox.getCombo().dispose();
		} else {
			this.text.dispose();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// nothing is done!
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.text.getText() != this.item.getValue()) {
			this.setValue(this.text.getText());
		}
	}

	private void setValue(String value) {
		this.scenario.setExternalVariableValueFor(this.project, this.item.getName(), value);
	}

	static private String Capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
}
