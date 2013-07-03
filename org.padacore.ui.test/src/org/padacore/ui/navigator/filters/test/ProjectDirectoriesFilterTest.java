package org.padacore.ui.navigator.filters.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.test.utils.CommonTestUtils;
import org.padacore.ui.Activator;
import org.padacore.ui.navigator.filters.ProjectDirectoriesFilter;
import org.padacore.ui.preferences.IPreferenceConstants;

public class ProjectDirectoriesFilterTest {

	private IProject project;
	private ProjectDirectoriesFilter sut;

	@Before
	public void createFixture() {
		this.project = CommonTestUtils.CreateAdaProject();
		this.sut = new ProjectDirectoriesFilter();
	}

	private void setStringPreferenceTo(String prefName, String prefValue) {
		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();
		preferenceStore.setValue(prefName, prefValue);
	}

	private void setBooleanPreferenceTo(String prefName, boolean prefValue) {
		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();
		preferenceStore.setValue(prefName, prefValue);
	}

	private void checkAllResourcesAreSelected(IResource[] resources) {
		this.checkResourceSelection(resources, new IResource[] {});
	}

	private void checkNoResourceIsSelected(IResource[] resources) {
		this.checkResourceSelection(new IResource[] {}, resources);
	}

	private void checkResourceSelection(IResource[] expectedSelectedResources,
			IResource[] expectedNotSelectedResources) {

		for (IResource selectedRes : expectedSelectedResources) {
			this.checkResourceIsSelected(true, selectedRes,
					selectedRes.getName());
		}

		for (IResource notSelectedRes : expectedNotSelectedResources) {
			this.checkResourceIsSelected(false, notSelectedRes,
					notSelectedRes.getName());
		}
	}

	private void checkResourceIsSelected(boolean isSelected,
			IResource resource, String comment) {
		assertTrue(comment, this.sut.select(null, null, resource) == isSelected);
	}

	private void configureProjectDirectories(IFolder[] sourceDirs,
			IFolder objDir, IFolder exeDir) {
		IAdaProject adaProject = mock(IAdaProject.class);
		List<IPath> sourceDirLocations;

		if (sourceDirs == null) {
			sourceDirLocations = new ArrayList<IPath>(0);
		} else {
			sourceDirLocations = new ArrayList<IPath>(sourceDirs.length);
			for (IFolder sourceDir : sourceDirs) {
				sourceDirLocations.add(sourceDir.getLocation());
			}
		}

		when(adaProject.getSourceDirectoriesPaths()).thenReturn(
				sourceDirLocations);

		if (objDir == null) {
			when(adaProject.getObjectDirectoryPath()).thenReturn(
					this.project.getLocation());
		} else {
			when(adaProject.getObjectDirectoryPath()).thenReturn(
					objDir.getLocation());
		}

		if (exeDir == null) {
			when(adaProject.getExecutableDirectoryPath()).thenReturn(
					this.project.getLocation());
		} else {
			when(adaProject.isExecutable()).thenReturn(true);
			when(adaProject.getExecutableDirectoryPath()).thenReturn(
					exeDir.getLocation());
		}

		CommonTestUtils.SetAssociatedAdaProject(this.project, adaProject);
	}

	@Test
	public void testSourceFolderFilter() {

		// source folders hierarchy:
		// starred folders shall be displayed
		// |--- src1*
		// ------|--- src2*
		// |--- not_src1*
		// --------|--- src3*
		// --------|--- not_src2*
		// ----------------|--- src4*
		// --------|--- not_src3

		IFolder src1 = this.project.getFolder("src1");
		IFolder src2 = src1.getFolder("src2");
		IFolder not_src1 = this.project.getFolder("not_src1");
		IFolder src3 = not_src1.getFolder("src3");
		IFolder not_src2 = not_src1.getFolder("not_src2");
		IFolder src4 = not_src2.getFolder("src4");
		IFolder not_src3 = not_src1.getFolder("not_src3");

		this.configureProjectDirectories(
				new IFolder[] { src1, src2, src3, src4 }, null, null);

		this.checkResourceSelection(new IResource[] { src1, src2, not_src1,
				src3, not_src2, src4 }, new IResource[] { not_src3 });

	}

	private enum ProjectDirKind {
		OBJ_DIR, EXE_DIR;
	}

	private void runTestCaseForSimpleProjectDir(ProjectDirKind projectDirKind) {
		String folderNamePrefix = "";

		switch (projectDirKind) {
			case OBJ_DIR:
				folderNamePrefix = "obj";
				break;
			case EXE_DIR:
				folderNamePrefix = "exe";
				break;
			default:
				break;
		}

		IFolder not_ObjOrExe1 = project.getFolder("not_" + folderNamePrefix
				+ "1");
		IFolder not_ObjOrExe2 = not_ObjOrExe1.getFolder("not_"
				+ folderNamePrefix + "2");
		IFolder objOrExe = not_ObjOrExe2.getFolder(folderNamePrefix);
		IFolder not_objOrExe3 = not_ObjOrExe2.getFolder("not_"
				+ folderNamePrefix + "3");

		this.configureProjectDirectories(null, objOrExe, null);

		this.checkResourceSelection(new IResource[] { not_ObjOrExe1,
				not_ObjOrExe2, objOrExe }, new IResource[] { not_objOrExe3 });
	}

	@Test
	public void testObjFolderFilter() {
		// folders hierarchy:
		// starred folders shall be displayed
		// project
		// |--- not_obj1*
		// -------|--- not_obj2*
		// ---------------|--- obj*
		// ---------------|--- not_obj3

		this.runTestCaseForSimpleProjectDir(ProjectDirKind.OBJ_DIR);
	}

	@Test
	public void testExeFolderFilter() {
		// folders hierarchy:
		// starred folders shall be displayed
		// project
		// |--- not_exe1*
		// -------|--- not_exe2*
		// ---------------|--- exe*
		// ---------------|--- not_exe3

		this.runTestCaseForSimpleProjectDir(ProjectDirKind.EXE_DIR);

	}

	@Test
	public void testSrcFileFilter() {
		this.setStringPreferenceTo(
				IPreferenceConstants.NAVIGATOR_SOURCE_EXTENSIONS, "ads;adb");

		IFolder srcFolder = this.project.getFolder("src");
		IFile adaSpec = srcFolder.getFile("mySpec.ads");
		IFile adaBody = srcFolder.getFile("myBody.adb");
		IFile nonSelected = srcFolder.getFile("notAnAdaFile.c");

		this.configureProjectDirectories(new IFolder[] { srcFolder }, null,
				null);

		this.checkResourceSelection(new IResource[] { adaSpec, adaBody },
				new IResource[] { nonSelected });
	}

	@Test
	public void testObjFileFilter() {
		this.setStringPreferenceTo(
				IPreferenceConstants.NAVIGATOR_OBJECT_EXTENSIONS, "o;ali");

		IFolder objFolder = this.project.getFolder("obj");
		IFile aliFile = objFolder.getFile("mySource.ali");
		IFile objectFile = objFolder.getFile("mySource.o");
		IFile srcFile = objFolder.getFile("mySource.ads");

		this.configureProjectDirectories(null, objFolder, null);

		this.checkResourceSelection(new IResource[] { aliFile, objectFile },
				new IResource[] { srcFile });
	}

	@Test
	public void testExeFileFilter() {
		this.setStringPreferenceTo(
				IPreferenceConstants.NAVIGATOR_EXEC_EXTENSIONS, "exe");

		IFolder exeFolder = this.project.getFolder("exe");
		IFile binaryFile = exeFolder.getFile("myMain.exe");
		IFile objFile = exeFolder.getFile("mySource.o");

		this.configureProjectDirectories(null, null, exeFolder);

		this.checkResourceSelection(new IResource[] { binaryFile },
				new IResource[] { objFile });
	}

	@Test
	public void testGnatProjectFileIsSelected() {
		IFile gnatProjectFileOfProject = this.project.getFile(this.project
				.getName() + ".gpr");
		IFile anotherGnatProjectFile = this.project
				.getFile("anotherGnatProject.gpr");

		this.checkResourceIsSelected(true, gnatProjectFileOfProject,
				"GNAT project file of project");
		this.checkResourceIsSelected(false, anotherGnatProjectFile,
				"Another GNAT project file");
	}

	@Test
	public void testFileWithoutExtension() {
		IFolder srcFolder = this.project.getFolder("src");
		IFile noExtensionInSrc = srcFolder.getFile("inSrc");

		IFolder objFolder = this.project.getFolder("obj");
		IFile noExtensionInObj = objFolder.getFile("inObj");

		IFolder exeFolder = this.project.getFolder("bin");
		IFile noExtensionInExe = exeFolder.getFile("inBin");

		this.configureProjectDirectories(new IFolder[] { srcFolder },
				objFolder, exeFolder);

		boolean displayFilesWithoutExtensions = false;

		this.setBooleanPreferenceTo(
				IPreferenceConstants.NAVIGATOR_FILE_WITHOUT_EXTENSION,
				displayFilesWithoutExtensions);
		this.checkNoResourceIsSelected(new IResource[] { noExtensionInSrc,
				noExtensionInObj, noExtensionInExe });

		displayFilesWithoutExtensions = true;

		this.setBooleanPreferenceTo(
				IPreferenceConstants.NAVIGATOR_FILE_WITHOUT_EXTENSION,
				displayFilesWithoutExtensions);
		this.checkAllResourcesAreSelected(new IResource[] { noExtensionInSrc,
				noExtensionInObj, noExtensionInExe });

	}

	// FIXME add tests for files

}
