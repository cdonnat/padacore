package org.padacore.core.builder;

public class Error {

	private String file;
	private int line;
	private int column;
	private String message;
	private int severity;

	public static final int SEVERITY_ERROR = 2;
	public static final int SEVERITY_WARNING = 1;

	public Error(String file, int line, int column, int severity, String message) {
		this.file = file;
		this.line = line;
		this.column = column;
		this.severity = severity;
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

	public int severity() {
		return severity;
	}
}
