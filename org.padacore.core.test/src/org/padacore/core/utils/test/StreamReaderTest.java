package org.padacore.core.utils.test;

import static org.junit.Assert.assertEquals;

import java.util.Observer;

import org.junit.Test;
import org.padacore.core.test.stubs.InputStreamStub;
import org.padacore.core.test.stubs.ObserverStub;
import org.padacore.core.utils.StreamReader;

public class StreamReaderTest {

	ObserverStub observerStub = new ObserverStub();
	InputStreamStub inputStreamStub = new InputStreamStub();
	StreamReader sut;

	private void createFixture() {
		this.sut = new StreamReader(this.inputStreamStub, new Observer[] { this.observerStub });
	}

	@Test
	public void testRun() {
		this.createFixture();

		this.inputStreamStub.write("toto");

		this.sut.run();
		
		assertEquals("toto", this.observerStub.getStringNotified());
	}
}
