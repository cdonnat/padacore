package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.Path;
import org.gpr4j.api.ExternalVariable;
import org.junit.Test;
import org.padacore.core.builder.AdaProjectBuilderCmds;
import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.gnat.ScenarioItem;

public class AdaProjectBuilderCmdsTest {

	private GnatAdaProject project;
	private Scenario scenario;
	private AdaProjectBuilderCmds sut;

	private void createFixture() {
		this.project = mock(GnatAdaProject.class);
		this.scenario = mock(Scenario.class);
		this.sut = new AdaProjectBuilderCmds(this.scenario, this.project);
	}

	private String getGprFullPath() {
		return new Path("test").append(this.project.getName() + ".gpr").toOSString();
	}

	private void setProjectName(String projectName) {
		when(this.project.getName()).thenReturn(projectName);
		when(this.project.getRootPath()).thenReturn(new Path("test"));
	}

	private void setExternalVariable(ScenarioItem var) {
		Set<ScenarioItem> vars = new TreeSet<>();
		vars.addAll(this.scenario.getExternalVariablesFor(this.project));
		vars.add(var);
		when(this.scenario.getExternalVariablesFor(this.project)).thenReturn(vars);
	}

	static private void CheckCommand(String comment, String[] expected, String[] computed) {
		assertEquals(comment + " size", expected.length, computed.length);
		for (int i = 0; i < computed.length; i++) {
			assertEquals(comment, expected[i], computed[i]);
		}
	}

	@Test
	public void testBuild() {
		this.createFixture();

		this.setProjectName("lang");

		CheckCommand("When no external variable defined", new String[] { "gprbuild", "-d", "-p",
				"-P", this.getGprFullPath() }, this.sut.build());

		this.setExternalVariable(new ScenarioItem(new ExternalVariable("platform", "native-zfp",
				null), "native-zfp"));

		CheckCommand("When one external variable defined", new String[] { "gprbuild", "-d", "-p",
				"-P", this.getGprFullPath(), "-Xplatform=native-zfp" }, this.sut.build());

		this.setExternalVariable(new ScenarioItem(new ExternalVariable("nb_proc", "1", null), "1"));

		CheckCommand("When two external variable defined", new String[] { "gprbuild", "-d", "-p",
				"-P", this.getGprFullPath(), "-Xnb_proc=1", "-Xplatform=native-zfp" }, this.sut.build());
	}

	@Test
	public void testClean() {
		this.createFixture();

		this.setProjectName("lang");

		CheckCommand("When no external variable defined",
				new String[] { "gprclean", "-P", this.getGprFullPath() }, this.sut.clean());

		this.setExternalVariable(new ScenarioItem(new ExternalVariable("PLATFORM", "native-zfp",
				null), "native-zfp"));

		CheckCommand("When one external variable defined",
				new String[] { "gprclean", "-P", this.getGprFullPath() }, this.sut.clean());
	}
}
