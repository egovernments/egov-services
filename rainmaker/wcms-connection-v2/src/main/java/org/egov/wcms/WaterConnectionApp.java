package org.egov.wcms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov", "org.egov.wcms", "org.egov.wcms.web.controllers" , "org.egov.wcms.config"})
public class WaterConnectionApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WaterConnectionApp.class, args);
    }

}
