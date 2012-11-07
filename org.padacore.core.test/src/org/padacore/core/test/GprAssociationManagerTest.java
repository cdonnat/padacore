package org.padacore.core.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.GprAssociationManager;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprAssociationManagerTest {

	private GprAssociationManager sut;
	private IProject adaProject;
	private IProject nonAdaProject;

	// private GprAssociationManagerStub gprAssociationManager;
	//
	// private final class GprAssociationManagerStub implements
	// IGprAssociationManager {
	//
	// private Map<IProject, IPath> associations;
	//
	// public GprAssociationManagerStub() {
	// this.associations = new HashMap<IProject, IPath>();
	// }
	//
	// @Override
	// public void performAssociationToGprProject(IProject adaProject,
	// IPath gprFilePath) {
	// this.associations.put(adaProject, gprFilePath);
	// }
	//
	// public Map<IProject, IPath> getAssociations() {
	// return associations;
	// }
	// }

	@Before
	public void createFixture() {
		this.sut = new GprAssociationManager();
		this.adaProject = CommonTestUtils.CreateAdaProject("adaProject");
		this.nonAdaProject = CommonTestUtils
				.CreateAndOpenNonAdaProject("nonAdaProject");
	}

	@After
	public void tearDown() {
		try {
			this.adaProject.delete(true, null);
			this.nonAdaProject.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void runAssociationTest(IProject project, boolean associationShallBePerformed) {
		CommonTestUtils.CheckGprAssociationToProject(project, associationShallBePerformed);

		try {
			project.close(null);
			project.open(null);
			CommonTestUtils.CheckGprAssociationToProject(project, associationShallBePerformed);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAssociationToGprIsRestoredOnAdaProjectOpening() {
		this.runAssociationTest(this.adaProject, true);
	}

	@Test
	public void testAssociationToGprIsNotPerformedOnNonAdaProjectOpening() {
		this.runAssociationTest(this.nonAdaProject, false);
	}

	@Test
	public void testAssociationIsRestoredForAllOpenedAdaProjectsInWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		this.sut.performAssociationToGprProjectForAllAdaProjectsOf(workspace);

		CommonTestUtils.CheckGprAssociationToProject(this.adaProject, true);

		CommonTestUtils.CheckGprAssociationToProject(this.nonAdaProject, false);
	}
}
