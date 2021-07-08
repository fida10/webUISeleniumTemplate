package com.qa.testNG;

import com.aventstack.extentreports.ExtentTest;
import com.qa.utilFiles.coreCodeUtilFiles.*;
import com.qa.utilFiles.coreCodeUtilFiles.restAPICommands.TestRailsIntegration;
import org.apache.maven.shared.utils.cli.ShutdownHookUtils;
import org.testng.IAlterSuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;

import java.util.HashMap;
import java.util.List;

public class Listeners implements ITestListener, IAlterSuiteListener {

	protected final CucumberTestLooper cucumberTestLooper = new CucumberTestLooper();
	private GitAutoCommands gitAutoCommands;

	public static final TestRailsIntegration railsIntegration = new TestRailsIntegration(); //use this to find commented out testrails code
	public static final String testRunName = "MGO_NativeTests_" + PathGeneration.getTypeOfRunString().replace("\\", "__");

	public static String currentRunID;
	//test rails integration fields made static because only a single test run will be generated, even when running in parallel. Current run ID must also remain static (the same) across framework during run
	public static String currentEntryID;
	//entry ID used for test plans

	public static final ExtentReportGenerator extentReportGenerator = new ExtentReportGenerator();

	private static boolean frameworkEndedProperly = false; //for activating the failsafe java hook, in the event that the framework fails or quits unexpectedly
	private Thread currentShutdownHookThread;

	@Override
	public void alter(List<XmlSuite> suites) { //allows altering the testng xml before it is even run
		GetPropertiesFromSysOrConfig parallelProps = new GetPropertiesFromSysOrConfig();
		IAlterSuiteListener.super.alter(suites);
		XmlSuite suite = suites.get(0);
		suites.get(0).setParallel(ParallelMode.CLASSES);
		suite.setThreadCount(Integer.parseInt(parallelProps.getPropertyFromSysOrConfig("parallelThreadCountToBeUsed")));
	}

	@Override
	public void onTestStart(ITestResult result) {

	}
	@Override
	public void onTestSuccess(ITestResult result) {

	}
	@Override
	public void onTestFailure(ITestResult result) {

	}
	@Override
	public void onTestSkipped(ITestResult result) {

	}
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}
	@Override
	public void onTestFailedWithTimeout(ITestResult result) {

	}
	@Override
	public void onStart(ITestContext context) {

		System.out.println("Using the following path for logs and reports: " + PathGeneration.getLogsAndReportsDatedDirectory()); //cannot add this into extent reports, as extent report creation is done after this step

		extentReportGenerator.createExtentReport(); //initializes the single extent report, used throughout the framework and the run
		ExtentTest initialFrameworkTasksTest = extentReportGenerator.testDescriptionCreator("Initializing framework, not a test"); //use this as "current test" param in listeners

		extentReportGenerator.addInfoMessage(initialFrameworkTasksTest, "Determining OS for the host machine (TestNG/Listeners): " + System.getProperty("os.name") + " and setting up OS specific variables for Processes");
		DetermineOSForProcess.setFirstTwoArgsForProcessCmds(initialFrameworkTasksTest, extentReportGenerator);

		extentReportGenerator.addInfoMessage(initialFrameworkTasksTest, "Using the following path for logs and reports: " + PathGeneration.getLogsAndReportsDatedDirectory());

		gitAutoCommands = new GitAutoCommands(initialFrameworkTasksTest, extentReportGenerator);
		gitAutoCommands.gitAutoCommit(); //git auto commit before start

		cucumberTestLooper.linkCucumberFilesToExcel(initialFrameworkTasksTest, extentReportGenerator); //within the class itself, loads up properties, then ExcelReader, and finally writes to feature files

		System.out.println("Using test run name: " + testRunName);
		extentReportGenerator.addInfoMessage(initialFrameworkTasksTest, "Using test run name: " + testRunName);
		GetPropertiesFromSysOrConfig testRailProperties = new GetPropertiesFromSysOrConfig();
		if(testRailProperties.getPropertyFromSysOrConfig("usingTestPlan").equalsIgnoreCase("false")) {
			currentRunID = railsIntegration.addRun(initialFrameworkTasksTest, extentReportGenerator, testRunName); //adds test rails run
		} else {
			HashMap<String, String> runIDandEntryID = railsIntegration.addTestPlanEntry(initialFrameworkTasksTest, extentReportGenerator, testRunName);
			currentRunID = runIDandEntryID.get("currentRunID");
			currentEntryID = runIDandEntryID.get("currentEntryID");
		}
		extentReportGenerator.addInfoMessage(initialFrameworkTasksTest, "Initial tasks (onStart) complete");

		extentReportGenerator.addInfoMessage(initialFrameworkTasksTest, "Adding failsafe java hook");
		Runtime.getRuntime().addShutdownHook(currentShutdownHookThread = new Thread() { //puts a reasonable guarantee that onFinish tasks will be executed, even if the application crashes conventionally
			public void failsafeJavaHook(){
				onFinishTasks();
			}
		}); //failsafe hook does not work properly

		extentReportGenerator.tearDown(); //flushes report once initial tasks have been executed, so actual scenarios can be run
		System.out.println("Initial tasks (onStart) complete");

	}
	@Override
	public void onFinish(ITestContext context) {
		onFinishTasks();
		if(frameworkEndedProperly) {
			ShutdownHookUtils.removeShutdownHook(currentShutdownHookThread);
		}
	}

	public void onFinishTasks(){
		ExtentTest finisherFrameworkTasksTest = extentReportGenerator.testDescriptionCreator("Finishing framework, not a test");
		railsIntegration.closeRun(currentRunID, finisherFrameworkTasksTest, extentReportGenerator);

		cucumberTestLooper.unlinkCucumberFilesFromExcel(); //resets all feature files to their old state
		gitAutoCommands.gitAutoHardReset();
		extentReportGenerator.tearDown();

		frameworkEndedProperly = true; //ensures that the shutdown hook is not executed, since framework has exited normally to reach this point
	}
	//getters
}
