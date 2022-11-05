package com.tec.dslunittests.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.dsl")
public class ApplicationConfiguration {
}