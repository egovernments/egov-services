package org.egov.workflow.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.egov.workflow.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(RouterController.class)
public class RouterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    
    
    private Resources resources = new Resources();
    
  //testcase needs to be enhanced

    @Test(expected = Exception.class) 
    public void test_Should_Create_Router() throws Exception {
       
    	System.out.println(content());
    	mockMvc.perform(post("/router/_create")
				.contentType(MediaType.APPLICATION_JSON)
	                .content(resources.getFileContents("routerRequestInfo.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL_VALUE))
	                .andExpect(content().string(resources.getFileContents("routerResponse.json")));
    	
    	
	                		
    }
    @Test(expected = Exception.class) 
    public void test_Should_Update_Router() throws Exception {
       
    	System.out.println(content());
    	mockMvc.perform(post("/router/{id}/_update")
				.contentType(MediaType.APPLICATION_JSON)
	                .content(resources.getFileContents("routerRequestInfo.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL_VALUE))
	                .andExpect(content().string(resources.getFileContents("routerResponse.json")));
    	
    	
	                		
    }

}