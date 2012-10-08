package org.padacore.ui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.junit.Test;
import org.padacore.core.GprProject;
import org.padacore.core.NewAdaProject;
import org.padacore.ui.test.utils.ProjectDescriptionUtils;
import org.padacore.ui.test.utils.TestUtils;

public class NewAdaProjectTest {

	// FIXME shall be in core tests but requires workspace

	private NewAdaProject sut;

	private static void checkProjectIsNotNull(IProject project) {
		assertNotNull("Project shall be not null", project);
	}

	private static void checkProjectIsOpen(IProject project) {
		assertTrue("Project shall be open", project.isOpen());
	}

	private static void checkProjectLocation(IProject project,
			String expectedPath) {
		assertEquals("Project location", expectedPath, project.getLocationURI()
				.getPath());
	}

	private static void checkGprExists(IProject project) {
		assertTrue(
				"GPR project file shall exist",
				new File(TestUtils.getFileAbsolutePath(project,
						project.getName() + ".gpr")).exists());
	}

	@Test
	public void testAssociationToExistingGprProject() {
		sut = new NewAdaProject("TestProjectFromGpr", null);
		GprProject existingGpr = new GprProject("gpr_project");

		IProject createdProjectFromGpr = sut.createFrom(existingGpr);
		GprProject retrievedGpr = TestUtils
				.checkAGprIsAssociatedToProject(createdProjectFromGpr);

		assertTrue("Associated GprProject shall be the one created from",
				existingGpr == retrievedGpr);
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithMain() {

		sut = new NewAdaProject("TestProjectWithMain", null);

		IProject createdProjectWithMain = sut.create(true);

		checkProjectIsNotNull(createdProjectWithMain);
		checkProjectIsOpen(createdProjectWithMain);
		ProjectDescriptionUtils.CheckProjectContainsAdaNature(
				createdProjectWithMain,
				"Create new project with default location with main procedure");
		checkProjectLocation(createdProjectWithMain,
				TestUtils.getWorkspaceAbsolutePath() + "/"
						+ createdProjectWithMain.getName());
		checkGprExists(createdProjectWithMain);

		GprProject associatedGpr = TestUtils
				.checkAGprIsAssociatedToProject(createdProjectWithMain);
		if (associatedGpr != null) {
			TestUtils.checkDefaultGprContents(associatedGpr, true);
		}
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithoutMain() {

		sut = new NewAdaProject("TestProjectWithoutMain", null);

		IProject createdProjectWithoutMain = sut.create(false);

		checkProjectIsNotNull(createdProjectWithoutMain);
		checkProjectIsOpen(createdProjectWithoutMain);
		ProjectDescriptionUtils
				.CheckProjectContainsAdaNature(createdProjectWithoutMain,
						"Create new project with default location without main procedure");
		checkProjectLocation(createdProjectWithoutMain,
				TestUtils.getWorkspaceAbsolutePath() + "/"
						+ createdProjectWithoutMain.getName());
		checkGprExists(createdProjectWithoutMain);

		GprProject associatedGpr = TestUtils
				.checkAGprIsAssociatedToProject(createdProjectWithoutMain);
		if (associatedGpr != null) {
			TestUtils.checkDefaultGprContents(associatedGpr, false);
		}
	}
}
