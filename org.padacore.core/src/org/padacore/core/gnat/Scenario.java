package org.padacore.core.gnat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.gpr4j.api.ExternalVariable;

import com.google.common.base.Preconditions;

/**
 * 
 * Stores the value of the external variables defined for every projects.
 * 
 */
public class Scenario {

	private Map<ExternalVariable, String> values;

	/**
	 * Default constructor.
	 */
	public Scenario() {
		this.values = new HashMap<>();
	}

	/**
	 * Set an external variable value for a project. If the external variable is
	 * shared between two projects, this change will affect both project.
	 * 
	 * @pre The external variable is defined in the project.
	 * @param project
	 *            Project containing the external variable.
	 * @param variableName
	 *            Name of the variable to change.
	 * @param value
	 *            Value to set.
	 */
	public void setExternalVariableValueFor(GnatAdaProject project, String variableName,
			String value) {
		Preconditions
				.checkArgument(this.getExternalVariableFromName(project, variableName) != null);
		this.values.put(this.getExternalVariableFromName(project, variableName), value);
	}

	/**
	 * Return an ordered set of external variable sort by name. External
	 * variable from projects referenced by given project are are also returned.
	 * 
	 * @param project
	 *            Project containing external variable.
	 * @return An ordered set of external variable sort by name.
	 */
	public Set<ScenarioItem> getExternalVariablesFor(GnatAdaProject project) {
		Set<ScenarioItem> res = new TreeSet<>();
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

	/**
	 * 
	 * @param project
	 *            Project containing the external variable.
	 * @param variableName
	 *            Name of the external variable to look for.
	 * @return The external variable corresponding to given name or null if not
	 *         found.
	 */
	private ExternalVariable getExternalVariableFromName(GnatAdaProject project, String variableName) {
		ExternalVariable res = null;
		for (ExternalVariable var : project.getExternalVariables()) {
			if (var.getName() == variableName) {
				res = var;
				break;
			}
		}
		return res;
	}

}
