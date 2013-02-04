grammar Gpr;

@header {
package org.padacore.core.gnat;

import java.util.ArrayList;
}

@lexer::header {

package org.padacore.core.gnat;
}

@lexer::members {
  private List<RecognitionException> exceptions = new ArrayList<RecognitionException>();
 
    public List<RecognitionException> getExceptions() {
        return exceptions;
    }
 
    @Override
    public void reportError(RecognitionException e) {
        exceptions.add(e);
    }
} 

@parser::members {
 private GprLoader gprLoader = new GprLoader();
  private List<RecognitionException> exceptions = new ArrayList<RecognitionException>();
 
    public List<RecognitionException> getExceptions() {
        return exceptions;
    }
 
    @Override
    public void reportError(RecognitionException e) {
        exceptions.add(e);
    }
 
 public GprParser(GprLoader gprLoader, TokenStream input) {
        this(input, new RecognizerSharedState());
        this.gprLoader = gprLoader;
    }
}
  
project
  : 
  context_clause project_declaration EOF
  ;

context_clause : with_clause*;

with_clause 
  : WITH 
    first_path=path_name {gprLoader.addProject($first_path.result);}
    (',' other_path=path_name {gprLoader.addProject($other_path.result);})*  
    ';';

path_name returns [String result] 
  : 
  STRING_LITERAL
  {result = $STRING_LITERAL.text.replaceAll("\"", "");}
  ;

project_declaration : simple_project_declaration;

simple_project_declaration 
  : 
  PROJECT 
  begin_project_name=name 
  IS
  declarative_item*
  END
  end_project_name=name
  ';'
  {$begin_project_name.text.equals($end_project_name.text)}?
  ;

name returns [String result]
  :
  first = simple_name {$result = $first.text;} 
  ('.' other = simple_name {$result += "." + $other.text;})*
  ;
  
simple_name
  :
  IDENTIFIER
  ;

declarative_item 
  :
  simple_declarative_item 
  | typed_string_declaration
  | package_declaration
  ;
  
simple_declarative_item
  :
  variable_declaration
  | typed_variable_declaration
  | attribute_declaration
  | case_statement
  | empty_declaration
  ;

typed_string_declaration 
  :
  TYPE simple_name IS '(' STRING_LITERAL (',' STRING_LITERAL)* ')' ';'
  ;

case_statement 
  :
  CASE name IS (case_item)* END CASE ';'
  ;

case_item
  :
  WHEN discrete_choice_list '=>'
  (case_statement
  | attribute_declaration
  | variable_declaration
  | empty_declaration)+
  ;
  
discrete_choice_list
  :
  STRING_LITERAL ( '|' STRING_LITERAL)* 
  | OTHERS
  ;

package_declaration 
  :
  package_spec 
  | package_renaming 
  | package_extension
  ;
  
package_spec
  :
  PACKAGE begin_pkg_name = simple_name IS (simple_declarative_item)* END end_package_name = simple_name ';'
  {$begin_pkg_name.text.equals($end_package_name.text)}?
  {gprLoader.getCurrentContext().addReference(new Context($begin_pkg_name.text));}
  ;
   
package_renaming
  :
  PACKAGE simple_name RENAMES simple_name '.' simple_name ';'//TODO
  ;
     
package_extension
  :
  PACKAGE begin_package_name = simple_name EXTENDS simple_name '.' simple_name IS //TODO
  (simple_declarative_item)*
  END end_package_name = simple_name ';'
  {$begin_package_name.text.equals($end_package_name.text)}?
  ;

typed_variable_declaration 
  :
  simple_name 
  ':'
   name 
   ':='
   string_expression
   ';'
   {!gprLoader.variableIsDefined($simple_name.text)}?
   {gprLoader.addVariable ($simple_name.text, $string_expression.result);}
   ;
   
attribute_declaration
 :
 FOR 
 attribute_designator 
 USE
 expression 
 ';'
 {gprLoader.addAttribute($attribute_designator.result, $expression.result);}
 ;
 
attribute_designator returns [String result]
  :
  att = simple_name {$result = $att.text;}
  | att = simple_name ( '(' STRING_LITERAL ')' ) {$result = $att.text + "(" + $STRING_LITERAL.text + ")";}
  ; 
 
 attribute_reference returns [Symbol result]
  :
  attribute_prefix '\'' simple_name ('(' STRING_LITERAL ')' )? {
  
   Context context = $attribute_prefix.result;
   String attributeName = $simple_name.text;
   if($STRING_LITERAL.text !=null) {
      attributeName += "(" + $STRING_LITERAL.text + ")";
   }
   result = context.getAttribute(attributeName); }
  ;
 
 attribute_prefix returns [Context result]
  :
  PROJECT { result = gprLoader.getCurrentContext(); }
  | project_name = simple_name ('.' package_name = simple_name)? 
  {String ctxtName = $project_name.text;
  if($package_name.text != null) {
    ctxtName += "." + $package_name.text;
  }
  result = gprLoader.getContextByName(ctxtName);}  
  ;
 
external_value returns [Symbol result]
  : 
  EXTERNAL
  '(' 
  STRING_LITERAL 
  ',' defaultValue = STRING_LITERAL { $result = Symbol.CreateString($defaultValue.text);} 
  ')'
  ; 

variable_declaration 
  :
  simple_name 
  ':='
  expression
  ';'
  {gprLoader.addVariable ($simple_name.text, $expression.result);}
  ;

expression returns [Symbol result]
  :
  first = term {result = $first.result;}
  ( '&' other = term {result = Symbol.Concat(result, $other.result);} )* 
  ;

term returns [Symbol result]
  : 
  string_expression 
    {$result = $string_expression.result;}
  | string_list 
    {$result = $string_list.result;}
  ;
  
string_expression returns [Symbol result] // TODO : complete rule
  :
  STRING_LITERAL {result = Symbol.CreateString($STRING_LITERAL.text);}
  | name {result = gprLoader.getVariable($name.result);}
  | external_value {result = $external_value.result;}
  | attribute_reference {result = $attribute_reference.result;}
  ;

string_list returns [Symbol result] // TODO : complete rule 
  :
  '(' {result = Symbol.CreateStringList(new ArrayList<String>());}
  first = expression? {if (first != null) {result = Symbol.Concat (result, $first.result);}} 
  ( ',' other = expression {result = Symbol.Concat(result, $other.result);}  )* 
  ')'
  ;

empty_declaration
  :
  NULL ';'
  ;
  
COMMENT
  :
  '--'
  ~(
    '\n'
    | '\r'
    | '\f'
   )*
  { $channel = HIDDEN; };
  
STRING_LITERAL  
  : 
  '"' 
  (STRING_ELEMENT)* 
  '"';
  
ALL     : A L L;
AT      : A T;
CASE    : C A S E;
END     : E N D;
FOR     : F O R;
IS      : I S;
LIMITED : L I M I T E D;
NULL    : N U L L;
OTHERS  : O T H E R S;
PACKAGE : P A C K A G E;
PROJECT : P R O J E C T;
RENAMES : R E N A M E S;
TYPE    : T Y P E;
USE     : U S E;
WHEN    : W H E N;
WITH    : W I T H;
EXTENDS : E X T E N D S;
EXTERNAL: E X T E R N A L;

IDENTIFIER : (UPPER_CASE_LETTER | LOWER_CASE_LETTER) 
              (('_')? (UPPER_CASE_LETTER | LOWER_CASE_LETTER | DIGIT))*;
                                 
fragment UPPER_CASE_LETTER : 'A'..'Z';
fragment LOWER_CASE_LETTER : 'a'..'z';
fragment DIGIT : '0'..'9' ;
fragment STRING_ELEMENT 
  :
  (
    '"' '"' 
    | ~( '"' | '\n' | '\r')
  )
  ;
WS  : 
  (' '
  |'\t'
  |'\r'
  |'\b'
  |'\n' 
  |'\f' )+ { $channel = HIDDEN; };
  
fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');