package org.padacore.core.gnat.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.padacore.core.gnat.GprParser;
import org.padacore.core.gnat.Symbol;
import org.padacore.core.test.stubs.GprLoaderStub;

public class GprGrammarTest {

	private GprGrammarFixture fixture;

	private boolean doesInputComplyWithParserRuleChecker(String input,
			SimpleParserRuleChecker ruleChecker) {
		this.createFixture(input);

		return ruleChecker.isInputRecognizedByParserRule(this.fixture);
	}

	private boolean doesInputComplyWithParserRuleChecker(String input,
			SymbolParserRuleChecker ruleChecker, Symbol expectedSymbol) {
		this.createFixture(input);

		return ruleChecker.isInputRecognizedByParserRule(this.fixture,
				expectedSymbol);
	}

	private abstract class SimpleParserRuleChecker {

		private boolean isInputRecognizedByParserRule(GprGrammarFixture fixture) {
			try {
				this.executeParserRule(fixture);
			} catch (RecognitionException e) {
				e.printStackTrace();
			}

			return GprGrammarTestUtils.NoRecognitionExceptionOccurred(
					fixture.parser, fixture.lexer);
		}

		protected abstract void executeParserRule(GprGrammarFixture fixture)
				throws RecognitionException;
	}

	private abstract class SymbolParserRuleChecker {

		private boolean areSymbolsIdentical(Symbol left, Symbol right) {
			boolean areEqual;

			if (left.isAString() && right.isAString()) {
				areEqual = left.getAsString().equals(right.getAsString());
			} else {
				areEqual = !left.isAString()
						&& !right.isAString()
						&& left.getAsStringList().equals(
								right.getAsStringList());
			}

			return areEqual;
		}

		private boolean isInputRecognizedByParserRule(
				GprGrammarFixture fixture, Symbol expectedSymbol) {
			Symbol result = null;

			try {
				result = this.executeParserRule(fixture);
			} catch (RecognitionException e) {
				e.printStackTrace();
			}

			return GprGrammarTestUtils.NoRecognitionExceptionOccurred(
					fixture.parser, fixture.lexer)
					&& this.areSymbolsIdentical(result, expectedSymbol);
		}

		protected abstract Symbol executeParserRule(GprGrammarFixture fixture)
				throws RecognitionException;
	}

	private void commonCreateFixture() {
		this.fixture = new GprGrammarFixture();
		this.fixture.loader = new GprLoaderStub();
	}

	private void createFixture(String testString) {
		try {
			this.commonCreateFixture();
			this.fixture.lexer = GprGrammarTestUtils.CreateLexer(testString);
			this.fixture.parser = GprGrammarTestUtils.CreateParser(
					this.fixture.lexer, this.fixture.loader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isNameIdentified(String inputName) {
		GprParser.name_return ruleReturn = new GprParser.name_return();
		this.createFixture(inputName);

		try {
			ruleReturn = this.fixture.parser.name();
		} catch (RecognitionException e) {

		}

		return GprGrammarTestUtils.NoRecognitionExceptionOccurred(
				this.fixture.parser, this.fixture.lexer)
				&& ruleReturn.result.equals(inputName);
	}

	@Test
	public void testName() {
		assertTrue(isNameIdentified("simple_identifier"));
		assertTrue(isNameIdentified("one_level.identifier"));
		assertTrue(isNameIdentified("two.levels.identifier"));

		assertFalse(isNameIdentified("two__nextToEachOther"));
	}

	private boolean isEmptyDeclaration(String input) {
		SimpleParserRuleChecker emptyDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.empty_declaration();
			}
		};

		return this
				.doesInputComplyWithParserRuleChecker(input, emptyDecChecker);

	}

	@Test
	public void testEmptyDeclaration() {
		assertTrue(isEmptyDeclaration("null;"));
		assertFalse("Missing semicolon", isEmptyDeclaration("null"));
	}

	private boolean isExpressionIdentified(String input, Symbol expectedSymbol) {
		SymbolParserRuleChecker expressionChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.expression();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				expressionChecker, expectedSymbol);
	}

	@Test
	public void testExpression() {
		assertTrue(isExpressionIdentified("\"simple_string\"",
				Symbol.CreateString("simple_string")));

		assertTrue(
				"String & string",
				isExpressionIdentified("\"string\" & \"string\"",
						Symbol.CreateString("stringstring")));

		List<String> expStringList = new ArrayList<String>();
		expStringList.add("File_Name");
		assertTrue(
				"List & string",
				isExpressionIdentified("() & \"File_Name\"",
						Symbol.CreateStringList(expStringList)));

		expStringList.clear();
		expStringList.add("File_Name");
		expStringList.add("File_Name.orig");
		assertTrue(
				"List & list",
				isExpressionIdentified(
						"() & \"File_Name\" & (\"File_Name\" & \".orig\")",
						Symbol.CreateStringList(expStringList)));
	}

	private boolean isVariableDeclaration(String input) {
		SimpleParserRuleChecker varDec = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.variable_declaration();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input, varDec);
	}

	@Test
	public void testVariableDeclaration() {
		assertTrue(isVariableDeclaration("That_Os := \"GNU/Linux\";"));

		assertTrue(isVariableDeclaration("Name := \"readme.txt\";"));

		assertTrue(isVariableDeclaration("Save_Name := \"name\" & \".saved\";"));

		assertFalse(this.isVariableDeclaration("variable = \"missing_colon\";"));
		assertFalse(this
				.isVariableDeclaration("variable := \"missing_semicolon\""));
		assertFalse(this
				.isVariableDeclaration("typed_variable : type := \"GNU/Linux\";"));
	}

	private boolean isTypedVariableDeclarationIdentified(String input) {

		SimpleParserRuleChecker typedVariableDec = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.typed_variable_declaration();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				typedVariableDec);
	}

	@Test
	public void testTypedVariableDeclaration() {
		assertTrue(isTypedVariableDeclarationIdentified("That_OS : OS := \"GNU/Linux\";"));

		assertTrue(isTypedVariableDeclarationIdentified("This_OS : OS := external (\"OS\");"));

		assertFalse(isTypedVariableDeclarationIdentified("That_OS : OS := \"Missing_Semicolon\""));
		assertFalse(isTypedVariableDeclarationIdentified("Untyped_variable := \"GNU/Linux\";"));
	}

	private boolean isStringListIdentified(String input, Symbol expectedSymbol) {
		SymbolParserRuleChecker stringListChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.string_list();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				stringListChecker, expectedSymbol);
	}

	@Test
	public void testStringList() {
		List<String> expStringList = new ArrayList<String>();
		expStringList.add("One_Element");
		assertTrue(this.isStringListIdentified("(\"One_Element\")",
				Symbol.CreateStringList(expStringList)));

		expStringList.clear();
		expStringList.add("First");
		expStringList.add("Second");
		expStringList.add("Third");

		assertTrue(this.isStringListIdentified(
				"(\"First\", \"Second\", \"Third\"))",
				Symbol.CreateStringList(expStringList)));

		assertFalse(this.isStringListIdentified("(\"Missing\" \"comma\")",
				Symbol.CreateString("")));
	}

	private boolean isAttributeDeclaration(String input) {
		SimpleParserRuleChecker attributeDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_declaration();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				attributeDecChecker);
	}

	@Test
	public void testAttributeDeclarationWithSimpleAttributes() {

		assertTrue(isAttributeDeclaration("for Object_Dir use \"objects\";"));
		assertTrue(isAttributeDeclaration("for Source_Dirs use (\"units\", \"test/drivers\");)"));
	}

	@Test
	public void testAttributeDeclarationWithIndexedAttributes() {
		assertTrue(isAttributeDeclaration("for Body (\"main\") use \"Main.ada\";"));
		assertTrue(isAttributeDeclaration("for Switches (\"main.ada\") use (\"-v\", \"-gnatv\");"));
		assertTrue(isAttributeDeclaration("for Switches (\"main.ada\") use Builder'Switches (\"main.ada\") & \"-g\";"));
		assertTrue(isAttributeDeclaration("for Default_Switches use Default.Builder'Default_Switches;"));
	}

	private boolean isAttributeReference(String input) {
		SimpleParserRuleChecker attributeRefChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_reference();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				attributeRefChecker);
	}

	@Test
	public void testAttributeReference() {
		assertTrue(this.isAttributeReference("project'Object_Dir"));
		assertTrue(this.isAttributeReference("Naming'Dot_Replacement"));
		assertTrue(this.isAttributeReference("Imported_Project'Source_Dirs"));
		assertTrue(this.isAttributeReference("Imported_Project.Naming'Casing"));
		assertTrue(this
				.isAttributeReference("Builder'Default_Switches (\"Ada\")"));
	}

	private boolean isProjectDeclaration(String input) {
		SimpleParserRuleChecker projectDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.project_declaration();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				projectDecChecker);
	}

	@Test
	public void testProjectDeclaration() {
		assertTrue(isProjectDeclaration("project Empty is    \n\r  end Empty;"));

		assertFalse(isProjectDeclaration("project Missing_Semicolon is end Missing_Semicolon"));
		assertFalse(isProjectDeclaration("project Missing_End is Missing_End;"));
		assertFalse(isProjectDeclaration("project First_Name is	end Second_Name;"));
		assertFalse(isProjectDeclaration("project My_Proj is	my_var : my_type := \"Value\"; my_var : my_type := \"New_Value\"; end My_Proj;"));
	}

	@Test
	public void testExternalValue() {
		assertTrue(this.isExternalValue("external (\"Variable\")"));

		assertTrue(this
				.isExternalValue("external (\"Variable\", \"Default_Value\")"));
	}

	private boolean isExternalValue(String input) {
		SimpleParserRuleChecker externalValueChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.external_value();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				externalValueChecker);
	}

	@Test
	public void testWithClause() {
		assertTrue(this.isWithClause("with \"../../my_project.gpr\";"));
		assertTrue(this
				.isWithClause("with \"..\\..\\my_project_windows.gpr\", \"../my_project_linux.gpr\";"));

		assertFalse(this.isWithClause("with \"../../missing_semicolon.gpr\""));
	}

	private boolean isWithClause(String input) {
		SimpleParserRuleChecker withClauseChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.with_clause();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				withClauseChecker);
	}

	@Test
	public void testContextClause() {
		assertTrue(this.isContextClause("\"\""));
		assertTrue(this
				.isContextClause("with \"../../my_project.gpr\"; with \"..\\..\\my_project_windows.gpr\";"));
	}

	private boolean isContextClause(String input) {
		SimpleParserRuleChecker ctxtClauseChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.context_clause();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				ctxtClauseChecker);
	}

	@Test
	public void testCaseStatement() {
		assertTrue(isCaseStatement("case OS is "
				+ "when \"GNU/Linux\" | \"Unix\" => for Switches (\"Ada\") use (\"-gnath\"); "
				+ "when \"NT\" => for Switches (\"Ada\") use (\"-gnatP\"); "
				+ "when others => null; " + "end case;"));

		assertFalse(isCaseStatement("case OS is"
				+ " when \"GNU/Linux\" | \"Unix\" => for Switches (\"Ada\") use (\"-gnath\");"
				+ "when \"NT\" => for Switches (\"Ada\") use (\"-gnatP\"); "
				+ "when others => null; " + "end;"));
	}

	private boolean isCaseStatement(String input) {
		SimpleParserRuleChecker caseStmtChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.case_statement();
			}
		};

		return this
				.doesInputComplyWithParserRuleChecker(input, caseStmtChecker);
	}

	@Test
	public void testTypedStringDeclaration() {
		assertTrue(isTypedStringDeclaration("type OS is (\"NT\", \"nt\", \"Unix\", \"GNU/Linux\", \"other OS\");"));
	}

	private boolean isTypedStringDeclaration(String input) {
		SimpleParserRuleChecker typedStringDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.typed_string_declaration();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				typedStringDecChecker);
	}

	@Test
	public void testPackageDeclaration() {
		assertTrue(isPackageDeclaration("package Compiler renames Logging.Compiler;"));
	}

	private boolean isPackageDeclaration(String input) {
		SimpleParserRuleChecker packageDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.package_declaration();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input,
				packageDecChecker);
	}

	@Test
	public void testProject() {
		assertTrue(isProject("with \"../../included.gpr\"; project My_Project is end My_Project;"));
	}

	private boolean isProject(String input) {
		SimpleParserRuleChecker projectChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.project();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input, projectChecker);
	}
}
