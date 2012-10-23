package org.padacore.core.gnat.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.padacore.core.GprProject;

public class GprBuilder {

	private String projectName;
	private ArrayList<String> referencedProjects = new ArrayList<String>();
	private Map<String, ArrayList<String>> simpleAttributes = new HashMap<String, ArrayList<String>>();
	private Map<String, String> vars = new HashMap<String, String>();

	private final static String EXECUTABLE_DIRECTORY_ATTRIBUTE = "Exec_Dir";
	private final static String OBJECT_DIRECTORY_ATTRIBUTE = "Object_Dir";
	private final static String MAIN_ATTRIBUTE = "Main";
	private final static String SOURCE_DIRECTORIES_ATTRIBUTE = "Source_Dirs";

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void addReferencedProject(String referencedProject) {
		referencedProjects.add(referencedProject);
	}

	private ArrayList<String> removeQuotes(ArrayList<String> input) {
		ArrayList<String> res = new ArrayList<String>(input.size());
		for (String element : input) {
			if (element.startsWith("\"") && element.endsWith("\"")) {
				res.add(element.substring(1, element.length() - 1));
			} else {
				res.add(element);
			}
		}
		return res;
	}

	public void addSimpleAttribute(String attribute, ArrayList<String> attributeValue) {
		simpleAttributes.put(attribute, removeQuotes(attributeValue));
	}

	public boolean variableIsDefined(String varName) {
		return vars.containsKey(varName);
	}

	public void addVar(String varName, String varValue) {
		Assert.isLegal(!variableIsDefined(varName));
		vars.put(varName, varValue);
	}

	public GprProject build() {
		GprProject res = new GprProject(projectName);
		addReferencedProject(res);
		addSourceDir(res);
		addExecDir(res);
		addObjectDir(res);
		addExecutable(res);
		return res;
	}

	private void addExecutable(GprProject gprProject) {
		if (simpleAttributes.containsKey(MAIN_ATTRIBUTE)) {
			for (String execName : simpleAttributes.get(MAIN_ATTRIBUTE)) {
				gprProject.addExecutableName(execName);
			}
		}
	}

	private void addObjectDir(GprProject gprProject) {
		if (simpleAttributes.containsKey(OBJECT_DIRECTORY_ATTRIBUTE)) {
			gprProject.setObjectDir(simpleAttributes.get(OBJECT_DIRECTORY_ATTRIBUTE).get(0));
		}
	}

	private void addExecDir(GprProject gprProject) {
		if (simpleAttributes.containsKey(EXECUTABLE_DIRECTORY_ATTRIBUTE)) {
			gprProject.setExecutable(true);
			gprProject
					.setExecutableDir(simpleAttributes.get(EXECUTABLE_DIRECTORY_ATTRIBUTE).get(0));
		}
	}

	private void addSourceDir(GprProject gprProject) {
		if (simpleAttributes.containsKey(SOURCE_DIRECTORIES_ATTRIBUTE)) {
			for (String sourceDir : simpleAttributes.get(SOURCE_DIRECTORIES_ATTRIBUTE)) {
				gprProject.addSourceDir(sourceDir);
			}
		}
	}

	private void addReferencedProject(GprProject gprProject) {
		for (String referencedProject : referencedProjects) {
			gprProject.addWithedProject(referencedProject);
		}
	}

}
