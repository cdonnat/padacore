package org.padacore.core.gnat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class GprLoader {

	private static String GPR_EXTENSION = "gpr";

	private Stack<Project> projectsToLoad;
	private List<Project> loadedProjects;

	public GprLoader() {
		this.projectsToLoad = new Stack<Project>();
		this.loadedProjects = new ArrayList<Project>();
		this.projectsToLoad.push(new Project(new Path("FIXME"))); //for test purpose
	}

	public void addVariable(String name, Symbol value) {
		this.projectsToLoad.peek().addVariable(name, value);
	}

	public void addAttribute(String name, Symbol value) {
		this.projectsToLoad.peek().addAttribute(name, value);
	}

	public boolean variableIsDefined(String name) {
		return this.projectsToLoad.peek().variableIsDefined(name);
	}

	public boolean attributeIsDefined(String name) {
		return this.projectsToLoad.peek().attributeIsDefined(name);
	}

	public Symbol getVariable(String name) {
		return this.projectsToLoad.peek().getVariable(name);
	}

	public Symbol getAttribute(String name) {
		return this.projectsToLoad.peek().getAttribute(name);
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

	private IPath evaluatePath(String relativeProjectPath) {
		IPath referencePath = this.projectsToLoad.peek().getPath().removeLastSegments(1);
		IPath path = referencePath.append(relativeProjectPath);
		String extension = path.getFileExtension();

		if (extension == null) {
			path = path.addFileExtension(GPR_EXTENSION);
		}
		return path;
	}

	public void load(IPath pathToGpr) {
		Project projectToAdd = new Project(pathToGpr);
		this.projectsToLoad.push(projectToAdd);
		this.parseGpr(this.getCurrentProjectInProgress().getPath());
		this.projectsToLoad.pop();
		this.loadedProjects.add(0, projectToAdd);
		if (!this.projectsToLoad.isEmpty()) {
			this.getCurrentProjectInProgress().addReferenceProject(projectToAdd);
		}
	}

	public void addProject(String relativeProjectPath) {
		IPath projectToAddPath = evaluatePath(relativeProjectPath);

		if (this.projectIsAlreadyLoaded(projectToAddPath)) {
			Project projectToAdd = this.getProject(projectToAddPath);
			this.getCurrentProjectInProgress().addReferenceProject(projectToAdd);
		} else {
			this.load(projectToAddPath);
		}
	}

	public List<Project> getLoadedProjects() {
		return this.loadedProjects;
	}

	public void beginPackage (String packageName) {
		this.getCurrentProjectInProgress().beginPackage(packageName);
	}
	
	public void endPackage() {
		this.getCurrentProjectInProgress().endPackage();
	}
	
	public Project getCurrentProjectInProgress() {
		return this.projectsToLoad.peek();
	}

	private Project getProject(IPath pathToGpr) {
		Project project = null;
		for (Project loadedProject : this.loadedProjects) {
			if (loadedProject.getPath().equals(pathToGpr)) {
				project = loadedProject;
				break;
			}
		}
		return project;
	}

	private boolean projectIsAlreadyLoaded(IPath pathToGpr) {
		return getProject(pathToGpr) != null;
	}
}
