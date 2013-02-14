package org.padacore.core.test.stubs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.padacore.core.utils.IExternalProcess;

public class ExternalProcessStub implements IExternalProcess {

	private boolean isStopCalled;
	private boolean isFinished;

	public ExternalProcessStub() {
		this.reset();
	}

	@Override
	public void run(String[] cmdWithArgs, IProgressMonitor monitor) {

	}

	@Override
	public void stop() {
		this.isStopCalled = true;
	}

	@Override
	public boolean isFinished() {
		return this.isFinished;
	}

	public boolean isStopCalled() {
		return this.isStopCalled;
	}
	
	public void reset() {
		this.isFinished = false;
		this.isStopCalled = false;
	}

	public void setIsFinished(boolean flag) {
		this.isFinished = flag;
	}
}
