package org.padacore.core.gnat;

import org.gpr4j.api.ExternalVariable;

public class ScenarioItem {

	private ExternalVariable var;
	private String value;

	public ScenarioItem(ExternalVariable var, String value) {
		this.var = var;
		this.value = value;
	}

	public String getName() {
		return this.var.getName();
	}

	public String getValue() {
		return this.value;
	}
}
