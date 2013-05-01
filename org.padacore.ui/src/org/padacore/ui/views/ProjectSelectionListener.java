package org.padacore.ui.views;

import java.util.Observable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class ProjectSelectionListener extends Observable implements ISelectionListener {

	public ProjectSelectionListener() {
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService();
		selectionService.addPostSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IProject project = getCurrentSelectedProject();
		if (project != null) {
			this.setChanged();
			this.notifyObservers(project);
		}
	}

	private static IProject getCurrentSelectedProject() {
		IProject project = null;
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService();

		ISelection selection = selectionService.getSelection();

		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();

			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			}
		}
		return project;
	}

}
