package org.padacore.core.launch.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.launch.BuildingJob;
import org.padacore.core.test.utils.CommonTestUtils;

public class BuildingJobTest {

	private IPath absoluteExecPath;
	private IProject builtProject;
	private BuildingJob sut;

	@Before
	public void createFixture() {
		this.absoluteExecPath = mock(IPath.class);

		File mockedFile = mock(File.class);
		when(mockedFile.exists()).thenReturn(false).thenReturn(true);
		when(this.absoluteExecPath.toFile()).thenReturn(mockedFile);

		this.builtProject = mock(IProject.class);
		this.sut = new BuildingJob(this.absoluteExecPath, this.builtProject);
	}

	@SuppressWarnings("unchecked")
	private void checkProjectBuildHasBeenRequested() {
		try {
			verify(this.builtProject).build(
					eq(IncrementalProjectBuilder.INCREMENTAL_BUILD),
					eq("org.padacore.core.builder.AdaProjectBuilder"),
					(Map<String, String>) isNull(),
					(IProgressMonitor) anyObject());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testProjectBuildIsRequested() {
		CommonTestUtils.ScheduleJobAndWaitForIt(this.sut);

		this.checkProjectBuildHasBeenRequested();
	}
}
