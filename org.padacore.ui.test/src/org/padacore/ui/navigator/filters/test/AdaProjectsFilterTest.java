package org.padacore.ui.navigator.filters.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.ui.navigator.filters.AdaProjectsFilter;

public class AdaProjectsFilterTest {

	private AdaProjectsFilter sut = new AdaProjectsFilter();

	private void checkResourceIsSelected(boolean isSelected,
			IResource resource, String comment) {
		assertTrue(comment, this.sut.select(null, null, resource) == isSelected);
	}

	@Test
	public void testProjectFilter() {
		IProject project = mock(IProject.class);
		when(project.getType()).thenReturn(IResource.PROJECT);
		when(project.isOpen()).thenReturn(false);

		this.checkResourceIsSelected(true, project, "Closed project");

		when(project.isOpen()).thenReturn(true);

		this.checkResourceIsSelected(false, project, "Opened non-Ada project");

		try {
			when(project.hasNature(AdaProjectNature.NATURE_ID))
					.thenReturn(true);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		this.checkResourceIsSelected(true, project, "Opened Ada project");
	}
}
