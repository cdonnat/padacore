package org.padacore.core.gnat;

import org.eclipse.core.runtime.IPath;

/**
 * This class allows to create a GPR project by parsing an existing .gpr file.
 *   
 * @author RS
 *
 */
public class FileGprProjectFactory extends AbstractGprProjectFactory {
	
	private IPath gprFilePath;

	public FileGprProjectFactory(IPath gprFilePath) {
		this.gprFilePath = gprFilePath;
	}

	@Override
	public GprProject createGprProject() {
		
		GprLoader loader = new GprLoader ();
		loader.load(this.gprFilePath);
		GprBuilder builder = new GprBuilder (loader.getLoadedProjects().get(0));
		return builder.build();
	}

}
