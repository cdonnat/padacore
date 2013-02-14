package org.padacore.core.utils;

import java.util.Observable;
import java.util.Observer;

public class ExternalProcessOutput implements Observer {

	private IConsole console;

	public ExternalProcessOutput(IConsole console) {
		this.console = console;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.console.print((String) arg1);
	}
}
