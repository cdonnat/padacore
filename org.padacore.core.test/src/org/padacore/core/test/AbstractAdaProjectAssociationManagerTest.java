package org.padacore.core.test;

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
	private IProject firstAdaProject;

	@Before
	public void createFixture() {
		this.sut = new AdaProjectAssociationManagerStub();
		this.firstAdaProject = CommonTestUtils.CreateAdaProject("adaProject");
	}

	@After
	public void tearDown() {
		try {
			this.firstAdaProject.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAssociationIsRestoredForAllOpenedAdaProjectsInWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		assertFalse(
				"No association to Ada project at init",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.firstAdaProject));
		
		this.sut.performAssociationToAdaProjectForAllProjectsWithAdaNatureOf(workspace);

		assertTrue(
				"Association to Ada project restored",
				this.sut.isEclipseProjectAssociatedToAdaProject(this.firstAdaProject));
	}
}
