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
	 * Returns the error header from a message which contains the error type and
	 * may contain an error header.
	 * 
	 * @param errorTypeWithErrorHeader
	 *            a message which contains the error type and may contain an
	 *            error header.
	 * @return error header if there is such one or null otherwise.
	 */
	private String getErrorHeader(String errorTypeWithErrorHeader) {
		String[] splitErrorTypeWithErrorHeader = errorTypeWithErrorHeader
				.split(":");
		String errorHeader = null;

		if (splitErrorTypeWithErrorHeader.length == 2) {
			errorHeader = splitErrorTypeWithErrorHeader[1];
		}

		return errorHeader;
	}

	/**
	 * Precondition : lastEntryIndicatesError() Postcondition: returned error is
	 * not null
	 * 
	 * @return Error containing information about the current error.
	 */
	public Error error() {
		int severity = Error.SEVERITY_ERROR;
		StringBuilder message = new StringBuilder();
		String errorTypeWithErrorHeader = errorMatcher.group(4);
		String errorHeader;

		if (errorTypeWithErrorHeader != null) {
			severity = errorTypeWithErrorHeader.contains("warning") ? Error.SEVERITY_WARNING
					: Error.SEVERITY_ERROR;

			errorHeader = this.getErrorHeader(errorTypeWithErrorHeader);
			if (errorHeader != null) {
				message.append(errorHeader.trim());
				message.append(": ");
			}
		}

		message.append(errorMatcher.group(5));

		return new Error(errorMatcher.group(1), Integer.parseInt(errorMatcher
				.group(2)), Integer.parseInt(errorMatcher.group(3)), severity,
				message.toString());
	}
}