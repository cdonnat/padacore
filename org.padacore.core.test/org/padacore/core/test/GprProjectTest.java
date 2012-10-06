package org.padacore.core.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.padacore.core.GprProject;

public class GprProjectTest {
	
	private GprProject sut;

	@Test
	public void testConstructor() {

		this.sut = new GprProject ("First_Project");
	
		assertEquals("Project name shall be set", sut.getName(), "First_Project");
		assertEquals("One Source dir shall be set", sut.getSourcesDir().size(), 1);
		assertEquals("Source dir shall be set to .", sut.getSourcesDir().get(0), ".");
		assertEquals("Obj dir shall be null", sut.getObjectDir(), null);
		assertEquals("Exe dir shall be null", sut.getExecutableDir(), null);
	}

	
	@Test
	public void testToString() {
		this.sut = new GprProject("Test");
		
		this.sut.setExecutable(true);
		this.sut.setObjectDir("object");
		this.sut.setExecutableDir("exec");
		this.sut.addExecutableName("main1.adb");
		this.sut.addExecutableName("main2.ads");

		final String expectedSavedProject = "project Test is\n"
				+ "\tfor Source_Dirs use (\".\");\n"
				+ "\tfor Object_Dir use \"object\";\n"
				+ "\tfor Exec_Dir use \"exec\";\n"
				+ "\tfor Main use (\"main1.adb\", \"main2.ads\");\n"
				+ "end Test;";		

		assertEquals("Gpr project content", expectedSavedProject,sut.toString());
	}
	
	@Test
	public void testToStringForDefaultProject() {
		GprProject sut = new GprProject("Default");
		sut.setExecutable(true);
		sut.addExecutableName("main.adb");

		final String expectedSavedDefaultProject = "project Default is\n"
				+ "\tfor Source_Dirs use (\".\");\n"
				+ "\tfor Main use (\"main.adb\");\n"
				+ "end Default;";		
		
		assertEquals("Gpr project content", expectedSavedDefaultProject,sut.toString());
	}
}
 