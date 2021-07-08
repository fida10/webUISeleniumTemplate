package com.qa.utilFiles.coreCodeUtilFiles;

import com.aventstack.extentreports.ExtentTest;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class GitAutoCommands {
	private Git currentRepo;
	private final ExtentReportGenerator extentReportGeneratorForGitCommands;
	private final ExtentTest currentTestForGitCommands;
//using field approach for reporting
	public GitAutoCommands(ExtentTest currentTest, ExtentReportGenerator extentReportGenerator) {
		currentTestForGitCommands = currentTest;
		extentReportGeneratorForGitCommands = extentReportGenerator;
		/*
		* Add code here to see if this is a git repository
		* If not, initialize it as one.
		* */
		try {
			currentRepo = Git.open(new File(PathGeneration.returnAbsPath()));
		} catch (IOException e){
			extentReportGeneratorForGitCommands.addWarningMessage(currentTestForGitCommands, "IO Exception, error with git repo. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
	}
	public void gitAutoCommit(){
		try {
			FileWriter testRunTrackerWriter = new FileWriter(PathGeneration.returnAbsPath().concat("src" + File.separator + "test" + File.separator + "projectTestResources" + File.separator + "runTracker" + File.separator + "runTrackerDoNotDelete.log"));
			testRunTrackerWriter.write("Last attempted test run start on: " + LocalDateTime.now() + "\n");
			testRunTrackerWriter.close();
		} catch (IOException e){
			extentReportGeneratorForGitCommands.addWarningMessage(currentTestForGitCommands, "IO Exception thrown, unable to write test run start message for gitAutoCommit. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
		try {
			currentRepo.add().addFilepattern("src").call();
			currentRepo.add().addFilepattern("pom.xml").call();

			currentRepo.commit().setMessage("Fail-safe commit before test execution, commit done on: " + LocalDateTime.now()).call();
			extentReportGeneratorForGitCommands.addInfoMessage(currentTestForGitCommands, "Fail-safe auto commit done successfully to local"); //extentReports
		} catch (GitAPIException e){
			extentReportGeneratorForGitCommands.addWarningMessage(currentTestForGitCommands, "GIT API Exception thrown, unable to add/commit src folder and/or pom.xml. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
	}
	public void gitAutoHardReset(){
		try {
			currentRepo.reset().setMode(ResetType.HARD).call();
			extentReportGeneratorForGitCommands.addInfoMessage(currentTestForGitCommands, "Reversion to pre-argument feature files complete"); //extentReports
		} catch (GitAPIException e){
			extentReportGeneratorForGitCommands.addWarningMessage(currentTestForGitCommands, "GIT API Exception thrown, unable to do a hard reset. Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
	}
	public void gitAddAndCommitFileOrFolder(String pathOfFileToAddOrRM) {
		try {
			currentRepo.add().addFilepattern(pathOfFileToAddOrRM).call();
			extentReportGeneratorForGitCommands.addInfoMessage(currentTestForGitCommands, "Added file/folder from index: " + currentRepo.add().addFilepattern(pathOfFileToAddOrRM).call()); //extentReports
			currentRepo.commit().setMessage("Added/Updated the following file/folder: " + pathOfFileToAddOrRM).call();
		} catch (GitAPIException e){
			extentReportGeneratorForGitCommands.addWarningMessage(currentTestForGitCommands, "GIT API Exception thrown, unable to add/commit file or folder at: " + pathOfFileToAddOrRM + " || Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
	}
	public void gitRMFileOrFolder(String pathOfFileToRM){
		try {
			currentRepo.rm().setCached(true).addFilepattern(pathOfFileToRM).call();
			extentReportGeneratorForGitCommands.addInfoMessage(currentTestForGitCommands, "Removed file/folder from index: " + pathOfFileToRM); //extentReports
		} catch (GitAPIException e){
			extentReportGeneratorForGitCommands.addWarningMessage(currentTestForGitCommands, "GIT API Exception thrown, unable to remove file or folder at: " + pathOfFileToRM + " || Stacktrace: " + Arrays.toString(e.getStackTrace())); //extentReports
		}
	}
}
