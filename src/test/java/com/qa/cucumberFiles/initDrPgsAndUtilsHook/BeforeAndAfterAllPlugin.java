package com.qa.cucumberFiles.initDrPgsAndUtilsHook;

import com.aventstack.extentreports.ExtentTest;
import com.qa.coreFrameworkFiles.Initializer;
import com.qa.testNG.Listeners;
import com.qa.utilFiles.coreCodeUtilFiles.*;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;

public class BeforeAndAfterAllPlugin implements ConcurrentEventListener {

	private final Long threadID = Thread.currentThread().getId();
	private final ExtentReportGenerator extentReportGenerator = Listeners.extentReportGenerator;
	private String cucumberDebugModeRun;
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		eventPublisher.registerHandlerFor(TestRunStarted.class, setup);
		eventPublisher.registerHandlerFor(TestRunFinished.class, teardown);
	}
	private EventHandler<TestRunStarted> setup = event -> {
		beforeAll();
	};

	private void beforeAll() {
		//@BeforeAll code goes here
		//start up emulator here. also select the port(s) here, and use the same port(s) throughout this thread rather than regenerating one.
		if(extentReportGenerator.getExtent() == null){
			extentReportGenerator.createExtentReport();
		} //for when running directly from cucumber

		GetPropertiesFromSysOrConfig propertiesAndroidBeforeAll = new GetPropertiesFromSysOrConfig();
		ExtentTest currentTest = extentReportGenerator.testDescriptionCreator("Executing @BeforeAll tasks for cucumber.");
		Initializer.propertiesInit(currentTest, extentReportGenerator);

		extentReportGenerator.addInfoMessage(currentTest, "Determining OS for the host machine (cucumber): " + System.getProperty("os.name") + " and setting up OS specific variables for Processes");
		DetermineOSForProcess.setFirstTwoArgsForProcessCmds(currentTest, extentReportGenerator);
		extentReportGenerator.tearDown();
	}

	private EventHandler<TestRunFinished> teardown = event -> {
		afterAll();
	};

	private void afterAll() {
		ExtentTest currentTest = extentReportGenerator.testDescriptionCreator("Executing @AfterAll tasks for cucumber.");
		extentReportGenerator.tearDown();
	}
}
