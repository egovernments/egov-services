package org.egov.mr.web.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.egov.mr.TestConfiguration;
import org.egov.mr.service.ServiceConfigurationService;
import org.egov.mr.web.errorhandler.ErrorHandler;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceConfigurationController.class)
@Import(TestConfiguration.class)
public class ServiceConfigurationControllerTest {

	@MockBean
	private ErrorHandler errorHandler;

	@MockBean
	private ServiceConfigurationService serviceConfigurationService;

	@Test
	public void test() {

	}

}
