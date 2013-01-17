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

	public class Load {
		private IPath pathToGpr;
		private Context context;

		public Load(IPath path) {
			this.pathToGpr = path;
			this.context = new Context(path.removeFileExtension().lastSegment());
		}

		public IPath getPath() {
			return this.pathToGpr;
		}

		public Context getProject() {
			return this.context;
		}
	}

	private Stack<Load> projectsToLoad;
	private List<Load> loadedProjects;

	public GprLoader() {
		this.projectsToLoad = new Stack<Load>();
		this.loadedProjects = new ArrayList<Load>();
	}

	public GprLoader(IPath pathToGpr) {
		this();
		this.projectsToLoad.push(new Load(pathToGpr));
	}

	public void addVariable(String name, Symbol value) {
		this.projectsToLoad.peek().context.addVar(name, value);
	}

	public void addAttribute(String name, Symbol value) {
		this.projectsToLoad.peek().context.addAttribute(name, value);
	}

	public boolean variableIsDefined(String name) {
		return this.projectsToLoad.peek().context.variableIsDefined(name);
	}

	public boolean attributeIsDefined(String name) {
		return this.projectsToLoad.peek().context.attributeIsDefined(name);
	}

	public Symbol getVariable(String name) {
		return this.projectsToLoad.peek().context.getVariable(name);
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
		this.parseGpr(this.projectBeingParsed().getPath());
		Load lastParsedProject = this.projectsToLoad.pop();
		if (!this.isLoadAlreadyAdded(lastParsedProject)) {
			this.loadedProjects.add(0, lastParsedProject);
		}
	}

	public void addProject(String relativeProjectPath) {
		IPath referencePath = this.projectsToLoad.peek().pathToGpr.removeLastSegments(1);
		IPath path = referencePath.append(relativeProjectPath);
		String extension = path.getFileExtension();

		if (extension == null) {
			path = path.addFileExtension(GPR_EXTENSION);
		}

		Load projectToLoad = new Load(path);
		this.projectBeingParsed().context.addReference(projectToLoad.context);
		this.projectsToLoad.push(projectToLoad);
		this.load();
	}
	
	public List<Load> getLoadedProject() {
		return this.loadedProjects;
	}

	private Load projectBeingParsed() {
		return this.projectsToLoad.peek();
	}
	
	private boolean isLoadAlreadyAdded(Load load) {
		boolean isAdded = false;
		for (Load tmp : this.loadedProjects) {
			if (tmp.getPath().equals(load.getPath())) {
				isAdded = true;
				break;
			}
		}
		return isAdded;
	}
}
