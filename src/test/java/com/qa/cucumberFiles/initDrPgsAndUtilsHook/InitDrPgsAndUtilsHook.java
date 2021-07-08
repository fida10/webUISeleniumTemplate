//for use only in step defs. Do not reference this or its objects on page classes
package com.qa.cucumberFiles.initDrPgsAndUtilsHook;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qa.coreFrameworkFiles.Finisher;
import com.qa.cucumberFiles.commonSteps.stepDefFiles.CommonStepDefs;
import com.qa.initializeDriversPagesAndUtilsClasses.InitializeDriverPagesAndUtils;
import com.qa.coreFrameworkFiles.Initializer;
import com.qa.testNG.Listeners;
import com.qa.utilFiles.coreCodeUtilFiles.*;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class InitDrPgsAndUtilsHook { //unofficially also a listeners class for cucumber
	public InitializeDriverPagesAndUtils initDrPgsAndUtils;
	private final Long threadID = Thread.currentThread().getId();
	private String scenarioId;
	private String nameOfCurrentStepDef;
	private int currentStepNumber;

	protected Initializer initializer; //can pull driver object from here instead of making a dedicated object
	private Finisher finisher;
//	protected AppiumLogFileGeneration appiumLogFileGeneration; may replace with network log, browser log later
//	private TestVideoRecord testVideoRecord; may instantiate after fixing video for browser recording

	protected ExtentReportGenerator extentReportGenerator = Listeners.extentReportGenerator;
	protected ExtentTest currentTest;

	private Scenario scenario;

	public InitDrPgsAndUtilsHook(InitializeDriverPagesAndUtils initDrPgsAndUtils){
		this.initDrPgsAndUtils = initDrPgsAndUtils;
	}
	@Before
	public void toDoOnCucumberStart(Scenario scenario){
		System.out.println("Starting initial cucumber test tasks, on current thread: " + threadID);
		currentTest = extentReportGenerator.testDescriptionCreator("NOT A TEST, CUCUMBER @BEFORE TASKS");
		currentStepNumber = 0;
		this.scenario = scenario;

		initializer = new Initializer();
		finisher = new Finisher();
//		appiumLogFileGeneration = new AppiumLogFileGeneration(); may replace with network log, browser log later
//		testVideoRecord = new TestVideoRecord(); may instantiate after fixing video for browser recording
	}
	@BeforeStep
	public void returnMethodJustCompletedAndAddToExtentReport(){
		nameOfCurrentStepDef = "Finished a step, moving on to the next one. \n This is step number: " + currentStepNumber + " || ";
		//no way to get the current step as a string, other than with plugins. Currently just indicating when a step has finished on the report, and indicating what step number was finished.
		extentReportGenerator.addInfoMessage(currentTest, nameOfCurrentStepDef + "Still in the Scenario: " + scenarioId);
		if(currentStepNumber == 0){
			extentReportGenerator.tearDown();
		}
		currentStepNumber++;
	}
	@Given("Execution solution is started and driver is initialized for the test case {string}") //primary purpose of this step is to get the name of the test case from excel and attach to session or make available to cucumber for code/manipulation, as there is no way to extract name from feature file on a @Before tag. This step goes ahead of all others in every scenario outline.
	public void whenExecutionSolutionAndDriverAreStartedForFollowingTestCase(String testCaseName){
		scenarioId = generateScenarioID(scenario, testCaseName);
		currentTest = extentReportGenerator.testDescriptionCreator(scenarioId);
		extentReportGenerator.addInfoMessage(currentTest, "Starting initial cucumber test tasks, on current thread: " + Thread.currentThread().getId() + "and intended thread is: " + threadID);

		GetPropertiesFromSysOrConfig propsForInitDrPgsAndUtils = new GetPropertiesFromSysOrConfig();

		//initializing driver, pages and required utils
		initializer.driverInit(currentTest, extentReportGenerator, scenarioId, Listeners.testRunName);

		initDrPgsAndUtils.initializeDriverObject(initializer.getWebDriver());
		initDrPgsAndUtils.initializeAllPagesAndUtils(currentTest, extentReportGenerator, scenario);
		System.out.println("Started new initDrPgsAndUtils: " + initDrPgsAndUtils.toString() + " on thread: " + threadID);
		extentReportGenerator.addInfoMessage(currentTest, "Started new initDrPgsAndUtils: " + initDrPgsAndUtils.toString() + " on thread: " + Thread.currentThread().getId() + " and intended thread is: " + threadID);
	}
	@After
	public void toDoOnCucumberFinish(Scenario scenario){
	//	extentReportGenerator.addInfoMessage(currentTest, "Has app been removed properly?: " + initializer.getAppiumDriver().removeApp(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("androidAppPackage"))); //uninstalls the application for a fresh install for next test. not using because it takes too long

		if(scenario.getStatus() == io.cucumber.java.Status.PASSED) {
//			extentReportGenerator.addScreenShotBaseSixtyFourString(currentTest, ScreenshotCapture.takeScreenShot("pass", scenarioId, initializer.getAppiumDriver(), currentTest, extentReportGenerator)); //Screenshot class must be modified to work with browser rather than mobile device
			currentTest.log(Status.PASS, "Scenario: " + scenarioId.concat(" has passed."));
			finisher.closeCurrentBrowser(initializer.getWebDriver());
			extentReportGenerator.tearDown();
		} else if(scenario.getStatus() == io.cucumber.java.Status.FAILED) {
			try {
//				extentReportGenerator.addScreenShotBaseSixtyFourString(currentTest, ScreenshotCapture.takeScreenShot("fail", scenarioId, initializer.getAppiumDriver(), currentTest, extentReportGenerator)); //Screenshot class must be modified to work with browser rather than mobile device
				currentTest.log(Status.FAIL, "Scenario: " + scenarioId.concat(" has failed."));
				finisher.closeCurrentBrowser(initializer.getWebDriver());
				extentReportGenerator.tearDown();
			} catch (WebDriverException e) {
				ifWebDriverExceptionThrown();
			}
		} else if(scenario.getStatus() == io.cucumber.java.Status.SKIPPED) {
			try {
//				extentReportGenerator.addScreenShotBaseSixtyFourString(currentTest, ScreenshotCapture.takeScreenShot("skip", scenarioId, initializer.getAppiumDriver(), currentTest, extentReportGenerator)); //Screenshot class must be modified to work with browser rather than mobile device
				currentTest.log(Status.SKIP, "Scenario: " + scenarioId.concat(" has skipped."));
				finisher.closeCurrentBrowser(initializer.getWebDriver());
				extentReportGenerator.tearDown();
			} catch (WebDriverException e) {
				ifWebDriverExceptionThrown();
			}
		}
		finisher.quitDriver(initializer.getWebDriver(), currentTest, extentReportGenerator);
//		finisher.closeServer(initializer.getServer(), currentTest, extentReportGenerator);
		//add code to save network logs, and if possible browser logs, here.
		extentReportGenerator.tearDown();
	}

	private String generateScenarioID(Scenario scenario, String scenarioExcelName){ //this step must be initialized in a cucumber hooks file, like this one, hence why it is here and not in InitializeDriverPagesAndUtils like other utilities. Creates unique name for screenshots, video, etc.
		GetPropertiesFromSysOrConfig propertiesForScenarioID = new GetPropertiesFromSysOrConfig();
		return StringUtils.substringAfter(scenario.getId().replaceAll(":", "_").replaceAll("/", ".")
				.concat("_")
				.concat(propertiesForScenarioID.getPropertyFromSysOrConfig("platform") + "_" + propertiesForScenarioID.getPropertyFromSysOrConfig("env"))
				.concat("__ScenarioIs_")
				.concat(scenarioExcelName.replaceAll(" ", "")), "cucumberFiles.");
		//creates the name for screenshots, recordings, etc. StringUtils removes everything in name before "cucumberFiles.", else an entire path is generated from local machine system directory.
		//gets scenario and feature name, to append to name of files.
	}
	public String getScenarioId() {
		return scenarioId;
	}
	public String getNameOfCurrentStepDef() {
		return nameOfCurrentStepDef;
	}
	private void ifWebDriverExceptionThrown(){
		//Code here to append failed test case in the event that emulator shuts down or crashes (webDriverException)
		String webDriverExceptionMessage = "Web driver exception thrown at end of test execution, implying test execution vector has crashed or some unknown error has occurred. Reverting feature file arguments and closing test run on TestRails.";
		System.out.println(webDriverExceptionMessage);
		extentReportGenerator.addWarningMessage(currentTest, webDriverExceptionMessage);
		currentTest.log(Status.FAIL, "Scenario: " + scenarioId.concat(" has failed, WebDriver exception thrown."));
		extentReportGenerator.tearDown();
	}
}