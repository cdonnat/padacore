grammar GPR;

@header {
package org.padacore.core.gnat.project;

import java.util.ArrayList;
}

@lexer::header {
package org.padacore.core.gnat.project;
} 

@parser::members { 
  private GPRBuilder builder = new GPRBuilder();
}
  

project returns [GPRBuilder result]
  : 
  context_clause project_declaration EOF
  {result = builder;}
  ;

context_clause : with_clause*;

with_clause 
  : WITH 
    first_path=path_name {builder.addReferencedProject(first_path);}
    (',' other_path=path_name {builder.addReferencedProject(other_path);})* 
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
  {builder.setProjectName($begin_project_name.text);}
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

declarative_item // TODO
  :
  simple_declarative_item 
  ;
  
 simple_declarative_item // TODO
  :
  variable_declaration
  | typed_variable_declaration
  | attribute_declaration
  | empty_declaration
  ;

typed_variable_declaration 
  :
  simple_name 
  ':'
   name 
   ':='
   string_expression
   ';'
   ;
   
attribute_declaration
  :
  simple_attribute_declaration 
  | indexed_attribute_declaration
  ;

indexed_attribute_declaration 
  :
  FOR simple_name '(' STRING_LITERAL ')' USE expression 
  ;

simple_attribute_declaration
 :
 'for' 
 attribute_designator 
 'use' 
 expression 
 ';'
 {builder.addSimpleAttribute($attribute_designator.result, $expression.result);}
 ;
 
attribute_designator returns [String result]
  :
  att = simple_name {$result = $att.text;}
  | att = simple_name ( '(' STRING_LITERAL ')' ) {$result = $att.text + "(\"" + $STRING_LITERAL.text + "\")";}
  ; 
 
external_value returns [String result] // FIXME : return an array?
  : 
  EXTERNAL {result = $EXTERNAL.text;} 
  '(' {result += "(";}
  s1 = STRING_LITERAL { result += $s1.text;} 
  (',' s2 = STRING_LITERAL { result += "," + $s2.text;})* 
  ')'
  ; 

variable_declaration 
  :
  simple_name 
  ':='
  expression
  ';'
  ;

expression returns [ArrayList<String> result]
  :
  first = term {result = $first.result;}
  ( '&' term)* // TODO : add to result
  ;

term returns [ArrayList<String> result]
  :
  string_expression 
    {result = new ArrayList<String>(1); result.add($string_expression.result);}
  | string_list 
    {$result = $string_list.result;}
  ;
  
string_expression returns [String result] // TODO : complete rule
  :
  STRING_LITERAL {result = $STRING_LITERAL.text;}
  | name {result = $name.result;}
  | external_value {result = $external_value.result;}
  ;

string_list returns [ArrayList<String> result] // TODO : complete rule 
  :
  '(' {result = new ArrayList<String>();}
  first = string_expression {result.add($first.result);} 
  ( ',' other = string_expression {result.add($other.result);}  )* 
  ')'
  ;

empty_declaration
  :
  NULL ';'
  ;
  
STRING_LITERAL  
  : 
  '"' 
  (STRING_ELEMENT)* 
  '"';
  
ALL     : 'all';
AT      : 'at';
CASE    : 'case';
END     : 'end';
FOR     : 'for';
IS      : 'is';
LIMITED : 'limited';
NULL    : 'null';
OTHERS  : 'others';
PACKAGE : 'package';
PROJECT : 'project';
RENAMES : 'renames';
TYPE    : 'type';
USE     : 'use';
WHEN    : 'when';
WITH    : 'with';
EXTENDS : 'extends';
EXTERNAL: 'external';

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