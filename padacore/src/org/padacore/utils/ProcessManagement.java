package org.padacore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ProcessManagement {
	
	private static final class ReaderThread implements Runnable {
		private BufferedReader reader;
		private PrintStream currentOutputStream;

		public ReaderThread(BufferedReader reader, PrintStream currentOutputStream) {
			this.reader = reader;
			this.currentOutputStream = currentOutputStream;
		}
		
		@Override
		public void run() {
			String errorLine;

			try {
				while ((errorLine = this.reader.readLine()) != null) {
					this.currentOutputStream.println(errorLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					this.reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Redirects the error output stream of the given process to the current error output stream. 
	 * 
	 * @param process the process for which the error output stream is redirected.
	 */
	public static void displayErrors(Process process) {

		final BufferedReader error = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));

		Thread errorReader = new Thread(new ReaderThread(error, System.err));		
		errorReader.start();
	}

	/**
	 * Redirects the standard output stream of the given process to the current standard output stream. 
	 * 
	 * @param process the process for which the standard output stream is redirected.
	 */
	public static void displayWarnings(Process process) throws IOException {
		BufferedReader output = new BufferedReader(new InputStreamReader(
				process.getInputStream()));

		Thread standardOutputReader = new Thread(new ReaderThread(output, System.out));		
		standardOutputReader.start();
	}
	
	/**
	 * Executes an external command and displays its standard / error output
	 * streams in current standard / output streams.
	 * 
	 * @param cmdWithArgs
	 *            the command to execute followed by its arguments.
	 */
	public static void executeExternalCommandWithArgs(String[] cmdWithArgs) {
		ProcessBuilder processBuilder = new ProcessBuilder(cmdWithArgs);

		try {
			Process process = processBuilder.start();
			
			for (int arg = 0; arg < cmdWithArgs.length; arg++) {
				System.out.print(cmdWithArgs[arg] + " ");
			}
			System.out.println();
			
			
			ProcessManagement.displayErrors(process);
			ProcessManagement.displayWarnings(process);
		} catch (IOException e) {
			System.err.println(cmdWithArgs[0] + " execution failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes an external command and displays its standard / error output
	 * streams in current standard / output streams.
	 * 
	 * @param cmdName the command to execute.
	 */
	public static void executeExternalCommand(String cmdName) {
		String[] cmdWithNoArgs = new String[]{ cmdName };

		ProcessManagement.executeExternalCommandWithArgs(cmdWithNoArgs);
	}
}
