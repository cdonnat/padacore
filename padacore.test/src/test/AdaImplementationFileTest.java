package src.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.padacore.AdaImplementationFile;

public class AdaImplementationFileTest {

	@Test
	public void ExtensionTest() {
		AdaImplementationFile sut = new AdaImplementationFile();

		assertEquals("Extension of Ada implementation shall be set to ",
				sut.getExtension(), "adb");
	}
}
