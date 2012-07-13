package org.padacore;

import java.util.ArrayList;
import java.util.List;


public class GprProject {

	private String       name;
	private List<String> sourcesDir = new ArrayList<String>();
	private String       objectDir  = "obj";
	private String       execDir    = "exe";
	
	public GprProject (String name) {
		assert !name.isEmpty();
		
		this.name = name;
		this.addSourceDir (".");
	}

	public void addSourceDir(String dirName) {
		this.sourcesDir.add(dirName);
	}
	
	public List<String> sourcesDir() {
		return sourcesDir;
	}

	public String objectDir() {
		return objectDir;
	}

	public void setObjectDir(String objectDir) {
		this.objectDir = objectDir;
	}

	public String execDir() {
		return execDir;
	}

	public void setExecDir(String execDir) {
		this.execDir = execDir;
	}

	public String name() {
		return name;
	}
}
