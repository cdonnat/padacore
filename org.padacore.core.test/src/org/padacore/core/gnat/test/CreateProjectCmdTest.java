package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Test;
import org.padacore.core.gnat.CreateProjectCmd;
import org.padacore.core.project.ProjectBuilder;

public class CreateProjectCmdTest {

	private IProgressMonitor monitor;
	private CreateProjectCmd sut;

	private void createFixture(String projectName, boolean maisIsRequired) {
		this.monitor = mock(IProgressMonitor.class);
		this.sut = new CreateProjectCmd(null, projectName, maisIsRequired);
	}

	@Test
	public void testWithoutMain() {
		final String projectName = "WithoutMain";
		this.createFixture(projectName, false);
		this.sut.run(this.monitor);
		CheckProjectIsInWorkspace(projectName);
		CheckCreatedFiles(projectName, false);
	}

	@Test
	public void testWithMain() {
		final String projectName = "WithMain";
		this.createFixture(projectName, true);
		this.sut.run(this.monitor);
		CheckProjectIsInWorkspace(projectName);
		CheckCreatedFiles(projectName, true);
	}

	private static void CheckProjectIsInWorkspace(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		assertTrue("Project in workspace", project.exists());
	}

	private static void CheckCreatedFiles(String projectName, boolean mainExists) {
		File file = new File(ProjectBuilder.GetProjectPath(projectName, null)
				.append(projectName + ".gpr").toOSString());
		assertTrue("Gpr shall be created", file.exists());
		file = new File(ProjectBuilder.GetProjectPath(projectName, null).append("main.adb")
				.toOSString());
		assertEquals("Main shall be created when required", mainExists, file.exists());
	}
}
