package org.padacore.core.builder;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;

/**
 * This class implements a build process listener which purpose is to detect all
 * the resources that were generated during the build process.
 * 
 * @author RS
 * 
 */
public class BuildProcessListener implements IJobChangeListener {

	private IProject builtProject;
	private ResourcesCollector projectResourcesBeforeBuild;
	private Job cleaningJob;

	public BuildProcessListener(IProject builtProject, Job cleaningJob) {
		this.cleaningJob = cleaningJob;
		this.builtProject = builtProject;
		this.projectResourcesBeforeBuild = new ResourcesCollector(builtProject);
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
		this.rememberResourcesOfProjectBeforeBuild();
	}

	@Override
	public void awake(IJobChangeEvent event) {
	}

	@Override
	public void done(IJobChangeEvent event) {
		this.markResourcesGeneratedByBuildAsDerived();
	}

	/**
	 * Stores the list of all the resources in project prior to the build start.
	 */
	private void rememberResourcesOfProjectBeforeBuild() {
		this.projectResourcesBeforeBuild.collectAllNonDerivedResources();
	}

	/**
	 * Marks all the resources that were generated during the build process as
	 * derived.
	 */
	private void markResourcesGeneratedByBuildAsDerived() {
		try {
			Collection<IResource> resourcesGeneratedByBuild = this
					.identifyResourcesGeneratedByBuild();

			for (IResource generatedResource : resourcesGeneratedByBuild) {
				generatedResource.setDerived(true, null);
			}

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
	// TODO Identification of generated resources could be made more efficient
	// by using information from GPR project: Object_Dir and Exec_Dir are by
	// definition derived.
	private Collection<IResource> identifyResourcesGeneratedByBuild()
			throws CoreException {
		ResourcesCollector projectResources = new ResourcesCollector(
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
