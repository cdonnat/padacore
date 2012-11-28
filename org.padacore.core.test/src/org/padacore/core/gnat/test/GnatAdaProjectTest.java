package org.padacore.core.gnat.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.GprProject;

public class GnatAdaProjectTest {

	private final static String TEST_EXEC_DIR_NAME = "exec_dir";
	private final static String TEST_OBJ_DIR_NAME = "object_dir";

	private GprProject gprProject;
	private GnatAdaProject sut;

	@Before
	public void createFixture() {
		this.gprProject = new GprProject("test");
		this.gprProject.setExecutable(true);
		this.sut = new GnatAdaProject(this.gprProject);
	}

	@Test
	public void testExeDirRetrievalWhenExecDirIsSpecified() {

		this.gprProject.setExecutableDir(TEST_EXEC_DIR_NAME);

		assertTrue("GPR project with exec dir", this.sut
				.getExecutableDirectoryPath().equals(TEST_EXEC_DIR_NAME));
	}

	@Test
	public void testExeDirRetrievalWhenOnlyObjectDirIsSpecified() {
		this.gprProject.setObjectDir(TEST_OBJ_DIR_NAME);

		assertTrue("GPR project with object dir", this.sut
				.getExecutableDirectoryPath().equals(TEST_OBJ_DIR_NAME));
	}

	@Test
	public void testExecDirRetrievalWhenNeitherExecDirNorObjectDirAreSpecified() {
		assertTrue("GPR project with neither exec nor object dir", this.sut
				.getExecutableDirectoryPath().equals("."));
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
	public void testExecutableNamesRetrieval() {
		this.gprProject.addExecutableName("main.ads");
		this.gprProject.addExecutableName("procedure.adb");

		boolean osIsWindows = System.getProperty("os.name").contains("win")
				|| System.getProperty("os.name").contains("Win");

		List<String> execNames = this.sut.getExecutableNames();

		assertTrue("Executable names list shall contain 2 elements",
				execNames.size() == 2);

		String firstExecNameExpected = (osIsWindows ? "main.exe" : "main");

		assertTrue("First executable name is correct",
				execNames.get(0).equals(firstExecNameExpected));

		String secondExecNameExpected = (osIsWindows ? "procedure.exe"
				: "procedure");
		assertTrue("Second executable name is correct", execNames.get(1)
				.equals(secondExecNameExpected));
	}
}
