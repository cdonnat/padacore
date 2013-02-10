package org.padacore.core.gnat.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.padacore.core.gnat.GprBuilder;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.GprProject;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprBuilderTest {

	@Test
	public void test() {

		GprLoader loader = new GprLoader();
		IPath sampleProjectPath = new Path(CommonTestUtils.GetPathToTestProject() + "sample_project.gpr"); 
		loader.load(sampleProjectPath);

		GprBuilder sut = new GprBuilder(loader.getLoadedProjects().get(0), sampleProjectPath);
		GprProject gpr = sut.build();

		assertEquals("sample_project", gpr.getName());
		assertEquals(new Path(CommonTestUtils.GetPathToTestProject()), gpr.getRootDirPath());
		assertEquals("new_exe", gpr.getExecutableDir());
		assertEquals("new_exe", gpr.getObjectDir());
		assertEquals(2, gpr.getSourcesDir().size());
		assertEquals("src", gpr.getSourcesDir().get(0));
		assertEquals("include", gpr.getSourcesDir().get(1));

	}

}
