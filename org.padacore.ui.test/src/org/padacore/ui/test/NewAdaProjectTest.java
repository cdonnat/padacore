package org.padacore.ui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;
import org.padacore.core.AdaProjectNature;
import org.padacore.ui.wizards.NewAdaProject;

public class NewAdaProjectTest {

	@Test
	public void testCreateProjectWithDefaultLocation() {

		final String projectName = "TestProject";
		NewAdaProject sut = new NewAdaProject(projectName, true, null);

		IProject createdProject = sut.create(true);

		final String projectPath = createdProject.getLocationURI().getPath();
		
		assertNotNull("Project shall be not null", createdProject);
		assertTrue("Project shall be open", createdProject.isOpen());

		try {
			IProjectDescription desc = createdProject.getDescription();

			assertEquals("Project shall contain one nature", 1, desc.getNatureIds().length);

			assertTrue("Project natures shall contain adaProjectNature",
					desc.hasNature(AdaProjectNature.NATURE_ID));

			assertEquals("Project location shall be in workspace", ResourcesPlugin.getWorkspace()
					.getRoot().getLocationURI().getPath()
					+ "/" + projectName, projectPath);
			
			assertTrue("GPR project file shall exist", new File(projectPath + "/" + projectName + ".gpr").exists());

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}