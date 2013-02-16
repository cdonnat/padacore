package org.padacore.core.gnat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;

/**
 * Represents a project in a gpr file.
 * 
 * @author Charles
 * 
 */
public class Project implements IPropertiesProvider {

	private String name;
	private IPath pathToGpr;
	private Package selfPackage;
	private Package currentPackage;
	private Map<String, Package> packages;
	private Map<String, Project> references;

	/**
	 * Constructors
	 * 
	 * @param name
	 *            Name of the project.
	 */
	public Project(IPath pathToGpr) {
		this.pathToGpr = pathToGpr;
		this.name = pathToGpr.removeFileExtension().lastSegment().toLowerCase();
		this.selfPackage = new Package("self");
		this.packages = new HashMap<String, Package>();
		this.references = new HashMap<String, Project>();
		this.currentPackage = this.selfPackage;
		this.addDefaultAttribute();

	}

	/**
	 * Add the default attribute to project ("name", "project_dir, etc).
	 */
	private void addDefaultAttribute() {
		this.addAttribute("name", Symbol.CreateString(this.name));
		this.addAttribute("project_dir",
				Symbol.CreateString(this.pathToGpr.removeLastSegments(1).toOSString()));
	}

	// Queries:

	/**
	 * @return The name of the context.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return The path to the gpr project.
	 */
	public IPath getPath() {
		return this.pathToGpr;
	}

	/**
	 * 
	 * @return The list of reference projects.
	 */
	public List<IPropertiesProvider> getReferenceProjects() {
		return new ArrayList<IPropertiesProvider>(this.references.values());
	}

	/**
	 * 
	 * @param varName
	 *            Name of the variable to look for.
	 * @return True is returned if the variable is found in the project.
	 */
	public boolean variableIsDefined(String varName) {
		return this.isDefined(varName, new VariablesProviderDelegate());
	}

	/**
	 * 
	 * @param attributeName
	 *            Name of the attribute to look for
	 * @return True is returned if the attribute is defined in the project.
	 */
	public boolean attributeIsDefined(String attributeName) {
		return this.isDefined(FormatAttribute(attributeName), new AttributesProviderDelegate());
	}

	/**
	 * @pre varName is defined in the context.
	 * @param varName
	 *            Name of the variable.
	 * @return Term corresponding to the name of variable.
	 * 
	 */
	public Symbol getVariable(String varName) {
		Assert.isLegal(this.variableIsDefined(varName));
		return this.get(varName, new VariablesProviderDelegate());
	}

	/**
	 * @pre attributeName is defined in the context.
	 * @param attributeName
	 *            Name of the attribute.
	 * @return Term corresponding to the name of the attribute.
	 */
	public Symbol getAttribute(String attributeName) {
		Assert.isLegal(this.attributeIsDefined(attributeName));
		return this.get(FormatAttribute(attributeName), new AttributesProviderDelegate());
	}

	// Commands:

	/**
	 * Add a variable to the context.
	 * 
	 * @param varName
	 *            Name of the variable to add.
	 * @param varValue
	 *            Value of the variable to add.
	 */
	public void addVariable(String varName, Symbol varValue) {
		this.currentPackage.addVariable(varName, varValue);
	}

	/**
	 * Add an attribute to the context
	 * 
	 * @param attributeName
	 *            Name of the attribute to add.
	 * @param attributeValue
	 *            Value of the attribute to add.
	 */
	public void addAttribute(String attributeName, Symbol attributeValue) {
		this.currentPackage.addAttribute(attributeName, attributeValue);
	}

	/**
	 * Add a reference project to the project. The project will have the
	 * visibility on the symbols defined in the referenced projects.
	 * 
	 * @param reference
	 *            Reference context to add.
	 */
	public void addReferenceProject(Project referenceProject) {
		this.references.put(referenceProject.getName(), referenceProject);
	}

	/**
	 * Notify the project that a begin package instruction has been found. A new
	 * package is create and it is set to the current package.
	 * 
	 * @param packageName
	 *            Name of the package.
	 */
	public void beginPackage(String packageName) {
		Package newPackage = new Package(packageName);
		this.packages.put(newPackage.getName(), newPackage);
		this.currentPackage = newPackage;
	}

	/**
	 * Current package is set to self package.
	 */
	public void endPackage() {
		this.currentPackage = this.selfPackage;
	}

	/**
	 * Add a new package to the current project based on another package.
	 * 
	 * @param newPackageName
	 *            Name of the package to be added.
	 * @param projectName
	 *            Name of the project containing the package to copy.
	 * @param packageName
	 *            Name of the package to copy.
	 */
	public void addPackageFrom(String newPackageName, String projectName, String packageName) {
		Assert.isLegal(this.references.containsKey(projectName.toLowerCase()));
		Assert.isLegal(this.references.get(projectName.toLowerCase()).packages
				.containsKey(packageName.toLowerCase()));

		Package newPackage = new Package(newPackageName,
				this.references.get(projectName.toLowerCase()).packages.get(packageName.toLowerCase()));
		this.packages.put(newPackageName.toLowerCase(), newPackage);
	}

	/**
	 * Join the string element of an array
	 * 
	 * @param tab
	 *            Array containing the element to join
	 * @param from
	 *            Starting index in the array
	 * @param size
	 *            Number of element to join
	 * @pre from + size - 1 <= tab.length
	 * @return A string containing the joined element.
	 */
	private static String Join(String[] tab, int from, int size) {
		Assert.isLegal(from + size - 1 <= tab.length);
		StringBuilder res = new StringBuilder();

		for (int i = from; i < from + size; i++) {
			res.append(tab[i]);
		}
		return res.toString();
	}

	/**
	 * Return the prefix of the given name.
	 * 
	 * @param fullName
	 *            Full name.
	 * @return Prefix of the fullName or fullName is no prefix is found.
	 */
	private static String GetPrefix(String fullName) {
		String[] fullNameAsList = fullName.split("\\.", 2);
		return fullNameAsList[0].toLowerCase();
	}

	/**
	 * Return the fullName without the prefix.
	 * 
	 * @param fullName
	 *            Full name.
	 * @pre There is a prefix.
	 * @return The full name without the prefix.
	 */
	private static String GetNameWithoutPrefix(String fullName) {
		Assert.isLegal(!GetPrefix(fullName).isEmpty());
		String[] fullNameAsList = fullName.split("\\.", 2);
		return Join(fullNameAsList, 1, fullNameAsList.length - 1);
	}

	interface IProviderDelegate {
		abstract boolean isDefined(IPropertiesProvider provider, String name);

		abstract Symbol get(IPropertiesProvider provider, String name);
	}

	class VariablesProviderDelegate implements IProviderDelegate {
		@Override
		public boolean isDefined(IPropertiesProvider provider, String name) {
			return provider.variableIsDefined(name);
		}

		@Override
		public Symbol get(IPropertiesProvider provider, String name) {
			return provider.getVariable(name);
		}
	}

	class AttributesProviderDelegate implements IProviderDelegate {
		@Override
		public boolean isDefined(IPropertiesProvider provider, String name) {
			return provider.attributeIsDefined(name);
		}

		@Override
		public Symbol get(IPropertiesProvider provider, String name) {
			return provider.getAttribute(name);
		}
	}

	private boolean isDefined(String symbolName, IProviderDelegate delegate) {
		String prefix = GetPrefix(symbolName);
		String nameWithoutPrefix = GetNameWithoutPrefix(symbolName);
		boolean isDefined = delegate.isDefined(this.currentPackage, symbolName);

		if (!isDefined) {
			isDefined = delegate.isDefined(this.selfPackage, symbolName);
		}

		if (!isDefined && this.references.containsKey(prefix)) {
			IPropertiesProvider referenceProvider = this.references.get(prefix);
			isDefined = delegate.isDefined(referenceProvider, nameWithoutPrefix);
		}

		if (!isDefined && this.packages.containsKey(prefix)) {
			IPropertiesProvider packageProvider = this.packages.get(prefix);
			isDefined = delegate.isDefined(packageProvider, nameWithoutPrefix);
		}
		return isDefined;
	}

	private Symbol get(String symbolName, IProviderDelegate delegate) {
		Symbol res = null;
		boolean isDefined = delegate.isDefined(this.currentPackage, symbolName);
		if (isDefined) {
			res = delegate.get(this.currentPackage, symbolName);
		} else if (delegate.isDefined(this.selfPackage, symbolName)) {
			res = delegate.get(this.selfPackage, symbolName);
		} else if (this.references.containsKey(GetPrefix(symbolName))) {
			IPropertiesProvider referenceProvider = this.references.get(GetPrefix(symbolName));
			res = delegate.get(referenceProvider, GetNameWithoutPrefix(symbolName));
		} else {
			IPropertiesProvider packageProvider = this.packages.get(GetPrefix(symbolName));
			res = delegate.get(packageProvider, GetNameWithoutPrefix(symbolName));
		}
		return res;
	}

	private static String FormatAttribute(String attributeName) {
		return attributeName.replace('\'', '.');
	}
}
