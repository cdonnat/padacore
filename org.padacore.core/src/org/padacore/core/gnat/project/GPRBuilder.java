package org.padacore.core.gnat.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.padacore.core.GprProject;

public class GPRBuilder {

	private String projectName;

	private ArrayList<String> referencedProjects = new ArrayList<String>();
	private Map<String, ArrayList<String>> simpleAttributes = new HashMap<String, ArrayList<String>>();

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void addReferencedProject(String referencedProject) {
		referencedProjects.add(referencedProject);
	}

	public void addSimpleAttribute(String attribute, ArrayList<String> attributeValue) {
		simpleAttributes.put(attribute, attributeValue);
	}

	public GprProject build() {
		GprProject res = new GprProject(projectName);

		for (String referencedProject : referencedProjects) {
			res.addWithedProject(referencedProject);
		}

		return res;
	}
}
