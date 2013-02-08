package org.padacore.core.gnat;

public interface ISymbolProvider {

	public boolean isDefined(String name);

	public Symbol get(String name);

}
