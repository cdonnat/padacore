package org.padacore.core.gnat.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.padacore.core.gnat.GprLexer;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.GprParser;
import org.padacore.core.gnat.Symbol;
import org.padacore.core.test.stubs.GprLoaderStub;

public class GprGrammarTest {

	private class Fixture {
		public GprLexer lexer;
		public GprParser parser;
		public GprLoader loader;
	}

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

		private boolean isInputRecognizedByParserRule(Fixture fixture) {
			try {
				this.executeParserRule(fixture);
			} catch (RecognitionException e) {
				e.printStackTrace();
			}

			return GprGrammarTestUtils.NoRecognitionExceptionOccurred(
					fixture.parser, fixture.lexer);
		}

		protected abstract void executeParserRule(Fixture fixture)
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

		private boolean isInputRecognizedByParserRule(Fixture fixture,
				Symbol expectedSymbol) {
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

		protected abstract Symbol executeParserRule(Fixture fixture)
				throws RecognitionException;
	}

	private Fixture fixture;

	private void createFixture(String testString) {
		try {
			this.fixture = new Fixture();
			this.fixture.lexer = GprGrammarTestUtils.CreateLexer(testString);
			this.fixture.loader = new GprLoaderStub();
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
			protected void executeParserRule(Fixture fixture)
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

	private boolean isExpressionIdentified(String input,
			Symbol expectedSymbol) {
		SymbolParserRuleChecker expressionChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(Fixture fixture)
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
			public void executeParserRule(Fixture fixture)
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
		assertFalse(this.isVariableDeclaration(
				"variable := \"missing_semicolon\""));
		assertFalse(this.isVariableDeclaration(
				"typed_variable : type := \"GNU/Linux\";"));
	}

	private boolean isTypedVariableDeclarationIdentified(String input) {

		SimpleParserRuleChecker typedVariableDec = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(Fixture fixture)
					throws RecognitionException {
				fixture.parser.typed_variable_declaration();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input, typedVariableDec);
	}

	@Test
	public void testTypedVariableDeclaration() {
		assertTrue(isTypedVariableDeclarationIdentified(
				"That_OS : OS := \"GNU/Linux\";"));

		assertTrue(isTypedVariableDeclarationIdentified(
				"This_OS : OS := external (\"OS\");"));

		assertFalse(isTypedVariableDeclarationIdentified(
				"That_OS : OS := \"Missing_Semicolon\""));
		assertFalse(isTypedVariableDeclarationIdentified(
				"Untyped_variable := \"GNU/Linux\";"));
	}

	private boolean isStringListIdentified(String input,
			Symbol expectedSymbol) {
		SymbolParserRuleChecker stringListChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(Fixture fixture)
					throws RecognitionException {
				return fixture.parser.string_list();
			}
		};

		return this.doesInputComplyWithParserRuleChecker(input, stringListChecker, expectedSymbol);
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

		assertFalse(this.isStringListIdentified(
				"(\"Missing\" \"comma\")", Symbol.CreateString("")));
	}

	private boolean isAttributeDeclaration(String input) {
		SimpleParserRuleChecker attributeDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(Fixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_declaration();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input, attributeDecChecker);
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
			public void executeParserRule(Fixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_reference();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input, attributeRefChecker);
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
			public void executeParserRule(Fixture fixture)
					throws RecognitionException {
				fixture.parser.project_declaration();

			}
		};

		return this.doesInputComplyWithParserRuleChecker(input, projectDecChecker);
	}

	@Test
	public void testProjectDeclaration() {
		assertTrue(isProjectDeclaration("project Empty is    \n\r  end Empty;"));

		assertFalse(isProjectDeclaration("project Missing_Semicolon is end Missing_Semicolon"));
		assertFalse(isProjectDeclaration("project Missing_End is Missing_End;"));
		assertFalse(isProjectDeclaration("project First_Name is	end Second_Name;"));
		assertFalse(isProjectDeclaration("project My_Proj is	my_var : my_type := \"Value\"; my_var : my_type := \"New_Value\"; end My_Proj;"));
	}

	// @Test
	// public void testExternalValue() {
	// assertTrue(this.isExternalValue("external (\"Variable\")",
	// Symbol.CreateString("Variable"));
	// assertTrue(this.isExternalValue("external (\"Variable\", \"Default_Value\")",
	// Symbol.CreateString("Variable"));
	// }

	// private boolean isExternalValue(String string, Symbol expectedSymbol) {
	// Symbol ruleReturn;
	// ParserRuleRunner externalValue = new ParserRuleRunner() {
	//
	// @Override
	// protected void executeParserRule(Fixture fixture)
	// throws RecognitionException {
	// fixture.parser.external_value();
	//
	// }
	// };
	// }
}
