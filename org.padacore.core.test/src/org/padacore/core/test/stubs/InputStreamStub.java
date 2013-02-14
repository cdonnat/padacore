package org.padacore.core.test.stubs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamStub extends InputStream {

	ByteArrayInputStream inputStream;

	public void write(String s) {
		inputStream = new ByteArrayInputStream(s.getBytes());
	}

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
