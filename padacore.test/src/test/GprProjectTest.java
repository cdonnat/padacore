package src.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.padacore.GprProject;

public class GprProjectTest {

	@Test
	public void constructorTest() {

		GprProject sut = new GprProject ("First_Project");
	
		assertEquals("Project name shall be set", sut.name(), "First_Project");
		assertEquals("One Source dir shall be set", sut.sourcesDir().size(), 1);
		assertEquals("Source dir shall be set to .", sut.sourcesDir().get(0), ".");
		assertEquals("Obj dir shall be set to obj", sut.objectDir(), "obj");
		assertEquals("Exe dir shall be set to exe", sut.execDir(), "exe");
	}

	
	@Test
	public void saveTest() {
		GprProject sut = new GprProject("Test");

		final String expectedDefaultProject = "project Test is\n"
				+ "\tfor Source_Dirs use (\".\");\n"
				+ "\tfor Object_Dir use \"obj\";\n" + "\tfor Exec_Dir use \"exe\";\n"
				+ "end Test;";
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		sut.save(output);

		assertEquals("Gpr project content", expectedDefaultProject, output.toString());
	}
}
 