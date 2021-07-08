package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;

public class DetermineOSForProcess { //start up and set on listeners
	private static final String os_Name = System.getProperty("os.name");
	private static String[] firstTwoArgsForProcessCmds;
	//USE THIS TO DETECT IF OS IS LINUX; PULL THE CORRECT DRIVER IF SO
	public static void setFirstTwoArgsForProcessCmds(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		firstTwoArgsForProcessCmds = new String[2];
		if(os_Name.contains("Win")){
			extentReportGenerator.addInfoMessage(currentTest, "OS Detected is Windows -- " + os_Name);
			firstTwoArgsForProcessCmds[0] = "cmd.exe";
			firstTwoArgsForProcessCmds[1] = "/c";
		} else if(os_Name.contains("Mac")){
			extentReportGenerator.addInfoMessage(currentTest, "OS Detected is Mac -- " + os_Name);
			firstTwoArgsForProcessCmds[0] = "bash";
			firstTwoArgsForProcessCmds[1] = "-c";
		} else {
			extentReportGenerator.addWarningMessage(currentTest, "Invalid OS Detected, please use Windows or Mac -- " + os_Name);
		}
	}
	public static String[] getFirstTwoArgsForProcessCmds() {
		return firstTwoArgsForProcessCmds;
	}
}
