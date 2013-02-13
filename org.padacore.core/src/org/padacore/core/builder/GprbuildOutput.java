package org.padacore.core.builder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GprbuildOutput {

	private static final String PROGRESS_PATTERN_STRING = "completed (\\d+) out of (\\d+)";
	private static final String ERROR_PATTERN_STRING = "^(.+):(\\d+):(\\d+):(.*:)?\\s+(.*)$";

	private Pattern progressPattern;
	private Pattern errorPattern;
	private Matcher progressMatcher;
	private Matcher errorMatcher;

	/**
	 * Default constructor.
	 */
	public GprbuildOutput() {
		progressPattern = Pattern.compile(PROGRESS_PATTERN_STRING);
		errorPattern = Pattern.compile(ERROR_PATTERN_STRING);
	}

	/**
	 * Request to evaluate a line. lastEntryIndicatesProgress() and total()
	 * provides informations about the evaluated line.
	 * 
	 * @param line
	 */
	public void evaluate(String line) {
		progressMatcher = progressPattern.matcher(line);
		errorMatcher = errorPattern.matcher(line);
	}

	/**
	 * Indicates whether the evaluated line is a line indicating progress or
	 * not.
	 * 
	 * @return True is returned if the evaluated line match ('completed xx out
	 *         of xx ...')
	 */
	public boolean lastEntryIndicatesProgress() {
		return progressMatcher.find();
	}

	/**
	 * Precondition : lastEntryIndicatesProgress() Provides the remaining
	 * percentage to process.
	 * 
	 * @return Remaining percentage of file to process is returned.
	 */
	public int nbRemainingFilesToProcess() {
		return Integer.parseInt(progressMatcher.group(2))
				- Integer.parseInt(progressMatcher.group(1));
	}

	/**
	 * Indicates whether the evaluated line is a line indicating a build error.
	 * 
	 * @return True is returned if the evaluated line match error pattern.
	 */
	public boolean lastEntryIndicatesError() {
		return errorMatcher.find();
	}

	/**
	 * Precondition : lastEntryIndicatesError() Postcondition: returned error is
	 * not null
	 * 
	 * @return Error containing information about the current error.
	 */
	public Error error() {
		int severity = Error.SEVERITY_ERROR;

		if (errorMatcher.group(4) != null) {
			severity = errorMatcher.group(4).contains("warning") ? Error.SEVERITY_WARNING
					: Error.SEVERITY_ERROR;
		}

		return new Error(errorMatcher.group(1), Integer.parseInt(errorMatcher.group(2)),
				Integer.parseInt(errorMatcher.group(3)), severity, errorMatcher.group(5));
	}
}