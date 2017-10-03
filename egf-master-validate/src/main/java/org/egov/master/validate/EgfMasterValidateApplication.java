package org.egov.master.validate;

import org.egov.master.validate.domain.model.MasterConfig;
import org.egov.master.validate.domain.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EgfMasterValidateApplication {
    public static void main(String[] args) {
	SpringApplication.run(EgfMasterValidateApplication.class, args);
	EgfMasterValidateApplication app = new EgfMasterValidateApplication();
    }
}
