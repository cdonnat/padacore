package org.padacore.core.gnat;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

/**
 * Symbol of a context.
 * @author Charles
 *
 */
public class SymbolTable {

	private Map<String, Symbol> properties;

	/**
	 * Default constructor.
	 */
	public SymbolTable() {
		this.properties = new HashMap<String, Symbol>();
	}
	
	/**
	 * Return whether a symbol is defined.
	 * @param name Name of the symbol.
	 * @return True if the symbol is defined.
	 */
	public boolean isDefined(String name) {
		return this.properties.containsKey(name.toLowerCase());
	}

	/**
	 * Add a symbol to the symbol table.
	 * @param name Name of the symbol.
	 * @param value Symbol value.
	 */
	public void add(String name, Symbol value) {
		this.properties.put(name.toLowerCase(), value);
	}

	/**
	 * Return the symbol value associated to a symbol.
	 * @param name Name of the symbol.
	 * @return Symbol value.
	 */
	public Symbol get(String name) {
		Assert.isLegal(this.isDefined(name));
		return this.properties.get(name.toLowerCase());
	}
}
