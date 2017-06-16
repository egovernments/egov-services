package org.egov.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
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
import org.egov.models.RequestInfoWrapper;
import org.egov.models.RoofType;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.UsageMaster;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.WoodType;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;
import org.egov.property.PtPropertyApplication;
import org.egov.property.services.Masterservice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { PtPropertyApplication.class })
public class PropertyServiceTest {

	@Autowired
	Masterservice masterService;

	@Autowired
	Environment environment;

	public Integer floorId = 1;
	public Integer roofId = 1;
	public Integer woodId = 1;
	public Long departmentId = 1l;
	public Long occupancyId = 1l;
	public Integer structureId = 1;
	public Integer usageId = 1;
	public Integer wallTypeId = 1;
	public Long propertyTypeId = 1l;

	@Test
	public void createRoofType() {
		String tenantId = "123";
		RequestInfo requestInfo = getRequestInfoObject();

		List<RoofType> roofTypes = new ArrayList<>();

		RoofType roofType = new RoofType();
		roofType.setTenantId("1234");
		roofType.setName("Gambrel Roof");
		roofType.setCode("256");
		roofType.setNameLocal("Gambrel");
		roofType.setDescription("Imported from Kurnool");
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("prasad");
		auditDetails.setLastModifiedBy("prasad");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		roofType.setAuditDetails(auditDetails);
		roofTypes.add(roofType);

		RoofTypeRequest roofTypeRequest = new RoofTypeRequest();
		roofTypeRequest.setRoofTypes(roofTypes);
		roofTypeRequest.setRequestInfo(requestInfo);

		try {
			RoofTypeResponse roofTypeResponse = masterService.createRoofype(roofTypeRequest, tenantId);
			if (roofTypeResponse.getRoofTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void updateRoofType() {
		String tenantId = "123";
		RequestInfo requestInfo = getRequestInfoObject();
		List<RoofType> roofTypes = new ArrayList<>();

		RoofType roofType = new RoofType();
		roofType.setTenantId("1234");
		roofType.setName("Mansard  Roof");
		roofType.setCode("256");
		roofType.setNameLocal("Mansard");
		roofType.setDescription("Imported from Kurnool");
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("prasad");
		auditDetails.setLastModifiedBy("prasad");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		roofType.setAuditDetails(auditDetails);
		roofTypes.add(roofType);

		RoofTypeRequest roofTypeRequest = new RoofTypeRequest();
		roofTypeRequest.setRoofTypes(roofTypes);
		roofTypeRequest.setRequestInfo(requestInfo);

		try {
			RoofTypeResponse roofTypeResponse = masterService.updateRoofType(roofTypeRequest, tenantId, roofId);

			if (roofTypeResponse.getRoofTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void searchRoofType() {

		String tenantId = "1234";
		String name = "Mansard  Roof";
		String code = "256";
		String nameLocal = "Mansard";
		Integer pageSize = Integer.valueOf(environment.getProperty("default.page.size").trim());
		Integer offset = Integer.valueOf(environment.getProperty("default.offset"));
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			RoofTypeResponse roofTypeResponse = masterService.getRoofypes(requestInfo, tenantId,
					new Integer[] { roofId }, name, code, nameLocal, pageSize, offset);
			if (roofTypeResponse.getRoofTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void createWoodType() {

		String tenantId = "1234";
		RequestInfo requestInfo = getRequestInfoObject();
		List<WoodType> woodTypes = new ArrayList<>();
		WoodType woodType = new WoodType();
		woodType.setTenantId("1234");
		woodType.setName("Walnut Wood Type");
		woodType.setCode("256");
		woodType.setNameLocal("Walnut");
		woodType.setDescription("Imported from Kurnool");
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("prasad");
		auditDetails.setLastModifiedBy("prasad");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		woodType.setAuditDetails(auditDetails);
		woodTypes.add(woodType);

		WoodTypeRequest woodTypeRequest = new WoodTypeRequest();
		woodTypeRequest.setWoodTypes(woodTypes);
		woodTypeRequest.setRequestInfo(requestInfo);

		try {
			WoodTypeResponse woodTypeResponse = masterService.createWoodType(woodTypeRequest, tenantId);

			if (woodTypeResponse.getWoodTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void searchWoodType() {

		String tenantId = "1234";
		String name = "Maple Wood Type";
		String code = "256";
		String nameLocal = "Maple";
		Integer pageSize = Integer.valueOf(environment.getProperty("default.page.size").trim());
		Integer offset = Integer.valueOf(environment.getProperty("default.offset"));
		RequestInfo requestInfo = getRequestInfoObject();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		try {
			WoodTypeResponse woodTypeResponse = masterService.getWoodTypes(requestInfo, tenantId,
					new Integer[] { woodId }, name, code, nameLocal, pageSize, offset);

			if (woodTypeResponse.getWoodTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void updateWoodType() {

		String tenantId = "1234";
		RequestInfo requestInfo = getRequestInfoObject();

		List<WoodType> woodTypes = new ArrayList<>();

		WoodType woodType = new WoodType();
		woodType.setTenantId("1234");
		woodType.setName("Maple Wood Type");
		woodType.setCode("256");
		woodType.setNameLocal("Maple");
		woodType.setDescription("Imported from Kurnool");
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("prasad");
		auditDetails.setLastModifiedBy("prasad");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		woodType.setAuditDetails(auditDetails);
		woodTypes.add(woodType);

		WoodTypeRequest woodTypeRequest = new WoodTypeRequest();
		woodTypeRequest.setWoodTypes(woodTypes);
		woodTypeRequest.setRequestInfo(requestInfo);

		try {
			WoodTypeResponse woodTypeResponse = masterService.updateWoodType(woodTypeRequest, tenantId, woodId);

			if (woodTypeResponse.getWoodTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void createFloorTypeTest() {

		String tenantId = "123";
		RequestInfo requestInfo = getRequestInfoObject();
		List<FloorType> floorTypes = new ArrayList<>();

		FloorType floorType = new FloorType();
		floorType.setTenantId("1234");
		floorType.setName("Stone Flooring");
		floorType.setCode("256");
		floorType.setNameLocal("Stone");
		floorType.setDescription("Imported from Kurnool");
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("prasad");
		auditDetails.setLastModifiedBy("prasad");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		floorType.setAuditDetails(auditDetails);
		floorTypes.add(floorType);

		FloorTypeRequest floorTypeRequest = new FloorTypeRequest();
		floorTypeRequest.setFloorTypes(floorTypes);
		floorTypeRequest.setRequestInfo(requestInfo);

		try {
			FloorTypeResponse floorTypeResponse = masterService.createFloorType(floorTypeRequest, tenantId);

			if (floorTypeResponse.getFloorTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);

		}

	}

	@Test
	public void searchFloorType() {

		String tenantId = "1234";
		String name = "Tile Flooring";
		String code = "256";
		String nameLocal = "Tile";
		Integer pageSize = Integer.valueOf(environment.getProperty("default.page.size").trim());
		Integer offset = Integer.valueOf(environment.getProperty("default.offset"));

		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			FloorTypeResponse floorTypeResponse = masterService.getFloorTypeMaster(requestInfo, tenantId,
					new Integer[] { floorId }, name, code, nameLocal, pageSize, offset);

			if (floorTypeResponse.getFloorTypes().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void updateFloorType() {
		String tenantId = "1234";
		RequestInfo requestInfo = getRequestInfoObject();
		List<FloorType> floorTypes = new ArrayList<>();

		FloorType floorType = new FloorType();
		floorType.setTenantId("1234");
		floorType.setName("Tile Flooring");
		floorType.setCode("256");
		floorType.setNameLocal("Tile");
		floorType.setDescription("Imported from Kurnool");

		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("prasad");
		auditDetails.setLastModifiedBy("prasad");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		floorType.setAuditDetails(auditDetails);
		floorTypes.add(floorType);

		FloorTypeRequest floorTypeRequest = new FloorTypeRequest();
		floorTypeRequest.setFloorTypes(floorTypes);
		floorTypeRequest.setRequestInfo(requestInfo);

		FloorTypeResponse floorTypeResponse = null;

		try {
			floorTypeResponse = masterService.updateFloorType(floorTypeRequest, tenantId, floorId);
			if (floorTypeResponse.getFloorTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : Test case for creating Occuapancy master
	 * 
	 * @param tenantId
	 * @param occuapancyRequest
	 * @throws Exception
	 */

	@Test
	public void createOccuapancyMaster() throws Exception {
		try {

			String tenantId = "default";

			List<OccuapancyMaster> occuapancyMaster = new ArrayList<OccuapancyMaster>();

			OccuapancyMaster master = new OccuapancyMaster();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("anil");
			auditDetails.setCreatedTime((long) 123456);
			auditDetails.setLastModifiedBy("anil");
			auditDetails.setLastModifiedTime((long) 123456);

			master.setTenantId("default");
			master.setName("Anil");
			master.setCode("testingcode");
			master.setNameLocal("kumar");
			master.setDescription("test for occuapancy");
			master.setActive(true);
			master.setOrderNumber(1);
			master.setAuditDetails(auditDetails);

			occuapancyMaster.add(master);

			OccuapancyMasterRequest occuapancyMasterRequest = new OccuapancyMasterRequest();
			occuapancyMasterRequest.setRequestInfo(getRequestInfoObject());
			occuapancyMasterRequest.setOccuapancyMasters(occuapancyMaster);

			OccuapancyMasterResponse occuapancyMasterResponse = masterService.createOccuapancyMaster(tenantId,
					occuapancyMasterRequest);
			if (occuapancyMasterResponse.getOccuapancyMasters().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * Description : Test case for updating occupancyType master api
	 * 
	 * @param tenantId
	 * @param id
	 * @param occuapancyRequest
	 * @throws Exception
	 */

	@Test
	public void updateOccuapancyMaster() throws Exception {
		try {

			String tenantId = "default";

			List<OccuapancyMaster> occuapancyMaster = new ArrayList<OccuapancyMaster>();

			OccuapancyMaster master = new OccuapancyMaster();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("anil");
			auditDetails.setCreatedTime((long) 123456);
			auditDetails.setLastModifiedBy("anil");
			auditDetails.setLastModifiedTime((long) 123456);

			master.setId((long) 4);
			master.setTenantId("default");
			master.setName("anil");
			master.setCode("testcode");
			master.setNameLocal("kumar");
			master.setDescription("test for occuapancy");
			master.setActive(true);
			master.setOrderNumber(1);

			master.setAuditDetails(auditDetails);

			occuapancyMaster.add(master);
			OccuapancyMasterRequest occuapancyMasterRequest = new OccuapancyMasterRequest();
			occuapancyMasterRequest.setRequestInfo(getRequestInfoObject());
			occuapancyMasterRequest.setOccuapancyMasters(occuapancyMaster);

			OccuapancyMasterResponse occuapancyMasterResponse = masterService.updateOccuapancyMaster(tenantId,
					occupancyId, occuapancyMasterRequest);
			if (occuapancyMasterResponse.getOccuapancyMasters().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * * Description : test case for searching occupancyType master api
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param active
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @throws Exception
	 */
	@Test
	public void searchOccuapancyMaster() throws Exception {
		try {

			String tenantId = "default";
			String name = "anil";
			String code = "testcode";
			String nameLocal = "kumar";
			Boolean active = true;
			Integer orderNumber = 1;
			Integer pageSize = Integer.valueOf(environment.getProperty("default.page.size").trim());
			Integer offset = Integer.valueOf(environment.getProperty("default.offset"));

			OccuapancyMasterResponse occuapancyMasterResponse = masterService.getOccuapancyMaster(
					getRequestInfoObject(), tenantId, new Integer[] { Integer.valueOf(occupancyId.toString()) }, name,
					code, nameLocal, active, orderNumber, pageSize, offset);
			if (occuapancyMasterResponse.getOccuapancyMasters().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * Description : Test case for creating propertytypes master
	 * 
	 * @param tenantId
	 * @param propertyTypeRequest
	 * @throws Exception
	 */

	@Test
	public void createPropertyTypeMaster() throws Exception {
		try {
			String tenantId = "default";

			List<PropertyType> propertyType = new ArrayList<PropertyType>();
			PropertyType master = new PropertyType();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("Anilkumar");
			auditDetails.setLastModifiedBy("Anilkumar");
			auditDetails.setCreatedTime((long) 564644560);
			auditDetails.setLastModifiedTime((long) 564644560);

			master.setTenantId("default");
			master.setName("Anilkumar");
			master.setCode("testcode");
			master.setNameLocal("kumar");
			master.setDescription("test for occuapancy");
			master.setActive(true);
			master.setOrderNumber(1);
			master.setAuditDetails(auditDetails);
			propertyType.add(master);

			PropertyTypeRequest propertyTypeRequest = new PropertyTypeRequest();
			propertyTypeRequest.setRequestInfo(getRequestInfoObject());
			propertyTypeRequest.setPropertyTypes(propertyType);

			PropertyTypeResponse propertyTypeResponse = masterService.createPropertyTypeMaster(tenantId,
					propertyTypeRequest);
			if (propertyTypeResponse.getPropertyTypes().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * Description : Test case for updating propertytype master api
	 * 
	 * @param tenantId
	 * @param id
	 * @param propertyTypeRequest
	 * @throws Exception
	 */

	@Test
	public void updatePropertyTypeMaster() throws Exception {
		try {

			String tenantId = "default";
			List<PropertyType> propertyType = new ArrayList<PropertyType>();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("Anil");
			auditDetails.setLastModifiedBy("Anil");
			auditDetails.setCreatedTime((long) 564644560);
			auditDetails.setLastModifiedTime((long) 564644560);

			PropertyType master = new PropertyType();
			master.setId((long) 1);
			master.setTenantId("default");
			master.setName("anil");
			master.setCode("testingcode");
			master.setNameLocal("kumar");
			master.setDescription("test for occuapancy");
			master.setActive(true);
			master.setOrderNumber(1);
			master.setAuditDetails(auditDetails);
			propertyType.add(master);

			PropertyTypeRequest propertyTypeRequest = new PropertyTypeRequest();
			propertyTypeRequest.setRequestInfo(getRequestInfoObject());
			propertyTypeRequest.setPropertyTypes(propertyType);

			PropertyTypeResponse propertyTypeResponse = masterService.updatePropertyTypeMaster(tenantId, propertyTypeId,
					propertyTypeRequest);
			if (propertyTypeResponse.getPropertyTypes().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * * Description : test case for searching propertytype master api
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param active
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @throws Exception
	 */
	@Test
	public void searchPropertyTypeMaster() throws Exception {
		try {

			String tenantId = "default";
			Integer[] ids = new Integer[] { Integer.valueOf(propertyTypeId.toString()) };
			String name = "anil";
			String code = "testingcode";
			String nameLocal = "kumar";
			Boolean active = true;
			Integer orderNumber = 1;
			Integer pageSize = Integer.valueOf(environment.getProperty("default.page.size").trim());
			Integer offset = Integer.valueOf(environment.getProperty("default.offset"));

			PropertyTypeResponse propertyTypeResponse = masterService.getPropertyTypeMaster(getRequestInfoObject(),
					tenantId, ids, name, code, nameLocal, active, orderNumber, pageSize, offset);
			if (propertyTypeResponse.getPropertyTypes().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * Description : Test case for creating department master
	 * 
	 * @param tenantId
	 * @param departmentRequest
	 * @throws Exception
	 */

	@Test
	public void createDepartmentMaster() throws Exception {
		try {

			String tenantId = "default";

			List<Department> department = new ArrayList<Department>();

			Department master = new Department();

			AuditDetails auditDetails = new AuditDetails();

			auditDetails.setCreatedBy("Anil");
			auditDetails.setLastModifiedBy("Anil");
			auditDetails.setCreatedTime((long) 564644560);
			auditDetails.setLastModifiedTime((long) 564644560);

			master.setTenantId("default");
			master.setCategory("software");
			master.setName("anil");
			master.setCode("testcode");
			master.setNameLocal("kumar");
			master.setDescription("test for occuapancy");
			master.setAuditDetails(auditDetails);
			department.add(master);

			DepartmentRequest departmentRequest = new DepartmentRequest();
			departmentRequest.setRequestInfo(getRequestInfoObject());
			departmentRequest.setDepartments(department);
			DepartmentResponseInfo departmentResponse = masterService.createDepartmentMaster(tenantId,
					departmentRequest);
			if (departmentResponse.getDepartments().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * Description : Test case for updating department master api
	 * 
	 * @param tenantId
	 * @param id
	 * @param departmentRequest
	 * @throws Exception
	 */

	@Test
	public void updateDepartmentMaster() throws Exception {
		try {

			String tenantId = "default";
			List<Department> department = new ArrayList<Department>();
			Department master = new Department();
			AuditDetails auditDetails = new AuditDetails();

			auditDetails.setCreatedBy("Anil");
			auditDetails.setLastModifiedBy("Anil");
			auditDetails.setCreatedTime((long) 564644560);
			auditDetails.setLastModifiedTime((long) 564644560);

			master.setId((long) 1);
			master.setTenantId("default");
			master.setCategory("software engineer");
			master.setName("anil");
			master.setCode("testcode");
			master.setNameLocal("kumar");
			master.setDescription("test for occuapancy");
			master.setAuditDetails(auditDetails);
			department.add(master);
			DepartmentRequest departmentRequest = new DepartmentRequest();
			departmentRequest.setRequestInfo(getRequestInfoObject());
			departmentRequest.setDepartments(department);

			DepartmentResponseInfo departmentResponse = masterService.updateDepartmentMaster(tenantId, departmentId,
					departmentRequest);
			if (departmentResponse.getDepartments().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * * Description : test case for searching department master api
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param category
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @throws Exception
	 */
	@Test
	public void searchDepartmentMaster() throws Exception {
		try {

			String tenantId = "default";
			Integer[] ids = new Integer[] { Integer.valueOf(departmentId.toString()) };
			String category = "software engineer";
			String name = "anil";
			String code = "testcode";
			String nameLocal = "kumar";
			Integer pageSize = Integer.valueOf(environment.getProperty("default.page.size").trim());
			Integer offset = Integer.valueOf(environment.getProperty("default.offset"));

			DepartmentResponseInfo departmentResponse = masterService.getDepartmentMaster(getRequestInfoObject(),
					tenantId, ids, category, name, code, nameLocal, pageSize, offset);
			if (departmentResponse.getDepartments().size() == 0) {
				assertTrue(false);
			}
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void createUsageMasterTest() {

		String tenantId = "default";

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("emp");
		requestInfo.setVer("1.0");
		requestInfo.setTs(new Long(122366));
		requestInfo.setDid("1");
		requestInfo.setKey("yyyykey");
		requestInfo.setMsgId("20170310130900");
		requestInfo.setRequesterId("yosadhara");
		requestInfo.setAuthToken("b5da31a4-b400-4d6e-aa46-9ebf33cce933");

		List<UsageMaster> usageMasters = new ArrayList<>();

		UsageMaster usageMaster = new UsageMaster();
		usageMaster.setTenantId("default");
		usageMaster.setName("Yoyo");
		usageMaster.setCode("1234");
		usageMaster.setNameLocal("test_namelocal");
		usageMaster.setDescription("test_description");

		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("yosadhara");
		auditDetails.setLastModifiedBy("yosadhara");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		usageMaster.setAuditDetails(auditDetails);
		usageMasters.add(usageMaster);

		UsageMasterRequest usageMasterRequest = new UsageMasterRequest();
		usageMasterRequest.setUsageMasters(usageMasters);
		usageMasterRequest.setRequestInfo(requestInfo);

		try {
			UsageMasterResponse response = masterService.createUsageMaster(tenantId, usageMasterRequest);

			if (response.getUsageMasters().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void updateUsageMasterTest() throws Exception {

		String tenantId = "default";
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("emp");
		requestInfo.setVer("1.0");
		requestInfo.setTs(new Long(122366));
		requestInfo.setDid("1");
		requestInfo.setKey("abcdkey");
		requestInfo.setMsgId("20170310130900");
		requestInfo.setRequesterId("yosadhara");
		requestInfo.setAuthToken("b5da31a4-b400-4d6e-aa46-9ebf33cce933");

		List<UsageMaster> usageMasters = new ArrayList<>();

		UsageMaster usageMaster = new UsageMaster();
		usageMaster.setTenantId("default");
		usageMaster.setName("Yoyo");
		usageMaster.setCode("1234");
		usageMaster.setNameLocal("update_namelocal");
		usageMaster.setDescription("update_description");

		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("yosadhara");
		auditDetails.setLastModifiedBy("yosadhara");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		usageMaster.setAuditDetails(auditDetails);
		usageMasters.add(usageMaster);

		UsageMasterRequest usageMasterRequest = new UsageMasterRequest();
		usageMasterRequest.setUsageMasters(usageMasters);
		usageMasterRequest.setRequestInfo(requestInfo);

		UsageMasterResponse usageMasterResponse = masterService.updateUsageMaster(tenantId, Long.valueOf(usageId),
				usageMasterRequest);

		if (usageMasterResponse.getUsageMasters().size() == 0)
			assertTrue(false);

		assertTrue(true);
	}



	private RequestInfo getRequestInfoObject() {
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
