package org.padacore;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	
	public void save(OutputStream out) {
		DataOutputStream data = new DataOutputStream(out);

		try {
			data.writeBytes("project " + name() + " is\n");
			data.writeBytes("\tfor Source_Dirs use (");

			for (int i = 0; i < sourcesDir().size() - 1; i++) {
				data.writeBytes("\"" + sourcesDir().get(i) + "\",\n");
			}
			data.writeBytes("\""
					+ sourcesDir().get(sourcesDir().size() - 1)
					+ "\");\n");

			data.writeBytes("\tfor Object_Dir use \"" + objectDir() + "\";\n");
			data.writeBytes("\tfor Exec_Dir use \"" + execDir() + "\";\n");
			data.writeBytes("end " + name() + ";");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
