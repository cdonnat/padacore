package org.padacore.core.launch.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.launch.LaunchingJob;
import org.padacore.core.test.utils.CommonTestUtils;

public class LaunchingJobTest {
	
	private LaunchingJob sut;
	private IPath absoluteExecPath;
	private ILaunchConfiguration launchConfig;
	
	@Before
	public void createFixture() {
		this.absoluteExecPath = mock(IPath.class);
		this.launchConfig = mock(ILaunchConfiguration.class);
		
		this.sut = new LaunchingJob(this.absoluteExecPath, this.launchConfig);
	}
	
	private void checkAppHasBeenLaunched() {
		try {
			verify(this.launchConfig).launch(eq(ILaunchManager.RUN_MODE), (IProgressMonitor)anyObject());
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testAppIsLaunched() {
		CommonTestUtils.ScheduleJobAndWaitForIt(this.sut);
		
		this.checkAppHasBeenLaunched();
	}
	

}
