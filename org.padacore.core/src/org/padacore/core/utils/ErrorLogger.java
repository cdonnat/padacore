package org.padacore.core.utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.statushandlers.*;
import org.padacore.core.Activator;

public class ErrorLogger {

	/**
	 * Appends the given CoreException to the error log.
	 * 
	 * @param e
	 *            the CoreException to log.
	 */
	public static void appendExceptionToErrorLog(CoreException e) {
		StatusManager.getManager().handle(e, Activator.PLUGIN_ID);
	}

	/**
	 * Appends the given message to the error log.
	 * 
	 * @param message
	 *            the message to log.
	 * @param severity
	 *            severity of the message, one of IStatus.INFO, IStatus.ERROR,
	 *            IStatus.WARNING, IStatus.CANCEL.
	 */
	public static void appendMessageToErrorLog(String message, int severity) {
		Assert.isLegal(severity == IStatus.INFO || severity == IStatus.ERROR
				|| severity == IStatus.WARNING || severity == IStatus.CANCEL);

		IStatus status = new Status(severity, Activator.PLUGIN_ID, message);

		StatusManager.getManager().handle(status);
	}

}
