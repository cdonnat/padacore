package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.padacore.core.builder.GprbuildOutput;

public class GprbuildOutputTest {	
	
	private GprbuildOutput sut;
	
	@Before
	public void createFixture() {
		sut = new GprbuildOutput();
	}
	
	private void performProgressionTestCase(String input, boolean lastEntryIndicatesProgress, int remaining) {
		sut.evaluate(input);
		assertEquals(sut.lastEntryIndicatesProgress(), lastEntryIndicatesProgress);
		if (lastEntryIndicatesProgress) {
			assertEquals(sut.remainingFileToProcess(), remaining);
		}
	}

	@Test
	public void testProgression() {
		performProgressionTestCase("completed 2 out of 24 (50%)...", true, 22);
		performProgressionTestCase("gcc -c main.adb", false, 0);
		performProgressionTestCase("completed 12 out of 100 (7%)...", true, 88);
		performProgressionTestCase("completed 12 out of 20 (100%)...", true, 8);
	}

	@Test
	public void testError() {
		sut.evaluate("main.adb:5:35: missing \";\"");
		assertTrue (sut.lastEntryIndicatesError());
		assertEquals("File containing error", sut.error().file(), "main.adb");
		assertEquals("Line containing error", sut.error().line(), 5);
		assertEquals("Column containing error", sut.error().column(), 35);
		assertEquals("Message error", sut.error().message(), "missing \";\"");
	}
}
