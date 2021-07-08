package com.qa.coreFrameworkFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.utilFiles.coreCodeUtilFiles.ExtentReportGenerator;
import com.qa.utilFiles.coreCodeUtilFiles.GetPropertiesFromSysOrConfig;
import com.qa.utilFiles.coreCodeUtilFiles.PathGeneration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Initializer {
	/*initializes the driver object + properties file.
Fields and methods are made static because they will not be changed once initialized and can also be called directly*/
	private final static Properties pro = new Properties();
	private static boolean havePropertiesBeenLoaded = false;

	private String homePageURL;

	private WebDriver webDriver = null;
	/*
	* Both fields above set to null to initialize. Values adds in methods below
	*/
	public static void propertiesInit(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator) { //properties remains static as there is only one config.properties file, stays constant regardless of parallel testing
		PathGeneration paths = new PathGeneration();
		if(!havePropertiesBeenLoaded) {
			try {
				pro.load(paths.convertStringToFileReaderObject(paths.returnPathToConfigProp()));
				extentReportGenerator.addInfoMessage(currentTest, "config.properties loaded successfully"); //extentReports
				havePropertiesBeenLoaded = true;
			} catch (IOException e) {
				extentReportGenerator.addWarningMessage(currentTest, "IO Exception thrown, invalid path to config.properties provided. Stacktrace: " + Arrays.toString(e.getStackTrace())
				); //extentReports
			}
		}
	}
	public static void propInitNoExtentReportMess() { //for initializing properties at the very beginning of the framework, even before extent report is initialized. Specifically just made for the generateRunName method done at beginning of listeners
		PathGeneration paths = new PathGeneration();
		if(!havePropertiesBeenLoaded) {
			try {
				pro.load(paths.convertStringToFileReaderObject(paths.returnPathToConfigProp()));
				System.out.println("config.properties loaded successfully"); //extentReports
				havePropertiesBeenLoaded = true;
			} catch (IOException e) {
				System.out.println("IO Exception thrown, invalid path to config.properties provided. Stacktrace: " + Arrays.toString(e.getStackTrace())
				); //extentReports
			}
		}
	}
	public void driverInit(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator,String scenarioName, String testRunName){
		String browserToUse = new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("browserPlatform");
		String pathToDriverExecutable = PathGeneration.returnPathToDriverExecutable(browserToUse);
		assert pathToDriverExecutable != null;
		InitializerDesiredCaps desiredCaps = new InitializerDesiredCaps();

		if(browserToUse.equalsIgnoreCase("chrome")) {
			extentReportGenerator.addInfoMessage(currentTest, "Running test cases via Chrome"); //extentReports
			int loopTimeForSessionNotCreated = Integer.parseInt(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("waitForChromeDriverSessionNotCreatedLoop"));
			boolean driverCreated = false;
			System.setProperty(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("chromeKey"), pathToDriverExecutable);
			Instant start = Instant.now();
			while (!driverCreated) {
				try {
					webDriver = new ChromeDriver(); //sets the webDriver as an ChromeDriver object
					driverCreated = true;
				} catch (SessionNotCreatedException e) {
					extentReportGenerator.addWarningMessage(currentTest, "DRIVER FAILED TO START DUE TO SESSION NOT CREATED EXCEPTION! ATTEMPTING TO CREATE AGAIN");
				}
				Instant end = Instant.now();
				long secondsPassed = Duration.between(start, end).getSeconds();
				if(secondsPassed > loopTimeForSessionNotCreated){
					extentReportGenerator.addWarningMessage(currentTest, "DRIVER FAILED TO START DUE TO SESSION NOT CREATED EXCEPTION, AND TIME HAS ELAPSED. BREAKING LOOP, WILL RESULT IN TEST FAILURE.");
					break;
				}
			}
		}
		else if(browserToUse.equalsIgnoreCase("firefox")){
			extentReportGenerator.addInfoMessage(currentTest, "Running test cases via Firefox"); //extentReports
			int loopTimeForSessionNotCreated = Integer.parseInt(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("waitForFirefoxDriverSessionNotCreatedLoop"));
			boolean driverCreated = false;
			System.setProperty(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("firefoxKey"), pathToDriverExecutable);
			Instant start = Instant.now();
			while (!driverCreated) {
				try {

					webDriver = new FirefoxDriver(); //sets the appiumDriver as an IOSDriver object
					driverCreated = true;
				} catch (SessionNotCreatedException e) {
					extentReportGenerator.addWarningMessage(currentTest, "DRIVER FAILED TO START DUE TO SESSION NOT CREATED EXCEPTION! ATTEMPTING TO CREATE AGAIN");
				}
				Instant end = Instant.now();
				long secondsPassed = Duration.between(start, end).getSeconds();
				if(secondsPassed > loopTimeForSessionNotCreated){
					extentReportGenerator.addWarningMessage(currentTest, "DRIVER FAILED TO START DUE TO SESSION NOT CREATED EXCEPTION, AND TIME HAS ELAPSED. BREAKING LOOP, WILL RESULT IN TEST FAILURE.");
					break;
				}
			}
		}
		//set implicit wait timer
		webDriver.manage().timeouts().implicitlyWait(Long.parseLong(pro.getProperty("implicitWaitTimer")), TimeUnit.SECONDS); //implicit wait, applies globally

		//launch website
		homePageURL = ""; //homepage url goes here
		webDriver.get(homePageURL);
	}
	public static Properties getPro(){
		return pro;
	}
	public String getHomePageURL() {
		return homePageURL;
	}
	public WebDriver getWebDriver(){
		return webDriver;
	} //use this to get the driver object in other parts of the framework
}
