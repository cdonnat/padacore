grammar test;

@header {
package org.padacore.core.gnat.project;
}

@lexer::header {
package org.padacore.core.gnat.project;
} 

@parser::members { 
  private GPRBuilder builder = new GPRBuilder();
}
  

project
  : 
  context_clause project_declaration EOF
  ;

context_clause : with_clause*;

with_clause 
  : WITH 
    first_path=path_name {builder.addReferencedProject(first_path);}
    (',' other_path=path_name)* {builder.addReferencedProject(other_path);}
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

name
  :
  simple_name ('.' simple_name)*
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
 ;
 
attribute_designator 
  :
  simple_name 
  | simple_name ( '(' STRING_LITERAL ')' )
  ; 
 
external_value 
  : 
  EXTERNAL '(' STRING_LITERAL (',' STRING_LITERAL)* ')'
  ; 

variable_declaration 
  :
  simple_name 
  ':='
  expression
  ';'
  ;

expression 
  :
  term 
  ( '&' term)*
  ;

term 
  :
  string_expression 
  | string_list
  ;
  
string_expression // TODO
  :
  STRING_LITERAL
  | name
  | external_value
  ;

string_list // TODO 
  :
  '(' string_expression ( ',' string_expression  )* ')'
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