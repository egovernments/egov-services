package org.egov.pt;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov.pt", "org.egov.pt.web.controllers" , "org.egov.pt.config","org.egov.pt.repository"})
public class PropertyApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(PropertyApplication.class, args);
    }

}
