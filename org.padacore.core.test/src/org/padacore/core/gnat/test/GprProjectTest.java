package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.padacore.core.gnat.GprProject;

public class GprProjectTest {

	private GprProject sut;

	@Test
	public void testConstructor() {

		this.sut = new GprProject("First_Project");

		assertEquals("Project name shall be set", sut.getName(),
				"First_Project");
		assertEquals("No Source dir shall be set", sut.getSourcesDir().size(),
				0);
		assertNull("Obj dir shall be null", sut.getObjectDir());
		assertFalse("Projet shall not be executable", sut.isExecutable());
	}

	@Test
	public void testToString() {
		this.sut = new GprProject("Test");

		this.sut.setExecutable(true);
		this.sut.addSourceDir(".");
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

		assertEquals("Gpr project content", expectedSavedProject,
				sut.toString());
	}
}
