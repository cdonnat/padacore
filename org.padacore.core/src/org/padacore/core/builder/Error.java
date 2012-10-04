package org.padacore.core.builder;

public class Error {

	private String file;
	private int line;
	private int column;
	private String message;

	public Error(String file, int line, int column, String message) {
		this.file = file;
		this.line = line;
		this.column = column;
		this.message = message;
	}

	public String file() {
		return file;
	}

	public int line() {
		return line;
	}

	public int column() {
		return column;
	}

	public String message() {
		return message;
	}
}
