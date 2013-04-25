package org.padacore.core.gnat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gpr4j.api.ExternalVariable;

import com.google.common.base.Preconditions;

public class Scenario {

	private Map<ExternalVariable, String> values;

	public Scenario() {
		this.values = new HashMap<>();
	}

	public void setExternalVariableValueFor(GnatAdaProject project, String variableName,
			String value) {
		Preconditions
				.checkArgument(this.getExternalVariableFromName(project, variableName) != null);
		this.values.put(this.getExternalVariableFromName(project, variableName), value);
	}

	public Set<ScenarioItem> getExternalVariablesFor(GnatAdaProject project) {
		Set<ScenarioItem> res = new HashSet<>();
		for (ExternalVariable var : project.getExternalVariables()) {
			String value = null;
			if (this.values.keySet().contains(var)) {
				value = this.values.get(var);
			} else {
				value = var.getDefaultValue();
			}
			res.add(new ScenarioItem(var, value));
		}
		return res;
	}

	private ExternalVariable getExternalVariableFromName(GnatAdaProject project, String variableName) {
		ExternalVariable res = null;
		for (ExternalVariable var : project.getExternalVariables()) {
			if (var.getName() == variableName) {
				res = var;
			}
		}
		return res;
	}

}
