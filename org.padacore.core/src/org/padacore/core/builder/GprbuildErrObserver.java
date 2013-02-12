package org.padacore.core.builder;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.padacore.core.AbstractAdaProjectAssociationManager;
import org.padacore.core.IAdaProject;
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
	public GprbuildErrObserver(IProject project) {
		this.parser = new GprbuildOutput();
		this.project = project;

		try {
			this.project.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
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

	private void addError(Error error) {

		try {
			IResource file = findResource(error.file());
			if (file != null) {
				IMarker m = file.createMarker(IMarker.PROBLEM);
				m.setAttribute(IMarker.LINE_NUMBER, error.line());
				m.setAttribute(IMarker.MESSAGE, error.message());
				m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
				m.setAttribute(IMarker.SEVERITY, error.severity());
			}
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	private IResource findResource(String fileName) throws CoreException {
		IResource file = project.findMember(fileName);
		IAdaProject adaProject = AbstractAdaProjectAssociationManager
				.GetAssociatedAdaProject(project);

		int i = 0;

		while (file == null && i < adaProject.getSourcesDir().size()) {
			String path = adaProject.getSourcesDir().get(i)
					+ System.getProperty("file.separator") + fileName;
			file = project.findMember(path);
			i++;
		}
		return file;
	}

}
