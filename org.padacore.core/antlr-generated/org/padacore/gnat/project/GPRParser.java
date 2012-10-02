// $ANTLR 3.4 /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g 2012-10-02 23:02:07

package org.padacore.gnat.project;

import org.padacore.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class GPRParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALL", "AT", "CASE", "DIGIT", "END", "END_OF_LINE", "EXTENDS", "EXTERNAL", "FOR", "IDENTIFIER", "IS", "LIMITED", "LOWER_CASE_LETTER", "NULL", "OTHERS", "PACKAGE", "PROJECT", "RENAMES", "STRING_ELEMENT", "STRING_LITERAL", "TYPE", "UPPER_CASE_LETTER", "USE", "WHEN", "WITH", "WS", "'('", "')'", "','", "'--'", "'.'", "':'", "':='", "';'", "'\\n'", "'\\r'"
    };

    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int ALL=4;
    public static final int AT=5;
    public static final int CASE=6;
    public static final int DIGIT=7;
    public static final int END=8;
    public static final int END_OF_LINE=9;
    public static final int EXTENDS=10;
    public static final int EXTERNAL=11;
    public static final int FOR=12;
    public static final int IDENTIFIER=13;
    public static final int IS=14;
    public static final int LIMITED=15;
    public static final int LOWER_CASE_LETTER=16;
    public static final int NULL=17;
    public static final int OTHERS=18;
    public static final int PACKAGE=19;
    public static final int PROJECT=20;
    public static final int RENAMES=21;
    public static final int STRING_ELEMENT=22;
    public static final int STRING_LITERAL=23;
    public static final int TYPE=24;
    public static final int UPPER_CASE_LETTER=25;
    public static final int USE=26;
    public static final int WHEN=27;
    public static final int WITH=28;
    public static final int WS=29;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public GPRParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public GPRParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return GPRParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g"; }


    private final static String EXECUTABLE_DIRECTORY_ATTRIBUTE = "Exec_Dir";
    private final static String OBJECT_DIRECTORY_ATTRIBUTE = "Object_Dir";
    private final static String MAIN_ATTRIBUTE = "Main";
    private final static String SOURCE_DIRECTORIES_ATTRIBUTE = "Source_Dirs";

    private GprProject project;
    private Map<String, ArrayList<String>> vars = new HashMap<String, ArrayList<String>>();
    private Map<String, ArrayList<String>> simpleAttributes = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> withedProjects = new ArrayList<String>();

    public GprProject getGprProject() {
    	return this.project;
    }

    /**
     * Return True if variable whose name is given as a parameter is already defined.
     *
     */
    private boolean isVariableDefined(String variableName) {
    	return this.vars.containsKey(variableName);
    }

    /**
     * Adds the given String variable with its value in the symbols table.
     *
     */
    private void addNewStringVariable(String variableName, String value) {
    	ArrayList<String> values = new ArrayList(1);
    	values.add(value);
    	this.vars.put(variableName, values);
    }

    /**
     *
     * Adds the given simple attribute to the list of found attributes.
     */
    private void addNewSimpleAttribute(String attributeName,
    		ArrayList<String> attributeValue) {
    	this.simpleAttributes.put(attributeName, attributeValue);
    }

    /**
     * Returns True if the simple attribute whose name is given as a parameter is defined, False otherwise.
     *
     */
    private boolean isSimpleAttributeDefined(String attributeName) {
    	return this.simpleAttributes.containsKey(attributeName);
    }

    /**
     * Updates the GPR project according to the simple attributes read in GPR file.
     *
     */
    private void processSimpleAttributes() {
    	if (this.simpleAttributes.containsKey(EXECUTABLE_DIRECTORY_ATTRIBUTE)) {

    		List<String> execDirectories = this.simpleAttributes
    				.get(EXECUTABLE_DIRECTORY_ATTRIBUTE);
    		assert (execDirectories.size() == 1);

    		this.project.setExecutableDir(execDirectories.get(0));
    	}

    	if (this.simpleAttributes.containsKey(OBJECT_DIRECTORY_ATTRIBUTE)) {
    		List<String> objectDirectories = this.simpleAttributes
    				.get(OBJECT_DIRECTORY_ATTRIBUTE);
    		assert (objectDirectories.size() == 1);
    		this.project.setObjectDir(objectDirectories.get(0));
    	}

    	if (this.simpleAttributes.containsKey(MAIN_ATTRIBUTE)) {
    		List<String> executables = this.simpleAttributes.get(MAIN_ATTRIBUTE);
    		this.project.setExecutable(true);
    		for (String executable : executables) {
    			this.project.addExecutableName(executable);
    		}
    	}

    	if (this.simpleAttributes.containsKey(SOURCE_DIRECTORIES_ATTRIBUTE)) {
    		List<String> sourceDirs = this.simpleAttributes
    				.get(SOURCE_DIRECTORIES_ATTRIBUTE);

    		for (String sourceDir : sourceDirs) {
    			this.project.addSourceDir(sourceDir);
    		}
    	}
    }

    private void processWithedProjects() {
    	for (String withedProject : this.withedProjects) {
    		this.project.addWithedProject(withedProject);
    	}
    }



    // $ANTLR start "comment"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:243:1: comment : '--' (~ ( '\\n' | '\\r' ) )* ;
    public final void comment() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:244:3: ( '--' (~ ( '\\n' | '\\r' ) )* )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:245:3: '--' (~ ( '\\n' | '\\r' ) )*
            {
            match(input,33,FOLLOW_33_in_comment447); 

            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:246:3: (~ ( '\\n' | '\\r' ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= ALL && LA1_0 <= 37)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:
            	    {
            	    if ( (input.LA(1) >= ALL && input.LA(1) <= 37) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "comment"


    public static class simple_name_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "simple_name"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:268:1: simple_name : IDENTIFIER ;
    public final GPRParser.simple_name_return simple_name() throws RecognitionException {
        GPRParser.simple_name_return retval = new GPRParser.simple_name_return();
        retval.start = input.LT(1);


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:269:3: ( IDENTIFIER )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:270:3: IDENTIFIER
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_simple_name578); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "simple_name"


    public static class name_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "name"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:273:1: name : simple_name ( '.' simple_name )* ;
    public final GPRParser.name_return name() throws RecognitionException {
        GPRParser.name_return retval = new GPRParser.name_return();
        retval.start = input.LT(1);


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:274:3: ( simple_name ( '.' simple_name )* )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:275:3: simple_name ( '.' simple_name )*
            {
            pushFollow(FOLLOW_simple_name_in_name593);
            simple_name();

            state._fsp--;


            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:275:15: ( '.' simple_name )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==34) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:275:16: '.' simple_name
            	    {
            	    match(input,34,FOLLOW_34_in_name596); 

            	    pushFollow(FOLLOW_simple_name_in_name598);
            	    simple_name();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "name"



    // $ANTLR start "empty_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:278:1: empty_declaration : NULL ';' ;
    public final void empty_declaration() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:279:3: ( NULL ';' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:280:3: NULL ';'
            {
            match(input,NULL,FOLLOW_NULL_in_empty_declaration615); 

            match(input,37,FOLLOW_37_in_empty_declaration617); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "empty_declaration"



    // $ANTLR start "variable_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:307:1: variable_declaration : var_name= simple_name ':=' var_value= expression ';' ;
    public final void variable_declaration() throws RecognitionException {
        GPRParser.simple_name_return var_name =null;

        GPRParser.expression_return var_value =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:308:3: (var_name= simple_name ':=' var_value= expression ';' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:309:3: var_name= simple_name ':=' var_value= expression ';'
            {
            pushFollow(FOLLOW_simple_name_in_variable_declaration730);
            var_name=simple_name();

            state._fsp--;


            match(input,36,FOLLOW_36_in_variable_declaration732); 

            pushFollow(FOLLOW_expression_in_variable_declaration736);
            var_value=expression();

            state._fsp--;


            match(input,37,FOLLOW_37_in_variable_declaration738); 


                                                                  this.addNewStringVariable((var_name!=null?input.toString(var_name.start,var_name.stop):null), (var_value!=null?input.toString(var_value.start,var_value.stop):null));
                                                                 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "variable_declaration"



    // $ANTLR start "typed_variable_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:315:1: typed_variable_declaration : var_name= simple_name ':' type= name ':=' var_value= string_expression ';' {...}?;
    public final void typed_variable_declaration() throws RecognitionException {
        GPRParser.simple_name_return var_name =null;

        GPRParser.name_return type =null;

        GPRParser.string_expression_return var_value =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:316:3: (var_name= simple_name ':' type= name ':=' var_value= string_expression ';' {...}?)
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:317:3: var_name= simple_name ':' type= name ':=' var_value= string_expression ';' {...}?
            {
            pushFollow(FOLLOW_simple_name_in_typed_variable_declaration811);
            var_name=simple_name();

            state._fsp--;


            match(input,35,FOLLOW_35_in_typed_variable_declaration813); 

            pushFollow(FOLLOW_name_in_typed_variable_declaration817);
            type=name();

            state._fsp--;


            match(input,36,FOLLOW_36_in_typed_variable_declaration819); 

            pushFollow(FOLLOW_string_expression_in_typed_variable_declaration823);
            var_value=string_expression();

            state._fsp--;


            match(input,37,FOLLOW_37_in_typed_variable_declaration825); 

            if ( !((!this.isVariableDefined((var_name!=null?input.toString(var_name.start,var_name.stop):null)))) ) {
                throw new FailedPredicateException(input, "typed_variable_declaration", "!this.isVariableDefined($var_name.text)");
            }


                                                                                                                                  this.addNewStringVariable((var_name!=null?input.toString(var_name.start,var_name.stop):null), (var_value!=null?input.toString(var_value.start,var_value.stop):null));
                                                                                                                                 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "typed_variable_declaration"


    public static class string_expression_return extends ParserRuleReturnScope {
        public String result;
    };


    // $ANTLR start "string_expression"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:324:1: string_expression returns [String result] : ( STRING_LITERAL | name | external_value );
    public final GPRParser.string_expression_return string_expression() throws RecognitionException {
        GPRParser.string_expression_return retval = new GPRParser.string_expression_return();
        retval.start = input.LT(1);


        Token STRING_LITERAL1=null;

        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:325:3: ( STRING_LITERAL | name | external_value )
            int alt3=3;
            switch ( input.LA(1) ) {
            case STRING_LITERAL:
                {
                alt3=1;
                }
                break;
            case IDENTIFIER:
                {
                alt3=2;
                }
                break;
            case EXTERNAL:
            case 31:
            case 32:
            case 37:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }

            switch (alt3) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:326:3: STRING_LITERAL
                    {
                    STRING_LITERAL1=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_expression967); 


                                      retval.result = (STRING_LITERAL1!=null?STRING_LITERAL1.getText():null).replaceAll("\"", "");
                                     

                    }
                    break;
                case 2 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:330:5: name
                    {
                    pushFollow(FOLLOW_name_in_string_expression994);
                    name();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:331:5: external_value
                    {
                    pushFollow(FOLLOW_external_value_in_string_expression1000);
                    external_value();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "string_expression"



    // $ANTLR start "string_list"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:334:1: string_list returns [ArrayList < String > result] : '(' expr1= string_expression ( ',' expr2= string_expression )* ')' ;
    public final ArrayList < String > string_list() throws RecognitionException {
        ArrayList < String > result = null;


        GPRParser.string_expression_return expr1 =null;

        GPRParser.string_expression_return expr2 =null;



        result = new ArrayList<String>();

        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:338:3: ( '(' expr1= string_expression ( ',' expr2= string_expression )* ')' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:339:3: '(' expr1= string_expression ( ',' expr2= string_expression )* ')'
            {
            match(input,30,FOLLOW_30_in_string_list1025); 

            pushFollow(FOLLOW_string_expression_in_string_list1029);
            expr1=string_expression();

            state._fsp--;



                                           result.add((expr1!=null?expr1.result:null));
                                          

            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:343:3: ( ',' expr2= string_expression )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==32) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:343:4: ',' expr2= string_expression
            	    {
            	    match(input,32,FOLLOW_32_in_string_list1067); 

            	    pushFollow(FOLLOW_string_expression_in_string_list1071);
            	    expr2=string_expression();

            	    state._fsp--;



            	                                    result.add((expr2!=null?expr2.result:null));
            	                                   

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            match(input,31,FOLLOW_31_in_string_list1109); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "string_list"



    // $ANTLR start "term"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:349:1: term returns [ArrayList < String > result] : ( string_expression | string_list );
    public final ArrayList < String > term() throws RecognitionException {
        ArrayList < String > result = null;


        GPRParser.string_expression_return string_expression2 =null;

        ArrayList < String > string_list3 =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:350:3: ( string_expression | string_list )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==EXTERNAL||LA5_0==IDENTIFIER||LA5_0==STRING_LITERAL||(LA5_0 >= 31 && LA5_0 <= 32)||LA5_0==37) ) {
                alt5=1;
            }
            else if ( (LA5_0==30) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:351:3: string_expression
                    {
                    pushFollow(FOLLOW_string_expression_in_term1129);
                    string_expression2=string_expression();

                    state._fsp--;



                                         result = new ArrayList<String>(1);
                                         result.add((string_expression2!=null?string_expression2.result:null));
                                        

                    }
                    break;
                case 2 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:356:5: string_list
                    {
                    pushFollow(FOLLOW_string_list_in_term1158);
                    string_list3=string_list();

                    state._fsp--;



                                     result = string_list3;
                                    

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "term"


    public static class expression_return extends ParserRuleReturnScope {
        public ArrayList < String > result;
    };


    // $ANTLR start "expression"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:362:1: expression returns [ArrayList < String > result] : term ;
    public final GPRParser.expression_return expression() throws RecognitionException {
        GPRParser.expression_return retval = new GPRParser.expression_return();
        retval.start = input.LT(1);


        ArrayList < String > term4 =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:363:3: ( term )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:365:3: term
            {
            pushFollow(FOLLOW_term_in_expression1199);
            term4=term();

            state._fsp--;



                    retval.result = term4;
                   

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "expression"



    // $ANTLR start "simple_declarative_item"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:371:1: simple_declarative_item : ( variable_declaration | typed_variable_declaration | attribute_declaration | empty_declaration );
    public final void simple_declarative_item() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:372:3: ( variable_declaration | typed_variable_declaration | attribute_declaration | empty_declaration )
            int alt6=4;
            switch ( input.LA(1) ) {
            case IDENTIFIER:
                {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==36) ) {
                    alt6=1;
                }
                else if ( (LA6_1==35) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
                }
                break;
            case FOR:
                {
                alt6=3;
                }
                break;
            case NULL:
                {
                alt6=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }

            switch (alt6) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:373:3: variable_declaration
                    {
                    pushFollow(FOLLOW_variable_declaration_in_simple_declarative_item1224);
                    variable_declaration();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:374:5: typed_variable_declaration
                    {
                    pushFollow(FOLLOW_typed_variable_declaration_in_simple_declarative_item1230);
                    typed_variable_declaration();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:375:5: attribute_declaration
                    {
                    pushFollow(FOLLOW_attribute_declaration_in_simple_declarative_item1236);
                    attribute_declaration();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:376:5: empty_declaration
                    {
                    pushFollow(FOLLOW_empty_declaration_in_simple_declarative_item1242);
                    empty_declaration();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "simple_declarative_item"



    // $ANTLR start "attribute_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:379:1: attribute_declaration : ( indexed_attribute_declaration | simple_attribute_declaration );
    public final void attribute_declaration() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:380:3: ( indexed_attribute_declaration | simple_attribute_declaration )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==FOR) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==IDENTIFIER) ) {
                    int LA7_2 = input.LA(3);

                    if ( (LA7_2==30) ) {
                        alt7=1;
                    }
                    else if ( (LA7_2==USE) ) {
                        alt7=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 7, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:381:3: indexed_attribute_declaration
                    {
                    pushFollow(FOLLOW_indexed_attribute_declaration_in_attribute_declaration1258);
                    indexed_attribute_declaration();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:382:5: simple_attribute_declaration
                    {
                    pushFollow(FOLLOW_simple_attribute_declaration_in_attribute_declaration1264);
                    simple_attribute_declaration();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "attribute_declaration"



    // $ANTLR start "indexed_attribute_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:385:1: indexed_attribute_declaration : FOR simple_name '(' STRING_LITERAL ')' USE expression ';' ;
    public final void indexed_attribute_declaration() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:386:3: ( FOR simple_name '(' STRING_LITERAL ')' USE expression ';' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:387:3: FOR simple_name '(' STRING_LITERAL ')' USE expression ';'
            {
            match(input,FOR,FOLLOW_FOR_in_indexed_attribute_declaration1279); 

            pushFollow(FOLLOW_simple_name_in_indexed_attribute_declaration1281);
            simple_name();

            state._fsp--;


            match(input,30,FOLLOW_30_in_indexed_attribute_declaration1283); 

            match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_indexed_attribute_declaration1285); 

            match(input,31,FOLLOW_31_in_indexed_attribute_declaration1287); 

            match(input,USE,FOLLOW_USE_in_indexed_attribute_declaration1289); 

            pushFollow(FOLLOW_expression_in_indexed_attribute_declaration1291);
            expression();

            state._fsp--;


            match(input,37,FOLLOW_37_in_indexed_attribute_declaration1293); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "indexed_attribute_declaration"



    // $ANTLR start "simple_attribute_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:390:1: simple_attribute_declaration : FOR att_name= simple_name USE att_value= expression ';' {...}?;
    public final void simple_attribute_declaration() throws RecognitionException {
        GPRParser.simple_name_return att_name =null;

        GPRParser.expression_return att_value =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:391:3: ( FOR att_name= simple_name USE att_value= expression ';' {...}?)
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:392:3: FOR att_name= simple_name USE att_value= expression ';' {...}?
            {
            match(input,FOR,FOLLOW_FOR_in_simple_attribute_declaration1308); 

            pushFollow(FOLLOW_simple_name_in_simple_attribute_declaration1312);
            att_name=simple_name();

            state._fsp--;


            match(input,USE,FOLLOW_USE_in_simple_attribute_declaration1314); 

            pushFollow(FOLLOW_expression_in_simple_attribute_declaration1318);
            att_value=expression();

            state._fsp--;


            match(input,37,FOLLOW_37_in_simple_attribute_declaration1320); 

            if ( !((!this.isSimpleAttributeDefined((att_name!=null?input.toString(att_name.start,att_name.stop):null)))) ) {
                throw new FailedPredicateException(input, "simple_attribute_declaration", "!this.isSimpleAttributeDefined($att_name.text)");
            }


                                                                                                                       this.addNewSimpleAttribute((att_name!=null?input.toString(att_name.start,att_name.stop):null), (att_value!=null?att_value.result:null));
                                                                                                                      

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "simple_attribute_declaration"



    // $ANTLR start "attribute_designator"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:399:1: attribute_designator : ( simple_name | simple_name '(' STRING_LITERAL ')' );
    public final void attribute_designator() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:400:3: ( simple_name | simple_name '(' STRING_LITERAL ')' )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==IDENTIFIER) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==EOF) ) {
                    alt8=1;
                }
                else if ( (LA8_1==30) ) {
                    alt8=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:401:3: simple_name
                    {
                    pushFollow(FOLLOW_simple_name_in_attribute_designator1449);
                    simple_name();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:402:5: simple_name '(' STRING_LITERAL ')'
                    {
                    pushFollow(FOLLOW_simple_name_in_attribute_designator1455);
                    simple_name();

                    state._fsp--;


                    match(input,30,FOLLOW_30_in_attribute_designator1457); 

                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_attribute_designator1459); 

                    match(input,31,FOLLOW_31_in_attribute_designator1461); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "attribute_designator"



    // $ANTLR start "external_value"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:405:1: external_value : ( EXTERNAL '(' STRING_LITERAL ( ',' STRING_LITERAL )* ')' |);
    public final void external_value() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:406:3: ( EXTERNAL '(' STRING_LITERAL ( ',' STRING_LITERAL )* ')' |)
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==EXTERNAL) ) {
                alt10=1;
            }
            else if ( ((LA10_0 >= 31 && LA10_0 <= 32)||LA10_0==37) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:407:3: EXTERNAL '(' STRING_LITERAL ( ',' STRING_LITERAL )* ')'
                    {
                    match(input,EXTERNAL,FOLLOW_EXTERNAL_in_external_value1476); 

                    match(input,30,FOLLOW_30_in_external_value1478); 

                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_external_value1480); 

                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:407:31: ( ',' STRING_LITERAL )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==32) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:407:32: ',' STRING_LITERAL
                    	    {
                    	    match(input,32,FOLLOW_32_in_external_value1483); 

                    	    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_external_value1485); 

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);


                    match(input,31,FOLLOW_31_in_external_value1489); 

                    }
                    break;
                case 2 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:409:3: 
                    {
                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "external_value"



    // $ANTLR start "declarative_item"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:413:1: declarative_item : simple_declarative_item ;
    public final void declarative_item() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:414:3: ( simple_declarative_item )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:415:3: simple_declarative_item
            {
            pushFollow(FOLLOW_simple_declarative_item_in_declarative_item1510);
            simple_declarative_item();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "declarative_item"



    // $ANTLR start "simple_project_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:418:1: simple_project_declaration returns [GprProject result] : PROJECT begin_project_name= name IS ( declarative_item )* END end_project_name= name ';' EOF {...}?;
    public final GprProject simple_project_declaration() throws RecognitionException {
        GprProject result = null;


        GPRParser.name_return begin_project_name =null;

        GPRParser.name_return end_project_name =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:419:3: ( PROJECT begin_project_name= name IS ( declarative_item )* END end_project_name= name ';' EOF {...}?)
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:420:3: PROJECT begin_project_name= name IS ( declarative_item )* END end_project_name= name ';' EOF {...}?
            {
            match(input,PROJECT,FOLLOW_PROJECT_in_simple_project_declaration1530); 

            pushFollow(FOLLOW_name_in_simple_project_declaration1534);
            begin_project_name=name();

            state._fsp--;


            match(input,IS,FOLLOW_IS_in_simple_project_declaration1536); 

            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:420:38: ( declarative_item )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0 >= FOR && LA11_0 <= IDENTIFIER)||LA11_0==NULL) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:420:38: declarative_item
            	    {
            	    pushFollow(FOLLOW_declarative_item_in_simple_project_declaration1538);
            	    declarative_item();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            match(input,END,FOLLOW_END_in_simple_project_declaration1541); 

            pushFollow(FOLLOW_name_in_simple_project_declaration1545);
            end_project_name=name();

            state._fsp--;


            match(input,37,FOLLOW_37_in_simple_project_declaration1547); 

            match(input,EOF,FOLLOW_EOF_in_simple_project_declaration1549); 

            if ( !(((begin_project_name!=null?input.toString(begin_project_name.start,begin_project_name.stop):null).equals((end_project_name!=null?input.toString(end_project_name.start,end_project_name.stop):null)))) ) {
                throw new FailedPredicateException(input, "simple_project_declaration", "$begin_project_name.text.equals($end_project_name.text)");
            }


                                                                                                                                                                 return new GprProject((begin_project_name!=null?input.toString(begin_project_name.start,begin_project_name.stop):null));
                                                                                                                                                                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "simple_project_declaration"



    // $ANTLR start "path_name"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:426:1: path_name returns [String result] : STRING_LITERAL ;
    public final String path_name() throws RecognitionException {
        String result = null;


        Token STRING_LITERAL5=null;

        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:427:3: ( STRING_LITERAL )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:428:3: STRING_LITERAL
            {
            STRING_LITERAL5=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_path_name1721); 


                              result = (STRING_LITERAL5!=null?STRING_LITERAL5.getText():null).replaceAll("\"", "");
                             

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "path_name"



    // $ANTLR start "with_clause"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:434:1: with_clause : WITH first_path= path_name ( ',' second_path= path_name )* ';' ;
    public final void with_clause() throws RecognitionException {
        String first_path =null;

        String second_path =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:435:3: ( WITH first_path= path_name ( ',' second_path= path_name )* ';' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:436:3: WITH first_path= path_name ( ',' second_path= path_name )* ';'
            {
            match(input,WITH,FOLLOW_WITH_in_with_clause1756); 

            pushFollow(FOLLOW_path_name_in_with_clause1760);
            first_path=path_name();

            state._fsp--;



                                         this.withedProjects.add(first_path);
                                        

            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:440:3: ( ',' second_path= path_name )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==32) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:440:4: ',' second_path= path_name
            	    {
            	    match(input,32,FOLLOW_32_in_with_clause1796); 

            	    pushFollow(FOLLOW_path_name_in_with_clause1800);
            	    second_path=path_name();

            	    state._fsp--;



            	                                  this.withedProjects.add(second_path);
            	                                 

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            match(input,37,FOLLOW_37_in_with_clause1836); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "with_clause"



    // $ANTLR start "context_clause"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:446:1: context_clause : ( with_clause )* ;
    public final void context_clause() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:447:3: ( ( with_clause )* )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:448:3: ( with_clause )*
            {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:448:3: ( with_clause )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==WITH) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:448:3: with_clause
            	    {
            	    pushFollow(FOLLOW_with_clause_in_context_clause1851);
            	    with_clause();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "context_clause"



    // $ANTLR start "project"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:451:1: project : context_clause project_declaration ;
    public final void project() throws RecognitionException {
        GprProject project_declaration6 =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:452:3: ( context_clause project_declaration )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:453:3: context_clause project_declaration
            {
            pushFollow(FOLLOW_context_clause_in_project1867);
            context_clause();

            state._fsp--;


            pushFollow(FOLLOW_project_declaration_in_project1869);
            project_declaration6=project_declaration();

            state._fsp--;



                                                  this.project = project_declaration6;
                                                  this.processSimpleAttributes();
                                                  this.processWithedProjects();
                                                 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "project"



    // $ANTLR start "project_declaration"
    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:461:1: project_declaration returns [GprProject result] : simple_project_declaration ;
    public final GprProject project_declaration() throws RecognitionException {
        GprProject result = null;


        GprProject simple_project_declaration7 =null;


        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:462:3: ( simple_project_declaration )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:463:3: simple_project_declaration
            {
            pushFollow(FOLLOW_simple_project_declaration_in_project_declaration1928);
            simple_project_declaration7=simple_project_declaration();

            state._fsp--;


            result = simple_project_declaration7;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "project_declaration"

    // Delegated rules


 

    public static final BitSet FOLLOW_33_in_comment447 = new BitSet(new long[]{0x0000003FFFFFFFF2L});
    public static final BitSet FOLLOW_IDENTIFIER_in_simple_name578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_name_in_name593 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_name596 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_simple_name_in_name598 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_NULL_in_empty_declaration615 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_empty_declaration617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_name_in_variable_declaration730 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_variable_declaration732 = new BitSet(new long[]{0x0000000040802800L});
    public static final BitSet FOLLOW_expression_in_variable_declaration736 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_variable_declaration738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_name_in_typed_variable_declaration811 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_typed_variable_declaration813 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_name_in_typed_variable_declaration817 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_typed_variable_declaration819 = new BitSet(new long[]{0x0000000000802800L});
    public static final BitSet FOLLOW_string_expression_in_typed_variable_declaration823 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_typed_variable_declaration825 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_string_expression967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_name_in_string_expression994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_value_in_string_expression1000 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_string_list1025 = new BitSet(new long[]{0x0000000000802800L});
    public static final BitSet FOLLOW_string_expression_in_string_list1029 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_32_in_string_list1067 = new BitSet(new long[]{0x0000000000802800L});
    public static final BitSet FOLLOW_string_expression_in_string_list1071 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_31_in_string_list1109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_expression_in_term1129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_list_in_term1158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression1199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_declaration_in_simple_declarative_item1224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typed_variable_declaration_in_simple_declarative_item1230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attribute_declaration_in_simple_declarative_item1236 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_empty_declaration_in_simple_declarative_item1242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_indexed_attribute_declaration_in_attribute_declaration1258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_attribute_declaration_in_attribute_declaration1264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_indexed_attribute_declaration1279 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_simple_name_in_indexed_attribute_declaration1281 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_indexed_attribute_declaration1283 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_indexed_attribute_declaration1285 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_indexed_attribute_declaration1287 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_USE_in_indexed_attribute_declaration1289 = new BitSet(new long[]{0x0000000040802800L});
    public static final BitSet FOLLOW_expression_in_indexed_attribute_declaration1291 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_indexed_attribute_declaration1293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_simple_attribute_declaration1308 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_simple_name_in_simple_attribute_declaration1312 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_USE_in_simple_attribute_declaration1314 = new BitSet(new long[]{0x0000000040802800L});
    public static final BitSet FOLLOW_expression_in_simple_attribute_declaration1318 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_simple_attribute_declaration1320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_name_in_attribute_designator1449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_name_in_attribute_designator1455 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_attribute_designator1457 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_attribute_designator1459 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_attribute_designator1461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTERNAL_in_external_value1476 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_external_value1478 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_external_value1480 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_32_in_external_value1483 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_external_value1485 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_31_in_external_value1489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_declarative_item_in_declarative_item1510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROJECT_in_simple_project_declaration1530 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_name_in_simple_project_declaration1534 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_IS_in_simple_project_declaration1536 = new BitSet(new long[]{0x0000000000023100L});
    public static final BitSet FOLLOW_declarative_item_in_simple_project_declaration1538 = new BitSet(new long[]{0x0000000000023100L});
    public static final BitSet FOLLOW_END_in_simple_project_declaration1541 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_name_in_simple_project_declaration1545 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_simple_project_declaration1547 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_simple_project_declaration1549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_path_name1721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_with_clause1756 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_path_name_in_with_clause1760 = new BitSet(new long[]{0x0000002100000000L});
    public static final BitSet FOLLOW_32_in_with_clause1796 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_path_name_in_with_clause1800 = new BitSet(new long[]{0x0000002100000000L});
    public static final BitSet FOLLOW_37_in_with_clause1836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_with_clause_in_context_clause1851 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_context_clause_in_project1867 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_project_declaration_in_project1869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_project_declaration_in_project_declaration1928 = new BitSet(new long[]{0x0000000000000002L});

}