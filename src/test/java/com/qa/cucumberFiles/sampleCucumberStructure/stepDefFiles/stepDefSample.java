package com.qa.cucumberFiles.sampleCucumberStructure.stepDefFiles;

import com.qa.initializeDriversPagesAndUtilsClasses.InitializeDriverPagesAndUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class stepDefSample {

	InitializeDriverPagesAndUtils initDrPgsAndUtils;

	public stepDefSample(InitializeDriverPagesAndUtils initDrPgsAndUtils) {
		this.initDrPgsAndUtils = initDrPgsAndUtils;
	}
	@Given("A step with param {string}")
	public void aStepWithParam(String param){
		//steps
	}

	@When("A step happens with param {string}")
	public void aStepHappensWithParam(String param){
		//steps
	}

	@Then("Another step happens with param {string}")
	public void anotherStepHappensWithParam(String param){
		//steps
	}
}