package org.padacore.core.launch.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.launch.AdaLaunchConfigurationUtils;
import org.padacore.core.launch.AdaLaunchConstants;
import org.padacore.core.test.utils.CommonTestUtils;

public class AdaLaunchConfigurationsUtilsTest {

	private IProject adaProject;
	private ILaunchConfiguration adaLaunchConfig;

	private void createLaunchConfiguration() {
		try {

			this.adaLaunchConfig = CommonTestUtils.CreateAdaLaunchConfigurationFor("conf1",
					this.adaProject, "existing_file");

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void createFixture() {

		this.adaProject = CommonTestUtils.CreateAdaProject();

		this.createLaunchConfiguration();
	}
	
	@After
	public void tearDown() {
		try {
			this.adaProject.delete(true, null);
			this.adaLaunchConfig.delete();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExistingLaunchConfigRetrieval() {
		IFile fileWithExistingConfig = this.adaProject.getFile("existing_file");

		try {

			this.checkRetrievedConfigCorrespondsToFile(fileWithExistingConfig,
					"Existing Ada launch configuration retrieval");

			assertTrue(
					"No new Ada launch configuration has been created",
					CommonTestUtils.RetrieveAdaLaunchConfigurations().length == 1);

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNewLaunchConfigCreation() {
		IFile fileWithNoConfig = this.adaProject.getFile("new_file");

		try {

			this.checkRetrievedConfigCorrespondsToFile(fileWithNoConfig,
					"New Ada launch configuration creation");

			assertTrue(
					"A new Ada launch configuration has been created",
					CommonTestUtils.RetrieveAdaLaunchConfigurations().length == 2);

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void checkRetrievedConfigCorrespondsToFile(IFile file,
			String comment) throws CoreException {
		ILaunchConfiguration retrievedLaunchConfig = AdaLaunchConfigurationUtils
				.GetLaunchConfigurationFor(file.getRawLocation());

		String retrievedExecPath = retrievedLaunchConfig.getAttribute(
				AdaLaunchConstants.EXECUTABLE_PATH, "");

		String expectedExecPath = CommonTestUtils.GetFileAbsolutePath(
				this.adaProject, file.getName());

		assertTrue(comment, retrievedExecPath.equals(expectedExecPath));

	}

}
