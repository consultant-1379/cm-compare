/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.tafTests.data;

import java.util.*;

import com.ericsson.cifwk.taf.DataProvider;
import com.ericsson.cifwk.taf.data.*;
import com.ericsson.cifwk.taf.utils.csv.CsvReader;
/*
 * Data Provider contains logic required to fetch data for test purposes.
 * It is containing all necessary sources,e.g. reading files from disk, databases
 * or Data Providers delivered by other teams
 * 
 *  Data Provider is used by Test Data class for providing data entries to test methods
 *  It is also contacted by Generic Operator to calculate expected results
 */
public class CMRestToolDataProvider implements DataProvider{

	public static final String DATA_FILE = "CommandData.csv";
	public static final String DEFAULT_SEPERATOR = "#";
	public static final String HOST_NAME = "cm";
	public static final String UI_HOST_NAME = "ui";
	public static final String VALID_USER_MARK = "TRUE";
	public static final int FDN = 0;
	public static final int VALID_COLUMN = 1;
	public static final int COMMAND = 0;
	public static final int EXPECTED_RESULT = 1;
	public static final int USER_STORY_NUMBER_COLUMN = 3;
	
	/*
	 * Host is fetched using generic mechanism, so it can be changed if changes
	 */
	public static Host getCmHost(){
		return DataHandler.getHostByName(HOST_NAME);
	}
	
	/*
	 * Host is fetched using generic mechanism, so it can be changed if changes
	 */
	public static Host getUIHost(){
		return DataHandler.getHostByName(UI_HOST_NAME);
	}

	/*
	 * Test Data is fetched from a file that is an output of test analysis, but it may be a combination
	 * of test analysis data and dynamic reconciliation of data
	 */
	
	public static List<List<String>> getGoodFDNFromCSV(){
		String goodFDN;
		List<List<String>> result = new ArrayList<List<String>>();

		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		for (int i=1; i< testItems.getRowCount();i++){
			goodFDN = testItems.getCell(FDN, i);
			System.out.println("Good FDN = " + goodFDN);
			result.add(Arrays.asList(goodFDN));

		}
		return result;
	}
	
	public static List<List<String>> getDataFromCSV(String dataFilename, String userStoryNumber){
		String command;
		String expectedResult;
		List<List<String>> result = new ArrayList<List<String>>();
		CsvReader testItems = DataHandler.readCsv(dataFilename, DEFAULT_SEPERATOR);		
		for (int i= getStartRowNumber(userStoryNumber); i<= getEndRowNumber(userStoryNumber);i++){
			command = testItems.getCell(COMMAND, i);
			expectedResult = testItems.getCell(EXPECTED_RESULT, i);
			result.add(Arrays.asList(command, expectedResult));
		}
		return result;
	}
	
	
	public static int getStartRowNumber(String userStoryNumber){
		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		int i = 1;
        while (i < testItems.getRowCount()) {
        	int startRowNumber = 1;
    	if (userStoryNumber.equals(testItems.getCell(USER_STORY_NUMBER_COLUMN, i))){	
    			startRowNumber = i;
    			return startRowNumber;
    		}else{
    			i++;	
    		}	 
        }
        return 0;	
	}
	
	public static int getEndRowNumber(String userStoryNumber){
		CsvReader testItems = DataHandler.readCsv(DATA_FILE, DEFAULT_SEPERATOR);
		int i = 1;
		int rowsForUserStoryCount = 0;
		
        while (i < testItems.getRowCount()) {
    	if (userStoryNumber.equals(testItems.getCell(USER_STORY_NUMBER_COLUMN, i))) {	
    		rowsForUserStoryCount++;
    		i++;
    	}else{
    		i++;	
    		}	 
        }
        int endRowNumber;
        endRowNumber = (rowsForUserStoryCount + getStartRowNumber(userStoryNumber)) - 1;
       
        return endRowNumber;	
	}
	
	public boolean areValidCredentials(String fdn) {
		CsvReader testItems = DataHandler.readCsv(DATA_FILE);
		for (int i=1; i< testItems.getRowCount();i++){
			if(fdn.equals((testItems).getCell(FDN,i))){
				String isValidString =  (testItems.getCell(VALID_COLUMN,i)).trim();
				if(isValidString.equalsIgnoreCase(VALID_USER_MARK))
					return true;
			}
		}
		return false;
	}
}
