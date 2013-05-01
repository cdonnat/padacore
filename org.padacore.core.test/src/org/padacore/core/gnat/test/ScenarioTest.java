package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.gpr4j.api.ExternalVariable;
import org.junit.Test;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.gnat.ScenarioItem;

public class ScenarioTest {

	Scenario sut;

	private void createFixture() {
		this.sut = new Scenario();
	}

	static private void AddExternalVariable(GnatAdaProject project, ExternalVariable var) {
		Set<ExternalVariable> vars = project.getExternalVariables();
		vars.add(var);
		when(project.getExternalVariables()).thenReturn(vars);
	}

	static private void RemoveExternalVariable(GnatAdaProject project, ExternalVariable var) {
		Set<ExternalVariable> vars = project.getExternalVariables();
		vars.remove(var);
		when(project.getExternalVariables()).thenReturn(vars);
	}

	private void checkExternalVariableIn(GnatAdaProject project, String varName,
			String expectedValue, boolean exists, String comment) {
		boolean isFound = false;
		String value = null;
		Set<ScenarioItem> computed = this.sut.getExternalVariablesFor(project);
		for (ScenarioItem item : computed) {
			if (item.getName() == varName) {
				value = item.getValue();
				isFound = true;
			}
		}
		assertEquals(comment, exists, isFound);
		if (isFound) {
			assertEquals(comment, expectedValue, value);
		}
	}

	@Test
	public void testSetExternalVariableValueFor() {
		this.createFixture();

		GnatAdaProject project1 = mock(GnatAdaProject.class);
		GnatAdaProject project2 = mock(GnatAdaProject.class);

		ExternalVariable var1 = new ExternalVariable("var1", "default", null);
		ExternalVariable var2 = new ExternalVariable("var2", "default2", null);

		AddExternalVariable(project1, new ExternalVariable("var1", "default", null));
		AddExternalVariable(project1, new ExternalVariable("var1", "default", null));
		AddExternalVariable(project1, var2);
		AddExternalVariable(project2, var1);

		this.sut.setExternalVariableValueFor(project1, "var1", "var1 new value");

		assertEquals(2, this.sut.getExternalVariablesFor(project1).size());

		this.checkExternalVariableIn(project1, "var1", "var1 new value", true, "var1 for project1");
		this.checkExternalVariableIn(project1, "var2", "default2", true, "var2 for project1");
		this.checkExternalVariableIn(project2, "var1", "var1 new value", true, "var1 for project2");

		RemoveExternalVariable(project1, var1);

		this.checkExternalVariableIn(project1, "var1", "", false, "var1 for project1 after");
		this.checkExternalVariableIn(project1, "var2", "default2", true, "var2 for project1 after");
		this.checkExternalVariableIn(project2, "var1", "var1 new value", true,
				"var1 for project2 after");

		this.sut.setExternalVariableValueFor(project1, "var2", "newValue");

		this.checkExternalVariableIn(project1, "var2", "newValue", true,
				"var2 for project1 after renaming");
	}
	
	@Test
	public void testGetIsOrdered() {
		this.createFixture();

		GnatAdaProject project1 = mock(GnatAdaProject.class);

		ExternalVariable var1 = new ExternalVariable("var1", "default", null);
		ExternalVariable var2 = new ExternalVariable("var2", "default2", null);

		AddExternalVariable(project1, var1);
		AddExternalVariable(project1, var2);
		
		String[] expected = {"var1", "var2"};
		int i = 0;
		
		for (ScenarioItem var : this.sut.getExternalVariablesFor(project1)) {
			assertEquals("Check order", expected[i], var.getName());
			i++;
		}
	}
}
