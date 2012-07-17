package padacore.test_plugin;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.padacore.AdaSpecificationFile;

public class AdaSpecificationFileTest {

	@Test
	public void extensionTest() {
		AdaSpecificationFile sut = new AdaSpecificationFile();

		assertEquals("Extension of Ada specification shall be set to ",
				sut.getExtension(), "ads");
	}

}
