package com.qa.coreFrameworkFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.utilFiles.coreCodeUtilFiles.ExtentReportGenerator;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.WebDriver;

public class Finisher{
	//add method here to end appium server once all tests have run
	public void closeCurrentBrowser(WebDriver webDriver){
		webDriver.close();
	}
	public void quitDriver(WebDriver webDriver, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		if(webDriver != null) {
			webDriver.quit();
			extentReportGenerator.addInfoMessage(currentTest, "Driver object: " + webDriver + " has stopped."); //extentReports
		} else {
			extentReportGenerator.addWarningMessage(currentTest, "Driver close command given but driver is already closed or null!"); //extentReports
		}
	}
}
