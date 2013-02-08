package org.padacore.core.gnat;

import org.eclipse.core.runtime.Assert;

/**
 * Represents the package defined in a gpr file.
 * 
 */
public class Package implements IPropertiesProvider {

	private String name;
	private SymbolTable variables;
	private SymbolTable attributes;

	/**
	 * Constructors
	 * 
	 * @param name
	 *            Name of the package.
	 */
	public Package(String name) {
		this.name = new String(name.toLowerCase());
		this.variables = new SymbolTable();
		this.attributes= new SymbolTable();
	}

	/**
	 * @return The name of the context.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 *            Name of the variable to look for.
	 * @return True is returned if the variable is found in the context.
	 */
	@Override
	public boolean variableIsDefined(String name) {
		return this.variables.isDefined(name);
	}

	/**
	 * 
	 * @param name
	 *            Name of the attribute to look for
	 * @return True is returned if the attribute is defined in the context.
	 */
	@Override
	public boolean attributeIsDefined(String name) {
		return this.attributes.isDefined(name);
	}

	/**
	 * @pre varName is defined in the context.
	 * @param varName
	 *            Name of the variable.
	 * @return Term corresponding to the name of variable.
	 * 
	 */
	@Override
	public Symbol getVariable(String name) {
		Assert.isLegal(this.variableIsDefined(name));
		return this.variables.get(name);
	}

	/**
	 * @pre attributeName is defined in the package.
	 * @param attributeName
	 *            Name of the attribute.
	 * @return Symbol corresponding to the name of the attribute.
	 */
	@Override
	public Symbol getAttribute(String name) {
		Assert.isLegal(this.attributeIsDefined(name));
		return this.attributes.get(name);
	}

	/**
	 * Add a variable to the package.
	 * 
	 * @param varName
	 *            Name of the variable to add.
	 * @param varValue
	 *            Value of the variable to add.
	 */
	public void addVariable(String varName, Symbol varValue) {
		this.variables.add(varName, varValue);
	}

	/**
	 * Add an attribute to the package.
	 * 
	 * @param attributeName
	 *            Name of the attribute to add.
	 * @param attributeValue
	 *            Value of the attribute to add.
	 */
	public void addAttribute(String attributeName, Symbol attributeValue) {
		this.attributes.add(attributeName, attributeValue);
	}
}
