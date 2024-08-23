package com.mychallenge;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.CUCUMBER_PROPERTIES_FILE_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/mychallenge/steps")
@ConfigurationParameter(key = CUCUMBER_PROPERTIES_FILE_NAME, value = "junit-platform.yaml")
public class CucumberRunner {}
