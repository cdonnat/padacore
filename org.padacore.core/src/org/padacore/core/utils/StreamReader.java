package org.padacore.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IStatus;

public class StreamReader extends Observable implements Runnable {

	private BufferedReader input;

	public StreamReader(InputStream input, Observer[] observers) {
		this.input = new BufferedReader(new InputStreamReader(input));
		for (int i = 0; i < observers.length; i++) {
			this.addObserver(observers[i]);
		}
	}

	@Override
	public void run() {
		String line;

		try {
			while ((line = this.input.readLine()) != null) {
				this.setChanged();
				this.notifyObservers(new String(line));
			}
		} catch (IOException e) {
			ErrorLog.appendException(e, IStatus.ERROR);
		} finally {
			try {
				this.input.close();
			} catch (IOException e) {
			}
		}
	}
}
