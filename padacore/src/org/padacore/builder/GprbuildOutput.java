package org.padacore.builder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GprbuildOutput {

	private static final String patternString = "completed [0-9]+ out of ([0-9]+)";

	private Pattern pattern;
	private Matcher matcher;
	private boolean lastEntryIndicatesProgress;

	/**
	 * Default constructor.
	 */
	public GprbuildOutput() {
		pattern = Pattern.compile(patternString);
		lastEntryIndicatesProgress = false;
	}

	/**
	 * Request to evaluate a line. lastEntryIndicatesProgress() and total() provides
	 * informations about the evaluated line.
	 * @param line
	 */
	public void evaluate(String line) {
		matcher = pattern.matcher(line);
		lastEntryIndicatesProgress = matcher.find();
	}

	/**
	 * Indicates whether the evaluated line is a line indicating progress or not.
	 * @return True is returned if the evaluated line match ('completed xx out of xx ...')
	 */
	public boolean lastEntryIndicatesProgress() {
		return lastEntryIndicatesProgress;
	}

	/**
	 * Precondition : lastEntryIndicatesProgress()
	 * Provides the total number of to process. 
	 * @return Total number of file to process is returned.
	 */
	public int total() {
		return Integer.parseInt(matcher.group(1));
	}
}