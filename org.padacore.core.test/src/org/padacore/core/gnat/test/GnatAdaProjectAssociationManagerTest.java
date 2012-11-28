package org.padacore.core.gnat.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.gnat.GnatAdaProjectAssociationManager;
import org.padacore.core.test.utils.CommonTestUtils;

public class GnatAdaProjectAssociationManagerTest {

	private GnatAdaProjectAssociationManager sut;
	private IProject firstAdaProject;

	@Before
	public void createFixture() {
		this.sut = new GnatAdaProjectAssociationManager();
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
	
	private void runAssociationTest(IProject project) {
		CommonTestUtils.CheckAdaProjectAssociationToProject(project, false);

		this.sut.associateToAdaProject(project);
		CommonTestUtils.CheckAdaProjectAssociationToProject(project, true);
	}

	@Test
	public void testAssociationToAdaProject() {
		this.runAssociationTest(this.firstAdaProject);
	}

	@Test
	public void testAssociationIsRestoredForAllOpenedAdaProjectsInWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		CommonTestUtils.RemoveAssociationToAdaProject(this.firstAdaProject);
		CommonTestUtils.CheckAdaProjectAssociationToProject(this.firstAdaProject, false);
		
		this.sut.performAssociationToAdaProjectForAllProjectsWithAdaNatureOf(workspace);

		CommonTestUtils.CheckAdaProjectAssociationToProject(this.firstAdaProject, true);
	}
}
