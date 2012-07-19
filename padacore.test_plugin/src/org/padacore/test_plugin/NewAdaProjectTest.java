package org.padacore.test_plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.padacore.AdaProjectNature;
import org.padacore.wizards.NewAdaProject;

public class NewAdaProjectTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void testCreateProjectWithDefaultLocation() {

		NewAdaProject sut = new NewAdaProject("TestProject", true, null);

		IProject createdProject = sut.Create();

		assertNotNull("Project shall be not null", createdProject);
		assertTrue("Project shall be open", createdProject.isOpen());

		try {
			IProjectDescription desc = createdProject.getDescription();
						
			assertEquals("Project shall contain one nature", 1, desc.getNatureIds().length);
			assertEquals(
					"Project natures shall contain adaProjectNature",
					AdaProjectNature.NATURE_ID, desc.getNatureIds()[0]);
			
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
