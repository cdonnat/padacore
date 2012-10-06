package org.padacore.ui.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.AdaProjectNature;
import org.padacore.ui.test.utils.ProjectDescriptionUtils;
import org.padacore.ui.test.utils.TestUtils;

public class AdaProjectNatureTest {

	private IProject adaProject;

	@Before
	public void createAdaProject() {
		this.adaProject = TestUtils.createAdaProject();
	}

	@Test
	public void testConfigureProject() {
		ProjectDescriptionUtils.CheckAdaBuilderIsTheFirstBuilder(adaProject, "Configure project");
	}

	@Test
	public void testDeconfigureProject() {
		AdaProjectNature adaNature = new AdaProjectNature();
		adaNature.setProject(this.adaProject);
		try {
			adaNature.deconfigure();
			ProjectDescriptionUtils.CheckThereIsNoAdaBuilder(adaProject, "Deconfigure project");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
