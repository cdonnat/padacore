package org.padacore.core.builder;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * This class enables to collect all the non-derived ressources that are present
 * in a container in a recursive way (i.e. resources which are present in a
 * sub-container of given container are also collected).
 * 
 * @author RS
 * 
 */
public class ResourcesCollector {

	private IContainer container;
	private Set<IResource> containedResources;

	public ResourcesCollector(IContainer container) {
		this.container = container;
		this.containedResources = new HashSet<IResource>();
	}

	/**
	 * Returns a set containing all the non-derived resources present in the
	 * container.
	 * 
	 * @return a set containing all the non-derived resources present in the
	 *         container.
	 */
	public Set<IResource> getNonDerivedResources() {
		return this.containedResources;
	}

	/**
	 * Processes the container to find all its non-derived resources.
	 */
	public void collectAllNonDerivedResources() {
		try {
			this.container
					.accept(new ContainerVisitor(this.containedResources));
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This class implements a visitor for collecting all the non-derived
	 * resources of a container.
	 * 
	 * @author RS
	 * 
	 */
	private class ContainerVisitor implements IResourceVisitor {

		private Set<IResource> containedNonDerivedResources;

		public ContainerVisitor(Set<IResource> containedResources) {
			this.containedNonDerivedResources = containedResources;
		}

		@Override
		public boolean visit(IResource resource) throws CoreException {
			if (resource.getType() != IResource.PROJECT) {
				this.containedNonDerivedResources.add(resource);
			}

			return !resource.isDerived();
		}
	}

}
