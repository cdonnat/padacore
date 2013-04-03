package org.padacore.ui.launch;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * This class enables to create an executable selection dialog to choose among
 * different executables.
 * 
 * @author RS
 * 
 */
public class ExecutableSelectionDialogFactory {

	/**
	 * Creates a dialog which enables the user to select one of the executables
	 * of the project.
	 * 
	 * @param executableNames
	 *            the names of the executables of the project.
	 * @return a ListDialog which enables the user to select one of the
	 *         executables of the project.
	 */
	public ListDialog createExecutableSelectionDialogFor(
			List<String> executableNames) {

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		return new ExecutableSelectionDialog(shell, executableNames);
	}

}
