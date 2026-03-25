package com.robot.cairo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @ComponentScan is already implied by @SpringBootApplication; no need to repeat it.
@SpringBootApplication
public class CairoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CairoApplication.class, args);
    }
}
