package org.padacore.core.gnat;

import java.nio.file.Paths;

import org.antlr.runtime.RecognitionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.gpr4j.api.Factory;
import org.gpr4j.api.Gpr;
import org.gpr4j.api.ILoader;
import org.padacore.core.utils.ErrorLog;

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
			ErrorLog.appendMessage(e.getMessage(), IStatus.ERROR);
		}
		return Factory.CreateGpr(loader.getLoadedProjects().get(0));
	}

}
