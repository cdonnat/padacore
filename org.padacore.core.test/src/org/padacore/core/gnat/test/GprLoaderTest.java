package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprLoaderTest {

	@Test
	public void testSingleGpr() {
		GprLoader sut = new GprLoader(new Path(CommonTestUtils.GetPathToTestProject()
				+ "sample_project.gpr"));

		sut.load();

		assertEquals(1, sut.getLoadedProject().size());
		assertEquals("My_Var.saved", sut.getLoadedProject().get(0).getProject().getVariable("Save_Name")
				.getAsString());
	}

	@Test
	public void testMultipleGpr() {
		GprLoader sut = new GprLoader(new Path(CommonTestUtils.GetPathToTestProject() + "b.gpr"));

		sut.load();

		assertEquals(3, sut.getLoadedProject().size());
		assertEquals("static", sut.getLoadedProject().get(0).getProject().getVariable("from_external")
				.getAsString());
	}
}
