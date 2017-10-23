package org.egov.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.enums.ApplicationEnum;
import org.egov.models.Apartment;
import org.egov.models.ApartmentRequest;
import org.egov.models.ApartmentResponse;
import org.egov.models.ApartmentSearchCriteria;
import org.egov.models.AppConfiguration;
import org.egov.models.AppConfigurationRequest;
import org.egov.models.AppConfigurationResponse;
import org.egov.models.AppConfigurationSearchCriteria;
import org.egov.models.AuditDetails;
import org.egov.models.DemolitionReason;
import org.egov.models.DemolitionReasonRequest;
import org.egov.models.DemolitionReasonResponse;
import org.egov.models.DemolitionReasonSearchCriteria;
import org.egov.models.Department;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.DepartmentSearchCriteria;
import org.egov.models.Depreciation;
import org.egov.models.DepreciationRequest;
import org.egov.models.DepreciationResponse;
import org.egov.models.DepreciationSearchCriteria;
import org.egov.models.DocumentType;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.DocumentTypeSearchCriteria;
import org.egov.models.FloorType;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.FloorTypeSearchCriteria;
import org.egov.models.GuidanceValueBoundary;
import org.egov.models.GuidanceValueBoundaryRequest;
import org.egov.models.GuidanceValueBoundaryResponse;
import org.egov.models.GuidanceValueBoundarySearchCriteria;
import org.egov.models.MutationMaster;
import org.egov.models.MutationMasterRequest;
import org.egov.models.MutationMasterResponse;
import org.egov.models.MutationMasterSearchCriteria;
import org.egov.models.OccuapancyMaster;
import org.egov.models.OccuapancyMasterRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.OccuapancyMasterSearchCriteria;
import org.egov.models.PropertyType;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.PropertyTypeSearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.RoofType;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.RoofTypeSearchCriteria;
import org.egov.models.SpecialNotice;
import org.egov.models.SpecialNoticeRequest;
import org.egov.models.SpecialNoticeResponse;
import org.egov.models.StructureClass;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.models.StructureClassSearchCriteria;
import org.egov.models.TaxExemptionReason;
import org.egov.models.TaxExemptionReasonRequest;
import org.egov.models.TaxExemptionReasonResponse;
import org.egov.models.TaxExemptionReasonSearchCriteria;
import org.egov.models.UsageMaster;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.UsageMasterSearchCriteria;
import org.egov.models.WallType;
import org.egov.models.WallTypeRequest;
import org.egov.models.WallTypeResponse;
import org.egov.models.WallTypeSearchCriteria;
import org.egov.models.WoodType;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;
import org.egov.models.WoodTypeSearchCriteria;
import org.egov.property.PtPropertyApplication;
import org.egov.property.services.Masterservice;
import org.egov.property.services.NoticeService;
import org.egov.property.services.PropertyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = { PtPropertyApplication.class })
public class PropertyMasterControllerTest {

	@MockBean
	private Masterservice masterService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	PropertyService propertyService;

	@MockBean
	KafkaTemplate kafkaTemplate;

	@MockBean
	NoticeService noticeService;

	@Test
	public void testShouldCreateMasterFloorType() {
		List<FloorType> floorTypes = new ArrayList<>();
		FloorType floorType = new FloorType();
		floorType.setTenantId("123");
		floorType.setCode("Khan");
		floorType.setName("Prasad");

		AuditDetails auditDetails = new AuditDetails();
		floorType.setAuditDetails(auditDetails);

		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();
		floorTypes.add(floorType);

		floorTypeResponse.setResponseInfo(new ResponseInfo());
		floorTypeResponse.setFloorTypes(floorTypes);

		try {
			when(masterService.createFloorType(any(FloorTypeRequest.class), any(String.class)))
					.thenReturn(floorTypeResponse);

			mockMvc.perform(post("/property/floortypes/_create").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("createFloor.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testshouldupdatefloortype() {

		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();
		List<FloorType> floorTypes = new ArrayList<>();
		FloorType floorType = new FloorType();
		floorType.setId((long) 1);
		floorType.setTenantId("1234");
		floorType.setName("Stone Flooring");
		floorType.setCode("2566");
		floorType.setNameLocal("Stone Flooring");
		floorType.setDescription("Stone Flooring");

		AuditDetails auditDetails = new AuditDetails();
		floorType.setAuditDetails(auditDetails);

		floorTypes.add(floorType);

		floorTypeResponse.setResponseInfo(new ResponseInfo());
		floorTypeResponse.setFloorTypes(floorTypes);

		try {
			when(masterService.updateFloorType(any(FloorTypeRequest.class))).thenReturn(floorTypeResponse);

			mockMvc.perform(post("/property/floortypes/_update").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("updateFloorTypeReuqest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateFloorTypeResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testShouldSearchFloorType() {

		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();
		List<FloorType> floorTypes = new ArrayList<>();
		FloorType floorType = new FloorType();
		floorType.setTenantId("1234");
		floorType.setCode("256");
		floorType.setName("Stone Flooring");
		floorType.setNameLocal("Stone");
		floorType.setDescription("Stone Flooring");

		AuditDetails auditDetails = new AuditDetails();
		floorType.setAuditDetails(auditDetails);

		floorTypes.add(floorType);

		floorTypeResponse.setResponseInfo(new ResponseInfo());
		floorTypeResponse.setFloorTypes(floorTypes);

		try {
			when(masterService.getFloorTypeMaster(any(RequestInfo.class), any(FloorTypeSearchCriteria.class)))
					.thenReturn(floorTypeResponse);

			mockMvc.perform(post("/property/floortypes/_search").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("searchFloorTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchFloorTypeResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testShouldCreateRoofType() {

		List<RoofType> roofTypes = new ArrayList<>();
		RoofType roofType = new RoofType();
		roofType.setTenantId("1234");
		roofType.setCode("MRTT");
		roofType.setName("KKL16");
		roofType.setNameLocal("Mansard KKL");
		roofType.setDescription("Imported from USA");

		AuditDetails auditDetails = new AuditDetails();
		roofType.setAuditDetails(auditDetails);
		roofTypes.add(roofType);

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();

		roofTypeResponse.setResponseInfo(new ResponseInfo());
		roofTypeResponse.setRoofTypes(roofTypes);

		try {
			when(masterService.createRoofype(any(RoofTypeRequest.class), any(String.class)))
					.thenReturn(roofTypeResponse);

			mockMvc.perform(post("/property/rooftypes/_create").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("createRoofTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createRoofTypeResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testShouldUpdateRoofType() {

		List<RoofType> roofTypes = new ArrayList<>();
		RoofType roofType = new RoofType();
		roofType.setTenantId("1234");
		roofType.setCode("GRTT");
		roofType.setName("Gambrel Roof Type");
		roofType.setNameLocal("Gambrel Roof");
		roofType.setDescription("Imported from USA");

		AuditDetails auditDetails = new AuditDetails();
		roofType.setAuditDetails(auditDetails);
		roofTypes.add(roofType);

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();

		roofTypeResponse.setResponseInfo(new ResponseInfo());
		roofTypeResponse.setRoofTypes(roofTypes);

		try {
			when(masterService.updateRoofType(any(RoofTypeRequest.class))).thenReturn(roofTypeResponse);

			mockMvc.perform(post("/property/rooftypes/_update").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("updateRoofTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateRoofTypeResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testShouldSearchRoofType() {

		List<RoofType> roofTypes = new ArrayList<>();
		RoofType roofType = new RoofType();
		roofType.setTenantId("1234");
		roofType.setCode("MRT");
		roofType.setName("Mansard Roof Type");
		roofType.setNameLocal("Mansard");
		roofType.setDescription("Imported from USA");

		AuditDetails auditDetails = new AuditDetails();
		roofType.setAuditDetails(auditDetails);
		roofTypes.add(roofType);

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();
		roofTypeResponse.setRoofTypes(roofTypes);
		roofTypeResponse.setResponseInfo(new ResponseInfo());

		try {
			when(masterService.getRoofypes(any(RequestInfo.class), any(RoofTypeSearchCriteria.class)))
					.thenReturn(roofTypeResponse);

			mockMvc.perform(post("/property/rooftypes/_search").param("tenantId", "1234")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("searchRoofTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchRoofTypeResponse.json")));
		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testShouldCreateWoodType() {

		List<WoodType> woodTypes = new ArrayList<>();
		WoodType woodType = new WoodType();
		woodType.setTenantId("1237");
		woodType.setName("Maple Wood Type");
		woodType.setCode("MWTT");
		woodType.setNameLocal("Maple Type");
		woodType.setDescription("Imported from USA");

		AuditDetails auditDetails = new AuditDetails();
		woodType.setAuditDetails(auditDetails);
		woodTypes.add(woodType);

		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();

		woodTypeResponse.setResponseInfo(new ResponseInfo());
		woodTypeResponse.setWoodTypes(woodTypes);

		try {
			when(masterService.createWoodType(any(WoodTypeRequest.class), any(String.class)))
					.thenReturn(woodTypeResponse);

			mockMvc.perform(post("/property/woodtypes/_create").param("tenantId", "1237")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("createWoodTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createWoodTypeResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testShouldUpdateWoodType() {

		List<WoodType> woodTypes = new ArrayList<>();
		WoodType woodType = new WoodType();
		woodType.setTenantId("1237");
		woodType.setCode("WWTT");
		woodType.setName("Walnut Wood Type");
		woodType.setNameLocal("Walnut Type");
		woodType.setDescription("Imported from USA");

		AuditDetails auditDetails = new AuditDetails();
		woodType.setAuditDetails(auditDetails);
		woodTypes.add(woodType);

		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();

		woodTypeResponse.setResponseInfo(new ResponseInfo());
		woodTypeResponse.setWoodTypes(woodTypes);

		try {
			when(masterService.updateWoodType(any(WoodTypeRequest.class))).thenReturn(woodTypeResponse);

			mockMvc.perform(post("/property/woodtypes/_update").param("tenantId", "1237")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("updateWoodTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateWoodTypeResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testShouldSearchWoodType() {

		List<WoodType> woodTypes = new ArrayList<>();
		WoodType woodType = new WoodType();
		woodType.setTenantId("1237");
		woodType.setCode("WWTT");
		woodType.setName("Walnut Wood Type");
		woodType.setNameLocal("Walnut");
		woodType.setDescription("Imported from USA");

		AuditDetails auditDetails = new AuditDetails();
		woodType.setAuditDetails(auditDetails);
		woodTypes.add(woodType);

		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();

		woodTypeResponse.setResponseInfo(new ResponseInfo());
		woodTypeResponse.setWoodTypes(woodTypes);

		try {
			when(masterService.getWoodTypes(any(RequestInfo.class), any(WoodTypeSearchCriteria.class)))
					.thenReturn(woodTypeResponse);

			mockMvc.perform(post("/property/woodtypes/_search").param("tenantId", "1237")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("searchWoodTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchWoodTypeResponse.json")));
		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: create department master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateDepartment() throws Exception {

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		List<Department> departments = new ArrayList<Department>();
		Department department = new Department();
		department.setTenantId("default");
		department.setCategory("testing");
		department.setCode("0009");
		department.setName("kumar");

		AuditDetails auditDetails = new AuditDetails();
		department.setAuditDetails(auditDetails);
		departments.add(department);

		departmentResponse.setResponseInfo(new ResponseInfo());
		departmentResponse.setDepartments(departments);
		try {
			when(masterService.createDepartmentMaster(any(String.class), any(DepartmentRequest.class)))
					.thenReturn(departmentResponse);
			mockMvc.perform(post("/property/departments/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("departmentcreaterequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("departmentcreateresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: update department master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateDepartment() throws Exception {

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		List<Department> departments = new ArrayList<Department>();
		Department department = new Department();
		department.setTenantId("default");
		department.setCategory("testing");
		department.setCode("testing");
		department.setName("testing");
		AuditDetails auditDetails = new AuditDetails();
		department.setAuditDetails(auditDetails);
		departments.add(department);

		departmentResponse.setResponseInfo(new ResponseInfo());
		departmentResponse.setDepartments(departments);
		try {
			when(masterService.updateDepartmentMaster(any(DepartmentRequest.class))).thenReturn(departmentResponse);

			mockMvc.perform(post("/property/departments/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("departmentupdaterequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("departmentupdateresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: search department master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchDepartment() throws Exception {

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		List<Department> departments = new ArrayList<Department>();
		Department department = new Department();
		department.setTenantId("default");
		department.setCategory("testing");
		department.setCode("testing");
		department.setName("testing");
		AuditDetails auditDetails = new AuditDetails();
		department.setAuditDetails(auditDetails);
		departments.add(department);

		departmentResponse.setResponseInfo(new ResponseInfo());
		departmentResponse.setDepartments(departments);
		try {
			when(masterService.getDepartmentMaster(any(RequestInfo.class), any(DepartmentSearchCriteria.class)))
					.thenReturn(departmentResponse);

			mockMvc.perform(post("/property/departments/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("departmentsearchrequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("departmentsearchresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	/**
	 * Description: create Occuapancy master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateOccuapancy() throws Exception {

		OccuapancyMasterResponse occuapancyResponse = new OccuapancyMasterResponse();
		List<OccuapancyMaster> occuapancies = new ArrayList<OccuapancyMaster>();
		OccuapancyMaster occuapancy = new OccuapancyMaster();
		occuapancy.setTenantId("default");
		occuapancy.setCode("testing");
		occuapancy.setName("testing");

		AuditDetails auditDetails = new AuditDetails();
		occuapancy.setAuditDetails(auditDetails);
		occuapancies.add(occuapancy);

		occuapancyResponse.setResponseInfo(new ResponseInfo());
		occuapancyResponse.setOccuapancyMasters(occuapancies);
		try {
			when(masterService.createOccuapancyMaster(any(String.class), any(OccuapancyMasterRequest.class)))
					.thenReturn(occuapancyResponse);
			mockMvc.perform(post("/property/occuapancies/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("occupanciescreaterequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("occupanciescreateresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: update occuapancies master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateOccupancies() throws Exception {

		OccuapancyMasterResponse occuapancyResponse = new OccuapancyMasterResponse();
		List<OccuapancyMaster> occuapancies = new ArrayList<OccuapancyMaster>();
		OccuapancyMaster occuapancy = new OccuapancyMaster();
		occuapancy.setTenantId("default");
		occuapancy.setCode("testing");
		occuapancy.setName("testing");

		AuditDetails auditDetails = new AuditDetails();
		occuapancy.setAuditDetails(auditDetails);
		occuapancies.add(occuapancy);

		occuapancyResponse.setResponseInfo(new ResponseInfo());
		occuapancyResponse.setOccuapancyMasters(occuapancies);
		try {
			when(masterService.updateOccuapancyMaster(any(OccuapancyMasterRequest.class)))
					.thenReturn(occuapancyResponse);

			mockMvc.perform(post("/property/occuapancies/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("occupanciesupdaterequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("occupanciesupdateresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: search occupancies master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchOccupancies() throws Exception {

		OccuapancyMasterResponse occuapancyResponse = new OccuapancyMasterResponse();
		List<OccuapancyMaster> occuapancies = new ArrayList<OccuapancyMaster>();
		OccuapancyMaster occuapancy = new OccuapancyMaster();
		occuapancy.setTenantId("default");
		occuapancy.setCode("testing");
		occuapancy.setName("testing");

		AuditDetails auditDetails = new AuditDetails();
		occuapancy.setAuditDetails(auditDetails);
		occuapancies.add(occuapancy);

		occuapancyResponse.setResponseInfo(new ResponseInfo());
		occuapancyResponse.setOccuapancyMasters(occuapancies);
		try {
			when(masterService.getOccuapancyMaster(any(RequestInfo.class), any(OccuapancyMasterSearchCriteria.class)))
					.thenReturn(occuapancyResponse);

			mockMvc.perform(post("/property/occuapancies/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("occupanciessearchrequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("occupanciessearchresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	/**
	 * Description: create propertytypes master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreatePropertyTypes() throws Exception {

		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();
		List<PropertyType> propertyTypes = new ArrayList<PropertyType>();
		PropertyType propertyType = new PropertyType();
		propertyType.setTenantId("default");
		propertyType.setCode("0007");
		propertyType.setName("anil");

		AuditDetails auditDetails = new AuditDetails();
		propertyType.setAuditDetails(auditDetails);
		propertyTypes.add(propertyType);

		propertyTypeResponse.setResponseInfo(new ResponseInfo());
		propertyTypeResponse.setPropertyTypes(propertyTypes);
		try {
			when(masterService.createPropertyTypeMaster(any(String.class), any(PropertyTypeRequest.class)))
					.thenReturn(propertyTypeResponse);
			mockMvc.perform(post("/property/propertytypes/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("propertytypescreaterequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("propertytypescreateresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: update propertytypes master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatePropertyTypes() throws Exception {

		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();
		List<PropertyType> propertyTypes = new ArrayList<PropertyType>();
		PropertyType propertyType = new PropertyType();
		propertyType.setTenantId("default");
		propertyType.setCode("testing");
		propertyType.setName("testing");

		AuditDetails auditDetails = new AuditDetails();
		propertyType.setAuditDetails(auditDetails);
		propertyTypes.add(propertyType);

		propertyTypeResponse.setResponseInfo(new ResponseInfo());
		propertyTypeResponse.setPropertyTypes(propertyTypes);
		try {
			when(masterService.updatePropertyTypeMaster(any(PropertyTypeRequest.class)))
					.thenReturn(propertyTypeResponse);
			mockMvc.perform(post("/property/propertytypes/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("propertytypesupdaterequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("propertytypesupdateresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description: search property type master
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchPropertyTypes() throws Exception {
		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();
		List<PropertyType> propertyTypes = new ArrayList<PropertyType>();
		PropertyType propertyType = new PropertyType();
		propertyType.setTenantId("default");
		propertyType.setCode("testing");
		propertyType.setName("testing");
		propertyType.setId(null);
		propertyType.setNameLocal(null);
		propertyType.setDescription(null);
		propertyType.setActive(null);
		propertyType.setOrderNumber(null);
		propertyType.setData(null);
		AuditDetails auditDetails = new AuditDetails();
		propertyType.setAuditDetails(auditDetails);
		propertyTypes.add(propertyType);

		propertyTypeResponse.setResponseInfo(new ResponseInfo());
		propertyTypeResponse.setPropertyTypes(propertyTypes);
		try {
			when(masterService.getPropertyTypeMaster(any(RequestInfo.class), any(PropertyTypeSearchCriteria.class)))
					.thenReturn(propertyTypeResponse);

			mockMvc.perform(post("/property/propertytypes/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("propertytypessearchrequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("propertytypessearchresponse.json")));
		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testCreateStructureClasses() throws Exception {

		List<StructureClass> structureClasses = new ArrayList<>();
		StructureClass structureClass = new StructureClass();
		structureClass.setTenantId("default");
		structureClass.setName("kkl16");
		structureClass.setCode("1234");

		AuditDetails auditDetails = new AuditDetails();
		structureClass.setAuditDetails(auditDetails);

		StructureClassResponse structureClassResponse = new StructureClassResponse();
		structureClasses.add(structureClass);

		structureClassResponse.setResponseInfo(new ResponseInfo());
		structureClassResponse.setStructureClasses(structureClasses);

		try {

			when(masterService.craeateStructureClassMaster(any(String.class), any(StructureClassRequest.class)))
					.thenReturn(structureClassResponse);

			mockMvc.perform(post("/property/structureclasses/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("structureCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("structureCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testUpdateStructureClasses() throws Exception {

		StructureClassResponse structureClassResponse = new StructureClassResponse();
		List<StructureClass> structureClasses = new ArrayList<>();
		StructureClass structureClass = new StructureClass();
		structureClass.setTenantId("default");
		structureClass.setName("kkl16");
		structureClass.setCode("1234");

		AuditDetails auditDetails = new AuditDetails();
		structureClass.setAuditDetails(auditDetails);

		structureClasses.add(structureClass);

		structureClassResponse.setResponseInfo(new ResponseInfo());
		structureClassResponse.setStructureClasses(structureClasses);

		try {

			when(masterService.updateStructureClassMaster(any(StructureClassRequest.class)))
					.thenReturn(structureClassResponse);
			mockMvc.perform(post("/property/structureclasses/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("structureClassUpdateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("structureClassUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testSearchStructureClasses() throws Exception {

		StructureClassResponse structureClassResponse = new StructureClassResponse();
		List<StructureClass> structureClasses = new ArrayList<>();
		StructureClass structureClass = new StructureClass();
		structureClass.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		structureClass.setAuditDetails(auditDetails);

		structureClasses.add(structureClass);

		structureClassResponse.setResponseInfo(new ResponseInfo());
		structureClassResponse.setStructureClasses(structureClasses);

		try {

			when(masterService.getStructureClassMaster(any(RequestInfo.class), any(StructureClassSearchCriteria.class)))
					.thenReturn(structureClassResponse);

			mockMvc.perform(post("/property/structureclasses/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("structureclassSearchRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("structureclassSearchResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testSearchUsageMaster() throws Exception {

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();
		List<UsageMaster> usageMasters = new ArrayList<>();
		UsageMaster usageMaster = new UsageMaster();
		usageMaster.setTenantId("default");
		usageMaster.setName("test_search");
		usageMaster.setCode("1234");
		usageMaster.setDescription("test search_description");
		usageMaster.setNameLocal("12212");

		AuditDetails auditDetails = new AuditDetails();
		usageMaster.setAuditDetails(auditDetails);

		usageMasters.add(usageMaster);

		usageMasterResponse.setResponseInfo(new ResponseInfo());
		usageMasterResponse.setUsageMasters(usageMasters);

		try {

			when(masterService.getUsageMaster(any(RequestInfo.class), any(UsageMasterSearchCriteria.class)))
					.thenReturn(usageMasterResponse);

			mockMvc.perform(post("/property/usages/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("usageMasterSearchRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("usageMasterSearchResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testCreateUsageMaster() throws Exception {

		List<UsageMaster> usageMasters = new ArrayList<>();
		UsageMaster usageMaster = new UsageMaster();
		usageMaster.setTenantId("default");
		usageMaster.setName("Yoyo");
		usageMaster.setCode("1234");
		AuditDetails auditDetails = new AuditDetails();
		usageMaster.setAuditDetails(auditDetails);

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();
		usageMasters.add(usageMaster);

		usageMasterResponse.setResponseInfo(new ResponseInfo());
		usageMasterResponse.setUsageMasters(usageMasters);

		try {

			when(masterService.createUsageMaster(any(String.class), any(UsageMasterRequest.class)))
					.thenReturn(usageMasterResponse);

			mockMvc.perform(post("/property/usages/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("usageMasterCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("usageMasterCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testUpdateUsageMaster() throws Exception {

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();
		List<UsageMaster> usageMasters = new ArrayList<>();
		UsageMaster usageMaster = new UsageMaster();
		usageMaster.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		usageMaster.setAuditDetails(auditDetails);

		usageMasters.add(usageMaster);

		usageMasterResponse.setResponseInfo(new ResponseInfo());
		usageMasterResponse.setUsageMasters(usageMasters);

		try {
			when(masterService.updateUsageMaster(any(UsageMasterRequest.class))).thenReturn(usageMasterResponse);

			mockMvc.perform(post("/property/usages/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("usageMaterUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("usageMasterUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testSearchWallTypes() throws Exception {

		WallTypeResponse wallTypeResponse = new WallTypeResponse();
		List<WallType> wallTypes = new ArrayList<>();
		WallType wallType = new WallType();
		wallType.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		wallType.setAuditDetails(auditDetails);

		wallTypes.add(wallType);

		wallTypeResponse.setResponseInfo(new ResponseInfo());
		wallTypeResponse.setWallTypes(wallTypes);

		try {

			when(masterService.getWallTypeMaster(any(RequestInfo.class), any(WallTypeSearchCriteria.class)))
					.thenReturn(wallTypeResponse);

			mockMvc.perform(post("/property/walltypes/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("wallTypeSearchRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("wallTypeSearchResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testCreateWallTypes() throws Exception {

		List<WallType> wallTypes = new ArrayList<>();
		WallType wallType = new WallType();
		wallType.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		wallType.setAuditDetails(auditDetails);

		WallTypeResponse wallTypeResponse = new WallTypeResponse();
		wallTypes.add(wallType);

		wallTypeResponse.setResponseInfo(new ResponseInfo());
		wallTypeResponse.setWallTypes(wallTypes);

		try {

			when(masterService.createWallTypeMaster(any(String.class), any(WallTypeRequest.class)))
					.thenReturn(wallTypeResponse);

			mockMvc.perform(post("/property/walltypes/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("wallTypeCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("wallTypeCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testUpdateWallTypes() throws Exception {

		WallTypeResponse wallTypeResponse = new WallTypeResponse();
		List<WallType> wallTypes = new ArrayList<>();
		WallType wallType = new WallType();
		wallType.setId(1l);
		wallType.setTenantId("default");
		wallType.setName("kkl16");
		wallType.setCode("1234");

		AuditDetails auditDetails = new AuditDetails();
		wallType.setAuditDetails(auditDetails);

		wallTypes.add(wallType);

		wallTypeResponse.setResponseInfo(new ResponseInfo());
		wallTypeResponse.setWallTypes(wallTypes);

		try {

			when(masterService.updateWallTypeMaster(any(WallTypeRequest.class))).thenReturn(wallTypeResponse);

			mockMvc.perform(post("/property/walltypes/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("wallTypeUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("wallTypeUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void createDepreciationTest() {

		DepreciationResponse depreciationResponse = new DepreciationResponse();
		List<Depreciation> depreciations = new ArrayList<>();
		Depreciation depreciation = new Depreciation();
		depreciation.setTenantId("kiran");
		depreciation.setCode("praa");

		AuditDetails auditDetails = new AuditDetails();
		depreciation.setAuditDetails(auditDetails);

		depreciationResponse.setResponseInfo(new ResponseInfo());
		depreciations.add(depreciation);
		depreciationResponse.setDepreciations(depreciations);

		try {
			when(masterService.createDepreciation(anyString(), any(DepreciationRequest.class)))
					.thenReturn(depreciationResponse);
			mockMvc.perform(post("/property/depreciations/_create").param("tenantId", "kiran")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("createDepreciationRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createDepreciationResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(true);

	}

	@Test
	public void modifyDepreciationTest() {

		DepreciationResponse depreciationResponse = new DepreciationResponse();
		List<Depreciation> depreciations = new ArrayList<>();
		Depreciation depreciation = new Depreciation();
		depreciation.setTenantId("kiranprasad");
		depreciation.setId(5l);
		depreciation.setCode("pra");

		AuditDetails auditDetails = new AuditDetails();
		depreciation.setAuditDetails(auditDetails);

		depreciationResponse.setResponseInfo(new ResponseInfo());
		depreciations.add(depreciation);
		depreciationResponse.setDepreciations(depreciations);

		try {
			when(masterService.updateDepreciation(any(DepreciationRequest.class))).thenReturn(depreciationResponse);
			mockMvc.perform(post("/property/depreciations/_update").param("tenantId", "kiranprasad")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("updateDepreciationRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateDepreciationResponse.json")));
		}

		catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(true);
	}

	@Test
	public void searchDepreciationTest() {

		DepreciationResponse depreciationResponse = new DepreciationResponse();
		List<Depreciation> depreciations = new ArrayList<>();
		Depreciation depreciation = new Depreciation();
		depreciation.setTenantId("default");
		depreciation.setId(4l);
		depreciation.setCode("prasad");

		AuditDetails auditDetails = new AuditDetails();
		depreciation.setAuditDetails(auditDetails);

		depreciationResponse.setResponseInfo(new ResponseInfo());
		depreciations.add(depreciation);
		depreciationResponse.setDepreciations(depreciations);

		try {
			when(masterService.searchDepreciation(any(RequestInfo.class), any(DepreciationSearchCriteria.class)))
					.thenReturn(depreciationResponse);
			mockMvc.perform(post("/property/depreciations/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("searchDepreciationRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchDepreciationResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
		}

		assertTrue(true);

	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}

	@Test
	public void createMutationMasterTest() {

		MutationMasterResponse mutationMasterResponse = new MutationMasterResponse();
		List<MutationMaster> mutationMasters = new ArrayList<>();
		MutationMaster mutationMaster = new MutationMaster();
		mutationMaster.setTenantId("default");
		mutationMaster.setCode("prasad");

		AuditDetails auditDetails = new AuditDetails();
		mutationMaster.setAuditDetails(auditDetails);

		mutationMasterResponse.setResponseInfo(new ResponseInfo());
		mutationMasters.add(mutationMaster);
		mutationMasterResponse.setMutationMasters(mutationMasters);

		try {
			when(masterService.createMutationMater(anyString(), any(MutationMasterRequest.class)))
					.thenReturn(mutationMasterResponse);
			mockMvc.perform(post("/property/mutationmasters/_create").param("tenantId", "kiran")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createMutationMasterRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createMutationMasterResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(true);

	}

	@Test
	public void modifyMutationMasterTest() {

		MutationMasterResponse mutationMasterResponse = new MutationMasterResponse();
		List<MutationMaster> mutationMasters = new ArrayList<>();
		MutationMaster mutationMaster = new MutationMaster();
		mutationMaster.setTenantId("tenantid");
		mutationMaster.setCode("code");

		AuditDetails auditDetails = new AuditDetails();
		mutationMaster.setAuditDetails(auditDetails);

		mutationMasterResponse.setResponseInfo(new ResponseInfo());
		mutationMasters.add(mutationMaster);
		mutationMasterResponse.setMutationMasters(mutationMasters);

		try {

			when(masterService.updateMutationMaster(any(MutationMasterRequest.class)))
					.thenReturn(mutationMasterResponse);
			mockMvc.perform(post("/property/mutationmasters/_update").param("tenantId", "kiran")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateMutationMasterRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateMutationMasterResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(true);

	}

	@Test
	public void searchMutationMasterTest() {

		MutationMasterResponse mutationMasterResponse = new MutationMasterResponse();
		List<MutationMaster> mutationMasters = new ArrayList<>();
		MutationMaster mutationMaster = new MutationMaster();
		mutationMaster.setTenantId("tenantid");
		mutationMaster.setCode("name");

		AuditDetails auditDetails = new AuditDetails();
		mutationMaster.setAuditDetails(auditDetails);

		mutationMasterResponse.setResponseInfo(new ResponseInfo());
		mutationMasters.add(mutationMaster);
		mutationMasterResponse.setMutationMasters(mutationMasters);
		try {
			when(masterService.searchMutationMaster(any(RequestInfo.class), any(MutationMasterSearchCriteria.class)))
					.thenReturn(mutationMasterResponse);
			mockMvc.perform(post("/property/mutationmasters/_search").param("tenantId", "tenantid")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchMutationMasterRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchMutationMasterResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
		}

		assertTrue(true);
	}

	@Test
	public void createDocumentTypeMasterTest() {

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("tenantid1");
		documentType.setName("username1");
		documentType.setCode("1234");
		documentType.setApplication(ApplicationEnum.BIFURCATION);

		AuditDetails auditDetails = new AuditDetails();
		documentType.setAuditDetails(auditDetails);

		documentTypeResponse.setResponseInfo(new ResponseInfo());
		documentTypes.add(documentType);
		documentTypeResponse.setDocumentType(documentTypes);

		try {
			when(masterService.createDocumentTypeMaster(anyString(), any(DocumentTypeRequest.class)))
					.thenReturn(documentTypeResponse);
			mockMvc.perform(post("/property/documenttypes/_create").param("tenantId", "tenantid")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("createDocumentTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createDocumentTypeResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(true);

	}

	@Test
	public void modifyDocumentTypeMasterTest() {

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("default2");
		documentType.setCode("codevalue2");
		documentType.setName("veswanth2");

		AuditDetails auditDetails = new AuditDetails();
		documentType.setAuditDetails(auditDetails);

		documentTypeResponse.setResponseInfo(new ResponseInfo());
		documentTypes.add(documentType);
		documentTypeResponse.setDocumentType(documentTypes);

		try {

			when(masterService.updateDocumentTypeMaster(any(DocumentTypeRequest.class)))
					.thenReturn(documentTypeResponse);
			mockMvc.perform(post("/property/documenttypes/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateDocumentTypeRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateDocumentTypeResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(true);

	}

	@Test
	public void searchDocumentTypeTest() {

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("default");
		documentType.setName("veswanth");

		AuditDetails auditDetails = new AuditDetails();
		documentType.setAuditDetails(auditDetails);

		documentTypeResponse.setResponseInfo(new ResponseInfo());
		documentTypes.add(documentType);
		documentTypeResponse.setDocumentType(documentTypes);
		try {
			when(masterService.searchDocumentTypeMaster(any(RequestInfo.class), any(DocumentTypeSearchCriteria.class)))
					.thenReturn(documentTypeResponse);
			mockMvc.perform(post("/property/documenttypes/_search").param("tenantId", "tenantid")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("searchDocumentTypeRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchDocumentTypeResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
		}

		assertTrue(true);
	}

	@Test
	public void generateSpecialNoticeTest() {
		SpecialNoticeResponse specialNoticeResponse = new SpecialNoticeResponse();
		SpecialNotice notice = new SpecialNotice();

		notice.setTenantId("AP.TESTONE");
		notice.setUpicNo("1015111122");

		specialNoticeResponse.setNotice(notice);
		specialNoticeResponse.setResponseInfo(new ResponseInfo());

		try {
			when(propertyService.generateSpecialNotice(any(SpecialNoticeRequest.class)))
					.thenReturn(specialNoticeResponse);

			mockMvc.perform(post("/properties/specialnotice/_generate").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("generateSpecialNoticeRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("generateSpecialNoticeResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
		}

		assertTrue(true);
	}

	@Test
	public void createApartmentTest() {

		List<Apartment> apartments = new ArrayList<>();

		Apartment apartment = new Apartment();
		apartment.setTenantId("default");
		apartment.setCode("1010");
		apartment.setName("GOOGLE-FB");
		apartment.setTotalFloors(19l);
		apartment.setSourceOfWater("test");
		apartment.setTotalOpenSpace(10.5);
		apartment.setTotalProperties(145l);
		apartment.setResidtinalProperties(10l);
		apartment.setTotalBuiltUpArea(5555d);
		AuditDetails auditDetails = new AuditDetails();
		apartment.setAuditDetails(auditDetails);

		ApartmentResponse apartmentResponse = new ApartmentResponse();
		apartments.add(apartment);
		apartmentResponse.setResponseInfo(new ResponseInfo());
		apartmentResponse.setApartments(apartments);

		try {

			when(masterService.createApartment(any(String.class), any(ApartmentRequest.class)))
					.thenReturn(apartmentResponse);
			mockMvc.perform(post("/property/apartment/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createApartmentcomplexRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createApartmentcomplexResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void updateApartmentTest() {

		List<Apartment> apartments = new ArrayList<>();

		Apartment apartment = new Apartment();
		apartment.setTenantId("default");
		apartment.setName("FACEBOOK");
		apartment.setCode("1010");
		apartment.setTotalFloors(19l);
		apartment.setSourceOfWater("test");
		apartment.setTotalOpenSpace(10.5);
		apartment.setTotalProperties(145l);
		apartment.setResidtinalProperties(10l);
		apartment.setTotalBuiltUpArea(5555d);

		AuditDetails auditDetails = new AuditDetails();
		apartment.setAuditDetails(auditDetails);
		apartments.add(apartment);

		ApartmentResponse apartmentResponse = new ApartmentResponse();
		apartmentResponse.setResponseInfo(new ResponseInfo());
		apartmentResponse.setApartments(apartments);

		try {
			when(masterService.updateApartment(any(ApartmentRequest.class))).thenReturn(apartmentResponse);
			mockMvc.perform(post("/property/apartment/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateApartmentcomplexRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateApartmentcomplexResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void searchApartmentTest() {

		List<Apartment> apartments = new ArrayList<>();

		Apartment apartment = new Apartment();
		apartment.setTenantId("default");
		apartment.setCode("1010");
		apartment.setName("FACEBOOK");

		AuditDetails auditDetails = new AuditDetails();
		apartment.setAuditDetails(auditDetails);
		apartments.add(apartment);

		ApartmentResponse apartmentResponse = new ApartmentResponse();
		apartmentResponse.setResponseInfo(new ResponseInfo());
		apartmentResponse.setApartments(apartments);

		try {

			when(masterService.searchApartment(any(RequestInfo.class), any(ApartmentSearchCriteria.class)))
					.thenReturn(apartmentResponse);
			mockMvc.perform(post("/property/apartment/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchApartmentcomplexRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchApartmentcomplexResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);
	}

	@Test
	public void createGuidanceValueBoundaryTest() {
		List<GuidanceValueBoundary> guidanceValueBoundaries = new ArrayList<>();
		GuidanceValueBoundary guidanceValueBoundary = new GuidanceValueBoundary();
		guidanceValueBoundary.setTenantId("default");
		guidanceValueBoundary.setGuidanceValueBoundary1("gvb1");
		guidanceValueBoundary.setGuidanceValueBoundary2("gvb2");
		AuditDetails auditDetails = new AuditDetails();
		guidanceValueBoundary.setAuditDetails(auditDetails);
		GuidanceValueBoundaryResponse guidanceValueBoundaryResponse = new GuidanceValueBoundaryResponse();
		guidanceValueBoundaries.add(guidanceValueBoundary);
		guidanceValueBoundaryResponse.setResponseInfo(new ResponseInfo());
		guidanceValueBoundaryResponse.setGuidanceValueBoundaries(guidanceValueBoundaries);

		try {
			when(masterService.createGuidanceValueBoundary(any(String.class), any(GuidanceValueBoundaryRequest.class)))
					.thenReturn(guidanceValueBoundaryResponse);
			mockMvc.perform(post("/property/guidancevalueboundary/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createGuidanceValueBoundaryRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createGuidanceValueBoundaryResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void updateGuidanceValueBoundaryTest() {
		List<GuidanceValueBoundary> guidanceValueBoundaries = new ArrayList<>();
		GuidanceValueBoundary guidanceValueBoundary = new GuidanceValueBoundary();
		guidanceValueBoundary.setId(1l);
		guidanceValueBoundary.setTenantId("default");
		guidanceValueBoundary.setGuidanceValueBoundary1("updated-gvb1");
		guidanceValueBoundary.setGuidanceValueBoundary2("updated-gvb2");
		AuditDetails auditDetails = new AuditDetails();
		guidanceValueBoundary.setAuditDetails(auditDetails);
		GuidanceValueBoundaryResponse guidanceValueBoundaryResponse = new GuidanceValueBoundaryResponse();
		guidanceValueBoundaries.add(guidanceValueBoundary);
		guidanceValueBoundaryResponse.setResponseInfo(new ResponseInfo());
		guidanceValueBoundaryResponse.setGuidanceValueBoundaries(guidanceValueBoundaries);

		try {
			when(masterService.updateGuidanceValueBoundary(any(GuidanceValueBoundaryRequest.class)))
					.thenReturn(guidanceValueBoundaryResponse);
			mockMvc.perform(post("/property/guidancevalueboundary/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateGuidanceValueBoundaryRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateGuidanceValueBoundaryResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void searchGuidanceValueBoundaryTest() {
		List<GuidanceValueBoundary> guidanceValueBoundaries = new ArrayList<>();
		GuidanceValueBoundary guidanceValueBoundary = new GuidanceValueBoundary();
		guidanceValueBoundary.setTenantId("default");
		guidanceValueBoundary.setGuidanceValueBoundary1("updated-gvb1");
		guidanceValueBoundary.setGuidanceValueBoundary2("updated-gvb2");
		AuditDetails auditDetails = new AuditDetails();
		guidanceValueBoundary.setAuditDetails(auditDetails);
		guidanceValueBoundaries.add(guidanceValueBoundary);
		GuidanceValueBoundaryResponse guidanceValueBoundaryResponse = new GuidanceValueBoundaryResponse();
		guidanceValueBoundaryResponse.setResponseInfo(new ResponseInfo());
		guidanceValueBoundaryResponse.setGuidanceValueBoundaries(guidanceValueBoundaries);

		try {
			when(masterService.getGuidanceValueBoundary(any(RequestInfo.class),
					any(GuidanceValueBoundarySearchCriteria.class))).thenReturn(guidanceValueBoundaryResponse);
			mockMvc.perform(post("/property/guidancevalueboundary/_search").param("tenantId", "default")
					.param("guidanceValueBoundary1", "updated-gvb1").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchGuidanceValueBoundaryRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchGuidanceValueBoundaryResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void createAppConfigurationTest() {
		List<AppConfiguration> appConfigurations = new ArrayList<>();
		List<String> values = new ArrayList<>();
		values.add("appvalue1");
		values.add("appvalue2");
		values.add("appvalue3");
		AppConfiguration appConfiguration = new AppConfiguration();
		appConfiguration.setTenantId("default");
		appConfiguration.setKeyName("AppConfig");
		appConfiguration.setValues(values);
		AuditDetails auditDetails = new AuditDetails();
		appConfiguration.setAuditDetails(auditDetails);
		AppConfigurationResponse appConfigurationResponse = new AppConfigurationResponse();
		appConfigurations.add(appConfiguration);
		appConfigurationResponse.setResponseInfo(new ResponseInfo());
		appConfigurationResponse.setAppConfigurations(appConfigurations);

		try {
			when(masterService.createAppConfiguration(any(String.class), any(AppConfigurationRequest.class)))
					.thenReturn(appConfigurationResponse);
			mockMvc.perform(post("/property/appconfiguration/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createAppConfigurationRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createAppConfigurationResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void updateAppConfigurationTest() {
		List<AppConfiguration> appConfigurations = new ArrayList<>();
		List<String> values = new ArrayList<>();
		values.add("appvalueupdate1");
		values.add("appvalueupdate2");
		values.add("appvalueupdate3");
		AppConfiguration appConfiguration = new AppConfiguration();
		appConfiguration.setId(1l);
		appConfiguration.setTenantId("default");
		appConfiguration.setKeyName("AppConfigUpdated");
		appConfiguration.setValues(values);
		AuditDetails auditDetails = new AuditDetails();
		appConfiguration.setAuditDetails(auditDetails);
		AppConfigurationResponse appConfigurationResponse = new AppConfigurationResponse();
		appConfigurations.add(appConfiguration);
		appConfigurationResponse.setResponseInfo(new ResponseInfo());
		appConfigurationResponse.setAppConfigurations(appConfigurations);

		try {
			when(masterService.updateAppConfiguration(any(AppConfigurationRequest.class)))
					.thenReturn(appConfigurationResponse);
			mockMvc.perform(post("/property/appconfiguration/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateAppConfigurationRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateAppConfigurationResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void searchAppConfigurationTest() {
		List<AppConfiguration> appConfigurations = new ArrayList<>();
		List<String> values = new ArrayList<>();
		values.add("appvalueupdate1");
		values.add("appvalueupdate2");
		values.add("appvalueupdate3");
		AppConfiguration appConfiguration = new AppConfiguration();
		appConfiguration.setTenantId("default");
		appConfiguration.setKeyName("AppConfigUpdated");
		appConfiguration.setValues(values);
		AuditDetails auditDetails = new AuditDetails();
		appConfiguration.setAuditDetails(auditDetails);
		appConfigurations.add(appConfiguration);
		AppConfigurationResponse appConfigurationResponse = new AppConfigurationResponse();
		appConfigurationResponse.setResponseInfo(new ResponseInfo());
		appConfigurationResponse.setAppConfigurations(appConfigurations);

		try {
			when(masterService.getAppConfiguration(any(RequestInfo.class), any(AppConfigurationSearchCriteria.class)))
					.thenReturn(appConfigurationResponse);
			mockMvc.perform(post("/property/appconfiguration/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchAppConfigurationRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchAppConfigurationResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void testCreateDemolitionReason() throws Exception {

		List<DemolitionReason> demolitionReasons = new ArrayList<>();
		DemolitionReason demolitionReason = new DemolitionReason();
		demolitionReason.setTenantId("default");
		demolitionReason.setName("manish");
		demolitionReason.setCode("123654");

		AuditDetails auditDetails = new AuditDetails();
		demolitionReason.setAuditDetails(auditDetails);

		DemolitionReasonResponse demolitionReasonResponse = new DemolitionReasonResponse();
		demolitionReasons.add(demolitionReason);

		demolitionReasonResponse.setResponseInfo(new ResponseInfo());
		demolitionReasonResponse.setDemolitionReason(demolitionReasons);

		try {
			when(masterService.createDemolitionReason(any(String.class), any(DemolitionReasonRequest.class)))
					.thenReturn(demolitionReasonResponse);

			mockMvc.perform(post("/property/demolitionreason/_create").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createDemolitionReasonRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createDemolitionReasonResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testUpdateDemolitionReason() throws Exception {

		DemolitionReasonResponse demolitionReasonResponse = new DemolitionReasonResponse();
		List<DemolitionReason> demolitionReasons = new ArrayList<>();
		DemolitionReason demolitionReason = new DemolitionReason();
		demolitionReason.setId((long) 9);
		demolitionReason.setTenantId("default");
		demolitionReason.setName("pallu");
		demolitionReason.setCode("102");

		AuditDetails auditDetails = new AuditDetails();
		demolitionReason.setAuditDetails(auditDetails);

		demolitionReasons.add(demolitionReason);

		demolitionReasonResponse.setResponseInfo(new ResponseInfo());
		demolitionReasonResponse.setDemolitionReason(demolitionReasons);
		;

		try {

			when(masterService.updateDemolitionReason(any(DemolitionReasonRequest.class)))
					.thenReturn(demolitionReasonResponse);
			mockMvc.perform(post("/property/demolitionreason/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateDemolitionReasonRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateDemolitionReasonResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void testSearchDemolitionReason() throws Exception {

		DemolitionReasonResponse demolitionReasonResponse = new DemolitionReasonResponse();
		List<DemolitionReason> demolitionReasons = new ArrayList<>();
		DemolitionReason demolitionReason = new DemolitionReason();
		demolitionReason.setTenantId("default");
		demolitionReason.setName("pallu");
		demolitionReason.setCode("456");

		AuditDetails auditDetails = new AuditDetails();
		demolitionReason.setAuditDetails(auditDetails);

		demolitionReasons.add(demolitionReason);

		demolitionReasonResponse.setResponseInfo(new ResponseInfo());
		demolitionReasonResponse.setDemolitionReason(demolitionReasons);

		try {
			when(masterService.getDemolitionReason(any(RequestInfo.class), any(DemolitionReasonSearchCriteria.class)))
					.thenReturn(demolitionReasonResponse);
			mockMvc.perform(post("/property/demolitionreason/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchDemolitionReasonRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchDemolitionReasonResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	@Test
	public void creatTaxExemptionReasonTest() {

		TaxExemptionReasonResponse taxExemptionReasonResponse = new TaxExemptionReasonResponse();
		List<TaxExemptionReason> exemptionReasons = new ArrayList<>();
		List<String> taxHeads = new ArrayList<>();
		taxHeads.add("THU");
		TaxExemptionReason taxExemptionReason = new TaxExemptionReason();
		taxExemptionReason.setTenantId("default");
		taxExemptionReason.setCode("ZIPCODE");
		taxExemptionReason.setName("X-MAN");
		taxExemptionReason.setPercentageRate(45d);
		taxExemptionReason.setDescription("Test Update Reason!");
		taxExemptionReason.setActive(true);
		taxExemptionReason.setTaxHeads(taxHeads);
		AuditDetails auditDetails = new AuditDetails();
		taxExemptionReason.setAuditDetails(auditDetails);
		taxExemptionReasonResponse.setResponseInfo(new ResponseInfo());
		exemptionReasons.add(taxExemptionReason);
		taxExemptionReasonResponse.setTaxExemptionReasons(exemptionReasons);

		try {
			when(masterService.createTaxExemptionReason(any(TaxExemptionReasonRequest.class)))
					.thenReturn(taxExemptionReasonResponse);
			mockMvc.perform(post("/property/taxexemptionreason/_create").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createTaxExemptionReasonRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createTaxExemptionReasonResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(true);
	}

	@Test
	public void updateTaxExemptionReasonTest() {

		TaxExemptionReasonResponse taxExemptionReasonResponse = new TaxExemptionReasonResponse();
		List<TaxExemptionReason> taxExemptionReasons = new ArrayList<>();
		List<String> taxHeads = new ArrayList<>();
		taxHeads.add("Test 1 TAXHEAD1");
		TaxExemptionReason taxExemptionReason = new TaxExemptionReason();
		taxExemptionReason.setId(1l);
		taxExemptionReason.setTenantId("default");
		taxExemptionReason.setCode("UPDATE-ZIPCODE");
		taxExemptionReason.setName("UPDATE-MAN");
		taxExemptionReason.setPercentageRate(45d);
		taxExemptionReason.setDescription("Test Update Reason!");
		taxExemptionReason.setActive(true);
		taxExemptionReason.setTaxHeads(taxHeads);
		AuditDetails auditDetails = new AuditDetails();
		taxExemptionReason.setAuditDetails(auditDetails);
		taxExemptionReasonResponse.setResponseInfo(new ResponseInfo());
		taxExemptionReasons.add(taxExemptionReason);
		taxExemptionReasonResponse.setTaxExemptionReasons(taxExemptionReasons);

		try {
			when(masterService.updateTaxExemptionReason(any(TaxExemptionReasonRequest.class)))
					.thenReturn(taxExemptionReasonResponse);
			mockMvc.perform(post("/property/taxexemptionreason/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateTaxExemptionReasonRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateTaxExemptionReasonResponse.json")));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(true);
	}

	@Test
	public void searchTaxExemptionReasonTest() {

		List<TaxExemptionReason> taxExemptionReasons = new ArrayList<>();
		TaxExemptionReasonSearchCriteria exemptionReasonSearchCriteria = new TaxExemptionReasonSearchCriteria();
		exemptionReasonSearchCriteria.setTenantId("default");
		exemptionReasonSearchCriteria.setCode("UPDATE-ZIPCODE");
		List<String> taxHeads = new ArrayList<>();
		taxHeads.add("Test 1 TAXHEAD1");
		TaxExemptionReason taxExemptionReason = new TaxExemptionReason();
		taxExemptionReason.setId(1l);
		taxExemptionReason.setTenantId("default");
		taxExemptionReason.setCode("UPDATE-ZIPCODE");
		taxExemptionReason.setName("UPDATE-MAN");
		taxExemptionReason.setPercentageRate(50.0);
		taxExemptionReason.setDescription("Test Update Reason!");
		taxExemptionReason.setActive(true);
		taxExemptionReason.setTaxHeads(taxHeads);
		AuditDetails auditDetails = new AuditDetails();
		taxExemptionReason.setAuditDetails(auditDetails);
		taxExemptionReasons.add(taxExemptionReason);
		TaxExemptionReasonResponse exemptionReasonResponse = new TaxExemptionReasonResponse();
		exemptionReasonResponse.setResponseInfo(new ResponseInfo());
		exemptionReasonResponse.setTaxExemptionReasons(taxExemptionReasons);

		try {
			when(masterService.getTaxExemptionReason((any(RequestInfo.class)),
					any(TaxExemptionReasonSearchCriteria.class))).thenReturn(exemptionReasonResponse);
			mockMvc.perform(post("/property/taxexemptionreason/_search").param("tenantId", "default")
					.param("code", "UPDATE-CODE").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("searchTaxExemptionReasonRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchTaxExemptionReasonResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}
}
