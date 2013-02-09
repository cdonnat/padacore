package org.padacore.core.gnat.test;

import java.io.IOException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.padacore.core.gnat.GprLexer;
import org.padacore.core.gnat.GprLoader;
import org.padacore.core.gnat.GprParser;
import org.padacore.core.gnat.Symbol;

public class GprGrammarTestUtils {

	public static GprParser CreateParser(GprLexer lexer, GprLoader loader)
			throws IOException {
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GprParser parser = new GprParser(loader, tokens);

		return parser;
	}

	public static GprLexer CreateLexer(String testString) throws IOException {
		CharStream stream = new ANTLRStringStream(testString);
		GprLexer lexer = new GprLexer(stream);

		return lexer;
	}

	public static boolean NoRecognitionExceptionOccurred(GprParser parser,
			GprLexer lexer) {
		return parser.getExceptions().isEmpty()
				&& lexer.getExceptions().isEmpty();
	}

	private static boolean RunSimpleParserRuleCheck(
			SimpleParserRuleChecker checker, String input) {
		GprGrammarFixture fixture = new GprGrammarFixture(input);

		return checker.isInputRecognizedByParserRule(fixture);
	}

	private static boolean RunSymbolParserRuleCheck(
			SymbolParserRuleChecker checker, String input, Symbol expSymbol) {
		GprGrammarFixture fixture = new GprGrammarFixture(input);

		return checker.isInputRecognizedByParserRule(fixture, expSymbol);
	}

	public static abstract class SymbolParserRuleChecker {

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

		public boolean isInputRecognizedByParserRule(GprGrammarFixture fixture,
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

		protected abstract Symbol executeParserRule(GprGrammarFixture fixture)
				throws RecognitionException;
	}

	public static abstract class SimpleParserRuleChecker {
		public boolean isInputRecognizedByParserRule(GprGrammarFixture fixture) {
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

	public static boolean IsNameIdentified(String inputName) {
		GprParser.name_return ruleReturn = new GprParser.name_return();
		GprGrammarFixture fixture = new GprGrammarFixture(inputName);

		try {
			ruleReturn = fixture.parser.name();
		} catch (RecognitionException e) {

		}

		return GprGrammarTestUtils.NoRecognitionExceptionOccurred(
				fixture.parser, fixture.lexer)
				&& ruleReturn.result.equals(inputName);
	}

	public static boolean IsEmptyDeclaration(String input) {
		SimpleParserRuleChecker emptyDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.empty_declaration();
			}
		};

		return RunSimpleParserRuleCheck(emptyDecChecker, input);
	}

	public static boolean IsExpressionIdentified(String input,
			Symbol expectedSymbol) {
		SymbolParserRuleChecker expressionChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.expression();
			}
		};

		return RunSymbolParserRuleCheck(expressionChecker, input,
				expectedSymbol);
	}

	public static boolean IsVariableDeclaration(String input) {
		SimpleParserRuleChecker varDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.variable_declaration();

			}
		};

		return RunSimpleParserRuleCheck(varDecChecker, input);
	}

	public static boolean IsTypedVariableDeclarationIdentified(String input) {
		SimpleParserRuleChecker typedVariableDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.typed_variable_declaration();
			}
		};

		return RunSimpleParserRuleCheck(typedVariableDecChecker, input);
	}

	public static boolean isStringListIdentified(String input,
			Symbol expectedSymbol) {
		SymbolParserRuleChecker stringListChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.string_list();
			}
		};

		return RunSymbolParserRuleCheck(stringListChecker, input,
				expectedSymbol);
	}

	public static boolean isAttributeDeclaration(String input) {
		SimpleParserRuleChecker attributeDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_declaration();

			}
		};

		return RunSimpleParserRuleCheck(attributeDecChecker, input);
	}

	public static boolean isAttributeReference(String input) {
		SimpleParserRuleChecker attributeRefChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_reference();

			}
		};

		return RunSimpleParserRuleCheck(attributeRefChecker, input);
	}

	public static boolean isProjectDeclaration(String input) {
		SimpleParserRuleChecker projectDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.project_declaration();

			}
		};

		return RunSimpleParserRuleCheck(projectDecChecker, input);
	}

	public static boolean isExternalValue(String input) {
		SimpleParserRuleChecker externalValueChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.external_value();
			}
		};

		return RunSimpleParserRuleCheck(externalValueChecker, input);
	}

	public static boolean isWithClause(String input) {
		SimpleParserRuleChecker withClauseChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.with_clause();
			}
		};

		return RunSimpleParserRuleCheck(withClauseChecker, input);
	}

	public static boolean isContextClause(String input) {
		SimpleParserRuleChecker ctxtClauseChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.context_clause();
			}
		};

		return RunSimpleParserRuleCheck(ctxtClauseChecker, input);
	}

	public static boolean isCaseStatement(String input) {
		SimpleParserRuleChecker caseStmtChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.case_statement();
			}
		};

		return RunSimpleParserRuleCheck(caseStmtChecker, input);
	}

	public static boolean isTypedStringDeclaration(String input) {
		SimpleParserRuleChecker typedStringDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.typed_string_declaration();
			}
		};

		return RunSimpleParserRuleCheck(typedStringDecChecker, input);
	}

	public static boolean isPackageDeclaration(String input) {
		SimpleParserRuleChecker packageDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.package_declaration();
			}
		};

		return RunSimpleParserRuleCheck(packageDecChecker, input);
	}

	public static boolean isProject(String input) {
		SimpleParserRuleChecker projectChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.project();

			}
		};

		return RunSimpleParserRuleCheck(projectChecker, input);
	}

}