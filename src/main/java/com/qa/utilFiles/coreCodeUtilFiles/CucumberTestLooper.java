package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.coreFrameworkFiles.Initializer;
import org.apache.commons.io.FileUtils;

import java.io.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CucumberTestLooper {

	private HashMap<File, StringBuffer> originalFeatureFiles;
	private ExtentReportGenerator extentReportGeneratorForCucumberTestLooper;
	private ExtentTest currentTestForCucumberTestLooper;

	private ExcelReader excelReader;
	//using field approach for reporting
	public void linkCucumberFilesToExcel(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		Initializer.propertiesInit(currentTest, extentReportGenerator); //excelreader will not work without properties being initialized, and this CucumberTestLooper will not work without excelReader. Initializing here.
		extentReportGeneratorForCucumberTestLooper = extentReportGenerator;
		currentTestForCucumberTestLooper = currentTest;

		excelReader = new ExcelReader(currentTestForCucumberTestLooper, extentReportGeneratorForCucumberTestLooper); //done to initialize the various fields within class at least once and avoid NullPointerException, and for excel reader usage throughout class
		originalFeatureFiles = saveOriginalFeatureFiles();
		writeNewArgumentsToFeatureFiles(featureFilesAndExcelSheetTriHashMap());
	}
	public void unlinkCucumberFilesFromExcel(){
		deleteOldArgumentsFromFeatureFiles();
	}
	public LinkedList<File> getAllFilesWithCertainExtension(String pathToSearch, String extension){
		File cucumberFilesPath = new File(pathToSearch);
		String[] extensions = {extension};
		LinkedList<File> allFeatureFiles = (LinkedList<File>) FileUtils.listFiles(cucumberFilesPath, extensions, true); //method supports multiple extensions

		extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, "Found " + allFeatureFiles.size() + " .feature files, in the following directories: "); //extentReports
		for (int i = 0; i < allFeatureFiles.size(); i++) {
			extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, allFeatureFiles.get(i).toString()); //extentReports
		}
		return allFeatureFiles;
	}
	public LinkedList<File> getListOfFilesWithMatchingText(String matchingTest, LinkedList<File> listOfFilesToSearch){
		LinkedList<File> filesWithMatchingTextFullLines = new LinkedList<>();
		for(File currentFile : listOfFilesToSearch){
			try {
				BufferedReader br = new BufferedReader(new FileReader(currentFile));
				String currentLine = null; //the string to hold what is being read currently
				while((currentLine = br.readLine()) != null){
					if (currentLine.contains(matchingTest)){
						extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, "File has text which matches: " + matchingTest + " -- File " + currentFile + " adding to LinkedList"); //extentReports
						filesWithMatchingTextFullLines.add(currentFile);
						break;
					}
				}
			} catch (IOException e){
				extentReportGeneratorForCucumberTestLooper.addWarningMessage(currentTestForCucumberTestLooper, "IO Exception thrown, feature file is invalid. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
			}
		}
		extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, "Found " + filesWithMatchingTextFullLines.size() + " files which has/have matching text, in the following directories: "); //extentReports
		for(File matchingFile : filesWithMatchingTextFullLines) {
			extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, matchingFile.toString()); //extentReports
		}
		return filesWithMatchingTextFullLines;
	}
	public LinkedList<String> getLinesOfMatchingTextFromSingleFile(String matchingText, File fileToSearch) {
		LinkedList<String> matchingTextFullLines = new LinkedList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileToSearch));
			String currentLine = null; //the string to hold what is being read currently
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.contains(matchingText) && !currentLine.contains("#")) {
					extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, "Line has text which matches: " + matchingText + " -- Adding Line " + currentLine + " to LinkedList"); //extentReports
					matchingTextFullLines.add(currentLine);
				}
			}
		} catch (IOException e) {
			extentReportGeneratorForCucumberTestLooper.addWarningMessage(currentTestForCucumberTestLooper, "IO Exception thrown, feature file is invalid. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
		extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, "Found " + matchingTextFullLines.size() + " lines which has/have matching text. They are: "); //extentReports
		for (String matchingLine : matchingTextFullLines) {
			extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, matchingLine); //extentReports
		}
		return matchingTextFullLines;
	}
	public LinkedList<String> getLineAfterMatchingTextFromSingleFile(String matchingText, File fileToSearch) {
		/*Gets the line right AFTER the matching text
	Used ideally to get the line right after "Example" in a scenario outline on a feature file.
	This line is the one that contains the needed params, which we can parse.*/
		LinkedList<String> nextLineMatchingTextFullLines = new LinkedList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileToSearch));
			String currentLine = null; //the string to hold what is being read currently
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.contains(matchingText) && !currentLine.contains("#")) {
					extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, "Line has text which matches: " + matchingText + " -- Going to next line"); //extentReports
					currentLine = br.readLine(); //reads the next line down
					extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper,  "Next line text is: " + currentLine + " -- Adding to LinkedList");//extentReports
					nextLineMatchingTextFullLines.add(currentLine);
				}
			}
		} catch (IOException e) {
			extentReportGeneratorForCucumberTestLooper.addWarningMessage(currentTestForCucumberTestLooper, "IO Exception thrown, feature file is invalid.. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
		extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, "Found " + nextLineMatchingTextFullLines.size() + " lines which has/have matching text. They are: "); //extentReports
		for (String nextLineAfterMatchingLine : nextLineMatchingTextFullLines) {
			extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper, nextLineAfterMatchingLine); //extentReports
		}
		return nextLineMatchingTextFullLines;
	}
	public String toTitleCase(String givenString) { //capitalizes each letter after string
		char[] chars = givenString.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
				found = false;
			}
		}
		return String.valueOf(chars);
	} //changes all the letters after a space to uppercase
	public BufferedReader startFromSpecifiedLine(BufferedReader bufferedReader, int lineNo){
		try {
			for (int i = 0; i < lineNo; i++) {
				bufferedReader.readLine();
			}
		} catch(IOException e){
			extentReportGeneratorForCucumberTestLooper.addWarningMessage(currentTestForCucumberTestLooper, "IO exception thrown while reading file. Stacktrace: " + Arrays.toString(e.getStackTrace()));//extentReports
		}
		return bufferedReader; //should return a buffered reader object that is now on the line specified in param lineNo
	}
	public HashMap<File, StringBuffer> saveOriginalFeatureFiles(){
		HashMap<File, StringBuffer> originalFeatureFiles = new HashMap<>(); //Key is the files to save, StringBuffer is the inside contents
		//get a list of all the feature files with "Scenario Outline:" within them
		LinkedList<File> filesThatWillBeChanged = getListOfFilesWithMatchingText("Scenario Outline:", getAllFilesWithCertainExtension(new PathGeneration().returnPathToCucumberFiles(), "feature"));
		for(File file : filesThatWillBeChanged){
			originalFeatureFiles.put(file, getFileBackAsStringBuffer(file));
		}
		return originalFeatureFiles;
	}
	public StringBuffer getFileBackAsStringBuffer(File fileToParse){
		StringBuffer inputBuffer = new StringBuffer();
		try{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToParse));
			String currentLine = bufferedReader.readLine();
			inputBuffer.append(currentLine.concat("\n"));
			while(true){
				currentLine = bufferedReader.readLine();
				if(currentLine == null){
					break; //avoid writing "null" to page at page end
				}
				inputBuffer.append(currentLine.concat("\n"));
			}
			bufferedReader.close();
		} catch(IOException e){
			extentReportGeneratorForCucumberTestLooper.addWarningMessage(currentTestForCucumberTestLooper, "IO Exception thrown, error reading file or file invalid. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
		return inputBuffer;
	}
	private void writeArgsToScenarioOutline(File fileToRead, String scenarioOutline, String argsIntoScenarioOutline){
		try {
			StringBuffer inputBuffer = new StringBuffer(); //as the file is read, each line is stored here. This object will be used to overwrite the file once generated.
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead));
			String currentLine = bufferedReader.readLine();
			inputBuffer.append(currentLine.concat("\n"));
			while(true){
				if(currentLine.contains(scenarioOutline) && !currentLine.contains("#")){
					//verified that bufferedReader has entered the scenario outline with above if!
					//now, keep reading until example
					while(true){
						currentLine = bufferedReader.readLine();
						inputBuffer.append(currentLine.concat("\n"));
						if(currentLine.contains("Examples:")){
							currentLine = bufferedReader.readLine();
							inputBuffer.append(currentLine);
							//at this point reader is directly at the end of the param line in the scenario Outline. At this point, write the generated string containing the args to the file.
							if(!argsIntoScenarioOutline.contains("~~unsupported~~")) {
								inputBuffer.append('\n'); //creates a new line directly under the param line
								inputBuffer.append(argsIntoScenarioOutline);
							}
							break; //break this loop once written
						}
					}
				}
				currentLine = bufferedReader.readLine();
				if(currentLine == null){
					break; //avoid writing "null" to page at page end
				}
				inputBuffer.append(currentLine.concat("\n"));
			}
			bufferedReader.close(); //closes the reader
			FileOutputStream fileOutputStream = new FileOutputStream(fileToRead);
			fileOutputStream.write(inputBuffer.toString().getBytes()); //overwrites the old feature file with the new, processed text
			fileOutputStream.close();
		} catch (IOException e){
			System.out.println("IO Exception thrown, file: " + fileToRead.toString() + " is invalid.");
		}
	}
	private HashMap<File, HashMap<String, LinkedHashMap<String, String[]>>> featureFilesAndExcelSheetTriHashMap(){
		LinkedHashMap<String, LinkedHashMap<String, String[]>> allDataOnExcelOrgInScenarios = excelReader.returnAllDataOnExcelOrgInScenarios();
		/*gets all the data from the excel sheet in a hashmap, organized into columns. See ExcelReader for more on this class
		*/
		HashMap<File, HashMap<String, LinkedHashMap<String, String[]>>> allFeatureFilesThatMultiRun = new HashMap<>();
		/*
		Innermost String array represents one scenario outline, and comprises of individual params as part of that scenario. Key is params, value is String array of arguments for that param
		Mid Layer Hash Map represents one feature file, and comprises all the scenario outlines within that feature file. Key is scenario outlines.
		Outermost hashmap represents all feature files that have scenario outlines. Key is feature files.
		There is no way to name objects dynamically, ideally naming each list listScenarioOutlines after the file it represents would be better, thus hashmaps are used instead
		*/
		LinkedList <File> allFeatureFilesMultiRunScenario = getListOfFilesWithMatchingText("Scenario Outline:", getAllFilesWithCertainExtension(new PathGeneration().returnPathToCucumberFiles(), "feature")); /*Hardcoded, as feature files are all in the same location, and all the other params do not ever change.
		This is a list of all the feature files that have Scenario Outlines (multi run scenarios) in them.*/
		for (int i = 0; i < allFeatureFilesMultiRunScenario.size(); i++) {
			File currentFile = allFeatureFilesMultiRunScenario.get(i);
			LinkedList<String> getLineWithScenarioName = getLinesOfMatchingTextFromSingleFile("Scenario Outline:", currentFile); //returns all the lines that have the name of the scenario outline, used to keybind the param arrays in the next loop
			LinkedList<String> getLineWithParams = getLineAfterMatchingTextFromSingleFile("Examples:", currentFile); //returns all the lines that match "Examples:" in currentFile
			extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper,  "Currently processing file: " + currentFile.getName()); //extentReports
			HashMap<String, LinkedHashMap<String ,String[]>> allScenariosPerFeatureFile = new HashMap<>();
			for (int j = 0; j < getLineWithScenarioName.size(); j++) {
				String scenarioOutlineProcessed = (getLineWithScenarioName.get(j)).split("Scenario Outline:")[1].replaceAll(" ", ""); //gets only the text after Scenario Outline:. Deletes all spaces.
				LinkedHashMap<String, String[]> paramsAndArgsForScenario = allDataOnExcelOrgInScenarios.get(scenarioOutlineProcessed); //Key is column/param, Value is args from that column and for that param
				String paramLine = getLineWithParams.get(j); //gets the line with the params on it, which is the line right after "Examples:"
				paramLine = paramLine.replaceFirst("\\|", "");
				//get rid of the first | as code counts the space before the first | as an element and results in a null pointer.
				// "\\" is because | is a special character and needs to be denoted as such"
				String[] params = (paramLine.replaceAll(" ", "")).split("\\|");
				//remove spaces, and use '|' to separate strings
				// '|' is a special character, hence why \\ is added. Will delete all spaces and then split the line into an array of strings, separated by '|'
				LinkedHashMap<String, String[]> scenarioExampleParamsCurrentFeatFile = new LinkedHashMap<>(); //Defines all the arguments for an individual Scenario outline
				for (int k = 0; k < params.length; k++) { //loops thru all the params in an individual scenario and assigns the arguments to it
					String currentParams = params[k];
					if(paramsAndArgsForScenario == null){
						scenarioExampleParamsCurrentFeatFile.put(params[k], new String[]{"~~unsupported~~"}); //for if a row does not exist on the excel sheet
					} else {
						String[] argumentsForParam = paramsAndArgsForScenario.get(currentParams);
						scenarioExampleParamsCurrentFeatFile.put(params[k], argumentsForParam);
					/*
					Matches the current param with the excel sheet.
					The current param matches one of the columns on the excel sheet
					The resulting data of that column is returned in the array argumentsForParam. This data is selective based on scenario, so we only get the column data that is relevant to the scenario.
					The below line then stores that array in a hashmap, where the key is the column name.
					The goal of this is, rather than getting all the data from the excel sheet, we only pull the parts we need to complete the scenario outline
					*/
					}
				}
				allScenariosPerFeatureFile.put(scenarioOutlineProcessed, scenarioExampleParamsCurrentFeatFile);
				/*this adds an individual scenario to the hash map that stores all the scenarios for one feature file.
				scenarioOutlineLine will be used both to identify what scenario outline these params belong to + later to write these params to the file from the excel sheet, identifying WHERE to write them. */
			}
			allFeatureFilesThatMultiRun.put(currentFile, allScenariosPerFeatureFile); //this adds a single feature file to the hash map that stores all the feature files
		}
		return allFeatureFilesThatMultiRun;
	}
	private void writeNewArgumentsToFeatureFiles(HashMap<File, HashMap<String, LinkedHashMap<String,String[]>>> combinedFeatFileDataSheet){
		for(HashMap.Entry<File, HashMap<String, LinkedHashMap<String,String[]>>> featureFileLevel : combinedFeatFileDataSheet.entrySet()){
			File currentFile = featureFileLevel.getKey();
			HashMap<String, LinkedHashMap<String, String[]>> scenariosInCurrentFile = featureFileLevel.getValue();

			//on this level, we will go into the file (by opening it to read and write on)
			for(HashMap.Entry<String, LinkedHashMap<String, String[]>> scenarioLevel : scenariosInCurrentFile.entrySet()){
				String currentScenario = scenarioLevel.getKey();
				HashMap<String, String[]> paramsInCurrentScenario = scenarioLevel.getValue();
				//on this level, we will go into the scenario (by finding it on the page) and append the arguments at the end of the loop.
				String completeArgs = "";
				int numberOfArgRows = 0;
				for (HashMap.Entry<String, String[]> noOfArgRowsFinder : paramsInCurrentScenario.entrySet()) { //to get the number of rows to generate for the upcoming loop
					extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper,  "Current scenario appending to cucumber: " + currentScenario + " || and current arg: " + noOfArgRowsFinder.getKey());
					System.out.println("Current scenario appending to cucumber: " + currentScenario + " || and current arg: " + noOfArgRowsFinder.getKey()); //does not append sometimes to extent report as this is during initialization, so keeping this statement as an output for debugging purposes
					numberOfArgRows = noOfArgRowsFinder.getValue().length;
				}
				for (int i = 0; i < numberOfArgRows; i++) { //generates the argument rows, one row at a time
					String currentRowArgs = "|";
					boolean rowNotSkipped = true;
					for (HashMap.Entry<String, String[]> paramLevel : paramsInCurrentScenario.entrySet()) { //looping thru each param here
						String param = paramLevel.getKey();
						String[] argumentsForParam = paramLevel.getValue();
						String processedArgumentsForParam = argumentsForParam[i].replaceAll("\\|", "_");
						if(processedArgumentsForParam.contains("~~unsupported~~")){
							currentRowArgs = "";
							rowNotSkipped = false;
							break;
						} else {
							currentRowArgs = currentRowArgs.concat(processedArgumentsForParam).concat("|"); //only concat to appendable arguments if processed arguments do not contain ~~unsupported~~
						}
					}
					if(rowNotSkipped) {
						completeArgs = completeArgs.concat(currentRowArgs.concat("\n")); //starts new line after a row is generated
					}
				}
				extentReportGeneratorForCucumberTestLooper.addInfoMessage(currentTestForCucumberTestLooper,  "Complete args to be added are: " + completeArgs); //extentReports
				//Append the arguments here
				writeArgsToScenarioOutline(currentFile, currentScenario, completeArgs);
			}
		}
	}
	private void deleteOldArgumentsFromFeatureFiles(){
		//revert back to old feature files
		System.out.println("Tests complete, reverting feature files; removing old arguments");
		for(HashMap.Entry<File, StringBuffer> indivFeatureFiles : originalFeatureFiles.entrySet()){
			File currentFile = indivFeatureFiles.getKey();
			StringBuffer currentFileContents = indivFeatureFiles.getValue();

			try {
				FileOutputStream fileOutputStream = new FileOutputStream(currentFile);
				fileOutputStream.write(currentFileContents.toString().getBytes()); //overwrites the old feature file with the new, processed text
				fileOutputStream.close();
			} catch (IOException e){
			System.out.println("IO Exception thrown, file: " + currentFile.toString() + " is invalid.");
			}
		}
	}
}