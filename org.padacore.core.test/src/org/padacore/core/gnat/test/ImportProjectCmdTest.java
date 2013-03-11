package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.padacore.core.gnat.ImportProjectCmd;
import org.padacore.core.test.utils.CommonTestUtils;

public class ImportProjectCmdTest {

	private static String ProjectName = "sample_project";

	private IProgressMonitor monitor;
	private ImportProjectCmd sut;

	private void createFixture() {
		this.monitor = mock(IProgressMonitor.class);
		this.sut = new ImportProjectCmd(new Path(CommonTestUtils.GetPathToTestProject()
				+ ProjectName + ".gpr"));
	}

	@Before
	public void setUp() {
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				if (project.exists()) {
					project.delete(true, null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		CheckProjectIsInWorkspace(false);
	}

	@Test
	public void testImportSuccessul() {
		this.createFixture();
		this.sut.run(this.monitor);
		CheckProjectIsInWorkspace(true);
		CheckReferences();
		assertEquals("Number of project in workspace", 2, ResourcesPlugin.getWorkspace().getRoot()
				.getProjects().length);
	}

	@Test
	public void testImportAborted() {
		this.createFixture();
		Mockito.when(this.monitor.isCanceled()).thenReturn(false, true, false);
		this.sut.run(this.monitor);
		CheckProjectIsInWorkspace(false);
	}

	private static void CheckProjectIsInWorkspace(boolean isInWorkspace) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(ProjectName);
		assertEquals("Project in workspace", isInWorkspace, project.exists());
	}

	private static void CheckReferences() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(ProjectName);
		IProjectDescription description = null;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			e.printStackTrace();
		}

		assertEquals("Project shall contain one reference", 1,
				description.getReferencedProjects().length);
	}
}
