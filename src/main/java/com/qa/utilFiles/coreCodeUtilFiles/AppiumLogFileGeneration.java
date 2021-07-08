package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;

import java.io.*;
import java.util.Arrays;

public class AppiumLogFileGeneration {
	private final String pathToAppiumLogFilesProcessed = new PathGeneration().returnUnifiedPathToLogs_Reports("logs" + File.separator + "appiumLogs" + File.separator);
	private File logFile;
	public File appiumLogFileGenerator(String featureAndScenarioId, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		String processedLogFilePath = pathToAppiumLogFilesProcessed.concat(featureAndScenarioId + ".log");
		boolean logFileCreatedSuccessfully = false; //initially false, will return true if file made successfully
		File newLogFile = new File(processedLogFilePath);
		try {
			logFileCreatedSuccessfully = newLogFile.createNewFile();
			extentReportGenerator.addInfoMessage(currentTest, "Appium Processed Log File created successfully"); //extentReports
		} catch(IOException e){
			extentReportGenerator.addWarningMessage(currentTest, "IO Exception thrown, path is invalid for Appium log file. Path used: " + processedLogFilePath + ". Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		} finally {
			extentReportGenerator.addInfoMessage(currentTest, "Appium Processed Log file created successful confirmation? --> " + logFileCreatedSuccessfully); //extentReports
		}
		logFile = newLogFile;
		return newLogFile;
	}
	public File getLogFile(){
		return logFile;
	}
}
