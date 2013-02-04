package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprLoaderTest {

	private GprLoader sut;

	private void createFixture(String gprProjectName) {
		sut = new GprLoader(new Path(CommonTestUtils.GetPathToTestProject() + gprProjectName));
	}

	@Test
	public void testSingleGpr() {
		createFixture("sample_project.gpr");

		sut.load();

		checkNbOfLoadedProjects(1);

		checkAttribute("Exec_Dir", "new_exe");
		checkAttribute("Object_Dir", "objects");
		checkAttribute("Source_Dirs", new String[] { "src", "include" });
		checkAttribute("Main", new String[] { "proc1.adb" });
		checkAttribute("Switches (\"main.ada\")", new String[] { "-v", "-gnatv" });

		checkVariable("my_var", "My_Var");
		checkVariable("my_var2", "My_Var2");
		checkVariable("Name", "My_Var");
		checkVariable("Save_Name", "My_Var.saved");
		checkVariable("Empty_List", new String[] {});
		checkVariable("List_With_One_Element", new String[] { "-gnaty" });
		checkVariable("List_With_Two_Elements", new String[] { "-gnaty", "-gnatg" });
		checkVariable("Long_List", new String[] { "main.ada", "pack1_.ada", "pack1.ada",
				"pack2_.ada" });
	}

	// @Test
	public void testMultipleGpr() {
		createFixture("b.gpr");

		sut.load();

		checkNbOfLoadedProjects(1);
		checkVariable("from_external", "static");
	}

	private void checkNbOfLoadedProjects(int expectedNumberOfLoadedProject) {
		assertEquals("Number of loaded projects", expectedNumberOfLoadedProject, sut
				.getLoadedProjects().size());
	}

	private void checkVariable(String variableName, String expectedValue) {
		assertEquals("Variable value", expectedValue, sut.getLoadedProjects().get(0).getProject()
				.getVariable(variableName).getAsString());
	}

	private void checkVariable(String variableName, String[] expectedValues) {
		List<String> computed = sut.getLoadedProjects().get(0).getProject()
				.getVariable(variableName).getAsStringList();
		assertEquals("Variable size", expectedValues.length, computed.size());
		for (int i = 0; i < computed.size(); i++) {
			assertEquals("Variable n¡" + i, expectedValues[i], computed.get(i));
		}
	}

	private void checkAttribute(String attributeName, String expectedValue) {
		assertEquals("Attribute value", expectedValue, sut.getLoadedProjects().get(0).getProject()
				.getAttribute(attributeName).getAsString());
	}

	private void checkAttribute(String attributeName, String[] expectedValues) {
		List<String> computed = sut.getLoadedProjects().get(0).getProject()
				.getAttribute(attributeName).getAsStringList();
		assertEquals("Attribute size", expectedValues.length, computed.size());
		for (int i = 0; i < computed.size(); i++) {
			assertEquals("Attribute n¡" + i, expectedValues[i], computed.get(i));
		}
	}
}
