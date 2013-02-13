package org.padacore.core.builder;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.SubMonitor;

public class GprbuildObserver implements Observer {

	private SubMonitor subMonitor;
	private GprbuildOutput outputParser;

	public GprbuildObserver(SubMonitor subMonitor) {
		this.outputParser = new GprbuildOutput();
		this.subMonitor = subMonitor;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String line = (String) arg1;

		this.outputParser.evaluate(line);
		if (this.outputParser.lastEntryIndicatesProgress()) {
			subMonitor.setWorkRemaining(outputParser.nbRemainingFilesToProcess());
			subMonitor.worked(1);
		}
	}

}
