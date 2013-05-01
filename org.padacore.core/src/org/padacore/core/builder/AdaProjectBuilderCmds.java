package org.padacore.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.padacore.core.gnat.GnatAdaProject;
import org.padacore.core.gnat.Scenario;
import org.padacore.core.gnat.ScenarioItem;

public class AdaProjectBuilderCmds {

	private Scenario scenario;
	private GnatAdaProject project;

	public AdaProjectBuilderCmds(Scenario scenario, GnatAdaProject project) {
		this.project = project;
		this.scenario = scenario;
	}

	private String getGprFullPath() {
		return this.project.getRootPath().append(this.project.getName() + ".gpr").toOSString();
	}

	private void addExternalVariableIfAny(List<String> cmd) {
		for (ScenarioItem externalVariable : this.scenario.getExternalVariablesFor(this.project)) {
			StringBuilder builder = new StringBuilder();
			builder.append("-X");
			builder.append(externalVariable.getName());
			builder.append("=");
			builder.append(externalVariable.getValue());
			cmd.add(builder.toString());
		}
	}

	public String[] build() {
		List<String> cmd = new ArrayList<>();
		cmd.add("gprbuild");
		cmd.add("-d");
		cmd.add("-p");
		cmd.add("-P");
		cmd.add(this.getGprFullPath());
		this.addExternalVariableIfAny(cmd);

		String[] cmdAsArray = new String[cmd.size()];
		cmd.toArray(cmdAsArray);
		return cmdAsArray;
	}

	public String[] clean() {
		return new String[] { "gprclean", "-P", getGprFullPath() };
	}
}
