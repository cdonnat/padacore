grammar GPR;

@header {
package org.padacore.gnat.project;

import org.padacore.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
}

@lexer::header {
package org.padacore.gnat.project;
}

@parser::members {
private GprProject project;
private Map<String, ArrayList<String>> vars = new HashMap<String, ArrayList<String>>();

GprProject getGprProject() {
  return this.project;
}


boolean isVariableDefined(String variableName) {
return false;
}
}

// Fragment rules

fragment
UPPER_CASE_LETTER
  :
  'A'..'Z'
  ;

fragment
LOWER_CASE_LETTER
  :
  'a'..'z'
  ;

fragment
DIGIT
  :
  '0'..'9'
  ;

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
  (
    LOWER_CASE_LETTER
    | UPPER_CASE_LETTER
  )
  (
    ('_')?
    (
      LOWER_CASE_LETTER
      | UPPER_CASE_LETTER
      | DIGIT
    )
  )*
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
STRING_ELEMENT
  :
  '"' '"'
  |
  ~(
    '"'
    | '\n'
    | '\r'
   )
  ;

STRING_LITERAL
  :
  '"' STRING_ELEMENT* '"'
  ;

variable_declaration
  :
  var_name=simple_name ':=' var_value=expression ';'
  {ArrayList<String> var = new ArrayList(1);
   var.add($var_value.text);
   this.vars.put($var_name.text, var);}
  ;

typed_variable_declaration
  :
 simple_name ':' name ':=' string_expression ';'
  ;
//TODO check if rule is correct in GPRbuild user manual (should be expression ?)

string_expression
  :
  STRING_LITERAL
  | name
  | external_value
  ; // TODO complete rule

string_list
  :
  '(' string_expression (',' string_expression)* ')'
  ; //TODO complete rule

term
  :
  string_expression
  | string_list
  ;

expression
  :
  term ('&' term)*
  ;
simple_declarative_item
  :
  variable_declaration 
  | typed_variable_declaration
  | attribute_declaration
  | empty_declaration
  ; // TODO complete rule with case construction

attribute_declaration
  :
  indexed_attribute_declaration
  | simple_attribute_declaration
  ;

indexed_attribute_declaration
  :
  FOR simple_name '(' STRING_LITERAL ')' USE expression ';'
  ;

simple_attribute_declaration
  :
  FOR att_name=simple_name USE att_value=expression ';' // rule slightly adapted
  {
                                                         System.out.println("found attribute " + $att_name.text + " with value "
                                                            + $att_value.text);
                                                        }
  ;

attribute_designator
  :
  simple_name
  | simple_name '(' STRING_LITERAL ')'
  ;

external_value
  :
  EXTERNAL '(' STRING_LITERAL (',' STRING_LITERAL)* ')'
  |
  ;

//TODO add external_as_list

declarative_item
  :
  simple_declarative_item
  ; //TODO complete rule

simple_project_declaration returns [GprProject simpleProject]
  :
  PROJECT begin_project_name=name IS declarative_item* END end_project_name=name ';' EOF
  {$begin_project_name.text.equals($end_project_name.text)}?  
                                                                  {
                                                                   simpleProject = new GprProject($begin_project_name.text);
                                                                  }
  ;

project_declaration
  :

  simple_project_declaration 
                             {
                              this.project = $simple_project_declaration.simpleProject;
                             }
  ;