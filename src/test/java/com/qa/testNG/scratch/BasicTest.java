package com.qa.testNG.scratch;

import org.testng.annotations.Test;

public class BasicTest {
	@Test
	public void helloWorld() throws InterruptedException{
		System.out.println("Hello world");
		Thread.sleep(10000);
	}

}
