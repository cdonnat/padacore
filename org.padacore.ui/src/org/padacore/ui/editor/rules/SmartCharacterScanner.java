package org.padacore.ui.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;

public class SmartCharacterScanner {

	private ICharacterScanner scanner;
	private int nbReadCalled;

	public SmartCharacterScanner(ICharacterScanner scanner) {
		this.scanner = scanner;
		this.nbReadCalled = 0;
	}

	public int read() {
		this.nbReadCalled++;
		return this.scanner.read();
	}

	public void reset() {
		for (int i = 0; i < this.nbReadCalled; i++) {
			this.scanner.unread();
		}
	}
}
