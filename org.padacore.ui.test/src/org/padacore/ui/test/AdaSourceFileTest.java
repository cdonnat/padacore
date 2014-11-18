package org.padacore.ui.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.padacore.ui.AdaSourceFile;

public class AdaSourceFileTest {

	private AdaSourceFile sut;

	private class AdaSourceFileImpl extends AdaSourceFile {

		@Override
		public String getExtension() {
			return ".ext@invalid";
		}

		@Override
		public String getFileTypeDescription() {
			return "Ada source file for test";
		}

		@Override
		public InputStream getTemplate() {
			return new ByteArrayInputStream("This is only a test".getBytes());
		}
	}

	private String addExtension(String rootOfFileName) {
		return rootOfFileName.concat(".").concat(this.sut.getExtension());
	}

	@Before
	public void createFixture() {
		this.sut = new AdaSourceFileImpl();
	}

	@Test
	public void checkNonAlphabeticFileNameIsInvalid() {
		String nonAlphaNumeric = this.addExtension("file123@");

		assertFalse("Non alphanumeric file name is invalid",
				this.sut.isFileNameValid(nonAlphaNumeric));

		String alphaNumericWithDot = this.addExtension("file123.toto");
		assertFalse("File name which contains dot is invalid",
				this.sut.isFileNameValid(alphaNumericWithDot));
	}

	@Test
	public void checkFileNameWithLeadingUnderscoreIsInvalid() {
		String leadingUnderscore = this.addExtension("_file");

		assertFalse("File name with leading underscore is invalid",
				this.sut.isFileNameValid(leadingUnderscore));
	}

	@Test
	public void checkFileNameWithLeadingDashIsInvalid() {
		String leadingDash = this.addExtension("-file");

		assertFalse("File name with leading dash is invalid",
				this.sut.isFileNameValid(leadingDash));
	}

	@Test
	public void checkFileNameWithLeadingDigitIsInvalid() {
		String leadingDigit = this.addExtension("2file");
		assertFalse("File name with leading digit is invalid",
				this.sut.isFileNameValid(leadingDigit));
	}

	@Test
	public void checkFileNameWithEndingUnderscoreIsInvalid() {
		String endingUnderscore = this.addExtension("file_");
		assertFalse("File name with ending underscore is invalid",
				this.sut.isFileNameValid(endingUnderscore));
	}

	@Test
	public void checkFileNameWithConsecutiveUnderscores() {
		String consecutiveUnderscores = this.addExtension("fil__e");
		assertFalse("File name with consecutive underscores is invalid",
				this.sut.isFileNameValid(consecutiveUnderscores));
	}

	@Test
	public void checkValidFileName() {
		String alphaNumWithDashAndUnderscore = this
				.addExtension("package-file_impl");

		assertTrue(
				"File name with alphanumeric characters, underscores and dashes is valid",
				this.sut.isFileNameValid(alphaNumWithDashAndUnderscore));
	}

}
