package org.padacore.core.gnat.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.padacore.core.gnat.Symbol;

public class GprGrammarTest {

	@Test
	public void testName() {
		assertTrue(GprGrammarTestUtils.IsNameIdentified("simple_identifier"));
		assertTrue(GprGrammarTestUtils.IsNameIdentified("one_level.identifier"));
		assertTrue(GprGrammarTestUtils
				.IsNameIdentified("two.levels.identifier"));

		assertFalse(GprGrammarTestUtils
				.IsNameIdentified("two__nextToEachOther"));
	}

	@Test
	public void testEmptyDeclaration() {
		assertTrue(GprGrammarTestUtils.IsEmptyDeclaration("null;"));
		assertFalse("Missing semicolon",
				GprGrammarTestUtils.IsEmptyDeclaration("null"));
	}

	@Test
	public void testExpression() {
		assertTrue(GprGrammarTestUtils.IsExpressionIdentified(
				"\"simple_string\"", Symbol.CreateString("simple_string")));

		assertTrue("String & string",
				GprGrammarTestUtils.IsExpressionIdentified(
						"\"string\" & \"string\"",
						Symbol.CreateString("stringstring")));

		List<String> expStringList = new ArrayList<String>();
		expStringList.add("File_Name");
		assertTrue("List & string", GprGrammarTestUtils.IsExpressionIdentified(
				"() & \"File_Name\"", Symbol.CreateStringList(expStringList)));

		expStringList.clear();
		expStringList.add("File_Name");
		expStringList.add("File_Name.orig");
		assertTrue("List & list", GprGrammarTestUtils.IsExpressionIdentified(
				"() & \"File_Name\" & (\"File_Name\" & \".orig\")",
				Symbol.CreateStringList(expStringList)));
	}

	@Test
	public void testVariableDeclaration() {
		assertTrue(GprGrammarTestUtils
				.IsVariableDeclaration("That_Os := \"GNU/Linux\";"));

		assertTrue(GprGrammarTestUtils
				.IsVariableDeclaration("Name := \"readme.txt\";"));

		assertTrue(GprGrammarTestUtils
				.IsVariableDeclaration("Save_Name := \"name\" & \".saved\";"));

		assertFalse(GprGrammarTestUtils
				.IsVariableDeclaration("variable = \"missing_colon\";"));
		assertFalse(GprGrammarTestUtils
				.IsVariableDeclaration("variable := \"missing_semicolon\""));
		assertFalse(GprGrammarTestUtils
				.IsVariableDeclaration("typed_variable : type := \"GNU/Linux\";"));
	}

	@Test
	public void testTypedVariableDeclaration() {
		assertTrue(GprGrammarTestUtils
				.IsTypedVariableDeclarationIdentified("That_OS : OS := \"GNU/Linux\";"));

		assertTrue(GprGrammarTestUtils
				.IsTypedVariableDeclarationIdentified("This_OS : OS := external (\"OS\");"));

		assertFalse(GprGrammarTestUtils
				.IsTypedVariableDeclarationIdentified("That_OS : OS := \"Missing_Semicolon\""));
		assertFalse(GprGrammarTestUtils
				.IsTypedVariableDeclarationIdentified("Untyped_variable := \"GNU/Linux\";"));
	}

	@Test
	public void testStringList() {
		List<String> expStringList = new ArrayList<String>();
		expStringList.add("One_Element");
		assertTrue(GprGrammarTestUtils.isStringListIdentified(
				"(\"One_Element\")", Symbol.CreateStringList(expStringList)));

		expStringList.clear();
		expStringList.add("First");
		expStringList.add("Second");
		expStringList.add("Third");

		assertTrue(GprGrammarTestUtils.isStringListIdentified(
				"(\"First\", \"Second\", \"Third\"))",
				Symbol.CreateStringList(expStringList)));

		assertFalse(GprGrammarTestUtils.isStringListIdentified(
				"(\"Missing\" \"comma\")", Symbol.CreateString("")));
	}

	@Test
	public void testAttributeDeclarationWithSimpleAttributes() {

		assertTrue(GprGrammarTestUtils
				.isAttributeDeclaration("for Object_Dir use \"objects\";"));
		assertTrue(GprGrammarTestUtils
				.isAttributeDeclaration("for Source_Dirs use (\"units\", \"test/drivers\");)"));
	}

	@Test
	public void testAttributeDeclarationWithIndexedAttributes() {
		assertTrue(GprGrammarTestUtils
				.isAttributeDeclaration("for Body (\"main\") use \"Main.ada\";"));
		assertTrue(GprGrammarTestUtils
				.isAttributeDeclaration("for Switches (\"main.ada\") use (\"-v\", \"-gnatv\");"));
		assertTrue(GprGrammarTestUtils
				.isAttributeDeclaration("for Switches (\"main.ada\") use Builder'Switches (\"main.ada\") & \"-g\";"));
		assertTrue(GprGrammarTestUtils
				.isAttributeDeclaration("for Default_Switches use Default.Builder'Default_Switches;"));
	}

	@Test
	public void testAttributeReference() {
		assertTrue(GprGrammarTestUtils
				.isAttributeReference("project'Object_Dir"));
		assertTrue(GprGrammarTestUtils
				.isAttributeReference("Naming'Dot_Replacement"));
		assertTrue(GprGrammarTestUtils
				.isAttributeReference("Imported_Project'Source_Dirs"));
		assertTrue(GprGrammarTestUtils
				.isAttributeReference("Imported_Project.Naming'Casing"));
		assertTrue(GprGrammarTestUtils
				.isAttributeReference("Builder'Default_Switches (\"Ada\")"));
	}

	@Test
	public void testProjectDeclaration() {
		assertTrue(GprGrammarTestUtils
				.isProjectDeclaration("project Empty is    \n\r  end Empty;"));

		assertFalse(GprGrammarTestUtils
				.isProjectDeclaration("project Missing_Semicolon is end Missing_Semicolon"));
		assertFalse(GprGrammarTestUtils
				.isProjectDeclaration("project Missing_End is Missing_End;"));
		assertFalse(GprGrammarTestUtils
				.isProjectDeclaration("project First_Name is	end Second_Name;"));
		assertFalse(GprGrammarTestUtils
				.isProjectDeclaration("project My_Proj is	my_var : my_type := \"Value\"; my_var : my_type := \"New_Value\"; end My_Proj;"));
	}

	@Test
	public void testExternalValue() {
		assertTrue(GprGrammarTestUtils
				.isExternalValue("external (\"Variable\")"));

		assertTrue(GprGrammarTestUtils
				.isExternalValue("external (\"Variable\", \"Default_Value\")"));
	}

	@Test
	public void testWithClause() {
		assertTrue(GprGrammarTestUtils
				.isWithClause("with \"../../my_project.gpr\";"));
		assertTrue(GprGrammarTestUtils
				.isWithClause("with \"..\\..\\my_project_windows.gpr\", \"../my_project_linux.gpr\";"));

		assertFalse(GprGrammarTestUtils
				.isWithClause("with \"../../missing_semicolon.gpr\""));
	}

	@Test
	public void testContextClause() {
		assertTrue(GprGrammarTestUtils.isContextClause("\"\""));
		assertTrue(GprGrammarTestUtils
				.isContextClause("with \"../../my_project.gpr\"; with \"..\\..\\my_project_windows.gpr\";"));
	}

	@Test
	public void testCaseStatement() {
		assertTrue(GprGrammarTestUtils
				.isCaseStatement("case OS is "
						+ "when \"GNU/Linux\" | \"Unix\" => for Switches (\"Ada\") use (\"-gnath\"); "
						+ "when \"NT\" => for Switches (\"Ada\") use (\"-gnatP\"); "
						+ "when others => null; " + "end case;"));

		assertFalse(GprGrammarTestUtils
				.isCaseStatement("case OS is"
						+ " when \"GNU/Linux\" | \"Unix\" => for Switches (\"Ada\") use (\"-gnath\");"
						+ "when \"NT\" => for Switches (\"Ada\") use (\"-gnatP\"); "
						+ "when others => null; " + "end;"));
	}

	@Test
	public void testTypedStringDeclaration() {
		assertTrue(GprGrammarTestUtils
				.isTypedStringDeclaration("type OS is (\"NT\", \"nt\", \"Unix\", \"GNU/Linux\", \"other OS\");"));
	}

	@Test
	public void testPackageDeclaration() {
		assertTrue(GprGrammarTestUtils
				.isPackageDeclaration("package Compiler renames Logging.Compiler;"));
	}

	@Test
	public void testProject() {
		assertTrue(GprGrammarTestUtils
				.isProject("with \"../../included.gpr\"; project My_Project is end My_Project;"));
	}
}
