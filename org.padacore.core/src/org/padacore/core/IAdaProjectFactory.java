package org.padacore.core;

public interface IAdaProjectFactory {
	
	/**
	 * Creates and returns a new Ada project.
	 * 
	 * @return a new Ada project initialized.
	 * @post returned Ada project is not null. 
	 */
	public IAdaProject createAdaProject();

}
