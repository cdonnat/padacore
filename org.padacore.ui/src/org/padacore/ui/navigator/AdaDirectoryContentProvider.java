package org.padacore.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.utils.ErrorLog;

public class AdaDirectoryContentProvider implements ITreeContentProvider {

	public AdaDirectoryContentProvider() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return this.getChildren(inputElement);
	}

	private IFolder[] getSourceChildrenFolders(IFolder folder) {
		IProject project = folder.getProject();
		IResource[] members;
		PropertiesManager propertiesManager = new PropertiesManager(project);
		IAdaProject adaProject = propertiesManager.getAdaProject();
		List<IFolder> subFolders = new ArrayList<IFolder>();
		IFolder currentFolder;

		try {
			members = project.members();

			for (IResource resource : members) {
				if (resource instanceof IFolder) {
					currentFolder = (IFolder) resource;
					if (adaProject.getSourceDirectoriesPaths().contains(
							currentFolder.getLocation())) {
						subFolders.add((IFolder) resource);
					}
				}
			}

		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		return subFolders.toArray(new IFolder[0]);

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Assert.isLegal(parentElement instanceof IFolder
				|| parentElement instanceof IProject);

		IResource[] children = new IResource[0];

		if (parentElement instanceof IFolder) {
			IFolder folder = (IFolder) parentElement;
			children = this.getSourceChildrenFolders(folder);

		}

		return children;

	}

	@Override
	public Object getParent(Object element) {
		Assert.isLegal(element instanceof IResource);
		IResource resource = (IResource) element;

		return resource.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length != 0;
	}
}
