/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
lexer grammar CmCommandLexer;

@lexer::members {
    boolean ignoreWhitespace = true;
}

WS  : [ \t\r\n]+ { if(ignoreWhitespace) skip(); } ;

COMMA : ',' ;

SEMI : ';' ;

COLON : ':' ;

EQ : '=' ;

LT : '<' ;

GT : '>' ;

LTEQ : '<=' ;

GTEQ : '>=' ;

LPAREN : '(';

RPAREN : ')';

LSQBRACKET : '[';

RSQBRACKET : ']';

DOT : '.' ;

NAMESPACE :  '-ns' | '-namespace' | '--namespace' ;

VERSION :  '-v' | '-version' | '--version' ;

NETYPE : '-ne' | '-neType' |  '--neType' ;

TABLE_OUTPUT : '-t' | '--table' ;

LIST_OUTPUT : '-l' | '--list' ;

ENFORCE_A_WHITESPACE : { ignoreWhitespace = false; } WS { ignoreWhitespace = true; } ;

CREATE_CONFIG_CMD : 'create' ENFORCE_A_WHITESPACE;

LIST_CONFIG_CMD : 'list';

SET_SCOPE_CONFIG_CMD : 'set scope' ENFORCE_A_WHITESPACE;

UPDATE_CONFIG_CMD : 'update' ENFORCE_A_WHITESPACE;

DELETE_CONFIG_CMD : 'delete' ENFORCE_A_WHITESPACE;

LOCK_CONFIG_CMD : 'lock' ENFORCE_A_WHITESPACE;

UNLOCK_CONFIG_CMD : 'unlock' ENFORCE_A_WHITESPACE;

DIFF_CONFIG_CMD : 'diff' ENFORCE_A_WHITESPACE;

LINK_CONFIG_CMD : 'link' ENFORCE_A_WHITESPACE;

UNLINK_CONFIG_CMD : 'unlink' ENFORCE_A_WHITESPACE;

CONFIG_SCOPE : 'scope'; 

CONFIG_FROM : 'from';

LIST_CONFIG_ALL_OPTION : '-all';

LIST_CONFIG_OWNER_OPTION : '-owner';

UPDATE_CONFIG_CREATE_OPTION : '-create';

UPDATE_CONFIG_DELETE_OPTION : '-delete';

UPDATE_CONFIG_OVERWRITE_OPTION : '-overwrite';

DIFF_CONFIG_DEEP_OPTION : '-deep';

DIFF_CONFIG_IMPORT_OPTION : '-import';

DIFF_CONFIG_UPDATE_OPTION : '-update';

CONFIG_TO : 'to';

ALL : '-ALL' ;

STAR : '*' ;

STAR_WS : STAR ENFORCE_A_WHITESPACE ;

ID  : [a-zA-Z0-9-_]+;

// string mode
LQUOTE : '"' -> pushMode(String);
    mode String;
    STRING : ~["]+;
RQUOTE : '"' -> popMode;


