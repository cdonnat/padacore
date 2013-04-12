package org.padacore.core.project.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.ResourceLocator;
import org.padacore.core.test.utils.CommonTestUtils;

public class ResourceLocatorTest {

	private IProject project;
	private ResourceLocator sut;
	private String linkedFolderName;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	public void createFixture(boolean projectIsImported) {
		IPath projectPath = new Path(this.testFolder.getRoot()
				.getAbsolutePath());
		this.project = CommonTestUtils.CreateAdaProjectAt(projectPath);
		IAdaProject adaProject = mock(IAdaProject.class);
		when(adaProject.getRootPath()).thenReturn(projectPath);
		CommonTestUtils.SetAssociatedAdaProject(this.project, adaProject);

		this.sut = new ResourceLocator(this.project);
		this.linkedFolderName = new Path(testFolder.getRoot().getAbsolutePath())
				.lastSegment();

		if (projectIsImported) {
			this.createFolder(this.project.getFolder(new Path(
					this.linkedFolderName)));
			try {
				this.project.setPersistentProperty(new QualifiedName(
						"org.padacore", "projectKind"), "imported");
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void testFileInProject(IPath fileName, boolean projectIsImported) {
		Assert.isLegal(fileName.segmentCount() <= 2);
		IFile fileInProject = this
				.getFileInProject(fileName, projectIsImported);

		if (fileName.segmentCount() == 2) {
			this.createFolder(this.getFolderInProject(
					new Path(fileName.segments()[0]), projectIsImported));
		}

		this.createFile(fileInProject);
		this.checkResourceWasFound(fileName);
	}

	private IFile getFileInProject(IPath fileName, boolean projectIsImported) {
		IFile file;

		if (projectIsImported) {
			file = this.project.getFolder(this.linkedFolderName).getFile(
					fileName);
		} else {
			file = this.project.getFile(fileName);
		}

		return file;
	}

	private void checkResourceWasFound(IPath relativeResourcePath) {
		IPath absoluteResourcePath = null;
		try {
			absoluteResourcePath = new Path(this.testFolder.getRoot()
					.getCanonicalPath()).append(relativeResourcePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		IResource retrievedResource = this.sut
				.findResourceFromPath(absoluteResourcePath);

		assertNotNull(retrievedResource);
	}

	private void testFolderInProject(IPath folderName, boolean projectIsImported) {
		Assert.isLegal(folderName.segmentCount() <= 2);
		IFolder folderInProject = this.getFolderInProject(folderName,
				projectIsImported);

		if (folderName.segmentCount() == 2) {
			this.createFolder(this.getFolderInProject(
					new Path(folderName.segments()[0]), projectIsImported));
		}

		this.createFolder(folderInProject);
		this.checkResourceWasFound(folderName);
	}

	private IFolder getFolderInProject(IPath folderName,
			boolean projectIsImported) {
		IFolder folder;

		if (projectIsImported) {
			folder = this.project.getFolder(this.linkedFolderName).getFolder(
					folderName);
		} else {
			folder = this.project.getFolder(folderName);
		}

		return folder;
	}

	private void createFile(IFile file) {
		try {
			file.create(new ByteArrayInputStream(new byte[] { 1, 2, 3, 4 }),
					true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void createFolder(IFolder folder) {
		try {
			folder.create(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testForCreatedProject() {
		this.createFixture(false);

		this.testFileInProject(new Path("testFile.txt"), false);
		this.testFileInProject(new Path("testFolder/testFile.exe"), false);

		this.testFolderInProject(new Path("firstFolder"), false);
		this.testFolderInProject(new Path("first/second"), false);
	}

	@Test
	public void testForImportedProject() {
		this.createFixture(true);

		this.testFileInProject(new Path("testFile.txt"), true);
		this.testFileInProject(new Path("testFolder/testFile.exe"), true);

		this.testFolderInProject(new Path("firstFolder"), true);
		this.testFolderInProject(new Path("first/second"), true);
	}
}
