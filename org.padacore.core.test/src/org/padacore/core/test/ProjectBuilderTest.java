package org.padacore.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.padacore.core.ProjectBuilder;
import org.padacore.core.IAdaProject;
import org.padacore.core.gnat.GprProject;
import org.padacore.core.test.stubs.AdaProjectAssociationManagerStub;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.core.test.utils.ProjectDescriptionUtils;

public class ProjectBuilderTest {

	private ProjectBuilder sut;
	private AdaProjectAssociationManagerStub associationManager;

	@Before
	public void createFixture() {
		this.associationManager = new AdaProjectAssociationManagerStub();
		this.sut = new ProjectBuilder(this.associationManager);
	}

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

		IAdaProject associatedAdaProject = CommonTestUtils
				.CheckAdaProjectAssociationToProject(project, true);
		if (associatedAdaProject != null) {
			CommonTestUtils.CheckDefaultAdaProjectContents(
					associatedAdaProject, mainIsExpected);
		}
	}

//	@Test
//	public void testAssociationToExistingGprProject() {
//
//		String gprProjectName = "test_gpr_project";
//
//		CommonTestUtils.CreateGprFileIn(new Path(this.testFolder.getRoot()
//				.getPath()), gprProjectName);
//
//		IProject createdProjectFromGpr = this.sut
//				.createProjectWithAdaNatureAt(gprProjectName, new Path(
//						testFolder.getRoot().getPath()), false);
//
//		IAdaProject retrievedAdaProject = CommonTestUtils
//				.CheckAdaProjectAssociationToProject(createdProjectFromGpr,
//						true);
//
//		assertNotNull("Associated AdaProject shall exist", retrievedAdaProject);
//	}

	// @Test
	// public void testCreateProjectWithDefaultLocationWithMain() {
	//
	// IProject createdProjectWithMain = EclipseAdaProjectBuilder.Create(
	// "TestProjectWithMain", null, true);
	//
	// CheckProject(createdProjectWithMain,
	// CommonTestUtils.GetWorkspaceAbsolutePath() + "/"
	// + createdProjectWithMain.getName(), true);
	// }
	//
	// @Test
	// public void testCreateProjectWithDefaultLocationWithoutMain() {
	//
	// IProject createdProjectWithoutMain = EclipseAdaProjectBuilder.Create(
	// "TestProjectWithoutMain", null, false);
	//
	// CheckProject(createdProjectWithoutMain,
	// CommonTestUtils.GetWorkspaceAbsolutePath() + "/"
	// + createdProjectWithoutMain.getName(), false);
	// }
	//
	// @Test
	// public void testCreateProjectWithSpecificLocationWithoutMain() {
	//
	// IProject createdProjectWithoutMain = EclipseAdaProjectBuilder.Create(
	// "SpecificLocWithoutMain", new Path(testFolder.getRoot()
	// .getPath()), false);
	//
	// CheckProject(createdProjectWithoutMain, testFolder.getRoot()
	// .getAbsolutePath(), false);
	// }
}
