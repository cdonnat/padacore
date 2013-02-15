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
public class PropertiesProvider {

	private final static String PROPERTY_QUALIFIER = "org.padacore";
	private final static String IMPORTED_PROJECT = "imported";
	private final static String CREATED_PROJECT = "created";
	private final static String PROJECT_KIND = "projectKind";

	private IProject project;

	public enum ProjectKind {
		IMPORTED, CREATED
	};

	public PropertiesProvider(IProject project) {
		try {
			Assert.isLegal(project.hasNature(AdaProjectNature.NATURE_ID));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		this.project = project;
	}

	public ProjectKind getProjectKind() {
		ProjectKind projectKind;

		String projectKindPpty = this.getPersistenPropertyNamed(PROJECT_KIND);
		Assert.isTrue(projectKindPpty.equals(IMPORTED_PROJECT)
				|| projectKindPpty.equals(CREATED_PROJECT));

		if (this.getPersistenPropertyNamed(PROJECT_KIND).equals(
				IMPORTED_PROJECT)) {
			projectKind = ProjectKind.IMPORTED;
		} else {
			projectKind = ProjectKind.CREATED;

		}

		return projectKind;
	}

	/**
	 * Returns the persistent property whose local name corresponds to given name. 
	 * @param name the local name of the property to retrieve
	 * @return the required persistent property
	 */
	private String getPersistenPropertyNamed(String name) {
		String property = null;

		try {
			property = this.project.getPersistentProperty(new QualifiedName(
					PROPERTY_QUALIFIER, name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		Assert.isNotNull(property);

		return property;
	}

	/**
	 * Returns the session property whose local name corresponds to given name.
	 * @param name the local name of the property to retrieve
	 * @return the required session property
	 */
	private Object getSessionPropertyNamed(String name) {
		Object property = null;

		try {
			property = this.project.getSessionProperty(new QualifiedName(
					PROPERTY_QUALIFIER, name));
		} catch (CoreException e) {
			ErrorLog.appendException(e);
		}

		Assert.isNotNull(property);

		return property;
	}

}
