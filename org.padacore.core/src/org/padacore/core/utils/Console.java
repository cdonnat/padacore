package org.padacore.core.utils;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.padacore.core.Activator;

public class Console implements org.padacore.core.utils.IConsole {

	private MessageConsole messageConsole;

	public Console() {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = consoleManager.getConsoles();

		for (int i = 0; i < consoles.length; i++) {
			if (consoles[i].getName().equals(Activator.PLUGIN_ID)) {
				messageConsole = (MessageConsole) consoles[i];
				break;
			}
		}

		if (messageConsole == null) {
			messageConsole = new MessageConsole(Activator.PLUGIN_ID, null);
			consoleManager.addConsoles(new IConsole[] { messageConsole });
		}
	}

	/**
	 * Display the given message in the MessageConsole of the plugin.
	 */
	@Override
	public void print(String message) {
		MessageConsoleStream out = this.messageConsole.newMessageStream();
		out.println(message);
	}
}
