// $ANTLR 3.4 /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g 2012-10-02 23:02:08

package org.padacore.gnat.project;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class GPRLexer extends Lexer {
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
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public GPRLexer() {} 
    public GPRLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public GPRLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g"; }

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:6:7: ( '(' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:6:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:7:7: ( ')' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:7:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:8:7: ( ',' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:8:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:9:7: ( '--' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:9:9: '--'
            {
            match("--"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:10:7: ( '.' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:10:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:11:7: ( ':' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:11:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:12:7: ( ':=' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:12:9: ':='
            {
            match(":="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:13:7: ( ';' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:13:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:14:7: ( '\\n' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:14:9: '\\n'
            {
            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:15:7: ( '\\r' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:15:9: '\\r'
            {
            match('\r'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "UPPER_CASE_LETTER"
    public final void mUPPER_CASE_LETTER() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:117:3: ( 'A' .. 'Z' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UPPER_CASE_LETTER"

    // $ANTLR start "LOWER_CASE_LETTER"
    public final void mLOWER_CASE_LETTER() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:123:3: ( 'a' .. 'z' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LOWER_CASE_LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:129:3: ( '0' .. '9' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "ALL"
    public final void mALL() throws RecognitionException {
        try {
            int _type = ALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:135:3: ( 'all' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:136:3: 'all'
            {
            match("all"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ALL"

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:140:3: ( 'at' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:141:3: 'at'
            {
            match("at"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AT"

    // $ANTLR start "CASE"
    public final void mCASE() throws RecognitionException {
        try {
            int _type = CASE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:145:3: ( 'case' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:146:3: 'case'
            {
            match("case"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CASE"

    // $ANTLR start "END"
    public final void mEND() throws RecognitionException {
        try {
            int _type = END;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:150:3: ( 'end' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:151:3: 'end'
            {
            match("end"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "END"

    // $ANTLR start "FOR"
    public final void mFOR() throws RecognitionException {
        try {
            int _type = FOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:155:3: ( 'for' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:156:3: 'for'
            {
            match("for"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FOR"

    // $ANTLR start "IS"
    public final void mIS() throws RecognitionException {
        try {
            int _type = IS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:160:3: ( 'is' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:161:3: 'is'
            {
            match("is"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IS"

    // $ANTLR start "LIMITED"
    public final void mLIMITED() throws RecognitionException {
        try {
            int _type = LIMITED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:165:3: ( 'limited' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:166:3: 'limited'
            {
            match("limited"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LIMITED"

    // $ANTLR start "NULL"
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:170:3: ( 'null' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:171:3: 'null'
            {
            match("null"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NULL"

    // $ANTLR start "OTHERS"
    public final void mOTHERS() throws RecognitionException {
        try {
            int _type = OTHERS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:175:3: ( 'others' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:176:3: 'others'
            {
            match("others"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OTHERS"

    // $ANTLR start "PACKAGE"
    public final void mPACKAGE() throws RecognitionException {
        try {
            int _type = PACKAGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:180:3: ( 'package' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:181:3: 'package'
            {
            match("package"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PACKAGE"

    // $ANTLR start "RENAMES"
    public final void mRENAMES() throws RecognitionException {
        try {
            int _type = RENAMES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:185:3: ( 'renames' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:186:3: 'renames'
            {
            match("renames"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RENAMES"

    // $ANTLR start "TYPE"
    public final void mTYPE() throws RecognitionException {
        try {
            int _type = TYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:190:3: ( 'type' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:191:3: 'type'
            {
            match("type"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TYPE"

    // $ANTLR start "USE"
    public final void mUSE() throws RecognitionException {
        try {
            int _type = USE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:195:3: ( 'use' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:196:3: 'use'
            {
            match("use"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "USE"

    // $ANTLR start "WHEN"
    public final void mWHEN() throws RecognitionException {
        try {
            int _type = WHEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:200:3: ( 'when' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:201:3: 'when'
            {
            match("when"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHEN"

    // $ANTLR start "WITH"
    public final void mWITH() throws RecognitionException {
        try {
            int _type = WITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:205:3: ( 'with' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:206:3: 'with'
            {
            match("with"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WITH"

    // $ANTLR start "EXTENDS"
    public final void mEXTENDS() throws RecognitionException {
        try {
            int _type = EXTENDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:210:3: ( 'extends' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:211:3: 'extends'
            {
            match("extends"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EXTENDS"

    // $ANTLR start "EXTERNAL"
    public final void mEXTERNAL() throws RecognitionException {
        try {
            int _type = EXTERNAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:215:3: ( 'external' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:216:3: 'external'
            {
            match("external"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EXTERNAL"

    // $ANTLR start "PROJECT"
    public final void mPROJECT() throws RecognitionException {
        try {
            int _type = PROJECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:220:3: ( 'project' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:221:3: 'project'
            {
            match("project"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PROJECT"

    // $ANTLR start "END_OF_LINE"
    public final void mEND_OF_LINE() throws RecognitionException {
        try {
            int _type = END_OF_LINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:225:3: ( '\\n' ( '\\r' )? )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:226:3: '\\n' ( '\\r' )?
            {
            match('\n'); 

            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:226:8: ( '\\r' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='\r') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:226:9: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "END_OF_LINE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:230:3: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:231:3: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:231:3: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '\t' && LA2_0 <= '\n')||LA2_0=='\r'||LA2_0==' ') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:
            	    {
            	    if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);



                _channel = HIDDEN;
               

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "IDENTIFIER"
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:253:3: ( ( LOWER_CASE_LETTER | UPPER_CASE_LETTER ) ( ( '_' )? ( LOWER_CASE_LETTER | UPPER_CASE_LETTER | DIGIT ) )* )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:254:3: ( LOWER_CASE_LETTER | UPPER_CASE_LETTER ) ( ( '_' )? ( LOWER_CASE_LETTER | UPPER_CASE_LETTER | DIGIT ) )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:258:3: ( ( '_' )? ( LOWER_CASE_LETTER | UPPER_CASE_LETTER | DIGIT ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '0' && LA4_0 <= '9')||(LA4_0 >= 'A' && LA4_0 <= 'Z')||LA4_0=='_'||(LA4_0 >= 'a' && LA4_0 <= 'z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:259:5: ( '_' )? ( LOWER_CASE_LETTER | UPPER_CASE_LETTER | DIGIT )
            	    {
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:259:5: ( '_' )?
            	    int alt3=2;
            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0=='_') ) {
            	        alt3=1;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:259:6: '_'
            	            {
            	            match('_'); 

            	            }
            	            break;

            	    }


            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IDENTIFIER"

    // $ANTLR start "STRING_ELEMENT"
    public final void mSTRING_ELEMENT() throws RecognitionException {
        String element = null;


        try {
            int character;


            element = new String();

            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:289:3: ( '\"' '\"' |character=~ ( '\"' | '\\n' | '\\r' ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\"') ) {
                alt5=1;
            }
            else if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\t')||(LA5_0 >= '\u000B' && LA5_0 <= '\f')||(LA5_0 >= '\u000E' && LA5_0 <= '!')||(LA5_0 >= '#' && LA5_0 <= '\uFFFF')) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:290:3: '\"' '\"'
                    {
                    match('\"'); 

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:291:5: character=~ ( '\"' | '\\n' | '\\r' )
                    {
                    character= input.LA(1);

                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }



                        element = new Integer(character).toString();
                       

                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING_ELEMENT"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:303:3: ( '\"' ( STRING_ELEMENT )* '\"' )
            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:304:3: '\"' ( STRING_ELEMENT )* '\"'
            {
            match('\"'); 

            // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:304:7: ( STRING_ELEMENT )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\"') ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1=='\"') ) {
                        alt6=1;
                    }


                }
                else if ( ((LA6_0 >= '\u0000' && LA6_0 <= '\t')||(LA6_0 >= '\u000B' && LA6_0 <= '\f')||(LA6_0 >= '\u000E' && LA6_0 <= '!')||(LA6_0 >= '#' && LA6_0 <= '\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:304:7: STRING_ELEMENT
            	    {
            	    mSTRING_ELEMENT(); 


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING_LITERAL"

    public void mTokens() throws RecognitionException {
        // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:8: ( T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | ALL | AT | CASE | END | FOR | IS | LIMITED | NULL | OTHERS | PACKAGE | RENAMES | TYPE | USE | WHEN | WITH | EXTENDS | EXTERNAL | PROJECT | END_OF_LINE | WS | IDENTIFIER | STRING_LITERAL )
        int alt7=32;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:10: T__30
                {
                mT__30(); 


                }
                break;
            case 2 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:16: T__31
                {
                mT__31(); 


                }
                break;
            case 3 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:22: T__32
                {
                mT__32(); 


                }
                break;
            case 4 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:28: T__33
                {
                mT__33(); 


                }
                break;
            case 5 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:34: T__34
                {
                mT__34(); 


                }
                break;
            case 6 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:40: T__35
                {
                mT__35(); 


                }
                break;
            case 7 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:46: T__36
                {
                mT__36(); 


                }
                break;
            case 8 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:52: T__37
                {
                mT__37(); 


                }
                break;
            case 9 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:58: T__38
                {
                mT__38(); 


                }
                break;
            case 10 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:64: T__39
                {
                mT__39(); 


                }
                break;
            case 11 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:70: ALL
                {
                mALL(); 


                }
                break;
            case 12 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:74: AT
                {
                mAT(); 


                }
                break;
            case 13 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:77: CASE
                {
                mCASE(); 


                }
                break;
            case 14 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:82: END
                {
                mEND(); 


                }
                break;
            case 15 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:86: FOR
                {
                mFOR(); 


                }
                break;
            case 16 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:90: IS
                {
                mIS(); 


                }
                break;
            case 17 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:93: LIMITED
                {
                mLIMITED(); 


                }
                break;
            case 18 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:101: NULL
                {
                mNULL(); 


                }
                break;
            case 19 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:106: OTHERS
                {
                mOTHERS(); 


                }
                break;
            case 20 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:113: PACKAGE
                {
                mPACKAGE(); 


                }
                break;
            case 21 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:121: RENAMES
                {
                mRENAMES(); 


                }
                break;
            case 22 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:129: TYPE
                {
                mTYPE(); 


                }
                break;
            case 23 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:134: USE
                {
                mUSE(); 


                }
                break;
            case 24 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:138: WHEN
                {
                mWHEN(); 


                }
                break;
            case 25 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:143: WITH
                {
                mWITH(); 


                }
                break;
            case 26 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:148: EXTENDS
                {
                mEXTENDS(); 


                }
                break;
            case 27 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:156: EXTERNAL
                {
                mEXTERNAL(); 


                }
                break;
            case 28 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:165: PROJECT
                {
                mPROJECT(); 


                }
                break;
            case 29 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:173: END_OF_LINE
                {
                mEND_OF_LINE(); 


                }
                break;
            case 30 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:185: WS
                {
                mWS(); 


                }
                break;
            case 31 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:188: IDENTIFIER
                {
                mIDENTIFIER(); 


                }
                break;
            case 32 :
                // /Users/Charles/Documents/workspace/padacore/org.padacore.core/src/org/padacore/core/gnat/project/GPR.g:1:199: STRING_LITERAL
                {
                mSTRING_LITERAL(); 


                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\6\uffff\1\33\1\uffff\1\34\1\36\15\30\6\uffff\1\60\1\uffff\1\30"+
        "\1\62\4\30\1\67\12\30\1\uffff\1\102\1\uffff\1\30\1\104\1\30\1\106"+
        "\1\uffff\7\30\1\116\2\30\1\uffff\1\121\1\uffff\1\30\1\uffff\1\30"+
        "\1\125\4\30\1\132\1\uffff\1\133\1\134\1\uffff\3\30\1\uffff\4\30"+
        "\3\uffff\3\30\1\147\3\30\1\153\1\30\1\155\1\uffff\1\156\1\157\1"+
        "\160\1\uffff\1\161\5\uffff";
    static final String DFA7_eofS =
        "\162\uffff";
    static final String DFA7_minS =
        "\1\11\5\uffff\1\75\1\uffff\2\11\1\154\1\141\1\156\1\157\1\163\1"+
        "\151\1\165\1\164\1\141\1\145\1\171\1\163\1\150\6\uffff\1\11\1\uffff"+
        "\1\154\1\60\1\163\1\144\1\164\1\162\1\60\1\155\1\154\1\150\1\143"+
        "\1\157\1\156\1\160\2\145\1\164\1\uffff\1\60\1\uffff\1\145\1\60\1"+
        "\145\1\60\1\uffff\1\151\1\154\1\145\1\153\1\152\1\141\1\145\1\60"+
        "\1\156\1\150\1\uffff\1\60\1\uffff\1\156\1\uffff\1\164\1\60\1\162"+
        "\1\141\1\145\1\155\1\60\1\uffff\2\60\1\uffff\1\144\1\156\1\145\1"+
        "\uffff\1\163\1\147\1\143\1\145\3\uffff\1\163\1\141\1\144\1\60\1"+
        "\145\1\164\1\163\1\60\1\154\1\60\1\uffff\3\60\1\uffff\1\60\5\uffff";
    static final String DFA7_maxS =
        "\1\172\5\uffff\1\75\1\uffff\2\40\1\164\1\141\1\170\1\157\1\163\1"+
        "\151\1\165\1\164\1\162\1\145\1\171\1\163\1\151\6\uffff\1\40\1\uffff"+
        "\1\154\1\172\1\163\1\144\1\164\1\162\1\172\1\155\1\154\1\150\1\143"+
        "\1\157\1\156\1\160\2\145\1\164\1\uffff\1\172\1\uffff\1\145\1\172"+
        "\1\145\1\172\1\uffff\1\151\1\154\1\145\1\153\1\152\1\141\1\145\1"+
        "\172\1\156\1\150\1\uffff\1\172\1\uffff\1\162\1\uffff\1\164\1\172"+
        "\1\162\1\141\1\145\1\155\1\172\1\uffff\2\172\1\uffff\1\144\1\156"+
        "\1\145\1\uffff\1\163\1\147\1\143\1\145\3\uffff\1\163\1\141\1\144"+
        "\1\172\1\145\1\164\1\163\1\172\1\154\1\172\1\uffff\3\172\1\uffff"+
        "\1\172\5\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\uffff\1\10\17\uffff\1\36\1\37\1\40"+
        "\1\7\1\6\1\11\1\uffff\1\12\21\uffff\1\35\1\uffff\1\14\4\uffff\1"+
        "\20\12\uffff\1\13\1\uffff\1\16\1\uffff\1\17\7\uffff\1\27\2\uffff"+
        "\1\15\3\uffff\1\22\4\uffff\1\26\1\30\1\31\12\uffff\1\23\3\uffff"+
        "\1\32\1\uffff\1\21\1\24\1\34\1\25\1\33";
    static final String DFA7_specialS =
        "\162\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\27\1\10\2\uffff\1\11\22\uffff\1\27\1\uffff\1\31\5\uffff\1"+
            "\1\1\2\2\uffff\1\3\1\4\1\5\13\uffff\1\6\1\7\5\uffff\32\30\6"+
            "\uffff\1\12\1\30\1\13\1\30\1\14\1\15\2\30\1\16\2\30\1\17\1\30"+
            "\1\20\1\21\1\22\1\30\1\23\1\30\1\24\1\25\1\30\1\26\3\30",
            "",
            "",
            "",
            "",
            "",
            "\1\32",
            "",
            "\2\27\2\uffff\1\35\22\uffff\1\27",
            "\2\27\2\uffff\1\27\22\uffff\1\27",
            "\1\37\7\uffff\1\40",
            "\1\41",
            "\1\42\11\uffff\1\43",
            "\1\44",
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51\20\uffff\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56\1\57",
            "",
            "",
            "",
            "",
            "",
            "",
            "\2\27\2\uffff\1\27\22\uffff\1\27",
            "",
            "\1\61",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "\1\103",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\105",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\117",
            "\1\120",
            "",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "\1\122\3\uffff\1\123",
            "",
            "\1\124",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "\1\135",
            "\1\136",
            "\1\137",
            "",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "",
            "",
            "",
            "\1\144",
            "\1\145",
            "\1\146",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\150",
            "\1\151",
            "\1\152",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\154",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | ALL | AT | CASE | END | FOR | IS | LIMITED | NULL | OTHERS | PACKAGE | RENAMES | TYPE | USE | WHEN | WITH | EXTENDS | EXTERNAL | PROJECT | END_OF_LINE | WS | IDENTIFIER | STRING_LITERAL );";
        }
    }
 

}