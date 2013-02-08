package org.padacore.core.gnat.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.padacore.core.gnat.GprLexer;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.GprParser;
import org.padacore.core.gnat.Symbol;

public class GprGrammarTest {

	private class Fixture {
		public GprLexer lexer;
		public GprParser parser;
		public GprLoader loader;
	}

	private Fixture fixture;

	public void createFixture(String testString) {
		try {
			this.fixture = new Fixture();
			this.fixture.lexer = this.createLexer(testString);
			this.fixture.loader = new GprLoader();
			this.fixture.parser = this.createParser(this.fixture.lexer, this.fixture.loader);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private interface ParserRuleRunner {
		public void executeParserRule(Fixture fixture) throws RecognitionException;
	}

	private GprParser createParser(GprLexer lexer, GprLoader loader) throws IOException {
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GprParser parser = new GprParser(loader, tokens);

		return parser;
	}

	private GprLexer createLexer(String testString) throws IOException {
		CharStream stream = new ANTLRStringStream(testString);
		GprLexer lexer = new GprLexer(stream);

		return lexer;
	}

	private boolean noRecognitionExceptionOccurred(GprParser parser, GprLexer lexer) {
		return parser.getExceptions().isEmpty() && lexer.getExceptions().isEmpty();
	}

	private boolean isNameCorrectlyIdentified(String inputName) {
		GprParser.name_return ruleReturn = new GprParser.name_return();
		this.createFixture(inputName);

		try {
			ruleReturn = this.fixture.parser.name();

		} catch (RecognitionException e) {

		}

		return this.noRecognitionExceptionOccurred(this.fixture.parser, this.fixture.lexer)
				&& ruleReturn.result.equals(inputName);
	}

	private boolean isInputRecognizedByParserRule(String input, ParserRuleRunner rule) {
		this.createFixture(input);
		try {
			rule.executeParserRule(this.fixture);
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		return this.noRecognitionExceptionOccurred(this.fixture.parser, this.fixture.lexer);
	}

	@Test
	public void testName() {
		assertTrue(isNameCorrectlyIdentified("simple_identifier"));
		assertTrue(isNameCorrectlyIdentified("one_level.identifier"));
		assertTrue(isNameCorrectlyIdentified("two.levels.identifier"));

		assertFalse(isNameCorrectlyIdentified("two__nextToEachOther"));
	}

	private boolean isEmptyDeclarationCorrectlyIdentified(String inputEmptyDec) {
		return this.isInputRecognizedByParserRule(inputEmptyDec, new ParserRuleRunner() {

			@Override
			public void executeParserRule(Fixture fixture) throws RecognitionException {
				fixture.parser.empty_declaration();
			}
		});
	}

	private boolean areSymbolsIdentical(Symbol left, Symbol right) {
		boolean areEqual;

		if (left.isAString() && right.isAString()) {
			areEqual = left.getAsString().equals(right.getAsString());
		} else {
			areEqual = !left.isAString() && !right.isAString()
					&& left.getAsStringList().equals(right.getAsStringList());
		}

		return areEqual;
	}

	@Test
	public void testEmptyDeclaration() {
		assertTrue(isEmptyDeclarationCorrectlyIdentified("null;"));
		assertFalse("Missing semicolon", isEmptyDeclarationCorrectlyIdentified("null"));
	}

	private boolean isExpressionCorrectlyIdentified(String inputExpr, Symbol expectedSymbol) {
		this.createFixture(inputExpr);

		Symbol ruleResult = null;
		boolean exprIsCorrect = false;

		try {
			ruleResult = this.fixture.parser.expression();

			exprIsCorrect = this.noRecognitionExceptionOccurred(this.fixture.parser,
					this.fixture.lexer) && this.areSymbolsIdentical(ruleResult, expectedSymbol);
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		return exprIsCorrect;
	}

	@Test
	public void testExpression() {
		assertTrue(isExpressionCorrectlyIdentified("\"simple_string\"",
				Symbol.CreateString("simple_string")));

		assertTrue(
				"String & string",
				isExpressionCorrectlyIdentified("\"string\" & \"string\"",
						Symbol.CreateString("stringstring")));

		List<String> expStringList = new ArrayList<String>();
		expStringList.add("File_Name");
		assertTrue(
				"List & string",
				isExpressionCorrectlyIdentified("() & \"File_Name\"",
						Symbol.CreateStringList(expStringList)));

		expStringList.clear();
		expStringList.add("File_Name");
		expStringList.add("File_Name.orig");
		assertTrue(
				"List & list",
				isExpressionCorrectlyIdentified("() & \"File_Name\" & (\"File_Name\" & \".orig\")",
						Symbol.CreateStringList(expStringList)));
	}

	private boolean isVariableDeclarationCorrectlyIdentified(String inputVarDec,
			String expectedVarName, Symbol expectedSymbol) {
		this.createFixture(inputVarDec);

		try {
			this.fixture.parser.variable_declaration();

		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		return this.noRecognitionExceptionOccurred(this.fixture.parser, this.fixture.lexer)
				&& this.areSymbolsIdentical(this.fixture.loader.getVariable(expectedVarName),
						expectedSymbol);
	}

	@Test
	public void testVariableDeclaration() {
		assertTrue(isVariableDeclarationCorrectlyIdentified("That_Os := \"GNU/Linux\";", "That_Os",
				Symbol.CreateString("GNU/Linux")));

		assertTrue(isVariableDeclarationCorrectlyIdentified("Name := \"readme.txt\";", "Name",
				Symbol.CreateString("readme.txt")));

		assertTrue(isVariableDeclarationCorrectlyIdentified("Save_Name := \"name\" & \".saved\";",
				"Save_Name", Symbol.CreateString("name.saved")));

		ParserRuleRunner varDec = new ParserRuleRunner() {

			@Override
			public void executeParserRule(Fixture fixture) throws RecognitionException {
				fixture.parser.variable_declaration();
			}
		};

		assertFalse(this.isInputRecognizedByParserRule("variable = \"missing_colon\";", varDec));
		assertFalse(this.isInputRecognizedByParserRule("variable := \"missing_semicolon\"", varDec));
		assertFalse(this.isInputRecognizedByParserRule("typed_variable : type := \"GNU/Linux\";",
				varDec));
	}

	private boolean isTypedVariableDeclarationCorrectlyIdentified(String inputTypedVarDec,
			String expectedVarName, Symbol expectedSymbol) {
		this.createFixture(inputTypedVarDec);

		try {
			this.fixture.parser.typed_variable_declaration();

		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		return this.noRecognitionExceptionOccurred(this.fixture.parser, this.fixture.lexer)
				&& this.areSymbolsIdentical(this.fixture.loader.getVariable(expectedVarName),
						expectedSymbol);
	}

	@Test
	public void testTypedVariableDeclaration() {
		assertTrue(isTypedVariableDeclarationCorrectlyIdentified("That_OS : OS := \"GNU/Linux\";",
				"That_OS", Symbol.CreateString("GNU/Linux")));

		assertTrue(isTypedVariableDeclarationCorrectlyIdentified(
				"This_OS : OS := external (\"OS\");", "This_OS", Symbol.CreateString("OS")));

		ParserRuleRunner typedVariableDec = new ParserRuleRunner() {

			@Override
			public void executeParserRule(Fixture fixture) throws RecognitionException {
				fixture.parser.typed_variable_declaration();
			}
		};

		assertFalse(this.isInputRecognizedByParserRule("That_OS : OS := \"Missing_Semicolon\"",
				typedVariableDec));
		assertFalse(this.isInputRecognizedByParserRule("Untyped_variable := \"GNU/Linux\";",
				typedVariableDec));
	}

	private boolean isStringListCorrectlyIdentified(String inputStringList, Symbol expectedSymbol) {
		this.createFixture(inputStringList);
		Symbol ruleReturn = null;

		try {
			ruleReturn = this.fixture.parser.string_list();

		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		return this.noRecognitionExceptionOccurred(this.fixture.parser, this.fixture.lexer)
				&& this.areSymbolsIdentical(ruleReturn, expectedSymbol);
	}

	@Test
	public void testStringList() {
		List<String> expStringList = new ArrayList<String>();
		expStringList.add("One_Element");
		assertTrue(this.isStringListCorrectlyIdentified("(\"One_Element\")",
				Symbol.CreateStringList(expStringList)));

		expStringList.clear();
		expStringList.add("First");
		expStringList.add("Second");
		expStringList.add("Third");

		assertTrue(this.isStringListCorrectlyIdentified("(\"First\", \"Second\", \"Third\"))",
				Symbol.CreateStringList(expStringList)));

		ParserRuleRunner string_List = new ParserRuleRunner() {

			@Override
			public void executeParserRule(Fixture fixture) throws RecognitionException {
				fixture.parser.string_list();
			}
		};

		assertFalse(isInputRecognizedByParserRule("(\"Missing\" \"comma\")", string_List));
	}
}
