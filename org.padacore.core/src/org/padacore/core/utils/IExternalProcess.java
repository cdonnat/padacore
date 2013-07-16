package org.padacore.core.utils;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IExternalProcess {

	/**
	 * Run the command given in parameter and attach to observers given to
	 * constructors. Observers (output stream and error stream) are notify when
	 * a new entry is written in the stream.
	 * 
	 * @param cmdWithArgs
	 *            Command to run
	 * @param monitor
	 *            Progress monitor to display external process progress
	 * @throws ProgramNotFoundException
	 *             if command given in parameter does not identify an existing
	 *             command.
	 */
	public void run(String[] cmdWithArgs, IProgressMonitor monitor)
			throws ProgramNotFoundException;

	/**
	 * Stop the external process.
	 */
	public void stop();

	/**
	 * 
	 * @return True if the external process is finished, false otherwise.
	 */
	public boolean isFinished();
}
