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
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.padacore.core.AbstractAdaProjectAssociationManager;
import org.padacore.core.IAdaProject;

/**
 * This class implements a build process listener which purpose is to detect all
 * the resources that were generated during the build process.
 * 
 * @author RS
 * 
 */
public class DerivedResourcesIdentifier implements IJobChangeListener {

	private IProject builtProject;
	private NonDerivedResourcesCollector projectResourcesBeforeBuild;
	private Job cleaningJob;

	public DerivedResourcesIdentifier(IProject builtProject, Job cleaningJob) {
		this.cleaningJob = cleaningJob;
		this.builtProject = builtProject;
		this.projectResourcesBeforeBuild = new NonDerivedResourcesCollector(builtProject);
	}

	@Override
	public void aboutToRun(IJobChangeEvent event) {
		if (this.cleaningJob != null) {
			try {
				this.cleaningJob.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.markExecAndObjectDirsAsDerived();
		this.rememberResourcesOfProjectBeforeBuild();
	}

	@Override
	public void awake(IJobChangeEvent event) {
	}

	@Override
	public void done(IJobChangeEvent event) {
		this.markResourcesGeneratedByBuildAsDerived();
	}

	private void addCurrentDirectoryToListIfItsNotCurrentDirectory(
			String directory, List<String> directories) {
		if (!directory.equals(".")) {
			directories.add(directory);
		}
	}

	private void markExecAndObjectDirsAsDerived() {
		IAdaProject builtAdaProject = AbstractAdaProjectAssociationManager
				.GetAssociatedAdaProject(this.builtProject);
		List<String> execAndObjectDirs = new ArrayList<String>();

		if (builtAdaProject.isExecutable()) {
			this.addCurrentDirectoryToListIfItsNotCurrentDirectory(
					builtAdaProject.getExecutableDirectoryPath(),
					execAndObjectDirs);
		}
		this.addCurrentDirectoryToListIfItsNotCurrentDirectory(
				builtAdaProject.getObjectDirectoryPath(), execAndObjectDirs);

		for (Iterator<String> sourceDirIt = builtAdaProject.getSourcesDir()
				.iterator(); sourceDirIt.hasNext();) {
			String sourceDir = sourceDirIt.next();
			execAndObjectDirs.remove(sourceDir);
		}

		List<IResource> execAndObjectDirsAsResources = new ArrayList<IResource>(
				execAndObjectDirs.size());
		for (Iterator<String> dirIt = execAndObjectDirs.iterator(); dirIt
				.hasNext();) {
			execAndObjectDirsAsResources.add(this.builtProject.getFolder(dirIt
					.next()));
		}

		this.setAllResourcesAsDerived(execAndObjectDirsAsResources);

	}

	/**
	 * Stores the list of all the resources in project prior to the build start.
	 */
	private void rememberResourcesOfProjectBeforeBuild() {
		this.projectResourcesBeforeBuild.collectAllNonDerivedResources();
	}

	private void setAllResourcesAsDerived(Collection<IResource> resources) {
		try {
			for (Iterator<IResource> resourceIt = resources.iterator(); resourceIt
					.hasNext();) {
				IResource resource = resourceIt.next();

				resource.setDerived(true, null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
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

			this.setAllResourcesAsDerived(resourcesGeneratedByBuild);

		} catch (CoreException e) {
			e.printStackTrace();
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

	@Override
	public void running(IJobChangeEvent event) {
	}

	@Override
	public void scheduled(IJobChangeEvent event) {
	}

	@Override
	public void sleeping(IJobChangeEvent event) {
	}

}
