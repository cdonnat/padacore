package org.padacore.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExternalProcessInfo {

	private static final DateFormat Format = new SimpleDateFormat("HH:mm:ss");
	private long startTimeInMs;
	private String processName;
	private IConsole console;

	public ExternalProcessInfo(String processName, IConsole console) {
		this.processName = processName;
		this.console = console;
	}

	public void start(String[] cmdWithArgs) {
		this.startTimeInMs = System.currentTimeMillis();
		StringBuilder cmd = new StringBuilder();
		for (int i = 0; i < cmdWithArgs.length; i++) {
			cmd.append(cmdWithArgs[i]);
			cmd.append(" ");
		}
		this.console.print(cmd.toString());
	}

	public void finish(boolean isSuccessful) {
		final double elapsedTimeInS = (System.currentTimeMillis() - startTimeInMs) / 1000.0;

		StringBuilder message = new StringBuilder(Format.format(new Date()));
		message.append(" ");
		message.append(processName);

		if (isSuccessful) {
			message.append(" successful in ");
		} else {
			message.append(" failed in ");
		}
		message.append(elapsedTimeInS);
		message.append(" seconds");

		this.console.print(message.toString());
	}
}
