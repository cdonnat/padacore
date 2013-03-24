package org.padacore.ui.launch;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * This class defines a dialog that enables the user to choose which executable
 * to launch for a given project.
 * 
 * @author RS
 * 
 */
public class ExecutableSelectionDialog extends ListDialog {

	private class ExecutablesLabelProvider extends LabelProvider {

		private final static String EXECUTABLE_FILE_IMAGE = "../../../../../icons/exec_dbg_obj.gif";
		private Image execImage;

		public ExecutablesLabelProvider() {
			super();
			this.execImage = new Image(Display.getCurrent(), this.getClass()
					.getResourceAsStream((EXECUTABLE_FILE_IMAGE)));
		}

		@Override
		public Image getImage(Object element) {
			return this.execImage;
		}

		@Override
		public void dispose() {
			super.dispose();
			this.execImage.dispose();
			this.execImage = null;
		}
	}

	public ExecutableSelectionDialog(Shell parent, List<String> executableNames) {
		super(parent);
		this.setContentProvider(new ArrayContentProvider());
		this.setLabelProvider(new ExecutablesLabelProvider());
		this.setInput(executableNames);
		this.setTitle("Executable selection");
		this.setHelpAvailable(false);
		this.setMessage("Please select the executable to launch.");
	}
}
