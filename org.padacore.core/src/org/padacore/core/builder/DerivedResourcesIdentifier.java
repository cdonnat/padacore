package org.padacore.core.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.padacore.core.project.AbstractAdaProjectAssociationManager;
import org.padacore.core.project.IAdaProject;
import org.padacore.core.project.ResourceLocator;
import org.padacore.core.utils.ErrorLog;

/**
 * This class identifies and set all the resources generated during build as
 * derived.
 * 
 * @author RS
 * 
 */
// TODO refactor me!
public class DerivedResourcesIdentifier {

	private IProject builtProject;
	private NonDerivedResourcesCollector projectResourcesBeforeBuild;
	private IAdaProject adaProjectForBuiltProject;

	public DerivedResourcesIdentifier(IProject builtProject) {
		this.builtProject = builtProject;
		this.projectResourcesBeforeBuild = new NonDerivedResourcesCollector(
				builtProject);
		this.adaProjectForBuiltProject = AbstractAdaProjectAssociationManager
				.GetAssociatedAdaProject(builtProject);
	}

	public void begin() {
		this.markExecAndObjectDirsAsDerived();
		this.rememberResourcesOfProjectBeforeBuild();
	}

	public void done() {
		this.markResourcesGeneratedByBuildAsDerived();
	}

	/**
	 * Adds the given directory to list if it not the current directory.
	 * 
	 * @param absoluteDirectoryPath
	 *            the absolute path of the directory to add in list.
	 * @param directories
	 *            the list of directories in which element shall be added.
	 */
	private void addDirectoryPathToList(IPath absoluteDirectoryPath,
			List<IPath> directories) {
		IPath projectRoot = this.adaProjectForBuiltProject.getRootPath();

		if (!absoluteDirectoryPath.equals(projectRoot)) {
			directories.add(absoluteDirectoryPath);
		}
	}

	/**
	 * Converts the given directory list (which contains name of directories) to
	 * a project resource list.
	 * 
	 * @param directoryList
	 *            the directory list to convert.
	 * @return the converted project resource list.
	 */
	private List<IResource> convertDirectoryListToResourceList(
			List<IPath> directoryList) {

		List<IResource> execAndObjectDirsAsResources = new ArrayList<IResource>(
				directoryList.size());
		IResource currentResource;
		ResourceLocator resourceLocator = new ResourceLocator(this.builtProject);

		for (Iterator<IPath> dirIt = directoryList.iterator(); dirIt.hasNext();) {
			currentResource = resourceLocator
					.findResourceFromPath(dirIt.next());
			execAndObjectDirsAsResources.add(currentResource);
		}

		return execAndObjectDirsAsResources;
	}

	/**
	 * Builds the list of executable and object directories which do not overlap
	 * with source directories.
	 * 
	 * @return the list of executable and object directories which do not
	 *         overlap with source directories.
	 */
	private List<IPath> buildListOfExecAndObjectDirsExcludingSourceDirs() {
		List<IPath> execAndObjectDirs = new ArrayList<IPath>();

		if (this.adaProjectForBuiltProject.isExecutable()) {
			this.addDirectoryPathToList(
					this.adaProjectForBuiltProject.getExecutableDirectoryPath(),
					execAndObjectDirs);
		}
		this.addDirectoryPathToList(
				this.adaProjectForBuiltProject.getObjectDirectoryPath(),
				execAndObjectDirs);

		for (Iterator<String> sourceDirIt = this.adaProjectForBuiltProject
				.getSourcesDir().iterator(); sourceDirIt.hasNext();) {
			String sourceDir = sourceDirIt.next();
			execAndObjectDirs.remove(sourceDir);
		}

		return execAndObjectDirs;
	}

	/**
	 * Set executable and object directories as derived.
	 */
	private void markExecAndObjectDirsAsDerived() {

		List<IPath> execAndObjectDirs = this
				.buildListOfExecAndObjectDirsExcludingSourceDirs();
		List<IResource> execAndObjectDirsAsResources = this
				.convertDirectoryListToResourceList(execAndObjectDirs);

		this.setResourcesAsDerived(execAndObjectDirsAsResources);

	}

	/**
	 * Stores the list of all the resources in project prior to the build start.
	 */
	private void rememberResourcesOfProjectBeforeBuild() {
		this.projectResourcesBeforeBuild.collectAllNonDerivedResources();
	}

	/**
	 * Sets the given resources as derived.
	 * 
	 * @param resources
	 *            the list of resources which have to be set as derived.
	 */
	private void setResourcesAsDerived(Collection<IResource> resources) {
		try {
			for (Iterator<IResource> resourceIt = resources.iterator(); resourceIt
					.hasNext();) {
				IResource resource = resourceIt.next();

				resource.setDerived(true, null);
			}
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Marks all the resources that were generated during the build process as
	 * derived.
	 */
	private void markResourcesGeneratedByBuildAsDerived() {
		try {
			Collection<IResource> resourcesGeneratedByBuild = this
					.identifyResourcesGeneratedByBuild();

			this.setResourcesAsDerived(resourcesGeneratedByBuild);

		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Identify the resources that were generated during the build process.
	 * 
	 * @throws CoreException
	 *             if member of projects could not be retrieved.
	 * @return a Collection of Resource that were generated during the build
	 *         process.
	 */
	private Collection<IResource> identifyResourcesGeneratedByBuild()
			throws CoreException {
		NonDerivedResourcesCollector projectResources = new NonDerivedResourcesCollector(
				this.builtProject);
		projectResources.collectAllNonDerivedResources();
		Set<IResource> projectResourcesAfterBuild = projectResources
				.getNonDerivedResources();

		Assert.isTrue(projectResourcesAfterBuild.size() >= this.projectResourcesBeforeBuild
				.getNonDerivedResources().size());

		Set<IResource> projectResourcesGeneratedByBuild = projectResourcesAfterBuild;
		projectResourcesGeneratedByBuild
				.removeAll(this.projectResourcesBeforeBuild
						.getNonDerivedResources());

		return projectResourcesGeneratedByBuild;
	}
}
