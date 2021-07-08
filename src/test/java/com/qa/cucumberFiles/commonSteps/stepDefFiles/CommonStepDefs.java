package com.qa.cucumberFiles.commonSteps.stepDefFiles;

import com.qa.initializeDriversPagesAndUtilsClasses.InitializeDriverPagesAndUtils;
import com.qa.utilFiles.coreCodeUtilFiles.GetPropertiesFromSysOrConfig;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import io.cucumber.java.en.Given;

import static com.qa.testNG.Listeners.*;

public class CommonStepDefs {

    // PREFILLED WITH TESTRAIL INTEGRATION METHODS AND STEPS. DELETE IF THIS IS UNDESIRABLE.
    private String testRailsTestCaseUniqueID;
    private boolean isTestCaseIDValid;
    InitializeDriverPagesAndUtils initDrPgsAndUtils;
    public CommonStepDefs(InitializeDriverPagesAndUtils initDrPgsAndUtils){
        this.initDrPgsAndUtils = initDrPgsAndUtils;
    }

    @Given("Current test scenario is supported by application {string} {string}") //if "~~unsupported~~" string exists in this column, this test case will be skipped
    //testRails portion is only being done at this point because testrail test ID needs to be obtained from excel, so initializing and closing testrail here rather than initDrPgsAndUtils
    public void currentTestScenarioIsSupportedByApplication(String testCaseTitle_TestRails, String testCaseID_TestRails){
        testCaseTitle_TestRails = testCaseTitle_TestRails.replaceAll(" ", "");
        initDrPgsAndUtils.getExtentReportGeneratorInitDrPgsAndUtils().addInfoMessage(initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), "Currently running TestRails TestCase with title: " + testCaseTitle_TestRails + " and TestCase ID: " + testCaseID_TestRails);
        System.out.println("Currently running TestRails TestCase with title: " + testCaseTitle_TestRails + " and TestCase ID: " + testCaseID_TestRails);
        testRailsTestCaseUniqueID = testCaseID_TestRails;

        isTestCaseIDValid = railsIntegration.checkIfCaseIDExists(testCaseID_TestRails, initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), initDrPgsAndUtils.getExtentReportGeneratorInitDrPgsAndUtils());

        if(!(testCaseID_TestRails.contains("NOT_A_TEST_CASE") || testCaseID_TestRails.contains("not a test") || testCaseID_TestRails.contains("blank"))) {
            if(isTestCaseIDValid) {
                if(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("usingTestPlan").equalsIgnoreCase("false")) {
                    railsIntegration.updateRunWithCase(testCaseID_TestRails, currentRunID, initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), initDrPgsAndUtils.getExtentReportGeneratorInitDrPgsAndUtils());
                } else {
                    railsIntegration.updatePlanEntryWithCase(testCaseID_TestRails, currentEntryID, initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), initDrPgsAndUtils.getExtentReportGeneratorInitDrPgsAndUtils());
                }
            }
        }
    }
    //test rails integration 'after' hook
    @After
    public void setTestRailsCaseStatus(Scenario passFail){
        String rerun = new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("rerun");
        if(!(testRailsTestCaseUniqueID.contains("NOT_A_TEST_CASE") || testRailsTestCaseUniqueID.contains("not a test") || testRailsTestCaseUniqueID.contains("blank"))) {
            if(isTestCaseIDValid) {
                System.out.println("Test case status before test rails upload: " + passFail.getStatus() + " on test case: " + passFail.getName());
                if (passFail.getStatus() == Status.FAILED) {
                    if (rerun.equalsIgnoreCase("false")) {
                        extentReportGenerator.addInfoMessage(initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), "This run is not a rerun, will set failed test case status to retest status on testrail, if active.");
                        railsIntegration.addResultForCase("4", testRailsTestCaseUniqueID, currentRunID, initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), initDrPgsAndUtils.getExtentReportGeneratorInitDrPgsAndUtils());
                    } else {
                        railsIntegration.addResultForCase("5", testRailsTestCaseUniqueID, currentRunID, initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), initDrPgsAndUtils.getExtentReportGeneratorInitDrPgsAndUtils());
                    }
                } else if(passFail.getStatus() == Status.PASSED) {
                    railsIntegration.addResultForCase("1", testRailsTestCaseUniqueID, currentRunID, initDrPgsAndUtils.getCurrentTestInitDrPgsAndUtils(), initDrPgsAndUtils.getExtentReportGeneratorInitDrPgsAndUtils());
                }
            }
        }
    }
}
