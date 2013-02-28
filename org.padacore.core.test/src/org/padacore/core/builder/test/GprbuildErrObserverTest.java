package org.padacore.core.builder.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.junit.Test;
import org.padacore.core.builder.Error;
import org.padacore.core.builder.GprbuildErrObserver;
import org.padacore.core.builder.GprbuildOutput;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprbuildErrObserverTest {

	private IProject project;
	private Fixture fixture;

	private class Fixture {
		public GprbuildErrObserver sut;
		public GprbuildOutput parser;
	}

	private IFile setupProjectWithFileUnderRoot(String nameOfFileWithError) {
		this.project = CommonTestUtils.CreateAdaProject();
		List<IPath> sourceDirs = new ArrayList<IPath>();
		sourceDirs.add(this.project.getLocation());
		
		IAdaProject adaProject = mock(IAdaProject.class);
		when(adaProject.getRootPath()).thenReturn(this.project.getLocation());
		when(adaProject.getSourceDirectoriesPaths()).thenReturn(sourceDirs);
		
		CommonTestUtils.SetAssociatedAdaProject(this.project, adaProject);

		IFile fileWithError = this.project.getFile(nameOfFileWithError);
		this.createAndFillWithDummyContents(fileWithError);

		return fileWithError;
	}

	private void createFixture(Error errorOnFile) {
		Assert.isNotNull(this.project);

		this.fixture = new Fixture();
		this.createMockedParser(errorOnFile);

		this.fixture.sut = new GprbuildErrObserver(this.project,
				this.fixture.parser);
	}

	private void createMockedParser(Error errorOnFile) {
		this.fixture.parser = mock(GprbuildOutput.class);
		when(this.fixture.parser.lastEntryIndicatesError()).thenReturn(true);
		when(this.fixture.parser.error()).thenReturn(errorOnFile);
	}

	private void createAndFillWithDummyContents(IFile file) {

		try {
			file.create(new ByteArrayInputStream(new byte[] { 'd', 'u', 'm',
					'm', 'y' }), true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void exercize() {
		this.fixture.sut.update(null, "useless");
	}

	private void checkMarkerOfFileIsCorrect(IFile file, Error errorOnFile) {
		IMarker[] markersOfResource = this.getErrorMarkersOfResource(file);

		assertTrue(markersOfResource.length == 1);
		assertTrue(this.checkMarkerMatchesError(errorOnFile,
				markersOfResource[0]));
	}

	@Test
	public void testFileDirectlyUnderProjectRoot() {
		String nameOfFileWithError = "main.adb";
		Error errorOnFile = new Error(nameOfFileWithError, 10, 7,
				Error.SEVERITY_ERROR, "directlyUnderRoot");

		IFile fileWithError = this
				.setupProjectWithFileUnderRoot(nameOfFileWithError);

		this.createFixture(errorOnFile);
		this.exercize();
		this.checkMarkerOfFileIsCorrect(fileWithError, errorOnFile);

	}

	@Test
	public void testFileInOneOfSourceDirectories() {
		String nameOfFileWithError = "another.ads";
		Error errorOnFile = new Error(nameOfFileWithError, 5, 11,
				Error.SEVERITY_WARNING, "inOneOfSourceDirs");

		IFile fileWithError = this
				.setupProjectWithFileInSourceDirs(nameOfFileWithError);

		this.createFixture(errorOnFile);
		this.exercize();
		this.checkMarkerOfFileIsCorrect(fileWithError, errorOnFile);

	}

	@Test
	public void testFileInAReferencedProject() {
		String nameOfFileWithError = "outtaProject";
		Error errorOnFile = new Error(nameOfFileWithError, 10, 12,
				Error.SEVERITY_ERROR, "inAReferencedProject");
		
		IFile fileWithError = this.setupProjectWithFileInAReferencedProject(nameOfFileWithError);
		
		this.createFixture(errorOnFile);
		this.exercize();
		this.checkMarkerOfFileIsCorrect(fileWithError, errorOnFile);
	}

	private IFile setupProjectWithFileInAReferencedProject(
			String nameOfFileWithError) {
		IProject referencedProject = CommonTestUtils.CreateAdaProject(); 
		IFolder folderInRefProject = this.createSourceDirsFor(referencedProject);
		
		this.project = CommonTestUtils.CreateAdaProject();
		try {
			IProjectDescription projectDesc = this.project.getDescription();
			projectDesc.setReferencedProjects(new IProject[] {referencedProject});
			this.project.setDescription(projectDesc, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		this.createSourceDirsFor(this.project);
		
		IFile fileWithError = folderInRefProject.getFile(nameOfFileWithError);
		this.createAndFillWithDummyContents(fileWithError);
		
		return fileWithError;

	}

	private IFolder createSourceDirsFor(IProject project) {
		String srcFolders[] = { "src1", "src2" };
		IAdaProject adaProject = mock(IAdaProject.class);
		List<IPath> sourceDirPaths = new ArrayList<IPath>();
		IFolder srcFolder = null;

		try {
			for (int folder = 0; folder < srcFolders.length; folder++) {
				srcFolder = project.getFolder(srcFolders[folder]);
				srcFolder.create(true, true, null);
				sourceDirPaths.add(srcFolder.getLocation());

				when(adaProject.getSourceDirectoriesPaths()).thenReturn(
						sourceDirPaths);
				when(adaProject.getRootPath()).thenReturn(
						project.getLocation());
				CommonTestUtils.SetAssociatedAdaProject(project,
						adaProject);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return srcFolder;

	}

	private IFile setupProjectWithFileInSourceDirs(String nameOfFileWithError) {
		this.project = CommonTestUtils.CreateAdaProject();
		IFile fileWithError = null;

		// create file with error in last source folder
		fileWithError = this.createSourceDirsFor(this.project).getFile(
				nameOfFileWithError);

		this.createAndFillWithDummyContents(fileWithError);

		return fileWithError;
	}

	private IMarker[] getErrorMarkersOfResource(IResource resource) {
		IMarker foundMarkers[] = null;
		try {
			foundMarkers = resource.findMarkers(IMarker.PROBLEM, false,
					IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return foundMarkers;
	}

	private boolean checkMarkerMatchesError(Error error, IMarker marker) {
		boolean markerMatchesError = false;

		try {
			markerMatchesError = marker.getType() == IMarker.PROBLEM
					&& marker.getAttribute(IMarker.LINE_NUMBER, 0) == error
							.line()
					&& marker.getAttribute(IMarker.PRIORITY, -1) == IMarker.PRIORITY_HIGH
					&& marker.getAttribute(IMarker.SEVERITY, -1) == error
							.severity()
					&& marker.getAttribute(IMarker.MESSAGE, "").equals(
							error.message());
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return markerMatchesError;
	}
}
