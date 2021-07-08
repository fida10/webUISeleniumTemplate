package com.qa.utilFiles.coreCodeUtilFiles;

import com.qa.coreFrameworkFiles.Initializer;

public class GetPropertiesFromSysOrConfig {
	public String getPropertyFromSysOrConfig(String propertyToGet){
		String propertyValue = System.getProperty(propertyToGet);
		if(propertyValue == null){
			propertyValue = Initializer.getPro().getProperty(propertyToGet);
		}
		return propertyValue;
	}
}
