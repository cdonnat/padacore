package org.padacore.ui.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.ui.ResourcePropertyTester;

public class ResourcePropertyTesterTest {

	private final static String EXECUTABLE_SOURCE_FILENAME = "main.adb";
	private final static String EXECUTABLE_BINARY_FILENAME = CommonTestUtils
			.GetExecutableNameFromExecSourceName(EXECUTABLE_SOURCE_FILENAME);

	private IProject executableAdaProject;
	private IProject nonExecutableAdaProject;
	private IProject nonAdaProject;
	private ResourcePropertyTester sut;
	private IFile editedFile;
	private IEditorPart editor;

	@Before
	public void createFixture() {
		this.executableAdaProject = CommonTestUtils.CreateAdaProject(true,
				true, new String[] { EXECUTABLE_SOURCE_FILENAME });
		this.nonExecutableAdaProject = CommonTestUtils.CreateAdaProject(true);
		this.nonAdaProject = CommonTestUtils.CreateNonAdaProject(true);
		this.editor = mock(IEditorPart.class);
		IFileEditorInput fileEditorInput = mock(IFileEditorInput.class);
		this.editedFile = mock(IFile.class);
		when(this.editor.getEditorInput()).thenReturn(fileEditorInput);
		when(fileEditorInput.getFile()).thenReturn(this.editedFile);
		this.sut = new ResourcePropertyTester();
	}

	private IFile createFileInProject(IProject project, String fileName) {
		IFile file = project.getFile(fileName);

		CommonTestUtils.CreateFileIfNotExisting(file);

		return file;
	}

	private void checkExecutableAdaProjectPropertyTestIsPassed(
			boolean isPassed, IProject project, String comment) {
		assertTrue(
				comment,
				this.sut.test(project, "isExecutableAdaProject", null, null) == isPassed);
	}

	private void checkInAdaProjectPropertyTestIsPassed(boolean isPassed,
			IResource resource, String comment) {
		assertTrue(
				comment,
				this.sut.test(resource, "isInAdaProject", null, null) == isPassed);
	}

	private void checkFilePropertyTestIsPassed(boolean isPassed, IFile file,
			String comment) {
		assertTrue(
				comment,
				this.sut.test(file, "isExecutableInAdaProject", null, null) == isPassed);

	}

	@Test
	public void testIsAdaExecutableProject() {
		this.checkExecutableAdaProjectPropertyTestIsPassed(true,
				this.executableAdaProject, "Executable Ada project");
		this.checkExecutableAdaProjectPropertyTestIsPassed(false,
				this.nonExecutableAdaProject, "Non-executable Ada project");
	}

	@Test
	public void testIsInAdaProject() {
		this.checkInAdaProjectPropertyTestIsPassed(true,
				this.executableAdaProject.getFile("test"),
				"In executable Ada project");
		this.checkInAdaProjectPropertyTestIsPassed(true,
				this.nonExecutableAdaProject.getFolder("test"),
				"In non-executable Ada project");
		this.checkInAdaProjectPropertyTestIsPassed(false,
				this.nonAdaProject.getFile("test"), "Non-Ada project");

	}

	private void runTestCaseForFile(IProject project, String fileNameToCreate,
			boolean expectedTestIsPassed, String comment) {
		IFile testFile = this.createFileInProject(project, fileNameToCreate);

		this.checkFilePropertyTestIsPassed(expectedTestIsPassed, testFile,
				comment);
	}

	@Test
	public void testIsAnExecutableOfAdaProject() {
		this.runTestCaseForFile(this.executableAdaProject, "notMyMain.adb",
				false, "Non-executable file");

		this.runTestCaseForFile(this.executableAdaProject,
				EXECUTABLE_SOURCE_FILENAME, true, "Executable source file");

		this.runTestCaseForFile(this.executableAdaProject,
				EXECUTABLE_BINARY_FILENAME, true, "Executable file");

		this.runTestCaseForFile(this.nonExecutableAdaProject,
				EXECUTABLE_SOURCE_FILENAME, false,
				"File in non-executable Ada project");
	}

	private void checkEditorPropertyTestIsPassed(boolean isPassed,
			String comment) {
		assertTrue(
				comment,
				this.sut.test(this.editor, "isAdaExecutableEditor", null, null) == isPassed);
	}

	private IPath getFileLocationFromFilename(IProject project, String filename) {
		return project.getFile(filename).getLocation();
	}

	private void runTestCaseForEditor(IProject project, String filename,
			boolean expectedTestIsPassed, String comment) {
		when(this.editedFile.getLocation()).thenReturn(
				this.getFileLocationFromFilename(project, filename));
		when(this.editedFile.getProject()).thenReturn(project);
		when(this.editedFile.getName()).thenReturn(filename);

		this.checkEditorPropertyTestIsPassed(expectedTestIsPassed, comment);
	}

	@Test
	public void testIsAnEditorOfExecutable() {
		this.runTestCaseForEditor(this.nonExecutableAdaProject,
				EXECUTABLE_SOURCE_FILENAME, false,
				"Editor for file in non-executable Ada project");

		this.runTestCaseForEditor(this.executableAdaProject, "notMyMain.adb",
				false, "Editor for non-executable file");

		this.runTestCaseForEditor(this.executableAdaProject,
				EXECUTABLE_SOURCE_FILENAME, true, "Executable source file");

		this.runTestCaseForEditor(this.executableAdaProject,
				EXECUTABLE_BINARY_FILENAME, true, "Executable file");
	}
}
