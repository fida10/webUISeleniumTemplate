package com.qa.testNG;

import com.aventstack.extentreports.ExtentTest;
import com.qa.coreFrameworkFiles.Finisher;
import com.qa.coreFrameworkFiles.Initializer;
import com.qa.cucumberFiles.android.initDrPgsAndUtilsHook.InitDrPgsAndUtilsHook;
import com.qa.utilFiles.coreCodeUtilFiles.AppiumLogFileGeneration;
import com.qa.utilFiles.coreCodeUtilFiles.CucumberTestLooper;
import com.qa.utilFiles.coreCodeUtilFiles.ExtentReportGenerator;
import com.qa.utilFiles.coreCodeUtilFiles.GetPropertiesFromSysOrConfig;
import com.qa.utilFiles.coreCodeUtilFiles.restAPICommands.TestRailsIntegration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriverException;

public class OnTestFinish{
	/*as TestNG Listeners does not provide a Listener to run onTestFinish, put all tasks that should run at the end of each test here, and call this class on all test finish conditions, i.e. onTestSuccess, onTestFailure, etc.
	This class should define GENERIC workflows that will happen on all end conditions. For specific end conditions (i.e. pass, fail, etc., apply directly to Listeners class)
	 */

	public void ifWebDriverExceptionThrown(){

	}
//	public void extentReportTearDown() {
//		ExtentReportGenerator.tearDown();
//	}
}
