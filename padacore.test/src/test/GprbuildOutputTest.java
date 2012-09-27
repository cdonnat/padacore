package src.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.padacore.builder.GprbuildOutput;

public class GprbuildOutputTest {	

	private void performTestCase(String input, boolean lastEntryIndicatesProgress, int remaining) {
		GprbuildOutput sut = new GprbuildOutput();

		sut.evaluate(input);
		assertEquals(sut.lastEntryIndicatesProgress(), lastEntryIndicatesProgress);
		if (lastEntryIndicatesProgress) {
			assertEquals(sut.remainingFileToProcess(), remaining);
		}
	}

	@Test
	public void test() {
		performTestCase("completed 2 out of 24 (50%)...", true, 22);
		performTestCase("gcc -c main.adb", false, 0);
		performTestCase("completed 12 out of 100 (7%)...", true, 88);
		performTestCase("completed 12 out of 20 (100%)...", true, 8);
	}

}
