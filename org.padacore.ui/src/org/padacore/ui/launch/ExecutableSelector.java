package org.padacore.ui.launch;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.ListDialog;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;

/**
 * This class selects which executable of a project to launch. It is either
 * selected by default if there is only one executable in the project or
 * requires user input when multiple executables exist.
 * 
 * @author RS
 * 
 */
public class ExecutableSelector {

	private IProject project;
	private IPath selectedExecutable;
	private ExecutableSelectionDialogFactory execDialogFactory;

	public ExecutableSelector(IProject project,
			ExecutableSelectionDialogFactory execDialogFactory) {
		this.project = project;
		this.selectedExecutable = null;
		this.execDialogFactory = execDialogFactory;

		this.selectExecutableOfProject();
	}

	/**
	 * Returns the absolute path of a given executable according to its name.
	 * 
	 * @param executableName
	 *            the name of an executable.
	 * @param adaProject
	 *            the IAdaProject associated to project.
	 * @return the absolute path of the executable.
	 */
	private IPath getExecutableFromName(String executableName,
			IAdaProject adaProject) {
		IPath executablePath = adaProject.getExecutableDirectoryPath().append(
				executableName);

		return executablePath;
	}

	/**
	 * Selects one of the executables of the project.
	 */
	private void selectExecutableOfProject() {
		PropertiesManager propMger = new PropertiesManager(project);
		IAdaProject adaProject = propMger.getAdaProject();
		List<String> executableNames = adaProject.getExecutableNames();

		if (executableNames.size() == 1) {
			this.selectedExecutable = this.getExecutableFromName(
					executableNames.get(0), adaProject);
		} else {
			ListDialog listDialog = this.execDialogFactory
					.createExecutableSelectionDialogFor(executableNames);
			boolean executableIsSelected = listDialog.open() == Window.OK;

			if (executableIsSelected) {
				Assert.isTrue(listDialog.getResult().length == 1);

				this.selectedExecutable = this.getExecutableFromName(
						(String) listDialog.getResult()[0], adaProject);
			}

		}

	}

	/**
	 * Checks if one of the executables of the project is selected.
	 * 
	 * @return True if and only if an executable has been selected in project.
	 */
	public boolean isExecutableSelected() {
		return this.selectedExecutable != null;
	}

	/**
	 * Returns the absolute path of the selected executable of project.
	 * 
	 * @pre an executable is selected in project.
	 * @return the absolute path of the selected executable of project.
	 */
	public IPath getSelectedExecutable() {
		Assert.isLegal(this.isExecutableSelected());

		return this.selectedExecutable;
	}

}
