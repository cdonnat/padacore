package org.padacore.ui.views.extvar;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.Scenario;

public class ContentProvider implements IStructuredContentProvider {

	private Scenario scenario;
	private GnatAdaProject project;
	private Object[] nothing;

	public ContentProvider(Scenario scenario) {
		this.scenario = scenario;
		this.nothing = new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.project = (GnatAdaProject) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object[] res = this.nothing;
		if (inputElement != null) {
			res = this.scenario.getExternalVariablesFor(this.project).toArray();
		}
		return res;
	}
}
