package org.padacore.core.utils;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.padacore.core.Activator;

public class Console {

	private static MessageConsole console = null;

	/**
	 * Lazy init to create or retrieve the MessageConsole of the plugin.
	 * @return The MessageConsole of the plugin.
	 */
	private static MessageConsole Get() {
		if (console == null) {
			IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
			IConsole[] consoles = consoleManager.getConsoles();

			for (int i = 0; i < consoles.length; i++) {
				if (consoles[i].getName().equals(Activator.PLUGIN_ID)) {
					console = (MessageConsole) consoles[i];
					break;
				}
			}

			if (console == null) {
				console = new MessageConsole(Activator.PLUGIN_ID, null);
				consoleManager.addConsoles(new IConsole[] { console });
			}
		}
		return console;
	}

	/**
	 * Display the given message in the MessageConsole of the plugin.
	 */
	public static void Print(String message) {
		MessageConsoleStream out = Get().newMessageStream();
		out.println(message);
	}
}
