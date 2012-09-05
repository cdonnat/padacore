package org.padacore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ProcessDisplay {
	
	static final class ReaderThread implements Runnable {
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
	static public void DisplayErrors(Process process) {

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
	static public void DisplayWarnings(Process process) throws IOException {
		BufferedReader output = new BufferedReader(new InputStreamReader(
				process.getInputStream()));

		Thread standardOutputReader = new Thread(new ReaderThread(output, System.out));		
		standardOutputReader.start();
	}
}
