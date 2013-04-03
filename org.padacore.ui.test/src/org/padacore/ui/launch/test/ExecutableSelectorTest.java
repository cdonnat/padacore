package org.padacore.ui.launch.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.ListDialog;
import org.junit.Test;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.ui.launch.ExecutableSelectionDialogFactory;
import org.padacore.ui.launch.ExecutableSelector;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExecutableSelectorTest {

	private static final String EXEC_DIRECTORY = "obj/exe";
	private ExecutableSelector sut;

	public void createFixture(List<String> executableNames) {
		this.createFixture(executableNames, false);
	}

	public void createFixture(List<String> executableNames,
			boolean selectionIsCancelled) {
		IAdaProject adaProject = mock(IAdaProject.class);
		when(adaProject.getExecutableNames()).thenReturn(executableNames);
		when(adaProject.getExecutableDirectoryPath()).thenReturn(
				new Path(EXEC_DIRECTORY));
		IProject project = mock(IProject.class);
		try {
			when(
					project.getSessionProperty(new QualifiedName(
							"org.padacore", "adaProject"))).thenReturn(
					adaProject);
			when(project.hasNature(AdaProjectNature.NATURE_ID))
					.thenReturn(true);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		ListDialog execSelectionDialog = mock(ListDialog.class);
		when(execSelectionDialog.open()).thenReturn(
				selectionIsCancelled ? Window.CANCEL : Window.OK);
		when(execSelectionDialog.getResult()).thenReturn(
				new Object[] { executableNames.get(0) });
		ExecutableSelectionDialogFactory factory = mock(ExecutableSelectionDialogFactory.class);
		when(factory.createExecutableSelectionDialogFor(executableNames))
				.thenReturn(execSelectionDialog);

		this.sut = new ExecutableSelector(project, factory);
	}

	private void checkExecutableIsSelectedAndCorrect(IPath expectedPath) {
		assertTrue("Executable is selected", this.sut.isExecutableSelected());
		if (this.sut.isExecutableSelected()) {
			assertTrue("Executable path is correct", this.sut
					.getSelectedExecutable().equals(expectedPath));
		}
	}

	private void checkExecutableIsNotSelected() {
		assertFalse("Executable is not selected",
				this.sut.isExecutableSelected());
	}

	@Test
	public void testSelectionWithOnlyOneExecutable() {
		List<String> executables = new ArrayList<String>();
		String execName = "main.adb";
		executables.add(execName);

		this.createFixture(executables);

		this.checkExecutableIsSelectedAndCorrect(new Path(EXEC_DIRECTORY)
				.append(execName));
	}

	@Test
	public void testSelectionWithMultipleExecutables() {
		List<String> executables = new ArrayList<String>();
		executables.add("another.ads");
		executables.add("main.adb");

		this.createFixture(executables);

		this.checkExecutableIsSelectedAndCorrect(new Path(EXEC_DIRECTORY)
				.append("another.ads"));

	}

	@Test
	public void testSelectionIsCancelled() {
		List<String> executables = new ArrayList<String>();
		executables.add("another.ads");
		executables.add("main.adb");

		this.createFixture(executables, true);

		this.checkExecutableIsNotSelected();
	}

}
