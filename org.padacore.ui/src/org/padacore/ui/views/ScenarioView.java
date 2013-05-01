package org.padacore.ui.views;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.gnat.ScenarioItem;
import org.padacore.core.project.PropertiesManager;
import org.padacore.ui.Activator;

import com.google.common.base.Preconditions;

public class ScenarioView extends ViewPart implements Observer {

	Scenario scenario;
	GnatAdaProject currentProject;
	Set<ExternalVariableViewer> viewers;
	Composite parent;

	@Override
	public void update(Observable o, Object arg) {
		Preconditions.checkArgument(arg instanceof IProject);

		PropertiesManager propertyManager = new PropertiesManager((IProject) arg);
		GnatAdaProject selectedProject = (GnatAdaProject) propertyManager.getAdaProject();

		if (this.currentProject != selectedProject) {
			this.currentProject = selectedProject;
			this.removePreviousExternalVariables();
			this.addExternalVariablesFromSelectedProject();
			this.refreshView();
		}
	}

	private void removePreviousExternalVariables() {
		for (ExternalVariableViewer viewer : this.viewers) {
			viewer.dispose();
		}
		this.viewers.clear();
	}

	private void addExternalVariablesFromSelectedProject() {
		for (ScenarioItem item : this.scenario.getExternalVariablesFor(this.currentProject)) {
			this.viewers.add(new ExternalVariableViewer(this.parent, this.scenario, item));
		}
	}

	private void refreshView() {
		this.parent.pack();
		this.parent.layout(true);
	}

	@Override
	public void createPartControl(Composite parent) {
		Activator.getDefault().getProjectSelectionListener().addObserver(this);

		this.scenario = new Scenario();
		this.viewers = new HashSet<>();
		this.parent = parent;
		
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
	}

	@Override
	public void setFocus() {
	}

}
