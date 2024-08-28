package com.ericsson.oss.tafTests.data;


import java.util.*;
import com.ericsson.cifwk.taf.DataProvider;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.utils.csv.CsvReader;

public class CMDataProvider implements DataProvider{
    
	public static final String DATA_FILE = "prototype.csv";
	public static final String HOST_NAME = "jira";
	public static final String VALID_USER_MARK = "TRUE";
	public static final int GOOD_COMMAND_COLUMN = 0;
	public static final int EXPECTED_RESULT_COLUMN = 1;
	public static final int BAD_COMMAND_COLUMN = 2;
	public static final int EXPECTED_RESULT_FOR_BAD_COMMAND_COLUMN = 3;
	public static final int SPECIFIC_ERBS_COMMAND_COLUMN = 4;
	public static final int SPECIFIC_ERBS_EXPECTED_RESULT_COLUMN = 5;
	
	
	public static final String DEFAULT_SEPERATOR = "#";
		
	public static List<List<String>> getExpectedResultFromCSV(){
		String goodCommand;
		String expectedResponse;
		List<List<String>> result = new ArrayList<List<String>>();

		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		for (int i=1; i< testItems.getRowCount();i++){
			goodCommand = testItems.getCell(GOOD_COMMAND_COLUMN, i);
			expectedResponse = testItems.getCell(EXPECTED_RESULT_COLUMN, i);
			result.add(Arrays.asList(goodCommand, expectedResponse));
		}
		return result;
	}
	
	public static List<List<String>> getGoodCommandFromCSV(){
		String goodCommand;
		List<List<String>> result = new ArrayList<List<String>>();

		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		for (int i=1; i< testItems.getRowCount();i++){
			goodCommand = testItems.getCell(GOOD_COMMAND_COLUMN, i);
			System.out.println("Good command = " + goodCommand);
			result.add(Arrays.asList(goodCommand));

		}
		return result;
	}
	
	public static List<List<String>> getBadCommandFromCSV(){
		String badCommand;
		List<List<String>> result = new ArrayList<List<String>>();

		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		for (int i=1; i< testItems.getRowCount();i++){
			badCommand = testItems.getCell(BAD_COMMAND_COLUMN, i);
			System.out.println("Good command = " + badCommand);
			result.add(Arrays.asList(badCommand));

		}
		return result;
	}
	
	public static List<List<String>> getExpectedResultBadCommandFromCSV(){
		String badCommand;
		String expectedResultForBadCommand;
		List<List<String>> result = new ArrayList<List<String>>();

		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		for (int i=1; i< testItems.getRowCount();i++){
			badCommand = testItems.getCell(BAD_COMMAND_COLUMN, i);
			expectedResultForBadCommand = testItems.getCell(EXPECTED_RESULT_FOR_BAD_COMMAND_COLUMN, i);
			result.add(Arrays.asList(badCommand,expectedResultForBadCommand));
		}
		return result;
	}
	

	public static List<List<String>> getSpecificERBSCommandExpectedResult(){
		String specificERBSCommand;
		String expectedResultForSpecificERBSCommand;
		List<List<String>> result = new ArrayList<List<String>>();

		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		for (int i=1; i< testItems.getRowCount();i++){
			specificERBSCommand = testItems.getCell(SPECIFIC_ERBS_COMMAND_COLUMN, i);
			expectedResultForSpecificERBSCommand = testItems.getCell(SPECIFIC_ERBS_EXPECTED_RESULT_COLUMN, i);
			result.add(Arrays.asList(specificERBSCommand,expectedResultForSpecificERBSCommand));
		}
		return result;
	}   
}
