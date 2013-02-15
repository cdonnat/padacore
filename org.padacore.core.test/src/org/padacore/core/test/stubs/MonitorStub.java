package org.padacore.core.test.stubs;

import org.eclipse.core.runtime.IProgressMonitor;

public class MonitorStub implements IProgressMonitor {

	private boolean isCanceled;

	public MonitorStub() {
		this.isCanceled = false;
	}

	@Override
	public void beginTask(String name, int totalWork) {

	}

	@Override
	public void done() {

	}

	@Override
	public void internalWorked(double work) {

	}

	@Override
	public boolean isCanceled() {
		return this.isCanceled;
	}

	@Override
	public void setCanceled(boolean value) {
		this.isCanceled = value;
	}

	@Override
	public void setTaskName(String name) {

	}

	@Override
	public void subTask(String name) {

	}

	@Override
	public void worked(int work) {
	}

}
