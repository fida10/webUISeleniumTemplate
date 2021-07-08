package com.qa.coreFrameworkFiles;

import com.aventstack.extentreports.ExtentTest;
import com.qa.utilFiles.coreCodeUtilFiles.*;
import org.openqa.selenium.remote.DesiredCapabilities;

public class InitializerDesiredCaps {
	private final GetPropertiesFromSysOrConfig getPropsForInitDesiredCaps = new GetPropertiesFromSysOrConfig();
// UNKNOWN WHAT DESIRED CAPABILITIES CAN BE USED FOR BROWSER, OR IF THEY ARE EVEN NEEDED. WILL KEEP FOR NOW.
	protected DesiredCapabilities getCapabilitiesForChrome(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){ //local physical device support for android still pending
		DesiredCapabilities desCap = new DesiredCapabilities();
		//desired caps go here
		return desCap;
	}

	protected DesiredCapabilities getCapabilitiesForFirefox(String currentIOSSimulatorUDID, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator, Integer wdaLocalPort, String scenarioName, String testRunName, String appVersionBeingTested){ //local physical device support for iOS still pending
		DesiredCapabilities desCap = new DesiredCapabilities();
		//desired caps go here
		return desCap;
	}
}
