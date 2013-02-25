package org.padacore.core.project.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.ProjectBuilder;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.core.test.utils.ProjectDescriptionUtils;

public class ProjectBuilderTest {

	private ProjectBuilder sut;

	public void createFixture(String projectName) {
		this.sut = new ProjectBuilder(projectName);
	}

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testNewProjectWithDefaultLocationWithoutMain() {
		final String projectName = "NewProjectWithDefaultLocationWithoutMain";
		String expectedLocation = CommonTestUtils.GetWorkspaceAbsolutePath() + "/" + projectName;
		this.testNewProject(projectName, false, null, expectedLocation);
	}

	@Test
	public void testNewProjectWithDefaultLocationWithMain() {
		final String projectName = "NewProjectWithDefaultLocationWithMain";
		String expectedLocation = CommonTestUtils.GetWorkspaceAbsolutePath() + "/" + projectName;
		this.testNewProject(projectName, true, null, expectedLocation);
	}

	@Test
	public void testNewProjectWithSpecificLocationWithoutMain() {
		final String projectName = "NewProjectWithSpecificLocationWithoutMain";
		String expectedLocation = testFolder.getRoot().getAbsolutePath();
		this.testNewProject(projectName, false, new Path(testFolder.getRoot().getPath()),
				expectedLocation);
	}

	@Test
	public void testNewProjectWithSpecificLocationWithMain() {
		final String projectName = "NewProjectWithSpecificLocationWithMain";
		String expectedLocation = testFolder.getRoot().getAbsolutePath();
		this.testNewProject(projectName, true, new Path(testFolder.getRoot().getPath()),
				expectedLocation);
	}

	private void testNewProject(String projectName, boolean addMain, IPath location,
			String expectedLocation) {

		this.createFixture(projectName);

		IAdaProject adaProject = mock(IAdaProject.class);
		this.sut.createNewProject(adaProject, location, addMain);

		this.checkProject(projectName, adaProject, addMain, expectedLocation,
				CommonTestUtils.CREATED_PROJECT);
	}

	@Test
	public void testImportProject() {
		IAdaProject adaProject = mock(IAdaProject.class);
		String projectName = "sample_project";
		this.createFixture(projectName);
		IPath importedProjectFilePath = new Path(CommonTestUtils.GetPathToTestProject()
				+ projectName + ".gpr");

		this.sut.importProject(importedProjectFilePath, adaProject);

		String expectedLocation = CommonTestUtils.GetWorkspaceAbsolutePath() + "/" + projectName;
		this.checkProject(projectName, adaProject, false, expectedLocation,
				CommonTestUtils.IMPORTED_PROJECT);
		this.checkLinks(projectName, importedProjectFilePath);
	}

	private void checkLinks(String projectName, IPath importedProjectFilePath) {
		IProject importedProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		IFolder toto = importedProject.getFolder("toto");
		assertTrue("Toto folder should be created", toto.exists());
		assertEquals("Toto folder should be link to folder containing ada project file",
				importedProjectFilePath.removeLastSegments(1), toto.getLocation());

		IFile projectFile = importedProject.getFile(importedProjectFilePath.lastSegment());
		assertTrue("Link to ada project file should be created (1)", projectFile.exists());
		assertEquals("Link to ada project file should be created (2)", importedProjectFilePath,
				projectFile.getLocation());
	}

	private void checkProject(final String projectName, IAdaProject adaProject,
			boolean mainIsExpected, String expectedLocation, String expectedKind) {
		IProject createdProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		this.checkProjectIsNotNull(createdProject);
		this.checkProjectIsOpen(createdProject);
		this.checkProjectLocation(createdProject, expectedLocation);
		ProjectDescriptionUtils.CheckProjectContainsAdaNature(createdProject);

		CommonTestUtils.CheckAdaProjectIsSetInProperties(createdProject, adaProject);
		CommonTestUtils.CheckProjectKindIsSetInProperties(createdProject, expectedKind);

		if (mainIsExpected) {
			ProjectDescriptionUtils.CheckMainProcedureHasBeenAddedWithCorrectContents(
					createdProject, expectedLocation);
		}
	}

	private void checkProjectIsNotNull(IProject project) {
		assertNotNull("Project shall be not null", project);
	}

	private void checkProjectIsOpen(IProject project) {
		assertTrue("Project shall be open", project.isOpen());
	}

	private void checkProjectLocation(IProject project, String expectedPath) {
		try {
			assertEquals("Project location", new File(expectedPath).getCanonicalFile(), project
					.getLocation().toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
