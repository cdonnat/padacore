package org.padacore.core.test.stubs;

import java.util.ArrayList;
import java.util.List;

import org.padacore.core.gnat.Context;
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
	public void load() {
	}

	@Override
	public void addProject(String relativeProjectPath) {
	}

	@Override
	public List<Load> getLoadedProjects() {
		return new ArrayList<GprLoader.Load>();
	}

	@Override
	public Context getContextByName(String qualifiedContextName) {
		return new Context("toto");
	}

	@Override
	public Context getCurrentContext() {
		return new Context("toto");
	}
}
