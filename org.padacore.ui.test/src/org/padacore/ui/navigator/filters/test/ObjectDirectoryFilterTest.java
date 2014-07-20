package org.padacore.ui.navigator.filters.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.junit.Test;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.ui.navigator.filters.ObjectDirectoryFilter;

public class ObjectDirectoryFilterTest {
	
	private final static IPath PROJECT_LOCATION = new Path("root");

	private ObjectDirectoryFilter sut = new ObjectDirectoryFilter();

	private void checkResourceIsSelected(boolean isSelected,
			IResource resource, String comment) {
		assertTrue(comment, this.sut.select(null, null, resource) == isSelected);
	}

	private IFolder getSelectedResourceFromExecutableOpenedProject(
			boolean withAdaNature, IAdaProject associatedAdaProject) {
		IProject project = mock(IProject.class);

		when(project.isOpen()).thenReturn(true);

		try {
			when(project.hasNature(AdaProjectNature.NATURE_ID)).thenReturn(
					withAdaNature);
			when(
					project.getSessionProperty(new QualifiedName(
							"org.padacore", "adaProject"))).thenReturn(
					associatedAdaProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		IFolder selectedResource = mock(IFolder.class);

		when(selectedResource.getType()).thenReturn(IResource.FOLDER);
		when(selectedResource.getProject()).thenReturn(project);

		return selectedResource;
	}

	@Test
	public void testNonAdaProject() {
		IFolder selectedResource = this
				.getSelectedResourceFromExecutableOpenedProject(false, null);

		this.checkResourceIsSelected(true, selectedResource,
				"In non-Ada project");
	}

	@Test
	public void testAdaProjectWithDifferentFolderHierarchies() {

		IAdaProject adaProject = mock(IAdaProject.class);
		/*[1] Object dir neither ancestor of source dir nor of object dir 
		 * |_exe
		 * |_obj
		 * |_src
		 */
		IPath objectDirLocation = PROJECT_LOCATION.append(new Path("/obj"));
		IPath execDirLocation = PROJECT_LOCATION.append(new Path("/exe"));
		when(adaProject.getExecutableDirectoryPath()).thenReturn(
				execDirLocation);
		when(adaProject.getObjectDirectoryPath()).thenReturn(objectDirLocation);

		List<IPath> sourceDirs = new ArrayList<IPath>();
		sourceDirs.add(PROJECT_LOCATION.append(new Path("/src")));

		when(adaProject.getSourceDirectoriesPaths()).thenReturn(sourceDirs);
		when(adaProject.isExecutable()).thenReturn(true);
		
		IFolder selectedResource = this.getSelectedResourceFromExecutableOpenedProject(true, adaProject);
		

		when(selectedResource.getLocation()).thenReturn(objectDirLocation);


		this.checkResourceIsSelected(false, selectedResource,
				"[1]");

		/* [2] Object dir is an ancestor of source dir
		 * |_obj
		 * 		|_useless
		 * 				|_src
		 * |_obj
		 * |_src
		 */
		sourceDirs.add(objectDirLocation.append(new Path("/useless/src")));

		this.checkResourceIsSelected(true, selectedResource,
				"[2]");

		/* [3] Object dir is an ancestor of executable dir
		 * |_obj
		 * 		|_exe
		 *		
		 */
		sourceDirs.clear();
		when(adaProject.getExecutableDirectoryPath()).thenReturn(
				objectDirLocation.append(new Path("/exe")));

		this.checkResourceIsSelected(true, selectedResource,
				"[3]");
				

	}

}
