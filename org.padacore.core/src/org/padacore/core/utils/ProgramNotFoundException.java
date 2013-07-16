package org.padacore.core.utils;

import java.io.IOException;

public class ProgramNotFoundException extends Exception {

	private static final long serialVersionUID = 942802465502510604L;

	public ProgramNotFoundException(String programName, IOException e) {
		super(programName, e);
	}

}
