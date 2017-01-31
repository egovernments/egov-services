package org.egov.pgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class PgrPersistenceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PgrPersistenceApplication.class, args);
    }
}