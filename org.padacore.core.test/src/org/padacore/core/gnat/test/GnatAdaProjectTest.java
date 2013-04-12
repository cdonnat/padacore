package org.padacore.core.gnat.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.gpr4j.api.Gpr;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.test.utils.CommonTestUtils;

public class GnatAdaProjectTest {

	private final static String TEST_EXEC_DIR_NAME = "exec_dir";
	private final static String TEST_OBJ_DIR_NAME = "object_dir";
	private final static String[] EXECUTABLE_SOURCE_NAMES = {"main.ads", "another.adb", "no_extension"};

	private Gpr gprProject;
	private GnatAdaProject sut;

	@Before
	public void createFixture() {
		this.gprProject = CommonTestUtils.CreateGprProject("test", true, EXECUTABLE_SOURCE_NAMES);
		this.sut = new GnatAdaProject(this.gprProject);
	}

	private static IPath GetAbsolutePathFor(String relativePath) {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation()
				.append(relativePath);
	}

	@Test
	public void testExeDirWhenExecDirIsSpecified() {

		this.gprProject.setExecutableDir(TEST_EXEC_DIR_NAME);

		assertTrue(
				"GPR project with exec dir",
				this.sut.getExecutableDirectoryPath().equals(
						GetAbsolutePathFor(TEST_EXEC_DIR_NAME)));
	}

	@Test
	public void testExeDirWithOnlyObjectDir() {
		this.gprProject.setObjectDir(TEST_OBJ_DIR_NAME);

		assertTrue(
				"GPR project with object dir",
				this.sut.getExecutableDirectoryPath().equals(
						GetAbsolutePathFor(TEST_OBJ_DIR_NAME)));
	}

	@Test
	public void testExecDirWithoutExecDirNorObjectDir() {
		assertTrue("GPR project with neither exec nor object dir", this.sut
				.getExecutableDirectoryPath().equals(GetAbsolutePathFor(".")));
	}

	@Test
	public void testExecutableAttribute() {
		this.gprProject.setExecutable(false);
		assertFalse("GPR project shall not be executable",
				this.sut.isExecutable());

		this.gprProject.setExecutable(true);
		assertTrue("GPR project shall be executable", this.sut.isExecutable());
	}

	@Test
	public void testExecutableNames() {
		boolean osIsWindows = System.getProperty("os.name").contains("win")
				|| System.getProperty("os.name").contains("Win");

		List<String> execNames = this.sut.getExecutableNames();

		assertTrue("Executable names list shall contain 3 elements",
				execNames.size() == 3);

		String firstExecNameExpected = (osIsWindows ? "main.exe" : "main");

		assertTrue("First executable name is correct",
				execNames.get(0).equals(firstExecNameExpected));

		String secondExecNameExpected = (osIsWindows ? "another.exe"
				: "another");
		assertTrue("Second executable name is correct", execNames.get(1)
				.equals(secondExecNameExpected));

		String thirdExecNameExpected = (osIsWindows ? "no_extension.exe"
				: "no_extension");
		assertTrue("Third executable name is correct",
				execNames.get(2).equals(thirdExecNameExpected));
	}

	@Test
	public void testExecutableSourceNames() {
		assertTrue("Executable source names list contains 3 elements", this.sut
				.getExecutableSourceNames().size() == 3);

		assertTrue("First executable source name is correct", this.sut
.getExecutableSourceNames().get(0)
						.equals(EXECUTABLE_SOURCE_NAMES[0]));
		assertTrue("Second executable source name is correct", this.sut
				.getExecutableSourceNames().get(1).equals(EXECUTABLE_SOURCE_NAMES[1]));
		assertTrue("Third executable source name is correct", this.sut
				.getExecutableSourceNames().get(2).equals(EXECUTABLE_SOURCE_NAMES[2]));

	}

	@Test
	public void testObjectDirWhenNoObjectDir() {
		assertTrue("Object directory is current directory", this.sut
				.getObjectDirectoryPath().equals(GetAbsolutePathFor(".")));
	}

	@Test
	public void testObjectDirWithObjectDir() {
		this.gprProject.setObjectDir(TEST_OBJ_DIR_NAME);
		assertTrue(
				"Object directory is TEST_OBJ_DIRECTORY",
				this.sut.getObjectDirectoryPath().equals(
						GetAbsolutePathFor(TEST_OBJ_DIR_NAME)));
	}

	@Test
	public void testRootDir() {
		assertTrue(this.sut.getRootPath().equals(GetAbsolutePathFor(".")));
	}

	@Test
	public void testSourcesDirWhenNoneSpecified() {
		assertTrue(this.sut.getSourceDirectoriesPaths().size() == 1);
		assertTrue(this.sut.getSourceDirectoriesPaths().get(0)
				.equals(this.sut.getRootPath()));

	}

	@Test
	public void testSourcesDirWhenSomeExist() {
		this.gprProject.addSourceDir("firstDir");
		this.gprProject.addSourceDir("secondDir");

		assertTrue(this.sut.getSourceDirectoriesPaths().size() == 2);
		assertTrue(this.sut.getSourceDirectoriesPaths().get(0)
				.equals(this.sut.getRootPath().append("firstDir")));
		assertTrue(this.sut.getSourceDirectoriesPaths().get(1)
				.equals(this.sut.getRootPath().append("secondDir")));

	}
}
