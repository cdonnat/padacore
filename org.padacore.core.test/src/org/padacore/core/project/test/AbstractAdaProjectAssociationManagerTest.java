package org.padacore.core.project.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.test.stubs.AdaProjectAssociationManagerStub;
import org.padacore.core.test.utils.CommonTestUtils;

public class AbstractAdaProjectAssociationManagerTest {

	private AdaProjectAssociationManagerStub sut;
	private IProject openedAdaProject;
	private IProject closedAdaProject;
	private IProject nonAdaProject;

	@Before
	public void createFixture() {
		this.sut = new AdaProjectAssociationManagerStub();
		this.openedAdaProject = CommonTestUtils.CreateAdaProject(true, false);
		this.closedAdaProject = CommonTestUtils.CreateAdaProject(false, false);
		this.nonAdaProject = CommonTestUtils.CreateNonAdaProject(
				"nonAdaProject", true);
	}

	@After
	public void tearDown() {
		try {
			this.openedAdaProject.delete(true, null);
			this.nonAdaProject.delete(true, null);
			this.closedAdaProject.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAssociationIsRestoredForAllOpenedAdaProjectsInWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		assertFalse(
				"No association to opened Ada project at init",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.openedAdaProject));
		assertFalse(
				"No association to closed Ada project at init",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.closedAdaProject));
		assertFalse(
				"No association to non-Ada project at init",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.nonAdaProject));

		this.sut.performAssociationToAdaProjectForAllProjectsWithAdaNatureOf(workspace);

		assertTrue(
				"Association to opened Ada project restored",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.openedAdaProject));
		assertFalse(
				"Association to closed Ada project not restored",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.closedAdaProject));
		assertFalse(
				"Association to non-Ada project not performed",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.nonAdaProject));
	}
}
