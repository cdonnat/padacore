package org.padacore.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.padacore.core.Activator;

public class ErrorLog {

	/**
	 * Appends the given CoreException to the error log.
	 * 
	 * @param e
	 *            the CoreException to log.
	 */
	public static void appendException(CoreException e) {
		StatusManager.getManager().handle(e, Activator.PLUGIN_ID);
	}

	/**
	 * Appends the given Exception to the error log.
	 * 
	 * @param e
	 *            the Exception to log
	 * @param severity
	 *            severity of the message, one of IStatus.ERROR,
	 *            IStatus.WARNING, IStatus.CANCEL.
	 */
	public static void appendException(Exception e, int severity) {
		Assert.isLegal(severity == IStatus.ERROR || severity == IStatus.WARNING
				|| severity == IStatus.CANCEL);

		StringBuilder exceptionMsgBuilder = new StringBuilder(e.getMessage());

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);

		String stackTrace = stringWriter.toString();
		exceptionMsgBuilder.append(stackTrace);

		IStatus status = new Status(severity, Activator.PLUGIN_ID,
				exceptionMsgBuilder.toString());

		StatusManager.getManager().handle(status);
	}

	/**
	 * Appends the given message to the error log.
	 * 
	 * @param message
	 *            the message to log
	 * @param severity
	 *            severity of the message (cf. IStatus fields)
	 */
	public static void appendMessage(String message, int severity) {
		Assert.isLegal(severity == IStatus.ERROR || severity == IStatus.WARNING
				|| severity == IStatus.CANCEL || severity == IStatus.OK
				|| severity == IStatus.INFO);

		IStatus status = new Status(severity, Activator.PLUGIN_ID, message);
		StatusManager.getManager().handle(status);
	}

}
