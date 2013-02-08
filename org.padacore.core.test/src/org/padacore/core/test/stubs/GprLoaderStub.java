package org.padacore.core.test.stubs;

import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.Symbol;

public class GprLoaderStub extends GprLoader {

	@Override
	public void addVariable(String name, Symbol value) {
	}

	@Override
	public void addAttribute(String name, Symbol value) {
	}

	@Override
	public boolean variableIsDefined(String name) {
		return true;
	}

	@Override
	public boolean attributeIsDefined(String name) {
		return true;
	}

	@Override
	public Symbol getVariable(String name) {
		return Symbol.CreateString("");
	}

	@Override
	public Symbol getAttribute(String name) {
		return Symbol.CreateString("");
	}

	@Override
	public void addProject(String relativeProjectPath) {
	}
}

