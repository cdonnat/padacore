grammar GPR;

@header {
package org.padacore.gnat.project;
}

@lexer::header {
package org.padacore.gnat.project;
}

// Fragment rules
fragment UPPER_CASE_LETTER : 
'A'..'Z';

fragment LOWER_CASE_LETTER :
'a'..'z'; 

fragment DIGIT :
'0'..'9';

// Reserved keywords
ALL 
  :  
  'all'
  ;  

AT
  :
  'at'
  ;

CASE
  :
  'case'
  ;

END
  :
  'end'
  ;

FOR
  :
  'for'
  ;

IS
  :
  'is'
  ;

LIMITED
  :
  'limited'
  ;

NULL
  :
  'null'
  ;

OTHERS
  :
  'others'
  ;

PACKAGE
  :
  'package'
  ;

RENAMES
  :
  'renames'
  ;

TYPE
  :
  'type'
  ;

USE
  :
  'use'
  ;

WHEN
  :
  'when'
  ;

WITH
  :
  'with'
  ;

EXTENDS
  :
  'extends'
  ;

EXTERNAL
  :
  'external'
  ;

PROJECT
  :
  'project'
  ;

END_OF_LINE
  :
  '\n' ('\r')?
  ;
  
WS
  :
  (
    ' '
    | '\t'
    | '\r'
    | '\n'
  )+
  
   {
    $channel = HIDDEN;
   }
  ;

comment
  :
  '--'
  ~(
    '\n'
    | '\r'
   )*
  ;
  
IDENTIFIER
  :
    (LOWER_CASE_LETTER | UPPER_CASE_LETTER) (('_')?  (LOWER_CASE_LETTER | UPPER_CASE_LETTER | DIGIT))*
  ;

simple_name
  :
  IDENTIFIER
  ;

name
  :
  simple_name ('.' simple_name)*
  ;

empty_declaration
  :
  NULL ';'
  ;

fragment
  STRING_ELEMENT:
  '"' '"' | ~('"' | '\n' | '\r')
  ;

STRING_LITERAL
: '"' STRING_ELEMENT* '"'
 ;
 
variable_declaration : simple_name ':=' expression ';';

string_expression : STRING_LITERAL;

term : string_expression;

expression : term ('&' term)*;

simple_declarative_item
  :
  empty_declaration
  ; // TODO complete rule
  
  external_value : EXTERNAL (STRING_LITERAL (',' STRING_LITERAL)?);

declarative_item
  :
  simple_declarative_item
  ; //TODO complete rule

simple_project_declaration
  :
  PROJECT name IS declarative_item* END name ';' EOF
  ;
