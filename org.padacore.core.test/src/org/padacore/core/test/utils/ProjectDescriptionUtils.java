package org.padacore.core.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.padacore.core.AdaProjectNature;

public class ProjectDescriptionUtils {

	public static void CheckProjectContainsAdaNature(IProject project) {
		try {
			IProjectDescription desc = project.getDescription();
			assertEquals("Project shall contain one nature", 1, desc.getNatureIds().length);
			assertTrue("Project natures shall contain adaProjectNature",
					desc.hasNature(AdaProjectNature.NATURE_ID));
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static void CheckAdaBuilderIsTheFirstBuilder(IProject project, String comment) {
		assertTrue("Ada builder command shall be first in builder commands list - " + comment,
				getAdaBuilderPositionInBuilderCmdsList(project) == 0);
	}

	public static void CheckThereIsNoAdaBuilder(IProject project, String comment) {
		assertTrue("Ada builder command shall not be found in builder commands list",
				getAdaBuilderPositionInBuilderCmdsList(project) == -1);
	}

	private static int getAdaBuilderPositionInBuilderCmdsList(IProject project) {
		int adaBuilderIndex = -1;
		try {
			ICommand[] builderCmds = project.getDescription().getBuildSpec();
			int currentCmd = 0;

			while (currentCmd < builderCmds.length) {
				if (builderCmds[currentCmd].getBuilderName()
						.equals(AdaProjectNature.ADA_BUILDER_ID)) {
					adaBuilderIndex = currentCmd;
				}
				currentCmd++;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return adaBuilderIndex;
	}

}
