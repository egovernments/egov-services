package org.lcms.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.egov.EgovLcmsApplication;
import org.egov.lcms.service.OpinionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = { EgovLcmsApplication.class })
public class OpinionControllerTest {

	@MockBean
	private OpinionService opinionService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testShouldCreateMasterFloorType() {
	
	}
	
	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}
}
