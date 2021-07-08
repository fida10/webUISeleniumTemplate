package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class ScreenshotCapture {
	private static final String pathToScreenshots = new PathGeneration().returnUnifiedPathToLogs_Reports("screenShots" + File.separator); /*gets path to screenshot directory in a String.
	This is so as not to avoid regeneration during test run.
	 This would result in multiple directories being made for each test case
	 We only want one directory per run, which stores all the screenshots of that run within*/
	//MODIFICATIONS REQUIRED TO WORK ON WEB.
	public static String takeScreenShot(String passFailSkip, String featureAndScenarioId, AppiumDriver<MobileElement> appiumDriver, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		String pathToSave = pathToScreenshots.concat(passFailSkip).concat(File.separator).concat(featureAndScenarioId).concat(".png");
		String baseSixtyFourStringScreenshot = null;
		try {
			File screenShot = ((TakesScreenshot) appiumDriver).getScreenshotAs(OutputType.FILE); /*takes screenshot on whatever is currently displayed on Appium driver
		which is basically the current phone screen
		and saves this as a file*/
			FileUtils.copyFile(screenShot, new File(pathToSave));
			extentReportGenerator.addInfoMessage(currentTest, "Test status: ".concat(passFailSkip).concat(" || taking screenshot")); //extentReports

			baseSixtyFourStringScreenshot = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(screenShot)); //takes screenshot and converts to a base64 string, for use in extent reports
		} catch (IOException e){
			extentReportGenerator.addWarningMessage(currentTest, "Screenshot file path invalid, IO Exception caught. Using path: ".concat(pathToSave) + ". Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
		return baseSixtyFourStringScreenshot;
	}
}
