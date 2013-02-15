package org.padacore.core.test.stubs;

import org.padacore.core.utils.IConsole;

public class ConsoleStub implements IConsole {

	private String lastMessage;

	@Override
	public void print(String message) {
		this.lastMessage = message;
	}

	public String lastMessage() {
		return this.lastMessage;
	}
}
