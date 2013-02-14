package org.padacore.core.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.padacore.core.test.stubs.ExternalProcessStub;
import org.padacore.core.test.stubs.MonitorStub;
import org.padacore.core.utils.ExternalProcessCanceler;

public class ExternalProcessCancelerTest {

	private ExternalProcessCanceler sut;
	private ExternalProcessStub externalProcessStub;
	private MonitorStub monitorStub;

	public void createFixture() {
		this.externalProcessStub = new ExternalProcessStub();
		this.monitorStub = new MonitorStub();
		this.sut = new ExternalProcessCanceler(this.externalProcessStub, this.monitorStub);
	}

	@Test
	public void teststopProcessWhenMonitorIsCanceled() {
		this.createFixture();

		this.monitorStub.setCanceled(true);
		this.sut.stopProcessWhenMonitorIsCanceled();
		assertTrue(this.externalProcessStub.isStopCalled());

		this.monitorStub.setCanceled(false);
		this.externalProcessStub.reset();
		this.sut.stopProcessWhenMonitorIsCanceled();
		assertFalse(this.externalProcessStub.isStopCalled());
	}

	@Test
	public void testRun() {
		this.createFixture();

		this.externalProcessStub.setIsFinished(true);
		this.sut.run();

		assertFalse(this.externalProcessStub.isStopCalled());
	}
}
