package org.padacore.core.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.test.utils.CommonTestUtils;

public class AdaProjectNatureTest {

	private IProject project;
	private IProjectNature adaNature;

	@Before
	public void createFixture() {
		this.project = CommonTestUtils.CreateAdaProject();
		try {
			this.adaNature = this.project.getNature(AdaProjectNature.NATURE_ID);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			this.project.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private boolean doesAdaBuildCmdExistForProject() {
		ICommand[] buildSpec = null;

		try {
			buildSpec = this.project.getDescription().getBuildSpec();
			for (int cmd = 0; cmd < buildSpec.length; cmd++) {
				if (buildSpec[cmd].getBuilderName().equals(
						AdaProjectNature.ADA_BUILDER_ID)) {
					return true;
				}
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}

		return false;
	}

	private void checkAdaBuildCommandIsConfiguredForProject() {
		assertTrue("Ada build command shall be configured for project",
				this.doesAdaBuildCmdExistForProject());
	}

	private void checkAdaBuildCommandIsNotConfiguredForProject() {
		assertFalse("Ada build command shall not be configured for project",
				this.doesAdaBuildCmdExistForProject());
	}

	private void addDummyBuildCommandToProject() {
		ICommand[] buildSpec;
		IProjectDescription newProjectDescription;
		try {
			buildSpec = this.project.getDescription().getBuildSpec();
			ICommand[] newBuildSpec = new ICommand[buildSpec.length + 1];
			System.arraycopy(buildSpec, 0, newBuildSpec, 1, buildSpec.length);
			newBuildSpec[0] = this.project.getDescription().newCommand();
			newBuildSpec[0].setBuilderName("toto");

			newProjectDescription = this.project.getDescription();
			newProjectDescription.setBuildSpec(newBuildSpec);

			this.project.setDescription(newProjectDescription, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConfigureAndDeconfigure() {
		try {
			adaNature.configure();
			this.checkAdaBuildCommandIsConfiguredForProject();

			this.addDummyBuildCommandToProject();

			adaNature.deconfigure();
			this.checkAdaBuildCommandIsNotConfiguredForProject();
			adaNature.deconfigure();
			this.checkAdaBuildCommandIsNotConfiguredForProject();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
