package org.padacore.ui.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.padacore.ui.AdaImplementationFile;

public class AdaImplementationFileTest {

	@Test
	public void extensionTest() {
		AdaImplementationFile sut = new AdaImplementationFile();

		assertEquals("Extension of Ada implementation shall be set to ",
				sut.getExtension(), "adb");
	}
}
