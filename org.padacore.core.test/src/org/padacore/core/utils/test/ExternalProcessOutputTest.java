package org.padacore.core.utils.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.padacore.core.test.stubs.ConsoleStub;
import org.padacore.core.utils.ExternalProcessOutput;

public class ExternalProcessOutputTest {

	private ExternalProcessOutput sut;
	private ConsoleStub consoleStub;

	private void createFixture() {
		this.consoleStub = new ConsoleStub();
		this.sut = new ExternalProcessOutput(this.consoleStub);
	}
	
	@Test
	public void testUpdate() {
		this.createFixture();
		
		this.sut.update(null, "hello");
		assertEquals("hello", this.consoleStub.lastMessage());
		
		this.sut.update(null, "world!");
		assertEquals("world!", this.consoleStub.lastMessage());
	}
}
