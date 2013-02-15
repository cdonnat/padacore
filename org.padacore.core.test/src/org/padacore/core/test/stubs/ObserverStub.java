package org.padacore.core.test.stubs;

import java.util.Observable;
import java.util.Observer;

public class ObserverStub implements Observer {

	private String stringNotified = new String();

	@Override
	public void update(Observable arg0, Object arg1) {
		this.stringNotified = (String) arg1;
	}

	public String getStringNotified() {
		return this.stringNotified;
	}

}
