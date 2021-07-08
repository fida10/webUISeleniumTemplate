package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.coreFrameworkFiles.Initializer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class ExcelReader {
	private final String dataSheetPath;
	private String platformToUse;
	private String environmentToUse;
	private String countryTwoLetter;

	protected Sheet mgoCountrySheet;
	protected int noOfRows;
	protected int noOfColumns;
	protected LinkedList<String> columnNames;

	private final ExtentReportGenerator extentReportGeneratorForExcelReader;
	private final ExtentTest currentTestForExcelReader;
	//using field approach
	/*
	This assumes that the environmentToUse will not be changed in a single test run. May need to be modified once jenkins params are integrated
	*/
	public ExcelReader(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		currentTestForExcelReader = currentTest;
		extentReportGeneratorForExcelReader = extentReportGenerator;

		dataSheetPath = new PathGeneration().returnExcelDataSheetDir();
		platformToUse = new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("platform");
		environmentToUse = new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("env");
		countryTwoLetter = new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("country");
		/*
		All these methods are from outside of the class and must be initialized before anything else. Not doing so = NullPointerException.
		They are therefore put in the constructor.
		*/
		mgoCountrySheet = loadExcelCountrySheet(loadExcelSpreadsheet());
		noOfRows = sheetNoOfRows(mgoCountrySheet);
		columnNames = returnColumnNames();
		noOfColumns = sheetNoOfColumns();
		/*Above 4 load up the following:
	The excel spreadsheet document
	The particular country spreadsheet specified in config.properties
	The number of rows of test data in the sheet, not including the first one (explained in next comment)
	The names of the columns (which is the first row of the spreadsheet) in an ArrayList
	The number of columns of test data, pulled directly from ArrayList

	All these methods are from inside the class. initExcelReader should be called at the start of the program, (Listeners on Start)
	*/
	}
	private XSSFWorkbook loadExcelSpreadsheet(){
		String pathToExcelDataSheets = dataSheetPath.concat(environmentToUse).concat(".xlsx");
		//Goes into the datasheet directory and getting a specific data sheet based on desired environment.
		extentReportGeneratorForExcelReader.addInfoMessage(currentTestForExcelReader,  "Using the following spreadsheet: " + pathToExcelDataSheets); //extentReports

		File excelSheet = new File(pathToExcelDataSheets);
		XSSFWorkbook workbook = null; //to initialize, defined in below try/catch block
		try {
			FileInputStream inputStream = new FileInputStream(excelSheet);
			workbook = new XSSFWorkbook(inputStream);
		} catch(IOException e){
			System.out.println("IO Exception thrown, invalid path to spreadsheet provided: " + pathToExcelDataSheets + ". Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
		return workbook;
	}
	private Sheet loadExcelCountrySheet(XSSFWorkbook workbook){
		return workbook.getSheet(countryTwoLetter);
	}
	private int sheetNoOfRows(Sheet mgoCountrySheet){
		return mgoCountrySheet.getLastRowNum() - mgoCountrySheet.getFirstRowNum();
		/*
		returns total number of rows on the sheet
		excludes the first row, which is there just to label and identify the columns
		*/
	}
	public String[] sortArrayAlphabetically(String[] arrayToSort){
		String temp;
		for (int i = 0; i < arrayToSort.length; i++) //sorts array alphabetically
		{
			for (int j = i + 1; j < arrayToSort.length; j++)
			{
				if (arrayToSort[i].compareTo(arrayToSort[j]) > 0)
				{
					temp = arrayToSort[i];
					arrayToSort[i] = arrayToSort[j];
					arrayToSort[j] = temp;
				}
			}
		}
		return arrayToSort;
	}
	public String[] removeDuplicatesFromArray(String[] arrayToProcess){
		sortArrayAlphabetically(arrayToProcess); //must be sorted once before processing
		return sortArrayAlphabetically(new HashSet<>(Arrays.asList(arrayToProcess)).toArray(new String[0])); //result sorted alphabetically once more
	}
	private LinkedList<String> returnColumnNames(){
		/*
		This method returns the first row of the specified mgoCountrySheet.
		Essentially, this represents what each of the columns will contain, i.e. name, username, etc.
		The arraylist is to store the cell values of the first row. */
		LinkedList<String> first_row = new LinkedList<>();
		Row row = mgoCountrySheet.getRow(0);
		/*
		This step gets the number of columns.
		*/
		for (int j = 0; j <row.getLastCellNum(); j++) {
			String cell = String.valueOf(mgoCountrySheet.getRow(0).getCell(j));
			first_row.add(cell); //adds each cell to the arraylist, thru iterations of the loop
			extentReportGeneratorForExcelReader.addInfoMessage(currentTestForExcelReader, "Column " + (j + 1) + " is for the following field: " + cell); //extentReports
		}
		return first_row;
	}
	public int sheetNoOfColumns(){
		return columnNames.size();
		/*
		columnNames is an Arraylist of each individual cell in the first row.
		Thus, by counting how many of these cells there are, we can ascertain number of columns.
		*/
	}
	public String[] returnDesiredColumn(String columnHeader){
		String[] currentColumn = new String[noOfRows]; /*
		Initializes a new string array, with the size as no of rows, to store column contents
		We then go down each row in the matching column, and append to this array
		*/
		/*
		* So we should go to row 0, and find the cell number that has our column header
		* Then, using that cell number, we go down the row and get the cell that matches the above cell number
		* And we keep going down the row until noOfRows is done
		*
		* Two for loops
		* Outer for loop finds the cell number on the 1st (0 index) row
		* Inner loop then uses this cell number to get that same cell number, for each row. Once it gets the right cell for the row, it appends the contents to the array, then goes to the next row
		* Outer loop then breaks*/
		DataFormatter dataFormatter = new DataFormatter();
		for (int i = 0; i < noOfColumns; i++) {
			//cycle thru the first row on the spreadsheet, looking for a match for columnHeader
			Cell currentCell = mgoCountrySheet.getRow(0).getCell(i);
			String firstRowCurrentCellContents = currentCell.getStringCellValue(); //cycles thru each cell in the first row
			if(firstRowCurrentCellContents.equals(columnHeader)) { //if the cell contents match the columnHeader param, go into this for loop
				for (int j = 1; j <= noOfRows; j++) { //as we do not want the first row, which just shows the column names, we start from 1, not 0
					try {
						Cell cellInDesiredColumn = mgoCountrySheet.getRow(j).getCell(i); //cycles thru each cell in this column
						String cellValueInString= dataFormatter.formatCellValue(cellInDesiredColumn); //formats cells so that any cell value will be returned as a string, avoid illegalStateException
							currentColumn[j - 1] = cellValueInString; //adds the value of the current cell to the array
					} catch (NullPointerException e){

						extentReportGeneratorForExcelReader.addWarningMessage(currentTestForExcelReader, "Cell has null content, assigning it a value of '--blank--'. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
						currentColumn[j - 1] = "--blank--"; //if the value in the cell is null, add "--blank--" just to avoid nullPointerExceptions.
						/* j - 1 is added to both arrays because we are starting from the 2nd row of the desired column (index 1).
						 However, we still want the index to start from 0 and label the elements likewise, so we append the array elements to index j-1*/
					}
				}
			}
		}
		return currentColumn;
	}
	public int returnNoOfCellsMatchingTextInColumn(String columnHeader, String textToSearch){
		String[] column = returnDesiredColumn(columnHeader);
		int noOfMatches = 0;
		for(String columnElements : column){
			if(columnElements.equalsIgnoreCase(textToSearch)){
				noOfMatches++;
			}
		}
		return noOfMatches;
	}
	public String[] returnDesiredColumnScenarioFilter(String columnHeader, String scenario){
		String[] currentColumn = new String[returnNoOfCellsMatchingTextInColumn("Scenario", scenario)];
		int currentColumnIterator = 0; //used to add values to currentColumn via index
		DataFormatter dataFormatter = new DataFormatter();
		for (int i = 0; i < noOfColumns; i++) {
			//cycle thru the first row on the spreadsheet, looking for a match for columnHeader
			Cell currentCell = mgoCountrySheet.getRow(0).getCell(i);
			String firstRowCurrentCellContents = currentCell.getStringCellValue(); //cycles thru each cell in the first row
			if(firstRowCurrentCellContents.equals(columnHeader)) { //if the cell contents match the columnHeader param, go into this for loop
				for (int j = 1; j <= noOfRows; j++) { //as we do not want the first row, which just shows the column names, we start from 1, not 0
					Cell cellInDesiredColumn = mgoCountrySheet.getRow(j).getCell(i); //cycles thru each cell in this column
					String currentScenario = dataFormatter.formatCellValue(mgoCountrySheet.getRow(j).getCell(0)); //gets the currentScenario that this cell is matched with, as a string.
					if (currentScenario.equalsIgnoreCase(scenario)) { //add to array only if current scenario cell matches the intended scenario, specified in params
						String cellValueInString = dataFormatter.formatCellValue(cellInDesiredColumn); //formats cells so that any cell value will be returned as a string, avoid illegalStateException
						if (cellValueInString.equalsIgnoreCase("")) {
							cellValueInString = "--blank--"; //if the value in the cell is null, add "--blank--" just to avoid nullPointerExceptions and make clear that cell is empty.
						}
						currentColumn[currentColumnIterator] = cellValueInString; //adds the value of the current cell to the array
						currentColumnIterator++;
					}

					/* j - 1 is added to both arrays because we are starting from the 2nd row of the desired column (index 1).
						 However, we still want the index to start from 0 and label the elements likewise, so we append the array elements to index j-1*/
				}
			}
		}
		extentReportGeneratorForExcelReader.addInfoMessage(currentTestForExcelReader,  "Array for column generated, specific scenario: " + scenario + " -- Array: " + columnHeader + ": " + Arrays.toString(currentColumn)); //extentReport
		return currentColumn;
		/*

		* */
	}
	public ArrayList<String> returnDesiredTestCaseRow(String testCaseId){
		extentReportGeneratorForExcelReader.addInfoMessage(currentTestForExcelReader,  "Total number of rows in sheet: " + noOfRows); //extentReports
		//This arraylist is to store the cell values of the respective testcase case row
		ArrayList<String> current_row = new ArrayList<String>();
		for (int k = 1; k <= noOfRows; k++) { //cycles thru rows
			String cell_value = mgoCountrySheet.getRow(k).getCell(0).getStringCellValue();
			extentReportGeneratorForExcelReader.addInfoMessage(currentTestForExcelReader, "Test Case Name = " + cell_value); //extentReports
			/* This returns the first element of the second row, or first row if the labels are not included.
			 Because of the format of the excel sheet, this will always return the testCaseId field in excel sheet, or name of the test case, which is reflected in the print statement.
			 Based on this, the below if statement will check to see if the value in the cell (which is the test case ID), matches the intended testCaseId, specified in method params. If they do, the nested for loop (j) will cycle thru the individual cells of that row to retrieve the correct test data.
			 */
			if (testCaseId.equalsIgnoreCase(cell_value)) {
				for (int j = 0; j < noOfColumns; j++) { //cycles thru columns
					Cell cell = mgoCountrySheet.getRow(k).getCell(j);
					String Cell_Value;
					if(cell != null) {
						if (cell.getCellType() == CellType.NUMERIC) {
							BigDecimal Numeric_value = BigDecimal.valueOf(cell.getNumericCellValue());
							Cell_Value = Numeric_value.toPlainString();
						} else {
							Cell_Value = cell.toString();
						}
						current_row.add(Cell_Value);
					}
				}
				break; //'k' loop broken once desired row is reached and processed into current_row
			}
		}
		return current_row;
	}
	public HashMap <String, String> returnTestCaseDataHashMap(String testCaseId) {
		//This hashmap is to map and store the first row with the respective row
		ArrayList<String> desiredRow = returnDesiredTestCaseRow(testCaseId);
		HashMap<String, String> map = new HashMap<String, String>();
		int i = 0;
		while (i < desiredRow.size()) {
			map.put(columnNames.get(i), desiredRow.get(i));
			i++;
		}
		return map;
	}
	public LinkedHashMap<String, String[]> returnAllDataOnExcelSheet(){
		/*
		* Returns a hashmap of the entire excel sheet
		* Key is the name of the column
		* Value is an array of all the values within that column
		* */
		LinkedHashMap <String, String[]> allDataOnExcelSheetInColumns = new LinkedHashMap<>();
		for (int i = 0; i < columnNames.size(); i++) {
			String[] elementsInColumn = returnDesiredColumn(columnNames.get(i)); //each elements in column represents a single column
			allDataOnExcelSheetInColumns.put(columnNames.get(i), elementsInColumn);
		}
		return allDataOnExcelSheetInColumns;
	}
	public LinkedHashMap<String, LinkedHashMap<String, String[]>> returnAllDataOnExcelOrgInScenarios(){
		LinkedHashMap<String, LinkedHashMap<String, String[]>> allDataOnExcelOrgInScenarios = new LinkedHashMap<>();
		/*
		* Outer hashmap stores all the names of each scenario from the excel sheet, and this will be matched with the scenario outline with a contains. Key is scenario from scenario column itself in excel
		* Inner hashmap stores the column contents specifically for that scenario, rather than all of the contents. Key is the column name.
		* Inner String[] stores the column contents, specifically related to that scenario*/
		String[] scenarios = removeDuplicatesFromArray(returnDesiredColumn("Scenario"));
		for(String indivScenario : scenarios){
			LinkedHashMap<String, String[]> dataForIndivScenarios = new LinkedHashMap<>();
			for(int i = 1; i < columnNames.size(); i++){ //starting from one as 0th index first column is Scenario
				String currentColumn = columnNames.get(i);
				String[] indivArgs = returnDesiredColumnScenarioFilter(currentColumn, indivScenario); //gets all the contents of a specified column that also matches the specified scenario
				dataForIndivScenarios.put(currentColumn, indivArgs);
			}
			allDataOnExcelOrgInScenarios.put(indivScenario, dataForIndivScenarios);
		}
		return allDataOnExcelOrgInScenarios;
	}
	//getter methods
	public Sheet getMgoCountrySheet() {
		return mgoCountrySheet;
	}
	public int getNoOfRows() {
		return noOfRows;
	}
	public int getNoOfColumns() {
		return noOfColumns;
	}
	public LinkedList<String> getColumnNames() {
		return columnNames;
	}
}
