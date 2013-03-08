package org.padacore.ui.launch.test;

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

	private static boolean IsWindowsSystem() {
		String osName = System.getProperty("os.name");

		return osName.contains("win") || osName.contains("Win");
	}

	private final static String EXECUTABLE_SOURCE_FILENAME = "main.adb";
	private final static String EXECUTABLE_BINARY_FILENAME = IsWindowsSystem() ? "main.exe"
			: "main";

	private IProject executableAdaProject;
	private IProject nonAdaProject;
	private IProject nonExecutableAdaProject;
	private AdaProjectPropertyTester sut;

	@Before
	public void createFixture() {
		this.executableAdaProject = CommonTestUtils.CreateAdaProject(true,
				true, new String[] { EXECUTABLE_SOURCE_FILENAME });
		this.nonExecutableAdaProject = CommonTestUtils.CreateAdaProject(true);
		this.nonAdaProject = CommonTestUtils.CreateNonAdaProject(true);
		this.sut = new AdaProjectPropertyTester();
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

	private IFile createFileInProject(IProject project, String fileName) {
		IFile file = project.getFile(fileName);

		this.createFile(file);

		return file;
	}

	private void checkProjectPropertyTestIsPassed(boolean isPassed,
			IProject project, String comment) {
		assertTrue(comment,
				this.sut.test(project, "isAdaProject", null, null) == isPassed);
	}

	private void checkFilePropertyTestIsPassed(boolean isPassed, IFile file,
			String comment) {
		assertTrue(
				comment,
				this.sut.test(file, "belongsToAdaProject", null, null) == isPassed);

	}

	@Test
	public void testIsAdaExecutableProject() {
		this.checkProjectPropertyTestIsPassed(true, this.executableAdaProject,
				"Executable Ada project");
		this.checkProjectPropertyTestIsPassed(false,
				this.nonExecutableAdaProject, "Non executable Ada project");
		this.checkProjectPropertyTestIsPassed(false, this.nonAdaProject,
				"Non Ada project");
	}

	@Test
	public void testIsAnExecutableOfAdaProject() {
		IFile nonExecutableFileInExecutableAdaProject = this
				.createFileInProject(this.executableAdaProject, "notMyMain.adb");
		IFile executableSourceFileInAdaProject = this.createFileInProject(
				this.executableAdaProject, EXECUTABLE_SOURCE_FILENAME);
		IFile executableBinaryFileInAdaProject = this.createFileInProject(
				this.executableAdaProject, EXECUTABLE_BINARY_FILENAME);
		IFile fileInNonExecutableAdaProject = this.createFileInProject(
				this.nonExecutableAdaProject, EXECUTABLE_SOURCE_FILENAME);
		IFile fileInNonAdaProject = this.createFileInProject(
				this.nonAdaProject, EXECUTABLE_SOURCE_FILENAME);

		this.checkFilePropertyTestIsPassed(true,
				executableSourceFileInAdaProject,
				"Executable source file in Ada project");

		this.checkFilePropertyTestIsPassed(true,
				executableBinaryFileInAdaProject,
				"Executable binary file in Ada project");

		this.checkFilePropertyTestIsPassed(false,
				nonExecutableFileInExecutableAdaProject,
				"Non executable file in executable Ada project");

		this.checkFilePropertyTestIsPassed(false,
				fileInNonExecutableAdaProject,
				"File in non executable Ada project");

		this.checkFilePropertyTestIsPassed(false, fileInNonAdaProject,
				"File in non Ada project");
	}

}
