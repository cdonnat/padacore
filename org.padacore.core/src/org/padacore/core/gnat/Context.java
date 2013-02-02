package org.padacore.core.gnat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

/**
 * Represents all the symbols (variables and attributes).
 * 
 * @author Charles
 * 
 */
public class Context {

	private String name;
	private SymbolTable symbols;
	private Map<String, Context> references;

	private static final String VarType = "___var___";
	private static final String AttributeType = "___att___";

	/**
	 * Constructors
	 * 
	 * @param name
	 *            Name of the context.
	 */
	public Context(String name) {
		this.name = new String(name.toLowerCase());
		this.symbols = new SymbolTable();
		this.references = new HashMap<String, Context>();
	}

	/**
	 * @return The name of the context.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param varName
	 *            Name of the variable to look for.
	 * @return True is returned if the variable is found in the context.
	 */
	public boolean variableIsDefined(String varName) {
		return this.isDefined(varName, VarType);
	}

	/**
	 * 
	 * @param attributeName
	 *            Name of the attribute to look for
	 * @return True is returned if the attribute is defined in the context.
	 */
	public boolean attributeIsDefined(String attributeName) {
		return this.isDefined(FormatAttributeName(attributeName), AttributeType);
	}

	/**
	 * @pre varName is defined in the context.
	 * @param varName
	 *            Name of the variable.
	 * @return Term corresponding to the name of variable.
	 * 
	 */
	public Symbol getVariable(String varName) {
		Assert.isLegal(this.variableIsDefined(varName));
		return this.get(varName, VarType);
	}

	/**
	 * @pre attributeName is defined in the context.
	 * @param attributeName
	 *            Name of the attribute.
	 * @return Term corresponding to the name of the attribute.
	 */
	public Symbol getAttribute(String attributeName) {
		Assert.isLegal(this.attributeIsDefined(attributeName));
		return this.get(FormatAttributeName(attributeName), AttributeType);
	}

	/**
	 * Add a variable to the context.
	 * 
	 * @param varName
	 *            Name of the variable to add.
	 * @param varValue
	 *            Value of the variable to add.
	 */
	public void addVar(String varName, Symbol varValue) {
		this.add(varName, VarType, varValue);
	}

	/**
	 * Add a symbol to the context.
	 * 
	 * @param name
	 *            Name of the symbol to add.
	 * @param type
	 *            Type of the symbol to add.
	 * @param value
	 *            Value of the symbol to add.
	 */
	private void add(String name, String type, Symbol value) {
		this.symbols.add(type + name, value);
	}

	/**
	 * Add an attribute to the context
	 * 
	 * @param attributeName
	 *            Name of the attribute to add.
	 * @param attributeValue
	 *            Value of the attribute to add.
	 */
	public void addAttribute(String attributeName, Symbol attributeValue) {
		this.add(FormatAttributeName(attributeName), AttributeType, attributeValue);
	}

	/**
	 * Add a reference to another context. The current context will have the
	 * visibility on the symbols defined in the referenced context.
	 * 
	 * @param reference
	 *            Reference context to add.
	 */
	public void addReference(Context reference) {
		this.references.put(reference.getName(), reference);
	}

	/**
	 * Return the references context.
	 * @return The references context.
	 */
	public List<Context> getReferences() {
		return new ArrayList<Context>(this.references.values());
	}
	
	/**
	 * Return the referenced context corresponding to given name.
	 * @return the referenced context
	 */
	public Context getReferencedContextByName(String ctxtName) {
		return this.references.get(ctxtName);
	}

	/**
	 * Join the string element of an array
	 * 
	 * @param tab
	 *            Array containing the element to join
	 * @param from
	 *            Starting index in the array
	 * @param size
	 *            Number of element to join
	 * @pre from + size - 1 <= tab.length
	 * @return A string containing the joined element.
	 */
	private static String Join(String[] tab, int from, int size) {
		Assert.isLegal(from + size - 1 <= tab.length);
		StringBuilder res = new StringBuilder();

		for (int i = from; i < from + size; i++) {
			res.append(tab[i]);
		}
		return res.toString();
	}

	/**
	 * Return the prefix of the given name.
	 * 
	 * @param fullName
	 *            Full name.
	 * @return Prefix of the fullName or fullName is no prefix is found.
	 */
	private static String GetPrefix(String fullName) {
		String[] fullNameAsList = fullName.split("\\.", 2);
		return fullNameAsList[0].toLowerCase();
	}

	/**
	 * Return the fullName without the prefix.
	 * 
	 * @param fullName
	 *            Full name.
	 * @pre There is a prefix.
	 * @return The full name without the prefix.
	 */
	private static String GetNameWithoutPrefix(String fullName) {
		Assert.isLegal(!GetPrefix(fullName).isEmpty());
		String[] fullNameAsList = fullName.split("\\.", 2);
		return Join(fullNameAsList, 1, fullNameAsList.length - 1);
	}

	/**
	 * Return whether the symbol is defined in the context or not.
	 * 
	 * @param name
	 *            Name of the symbol.
	 * @param type
	 *            Type of the symbol.
	 * @return True if the symbol is defined.
	 */
	private boolean isDefined(String name, String type) {
		boolean isDefined = this.symbols.isDefined(type + name);

		if (!isDefined && references.containsKey(GetPrefix(name))) {
			isDefined = references.get(GetPrefix(name)).isDefined(GetNameWithoutPrefix(name), type);
		}
		return isDefined;
	}

	/**
	 * Return the value of a symbol.
	 * 
	 * @param name
	 *            Name of the symbol.
	 * @param type
	 *            Type of the symbol.
	 * @pre The symbol is defined in the context.
	 * @return The term corresponding to the symbol name.
	 */
	private Symbol get(String name, String type) {
		Assert.isLegal(this.isDefined(name, type));
		Symbol res = null;
		if (this.symbols.isDefined(type + name)) {
			res = this.symbols.get(type + name);
		} else {
			res = this.references.get(GetPrefix(name)).get(GetNameWithoutPrefix(name), type);
		}
		return res;
	}

	/**
	 * Remove the ' in the attribute name
	 * 
	 * @param attributeName
	 *            Attribute name.
	 * @return Attribute name without '.
	 */
	private static String FormatAttributeName(String attributeName) {
		return attributeName.replace('\'', '.');
	}
}
