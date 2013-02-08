package org.padacore.ui.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.ui.launch.AdaProjectPropertyTester;

public class AdaProjectPropertyTesterTest {

	private IProject executableAdaProject;
	private IProject nonAdaProject;
	private IProject nonExecutableAdaProject;

	@Before
	public void createFixture() {
		this.executableAdaProject = CommonTestUtils
				.CreateExecutableAdaProject();
		this.nonExecutableAdaProject = CommonTestUtils.CreateAdaProject();
		this.nonAdaProject = CommonTestUtils.CreateNonAdaProject(true);
	}

	private void createFile(IFile fileHandle) {
		try {
			if (fileHandle.exists()) {
				fileHandle.delete(false, null);
			}
			fileHandle.create(new ByteArrayInputStream(new byte[] { 'e' }),
					false, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	private IFile createFileInProject(IProject project) {
		IFile file = project.getFile("test_file");

		this.createFile(file);

		return file;
	}

	@Test
	public void testIsAdaExecutableProject() {
		AdaProjectPropertyTester sut = new AdaProjectPropertyTester();

		assertTrue("Executable Ada project",
				sut.test(this.executableAdaProject, "isAdaProject", null, null));

		assertFalse("Non executable Ada project", sut.test(
				this.nonExecutableAdaProject, "isAdaProject", null, null));

		assertFalse("Non Ada project",
				sut.test(this.nonAdaProject, "isAdaProject", null, null));
	}

	@Test
	public void testBelongsToExecutableAdaProject() {
		AdaProjectPropertyTester sut = new AdaProjectPropertyTester();

		IFile fileInExecutableAdaProject = this
				.createFileInProject(this.executableAdaProject);
		IFile fileInNonExecutableAdaProject = this
				.createFileInProject(this.nonExecutableAdaProject);
		IFile fileInNonAdaProject = this
				.createFileInProject(this.nonAdaProject);

		assertTrue("File in executable Ada project", sut.test(
				fileInExecutableAdaProject, "belongsToAdaProject", null, null));

		assertFalse("File in non executable Ada project", sut.test(
				fileInNonExecutableAdaProject, "belongsToAdaProject", null,
				null));

		assertFalse("File in non Ada project", sut.test(fileInNonAdaProject,
				"belongsToAdaProject", null, null));
	}

}
