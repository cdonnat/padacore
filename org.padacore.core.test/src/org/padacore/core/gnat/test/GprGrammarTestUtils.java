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
			SimpleParserRuleChecker checker, String input,
			boolean forceVariableDefinition) {
		GprGrammarFixture fixture = new GprGrammarFixture(input,
				forceVariableDefinition);

		return checker.isInputRecognizedByParserRule(fixture);
	}

	private static boolean RunSimpleParserRuleCheck(
			SimpleParserRuleChecker checker, String input) {
		GprGrammarFixture fixture = new GprGrammarFixture(input);

		return checker.isInputRecognizedByParserRule(fixture);
	}

	private static boolean RunStringParserRuleCheck(
			StringParserRuleChecker checker, String input, String expString) {
		GprGrammarFixture fixture = new GprGrammarFixture(input);

		return checker.isInputRecognizedByParserRule(fixture, expString);
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

	public static abstract class StringParserRuleChecker {

		public boolean isInputRecognizedByParserRule(GprGrammarFixture fixture,
				String expectedString) {
			String result = null;

			try {
				result = this.executeParserRule(fixture);
			} catch (RecognitionException e) {
				e.printStackTrace();
			}

			return GprGrammarTestUtils.NoRecognitionExceptionOccurred(
					fixture.parser, fixture.lexer)
					&& result.equals(expectedString);
		}

		protected abstract String executeParserRule(GprGrammarFixture fixture)
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

	public static boolean IsTypedVariableDeclaration(String input) {
		SimpleParserRuleChecker typedVariableDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.typed_variable_declaration();
			}
		};

		return RunSimpleParserRuleCheck(typedVariableDecChecker, input, false);
	}

	public static boolean IsStringListIdentified(String input,
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

	public static boolean IsAttributeDeclaration(String input) {
		SimpleParserRuleChecker attributeDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_declaration();

			}
		};

		return RunSimpleParserRuleCheck(attributeDecChecker, input);
	}

	public static boolean IsAttributeReference(String input) {
		SimpleParserRuleChecker attributeRefChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.attribute_reference();

			}
		};

		return RunSimpleParserRuleCheck(attributeRefChecker, input);
	}

	public static boolean IsProjectDeclaration(String input) {
		SimpleParserRuleChecker projectDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.project_declaration();

			}
		};

		return RunSimpleParserRuleCheck(projectDecChecker, input);
	}

	public static boolean IsSimpleProjectDeclaration(String input) {
		SimpleParserRuleChecker simpleProjectDecChecker = new SimpleParserRuleChecker() {

			@Override
			public void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.simple_project_declaration();

			}
		};

		return RunSimpleParserRuleCheck(simpleProjectDecChecker, input);
	}

	public static boolean IsExternalValue(String input) {
		SimpleParserRuleChecker externalValueChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.external_value();
			}
		};

		return RunSimpleParserRuleCheck(externalValueChecker, input);
	}

	public static boolean IsWithClause(String input) {
		SimpleParserRuleChecker withClauseChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.with_clause();
			}
		};

		return RunSimpleParserRuleCheck(withClauseChecker, input);
	}

	public static boolean IsContextClause(String input) {
		SimpleParserRuleChecker ctxtClauseChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.context_clause();
			}
		};

		return RunSimpleParserRuleCheck(ctxtClauseChecker, input);
	}

	public static boolean IsCaseStatement(String input) {
		SimpleParserRuleChecker caseStmtChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.case_statement();
			}
		};

		return RunSimpleParserRuleCheck(caseStmtChecker, input);
	}

	public static boolean IsTypedStringDeclaration(String input) {
		SimpleParserRuleChecker typedStringDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.typed_string_declaration();
			}
		};

		return RunSimpleParserRuleCheck(typedStringDecChecker, input);
	}

	public static boolean IsPackageDeclaration(String input) {
		SimpleParserRuleChecker packageDecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.package_declaration();
			}
		};

		return RunSimpleParserRuleCheck(packageDecChecker, input);
	}

	public static boolean IsProject(String input) {
		SimpleParserRuleChecker projectChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.project();

			}
		};

		return RunSimpleParserRuleCheck(projectChecker, input);
	}

	public static boolean IsPathNameIdentified(String input,
			String expectedString) {
		StringParserRuleChecker pathNameChecker = new StringParserRuleChecker() {

			@Override
			protected String executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.path_name();
			}
		};

		return RunStringParserRuleCheck(pathNameChecker, input, expectedString);
	}

	public static boolean IsSimpleName(String input) {
		SimpleParserRuleChecker simpleNameChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.simple_name();

			}
		};

		return RunSimpleParserRuleCheck(simpleNameChecker, input);
	}

	public static boolean IsSimpleDeclarativeItem(String input) {
		SimpleParserRuleChecker simpleDecItemChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.simple_declarative_item();

			}
		};

		return RunSimpleParserRuleCheck(simpleDecItemChecker, input, false);
	}

	public static boolean IsDeclarativeItem(String input) {
		SimpleParserRuleChecker declarativeItemChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.declarative_item();

			}
		};

		return RunSimpleParserRuleCheck(declarativeItemChecker, input);
	}

	public static boolean IsDiscreteChoiceList(String input) {
		SimpleParserRuleChecker discreteChoiceChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.discrete_choice_list();

			}
		};

		return RunSimpleParserRuleCheck(discreteChoiceChecker, input, false);
	}

	public static boolean IsCaseItem(String input) {
		SimpleParserRuleChecker caseItemChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.case_item();

			}
		};

		return RunSimpleParserRuleCheck(caseItemChecker, input);
	}

	public static boolean IsPackageSpec(String input) {
		SimpleParserRuleChecker packageSpecChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.package_spec();

			}
		};

		return RunSimpleParserRuleCheck(packageSpecChecker, input, false);
	}

	public static boolean IsPackageRenaming(String input) {
		SimpleParserRuleChecker packageRenamingChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.package_renaming();

			}
		};

		return RunSimpleParserRuleCheck(packageRenamingChecker, input, false);
	}

	public static boolean IsPackageExtension(String input) {
		SimpleParserRuleChecker packageExtensionChecker = new SimpleParserRuleChecker() {

			@Override
			protected void executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				fixture.parser.package_extension();

			}
		};

		return RunSimpleParserRuleCheck(packageExtensionChecker, input, false);
	}

	public static boolean IsAttributeDesignatorIdentified(String input,
			String expString) {
		StringParserRuleChecker attDesignatorChecker = new StringParserRuleChecker() {

			@Override
			protected String executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.attribute_designator();
			}
		};

		return RunStringParserRuleCheck(attDesignatorChecker, input, expString);
	}

	public static boolean IsAttributePrefix(String input, String expString) {
		StringParserRuleChecker attPrefixChecker = new StringParserRuleChecker() {

			@Override
			protected String executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.attribute_prefix();
			}
		};

		return RunStringParserRuleCheck(attPrefixChecker, input, expString);
	}

	public static boolean IsTermIdentified(String input, Symbol expSymbol) {
		SymbolParserRuleChecker termChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.expression();
			}
		};

		return RunSymbolParserRuleCheck(termChecker, input, expSymbol);
	}

	public static boolean IsStringExpressionIdentified(String input,
			Symbol expSymbol) {
		SymbolParserRuleChecker stringExpChecker = new SymbolParserRuleChecker() {

			@Override
			protected Symbol executeParserRule(GprGrammarFixture fixture)
					throws RecognitionException {
				return fixture.parser.string_expression();
			}
		};

		return RunSymbolParserRuleCheck(stringExpChecker, input, expSymbol);
	}

}