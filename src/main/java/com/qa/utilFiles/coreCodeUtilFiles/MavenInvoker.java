package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class MavenInvoker {
	private final InvocationRequest request;
	private final Invoker invoker;

	private final ExtentReportGenerator extentReportGeneratorForMavenInvoker;
	private final ExtentTest currentTestForMavenInvoker;
	public MavenInvoker(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator){
		currentTestForMavenInvoker = currentTest;
		extentReportGeneratorForMavenInvoker = extentReportGenerator;
		request = new DefaultInvocationRequest().setPomFile(new File(PathGeneration.returnAbsPath().concat("pom.xml")));
		invoker = new DefaultInvoker();
	}
	public void mavenClean() {
		try {
		request.setGoals(Collections.singletonList("clean"));
		invoker.execute(request);
			extentReportGeneratorForMavenInvoker.addWarningMessage(currentTestForMavenInvoker, "Executing maven clean"); //extentReports
		} catch (MavenInvocationException e){
			extentReportGeneratorForMavenInvoker.addInfoMessage(currentTestForMavenInvoker, "Maven exception thrown, please check command. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
	}
	public void mavenTest(){
		request.setGoals(Collections.singletonList("test"));
	}
}
