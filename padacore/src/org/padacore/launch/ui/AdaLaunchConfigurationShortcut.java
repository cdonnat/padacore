package org.padacore.launch.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

public class AdaLaunchConfigurationShortcut implements ILaunchShortcut {
	
	private boolean isOnlyOneElementSelected(IStructuredSelection selection) {
		return selection.size() == 1;
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			
			if(isOnlyOneElementSelected(structuredSelection)) {
				Object selectedElt = structuredSelection.getFirstElement();
				if (selectedElt instanceof IFile) {
					IFile selectedFile = (IFile)selectedElt;
					System.out.println("Selected file is: " + selectedFile.getFullPath());
				}
			}
		}

	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub

	}

}
