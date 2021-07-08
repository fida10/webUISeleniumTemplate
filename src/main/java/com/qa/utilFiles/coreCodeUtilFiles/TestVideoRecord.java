package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions.VideoQuality;
import io.appium.java_client.screenrecording.CanRecordScreen;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;

public class TestVideoRecord {
	//MODIFICATIONS REQUIRED TO WORK ON BROWSER. DISABLE FOR NOW.
	private final String pathToTestVideoRecord = new PathGeneration().returnUnifiedPathToLogs_Reports("testVideoRecords" + File.separator);
	private final boolean ffmpegVidRecordingActive = Boolean.parseBoolean(new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("ffmpegVidRecordingActive"));
	public void createNewRecording(AppiumDriver<MobileElement> appiumDriver, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		if(ffmpegVidRecordingActive) {
			String currentPlatform = new GetPropertiesFromSysOrConfig().getPropertyFromSysOrConfig("platform");
			if (currentPlatform.equalsIgnoreCase("android")) {
				((CanRecordScreen) appiumDriver).startRecordingScreen(new AndroidStartScreenRecordingOptions().enableBugReport().withTimeLimit(Duration.ofMinutes(20)));
			} else if (currentPlatform.equalsIgnoreCase("IOS")) {
				((CanRecordScreen) appiumDriver).startRecordingScreen(new IOSStartScreenRecordingOptions().withTimeLimit(Duration.ofMinutes(20)).withVideoQuality(VideoQuality.MEDIUM)); //.withVideoScale("1280:720") reduces resolution
			}
			extentReportGenerator.addInfoMessage(currentTest, "Test recording started successfully"); //extentReports
		}
	}
	public void endAndProcessCurrentRecording(String passFailSkip, String featureFileId, AppiumDriver<MobileElement> appiumDriver, ExtentTest currentTest, ExtentReportGenerator extentReportGenerator) {
		if (ffmpegVidRecordingActive) {
			String base64String = ((CanRecordScreen) appiumDriver).stopRecordingScreen();
			byte[] data = Base64.decodeBase64(base64String);
			String destinationPath = pathToTestVideoRecord.concat(passFailSkip).concat(File.separator);
			boolean testVideoRecordMade = new File(destinationPath).mkdirs();
			extentReportGenerator.addInfoMessage(currentTest, "Was the Test Video Recording directory made successfully: " + testVideoRecordMade); //extentReports
			Path path = Paths.get(destinationPath.concat(featureFileId).concat(".mpeg4"));
			try {
				Files.write(path, data);
				extentReportGenerator.addInfoMessage(currentTest, "Test Recording created successfully"); //extentReports
			} catch (IOException e) {
				extentReportGenerator.addWarningMessage(currentTest, "IO Exception thrown, file path to Test Video Recordings Invalid. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
			}
		}
	}
}
