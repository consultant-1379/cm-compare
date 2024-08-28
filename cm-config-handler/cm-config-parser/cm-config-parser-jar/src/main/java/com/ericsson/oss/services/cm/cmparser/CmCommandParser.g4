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

parser grammar CmCommandParser;

options { tokenVocab=CmCommandLexer; }

// Config Commands rules

command :
    (createConfigCommand | listConfigCommand | setScopeConfigCommand | updateConfigCommand | deleteConfigCommand | lockConfigCommand | unlockConfigCommand | diffConfigCommand | linkConfigCommand | unlinkConfigCommand) outputFormat? EOF
;

createConfigCommand :
    CREATE_CONFIG_CMD configName (createConfigScope)?
;

listConfigCommand :
    LIST_CONFIG_CMD (listConfigOptions)?
;

setScopeConfigCommand :
    SET_SCOPE_CONFIG_CMD configName setConfigScope
; 

updateConfigCommand :
    UPDATE_CONFIG_CMD configName CONFIG_FROM fromConfigName updateConfigScope updateConfigOptions 
; 

deleteConfigCommand :
    DELETE_CONFIG_CMD configName 
; 

lockConfigCommand :
    LOCK_CONFIG_CMD configName (reasonMessageString)? 
;

unlockConfigCommand :
    UNLOCK_CONFIG_CMD configName 
; 

diffConfigCommand :
    DIFF_CONFIG_CMD configName CONFIG_FROM fromConfigName diffConfigScope diffConfigOptions 
;

linkConfigCommand :
    LINK_CONFIG_CMD configName CONFIG_TO baseConfigName 
;  

unlinkConfigCommand :
    UNLINK_CONFIG_CMD configName 
;  


// Output format rules

outputFormat:
    listOutput | tableOutput
;

listOutput :
    LIST_OUTPUT
;

tableOutput :
	TABLE_OUTPUT
;

// Configuration name rule

configName :
    id
;

// Scope rule for create config command

createConfigScope :
	CONFIG_SCOPE configScope
;

configScope :
	scope (SEMI scope)* (cmObjectSpecification namespace? version?)?
;

// List config option rules

listConfigOptions :
	listConfigAllOption | listConfigOwnerOption
;
 
listConfigAllOption :
	LIST_CONFIG_ALL_OPTION
;	

listConfigOwnerOption :
	LIST_CONFIG_OWNER_OPTION configOwner
;	

configOwner :
	id
;

// Scope rule for set scope config command

setConfigScope :
	configScope
;

// Source configuration name rule 

fromConfigName :
	id
;

// Base config name (Link command)

baseConfigName :
	id
;

// Scope rule for update config command

updateConfigScope :
	configScope
;

// Update config option rules 

updateConfigOptions :
	(updateConfigCreateOption)? (updateConfigDeleteOption)? (updateConfigOverwriteOption)?
;

updateConfigCreateOption :
	UPDATE_CONFIG_CREATE_OPTION
;

updateConfigDeleteOption :
	UPDATE_CONFIG_DELETE_OPTION
;

updateConfigOverwriteOption :
	UPDATE_CONFIG_OVERWRITE_OPTION
;

// Lock message rule

reasonMessageString :
	attributeStringValue
;

// Scope rule for Diff config command
 
diffConfigScope :
	(configScope)?
;

// Diff config option rules

diffConfigOptions :
	(diffConfigDeepOption)? (diffConfigUpdateOption)? (diffConfigImportOption)?
;

diffConfigDeepOption :
	DIFF_CONFIG_DEEP_OPTION
;

diffConfigImportOption :
	DIFF_CONFIG_IMPORT_OPTION
;

diffConfigUpdateOption :
	DIFF_CONFIG_UPDATE_OPTION COLON diffConfigName
;
 
diffConfigName :
	id
; 

// Common scope rule

scope :
	meContextName | fdn | anyNode
; 

fdn :
    id EQ id (COMMA id EQ id)*
;

namespace :
    NAMESPACE EQ id
;

version :
    VERSION EQ versionValue
;

versionValue :
    id (DOT id)*
;

meContextName :
   meContextNameEquals | meContextNameStartsWith | meContextNameEndsWith | meContextNameContains
;

meContextNameEquals :
	id
;

meContextNameStartsWith :
    id STAR_WS
;    

meContextNameEndsWith :
    STAR id
;    

meContextNameContains :
    STAR id STAR_WS
;    

anyNode :
	STAR_WS | (STAR EOF) // to cover 'get *'
;

// Common Object Specification (or Type Specification) rule

cmObjectSpecification :
	type attributeSpecification? (COMMA (allTypes | cmObjectSpecification))?
;

type :
	id
;

allTypes :
	STAR
;

attributeSpecification :
	DOT (allAttributes | attributeFilter | ( LPAREN (allAttributes | attributeFilters) RPAREN ))
;

attributeSpecificationForComplex :
    attributeName EQ (sequenceAttributeValue | complexAttributeValue | simpleAttributeValue)
;

attributeSpecifications :
    attributeSpecificationForComplex (COMMA attributeSpecificationForComplex)*
;

attributeFilters :
	 attributeFilter (COMMA attributeFilter)*
;

attributeFilter :
    attributeName							#	attributeSelector
    |
    attributeName equalsTo comparedValue	# 	attributeComparison
    |
    attributeName numericComparator attributeIdValue	# 	attributeComparison
;

lessThanValue :
        LT
;

greaterThanValue :
        GT
;

lessThanOrEqualToValue :
        LTEQ
;

greaterThanOrEqualToValue :
        GTEQ
;

numericComparator :
        lessThanValue | greaterThanValue | lessThanOrEqualToValue | greaterThanOrEqualToValue
;

equalsTo :
    EQ EQ
;

comparedValue :
	startsWithValue | endsWithValue | containsValue | simpleAttributeValue | anyValue
;

startsWithValue:
	(simpleAttributeValue) (STAR | STAR_WS)
;

endsWithValue:
	STAR (simpleAttributeValue)
;

containsValue:
	STAR (simpleAttributeValue) (STAR | STAR_WS)
;

anyValue:
	(STAR | STAR_WS)
;

allAttributes :
	(STAR | STAR_WS)
;

simpleAttributeValue :
   attributeIdValue
   |
   attributeStringValue
;

attributeIdValue :
   id
;

attributeSetters :
   attributeSetter (COMMA attributeSetter)*
;

attributeSetter :
   attributeName EQ (sequenceAttributeValue | complexAttributeValue | simpleAttributeValue)
;

sequenceAttributeValue :
    LSQBRACKET attributeValueInSequence (COMMA attributeValueInSequence)* RSQBRACKET
    |
    LSQBRACKET complexAttributeValueInSequence (COMMA complexAttributeValueInSequence)* RSQBRACKET
;

attributeValueInSequence :
    simpleAttributeValue
;

complexAttributeValueInSequence :
    complexAttributeValue
;

complexAttributeValue :
    LPAREN attributeSpecifications RPAREN
;

attributeName :
    id
;

attributeStringValue :
	LQUOTE string RQUOTE
;

string :
    STRING
    | // match a blank string
;
   
allFlag :
	ALL
;

name :
    id
;


// this small 'id' is a rule to match either an ID token OR a keyword token
id :
    CREATE_CONFIG_CMD | LIST_CONFIG_CMD | SET_SCOPE_CONFIG_CMD | UPDATE_CONFIG_CMD | DELETE_CONFIG_CMD | LOCK_CONFIG_CMD | UNLOCK_CONFIG_CMD | DIFF_CONFIG_CMD | LINK_CONFIG_CMD | UNLINK_CONFIG_CMD | NAMESPACE | VERSION | ID
;

