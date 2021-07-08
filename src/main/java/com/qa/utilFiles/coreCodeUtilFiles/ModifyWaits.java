package com.qa.utilFiles.coreCodeUtilFiles;

import com.qa.coreFrameworkFiles.Initializer;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class ModifyWaits {
	public static void modifyImplicitWaitTimer(WebDriver webDriver, long amountOfTimeToWaitSeconds){
		webDriver.manage().timeouts().implicitlyWait(amountOfTimeToWaitSeconds, TimeUnit.SECONDS);
	}
	public static void setOriginalImplicitWaitTimer(WebDriver webDriver){
		webDriver.manage().timeouts().implicitlyWait(Long.parseLong(Initializer.getPro().getProperty("implicitWaitTimer")), TimeUnit.SECONDS);
	}
	//above two methods work as a pair. Apply modifyImplicitWaitTimer, execute code, then apply setOriginalImplicitWaitTimer
}
