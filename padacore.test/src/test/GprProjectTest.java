package src.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.padacore.GprProject;

public class GprProjectTest {

	@Test
	public void ConstructorTest() {

		GprProject sut = new GprProject ("First_Project");
	
		assertEquals("Project name shall be set", sut.name(), "First_Project");
		assertEquals("One Source dir shall be set", sut.sourcesDir().size(), 1);
		assertEquals("Source dir shall be set to .", sut.sourcesDir().get(0), ".");
		
	}

}
 