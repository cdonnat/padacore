package org.padacore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessDisplay {

	static public void DisplayErrors(Process process) throws IOException {
		BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String errorLine;

		while ((errorLine = error.readLine()) != null) {
			System.err.println(errorLine);
		}
	}

	static public void DisplayWarnings(Process process) throws IOException {
		BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String outputLine;

		while ((outputLine = output.readLine()) != null) {
			System.out.println(outputLine);
		}
	}
}
