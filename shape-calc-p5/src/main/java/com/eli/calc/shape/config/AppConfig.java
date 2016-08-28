package com.eli.calc.shape.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages="com.eli.calc.shape")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Autowired
    private Environment environment; // to have access to application.properties

    @Bean(name="executor")
    ExecutorService getExecutor() {

        return Executors.newFixedThreadPool(
            Integer.parseInt(environment.getProperty("executor.threadpool.size"))
        );
    }

}