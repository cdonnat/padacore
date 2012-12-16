package org.padacore.core.gnat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;

public class GprLoader {

	private static String GPR_EXTENSION = "gpr";
	
	private class Loadable {
		private IPath path;
		private Context context;

		public Loadable(IPath path) {
			this.path = path;
			this.context = new Context(path.removeFileExtension().lastSegment());
		}
	}

	private Stack<Loadable> toLoad;
	private List<Context> loadedProjects;

	public GprLoader() {
		this.toLoad = new Stack<Loadable>();
		this.loadedProjects = new ArrayList<Context>();
	}

	public GprLoader(IPath pathToGpr) {
		this();
		this.toLoad.push(new Loadable(pathToGpr));
	}

	public void addVariable(String name, Symbol value) {
		this.toLoad.peek().context.addVar(name, value);
	}

	public void addAttribute(String name, Symbol value) {
		this.toLoad.peek().context.addAttribute(name, value);
	}

	public boolean variableIsDefined(String name) {
		return this.toLoad.peek().context.variableIsDefined(name);
	}

	public boolean attributeIsDefined(String name) {
		return this.toLoad.peek().context.attributeIsDefined(name);
	}

	public Symbol getVariable(String name) {
		return this.toLoad.peek().context.getVariable(name);
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
		this.parseGpr(this.toLoad.peek().path);
		this.loadedProjects.add(0, this.toLoad.pop().context);
	}

	public void addProject(String relativeProjectPath) {
		IPath referencePath = this.toLoad.peek().path.removeLastSegments(1);
		IPath path = referencePath.append(relativeProjectPath);
		String extension = path.getFileExtension();

		if (extension == null) {
			path = path.addFileExtension(GPR_EXTENSION);
		}

		Loadable projectToLoad = new Loadable(path);
		this.toLoad.peek().context.addReference(projectToLoad.context);
		this.toLoad.push(projectToLoad);
		this.load();
	}

	public List<Context> getLoadedProject() {
		return this.loadedProjects;
	}
}
