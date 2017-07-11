package org.egov.eis.indexer.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.model.Designation;
import org.egov.eis.model.EmployeeType;
import org.egov.eis.model.Grade;
import org.egov.eis.model.Group;
import org.egov.eis.model.Position;
import org.egov.eis.model.RecruitmentMode;
import org.egov.eis.model.RecruitmentQuota;
import org.egov.eis.model.RecruitmentType;
import org.egov.eis.web.contract.DesignationResponse;
import org.egov.eis.web.contract.EmployeeTypeResponse;
import org.egov.eis.web.contract.GradeResponse;
import org.egov.eis.web.contract.GroupResponse;
import org.egov.eis.web.contract.PositionResponse;
import org.egov.eis.web.contract.RecruitmentModeResponse;
import org.egov.eis.web.contract.RecruitmentQuotaResponse;
import org.egov.eis.web.contract.RecruitmentTypeResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class HRMasterServiceTest {

	@Mock
	private PropertiesManager propertiesManager;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private HRMasterService hrMasterService;

	@Test
	public void testGetEmployeeType() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		EmployeeType employeeType = new EmployeeType().builder().id(100L).name("employee1").chartOfAccounts("employed")
				.tenantId("1").build();
		List<EmployeeType> employeeTypes = new ArrayList<>();
		employeeTypes.add(employeeType);
		EmployeeTypeResponse employeeTypeResponse = new EmployeeTypeResponse().builder().responseInfo(responseInfo)
				.employeeType(employeeTypes).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServiceEmployeeTypeSearchPath()).thenReturn("/employeetypes/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/hr-masters/employeetypes/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper),
				EmployeeTypeResponse.class)).thenReturn(employeeTypeResponse);

		EmployeeType insertedEmployeeType = hrMasterService.getEmployeeType(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedEmployeeType, employeeType);
	}

	@Test
	public void testGetRecruitmentMode() {
		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		RecruitmentMode recruitmentMode = new RecruitmentMode().builder().id(10L).name("rec").description("emp")
				.tenantId("1").build();
		List<RecruitmentMode> recruitmentModes = new ArrayList<>();
		recruitmentModes.add(recruitmentMode);
		RecruitmentModeResponse recruitmentModeResponse = new RecruitmentModeResponse().builder()
				.responseInfo(responseInfo).recruitmentMode(recruitmentModes).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServiceRecruitmentModeSearchPath()).thenReturn("/recruitmentmodes/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/hr-masters/recruitmentmodes/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper),
				RecruitmentModeResponse.class)).thenReturn(recruitmentModeResponse);

		RecruitmentMode insertedsRecruitmentMode = hrMasterService.getRecruitmentMode(100L, "1",
				new RequestInfoWrapper());
		assertEquals(insertedsRecruitmentMode, recruitmentMode);
	}

	@Test
	public void testGetRecruitmentQuota() {
		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		RecruitmentQuota recruitmentQuota = new RecruitmentQuota().builder().id(10L).name("rec").description("emp")
				.tenantId("1").build();
		List<RecruitmentQuota> recruitmentQuotas = new ArrayList<>();
		recruitmentQuotas.add(recruitmentQuota);
		RecruitmentQuotaResponse recruitmentQuotaResponse = new RecruitmentQuotaResponse().builder()
				.responseInfo(responseInfo).recruitmentQuota(recruitmentQuotas).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServiceRecruitmentQuotaSearchPath())
				.thenReturn("/recruitmentquotas/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/hr-masters/recruitmentquotas/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper),
				RecruitmentQuotaResponse.class)).thenReturn(recruitmentQuotaResponse);

		RecruitmentQuota insertedsRecruitmentQuota = hrMasterService.getRecruitmentQuota(100L, "1",
				new RequestInfoWrapper());
		assertEquals(insertedsRecruitmentQuota, recruitmentQuota);
	}

	@Test
	public void testGetRecruitmentType() {
		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		RecruitmentType recruitmentType = new RecruitmentType().builder().id(10L).name("rec").description("emp")
				.tenantId("1").build();
		List<RecruitmentType> recruitmentTypes = new ArrayList<>();
		recruitmentTypes.add(recruitmentType);
		RecruitmentTypeResponse recruitmentTypeResponse = new RecruitmentTypeResponse().builder()
				.responseInfo(responseInfo).recruitmentType(recruitmentTypes).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServiceRecruitmentTypeSearchPath()).thenReturn("/recruitmenttypes/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/hr-masters/recruitmenttypes/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper),
				RecruitmentTypeResponse.class)).thenReturn(recruitmentTypeResponse);

		RecruitmentType insertedsRecruitmentType = hrMasterService.getRecruitmentType(100L, "1",
				new RequestInfoWrapper());
		assertEquals(insertedsRecruitmentType, recruitmentType);
	}

	@Test
	public void testGetGrade() {
		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Grade grade = new Grade().builder().id(10L).name("b").description("b").orderNo(1).active(true).tenantId("1")
				.build();
		List<Grade> grades = new ArrayList<>();
		grades.add(grade);
		GradeResponse gradeResponse = new GradeResponse().builder().responseInfo(responseInfo).grade(grades).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServiceGradeSearchPath()).thenReturn("/grades/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/hr-masters/grades/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), GradeResponse.class))
				.thenReturn(gradeResponse);

		Grade insertedGrade = hrMasterService.getGrade(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedGrade, grade);
	}

	@Test
	public void testGetGroup() {
		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Group group = new Group().builder().id(10L).name("b").description("b").tenantId("1").build();
		List<Group> groups = new ArrayList<>();
		groups.add(group);
		GroupResponse groupResponse = new GroupResponse().builder().responseInfo(responseInfo).group(groups).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServiceGroupSearchPath()).thenReturn("/groups/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/hr-masters/groups/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), GroupResponse.class))
				.thenReturn(groupResponse);

		Group insertedGroup = hrMasterService.getGroup(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedGroup, group);
	}

	@Test
	public void testGetPosition() {
		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Position position = new Position().builder().id(10L).name("b").isPostOutsourced(false).active(true)
				.tenantId("1").build();
		List<Position> positions = new ArrayList<>();
		positions.add(position);
		PositionResponse positionResponse = new PositionResponse().builder().responseInfo(responseInfo)
				.position(positions).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServicePositionSearchPath()).thenReturn("/positions/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/hr-masters/positions/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), PositionResponse.class))
				.thenReturn(positionResponse);

		Position insertedPosition = hrMasterService.getPosition(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedPosition, position);
	}

	@Test
	public void testGetDesignation() {
		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Designation designation = new Designation().builder().id(10L).name("b").code("20").active(true).tenantId("1")
				.build();
		List<Designation> designations = new ArrayList<>();
		designations.add(designation);
		DesignationResponse designationResponse = new DesignationResponse().builder().responseInfo(responseInfo)
				.designation(designations).build();

		when(propertiesManager.getHrMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getHrMastersServiceBasepath()).thenReturn("/hr-masters");
		when(propertiesManager.getHrMastersServiceDesignationSearchPath()).thenReturn("/designations/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/hr-masters/designations/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), DesignationResponse.class))
				.thenReturn(designationResponse);

		Designation insertedDesignation = hrMasterService.getDesignation(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedDesignation, designation);
	}

	private Object getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RequestInfoWrapper> httpEntityRequest = new HttpEntity<RequestInfoWrapper>(requestInfoWrapper,
				headers);
		return httpEntityRequest;
	}
}
