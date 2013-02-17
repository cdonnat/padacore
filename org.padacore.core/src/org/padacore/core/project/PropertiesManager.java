package org.padacore.core.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.padacore.core.utils.ErrorLog;

/**
 * Provides access to the different properties associated to a project (either
 * session or persistent ones).
 * 
 * @author RS
 * 
 */
public class PropertiesManager {

	private final static String PROPERTY_QUALIFIER = "org.padacore";
	private final static String IMPORTED_PROJECT = "imported";
	private final static String CREATED_PROJECT = "created";
	private final static String PROJECT_KIND_PROPERTY = "projectKind";
	private final static String ADA_PROJECT_PROPERTY = "adaProject";

	private IProject project;

	public enum ProjectKind {
		IMPORTED, CREATED
	};

	public PropertiesManager(IProject project) {
		try {
			Assert.isLegal(project.hasNature(AdaProjectNature.NATURE_ID));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		this.project = project;
	}

	/**
	 * Returns the kind of project : either imported or created.
	 * 
	 * @return the kind of a project.
	 */
	public ProjectKind getProjectKind() {
		ProjectKind projectKind;

		String projectKindPpty = this
				.getPersistenPropertyNamed(PROJECT_KIND_PROPERTY);
		Assert.isTrue(projectKindPpty.equals(IMPORTED_PROJECT)
				|| projectKindPpty.equals(CREATED_PROJECT));

		if (this.getPersistenPropertyNamed(PROJECT_KIND_PROPERTY).equals(
				IMPORTED_PROJECT)) {
			projectKind = ProjectKind.IMPORTED;
		} else {
			projectKind = ProjectKind.CREATED;

		}

		return projectKind;
	}

	/**
	 * Returns the qualified name corresponding to given property name.
	 * 
	 * @param propertyName
	 *            the property for which a qualified name is needed.
	 * @return the qualified name for the property.
	 */
	private QualifiedName getQualifiedNameFor(String propertyName) {
		return new QualifiedName(PROPERTY_QUALIFIER, propertyName);
	}

	/**
	 * Sets the kind of project: either imported or created.
	 * 
	 * @param projectKind
	 *            the project kind to set.
	 */
	public void setProjectKind(ProjectKind projectKind) {
		try {
			this.project.setPersistentProperty(this
					.getQualifiedNameFor(PROJECT_KIND_PROPERTY),
					projectKind == ProjectKind.CREATED ? CREATED_PROJECT
							: IMPORTED_PROJECT);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}
	}

	/**
	 * Returns the IAdaProject associated to project.
	 * 
	 * @return the associated IAdaProject (cannot be null).
	 */
	public IAdaProject getAdaProject() {
		Object adaProjectPpty = this
				.getSessionPropertyNamed(ADA_PROJECT_PROPERTY);

		return (IAdaProject) adaProjectPpty;
	}

	/**
	 * Sets the given IAdaProject as a session property of the project.
	 * 
	 * @param adaProject
	 *            the IAdaProject to associate.
	 */
	public void setAdaProject(IAdaProject adaProject) {
		this.setSessionPropertyNamed(ADA_PROJECT_PROPERTY, adaProject);
	}

	/**
	 * Returns the persistent property whose local name corresponds to given
	 * name.
	 * 
	 * @param name
	 *            the local name of the property to retrieve
	 * @return the required persistent property
	 */
	private String getPersistenPropertyNamed(String name) {
		String property = null;

		try {
			property = this.project.getPersistentProperty(this
					.getQualifiedNameFor(name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		Assert.isNotNull(property);

		return property;
	}

	/**
	 * Sets the session property which local name corresponds to given name.
	 * 
	 * @param name
	 *            local name of the property.
	 * @param property
	 *            value of the property to set.
	 */
	private void setSessionPropertyNamed(String name, Object property) {

		try {
			this.project.setSessionProperty(this.getQualifiedNameFor(name),
					property);
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

	}

	/**
	 * Returns the session property which local name corresponds to given name.
	 * 
	 * @param name
	 *            the local name of the property to retrieve
	 * @return the required session property
	 */
	private Object getSessionPropertyNamed(String name) {
		Object property = null;

		try {
			property = this.project.getSessionProperty(this
					.getQualifiedNameFor(name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		return property;
	}

}
