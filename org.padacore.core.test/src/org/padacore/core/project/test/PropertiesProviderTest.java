package org.padacore.core.project.test;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.project.PropertiesProvider;
import org.padacore.core.project.PropertiesProvider.ProjectKind;
import org.padacore.core.test.utils.CommonTestUtils;

public class PropertiesProviderTest {

	private final static String QUALIFIED_NAME_PREFIX = "org.padacore";

	private class Fixture {
		public IProject project;
		public PropertiesProvider sut;
	}

	private Fixture fixture;

	@Before
	public void createFixture() {
		this.fixture = new Fixture();
		this.fixture.project = CommonTestUtils.CreateAdaProject();
		this.fixture.sut = new PropertiesProvider(this.fixture.project);
	}

	@Test
	public void testProjectKindRetrieval() {
		// Created
		try {
			this.fixture.project.setPersistentProperty(new QualifiedName(
					QUALIFIED_NAME_PREFIX, "projectKind"), "created");
		} catch (CoreException e) {
			e.printStackTrace();
		}
		assertTrue(this.fixture.sut.getProjectKind() == ProjectKind.CREATED);

		// Imported
		try {
			this.fixture.project.setPersistentProperty(new QualifiedName(
					QUALIFIED_NAME_PREFIX, "projectKind"), "imported");
		} catch (CoreException e) {
			e.printStackTrace();
		}
		assertTrue(this.fixture.sut.getProjectKind() == ProjectKind.IMPORTED);
	}

}
