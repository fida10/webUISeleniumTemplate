package com.qa.pages;

import com.aventstack.extentreports.ExtentTest;
import com.qa.appUtilFiles.ActionExecutor;
import com.qa.appUtilFiles.ExceptionHandling;
import com.qa.utilFiles.coreCodeUtilFiles.ExtentReportGenerator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.cucumber.java.Scenario;
import org.testng.Assert;

//TEMPLATE PAGE, NOT AN ACTUAL PAGE OBJECT CLASS. JUST A SAMPLE

public class somePageTemplate {
	private final WebDriver driver;
	private final ExtentReportGenerator extentReportGenerator;
	private final ExtentTest currentTest;
	private final Scenario scenario;

	private final ExceptionHandling exceptionHandling;
	private final ActionExecutor actionExecutor;

	@FindBy(xpath = "//")
	WebElement sampleElement;
	String sampleElementXPath = "//";

	public somePageTemplate(WebDriver driver, ExtentTest currentTestFromInitDrPgsAndUtilsClass, ExtentReportGenerator extentReportGeneratorFromInitDrPgsAndUtilsClass, Scenario scenario){
		this.driver = driver; //sets the driver object on this class to the same as the driver option passed into params when initializing this class.
		PageFactory.initElements(driver, this); //initializes these elements
		currentTest = currentTestFromInitDrPgsAndUtilsClass;
		extentReportGenerator = extentReportGeneratorFromInitDrPgsAndUtilsClass;
		this.scenario = scenario;

		exceptionHandling = new ExceptionHandling(currentTestFromInitDrPgsAndUtilsClass, extentReportGeneratorFromInitDrPgsAndUtilsClass);
		actionExecutor = new ActionExecutor(driver, currentTestFromInitDrPgsAndUtilsClass, extentReportGeneratorFromInitDrPgsAndUtilsClass);
	}

	public WebElement getSampleElement() {
		return sampleElement;
	}

	public String getSampleElementXPath() {
		return sampleElementXPath;
	}

	//validation methods begin
	public void validatePageIsOpen(){
		Assert.assertNotNull(exceptionHandling.combinedStaleAndIsElementDisplayedHandling(driver, sampleElementXPath, 0));
	}
	//validation methods end

	//flows related to this page begin
}
