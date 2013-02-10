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
	public void testSimpleName() {
		assertTrue(GprGrammarTestUtils.IsSimpleName("i"));
		assertTrue(GprGrammarTestUtils.IsSimpleName("identifier"));
		assertTrue(GprGrammarTestUtils.IsSimpleName("identifier"));
		assertTrue(GprGrammarTestUtils.IsSimpleName("identi_f_1er"));
		assertTrue(GprGrammarTestUtils.IsSimpleName("identi_f_1er"));

		assertFalse("Unauthorized character",
				GprGrammarTestUtils.IsSimpleName("identif*er"));
		assertFalse("Starts with a digit",
				GprGrammarTestUtils.IsSimpleName("1dentifier"));
		assertFalse("Starts with an underscore",
				GprGrammarTestUtils.IsSimpleName("_dentifier"));
		assertFalse("Two underscores next to each other",
				GprGrammarTestUtils.IsSimpleName("identif__er"));

		//FIXME fails with a NullPointerException
//		assertFalse("Ends with an underscore",
//				GprGrammarTestUtils.IsSimpleName("identifier_"));

	}

	@Test
	public void testEmptyDeclaration() {
		assertTrue(GprGrammarTestUtils.IsEmptyDeclaration("null;"));
		assertFalse("Missing semicolon",
				GprGrammarTestUtils.IsEmptyDeclaration("null"));
	}

	@Test
	public void testPathName() {
		assertTrue(GprGrammarTestUtils.IsPathNameIdentified(
				"\"project_wo_extension\"", "project_wo_extension"));
		assertTrue(GprGrammarTestUtils.IsPathNameIdentified(
				"\"project_w_extension.gpr\"", "project_w_extension.gpr"));
		assertTrue(GprGrammarTestUtils.IsPathNameIdentified(
				"\".\\windows_project.gpr\"", ".\\windows_project.gpr"));
		assertTrue(GprGrammarTestUtils.IsPathNameIdentified(
				"\"/home/rs/linux_project.gpr\"", "/home/rs/linux_project.gpr"));

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
	public void testStringExpression() {
		assertTrue(
				"String literal",
				GprGrammarTestUtils.IsStringExpressionIdentified("\"Toto\"",
						Symbol.CreateString("Toto")));
		
		assertTrue(
				"Name",
				GprGrammarTestUtils.IsStringExpressionIdentified("My.Name;",
						Symbol.CreateString("")));
		
		assertTrue(
				"External value",
				GprGrammarTestUtils.IsStringExpressionIdentified("external(\"External\", \"default\"));",
						Symbol.CreateString("default")));
		
		assertTrue(
				"Attribute reference",
				GprGrammarTestUtils.IsStringExpressionIdentified("project'Source_Dir;",
						Symbol.CreateString("")));
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
				.IsTypedVariableDeclaration("That_OS : OS := \"GNU/Linux\";"));

		assertTrue(GprGrammarTestUtils
				.IsTypedVariableDeclaration("This_OS : OS := external (\"OS\");"));

		assertFalse(GprGrammarTestUtils
				.IsTypedVariableDeclaration("That_OS : OS := \"Missing_Semicolon\""));
		assertFalse(GprGrammarTestUtils
				.IsTypedVariableDeclaration("Untyped_variable := \"GNU/Linux\";"));
	}

	@Test
	public void testDeclarativeItem() {
		assertTrue(
				"Simple declarative item",
				GprGrammarTestUtils
						.IsDeclarativeItem("Untyped_variable := \"GNU/Linux\";"));
		assertTrue(
				"Typed string declaration",
				GprGrammarTestUtils
						.IsDeclarativeItem("type OS is (\"NT\", \"nt\", \"Unix\", \"GNU/Linux\", \"other OS\");"));
		assertTrue(
				"Package declaration",
				GprGrammarTestUtils
						.IsDeclarativeItem("package Compiler renames Logging.Compiler;"));
	}

	@Test
	public void testSimpleDeclarativeItem() {
		assertTrue(
				"Typed variable declaration",
				GprGrammarTestUtils
						.IsSimpleDeclarativeItem("Typed_variable : OS := \"GNU/Linux\";"));
		assertTrue(
				"Variable declaration",
				GprGrammarTestUtils
						.IsSimpleDeclarativeItem("Untyped_variable := \"GNU/Linux\";"));
		assertTrue(
				"Attribute declaration",
				GprGrammarTestUtils
						.IsSimpleDeclarativeItem("for Object_Dir use \"objects\";"));
		assertTrue(
				"Case statement",
				GprGrammarTestUtils
						.IsSimpleDeclarativeItem("case OS is "
								+ "when \"GNU/Linux\" | \"Unix\" => for Switches (\"Ada\") use (\"-gnath\"); "
								+ "when \"NT\" => for Switches (\"Ada\") use (\"-gnatP\"); "
								+ "when others => null; " + "end case;"));
		assertTrue("Empty declaration",
				GprGrammarTestUtils.IsSimpleDeclarativeItem("null;"));
	}

	@Test
	public void testTerm() {
		assertTrue(
				"String expression",
				GprGrammarTestUtils.IsTermIdentified("\"Toto\"",
						Symbol.CreateString("Toto")));

		List<String> expStringList = new ArrayList<String>();
		expStringList.add("Toto");
		expStringList.add("Titi");
		assertTrue("String list", GprGrammarTestUtils.IsTermIdentified(
				"(\"Toto\", \"Titi\")", Symbol.CreateStringList(expStringList)));
	}

	@Test
	public void testStringList() {
		List<String> expStringList = new ArrayList<String>();
		expStringList.add("One_Element");
		assertTrue(GprGrammarTestUtils.IsStringListIdentified(
				"(\"One_Element\")", Symbol.CreateStringList(expStringList)));

		expStringList.clear();
		expStringList.add("First");
		expStringList.add("Second");
		expStringList.add("Third");

		assertTrue(GprGrammarTestUtils.IsStringListIdentified(
				"(\"First\", \"Second\", \"Third\"))",
				Symbol.CreateStringList(expStringList)));

		assertFalse(GprGrammarTestUtils.IsStringListIdentified(
				"(\"Missing\" \"comma\")", Symbol.CreateString("")));
	}

	@Test
	public void testAttributeDeclarationWithSimpleAttributes() {

		assertTrue(GprGrammarTestUtils
				.IsAttributeDeclaration("for Object_Dir use \"objects\";"));
		assertTrue(GprGrammarTestUtils
				.IsAttributeDeclaration("for Source_Dirs use (\"units\", \"test/drivers\");)"));
	}

	@Test
	public void testAttributeDeclarationWithIndexedAttributes() {
		assertTrue(GprGrammarTestUtils
				.IsAttributeDeclaration("for Body (\"main\") use \"Main.ada\";"));
		assertTrue(GprGrammarTestUtils
				.IsAttributeDeclaration("for Switches (\"main.ada\") use (\"-v\", \"-gnatv\");"));
		assertTrue(GprGrammarTestUtils
				.IsAttributeDeclaration("for Switches (\"main.ada\") use Builder'Switches (\"main.ada\") & \"-g\";"));
		assertTrue(GprGrammarTestUtils
				.IsAttributeDeclaration("for Default_Switches use Default.Builder'Default_Switches;"));
	}

	@Test
	public void testAttributeReference() {
		assertTrue(GprGrammarTestUtils
				.IsAttributeReference("project'Object_Dir"));
		assertTrue(GprGrammarTestUtils
				.IsAttributeReference("Builder'Default_Switches (\"Ada\")"));
	}

	@Test
	public void testAttributePrefix() {
		assertTrue(GprGrammarTestUtils.IsAttributePrefix("project'Object_Dir",
				""));
		assertTrue(GprGrammarTestUtils.IsAttributePrefix(
				"Imported_Project_Or_Package", "Imported_Project_Or_Package"));
		assertTrue(GprGrammarTestUtils.IsAttributePrefix(
				"Imported_Project.Naming", "Imported_Project.Naming"));

		assertFalse("More than 2 levels",
				GprGrammarTestUtils.IsAttributePrefix("One.Two.Three",
						"One.Two.Three"));

	}

	@Test
	public void testSimpleProjectDeclaration() {
		assertTrue(GprGrammarTestUtils
				.IsSimpleProjectDeclaration("project Empty is    \n\r  end Empty;"));

		assertFalse(GprGrammarTestUtils
				.IsSimpleProjectDeclaration("project Missing_Semicolon is end Missing_Semicolon"));
		assertFalse(GprGrammarTestUtils
				.IsSimpleProjectDeclaration("project Missing_End is Missing_End;"));
		assertFalse(GprGrammarTestUtils
				.IsSimpleProjectDeclaration("project First_Name is	end Second_Name;"));
		assertFalse(GprGrammarTestUtils
				.IsSimpleProjectDeclaration("project My_Proj is	my_var : my_type := \"Value\"; my_var : my_type := \"New_Value\"; end My_Proj;"));
	}

	@Test
	public void testProjectDeclaration() {
		assertTrue(GprGrammarTestUtils
				.IsProjectDeclaration("project Empty is    \n\r  end Empty;"));
	}

	@Test
	public void testExternalValue() {
		assertTrue(GprGrammarTestUtils
				.IsExternalValue("external (\"Variable\")"));

		assertTrue(GprGrammarTestUtils
				.IsExternalValue("external (\"Variable\", \"Default_Value\")"));
	}

	@Test
	public void testWithClause() {
		assertTrue(GprGrammarTestUtils
				.IsWithClause("with \"../../my_project.gpr\";"));
		assertTrue(GprGrammarTestUtils
				.IsWithClause("with \"..\\..\\my_project_windows.gpr\", \"../my_project_linux.gpr\";"));

		assertFalse(GprGrammarTestUtils
				.IsWithClause("with \"../../missing_semicolon.gpr\""));
	}

	@Test
	public void testContextClause() {
		assertTrue(GprGrammarTestUtils.IsContextClause("\"\""));
		assertTrue(GprGrammarTestUtils
				.IsContextClause("with \"../../my_project.gpr\"; with \"..\\..\\my_project_windows.gpr\";"));
	}

	@Test
	public void testCaseStatement() {
		assertTrue(GprGrammarTestUtils
				.IsCaseStatement("case OS is "
						+ "when \"GNU/Linux\" | \"Unix\" => for Switches (\"Ada\") use (\"-gnath\"); "
						+ "when \"NT\" => for Switches (\"Ada\") use (\"-gnatP\"); "
						+ "when others => null; " + "end case;"));

		assertFalse(
				"Missing end case",
				GprGrammarTestUtils
						.IsCaseStatement("case OS is"
								+ " when \"GNU/Linux\" | \"Unix\" => for Switches (\"Ada\") use (\"-gnath\");"
								+ "when \"NT\" => for Switches (\"Ada\") use (\"-gnatP\"); "
								+ "when others => null; " + "end;"));
	}

	@Test
	public void testCaseItem() {
		assertTrue("Empty declaration",
				GprGrammarTestUtils.IsCaseItem("when others => null;"));
		assertTrue(
				"Attribute declaration",
				GprGrammarTestUtils
						.IsCaseItem("when others => for Switches (\"Ada\") use (\"-gnath\");"));
		assertTrue("Variable declaration",
				GprGrammarTestUtils
						.IsCaseItem("when others => My_Var := \"Toto\";"));
		assertTrue(
				"Case statement",
				GprGrammarTestUtils
						.IsCaseItem("when others => case toto is when others => null; end CASE;"));

		assertTrue("Empty_declaration + variable declaration",
				GprGrammarTestUtils
						.IsCaseItem("when others => My_Var := \"Titi\"; null;"));

		assertFalse("Missing arrow",
				GprGrammarTestUtils.IsCaseItem("when others null;"));

	}

	@Test
	public void testDiscreteChoiceList() {
		assertTrue(GprGrammarTestUtils
				.IsDiscreteChoiceList("\"simple_choice\""));
		assertTrue(GprGrammarTestUtils
				.IsDiscreteChoiceList("\"1st_choice\" | \"2d_choice\" | \"3rd_choice\""));
		assertTrue(GprGrammarTestUtils.IsDiscreteChoiceList("others"));
		assertTrue(GprGrammarTestUtils.IsDiscreteChoiceList("OTHERS"));

		assertFalse(GprGrammarTestUtils.IsDiscreteChoiceList("not_a_string"));
	}

	@Test
	public void testTypedStringDeclaration() {
		assertTrue(GprGrammarTestUtils
				.IsTypedStringDeclaration("type OS is (\"NT\", \"nt\", \"Unix\", \"GNU/Linux\", \"other OS\");"));
	}

	@Test
	public void testPackageDeclaration() {
		assertTrue(
				"Package spec",
				GprGrammarTestUtils
						.IsPackageDeclaration("package Compiler is end Compiler;"));

		assertTrue(
				"Package renaming",
				GprGrammarTestUtils
						.IsPackageDeclaration("package Compiler renames Logging.Compiler;"));

		assertTrue(
				"Package extension",
				GprGrammarTestUtils
						.IsPackageDeclaration("package Compiler extends Shared.Compiler is end Compiler;"));
	}

	@Test
	public void testPackageSpec() {
		assertTrue(GprGrammarTestUtils.IsPackageSpec("package Compiler is "
				+ "null;" + "My_Var := \"Toto\";" + "end Compiler;"));

		assertFalse("Package name is different in begin and end",
				GprGrammarTestUtils
						.IsPackageSpec("package Compiler is end Binder;"));
	}

	@Test
	public void testPackageRenaming() {
		assertTrue(GprGrammarTestUtils
				.IsPackageRenaming("package Compiler renames Shared.Compiler;"));

		assertFalse(
				"Multiple levels",
				GprGrammarTestUtils
						.IsPackageRenaming("package Compiler renames First_Level.Second_Level.Compiler;"));
	}

	@Test
	public void testPackageExtension() {
		assertTrue(GprGrammarTestUtils
				.IsPackageExtension("package Compiler extends Shared.Compiler is "
						+ "null; " + "My_Var := \"Toto\";" + "end Compiler;"));

		assertFalse(
				"Package name is different in begin and end",
				GprGrammarTestUtils
						.IsPackageExtension("package Compiler extends Shared.Compiler is "
								+ "end Binder;"));
	}

	@Test
	public void testProject() {
		assertTrue(GprGrammarTestUtils
				.IsProject("with \"../../included.gpr\"; project My_Project is end My_Project;"));
	}
}
