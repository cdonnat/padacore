package org.padacore.core.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.padacore.core.project.AdaProjectNature;
import org.padacore.core.project.ProjectBuilder;

public class ProjectDescriptionUtils {

	public static void CheckProjectContainsAdaNature(IProject project) {
		try {
			IProjectDescription desc = project.getDescription();
			assertEquals("Project shall contain one nature", 1,
					desc.getNatureIds().length);
			assertTrue("Project natures shall contain adaProjectNature",
					desc.hasNature(AdaProjectNature.NATURE_ID));
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private static boolean ContentsAreIdentical(Reader first, Reader second)
			throws IOException {
		BufferedReader firstReader = new BufferedReader(first);
		BufferedReader secondReader = new BufferedReader(second);
		String lineFromFirstReader;
		String lineFromSecondReader;
		boolean identicalContents = true;

		while ((lineFromFirstReader = firstReader.readLine()) != null
				&& (lineFromSecondReader = secondReader.readLine()) != null
				&& identicalContents) {
			identicalContents = lineFromFirstReader
					.equals(lineFromSecondReader);
		}

		identicalContents = identicalContents && firstReader.readLine() == null
				&& secondReader.readLine() == null;

		return identicalContents;
	}

	public static void CheckMainProcedureHasBeenAddedWithCorrectContents(
			IProject project, String projectLocation) {
		IPath mainProcedurePath = new Path(projectLocation + IPath.SEPARATOR
				+ ProjectBuilder.DEFAULT_EXECUTABLE_NAME);
		File mainProcedure = new File(mainProcedurePath.toOSString());
		StringBuilder theoreticalMainContents = null;

		assertTrue("Main procedure shall be added", mainProcedure.exists());

		FileReader realMainReader;
		try {
			realMainReader = new FileReader(mainProcedure);

			theoreticalMainContents = new StringBuilder();
			theoreticalMainContents.append("with GNAT.IO;\n");
			theoreticalMainContents.append("procedure Main is\n");
			theoreticalMainContents.append("begin\n");
			theoreticalMainContents
					.append("GNAT.IO.Put_Line(\"Hello world\");\n");
			theoreticalMainContents.append("end Main;");

			StringReader theoreticalMainReader = new StringReader(
					theoreticalMainContents.toString());

			assertTrue("Main procedure contents shall be correct",
					ContentsAreIdentical(realMainReader, theoreticalMainReader));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
