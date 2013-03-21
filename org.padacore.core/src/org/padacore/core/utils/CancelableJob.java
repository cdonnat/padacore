package org.padacore.core.utils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public abstract class CancelableJob extends Job {

	public CancelableJob(String name) {
		super(name);
	}

	@Override
	public IStatus run(IProgressMonitor monitor) {
		try {
			this.execute(monitor);
		} catch (OperationCanceledException e) {
			this.executeWhenCanceled();
		}
		return monitor.isCanceled() ? Status.CANCEL_STATUS : Status.OK_STATUS;
	}

	/**
	 * Execute the operation to be perform by the Job.
	 * 
	 * @param monitor
	 *            Monitor
	 * @throws OperationCanceledException
	 */
	protected abstract void execute(IProgressMonitor monitor) throws OperationCanceledException;

	/**
	 * Execute the operation to be performed when the job is canceled.
	 */
	protected abstract void executeWhenCanceled();

	/**
	 * 
	 * @param monitor
	 */
	protected void checkCanceled(IProgressMonitor monitor) throws OperationCanceledException {
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}
}
