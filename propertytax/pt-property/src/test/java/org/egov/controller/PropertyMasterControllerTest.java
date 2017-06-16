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

import org.egov.models.AuditDetails;
import org.egov.models.Department;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.FloorType;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.OccuapancyMaster;
import org.egov.models.OccuapancyMasterRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyType;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.RoofType;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.StructureClass;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.models.UsageMaster;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.WallType;
import org.egov.models.WallTypeRequest;
import org.egov.models.WallTypeResponse;
import org.egov.models.WoodType;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;
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
@ContextConfiguration(classes = { PtPropertyApplication.class })
public class PropertyMasterControllerTest {

	@MockBean
	private Masterservice masterService;

	@Autowired
	private MockMvc mockMvc;

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
			when(masterService.updateFloorType(any(FloorTypeRequest.class), any(String.class), any(Integer.class)))
			.thenReturn(floorTypeResponse);

			mockMvc.perform(post("/property/floortypes/1/_update").param("tenantId", "1234")
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
			when(masterService.getFloorTypeMaster(any(RequestInfo.class), anyString(), any(Integer[].class),
					any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
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
		roofType.setCode("MRT");
		roofType.setName("Mansard Roof Type");
		roofType.setNameLocal("Mansard");
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
		roofType.setCode("GRT");
		roofType.setName("Gambrel Roof Type");
		roofType.setNameLocal("Gambrel");
		roofType.setDescription("Imported from USA");

		AuditDetails auditDetails = new AuditDetails();
		roofType.setAuditDetails(auditDetails);
		roofTypes.add(roofType);

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();

		roofTypeResponse.setResponseInfo(new ResponseInfo());
		roofTypeResponse.setRoofTypes(roofTypes);

		try {
			when(masterService.updateRoofType(any(RoofTypeRequest.class), any(String.class), any(Integer.class)))
			.thenReturn(roofTypeResponse);

			mockMvc.perform(post("/property/rooftypes/1/_update").param("tenantId", "1234")
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
			when(masterService.getRoofypes(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
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
		woodType.setCode("MWT");
		woodType.setName("Maple Wood Type");
		woodType.setNameLocal("Maple");
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
		woodType.setCode("WWT");
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
			when(masterService.updateWoodType(any(WoodTypeRequest.class), any(String.class), any(Integer.class)))
			.thenReturn(woodTypeResponse);

			mockMvc.perform(post("/property/woodtypes/1/_update").param("tenantId", "1237")
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
		woodType.setCode("WWT");
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
			when(masterService.getWoodTypes(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
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
		department.setCode("009");
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
			when(masterService.updateDepartmentMaster(any(String.class), any(Long.class), any(DepartmentRequest.class)))
			.thenReturn(departmentResponse);

			mockMvc.perform(post("/property/departments/1/_update").param("tenantId", "default")
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
			when(masterService.getDepartmentMaster(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(String.class), any(String.class), any(String.class), any(Integer.class),
					any(Integer.class))).thenReturn(departmentResponse);

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
			when(masterService.updateOccuapancyMaster(any(String.class), any(Long.class),
					any(OccuapancyMasterRequest.class))).thenReturn(occuapancyResponse);

			mockMvc.perform(post("/property/occuapancies/1/_update").param("tenantId", "default")
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
			when(masterService.getOccuapancyMaster(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(String.class), any(String.class), any(Boolean.class), any(Integer.class),
					any(Integer.class), any(Integer.class))).thenReturn(occuapancyResponse);

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
		propertyType.setCode("007");
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
			when(masterService.updatePropertyTypeMaster(any(String.class), any(Long.class),
					any(PropertyTypeRequest.class))).thenReturn(propertyTypeResponse);
			mockMvc.perform(post("/property/propertytypes/1/_update").param("tenantId", "default")
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
			when(masterService.getPropertyTypeMaster(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(String.class), any(String.class), any(Boolean.class), any(Integer.class),
					any(Integer.class), any(Integer.class))).thenReturn(propertyTypeResponse);

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
			
			when(masterService
				.getStructureClassMaster(
					any(RequestInfo.class), 
					any(String.class), 
					any(Integer[].class), 
					any(String.class), 
					any(String.class), 
					any(String.class),
					any(Boolean.class),
					any(Integer.class),
					any(Integer.class),
					any(Integer.class)))
				.thenReturn(structureClassResponse);
			
			mockMvc.perform(post("/property/structureclasses/_search")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("structureclassSearchRequest.json")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json(getFileContents("structureclassSearchResponse.json")));
			
		} catch (Exception e) {
			
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		
		assertTrue(Boolean.TRUE);
		
	}
	
	@Test
	public void testCreateStructureClasses() throws Exception {
		
		List<StructureClass> structureClasses = new ArrayList<>();
		StructureClass structureClass = new StructureClass();
		structureClass.setTenantId("default");
		
		AuditDetails auditDetails = new AuditDetails();
		structureClass.setAuditDetails(auditDetails);
		
		StructureClassResponse structureClassResponse = new StructureClassResponse();
		structureClasses.add(structureClass);
		
		structureClassResponse.setResponseInfo(new ResponseInfo());
		structureClassResponse.setStructureClasses(structureClasses);

		try {
			
			when(masterService
				.craeateStructureClassMaster(
					any(String.class), 
					any(StructureClassRequest.class)))
				.thenReturn(structureClassResponse);


			mockMvc.perform(post("/property/structureclasses/_create")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("structureclassCreateRequest.json")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json(getFileContents("structureclassCreateResponse.json")));

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
		
		AuditDetails auditDetails = new AuditDetails();
		structureClass.setAuditDetails(auditDetails);
		
		structureClasses.add(structureClass);
		
		structureClassResponse.setResponseInfo(new ResponseInfo());
		structureClassResponse.setStructureClasses(structureClasses);
		
		try {
			
			when(masterService
			.updateStructureClassMaster(
				any(String.class), 
				any(Long.class), 
				any(StructureClassRequest.class)))
			.thenReturn(structureClassResponse);
			mockMvc.perform(post("/property/structureclasses/1/_update")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("structureClassUpdateRequest.json")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json(getFileContents("structureClassUpdateResponse.json")));
			
			
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
			
			when(masterService
				.getUsageMaster(
					any(RequestInfo.class), 
					any(String.class), 
					any(Integer[].class), 
					any(String.class), 
					any(String.class), 
					any(String.class),
					any(Boolean.class),
					any(Boolean.class),
					any(Integer.class),
					any(Integer.class),
					any(Integer.class)))
				.thenReturn(usageMasterResponse);
			
			mockMvc.perform(post("/property/usages/_search")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("usageMasterSearchRequest.json")))
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

		AuditDetails auditDetails = new AuditDetails();
		usageMaster.setAuditDetails(auditDetails);

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();
		usageMasters.add(usageMaster);

		usageMasterResponse.setResponseInfo(new ResponseInfo());
		usageMasterResponse.setUsageMasters(usageMasters);

		try {
			
			when(masterService
				.createUsageMaster(
					any(String.class), 
					any(UsageMasterRequest.class)))
				.thenReturn(usageMasterResponse);


			mockMvc.perform(post("/property/usages/_create")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("usageMasterCreateRequest.json")))
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
			when(masterService
			.updateUsageMaster(
				any(String.class), 
				any(Long.class), 
				any(UsageMasterRequest.class)))
			.thenReturn(usageMasterResponse);
			
			
			mockMvc.perform(post("/property/usages/19/_update")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("usageMaterUpdateRequest.json")))
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
			
			when(masterService
				.getWallTypeMaster(
					any(RequestInfo.class), 
					any(String.class), 
					any(Integer[].class), 
					any(String.class), 
					any(String.class), 
					any(String.class),
					any(Integer.class),
					any(Integer.class)))
				.thenReturn(wallTypeResponse);
			
			mockMvc.perform(post("/property/walltypes/_search")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("wallTypeSearchRequest.json")))
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
			
			when(masterService
				.createWallTypeMaster(
					any(String.class), 
					any(WallTypeRequest.class)))
				.thenReturn(wallTypeResponse);


			mockMvc.perform(post("/property/walltypes/_create")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("wallTypeCreateRequest.json")))
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
		wallType.setTenantId("default");
		
		AuditDetails auditDetails = new AuditDetails();
		wallType.setAuditDetails(auditDetails);
		
		wallTypes.add(wallType);
		
		wallTypeResponse.setResponseInfo(new ResponseInfo());
		wallTypeResponse.setWallTypes(wallTypes);
		
		try {
		
			when(masterService
			.updateWallTypeMaster(
				any(String.class), 
				any(Long.class), 
				any(WallTypeRequest.class)))
			.thenReturn(wallTypeResponse);
			
			
			mockMvc.perform(post("/property/walltypes/1/_update")
					.param("tenantId","default")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("wallTypeUpdateRequest.json")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().json(getFileContents("wallTypeUpdateResponse.json")));
			
			
		} catch (Exception e) {
			
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		
		assertTrue(Boolean.TRUE);
		
	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}

}
