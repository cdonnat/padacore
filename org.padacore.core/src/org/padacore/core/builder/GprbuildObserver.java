package org.padacore.core.builder;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.SubMonitor;
import org.padacore.core.utils.IConsole;

public class GprbuildObserver implements Observer {

	private SubMonitor subMonitor;
	private GprbuildOutput outputParser;
	private IConsole console;

	public GprbuildObserver(SubMonitor subMonitor, IConsole console) {
		this.outputParser = new GprbuildOutput();
		this.subMonitor = subMonitor;
		this.console = console;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String line = (String) arg1;

		this.outputParser.evaluate(line);
		if (this.outputParser.lastEntryIndicatesProgress()) {
			this.subMonitor.setWorkRemaining(this.outputParser.nbRemainingFilesToProcess());
			this.subMonitor.worked(1);
		}
		else {
			this.console.print(line);
		}
	}

}
