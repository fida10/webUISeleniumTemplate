package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.coreFrameworkFiles.Initializer;
import com.qa.utilFiles.coreCodeUtilFiles.restAPICommands.BrowserStackAppCommands;
import com.qa.utilFiles.coreCodeUtilFiles.restAPICommands.EMTAdminCommands;
import com.qa.utilFiles.coreCodeUtilFiles.restAPICommands.MicrosoftAppCenterCommands;
import com.qa.utilFiles.coreCodeUtilFiles.restAPICommands.TestRailsIntegration;
import com.qa.utilFiles.coreCodeUtilFiles.restAPICommands.capi.CAPIRegistrationIDUpload;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.plaf.IconUIResource;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class DummyMainFile{
	public static void main(String[] args) throws Exception{
		//dummy main file extent report objects
		ExtentReportGenerator dummyMainFileExtentReportGen = new ExtentReportGenerator();
		dummyMainFileExtentReportGen.createExtentReport();
		ExtentTest dummyMainFileExtentTest = dummyMainFileExtentReportGen.testDescriptionCreator("Dummy main file testing, not a test");
		Initializer.propertiesInit(dummyMainFileExtentTest, dummyMainFileExtentReportGen);
		//saucelabs integration quick run
//		String currentAppToGetOS = "android_sauce";
//		new MicrosoftAppCenterCommands().returnDownloadLink(currentAppToGetOS);
//		System.setProperty("platform", currentAppToGetOS);
//		Initializer.startInit();
//		System.out.println("Test");
//		((JavascriptExecutor)Initializer.getAppiumDriver()).executeScript("sauce: break");

//		//app link and version number
//		MicrosoftAppCenterCommands microsoftAppCenterCommands = new MicrosoftAppCenterCommands();
//		System.out.println(microsoftAppCenterCommands.returnDownloadLink());
//		System.out.println(microsoftAppCenterCommands.returnAppVersionNumber());

//
//		//cucumber test looper quick run
		Initializer.propertiesInit(dummyMainFileExtentTest, dummyMainFileExtentReportGen);
		ExcelReader excelReader = new ExcelReader(dummyMainFileExtentTest, dummyMainFileExtentReportGen);
		CucumberTestLooper c = new CucumberTestLooper();
		c.linkCucumberFilesToExcel(dummyMainFileExtentTest, dummyMainFileExtentReportGen);
		System.out.println("Hello world");
		c.unlinkCucumberFilesFromExcel();

		//open chrome on mobile
//		Initializer.startInit();
//		Activity activity = new Activity("com.android.chrome", "org.chromium.chrome.browser.ChromeTabbedActivity");
//		((AndroidDriver) Initializer.getAppiumDriver()).startActivity(activity);
//		Thread.sleep(10000);
//		MobileElement urlBar = Initializer.getAppiumDriver().findElementById("com.android.chrome:id/url_bar");
//		urlBar.clear();
//		urlBar.click();
//		urlBar.sendKeys("http://google.com");
//		MobileElement urlBarFirstResult = Initializer.getAppiumDriver().findElementsByXPath("//android.widget.TextView").get(0);
//		urlBarFirstResult.click();
//
//		Thread.sleep(10000);
//		((AndroidDriver) Initializer.getAppiumDriver()).resetApp();

		//random number generator
//		String randomStringToSend = (UUID.randomUUID().toString()).replaceAll("-", "").substring(0, 8);
//		System.out.println(randomStringToSend);
//		System.out.println(RandomStringUtils.randomNumeric(6));
		//get the current month name as a string
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MONTH, 2);
//		cal.add(Calendar.YEAR, 1);
//
//		System.out.println(new SimpleDateFormat("yyyy").format(cal.getTime()));
//
//		//test radio button xpath for cancel transaction
//		int radioButtonNo = new Random().nextInt(6) + 1;
//		String path = String.format("//XCUIElementTypeCell[%s]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeStaticText/following-sibling::XCUIElementTypeOther", radioButtonNo);
//		System.out.println(path);

		//testing out testrails integration functions
//		TestRailsIntegration railsIntegration = new TestRailsIntegration();
//		railsIntegration.addRun();
//		railsIntegration.addResultForCase("2", "158372");
//		railsIntegration.closeRun();

		//browserStack app upload
//		new BrowserStackAppCommands().uploadApp(new MicrosoftAppCenterCommands().returnDownloadLink(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("platform"), dummyMainFileExtentTest, dummyMainFileExtentReportGen), "dummy run", dummyMainFileExtentTest, dummyMainFileExtentReportGen);

		//testing retrieval of build id from browserstack
//		System.out.println(new BrowserStackAppCommands().retrieveCurrentBuildIDForBrowserStackRun("", dummyMainFileExtentTest, dummyMainFileExtentReportGen));

		//browserstack appium log test
//		FileWriter writer = new FileWriter(new File("projectLogs_reports/singleRuns/prototypeLogFile/logFileAppium.log"));
//
//		String appiumLog = new BrowserStackAppCommands().retrieveAppiumLogForBrowserStackRun("923bbcbc991c861979a6be6f8922084b3a43dd77", "2ed07ea22100e122d6e9b407a8e4f44845486a9b", dummyMainFileExtentTest, dummyMainFileExtentReportGen);
//		System.out.println(appiumLog);
//		System.out.println("Writing to log file");
//		writer.write(appiumLog);
//		writer.close();


		//replacing the backslash
//		String someSlash = "\\some_text";
//		System.out.println(someSlash);
//		someSlash = someSlash.replace("\\", "Replaced!");
//		System.out.println(someSlash);

		//testing EMT and idUpload
//		EMTAdminCommands emtAdminCommands = new EMTAdminCommands();
//		CAPIRegistrationIDUpload capi = new CAPIRegistrationIDUpload();
//
//		System.setProperty("country", "GB");
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_GB_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen, emtAdminCommands.getProfileToUseArray()[5]);
//		emtAdminCommands.emtUpdateProfile(dummyMainFileExtentTest, dummyMainFileExtentReportGen, capi.getRegistrationJSON(), capi.getProfileID());
//		capi.uploadID(dummyMainFileExtentTest, dummyMainFileExtentReportGen, emtAdminCommands.getProfileToUse());

		//testing registration
//		CAPIRegistrationIDUpload capi = new CAPIRegistrationIDUpload();
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_US_geburtsort_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen);
//		capi.uploadID(dummyMainFileExtentTest, dummyMainFileExtentReportGen);

//		System.setProperty("country", "GB");
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_GB_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen);

// 		System.setProperty("country", "DE");
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_DE_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen);

// 		System.setProperty("country", "CA");
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_CA_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen);

// 		System.setProperty("country", "FR");
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_FR_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen, emtAdminCommands.getProfileToUseArray()[5]);
//		emtAdminCommands.emtUpdateProfileStatus(dummyMainFileExtentTest, dummyMainFileExtentReportGen, capi.getProfileID()); //to get around ID upload


// 		System.setProperty("country", "IT");
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_IT_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen);

// 		System.setProperty("country", "ES");
//		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_ES_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen);

// 		System.setProperty("country", "AU");
// 		capi.createProfileLoopForDuplicateEmailPhone("regNewAcc_AU_noName_1", dummyMainFileExtentTest, dummyMainFileExtentReportGen);

		//random string utils for random number
//		System.out.println(RandomStringUtils.randomNumeric(45));

		//capi ID upload
//		new CAPIRegistrationIDUpload().uploadIDOnFidoPassport(dummyMainFileExtentTest, dummyMainFileExtentReportGen, "Bearer 99868ec8-c942-4fd3-ac75-2b6be1dd653a", "en_IT", "specimen_e_BE.png");

		//testrail test plan creation test -->
//		TestRailsIntegration testRailsIntegration = new TestRailsIntegration();
//
//		HashMap<String,String> runAndEntryID = testRailsIntegration.addTestPlanEntry(dummyMainFileExtentTest, dummyMainFileExtentReportGen, "testEntryFromIntelliJ");
//		String entryID = runAndEntryID.get("currentEntryID");
//		String runID = runAndEntryID.get("currentRunID");
//		System.out.println("Test entry added, check.");
//		testRailsIntegration.updatePlanEntryWithCase("158372", entryID, dummyMainFileExtentTest, dummyMainFileExtentReportGen);
//		testRailsIntegration.addResultForCase("1", "158372", runID, dummyMainFileExtentTest, dummyMainFileExtentReportGen);
//		System.out.println("Updated case status of 158372, check.");

		//checking ID upload from browserstack server and deletion
//		EMTAdminCommands emtAdminCommands = new EMTAdminCommands();
//		BrowserStackAppCommands browserStackAppCommands = new BrowserStackAppCommands();
//
//		browserStackAppCommands.uploadMediaToBrowserStack(emtAdminCommands.getProfileToUse(), dummyMainFileExtentTest, dummyMainFileExtentReportGen);
//		browserStackAppCommands.deleteMediaFromBrowserStack(browserStackAppCommands.getIdOfMediaUploadedIDUpload(), dummyMainFileExtentTest, dummyMainFileExtentReportGen);

	}
}