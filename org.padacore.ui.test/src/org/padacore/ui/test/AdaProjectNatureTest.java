package org.padacore.ui.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.NewAdaProject;

public class AdaProjectNatureTest {

	private IProject adaProject;

	@Before
	public void createAdaProject() {
		NewAdaProject adaProject = new NewAdaProject("TestProject", null);
		this.adaProject = adaProject.create(false);
	}

	@Test
	public void testConfigureProject() {

		try {
			ICommand[] projectBuilderCmds = this.adaProject.getDescription()
					.getBuildSpec();
			assertTrue(
					"Ada builder command shall be first in builder commands list",
					getAdaBuilderPositionInBuilderCmdsList(projectBuilderCmds) == 0);

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeconfigureProject() {
		AdaProjectNature adaNature = new AdaProjectNature();
		adaNature.setProject(this.adaProject);
		try {
			adaNature.deconfigure();
			ICommand[] projectBuilderCmds = this.adaProject.getDescription()
					.getBuildSpec();
			assertTrue(
					"Ada builder command shall not be found in builder commands list",
					getAdaBuilderPositionInBuilderCmdsList(projectBuilderCmds) == projectBuilderCmds.length);

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	private int getAdaBuilderPositionInBuilderCmdsList(ICommand[] builderCmds) {
		int adaBuilderIndex = builderCmds.length;
		int currentCmd = 0;

		while (adaBuilderIndex == builderCmds.length
				&& currentCmd < builderCmds.length) {
			if (builderCmds[currentCmd].getBuilderName().equals(
					AdaProjectNature.ADA_BUILDER_ID)) {
				adaBuilderIndex = currentCmd;
			}
			currentCmd++;
		}

		return adaBuilderIndex;
	}

}
