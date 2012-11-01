package org.padacore.core.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.GprAssociationManager;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprAssociationManagerTest {

	private GprAssociationManager sut;
	private IProject adaProject;

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
	}

	@After
	public void tearDown() {
		try {
			IResource[] resourcesToDelete = new IResource[1];
			resourcesToDelete[0] = ResourcesPlugin.getWorkspace().getRoot()
					.getProject("adaProject");
			ResourcesPlugin.getWorkspace()
					.delete(resourcesToDelete, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAssociationToGprIsRestoredOnProjectOpening() {
		this.sut.performAssociationToGprProject(this.adaProject, new Path(
				CommonTestUtils.GetPathToSampleProject()));

		CommonTestUtils.CheckGprAssociationToProject(this.adaProject, true);
	}

	@Test
	public void testAssociationIsRestoredForAllOpenedAdaProjectsInWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IProject nonAdaProject = null;

		try {
			nonAdaProject = CommonTestUtils
					.CreateNonAdaProject("nonAdaProject");
		} catch (CoreException e) {
			e.printStackTrace();
		}

		this.sut.performAssociationToGprProjectForAllAdaProjectsOf(workspace);

		CommonTestUtils.CheckGprAssociationToProject(this.adaProject, true);

		CommonTestUtils.CheckGprAssociationToProject(nonAdaProject, false);
	}
}
