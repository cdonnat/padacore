package org.padacore.core.gnat;

import java.nio.file.Paths;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;
import org.gpr4j.core.Factory;
import org.gpr4j.core.Gpr;
import org.gpr4j.core.ILoader;

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
	public Gpr createGprProject() {

		ILoader loader = Factory.CreateLoader();
		try {
			loader.load(Paths.get(this.gprFilePath.toOSString()));
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Factory.CreateGpr(loader.getLoadedProjects().get(0));
	}

}
