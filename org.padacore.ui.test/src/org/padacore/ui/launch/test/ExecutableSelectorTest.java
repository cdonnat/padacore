package org.padacore.ui.launch.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.junit.Test;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.ui.launch.ExecutableSelector;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExecutableSelectorTest {

	private static final String EXEC_DIRECTORY = "obj/exe";
	private ExecutableSelector sut;

	public void createFixture(List<String> executableNames) {
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
			when(project.hasNature(AdaProjectNature.NATURE_ID)).thenReturn(true);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		this.sut = new ExecutableSelector(project);
	}

	@Test
	public void testSelectionWithOnlyOneExecutable() {
		List<String> executables = new ArrayList<String>();
		String execName = "main.adb";
		executables.add(execName);
		this.createFixture(executables);

		assertTrue("Executable is selected", this.sut.isExecutableSelected());
		if (this.sut.isExecutableSelected()) {
			assertTrue(
					"Executable path is correct",
					this.sut.getSelectedExecutable().equals(
							new Path(EXEC_DIRECTORY).append(execName)));
		}
	}
	
	//TODO add tests for multiple executables using SWTBot
//	@Test
//	public void testSelectionWithMultipleExecutables() {
//		List<String> executables = new ArrayList<String>();
//		executables.add("main.adb");
//		executables.add("another.ads");
//		this.createFixture(executables);
//		assertTrue("Executable is selected", this.sut.isExecutableSelected());
//		
//		
//	}
}
