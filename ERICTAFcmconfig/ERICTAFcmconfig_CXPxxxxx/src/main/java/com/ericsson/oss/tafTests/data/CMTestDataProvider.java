package com.ericsson.oss.tafTests.data;


import java.util.*;
import org.testng.annotations.DataProvider;

import com.ericsson.oss.tafTests.data.CMDataProvider;
import com.ericsson.cifwk.taf.TestData;


public class CMTestDataProvider implements TestData{
	
	public static List<List<String>> expectedResultList = CMDataProvider.getExpectedResultFromCSV();
	public static List<List<String>> expectedResultForBadCommandList = CMDataProvider.getExpectedResultBadCommandFromCSV();
	public static List<List<String>> expectedResultForSpecificERBSGetCommandList = CMDataProvider.getSpecificERBSCommandExpectedResult();


	@DataProvider(name = "getExpectedResultForBadGetCommandFromCSV")
	public static Object[][] BadGetCommand() {
		Object[][] result = new Object[expectedResultForBadCommandList.size()][];
		int idx = 0;
		for (List<String> item : expectedResultForBadCommandList){
			Object[] testMethodArguments = new Object[2];
			testMethodArguments[0] = item.get(0);
			testMethodArguments[1] = item.get(1);
			result[idx] = testMethodArguments;
			idx++;
		}
		return result;
	}
	
	@DataProvider(name = "getExpectedResultForGoodGetCommandFromCSV")
	public static Object[][] getExpectedResultFromCSV() {
		System.out.println("expectedResultList.size() " + expectedResultList.size());
		Object[][] result = new Object[expectedResultList.size()][];
		int idx = 0;
		for (List<String> item : expectedResultList){
			Object[] testMethodArguments = new Object[2];
			testMethodArguments[0] = item.get(CMDataProvider.GOOD_COMMAND_COLUMN);
			testMethodArguments[1] = item.get(CMDataProvider.EXPECTED_RESULT_COLUMN);
			result[idx] = testMethodArguments;
			idx++;
		}
		return result;
	}
	
	@DataProvider(name = "getExpectedResultForSpecificERBSCommandFromCSV")
	public static Object[][] getSpecificERBSExpectedResultFromCSV() {
		System.out.println("expectedResultForSpecificERBSGetCommandList.size() " + expectedResultForSpecificERBSGetCommandList.size());
		Object[][] result = new Object[expectedResultForSpecificERBSGetCommandList.size()][];
		int idx = 0;
		for (List<String> item : expectedResultForSpecificERBSGetCommandList){
			Object[] testMethodArguments = new Object[2];
			testMethodArguments[0] = item.get(0);
			testMethodArguments[1] = item.get(1);
			result[idx] = testMethodArguments;
			idx++;
		}
		return result;
	}
	
	
	
}
