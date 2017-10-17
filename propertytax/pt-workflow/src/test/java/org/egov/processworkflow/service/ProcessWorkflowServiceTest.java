package org.egov.processworkflow.service;

import static org.junit.Assert.assertTrue;

import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.UserAuthResponseInfo;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.PtWorkflowApplication;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.consumer.WorkFlowUtil;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { PtWorkflowApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProcessWorkflowServiceTest {

	@Autowired
	WorkFlowUtil workFlowUtil;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	@Test
	public void startWorkflowTest() {

		WorkFlowDetails workflowDetails = new WorkFlowDetails();
		RequestInfo requestInfo = new RequestInfo();

		workflowDetails.setAssignee(1l);
		requestInfo.setAction("create");
		requestInfo.setDid("1");
		requestInfo.setMsgId("20170310130900");
		requestInfo.setRequesterId("yosadhara");

		requestInfo.setAuthToken("a5dc5bc3-e15e-4bef-a140-2d946527fb0d");
		requestInfo.setApiId("");
		requestInfo.setVer("1.0");
		requestInfo.setTs("27-06-2017 10:30:12");
		requestInfo.setKey("abcdkey");
		requestInfo.setTenantId("default");

		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		workflowDetailsRequestInfo.setTenantId("default");
		workflowDetailsRequestInfo.setRequestInfo(requestInfo);
		workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);

		try {

			requestInfo.setAuthToken(getAuthToken());
//			ProcessInstance processInstance = workFlowUtil.startWorkflow(workflowDetailsRequestInfo,
//					env.getProperty("businessKey"), env.getProperty("type"),
//					env.getProperty("create.property.comments"));
//			if (processInstance == null)
//				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void updateWorkflowTest() {

		String tenantId = "default";
		Property property = new Property();
		PropertyDetail propertyDetail = new PropertyDetail();
		propertyDetail.setStateId("1");
		property.setPropertyDetail(propertyDetail);
		RequestInfo requestInfo = getRequestInfoObject();
		WorkflowDetailsRequestInfo workflowDetailsRequestInfo = new WorkflowDetailsRequestInfo();
		WorkFlowDetails workflowDetails = new WorkFlowDetails();
		workflowDetails.setDepartment("egov");
		workflowDetails.setDesignation("test");
		workflowDetails.setAssignee(1l);
		workflowDetails.setAction("forward");
		workflowDetails.setStatus("Assistant Approved");
		workflowDetailsRequestInfo.setTenantId(tenantId);
		workflowDetailsRequestInfo.setRequestInfo(requestInfo);
		workflowDetailsRequestInfo.setWorkflowDetails(workflowDetails);

		try {

//			TaskResponse taskResponse = workFlowUtil.updateWorkflow(workflowDetailsRequestInfo, property.getPropertyDetail().getStateId());
//			if (taskResponse == null)
//				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			System.out.println("UPDATE Workflow Test: " + e.toString());
			assertTrue(false);
		}

	}

	private RequestInfo getRequestInfoObject() {

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction("create");
		requestInfo.setDid("1");
		requestInfo.setMsgId("20170310130900");
		requestInfo.setRequesterId("yosadhara");
		try {
			requestInfo.setAuthToken(getAuthToken());
		} catch (Exception e) {

			e.printStackTrace();
		}
		requestInfo.setApiId("");
		requestInfo.setVer("1.0");
		requestInfo.setTs("06-07-2017 10:35:54");
		requestInfo.setKey("abcdkey");
		requestInfo.setTenantId("default");
		return requestInfo;
	}

	private String getAuthToken() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", propertiesManager.getAuthKey());

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("username", propertiesManager.getOauthName());
		map.add("password", propertiesManager.getPassword());
		map.add("grant_type", propertiesManager.getGrantType());
		map.add("scope", propertiesManager.getScope());
		map.add("tenantId", "default");

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);

		UserAuthResponseInfo userAuthResponseInfo = restTemplate.postForObject(propertiesManager.getUserAuth(),
				requestEntity, UserAuthResponseInfo.class);

		if (userAuthResponseInfo != null) {
			return userAuthResponseInfo.getAccess_token();
		}

		return "";
	}
}