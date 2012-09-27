package org.padacore.builder;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class GprbuildObserver implements Observer {

	private static final String BUILDING_PROJECT = "Building project";

	private IProgressMonitor monitor;
	private SubMonitor subMonitor;
	private boolean monitorIsStarted;
	private GprbuildOutput outputParser;

	public GprbuildObserver(IProgressMonitor monitor) {
		this.monitorIsStarted = false;
		this.monitor = monitor;
		this.outputParser = new GprbuildOutput();
	}

	private void start() {
		monitorIsStarted = true;
		monitor.beginTask(BUILDING_PROJECT, outputParser.remainingFileToProcess());
		subMonitor = SubMonitor.convert(monitor, BUILDING_PROJECT, 100);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String line = (String) arg1;

		outputParser.evaluate(line);
		if (outputParser.lastEntryIndicatesProgress()) {
			if (!monitorIsStarted) {
				start();
			}
			subMonitor.setWorkRemaining(outputParser.remainingFileToProcess());
			subMonitor.worked(1);

			if (outputParser.remainingFileToProcess() == 0) {
				monitor.done();
			}
		}
		else {
			System.out.println(line);
		}
	}

}
