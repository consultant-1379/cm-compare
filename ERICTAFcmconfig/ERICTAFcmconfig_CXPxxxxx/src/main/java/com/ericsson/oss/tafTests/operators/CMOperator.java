package com.ericsson.oss.tafTests.operators;

import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.oss.tafTests.operators.rest.CMRestOperator;

public class CMOperator implements GenericOperator{
	static CMRestOperator CMRestOp = new CMRestOperator();	
	
	public static String sendCommandtoScriptEngine(String command){
		return CMRestOp.getScriptEnginetoExcecuteCommand(command);
	}    
}
