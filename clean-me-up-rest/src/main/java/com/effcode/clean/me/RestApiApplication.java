package com.effcode.clean.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * REST API application runner, performs auto-scan of base package for annotation based application component discovery
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see SpringApplication
 * @since 1.0
 */
@SpringBootApplication(scanBasePackageClasses = {RestApiApplication.class})
public class RestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

}
