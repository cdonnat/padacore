package org.padacore.core.project.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.project.PropertiesManager.ProjectKind;
import org.padacore.core.test.utils.CommonTestUtils;

public class PropertiesManagerTest {

	private class Fixture {
		public IProject project;
		public PropertiesManager sut;
	}

	private Fixture fixture;

	@Before
	public void createFixture() {
		this.fixture = new Fixture();
		this.fixture.project = CommonTestUtils.CreateAdaProject();
		this.fixture.sut = new PropertiesManager(this.fixture.project);
	}

	@Test
	public void testProjectKind() {
		// Created
		this.fixture.sut.setProjectKind(ProjectKind.CREATED);
		assertTrue(this.fixture.sut.getProjectKind() == ProjectKind.CREATED);

		// Imported
		this.fixture.sut.setProjectKind(ProjectKind.IMPORTED);
		assertTrue(this.fixture.sut.getProjectKind() == ProjectKind.IMPORTED);
	}

	@Test
	public void testAdaProject() {
		IAdaProject adaProject = mock(IAdaProject.class);

		this.fixture.sut.setAdaProject(adaProject);

		assertTrue(this.fixture.sut.getAdaProject() == adaProject);
	}

	@After
	public void tearDown() {
		try {
			this.fixture.project.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
