package com.qa.appUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.utilFiles.coreCodeUtilFiles.ExtentReportGenerator;
import com.qa.utilFiles.coreCodeUtilFiles.GetPropertiesFromSysOrConfig;
import com.qa.utilFiles.coreCodeUtilFiles.ModifyWaits;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.*;

import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.Instant;

public class ActionExecutor { //emulate various user actions, thru touch actions
	private final WebDriver webDriver; //for generic driver actions

	private final ExtentReportGenerator extentReportGeneratorForActionExec;
	private final ExtentTest currentTestForActionExec;
	private final int scrollTime = Integer.parseInt(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("scrollTimeAndroid"));
	public ActionExecutor(WebDriver webDriver, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		extentReportGeneratorForActionExec = extentReportGenerator;
		currentTestForActionExec = currentTest;

		this.webDriver = webDriver;
	}
	public void singleScrollDown(){

	}
	public void singleScrollUp(){

	}
	public void scrollToElement(WebDriver webDriver, WebElement elementToScrollTo, ExceptionHandling exceptionHandling) { //scrolls down
		//note that this scroll does not handle stales! scrollToElementViaDynamicXPath does, use that if stale element handling needed, else do validations first.
		ModifyWaits.modifyImplicitWaitTimer(webDriver, 1);
		Instant start = Instant.now();
		boolean isElementDisplayed = exceptionHandling.isDisplayedNoSuchElementExcp(elementToScrollTo);
		JavascriptExecutor javascriptExec = (JavascriptExecutor) webDriver;
		while (!isElementDisplayed) {
			extentReportGeneratorForActionExec.addInfoMessage(currentTestForActionExec, "Element is not visible. Attempting to scroll to it.");
			javascriptExec.executeScript("arguments[0].scrollIntoView();", elementToScrollTo);
			isElementDisplayed = exceptionHandling.isDisplayedNoSuchElementExcp(elementToScrollTo);
			extentReportGeneratorForActionExec.addInfoMessage(currentTestForActionExec, "Element found from scrolling? " + isElementDisplayed);

			Instant end = Instant.now();
			long secondsPassed = Duration.between(start, end).getSeconds();
			if (secondsPassed > scrollTime) {
				extentReportGeneratorForActionExec.addWarningMessage(currentTestForActionExec, "Scroll timer has elapsed and desired element is not available. Failing test.");
				Assert.fail("Scroll timer has elapsed and desired element: " + elementToScrollTo + " is not available. Failing test.");
				break;
			}
		}
		ModifyWaits.setOriginalImplicitWaitTimer(webDriver);
	}
	public void scrollToElementViaDynamicXPath(WebDriver webDriver, String elementXpath, ExceptionHandling exceptionHandling){ //scrolls down
		/*
		 * This method scrolls to an element, when that element has just been defined but is not visible.
		 * Usually for when xpath is dynamic and needs to be generated dynamically before being defined, but the elements is not yet visible and needs to be scrolled to.
		 * Stale element handling is integrated before scroll start.
		 * Best not to use this if element already exists on the page and need to scroll to it as this may cause the element to mistakenly show up as visible; in this case, use regular scrollToElement */
		boolean isElementDisplayed = exceptionHandling.combinedStaleAndIsElementDisplayedHandling(webDriver, elementXpath, 1) != null;
		JavascriptExecutor javascriptExec = (JavascriptExecutor) webDriver;
		Instant start = Instant.now();
		while (!isElementDisplayed) {
			extentReportGeneratorForActionExec.addInfoMessage(currentTestForActionExec, "Element is not visible. Attempting to scroll to it.");
			javascriptExec.executeScript("arguments[0].scrollIntoView();", webDriver.findElement(By.xpath(elementXpath)));
			isElementDisplayed = exceptionHandling.combinedStaleAndIsElementDisplayedHandling(webDriver, elementXpath, 1) != null;
			extentReportGeneratorForActionExec.addInfoMessage(currentTestForActionExec, "Element found from scrolling? " + isElementDisplayed);

			Instant end = Instant.now();
			long secondsPassed = Duration.between(start, end).getSeconds();
			if(secondsPassed > scrollTime){
				extentReportGeneratorForActionExec.addWarningMessage(currentTestForActionExec, "Scroll timer has elapsed and desired element is not available. Failing test.");
				Assert.fail("Scroll timer has elapsed and desired element: " + elementXpath +" is not available. Failing test.");
				break;
			}
		}
	}
	public void waitFiveSeconds(){
		try{
			Thread.sleep(5000);
		} catch (InterruptedException e){
			System.out.println("Interrupted exception thrown, unable to pause for 5 seconds");
		}
	}
	public void waitOnePointFiveSeconds(){
		try{
			Thread.sleep(1500);
		} catch (InterruptedException e){
			System.out.println("Interrupted exception thrown, unable to pause for 1.5 seconds");
		}
	}
	public String getText(MobileElement e) {
		String txt;
		txt = e.getAttribute("text");
		return txt;
	}
	public void reinforcedClick(String xpathOfElementToClick, String xpathOfBreakingElement, WebDriver driverObject, ExceptionHandling exceptionHandlingObject){ //another version may be needed, where the breaking element is something that appears on the same page as the clicked element; if it does not appear after click, then the click was successful.
		int timeToWaitForReinforcedClick = Integer.parseInt(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("timeToWaitForReinforcedClick"));
		int timeToWaitForBreakingElementReinforcedClick = Integer.parseInt(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("timeToWaitForBreakingElementReinforcedClick"));
		Assert.assertNotNull(exceptionHandlingObject.combinedStaleAndIsElementDisplayedHandling(driverObject, xpathOfElementToClick, 1));
		Instant start = Instant.now();
		while(exceptionHandlingObject.combinedStaleAndIsElementDisplayedHandling(driverObject, xpathOfElementToClick, 1) != null) {
			driverObject.findElement(By.xpath(xpathOfElementToClick)).click();
			if (exceptionHandlingObject.combinedStaleAndIsElementDisplayedHandling(driverObject, xpathOfBreakingElement, timeToWaitForBreakingElementReinforcedClick) != null) { //breaking element is an element that should be visible after clicking, but not before. Seeing this element will confirm that the click was successful, breaking the loop.
				break;
			}
			Instant end = Instant.now();
			long secondsPassed = Duration.between(start, end).getSeconds();
			if(secondsPassed > timeToWaitForReinforcedClick){
				extentReportGeneratorForActionExec.addWarningMessage(currentTestForActionExec, "Reinforced click timer has elapsed and click was still not successful. Failing test.");
				Assert.fail("Reinforced click timer has elapsed and click was still not successful. Failing test.");
				break;
			}
		}
		Assert.assertNotNull(exceptionHandlingObject.combinedStaleAndIsElementDisplayedHandling(driverObject, xpathOfBreakingElement, 1));
	}
	public void assertText(MobileElement e, String text){
		Assert.assertTrue(getText(e).equalsIgnoreCase(text));
	}

	/**
	 * Method to enter keyboard or Done in Native
	 */
	public void EnterKeyboard(){
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
