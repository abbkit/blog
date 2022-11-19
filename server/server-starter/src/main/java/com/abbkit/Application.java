package com.abbkit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
//@Import({ SomeConfiguration.class, AnotherConfiguration.class })
@EnableScheduling
@ComponentScan("com.abbkit")
@MapperScan("com.abbkit.project.*.*.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
