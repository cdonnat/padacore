package org.padacore.core.builder;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
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
	 * Finds the resource corresponding to the given filename in all referenced
	 * projects of current project.
	 * 
	 * @param fileName
	 *            the filename for which we want to retrieve a resource.
	 * @return the resource handle corresponding to the given filename (cannot
	 *         be null).
	 **/
	private IResource findResource(String fileName) {
		IResource file = this.findResourceInProject(fileName, this.project);

		if (file == null) {
			try {
				IProject[] referencedProjects = this.project
						.getReferencedProjects();
				int project = 0;

				while (file == null && project < referencedProjects.length) {
					file = this.findResourceInProject(fileName,
							referencedProjects[project]);
					project++;
				}

			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		Assert.isNotNull(file);

		return file;
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
		String absoluteFilePath;

		while (file == null
				&& srcDir < adaProject.getSourceDirectoriesPaths().size()) {
			absoluteFilePath = adaProject.getSourceDirectoriesPaths().get(
					srcDir)
					+ System.getProperty("file.separator") + fileName;

			file = resourceLocator.findResourceFromPath(new Path(
					absoluteFilePath));
			srcDir++;
		}

		return file;
	}

}
