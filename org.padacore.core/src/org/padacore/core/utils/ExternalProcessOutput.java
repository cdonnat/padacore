package org.padacore.core.utils;

import java.util.Observable;
import java.util.Observer;

public class ExternalProcessOutput implements Observer {

	public ExternalProcessOutput(String name) {
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String line = (String) arg1;
		Console.Print(line);
	}
}
