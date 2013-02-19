package org.padacore.core.project.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.ProjectBuilder;
import org.padacore.core.test.stubs.AdaProjectAssociationManagerStub;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.core.test.utils.ProjectDescriptionUtils;

public class ProjectBuilderTest {

	private ProjectBuilder sut;
	private AdaProjectAssociationManagerStub associationManager;
	private IProject projectWithDefautLocationWithoutMain;
	private IProject projectWithDefaultLocationWithMain;
	private IProject projectWithSpecificLocationWithoutMain;
	private IProject projectWithSpecificLocationWithMain;

	@Before
	public void createFixture() {
		this.associationManager = new AdaProjectAssociationManagerStub();
		this.sut = new ProjectBuilder(this.associationManager);
	}

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

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

	private void checkProject(IProject project, String expectedLocation, boolean mainIsExpected) {
		this.checkProjectIsNotNull(project);
		this.checkProjectIsOpen(project);
		ProjectDescriptionUtils.CheckProjectContainsAdaNature(project);
		this.checkProjectLocation(project, expectedLocation);
		this.checkAssociationToAdaProjectIsPerformed(project);
		if (mainIsExpected) {
			ProjectDescriptionUtils.CheckMainProcedureHasBeenAddedWithCorrectContents(project,
					expectedLocation);
		}
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithMain() {

		this.projectWithDefaultLocationWithMain = this.createProjectUsingProjectBuilder(
				"TestProjectWithMain", null, true);

		this.checkProject(this.projectWithDefaultLocationWithMain,
				CommonTestUtils.GetWorkspaceAbsolutePath() + "/"
						+ this.projectWithDefaultLocationWithMain.getName(), true);
	}

	private void checkAssociationToAdaProjectIsPerformed(IProject project) {
		assertTrue("Project " + project.getName() + " shall be associated to Ada project",
				this.associationManager.isEclipseProjectAssociatedToAdaProject(project));

	}

	private IProject createProjectUsingProjectBuilder(String projectName, IPath location,
			boolean addMain) {

		File projectFolder = new File(ProjectBuilder.GetProjectPath(projectName, location)
				.toOSString());
		projectFolder.mkdirs();

		return this.sut.createProjectWithAdaNatureAt(projectName, location, addMain);
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithoutMain() {

		this.projectWithDefautLocationWithoutMain = this.createProjectUsingProjectBuilder(
				"TestProjectWithoutMain", null, false);

		this.checkProject(this.projectWithDefautLocationWithoutMain,
				CommonTestUtils.GetWorkspaceAbsolutePath() + "/"
						+ this.projectWithDefautLocationWithoutMain.getName(), false);
	}

	@Test
	public void testCreateProjectWithSpecificLocationWithoutMain() {

		this.projectWithSpecificLocationWithoutMain = this.createProjectUsingProjectBuilder(
				"SpecificLocWithoutMain", new Path(testFolder.getRoot().getPath()), false);

		this.checkProject(this.projectWithSpecificLocationWithoutMain, testFolder.getRoot()
				.getAbsolutePath(), false);
	}

	@Test
	public void testCreateProjectWithSpecificLocationWithMain() {

		this.projectWithSpecificLocationWithMain = this.createProjectUsingProjectBuilder(
				"SpecificLocWithMain", new Path(testFolder.getRoot().getPath()), true);

		this.checkProject(this.projectWithSpecificLocationWithMain, testFolder.getRoot()
				.getAbsolutePath(), true);
	}

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
		this.testNewProject(projectName, false, new Path(testFolder.getRoot().getPath()), expectedLocation);
	}
	
	@Test
	public void testNewProjectWithSpecificLocationWithMain() {
		final String projectName = "NewProjectWithSpecificLocationWithMain";
		String expectedLocation = testFolder.getRoot().getAbsolutePath();
		this.testNewProject(projectName, true, new Path(testFolder.getRoot().getPath()), expectedLocation);
	}

	private void testNewProject(String projectName, boolean addMain, IPath location,
			String expectedLocation) {
		IAdaProject adaProject = mock(IAdaProject.class);

		this.sut.createNewProject(projectName, adaProject, location, addMain);

		this.checkProject(projectName, adaProject, addMain, expectedLocation,
				CommonTestUtils.CREATED_PROJECT);
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

}
