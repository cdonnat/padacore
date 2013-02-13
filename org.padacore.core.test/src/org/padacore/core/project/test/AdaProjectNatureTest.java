package org.padacore.core.project.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.project.AdaProjectNature;
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

	private int nbAdaBuildCmdsForProject() {
		ICommand[] buildSpec = null;
		int nbAdaBuildCmds = 0;

		try {
			buildSpec = this.project.getDescription().getBuildSpec();
			for (int cmd = 0; cmd < buildSpec.length; cmd++) {
				if (buildSpec[cmd].getBuilderName().equals(
						AdaProjectNature.ADA_BUILDER_ID)) {
					nbAdaBuildCmds++;
				}
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}

		return nbAdaBuildCmds;
	}

	private void checkAdaBuildCommandIsConfiguredOnlyOnceForProject() {
		assertTrue("Ada build command shall be configured only once for project",
				this.nbAdaBuildCmdsForProject() == 1);
	}

	private void checkAdaBuildCommandIsNotConfiguredForProject() {
		assertTrue("Ada build command shall not be configured for project",
				this.nbAdaBuildCmdsForProject() == 0);
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
			this.checkAdaBuildCommandIsConfiguredOnlyOnceForProject();
			adaNature.configure();
			this.checkAdaBuildCommandIsConfiguredOnlyOnceForProject();
			
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
