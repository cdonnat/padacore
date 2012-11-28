package org.padacore.core.gnat;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.padacore.core.AbstractAdaProjectAssociationManager;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.IAdaProjectFactory;

public class GnatAdaProjectAssociationManager extends
		AbstractAdaProjectAssociationManager {

	private static final String GNAT_PROJECT_EXTENSION = ".gpr";

	@Override
	public void associateToAdaProject(IProject eclipseProject) {
		try {
			Assert.isLegal(eclipseProject.hasNature(AdaProjectNature.NATURE_ID)
					&& eclipseProject.isOpen());
		} catch (CoreException e) {
			e.printStackTrace();
		}

		try {
			if (AbstractAdaProjectAssociationManager
					.GetAssociatedAdaProject(eclipseProject) == null) {
				this.performAssociationToGnatAdaProject(eclipseProject,
						this.GetGprAbsolutePath(eclipseProject));
			}

			Assert.isTrue(AbstractAdaProjectAssociationManager
					.GetAssociatedAdaProject(eclipseProject) != null);
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
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
	private IPath GetGprAbsolutePath(IProject project) {

		return project.getFile(project.getName() + GNAT_PROJECT_EXTENSION).getLocation();
		
	}
}
