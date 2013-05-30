package org.padacore.ui.navigator.test;

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
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.BeforeClass;
import org.junit.Test;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.ui.Activator;
import org.padacore.ui.navigator.ProjectAndDirectoriesFilter;
import org.padacore.ui.preferences.IPreferenceConstants;

public class ProjectAndDirectoriesFilterTest {

	private ProjectAndDirectoriesFilter sut = new ProjectAndDirectoriesFilter();

	@BeforeClass
	public static void setup() {
		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();
		preferenceStore.setValue(
				IPreferenceConstants.NAVIGATOR_SOURCE_EXTENSIONS, "src");
		preferenceStore.setValue(
				IPreferenceConstants.NAVIGATOR_OBJECT_EXTENSIONS, "obj");
		preferenceStore.setValue(
				IPreferenceConstants.NAVIGATOR_EXEC_EXTENSIONS, "exe");
		preferenceStore.setValue(
				IPreferenceConstants.NAVIGATOR_FILE_WITHOUT_EXTENSION, false);
	}

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

	@Test
	public void testSourceFolderFilter() {

		// source folders hierarchy:
		// starred folders shall be displayed
		// |--- src*
		// ------|--- src*
		// |--- not_src*
		// --------|--- src*
		// --------|--- not_src*
		// ----------------|--- src*
		// --------|--- not_src

		IProject project = CommonTestUtils.CreateAdaProject();

		IAdaProject adaProject = mock(IAdaProject.class);
		List<IPath> sourceDirs = new ArrayList<>();

		IFolder firstLevelSrcFolder = project.getFolder("src");
		IFolder secondLevelSrcFolderInSrc = firstLevelSrcFolder
				.getFolder("src");
		IFolder firstLevelNotSrcFolder = project.getFolder("not_src");
		IFolder secondLevelSrcFolderInNotSrc = firstLevelNotSrcFolder
				.getFolder("src");
		IFolder secondLevelNotSrcFolderInNotSrc = firstLevelNotSrcFolder
				.getFolder("not_src");
		IFolder thirdLevelSrcFolderInNotSrc = secondLevelNotSrcFolderInNotSrc
				.getFolder("src");
		IFolder secondLevelNotSrcFolderInNotSrc2 = firstLevelNotSrcFolder
				.getFolder("not_src2");

		when(adaProject.getSourceDirectoriesPaths()).thenReturn(sourceDirs);
		sourceDirs.add(firstLevelSrcFolder.getLocation());
		sourceDirs.add(secondLevelSrcFolderInSrc.getLocation());
		sourceDirs.add(secondLevelSrcFolderInNotSrc.getLocation());
		sourceDirs.add(thirdLevelSrcFolderInNotSrc.getLocation());

		when(adaProject.getObjectDirectoryPath()).thenReturn(
				project.getLocation());
		when(adaProject.getExecutableDirectoryPath()).thenReturn(
				project.getLocation());

		CommonTestUtils.SetAssociatedAdaProject(project, adaProject);

		this.checkResourceIsSelected(true, firstLevelSrcFolder,
				"First level source folder");
		this.checkResourceIsSelected(true, secondLevelSrcFolderInSrc,
				"Second level source folder in source folder");
		this.checkResourceIsSelected(true, firstLevelNotSrcFolder,
				"First level not source folder");
		this.checkResourceIsSelected(true, secondLevelSrcFolderInNotSrc,
				"Second level source folder in not source folder");
		this.checkResourceIsSelected(true, secondLevelNotSrcFolderInNotSrc,
				"Second level not source folder in not source folder");
		this.checkResourceIsSelected(true, thirdLevelSrcFolderInNotSrc,
				"Third level source folder in not source folder");
		this.checkResourceIsSelected(false, secondLevelNotSrcFolderInNotSrc2,
				"Second level not source folder in not source folder - 2");
	}
	
	@Test
	public void testObjFolderFilter() {
		// folders hierarchy:
		// starred folders shall be displayed
		// project
		// |--- not_obj*
		// -------|--- not_obj*
		// ---------------|--- obj*
		// ---------------|--- not_obj

		IProject project = CommonTestUtils.CreateAdaProject();

		IAdaProject adaProject = mock(IAdaProject.class);
		List<IPath> sourceDirs = new ArrayList<>();

		IFolder firstLevelNotObjFolder = project.getFolder("not_obj");
		IFolder secondLevelNotObjFolder = firstLevelNotObjFolder
				.getFolder("not_obj");
		IFolder thirdLevelObjFolder = secondLevelNotObjFolder.getFolder("obj");
		IFolder thirdLevelNotObjFolder = secondLevelNotObjFolder
				.getFolder("not_obj");

		when(adaProject.getSourceDirectoriesPaths()).thenReturn(sourceDirs);
		when(adaProject.getObjectDirectoryPath()).thenReturn(
				thirdLevelObjFolder.getLocation());
		when(adaProject.getExecutableDirectoryPath()).thenReturn(
				project.getLocation());
		
		CommonTestUtils.SetAssociatedAdaProject(project, adaProject);

		this.checkResourceIsSelected(true, firstLevelNotObjFolder,
				"First level not object folder");
		this.checkResourceIsSelected(true, secondLevelNotObjFolder,
				"Second level not object folder");
		this.checkResourceIsSelected(true, thirdLevelObjFolder,
				"Third level object folder");
		this.checkResourceIsSelected(false, thirdLevelNotObjFolder,
				"Third level not object folder");
	}
	
	@Test
	public void testExeFolderFilter() {
		// folders hierarchy:
		// starred folders shall be displayed
		// project
		// |--- not_exe*
		// -------|--- not_exe*
		// ---------------|--- exe*
		// ---------------|--- not_exe

		IProject project = CommonTestUtils.CreateAdaProject();

		IAdaProject adaProject = mock(IAdaProject.class);
		List<IPath> sourceDirs = new ArrayList<>();

		IFolder firstLevelNotExeFolder = project.getFolder("not_exe");
		IFolder secondLevelNotExeFolder = firstLevelNotExeFolder
				.getFolder("not_exe");
		IFolder thirdLevelExeFolder = secondLevelNotExeFolder.getFolder("exe");
		IFolder thirdLevelNotExeFolder = secondLevelNotExeFolder
				.getFolder("not_exe");

		when(adaProject.isExecutable()).thenReturn(true);
		when(adaProject.getSourceDirectoriesPaths()).thenReturn(sourceDirs);
		when(adaProject.getObjectDirectoryPath()).thenReturn(project.getLocation());
		when(adaProject.getExecutableDirectoryPath()).thenReturn(
				thirdLevelExeFolder.getLocation());
		
		CommonTestUtils.SetAssociatedAdaProject(project, adaProject);

		this.checkResourceIsSelected(true, firstLevelNotExeFolder,
				"First level not executable folder");
		this.checkResourceIsSelected(true, secondLevelNotExeFolder,
				"Second level not executable folder");
		this.checkResourceIsSelected(true, thirdLevelExeFolder,
				"Third level executable folder");
		this.checkResourceIsSelected(false, thirdLevelNotExeFolder,
				"Third level not executable folder");
	}
	
	//TODO add tests for files
	
}
