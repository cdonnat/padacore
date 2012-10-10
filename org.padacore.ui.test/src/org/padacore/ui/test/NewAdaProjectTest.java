package org.padacore.ui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.padacore.core.GprProject;
import org.padacore.core.NewAdaProject;
import org.padacore.ui.test.utils.ProjectDescriptionUtils;
import org.padacore.ui.test.utils.TestUtils;

public class NewAdaProjectTest {

	// FIXME shall be in core tests but requires workspace

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
		assertEquals("Project location", new File(expectedPath), project
				.getLocation().toFile());
	}

	private static void CheckGprExists(IProject project) {
		assertTrue(
				"GPR project file shall exist",
				new File(TestUtils.GetFileAbsolutePath(project,
						project.getName() + ".gpr")).exists());
	}

	private static void CheckProject(IProject project, String expectedLocation,
			boolean mainIsExpected) {
		CheckProjectIsNotNull(project);
		CheckProjectIsOpen(project);
		ProjectDescriptionUtils.CheckProjectContainsAdaNature(project);
		CheckProjectLocation(project, expectedLocation);
		CheckGprExists(project);

		GprProject associatedGpr = TestUtils
				.CheckAGprIsAssociatedToProject(project);
		if (associatedGpr != null) {
			TestUtils.CheckDefaultGprContents(associatedGpr, mainIsExpected);
		}
	}

	@Test
	public void testAssociationToExistingGprProject() {

		GprProject existingGpr = new GprProject("gpr_project");

		IProject createdProjectFromGpr = NewAdaProject.CreateFrom(existingGpr,
				null);
		GprProject retrievedGpr = TestUtils
				.CheckAGprIsAssociatedToProject(createdProjectFromGpr);

		assertTrue("Associated GprProject shall be the one created from",
				existingGpr == retrievedGpr);
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithMain() {

		IProject createdProjectWithMain = NewAdaProject.Create(
				"TestProjectWithMain", null, true);

		CheckProject(createdProjectWithMain,
				TestUtils.GetWorkspaceAbsolutePath() + "/"
						+ createdProjectWithMain.getName(), true);
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithoutMain() {

		IProject createdProjectWithoutMain = NewAdaProject.Create(
				"TestProjectWithoutMain", null, false);

		CheckProject(createdProjectWithoutMain,
				TestUtils.GetWorkspaceAbsolutePath() + "/"
						+ createdProjectWithoutMain.getName(), false);
	}

	@Test
	public void testCreateProjectWithSpecificLocationWithoutMain() {

		IProject createdProjectWithoutMain = NewAdaProject.Create(
				"SpecificLocWithoutMain", testFolder.getRoot().toURI(), false);

		CheckProject(createdProjectWithoutMain, testFolder.getRoot()
				.getAbsolutePath(), false);
	}
}
