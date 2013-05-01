package org.padacore.core.gnat;

import org.gpr4j.api.ExternalVariable;
import org.gpr4j.api.Type;

import com.google.common.base.Preconditions;

public class ScenarioItem implements Comparable<ScenarioItem> {

	private ExternalVariable var;
	private String value;

	public ScenarioItem(ExternalVariable var, String value) {
		this.var = var;
		this.value = value;
	}

	public String getName() {
		return this.var.getName();
	}

	public boolean isTyped() {
		return this.var.isTyped();
	}

	public Type getType() {
		Preconditions.checkState(this.isTyped());
		return this.var.getType();
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public int compareTo(ScenarioItem o) {
		return this.getName().compareTo(o.getName());
	}
}
