package org.egov.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EgovHRPersistApplication extends Thread {
    public static void main(final String[] args) {
        SpringApplication.run(EgovHRPersistApplication.class, args);
    }
}
