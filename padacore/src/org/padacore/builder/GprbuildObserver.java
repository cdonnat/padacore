package org.padacore.builder;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;

public class GprbuildObserver implements Observer {

	private static final String BUILDING_PROJECT = "Building project";

	private IProgressMonitor monitor;
	private boolean monitorIsStarted;
	private GprbuildOutput outputParser;

	public GprbuildObserver(IProgressMonitor monitor) {
		this.monitor = monitor;
		this.monitorIsStarted = false;
		this.outputParser = new GprbuildOutput();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String line = (String) arg1;

		this.outputParser.evaluate(line);
		if (this.outputParser.lastEntryIndicatesProgress()) {
			if (!this.monitorIsStarted) {
				this.monitorIsStarted = true;
				this.monitor.beginTask(BUILDING_PROJECT, this.outputParser.total() );
			}
			this.monitor.worked(1);
		}

		System.out.println(line);
	}

}
