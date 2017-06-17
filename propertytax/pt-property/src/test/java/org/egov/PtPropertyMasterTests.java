/*package org.egov;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.Department;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.RequestInfo;
import org.egov.property.api.PropertyMasterController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;


@RunWith(SpringRunner.class)
@WebMvcTest(PropertyMasterController.class)
public class PtPropertyMasterTests {

@Autowired	
MockMvc mockMvc;

	  @Test
		public void createDepartmentTest(){
		  try{
		String url="http://localhost:8080/property/departments/_create";
		Department department=new Department();
		department.setName("test");
		department.setCode("test");
		department.setTenantId("test");
		department.setDescription("testing create department");
		department.setNameLocal("test");
		AuditDetails auditDetails=new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setCreatedTime(new Date().getTime());
		auditDetails.setLastModifiedTime(new Date().getTime());
		department.setAuditDetails(auditDetails);
		DepartmentRequest departmentRequest=new DepartmentRequest();
	    List<Department> departmentList=	new ArrayList<Department>();
		departmentRequest.setDepartments(departmentList);
		departmentRequest.setRequestInfo(new RequestInfo());
		Gson gson=new Gson();
		RestTemplate restTemplate=new RestTemplate();
	DepartmentResponseInfo departmentResponse=restTemplate.postForObject(url,gson.toJson(departmentRequest),DepartmentResponseInfo.class);
		assertTh
			  
			  Agreement agreement = new Agreement();
				agreement.setTenantId("1");
				agreement.setAction(Action.CREATE);
				agreement.setAcknowledgementNumber("ack");
				ResponseInfo responseInfo = new ResponseInfo();
				when(agreementService.createAgreement(any(AgreementRequest.class))).thenReturn(agreement); 
				when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(responseInfo);
			       
			        mockMvc.perform(post("/agreements/_create")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(getFileContents("agreementrequest.json")))
			                .andExpect(status().isCreated())
			                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			                .andExpect(content().json(getFileContents("agreementsearchresponse.json")));
		  }catch(Exception ex){
			  
		  }
	}
	  
	  private String getFileContents(String fileName) throws IOException {
			return new FileUtils().getFileContents(fileName);
		}
}
*/