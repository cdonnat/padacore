package org.padacore.core.builder.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.padacore.core.builder.Error;
import org.padacore.core.builder.GprbuildOutput;

public class GprbuildOutputTest {

	private GprbuildOutput sut;

	@Before
	public void createFixture() {
		sut = new GprbuildOutput();
	}

	private void performProgressionTestCase(String input, boolean lastEntryIndicatesProgress,
			int remaining) {
		sut.evaluate(input);
		assertEquals(sut.lastEntryIndicatesProgress(), lastEntryIndicatesProgress);
		if (lastEntryIndicatesProgress) {
			assertEquals(sut.nbRemainingFilesToProcess(), remaining);
		}
	}

	@Test
	public void testProgression() {
		performProgressionTestCase("completed 2 out of 24 (50%)...", true, 22);
		performProgressionTestCase("gcc -c main.adb", false, 0);
		performProgressionTestCase("completed 12 out of 100 (7%)...", true, 88);
		performProgressionTestCase("completed 12 out of 20 (100%)...", true, 8);
	}

	private void performErrorTestCase(String input, String file, int line, int column,
			int severity, String message) {
		sut.evaluate(input);
		assertEquals("Error indicator", sut.lastEntryIndicatesError(), true);
		assertEquals("File containing error", sut.error().file(), file);
		assertEquals("Line containing error", sut.error().line(), line);
		assertEquals("Column containing error", sut.error().column(), column);
		assertEquals("Message error", sut.error().message(), message);
		assertEquals("Severity", sut.error().severity(), severity);
	}

	@Test
	public void testError() {
		performErrorTestCase("main.adb:5:35: missing \";\"", "main.adb", 5, 35,
				Error.SEVERITY_ERROR, "missing \";\"");
		performErrorTestCase("toto_test.ads:5:35:warning: variable toto is not referenced",
				"toto_test.ads", 5, 35, Error.SEVERITY_WARNING, "variable toto is not referenced");
		performErrorTestCase("toto_test.ads:5:35:error: variable toto is not referenced",
				"toto_test.ads", 5, 35, Error.SEVERITY_ERROR, "variable toto is not referenced");
	}
}
