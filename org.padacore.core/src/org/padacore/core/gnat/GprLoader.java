package org.padacore.core.gnat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class GprLoader {

	private IPath referencePath;
	private Context current;
	private List<Context> projects;

	public GprLoader() {
		this.referencePath = new Path(".");
		this.current = new Context("");
		this.projects = new ArrayList<Context>();
	}
	
	public GprLoader(IPath pathToGpr) {
		this.referencePath = pathToGpr.removeLastSegments(1);
		this.current = new Context(pathToGpr.removeFileExtension().lastSegment());
		this.projects = new ArrayList<Context>();
		this.projects.add(this.current);
	}

	public void addVariable(String name, Symbol value) {
		this.current.addVar(name, value);
	}

	public void addAttribute(String name, Symbol value) {
		this.current.addAttribute(name, value);
	}

	public boolean variableIsDefined(String name) {
		return this.current.variableIsDefined(name);
	}

	public boolean attributeIsDefined(String name) {
		return this.current.attributeIsDefined(name);
	}

	public Symbol getVariable(String name) {
		return this.current.getVariable(name);
	}

	private void parseGpr(IPath path) {
		try {
			GprLexer lexer = new GprLexer(new ANTLRFileStream(path.toOSString()));
			GprParser parser = new GprParser(this, new CommonTokenStream(lexer));
			parser.project();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		this.parseGpr(new Path(this.referencePath.toOSString() + "/" + this.current.getName()
				+ ".gpr"));
	}

	public void addProject(String relativeProjectPath) {
		String projectName = new Path (relativeProjectPath).removeFileExtension().lastSegment();
		Context newContext = new Context(projectName);
		this.current.addReference(newContext);
		this.projects.add(newContext);

		Context previousContext = this.current;

		this.current = newContext;
		this.load();
		this.current = previousContext;
	}

	public List<Context> getLoadedProject() {
		return this.projects;
	}
}
