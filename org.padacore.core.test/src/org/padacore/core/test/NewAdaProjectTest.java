package org.padacore.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.padacore.core.GprProject;
import org.padacore.core.NewAdaProject;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.core.test.utils.ProjectDescriptionUtils;

public class NewAdaProjectTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private static void CheckProjectIsNotNull(IProject project) {
		assertNotNull("Project shall be not null", project);
	}

	private static void CheckProjectIsOpen(IProject project) {
		assertTrue("Project shall be open", project.isOpen());
	}

	private static void CheckProjectLocation(IProject project,
			String expectedPath) {
		try {
			assertEquals("Project location",
					new File(expectedPath).getCanonicalFile(), project
							.getLocation().toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void CheckGprExists(IProject project) {
		assertTrue(
				"GPR project file shall exist",
				new File(CommonTestUtils.GetFileAbsolutePath(project,
						project.getName() + ".gpr")).exists());
	}

	private static void CheckProject(IProject project, String expectedLocation,
			boolean mainIsExpected) {
		CheckProjectIsNotNull(project);
		CheckProjectIsOpen(project);
		ProjectDescriptionUtils.CheckProjectContainsAdaNature(project);
		CheckProjectLocation(project, expectedLocation);
		CheckGprExists(project);

		GprProject associatedGpr = CommonTestUtils
				.CheckGprAssociationToProject(project, true);
		if (associatedGpr != null) {
			CommonTestUtils.CheckDefaultGprContents(associatedGpr,
					mainIsExpected);
		}
	}

	@Test
	public void testAssociationToExistingGprProject() {

		String gprProjectName = "test_gpr_project";

		CommonTestUtils.CreateGprFileIn(new Path(this.testFolder.getRoot()
				.getPath()), gprProjectName);

		IProject createdProjectFromGpr = NewAdaProject.CreateFrom(new Path(
				testFolder.getRoot().getPath()
						+ System.getProperty("file.separator") + gprProjectName
						+ ".gpr"));
		GprProject retrievedGpr = CommonTestUtils.CheckGprAssociationToProject(
				createdProjectFromGpr, true);

		assertTrue("Associated GprProject shall be the one created from",
				retrievedGpr.getName().equals(gprProjectName));
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithMain() {

		IProject createdProjectWithMain = NewAdaProject.Create(
				"TestProjectWithMain", null, true);

		CheckProject(createdProjectWithMain,
				CommonTestUtils.GetWorkspaceAbsolutePath() + "/"
						+ createdProjectWithMain.getName(), true);
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithoutMain() {

		IProject createdProjectWithoutMain = NewAdaProject.Create(
				"TestProjectWithoutMain", null, false);

		CheckProject(createdProjectWithoutMain,
				CommonTestUtils.GetWorkspaceAbsolutePath() + "/"
						+ createdProjectWithoutMain.getName(), false);
	}

	@Test
	public void testCreateProjectWithSpecificLocationWithoutMain() {

		IProject createdProjectWithoutMain = NewAdaProject.Create(
				"SpecificLocWithoutMain", new Path(testFolder.getRoot()
						.getPath()), false);

		CheckProject(createdProjectWithoutMain, testFolder.getRoot()
				.getAbsolutePath(), false);
	}
}
