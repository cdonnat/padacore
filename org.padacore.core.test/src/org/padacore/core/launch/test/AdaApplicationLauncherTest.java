package org.padacore.core.launch.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.junit.After;
import org.junit.Test;
import org.padacore.core.launch.AdaApplicationLauncher;
import org.padacore.core.launch.BuildingJob;
import org.padacore.core.launch.ILaunchConfigurationProvider;
import org.padacore.core.launch.ApplicationLauncherJobFactory;
import org.padacore.core.launch.LaunchingJob;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.test.utils.CommonTestUtils;

public class AdaApplicationLauncherTest {

	private Fixture fixture;

	private class Fixture {
		public AdaApplicationLauncher sut;
		public IProject project;
		public Job launchingJob;
		public Job buildingJob;
		public ApplicationLauncherJobFactory factory;
		public ILaunchConfiguration launchConfig;
		public IFile executableFile;
		public IPath expectedExecutablePath;
	}

	private void createFixture(String executableSourceName,
			boolean createExecutable, boolean stubBuildingAndLaunchingJobs,
			boolean forceFailedBuild) {
		this.fixture = new Fixture();
		this.fixture.project = CommonTestUtils.CreateAdaProject(true, true,
				new String[] { executableSourceName }, forceFailedBuild);

		this.fixture.expectedExecutablePath = this
				.getExecutablePathFor(executableSourceName);

		if (createExecutable) {
			this.fixture.executableFile = this
					.createExecFileFor(executableSourceName);
		}

		ILaunchConfigurationProvider launchConfigProvider = mock(ILaunchConfigurationProvider.class);
		this.fixture.launchConfig = mock(ILaunchConfiguration.class);
		when(
				launchConfigProvider
						.getLaunchConfigurationFor(this.fixture.expectedExecutablePath))
				.thenReturn(this.fixture.launchConfig);

		if (stubBuildingAndLaunchingJobs) {
			this.fixture.launchingJob = mock(Job.class);
			this.fixture.buildingJob = mock(Job.class);
		} else {
			this.fixture.launchingJob = new LaunchingJob(
					this.fixture.expectedExecutablePath,
					this.fixture.launchConfig);
			this.fixture.buildingJob = new BuildingJob(
					this.fixture.expectedExecutablePath, this.fixture.project);
		}

		this.fixture.factory = mock(ApplicationLauncherJobFactory.class);
		when(
				this.fixture.factory.createLaunchingJobFor(
						this.fixture.expectedExecutablePath,
						this.fixture.launchConfig)).thenReturn(
				this.fixture.launchingJob);
		when(
				this.fixture.factory
						.createBuildingJobFor(this.fixture.expectedExecutablePath))
				.thenReturn(this.fixture.buildingJob);

		PropertiesManager propertiesManager = new PropertiesManager(
				this.fixture.project);
		this.fixture.sut = new AdaApplicationLauncher(
				propertiesManager.getAdaProject(), launchConfigProvider,
				this.fixture.factory);
	}

	private void createFixture(String executableSourceName,
			boolean createExecutable, boolean stubBuildingAndLaunchingJobs) {
		this.createFixture(executableSourceName, createExecutable,
				stubBuildingAndLaunchingJobs, false);
	}

	private void createFile(IFile file) {
		try {
			file.create(new ByteArrayInputStream("toto".getBytes()), true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private IFile createExecFileFor(String execSourceFilename) {
		IFile executableFile = this.fixture.project.getFile(CommonTestUtils
				.GetExecutableNameFromExecSourceName(execSourceFilename));
		this.createFile(executableFile);

		return executableFile;
	}

	@Test
	public void testLaunchFromExecFile() {
		String execSourceFile = "main.adb";

		this.createFixture(execSourceFile, true, true);

		this.fixture.sut.performLaunchFromFile(this.fixture.executableFile.getLocation());

		this.checkOnlyLaunchHasBeenPerformed();
	}

	private IPath getExecutablePathFor(String execSourceFilename) {
		return this.fixture.project
				.getLocation()
				.append(CommonTestUtils
						.GetExecutableNameFromExecSourceName(execSourceFilename));
	}

	@After
	public void tearDown() {
		try {
			this.fixture.project.delete(true, null);
			this.fixture = null;
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLaunchFromSourceFileWithExistingExecFile() {
		String execSourceFile = "main.adb";

		this.createFixture(execSourceFile, true, true);

		this.fixture.sut.performLaunchFromFile(this.fixture.project
				.getFile(execSourceFile).getLocation());

		this.checkOnlyLaunchHasBeenPerformed();
	}

	@Test
	public void testLaunchFromSourceFileWithMissingExecFile() {
		String execSourceFile = "main.adb";
		this.createFixture(execSourceFile, false, false);

		this.fixture.sut.performLaunchFromFile(this.fixture.project
				.getFile(execSourceFile).getLocation());

		this.checkBothBuildAndLaunchHaveBeenPerformed();
	}

	@Test
	public void testLaunchFromSourceFileWithMissingExecFileAndFailedBuild() {
		String execSourceFile = "main.adb";
		this.createFixture(execSourceFile, false, false, true);

		this.fixture.sut.performLaunchFromFile(this.fixture.project
				.getFile(execSourceFile).getLocation());

		this.checkOnlyBuildHasBeenPerformedAndExecutableFileDoesNotExist();
	}

	private void checkBuildAndLaunch(
			boolean expectedSuccessfulBuildAndLaunchPerformed) {
		try {
			this.fixture.buildingJob.join();
			assertTrue(this.fixture.expectedExecutablePath.toFile().exists() == expectedSuccessfulBuildAndLaunchPerformed);
			this.fixture.launchingJob.join();
			if (expectedSuccessfulBuildAndLaunchPerformed) {
				verify(this.fixture.launchConfig).launch(
						eq(ILaunchManager.RUN_MODE),
						(IProgressMonitor) anyObject());
			} else {
				verifyZeroInteractions(this.fixture.launchConfig);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void checkOnlyBuildHasBeenPerformedAndExecutableFileDoesNotExist() {
		this.checkCorrectLaunchingJobHasBeenCreated();
		this.checkCorrectBuildingJobHasBeenCreated();

		this.checkBuildAndLaunch(false);
	}

	private void checkCorrectLaunchingJobHasBeenCreated() {
		verify(this.fixture.factory).createLaunchingJobFor(
				this.fixture.expectedExecutablePath, this.fixture.launchConfig);
	}

	private void checkCorrectBuildingJobHasBeenCreated() {
		verify(this.fixture.factory).createBuildingJobFor(
				this.fixture.expectedExecutablePath);
	}

	private void checkBothBuildAndLaunchHaveBeenPerformed() {
		this.checkCorrectLaunchingJobHasBeenCreated();
		this.checkCorrectBuildingJobHasBeenCreated();

		this.checkBuildAndLaunch(true);
	}

	private void checkOnlyLaunchHasBeenPerformed() {
		this.checkCorrectLaunchingJobHasBeenCreated();
		verifyNoMoreInteractions(this.fixture.factory);

		verify(this.fixture.launchingJob).schedule();
		verifyZeroInteractions(this.fixture.buildingJob);
	}

}
