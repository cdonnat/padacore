package org.padacore.ui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.junit.Test;
import org.padacore.core.AdaProjectNature;
import org.padacore.core.GprProject;
import org.padacore.core.NewAdaProject;

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

	private static void checkProjectContainsAdaNature(IProject project) {
		try {
			IProjectDescription desc = project.getDescription();
			assertEquals("Project shall contain one nature", 1,
					desc.getNatureIds().length);
			assertTrue("Project natures shall contain adaProjectNature",
					desc.hasNature(AdaProjectNature.NATURE_ID));
		} catch (CoreException e) {
			e.printStackTrace();
		}
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
		GprProject retrievedGpr = checkAGprIsAssociatedToProject(createdProjectFromGpr);

		assertTrue("Associated GprProject shall be the one created from",
				existingGpr == retrievedGpr);
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithMain() {

		sut = new NewAdaProject("TestProjectWithMain", null);

		IProject createdProjectWithMain = sut.create(true);

		checkProjectIsNotNull(createdProjectWithMain);
		checkProjectIsOpen(createdProjectWithMain);
		checkProjectContainsAdaNature(createdProjectWithMain);
		checkProjectLocation(createdProjectWithMain,
				TestUtils.getWorkspaceAbsolutePath() + "/"
						+ createdProjectWithMain.getName());
		checkGprExists(createdProjectWithMain);

		GprProject associatedGpr = checkAGprIsAssociatedToProject(createdProjectWithMain);
		if (associatedGpr != null) {
			checkDefaultGprContents(associatedGpr, true);
		}
	}

	@Test
	public void testCreateProjectWithDefaultLocationWithoutMain() {

		sut = new NewAdaProject("TestProjectWithoutMain", null);

		IProject createdProjectWithoutMain = sut.create(false);

		checkProjectIsNotNull(createdProjectWithoutMain);
		checkProjectIsOpen(createdProjectWithoutMain);
		checkProjectContainsAdaNature(createdProjectWithoutMain);
		checkProjectLocation(createdProjectWithoutMain,
				TestUtils.getWorkspaceAbsolutePath() + "/"
						+ createdProjectWithoutMain.getName());
		checkGprExists(createdProjectWithoutMain);

		GprProject associatedGpr = checkAGprIsAssociatedToProject(createdProjectWithoutMain);
		if (associatedGpr != null) {
			checkDefaultGprContents(associatedGpr, false);
		}
	}

	private void checkDefaultGprContents(GprProject gprToCheck,
			boolean gprShallBeExecutable) {
		assertTrue("GprProject shall be executable: " + gprShallBeExecutable,
				gprToCheck.isExecutable() == gprShallBeExecutable);
		assertTrue(
				"GprProject shall have " + (gprShallBeExecutable ? "1" : "0")
						+ " executable",
				gprToCheck.getExecutableSourceNames().size() == (gprShallBeExecutable ? 1
						: 0));
		if (gprShallBeExecutable) {
			assertTrue(
					"GprProject executable shall be called main.adb",
					gprToCheck.getExecutableSourceNames().get(0)
							.equals("main.adb"));
		}

	}

	private GprProject checkAGprIsAssociatedToProject(IProject createdProject) {

		GprProject associatedGpr = null;

		try {
			Object associatedProperty = createdProject
					.getSessionProperty(new QualifiedName(
							NewAdaProject.GPR_PROJECT_SESSION_PROPERTY_QUALIFIER,
							createdProject.getName()));

			assertTrue("GprProject shall be associated",
					associatedProperty instanceof GprProject);

			if (associatedProperty instanceof GprProject) {
				associatedGpr = (GprProject) associatedProperty;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return associatedGpr;
	}
}
