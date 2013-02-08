package org.padacore.core.gnat;

public interface IPropertiesProvider {

	public abstract String getName();
	
	public abstract boolean variableIsDefined(String name);

	public abstract boolean attributeIsDefined(String name);

	public abstract Symbol getVariable(String name);

	public abstract Symbol getAttribute(String name);
}
