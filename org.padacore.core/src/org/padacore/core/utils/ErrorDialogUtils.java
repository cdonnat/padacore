package org.padacore.core.utils;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.padacore.core.Activator;

/**
 * This class provides utilities methods for managing error dialogs presented to
 * user.
 * 
 * @author RS
 * 
 */
public class ErrorDialogUtils {

	public static void DisplayErrorToUser(final String windowTitle,
			final String message) {
		final IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
				message);

		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}

		display.asyncExec(new Runnable() {
			public void run() {
				ErrorDialog.openError(null, windowTitle, null, status);
			}
		});
	}
}
