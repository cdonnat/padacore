package org.padacore.core.utils.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.padacore.core.test.stubs.ConsoleStub;
import org.padacore.core.utils.ExternalProcessInfo;

public class ExternalProcessInfoTest {

	private ExternalProcessInfo sut;
	private ConsoleStub consoleStub;

	private void createFixture() {
		this.consoleStub = new ConsoleStub();
		this.sut = new ExternalProcessInfo("testProcess", this.consoleStub);
	} 

	@Test
	public void testStart() {
		this.createFixture();

		this.sut.start(new String[] { "echo", "toto" });

		assertTrue(this.consoleStub.lastMessage().contains("echo"));
		assertTrue(this.consoleStub.lastMessage().contains("toto"));
	}

	@Test
	public void testFinish() {
		this.createFixture();

		this.sut.finish(true);
		assertTrue(this.consoleStub.lastMessage().contains("successful"));

		this.sut.finish(false);
		assertTrue(this.consoleStub.lastMessage().contains("failed"));
	}
}
