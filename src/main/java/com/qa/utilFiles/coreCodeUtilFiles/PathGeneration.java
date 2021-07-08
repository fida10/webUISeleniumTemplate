package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.coreFrameworkFiles.Initializer;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

public class PathGeneration {
	FileReader fileReader;
	private static final String typeOfRunString = typeOfRun();
	private static final String logsAndReportsDatedDirectory = logsAndReportsDatedDirectoryGenerateName();
	public static String returnAbsPath(){
		return System.getProperty("user.dir").concat(File.separator);
	}
	public String returnPathToConfigProp() {
		return returnAbsPath().concat("src"+ File.separator + "main" + File.separator + "projectCoreResources" + File.separator + "properties" + File.separator + "config.properties");
	}
	private static String typeOfRun(){ //for creating directory for either multi run (multiple countries + env) or single run (one country + one env)
		Initializer.propInitNoExtentReportMess();
		GetPropertiesFromSysOrConfig props = new GetPropertiesFromSysOrConfig();
		String dateFormat = "yyyy" + "_" + "MM" + "_" + "dd" + "__" + "a_hh_mm_ss";
		DateTimeFormatter dtForm = DateTimeFormatter.ofPattern(dateFormat); //formats date with the pattern specified above
		LocalDateTime current = LocalDateTime.now();
		String browserToUse = props.getPropertyFromSysOrConfig("browserPlatform");

		String uniqueIDForRun = browserToUse + "_" + props.getPropertyFromSysOrConfig("env") + "_"  + props.getPropertyFromSysOrConfig("country") + "_rerun_" + props.getPropertyFromSysOrConfig("rerun") + "__" + "dateTime_" + dtForm.format(current) + "_singleRunID_" + RandomStringUtils.randomAlphanumeric(8);

		String typeOfRun = "singleRuns" + File.separator + uniqueIDForRun;
		String typeOfRunProperty = props.getPropertyFromSysOrConfig("typeOfRun");
		if(typeOfRunProperty != null && typeOfRunProperty.contains("multiRun")){
			typeOfRun = "multiRuns" + File.separator + typeOfRunProperty;
		}
		return typeOfRun;
	}
	private static String logsAndReportsDatedDirectoryGenerateName(){
		Initializer.propInitNoExtentReportMess();
		return returnAbsPath().concat("projectLogs_reports" + File.separator).concat(typeOfRunString).concat(File.separator).concat("browser_" + new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("browserPlatform")).concat("_env_" + new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("env")).concat("_country_" + new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("country") + File.separator);
	}
	public static String returnCurrentTimeStringPath(){
		String dateFormat = "yyyy" + File.separator + "MM" + File.separator + "dd" + File.separator + "a_hh_mm"; //add 'a' at the end if AM/PM is desired
		DateTimeFormatter dtForm = DateTimeFormatter.ofPattern(dateFormat); //formats date with the pattern specified above

		LocalDateTime current = LocalDateTime.now();
		return dtForm.format(current).concat(File.separator);
		/* usually used in conjunction with another method and added on. No
		 create directories needed for this one. */
	}
	public static String returnPathToDriverExecutable(String browserToUse){ //may need to alter + add linux support if packaging in a container
		String pathToDriversDirectory = returnAbsPath() + "src" + File.separator + "main" + File.separator + "projectCoreResources" + File.separator + "drivers" + File.separator;
		if(browserToUse.equalsIgnoreCase("chrome")){
			return pathToDriversDirectory + "chromedriver.exe";
		} else if(browserToUse.equalsIgnoreCase("firefox")) {
			return pathToDriversDirectory + "geckodriver.exe";
		} else {
			return null;
		}
	}
	public String returnUnifiedPathToLogs_Reports(String pathOfFileWithEndingSlashes){
		String path = logsAndReportsDatedDirectory.concat(pathOfFileWithEndingSlashes.concat(File.separator));
		boolean directoryCreated = new File(path).mkdirs();
		System.out.println("Was the " + pathOfFileWithEndingSlashes +" directory made successfully?: " + directoryCreated); //cannot write this into extentReports without throwing a nullPointer, as this is needed to generate extentReports!
		return path;
	}
	public String returnExcelDataSheetDir(){
		return returnAbsPath().concat("src" + File.separator + "test" + File.separator + "projectTestResources" + File.separator + "testData" + File.separator + "excelFiles"  + File.separator);
	}
	public String returnPathToCucumberFiles(){
		return returnAbsPath() + "src" + File.separator + "test" + File.separator + "java" + File.separator + "com" + File.separator + "qa" + File.separator + "cucumberFiles" + File.separator;
	}
	public String returnPathToRegistrationIDUploadPictures(){
		return returnAbsPath().concat("src" + File.separator + "test" + File.separator + "projectTestResources" + File.separator + "registration" + File.separator + "uploadPhotos" + File.separator);
	}
	public String returnPathToRegistrationJSONPayload(){
		return returnAbsPath().concat("src" + File.separator + "test" + File.separator + "projectTestResources" + File.separator + "registration" + File.separator + "registrationJSONPayloads" + File.separator);
	}
	public String returnPathToCreatedAccountsLogTextFile(){
		return returnAbsPath().concat("src" + File.separator + "test" + File.separator + "projectTestResources" + File.separator + "registration" + File.separator + "createdAccounts" + File.separator + "createdAccountsRepository.log");
	}
	public String returnPathToAllTestsRunners(){
		return returnAbsPath().concat("src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator + "qa" + File.separator + "utilFiles" + File.separator + "testSuiteMultiRunners");
	}
	public FileReader convertStringToFileReaderObject(String pathToFile) {
		try { //exception is caught if the path provided is invalid
			fileReader = new FileReader(pathToFile);
		} catch (IOException e) {
			System.out.println("IO Exception thrown, Invalid path provided, unable to generate FileReader object. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
		return fileReader;
	}
	public File convertStringToFileObject(String pathToFile){
		return new File(pathToFile);
	}
	public void copyOneFileToAnother(String inputFilePathIncFile, String outputFilePathIncFile, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){ //generally used for log files when the name from cucumber is not yet available, because cucumber has not yet been initialized
		try {
			FileReader fr = new FileReader(inputFilePathIncFile);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(outputFilePathIncFile, true);
			String s;

			while ((s = br.readLine()) != null) { // read a line
				fw.write(s); // write to output file
				fw.flush();
			}
			br.close();
			fw.close();
			extentReportGenerator.addInfoMessage(currentTest, "Copied " + inputFilePathIncFile + " to " + outputFilePathIncFile + " successfully."); //extentReports
		} catch (IOException e){
			extentReportGenerator.addWarningMessage(currentTest, "IO exception thrown, unable to copy " + inputFilePathIncFile + " to " + outputFilePathIncFile + ". Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports;
		}
	}
	public static String getLogsAndReportsDatedDirectory() {
		return logsAndReportsDatedDirectory;
	}
	public static String getTypeOfRunString(){
		return typeOfRunString;
	}
}
