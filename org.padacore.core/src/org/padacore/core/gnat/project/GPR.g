grammar GPR;

@header {

package org.padacore.core.gnat.project;

import org.padacore.core.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
}

@lexer::header {

package org.padacore.core.gnat.project;
}

@parser::members {
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
STRING_ELEMENT returns [String element]
@init {
element = new String();
}
  :
  '"' '"' // no need to concatenate with empty string
  | character=
  ~(
    '"'
    | '\n'
    | '\r'
   )
  
   {
    element = new Integer(character).toString();
   }
  ;

STRING_LITERAL
  :
  '"' STRING_ELEMENT* '"'
  ;

variable_declaration
  :
  var_name=simple_name ':=' var_value=expression ';' 
                                                     {
                                                      this.addNewStringVariable($var_name.text, $var_value.text);
                                                     }
  ;

typed_variable_declaration
  :
  var_name=simple_name ':' type=name ':=' var_value=string_expression ';' {!this.isVariableDefined($var_name.text)}? 
                                                                                                                     {
                                                                                                                      this.addNewStringVariable($var_name.text, $var_value.text);
                                                                                                                     }
  ;
//TODO check if rule is correct in GPRbuild user manual (should be expression ?)

string_expression returns [String result]
  :
  STRING_LITERAL 
                 {
                  retval.result = $STRING_LITERAL.text.replaceAll("\"", "");
                 } //FIXME why is retval needed?
  | name
  | external_value
  ; // TODO complete rule

string_list returns [ArrayList < String > result]
@init {
result = new ArrayList<String>();
}
  :
  '(' expr1=string_expression 
                              {
                               result.add($expr1.result);
                              }
  (',' expr2=string_expression 
                               {
                                result.add($expr2.result);
                               })* ')'
  ; //TODO complete rule

term returns [ArrayList < String > result]
  :
  string_expression 
                    {
                     result = new ArrayList<String>(1);
                     result.add($string_expression.result);
                    }
  | string_list 
                {
                 result = $string_list.result;
                }
  ;

expression returns [ArrayList < String > result]
  :
  //term ('&' term)* //TODO add concatenation to expression
  term 
       {
        retval.result = $term.result;
       }
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
  {!this.isSimpleAttributeDefined($att_name.text)}? 
                                                                                                          {
                                                                                                           this.addNewSimpleAttribute($att_name.text, $att_value.result);
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

simple_project_declaration returns [GprProject result]
  :
  PROJECT begin_project_name=name IS declarative_item* END end_project_name=name ';' EOF {$begin_project_name.text.equals($end_project_name.text)}? 
                                                                                                                                                    {
                                                                                                                                                     return new GprProject($begin_project_name.text);
                                                                                                                                                    }
  ;

path_name returns [String result]
  :
  STRING_LITERAL 
                 {
                  result = $STRING_LITERAL.text.replaceAll("\"", "");
                 }
  ;

with_clause
  :
  WITH first_path=path_name 
                            {
                             this.withedProjects.add($first_path.result);
                            }
  (',' second_path=path_name 
                             {
                              this.withedProjects.add($second_path.result);
                             })* ';'
  ;

context_clause
  :
  with_clause*
  ;

project
  :
  context_clause project_declaration 
                                     {
                                      this.project = $project_declaration.result;
                                      this.processSimpleAttributes();
                                      this.processWithedProjects();
                                     }
  ;

project_declaration returns [GprProject result]
  :
  simple_project_declaration {result = $simple_project_declaration.result;}

  //TODO add project_extension
  ;
