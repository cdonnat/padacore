package org.padacore.core.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.padacore.core.GprProject;
import org.padacore.core.GprProjectInterpreter;

public class GprProjectInterpreterTest {

	private final static String TEST_EXEC_DIR_NAME = "exec_dir";
	private final static String TEST_OBJ_DIR_NAME = "object_dir";

	@Test
	public void testExecutableDirectoryRetrieval() {
		// [1] Exec dir is specified
		GprProject withExecDir = new GprProject("test");
		withExecDir.setExecutableDir(TEST_EXEC_DIR_NAME);

		assertTrue("GPR project with exec dir",
				GprProjectInterpreter.getExecutableDirectoryPath(withExecDir)
						.equals(TEST_EXEC_DIR_NAME));

		// [2] Only object dir is specified
		GprProject withObjectDir = new GprProject("test");
		withObjectDir.setObjectDir(TEST_OBJ_DIR_NAME);

		assertTrue("GPR project with object dir",
				GprProjectInterpreter.getExecutableDirectoryPath(withObjectDir)
						.equals(TEST_OBJ_DIR_NAME));

		// [3] Neither exec dir nor object dir is specified
		GprProject emptyObjectAndExecDir = new GprProject("test");
		assertTrue(
				"GPR project with neither exec nor object dir",
				GprProjectInterpreter.getExecutableDirectoryPath(
						emptyObjectAndExecDir).equals("."));
	}

	@Test
	public void testExecutableNamesRetrieval() {
		GprProject gpr = new GprProject("test");
		gpr.addExecutableName("main.ads");
		gpr.addExecutableName("procedure.adb");
		
		boolean osIsWindows = System.getProperty("os.name").contains(
				"win") || System.getProperty("os.name").contains("Win");

		List<String> execNames = GprProjectInterpreter.getExecutableNames(gpr);

		assertTrue("Executable names list shall contain 2 elements",
				execNames.size() == 2);

		String firstExecNameExpected = (osIsWindows ? "main.exe" : "main");

		assertTrue("First executable name is correct",
				execNames.get(0).equals(firstExecNameExpected));

		String secondExecNameExpected = (osIsWindows ? "procedure.exe" : "procedure");
		assertTrue("Second executable name is correct", execNames.get(1)
				.equals(secondExecNameExpected));
	}
}
