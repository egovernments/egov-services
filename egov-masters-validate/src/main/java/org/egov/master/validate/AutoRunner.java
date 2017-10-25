package org.egov.master.validate;

import java.io.IOException;

import org.egov.master.validate.domain.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class AutoRunner implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ValidateService validateService;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
	try {
		validateService.validate();
	} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}