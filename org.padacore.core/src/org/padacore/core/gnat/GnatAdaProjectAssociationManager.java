package org.padacore.core.gnat;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.padacore.core.project.AbstractAdaProjectAssociationManager;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProjectFactory;
import org.padacore.core.utils.ErrorLog;

public class GnatAdaProjectAssociationManager extends
		AbstractAdaProjectAssociationManager {

	private static final String GNAT_PROJECT_EXTENSION = ".gpr";

	@Override
	public void associateToAdaProject(IProject eclipseProject) {
		try {
			Assert.isLegal(eclipseProject.hasNature(AdaProjectNature.NATURE_ID)
					&& eclipseProject.isOpen());
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		IPath gprAbsolutePath = this.getGprAbsolutePath(eclipseProject);

		try {
			if (AbstractAdaProjectAssociationManager
					.GetAssociatedAdaProject(eclipseProject) == null) {
				this.performAssociationToGnatAdaProject(eclipseProject,
						gprAbsolutePath);
			}

			Assert.isTrue(AbstractAdaProjectAssociationManager
					.GetAssociatedAdaProject(eclipseProject) != null);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		} catch (IOException e) {
			ErrorLog.appendMessage("Error while opening GPR file:"
					+ gprAbsolutePath.toOSString(), IStatus.ERROR);
		} catch (RecognitionException e) {
			ErrorLog.appendMessage("GPR file " + gprAbsolutePath.toOSString()
					+ "format is incorrect", IStatus.ERROR);
		}
	}

	/**
	 * Associate a new GPR project (created from given GPR file) to an Eclipse
	 * project.
	 * 
	 * @param project
	 *            the Eclipse project to which the GPR project will be
	 *            associated.
	 * @param gprFileAbsolutePath
	 *            the absolute path of GPR file (filename included)
	 * @throws IOException
	 *             if the GPR file could not be opened.
	 * @throws RecognitionException
	 *             if the GPR file format is invalid.
	 * @throws CoreException
	 *             if GPR project could not be associated to Eclipse project.
	 */
	private void performAssociationToGnatAdaProject(IProject project,
			IPath gprFileAbsolutePath) throws IOException,
			RecognitionException, CoreException {

		IAdaProjectFactory adaProjectFactory = new GnatAdaProjectFactory(
				gprFileAbsolutePath);

		this.associateAdaProjectToEclipseProject(
				adaProjectFactory.createAdaProject(), project);
	}

	/**
	 * Return the file system absolute path for the GPR file associated to given
	 * project.
	 * 
	 * @param project
	 *            the project for which the GPR file path is requested
	 * @return the file system absolute path for the GPR file associated to
	 *         given project.
	 */
	private IPath getGprAbsolutePath(IProject project) {
		return project.getFile(project.getName() + GNAT_PROJECT_EXTENSION)
				.getLocation();
	}
}
