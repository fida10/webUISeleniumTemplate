package com.qa.testNG.scratch;

import com.qa.pages.android.AnimatedHomePage;
import com.qa.pages.android.LoginPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class ToInherit {
	//load up the appium driver + the required pages_android here, and have all testNG class files inherit them from here.
	protected AppiumDriver <MobileElement> testDriver;

	protected LoginPage loginPage;
	protected AnimatedHomePage animatedHomePage;
	//add any before and after methods here
	@BeforeMethod //Re-initializes these before each class
	public void setTestDriverWithPages(){ //initializes the test driver, and refreshes before each test (method)
		//setting testDriver
//		testDriver = Initializer.getAppiumDriver();

		//initializing page object classes
//		animatedHomePage = new AnimatedHomePage(testDriver);
//		loginPage = new LoginPage(testDriver);
	}
	@AfterMethod
	public void restartApp(){
		testDriver.resetApp(); /*resets the app after each test;
		previous tests data or state does not mess with new test.
		Since all tests must now be end to end, running tests in paralell should be far easier*/
	}
}
