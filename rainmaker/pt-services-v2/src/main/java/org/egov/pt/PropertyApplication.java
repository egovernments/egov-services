package org.egov.pt;


import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov.pt", "org.egov.pt.web.controllers" , "org.egov.pt.config","org.egov.pt.repository"})
@Import({ TracerConfiguration.class })
public class PropertyApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(PropertyApplication.class, args);
    }

}
