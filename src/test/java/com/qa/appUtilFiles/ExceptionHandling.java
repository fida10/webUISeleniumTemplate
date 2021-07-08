package com.qa.appUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.utilFiles.coreCodeUtilFiles.ExtentReportGenerator;
import com.qa.utilFiles.coreCodeUtilFiles.GetPropertiesFromSysOrConfig;
import org.openqa.selenium.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExceptionHandling {
	private final ExtentReportGenerator extentReportGeneratorForExepHandling;
	private final ExtentTest currentTestForExcepHandling;
	public ExceptionHandling(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		extentReportGeneratorForExepHandling = extentReportGenerator;
		currentTestForExcepHandling = currentTest;
	}
	public boolean isDisplayedNoSuchElementExcp(WebElement webElement){ //having this workflow done without throwing this exception will significantly increase speed of program
		boolean isElementDisplayed;
		try{
			if(webElement == null){
				isElementDisplayed = false;
			} else {
				isElementDisplayed = webElement.isDisplayed();
			}
			System.out.println(isElementDisplayed);
		} catch (NoSuchElementException e){
			extentReportGeneratorForExepHandling.addWarningMessage(currentTestForExcepHandling, "Element is not displayed, returning false. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
			isElementDisplayed = false;
		}
		return isElementDisplayed;
	}
	public WebElement combinedStaleAndIsElementDisplayedHandling(WebDriver driver, String xPathWebElement, int timeSecondsToWait_ZeroForDefault){ //combined staleElement + noSuchElement handling, for android. Will return a processable element. Method can be used for assertions, just check if the returned webElement is null or not.
		int timeToWaitForElement;
		int defaultImplicitWaitTime = Integer.parseInt(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("implicitWaitTimer"));
		if(timeSecondsToWait_ZeroForDefault == 0){
			extentReportGeneratorForExepHandling.addInfoMessage(currentTestForExcepHandling, "Param for wait time for combinedStaleAndIsElementDisplayedHandling set as '0', will use default value in config.properties: " + defaultImplicitWaitTime);
			timeToWaitForElement = defaultImplicitWaitTime;
		} else {
			timeToWaitForElement = timeSecondsToWait_ZeroForDefault;
			extentReportGeneratorForExepHandling.addInfoMessage(currentTestForExcepHandling, "Param for wait time for combinedStaleAndIsElementDisplayedHandling set as: " + timeSecondsToWait_ZeroForDefault + " as per provided args.");
		} //if param timeSecondsToWait_ZeroForDefault set to 0, will default to value set in properties.
		extentReportGeneratorForExepHandling.addInfoMessage(currentTestForExcepHandling, "Checking element staleness + visibility, using XPath: " + xPathWebElement);

		extentReportGeneratorForExepHandling.addInfoMessage(currentTestForExcepHandling, "Changed implicit wait timer to 1 second for combinedStaleAndIsElementDisplayedHandling");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS); //checks for element up to one second

		boolean isElementStaleAndNotVisible = true;
		WebElement webElementUnderTest = null;

		Instant start = Instant.now();
		while(isElementStaleAndNotVisible) {
			try {
				webElementUnderTest = driver.findElement(By.xpath(xPathWebElement));
				extentReportGeneratorForExepHandling.addInfoMessage(currentTestForExcepHandling, "Trying element with location: " + webElementUnderTest.getLocation() + " || Is this element displayed: " + webElementUnderTest.isDisplayed());
				if(webElementUnderTest.isDisplayed()) {
					extentReportGeneratorForExepHandling.addInfoMessage(currentTestForExcepHandling, "Element being checked for staleness verified as not stale, and is visible! Ending. Element: " + webElementUnderTest);
					isElementStaleAndNotVisible = false;
				}
			} catch (StaleElementReferenceException e) {
				extentReportGeneratorForExepHandling.addWarningMessage(currentTestForExcepHandling, "combinedStaleAndIsElementDisplayedHandling: this element being checked for staleness " + webElementUnderTest + " is stale. Attempting to reload the element. Stacktrace: " + Arrays.toString(e.getStackTrace())
				); //extentReports
				//as the code in the try catch block will reload the element anyway, no need to reload the whole page as before.
			} catch (NoSuchElementException e) {
				System.out.println("combinedStaleAndIsElementDisplayedHandling: this element being checked for staleness " + webElementUnderTest + " is not visible. Will wait specified time for element to be visible.");
				extentReportGeneratorForExepHandling.addWarningMessage(currentTestForExcepHandling, "combinedStaleAndIsElementDisplayedHandling: this element being checked for staleness " + webElementUnderTest + " is not visible. Will wait specified time for element to be visible.");
			}
			Instant end = Instant.now();
			long secondsPassed = Duration.between(start, end).getSeconds();
			if(secondsPassed > timeToWaitForElement && isElementStaleAndNotVisible){
				extentReportGeneratorForExepHandling.addWarningMessage(currentTestForExcepHandling, "combinedStaleAndIsElementDisplayedHandling: Timer --> " + timeToWaitForElement + " seconds has elapsed and element is either still stale or not visible. See log messages (above) for details.");
				break;
			}
		}
		driver.manage().timeouts().implicitlyWait(defaultImplicitWaitTime, TimeUnit.SECONDS);
		extentReportGeneratorForExepHandling.addInfoMessage(currentTestForExcepHandling, "Changed implicit wait timer back to: " + defaultImplicitWaitTime + " seconds for combinedStaleAndIsElementDisplayedHandling");
		return webElementUnderTest;
	}
}
