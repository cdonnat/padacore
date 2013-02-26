package org.padacore.core.builder.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;
import org.padacore.core.builder.Error;
import org.padacore.core.builder.GprbuildErrObserver;
import org.padacore.core.builder.GprbuildOutput;
import org.padacore.core.test.utils.CommonTestUtils;

public class GprbuildErrObserverTest {

	private IProject project;
	private GprbuildErrObserver sut;
	private GprbuildOutput parser;

	private void createFixture(Error errorOnFile) {
		this.project = CommonTestUtils.CreateAdaProject();

		this.createMockedParser(errorOnFile);

		this.sut = new GprbuildErrObserver(this.project, this.parser);
	}

	private void createMockedParser(Error errorOnFile) {
		this.parser = mock(GprbuildOutput.class);
		when(this.parser.lastEntryIndicatesError()).thenReturn(true);
		when(this.parser.error()).thenReturn(errorOnFile);
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
		this.sut.update(null, "useless");
	}

	@Test
	public void testFileDirectlyUnderProjectRoot() {
		String nameOfFileWithError = "main.adb";
		Error errorOnFile = new Error(nameOfFileWithError, 10, 7,
				Error.SEVERITY_ERROR, "directlyUnderRoot");

		this.createFixture(errorOnFile);
		IFile fileWithError = this.project.getFile(nameOfFileWithError);
		this.createAndFillWithDummyContents(fileWithError);

		this.exercize();

		IMarker[] markersOfResource = this
				.getErrorMarkersOfResource(fileWithError);

		assertTrue(markersOfResource.length == 1);
		assertTrue(this.checkMarkerMatchesError(errorOnFile,
				markersOfResource[0]));
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
