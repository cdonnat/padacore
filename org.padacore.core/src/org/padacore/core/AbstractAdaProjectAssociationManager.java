package org.padacore.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.padacore.core.utils.ErrorLogger;

public abstract class AbstractAdaProjectAssociationManager {

	public static final String ADA_PROJECT_SESSION_PROPERTY_QUALIFIER = "org.padacore";

	/**
	 * Associates a new Ada project to an Eclipse project.
	 * 
	 * @param eclipseProject
	 *            the Eclipse project to which the Ada project will be
	 *            associated.
	 * @pre eclipseProject has an Ada nature and is opened.
	 * @post an Ada project is associated to the Eclipse project.
	 */
	public abstract void associateToAdaProject(IProject eclipseProject);

	/**
	 * Associates the given Ada project to the given Eclipse project.
	 * 
	 * @param adaProject
	 *            the Ada project.
	 * @param eclipseProject
	 *            the Eclipse project.
	 * 
	 * @pre eclipseProject has an Ada nature and is opened.
	 * @post given Ada project is associated to given Eclipse project.
	 */
	protected void associateAdaProjectToEclipseProject(IAdaProject adaProject,
			IProject eclipseProject) {
		try {
			Assert.isLegal(eclipseProject.isOpen()
					&& eclipseProject.hasNature(AdaProjectNature.NATURE_ID));
		} catch (CoreException e) {
			ErrorLogger.appendExceptionToErrorLog(e);
		}

		QualifiedName qualifiedName = GetQualifiedNameForAdaProjectSessionProperty(eclipseProject);

		try {
			eclipseProject.setSessionProperty(qualifiedName, adaProject);
		} catch (CoreException e) {
			ErrorLogger.appendExceptionToErrorLog(e);
		}

	}

	/**
	 * Returns the Ada project associated to given Eclipse project.
	 * 
	 * @param eclipseProject
	 *            the Eclipse project for which we want the associated Ada
	 *            project.
	 * @return the Ada project associated to given Eclipse project.
	 * 
	 * @pre eclipseProject has an Ada nature and is opened.
	 */
	public static IAdaProject GetAssociatedAdaProject(IProject eclipseProject) {
		try {
			Assert.isLegal(eclipseProject.isOpen()
					&& eclipseProject.hasNature(AdaProjectNature.NATURE_ID));
		} catch (CoreException e) {
			ErrorLogger.appendExceptionToErrorLog(e);
		}

		QualifiedName qualifiedName = GetQualifiedNameForAdaProjectSessionProperty(eclipseProject);

		IAdaProject associatedAdaProject = null;

		try {
			associatedAdaProject = (IAdaProject) (eclipseProject
					.getSessionProperty(qualifiedName));
		} catch (CoreException e) {
			ErrorLogger.appendExceptionToErrorLog(e);
		}

		return associatedAdaProject;
	}

	/**
	 * Associate a new Ada project to all Eclipse projects with Ada nature found
	 * in workspace.
	 * 
	 * @param workspace
	 *            the workspace in which projects with Ada nature are looked
	 *            for.
	 **/
	public void performAssociationToAdaProjectForAllProjectsWithAdaNatureOf(
			IWorkspace workspace) {

		IProject[] workspaceProjects = workspace.getRoot().getProjects();
		IProject currentProject;

		for (int project = 0; project < workspaceProjects.length; project++) {
			currentProject = workspaceProjects[project];

			try {
				if (currentProject.isOpen()
						&& currentProject.hasNature(AdaProjectNature.NATURE_ID)) {

					this.associateToAdaProject(currentProject);
				}
			} catch (CoreException e) {
				ErrorLogger.appendExceptionToErrorLog(e);
			}
		}
	}

	/**
	 * Returns the qualified name for the session property used to store Ada
	 * project.
	 * 
	 * @param project
	 *            the project for which we want the session property qualified
	 *            name.
	 * @return the qualified name for the session property used to store Ada
	 *         project.
	 */
	private static QualifiedName GetQualifiedNameForAdaProjectSessionProperty(
			IProject project) {
		return new QualifiedName(ADA_PROJECT_SESSION_PROPERTY_QUALIFIER,
				project.getName());
	}

}
