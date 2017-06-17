package org.egov;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.StructureClass;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.property.PtPropertyApplication;
import org.egov.property.api.PropertyMasterController;
import org.egov.property.services.Masterservice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PropertyMasterController.class)
@ContextConfiguration(classes={PtPropertyApplication.class})
public class PtPropertyApplicationTests {

	@MockBean
	private Masterservice masterservice;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void test_Should_Create_Master_Structure_Classe() throws Exception{
		
		StructureClassResponse structureClassResponse = new StructureClassResponse();
		List<StructureClass> structureClasses=new ArrayList<StructureClass>();
		StructureClass structureClass=new StructureClass();
		structureClass.setCode("123");
		structureClass.setName("structure");
		structureClass.setTenantId("default");
		structureClass.setAuditDetails(new AuditDetails());
		structureClasses.add(structureClass);
		
		structureClassResponse.setResponseInfo(new ResponseInfo());
		structureClassResponse.setStructureClasses(structureClasses);
		when(masterservice.craeateStructureClassMaster(any(String.class), any(StructureClassRequest.class))).thenReturn(structureClassResponse); 
		
	
		mockMvc.perform(post("/property/structureclasses/_create")
        		.param("tenantId","default")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("structureclasscreaterequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("structureclasscreateresponse.json")));
	}
	
	

	 private String getFileContents(String fileName) throws IOException {
		 ClassLoader classLoader = getClass().getClassLoader();
			return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
		}
	 
	 private RequestInfo getRequestInfoObject(){
		 RequestInfo requestInfo = new RequestInfo();
		 requestInfo.setApiId("emp");
		 requestInfo.setVer("1.0");
		 requestInfo.setTs(new Long(122366));
		 requestInfo.setDid("1");
		 requestInfo.setKey("abcdkey");
		 requestInfo.setMsgId("20170310130900");
		 requestInfo.setRequesterId("rajesh");
		 requestInfo.setAuthToken("b5da31a4-b400-4d6e-aa46-9ebf33cce933");

		 return requestInfo;
		 }
}
