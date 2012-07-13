package org.padacore;

import java.util.ArrayList;
import java.util.List;


public class GprProject {

	private String name;
	private List<String> sourcesDir;
	private String objectDir;
	private String execDir;
	
	public GprProject (String name) {
		this.name = name;
		this.sourcesDir = new ArrayList<String>();
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
