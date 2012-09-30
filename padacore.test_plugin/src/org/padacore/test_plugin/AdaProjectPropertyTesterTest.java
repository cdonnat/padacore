package org.padacore.test_plugin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.padacore.launch.ui.AdaProjectPropertyTester;
import org.padacore.wizards.NewAdaProject;

public class AdaProjectPropertyTesterTest {

	private IProject projectWithAdaNature;
	private IProject projectWithoutAdaNature;
	private IFile fileInAdaProject;
	private IFile fileInStandardProject;

	@Before
	public void setupTests() {
		this.createTestProjects();
		this.addFilesToTestProjects();
	}

	private void createTestProjects() {
		NewAdaProject adaProject = new NewAdaProject("test", true, null);
		this.projectWithAdaNature = adaProject.create(false);

		NewAdaProject otherAdaProject = new NewAdaProject("other_test", true,
				null);
		this.projectWithoutAdaNature = otherAdaProject.create(false);
		IProjectDescription adaProjectDescription;

		try {
			adaProjectDescription = this.projectWithoutAdaNature
					.getDescription();
			adaProjectDescription
					.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
			this.projectWithoutAdaNature.setDescription(adaProjectDescription,
					null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
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

	private void addFilesToTestProjects() {
		this.fileInAdaProject = this.projectWithAdaNature.getFile("test_file");
		this.fileInStandardProject = this.projectWithoutAdaNature
				.getFile("test_file");

		this.createFile(this.fileInAdaProject);
		this.createFile(this.fileInStandardProject);
	}

	@Test
	public void isAdaProjectTest() {
		AdaProjectPropertyTester sut = new AdaProjectPropertyTester();

		assertTrue("Project with Ada nature",
				sut.test(this.projectWithAdaNature, "isAdaProject", null, null));

		assertFalse("Project without Ada nature", sut.test(
				this.projectWithoutAdaNature, "isAdaProject", null, null));
	}

	@Test
	public void belongsToAdaProjectTest() {
		AdaProjectPropertyTester sut = new AdaProjectPropertyTester();

		assertTrue("File in project with Ada nature", sut.test(
				this.fileInAdaProject, "belongsToAdaProject", null, null));

		assertFalse("File in project without Ada nature", sut.test(
				this.fileInStandardProject, "belongsToAdaProject", null, null));

	}

}
