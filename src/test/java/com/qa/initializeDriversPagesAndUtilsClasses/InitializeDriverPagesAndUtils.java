//for use only in step defs. Do not reference this or its objects on page classes

package com.qa.initializeDriversPagesAndUtilsClasses;

import com.aventstack.extentreports.ExtentTest;
import com.qa.pages.*;
import com.qa.utilFiles.coreCodeUtilFiles.ExcelReader;
import com.qa.utilFiles.coreCodeUtilFiles.ExtentReportGenerator;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class InitializeDriverPagesAndUtils {
    private WebDriver webDriver;

    private somePageTemplate somePageTemplate;

    private ExcelReader excelReader;
    //ActionExecutor and ExceptionHandling utilities objects are created and used within classes themselves
    public Scenario scenario;

    //extent report objects
    private ExtentTest currentTestInitDrPgsAndUtils;
    private ExtentReportGenerator extentReportGeneratorInitDrPgsAndUtils;

    //will be using field approach for reporting (reporting objects will be fields and defined in constructors in all below classes)
    public void initializeDriverObject(WebDriver webDriver){
        this.webDriver = webDriver;
    }
    public void initializeAllPagesAndUtils(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator, Scenario scenario){
        //initializing page object classes
        somePageTemplate = new somePageTemplate(webDriver, currentTest, extentReportGenerator, scenario);

        //initializes the excelReader
        excelReader = new ExcelReader(currentTest, extentReportGenerator);

        //utils
        this.scenario = scenario;

        //extent reporting objects
        currentTestInitDrPgsAndUtils = currentTest;
        extentReportGeneratorInitDrPgsAndUtils = extentReportGenerator;
    }
    //getDrivers
    public WebDriver getWebDriver(){
        return webDriver;
    }
    //getPages
    public somePageTemplate getSomePageTemplate() {
        return somePageTemplate;
    }

    //getUtilities
    public ExcelReader getExcelReader() {
        return excelReader;
    }
    public Scenario getScenario(){return scenario;}
    //getExtentReportObjects
    public ExtentTest getCurrentTestInitDrPgsAndUtils() {
        return currentTestInitDrPgsAndUtils;
    }
    public ExtentReportGenerator getExtentReportGeneratorInitDrPgsAndUtils() {
        return extentReportGeneratorInitDrPgsAndUtils;
    }
}
