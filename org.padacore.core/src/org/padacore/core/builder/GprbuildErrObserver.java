package org.padacore.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.padacore.core.project.AbstractAdaProjectAssociationManager;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.ResourceLocator;
import org.padacore.core.utils.ErrorLog;

/**
 * Display error messages from gprbuild.
 * 
 */
public class GprbuildErrObserver implements Observer {

	private GprbuildOutput parser;
	private IProject project;

	/**
	 * Default constructor.
	 */
	public GprbuildErrObserver(IProject project, GprbuildOutput parser) {
		this.parser = parser;
		this.project = project;

		try {
			this.project.deleteMarkers(IMarker.PROBLEM, true,
					IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String line = (String) arg1;

		parser.evaluate(line);

		if (parser.lastEntryIndicatesError()) {
			addError(parser.error());
		}
	}

	/**
	 * Adds the given error as a resource marker.
	 * 
	 * @param error
	 *            the error to convert to a resource marker.
	 */
	private void addError(Error error) {

		try {
			IResource file = findResource(error.file());

			IMarker m = file.createMarker(IMarker.PROBLEM);
			m.setAttribute(IMarker.LINE_NUMBER, error.line());
			m.setAttribute(IMarker.MESSAGE, error.message());
			m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			m.setAttribute(IMarker.SEVERITY, error.severity());

		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Collects all the referenced projects of the given project (in a recursive
	 * way) and add them to referencedProjects list.
	 * 
	 * @param referencedProjects
	 *            the list of recursively referenced projects.
	 * @param project
	 *            the project for which we want to collect referenced projects.
	 */
	private void collectAllReferencedProjects(
			List<IProject> referencedProjects, IProject project) {
		try {

			if (!referencedProjects.contains(project)
					&& project != this.project) {
				referencedProjects.add(project);
			}

			for (IProject ownReferencedProject : project
					.getReferencedProjects()) {
				this.collectAllReferencedProjects(referencedProjects,
						ownReferencedProject);
			}

		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

	}

	/**
	 * Finds the resource corresponding to the given filename in all referenced
	 * projects of current project (traversed in a recursive way).
	 * 
	 * @param fileName
	 *            the filename for which we want to retrieve a resource.
	 * @return the resource handle corresponding to the given filename (cannot
	 *         be null).
	 **/
	private IResource findResource(String fileName) {
		IResource file = this.findResourceInProject(fileName, this.project);
		List<IProject> recursiveReferencedProjects;
		recursiveReferencedProjects = new ArrayList<IProject>();
		this.collectAllReferencedProjects(recursiveReferencedProjects,
				this.project);

		if (file == null) {
			int project = 0;

			while (file == null && project < recursiveReferencedProjects.size()) {
				file = this.findResourceInProject(fileName,
						recursiveReferencedProjects.get(project));
				project++;
			}
		}

		Assert.isNotNull(file);

		return file;
	}

	/**
	 * Returns the absolute file path for the given file contained in the given
	 * folder. If fileName already denotes an absolute path, it is returned
	 * unchanged.
	 * 
	 * @param fileName
	 *            name of the file
	 * @param containingFolder
	 *            absolute path of the folder which contains the file
	 * @return the absolute file path for the given file contained in the given
	 *         folder.
	 */
	private IPath getAbsolutePathFromFileName(String fileName,
			IPath containingFolder) {

		IPath currentPath = new Path(fileName);
		IPath absolutePath;

		if (!currentPath.isAbsolute()) {
			StringBuilder absolutePathBuilder = new StringBuilder(
					containingFolder.toString());
			absolutePathBuilder.append(System.getProperty("file.separator"));
			absolutePathBuilder.append(fileName);

			absolutePath = new Path(absolutePathBuilder.toString());

		} else {
			absolutePath = currentPath;
		}

		return absolutePath;
	}

	/**
	 * Searches for the resource corresponding to the given filename in given
	 * project.
	 * 
	 * @param fileName
	 *            the filename for which we want to retrieve a resource.
	 * @return the resource handle corresponding to the given filename or null
	 *         if it was not found in given project.
	 */
	private IResource findResourceInProject(String fileName, IProject project) {
		IResource file = null;
		ResourceLocator resourceLocator = new ResourceLocator(project);
		IAdaProject adaProject = AbstractAdaProjectAssociationManager
				.GetAssociatedAdaProject(project);

		int srcDir = 0;
		IPath absoluteFilePath = this.getAbsolutePathFromFileName(fileName,
				adaProject.getRootPath());

		file = resourceLocator.findResourceFromPath(absoluteFilePath);

		while (file == null
				&& srcDir < adaProject.getSourceDirectoriesPaths().size()) {
			absoluteFilePath = this.getAbsolutePathFromFileName(fileName,
					adaProject.getSourceDirectoriesPaths().get(srcDir));

			file = resourceLocator.findResourceFromPath(absoluteFilePath);
			srcDir++;
		}

		return file;
	}
}
