package org.padacore.core.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.padacore.core.GprProject;

public class GprProjectTest {

	@Test
	public void constructorTest() {

		GprProject sut = new GprProject ("First_Project");
	
		assertEquals("Project name shall be set", sut.getName(), "First_Project");
		assertEquals("One Source dir shall be set", sut.getSourcesDir().size(), 1);
		assertEquals("Source dir shall be set to .", sut.getSourcesDir().get(0), ".");
		assertEquals("Obj dir shall be null", sut.getObjectDir(), null);
		assertEquals("Exe dir shall be null", sut.getExecutableDir(), null);
	}

	
	@Test
	public void saveTest() {
		GprProject sut = new GprProject("Test");
		sut.setExecutable(true);
		sut.setObjectDir("object");
		sut.setExecutableDir("exec");
		sut.addExecutableName("main1.adb");
		sut.addExecutableName("main2.ads");

		final String expectedSavedProject = "project Test is\n"
				+ "\tfor Source_Dirs use (\".\");\n"
				+ "\tfor Object_Dir use \"object\";\n"
				+ "\tfor Exec_Dir use \"exec\";\n"
				+ "\tfor Main use (\"main1.adb\", \"main2.ads\");\n"
				+ "end Test;";		

		assertEquals("Gpr project content", expectedSavedProject,sut.toString());
	}
}
 