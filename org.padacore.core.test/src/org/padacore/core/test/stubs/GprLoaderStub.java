package org.padacore.core.test.stubs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.Project;
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
	public void load(IPath pathToGpr) {

	}

	@Override
	public void addProject(String relativeProjectPath) {
	}

	@Override
	public List<Project> getLoadedProjects() {
		return new ArrayList<Project>();
	}

	@Override
	public void beginPackage(String packageName) {

	}

	@Override
	public void endPackage() {
	}

	@Override
	public Project getCurrentProjectInProgress() {
		return new Project(new Path("toto"));
	}
}
