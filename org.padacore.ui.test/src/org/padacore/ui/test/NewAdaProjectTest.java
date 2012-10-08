package org.padacore.ui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.padacore.ui.test.utils.ProjectDescriptionUtils.CheckProjectContainsAdaNature;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.junit.Test;
import org.padacore.ui.test.utils.ProjectDescriptionUtils;
import org.padacore.ui.test.utils.TestUtils;
import org.padacore.ui.wizards.NewAdaProject;

public class NewAdaProjectTest {

	private NewAdaProject sut;

	private static void checkProjectIsNotNull(IProject project) {
		assertNotNull("Project shall be not null", project);
	}

	private static void checkProjectIsOpen(IProject project) {
		assertTrue("Project shall be open", project.isOpen());
	}

	private static void checkProjectLocation(IProject project, String expectedPath) {
		assertEquals("Project location", expectedPath, project.getLocationURI().getPath());
	}

	private static void checkGprExists(IProject project) {
		assertTrue("GPR project file shall exist",
				new File(TestUtils.getFileAbsolutePath(project, project.getName() + ".gpr"))
						.exists());
	}

	@Test
	public void testCreateProjectWithDefaultLocation() {

		final String comment = "Create new project with default location";
		sut = new NewAdaProject("ProjectDefaultLocation", null);

		IProject createdProject = sut.create(false);

		checkProjectIsNotNull(createdProject);
		checkProjectIsOpen(createdProject);
		checkProjectLocation(createdProject, TestUtils.getWorkspaceAbsolutePath() + "/"
				+ createdProject.getName());
		checkGprExists(createdProject);
		ProjectDescriptionUtils.CheckAdaBuilderIsTheFirstBuilder(createdProject, comment);
		
		CheckProjectContainsAdaNature(createdProject, comment);
	}

}
