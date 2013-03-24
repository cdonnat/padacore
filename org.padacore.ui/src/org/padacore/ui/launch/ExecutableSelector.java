package org.padacore.ui.launch;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.project.ResourceLocator;

//TODO add some tests (you lazy bastard!)
public class ExecutableSelector {

	private IProject project;
	private IFile selectedExecutable;

	public ExecutableSelector(IProject project) {
		this.project = project;
		this.selectedExecutable = null;

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
	private IFile getExecutableFromName(String executableName,
			IAdaProject adaProject) {
		ResourceLocator resourceLocator = new ResourceLocator(this.project);
		IPath executablePath = adaProject.getExecutableDirectoryPath().append(
				executableName);

		return (IFile) resourceLocator.findResourceFromPath(executablePath);
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
			ListDialog listDialog = this
					.createDialogForExecutableSelection(executableNames);
			boolean executableIsSelected = listDialog.open() == Window.OK;

			if (executableIsSelected) {
				Assert.isTrue(listDialog.getResult().length == 1);

				this.selectedExecutable = this.getExecutableFromName(
						(String) listDialog.getResult()[0], adaProject);
			}

		}

	}

	/**
	 * Creates a dialog which enables the user to select one of the executables
	 * of the project.
	 * 
	 * @param executableNames
	 *            the names of the executables of the project.
	 * @return a ListDialog which enables the user to select one of the
	 *         executables of the project.
	 */
	private ListDialog createDialogForExecutableSelection(
			List<String> executableNames) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		return new ExecutableSelectionDialog(shell, executableNames);
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
	public IFile getSelectedExecutable() {
		Assert.isLegal(this.isExecutableSelected());

		return this.selectedExecutable;
	}

}
