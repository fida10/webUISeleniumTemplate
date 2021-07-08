package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class NetworkLogFileGeneration {
	private final String pathToNetworkLogFilesProcessed = new PathGeneration().returnUnifiedPathToLogs_Reports("logs" + File.separator + "networkLogs" + File.separator);
	private File logFile;
	public File networkLogFileGenerator(String featureAndScenarioId, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		String processedLogFilePath = pathToNetworkLogFilesProcessed.concat(featureAndScenarioId + ".log");
		boolean logFileCreatedSuccessfully = false; //initially false, will return true if file made successfully
		File newLogFile = new File(processedLogFilePath);
		try {
			logFileCreatedSuccessfully = newLogFile.createNewFile();
			extentReportGenerator.addInfoMessage(currentTest, "Network Processed Log File created successfully"); //extentReports
		} catch(IOException e){
			extentReportGenerator.addWarningMessage(currentTest, "IO Exception thrown, path is invalid for Network log file. Path used: " + processedLogFilePath + ". Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		} finally {
			extentReportGenerator.addInfoMessage(currentTest, "Network Processed Log file created successful confirmation? --> " + logFileCreatedSuccessfully); //extentReports
		}
		logFile = newLogFile;
		return newLogFile;
	}
	public File getLogFile(){
		return logFile;
	}
}
