package com.qa.utilFiles.coreCodeUtilFiles;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.qa.coreFrameworkFiles.Initializer;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

public class ExtentReportGenerator {

    private ExtentReports extent;
    private File createBlankExtentReport(){
        Initializer.propInitNoExtentReportMess();
        File generatedExtentReportFile = null;
        try {
            generatedExtentReportFile = new File(new PathGeneration().returnUnifiedPathToLogs_Reports("logs" + File.separator + "extentReports" + File.separator).concat("generatedExtentReport.html"));
            System.out.println("Extent Report File created successfully: " + generatedExtentReportFile.createNewFile());
        } catch (IOException e){
            System.out.println("IO exception thrown while attempting to create extent report HTML file");
            e.printStackTrace();
        }
        return generatedExtentReportFile;
    }
    public void createExtentReport(){
        extent = new ExtentReports(); //Create extent reports and attach reporter(s)
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(createBlankExtentReport());
        extent.attachReporter(sparkReporter);

        //initial tasks section of extent report
//        currentTest = extent.createTest("Initializing framework, not a test");
//        currentTest.log(Status.INFO, "Doing initial tasks for framework, i.e. gitAutoCommit, appending arguments from excel, initializing driver, etc.");
    }
    public void tearDown() {
        boolean extentReportFlushed = false;
        while(!extentReportFlushed) {
            try {
                extent.flush();
                extentReportFlushed = true;
                System.out.println("Successfully flushed extent report");
            } catch (ConcurrentModificationException e) {
                System.out.println("Caught concurrent modification exception while trying to flush extent report, meaning that the rare event where the extent report is concurrently accessed by multiple threads at the exact same time has occurred! Attempting to flush again.");
            }
        }
    }
    public ExtentTest testDescriptionCreator(String testName) { //must be made dynamic
        ExtentTest currentTest = extent.createTest(testName);
        addInfoMessage(currentTest,"Starting scenario: " + testName);
        return currentTest;
    }
    public void addInfoMessage(ExtentTest currentTest, String infoMessage){ //for general output statements
        try {
            if(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("printExtentReportMssgToConsole").equalsIgnoreCase("true")){
                System.out.println(infoMessage);
            }
            currentTest.log(Status.INFO, infoMessage);
        } catch (NullPointerException e){
            System.out.println("Extent report has not been initialized, outputting to console. Original INFO message: " + infoMessage);
        }
    }
    public void addWarningMessage(ExtentTest currentTest, String warningMessage){ //for exceptions
        try {
            if(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("printExtentReportMssgToConsole").equalsIgnoreCase("true")){
                System.out.println("EXCEPTION/WARNING: ".concat(warningMessage));
            }
            currentTest.log(Status.INFO, "EXCEPTION/WARNING: ".concat(warningMessage)); //cannot use actual Status.WARNING tag as that causes the test case to not show up as pass
        } catch (NullPointerException e){
            System.out.println("Extent report has not been initialized, outputting to console. Original EXCEPTION/WARNING message: " + warningMessage);
        }
    }
    public void addScreenShotBaseSixtyFourString(ExtentTest currentTest, String pathToScreenShot){
        currentTest.addScreenCaptureFromBase64String(pathToScreenShot);
    }
    public ExtentReports getExtent() {
        return extent;
    }
}
