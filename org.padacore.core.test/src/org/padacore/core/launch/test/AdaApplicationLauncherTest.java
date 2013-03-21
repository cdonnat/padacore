package org.padacore.core.launch.test;

import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.junit.Test;
import org.padacore.core.launch.AdaApplicationLauncher;
import org.padacore.core.launch.ILaunchConfigurationProvider;
import org.padacore.core.project.PropertiesManager;
import org.padacore.core.test.utils.CommonTestUtils;

public class AdaApplicationLauncherTest {

	private AdaApplicationLauncher sut;
	private IProject project;
	private ILaunchConfigurationProvider launchConfigProvider;
	private ILaunchConfiguration launchConfig;

	private void createFixture(String[] executableNames) {
		this.launchConfigProvider = mock(ILaunchConfigurationProvider.class);
		this.launchConfig = mock(ILaunchConfiguration.class);
		when(
				this.launchConfigProvider
						.getLaunchConfigurationFor((IPath) anyObject()))
				.thenReturn(this.launchConfig);
		this.project = CommonTestUtils.CreateAdaProject(true, true,
				executableNames);
		PropertiesManager propertiesManager = new PropertiesManager(
				this.project);
		this.sut = new AdaApplicationLauncher(
				propertiesManager.getAdaProject(), this.launchConfigProvider);

	}

	private void createFile(IFile file) {
		try {
			file.create(new ByteArrayInputStream("toto".getBytes()), true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private IFile createExecFileFor(String execSourceFilename) {
		IFile executableFile = this.project.getFile(CommonTestUtils
				.GetExecutableNameFromExecSourceName(execSourceFilename));
		this.createFile(executableFile);

		return executableFile;
	}

	private IFile createExecSourceFileNamed(String execSourceFilename) {
		IFile executableSourceFile = this.project.getFile(execSourceFilename);
		this.createFile(executableSourceFile);

		return executableSourceFile;
	}

	@Test
	public void testLaunchFromExecFile() {
		String[] execSourceFiles = new String[] { "main.adb" };

		this.createFixture(execSourceFiles);

		IFile executableFile = this.createExecFileFor(execSourceFiles[0]);
		IPath expectedExecutablePath = this
				.getExecutablePathFor(execSourceFiles[0]);

		this.sut.performLaunchFromFile(executableFile);
		this.checkExecutableWasLaunched(expectedExecutablePath);
	}

	private IPath getExecutablePathFor(String execSourceFilename) {
		return this.project
				.getLocation()
				.append(CommonTestUtils
						.GetExecutableNameFromExecSourceName(execSourceFilename));
	}

	@Test
	public void testLaunchFromExecSourceFile() {
		String[] execSourceFiles = new String[] { "main.adb" };

		this.createFixture(execSourceFiles);

		this.createExecFileFor(execSourceFiles[0]);
		IFile executableSourceFile = this
				.createExecSourceFileNamed(execSourceFiles[0]);
		IPath expectedExecutablePath = this
				.getExecutablePathFor(execSourceFiles[0]);

		this.sut.performLaunchFromFile(executableSourceFile);
		this.checkExecutableWasLaunched(expectedExecutablePath);
	}

	private void checkExecutableWasLaunched(IPath expectedExecutablePath) {
		verify(this.launchConfigProvider).getLaunchConfigurationFor(
				expectedExecutablePath);
		try {
			verify(this.launchConfig).launch(anyString(), (IProgressMonitor)anyObject());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
