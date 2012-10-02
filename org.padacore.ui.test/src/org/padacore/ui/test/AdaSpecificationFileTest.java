package org.padacore.ui.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.padacore.ui.AdaSpecificationFile;

public class AdaSpecificationFileTest {

	@Test
	public void extensionTest() {
		AdaSpecificationFile sut = new AdaSpecificationFile();

		assertEquals("Extension of Ada specification shall be set to ",
				sut.getExtension(), "ads");
	}

}
