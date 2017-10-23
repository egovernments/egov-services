package org.egov.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.enums.ApplicationEnum;
import org.egov.enums.ChannelEnum;
import org.egov.enums.CreationReasonEnum;
import org.egov.enums.SourceEnum;
import org.egov.enums.StatusEnum;
import org.egov.enums.UnitTypeEnum;
import org.egov.models.Address;
import org.egov.models.AppConfiguration;
import org.egov.models.AppConfigurationRequest;
import org.egov.models.AppConfigurationResponse;
import org.egov.models.AppConfigurationSearchCriteria;
import org.egov.models.AuditDetails;
import org.egov.models.Boundary;
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
import org.egov.models.Document;
import org.egov.models.DocumentType;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.DocumentTypeSearchCriteria;
import org.egov.models.Floor;
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
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyType;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.PropertyTypeSearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.Role;
import org.egov.models.RoofType;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.RoofTypeSearchCriteria;
import org.egov.models.TaxExemptionReason;
import org.egov.models.TaxExemptionReasonRequest;
import org.egov.models.TaxExemptionReasonResponse;
import org.egov.models.TaxExemptionReasonSearchCriteria;
import org.egov.models.Unit;
import org.egov.models.UsageMaster;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.User;
import org.egov.models.UserInfo;
import org.egov.models.VacantLandDetail;
import org.egov.models.WallType;
import org.egov.models.WallTypeRequest;
import org.egov.models.WallTypeResponse;
import org.egov.models.WallTypeSearchCriteria;
import org.egov.models.WoodType;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;
import org.egov.models.WoodTypeSearchCriteria;
import org.egov.models.WorkFlowDetails;
import org.egov.property.PtPropertyApplication;
import org.egov.property.config.PropertiesManager;
import org.egov.property.services.Masterservice;
import org.egov.property.services.PersisterService;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { PtPropertyApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-access")
public class PropertyServiceTest {

	@Autowired
	Masterservice masterService;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	PersisterService persisterService;

	public Long floorId = 1l;
	public Long roofId = 1l;
	public Long woodId = 1l;
	public Long departmentId = 1l;
	public Long occupancyId = 1l;
	public Integer structureId = 1;
	public Long usageId = 1l;
	public Integer wallTypeId = 1;
	public Long propertyTypeId = 1l;
	public Long demolitionId = 1l;

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
	public void modifyRoofType() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<RoofType> roofTypes = new ArrayList<>();

		RoofType roofType = new RoofType();
		roofType.setId(roofId);
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
			RoofTypeResponse roofTypeResponse = masterService.updateRoofType(roofTypeRequest);

			if (roofTypeResponse.getRoofTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void searchRoofType() {
		Integer[] ids = new Integer[] { roofId.intValue() };
		RoofTypeSearchCriteria roofTypeSearchCriteria = new RoofTypeSearchCriteria();
		roofTypeSearchCriteria.setTenantId("1234");
		roofTypeSearchCriteria.setIds(ids);
		roofTypeSearchCriteria.setName("Mansard  Roof");
		roofTypeSearchCriteria.setCode("256");
		roofTypeSearchCriteria.setNameLocal("Mansard");
		roofTypeSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));
		roofTypeSearchCriteria.setOffSet(Integer.valueOf(propertiesManager.getDefaultOffset()));
		RequestInfo requestInfo = getRequestInfoObject();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			RoofTypeResponse roofTypeResponse = masterService.getRoofypes(requestInfo, roofTypeSearchCriteria);
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
	public void modifyWoodType() {

		RequestInfo requestInfo = getRequestInfoObject();

		List<WoodType> woodTypes = new ArrayList<>();

		WoodType woodType = new WoodType();
		woodType.setId(woodId);
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
			WoodTypeResponse woodTypeResponse = masterService.updateWoodType(woodTypeRequest);

			if (woodTypeResponse.getWoodTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void searchWoodType() {
		Integer[] ids = new Integer[] { woodId.intValue() };
		WoodTypeSearchCriteria woodTypeSearchCriteria = new WoodTypeSearchCriteria();
		woodTypeSearchCriteria.setTenantId("1234");
		woodTypeSearchCriteria.setIds(ids);
		woodTypeSearchCriteria.setName("Maple Wood Type");
		woodTypeSearchCriteria.setCode("256");
		woodTypeSearchCriteria.setNameLocal("Maple");
		woodTypeSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));
		woodTypeSearchCriteria.setOffSet(Integer.valueOf(propertiesManager.getDefaultOffset()));
		RequestInfo requestInfo = getRequestInfoObject();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		try {
			WoodTypeResponse woodTypeResponse = masterService.getWoodTypes(requestInfo, woodTypeSearchCriteria);

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
	public void modifyFloorType() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<FloorType> floorTypes = new ArrayList<>();

		FloorType floorType = new FloorType();
		floorType.setId(floorId);
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
			floorTypeResponse = masterService.updateFloorType(floorTypeRequest);
			if (floorTypeResponse.getFloorTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void searchFloorType() {
		Integer[] ids = new Integer[] { floorId.intValue() };
		FloorTypeSearchCriteria floorTypeSearchCriteria = new FloorTypeSearchCriteria();
		floorTypeSearchCriteria.setTenantId("1234");
		floorTypeSearchCriteria.setIds(ids);
		floorTypeSearchCriteria.setName("Tile Flooring");
		floorTypeSearchCriteria.setCode("256");
		floorTypeSearchCriteria.setNameLocal("Tile");
		floorTypeSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));
		floorTypeSearchCriteria.setOffSet(Integer.valueOf(propertiesManager.getDefaultOffset()));
		RequestInfo requestInfo = getRequestInfoObject();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			FloorTypeResponse floorTypeResponse = masterService.getFloorTypeMaster(requestInfo,
					floorTypeSearchCriteria);

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
	public void modifyOccuapancyMaster() throws Exception {
		try {

			List<OccuapancyMaster> occuapancyMaster = new ArrayList<OccuapancyMaster>();

			OccuapancyMaster master = new OccuapancyMaster();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("anil");
			auditDetails.setCreatedTime((long) 123456);
			auditDetails.setLastModifiedBy("anil");
			auditDetails.setLastModifiedTime((long) 123456);

			// master.setId((long) 4);
			master.setId(occupancyId);
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

			OccuapancyMasterResponse occuapancyMasterResponse = masterService
					.updateOccuapancyMaster(occuapancyMasterRequest);
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

			OccuapancyMasterSearchCriteria occuapancyMasterSearchCriteria = new OccuapancyMasterSearchCriteria();
			occuapancyMasterSearchCriteria.setTenantId("default");
			occuapancyMasterSearchCriteria.setName("anil");
			occuapancyMasterSearchCriteria.setCode("testcode");
			occuapancyMasterSearchCriteria.setNameLocal("kumar");
			occuapancyMasterSearchCriteria.setActive(true);
			occuapancyMasterSearchCriteria.setOrderNumber(1);
			occuapancyMasterSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));
			occuapancyMasterSearchCriteria.setOffSet(Integer.valueOf(propertiesManager.getDefaultOffset()));

			OccuapancyMasterResponse occuapancyMasterResponse = masterService
					.getOccuapancyMaster(getRequestInfoObject(), occuapancyMasterSearchCriteria);
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
	public void modifyPropertyTypeMaster() throws Exception {
		try {

			List<PropertyType> propertyType = new ArrayList<PropertyType>();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("Anil");
			auditDetails.setLastModifiedBy("Anil");
			auditDetails.setCreatedTime((long) 564644560);
			auditDetails.setLastModifiedTime((long) 564644560);

			PropertyType master = new PropertyType();
			master.setId(propertyTypeId);
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

			PropertyTypeResponse propertyTypeResponse = masterService.updatePropertyTypeMaster(propertyTypeRequest);
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
	 * @param propertyTypeSearchCriteria
	 * @throws Exception
	 */
	@Test
	public void searchPropertyTypeMaster() throws Exception {
		try {

			Integer[] ids = new Integer[] { Integer.valueOf(propertyTypeId.toString()) };
			PropertyTypeSearchCriteria propertyTypeSearchCriteria = new PropertyTypeSearchCriteria();
			propertyTypeSearchCriteria.setTenantId("default");
			propertyTypeSearchCriteria.setIds(ids);
			propertyTypeSearchCriteria.setName("anil");
			propertyTypeSearchCriteria.setCode("testingcode");
			propertyTypeSearchCriteria.setNameLocal("kumar");
			propertyTypeSearchCriteria.setActive(true);
			propertyTypeSearchCriteria.setOrderNumber(1);
			propertyTypeSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));
			propertyTypeSearchCriteria.setOffSet(Integer.valueOf(propertiesManager.getDefaultOffset()));
			propertyTypeSearchCriteria.setParent(null);

			PropertyTypeResponse propertyTypeResponse = masterService.getPropertyTypeMaster(getRequestInfoObject(),
					propertyTypeSearchCriteria);
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
	public void modifyDepartmentMaster() throws Exception {
		try {

			List<Department> department = new ArrayList<Department>();
			Department master = new Department();
			AuditDetails auditDetails = new AuditDetails();

			auditDetails.setCreatedBy("Anil");
			auditDetails.setLastModifiedBy("Anil");
			auditDetails.setCreatedTime((long) 564644560);
			auditDetails.setLastModifiedTime((long) 564644560);

			master.setId(departmentId);
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

			DepartmentResponseInfo departmentResponse = masterService.updateDepartmentMaster(departmentRequest);
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
	 * @param departmentSearchCriteria
	 * @throws Exception
	 */
	@Test
	public void searchDepartmentMaster() throws Exception {
		try {
			Integer[] ids = new Integer[] { Integer.valueOf(departmentId.toString()) };
			DepartmentSearchCriteria departmentSearchCriteria = new DepartmentSearchCriteria();
			departmentSearchCriteria.setTenantId("default");
			departmentSearchCriteria.setIds(ids);
			departmentSearchCriteria.setCategory("software engineer");
			departmentSearchCriteria.setName("anil");
			departmentSearchCriteria.setCode("testcode");
			departmentSearchCriteria.setNameLocal("kumar");
			departmentSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));
			departmentSearchCriteria.setOffSet(Integer.valueOf(propertiesManager.getDefaultOffset()));

			DepartmentResponseInfo departmentResponse = masterService.getDepartmentMaster(getRequestInfoObject(),
					departmentSearchCriteria);
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

		try {
			String tenantId = "default";

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
			usageMasterRequest.setRequestInfo(getRequestInfoObject());

			UsageMasterResponse response = masterService.createUsageMaster(tenantId, usageMasterRequest);

			if (response.getUsageMasters().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void modifyUsageMasterTest() throws Exception {
		try {
			List<UsageMaster> usageMasters = new ArrayList<>();

			UsageMaster usageMaster = new UsageMaster();
			usageMaster.setId(usageId);
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
			usageMasterRequest.setRequestInfo(getRequestInfoObject());

			UsageMasterResponse usageMasterResponse = masterService.updateUsageMaster(usageMasterRequest);

			if (usageMasterResponse.getUsageMasters().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception ex) {
			assert (false);
		}
	}

	@Test
	public void createWallTypeTest() {
		try {
			String tenantId = "default";
			List<WallType> wallTypes = new ArrayList<>();
			WallType wallType = new WallType();
			wallType.setTenantId("default");
			wallType.setName("Yoyo");
			wallType.setCode("1234");
			wallType.setNameLocal("test_namelocal");
			wallType.setDescription("test_description");
			long createdTime = new Date().getTime();
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("yosadhara");
			auditDetails.setLastModifiedBy("yosadhara");
			auditDetails.setCreatedTime(createdTime);
			auditDetails.setLastModifiedTime(createdTime);
			wallType.setAuditDetails(auditDetails);
			wallTypes.add(wallType);
			WallTypeRequest wallTypeRequest = new WallTypeRequest();
			wallTypeRequest.setWallTypes(wallTypes);
			wallTypeRequest.setRequestInfo(getRequestInfoObject());

			WallTypeResponse wallTypeResponse;

			wallTypeResponse = masterService.createWallTypeMaster(tenantId, wallTypeRequest);

			if (wallTypeResponse.getWallTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void modifyWallTypeTest() {

		try {
			long id = 1;

			List<WallType> wallTypes = new ArrayList<>();

			WallType wallType = new WallType();
			wallType.setId(id);
			wallType.setTenantId("default");
			wallType.setName("Yoyo");
			wallType.setCode("1234");
			wallType.setNameLocal("test_namelocal");
			wallType.setDescription("test_description");

			long createdTime = new Date().getTime();

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("yosadhara");
			auditDetails.setLastModifiedBy("yosadhara");
			auditDetails.setCreatedTime(createdTime);
			auditDetails.setLastModifiedTime(createdTime);

			wallType.setAuditDetails(auditDetails);
			wallTypes.add(wallType);

			WallTypeRequest wallTypeRequest = new WallTypeRequest();
			wallTypeRequest.setWallTypes(wallTypes);
			wallTypeRequest.setRequestInfo(getRequestInfoObject());
			WallTypeResponse wallTypeResponse = masterService.updateWallTypeMaster(wallTypeRequest);
			if (wallTypeResponse.getWallTypes().size() == 0)
				assertTrue(false);
			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void searchWallTypeTest() {
		try {
			Integer[] ids = new Integer[] { 1 };
			WallTypeSearchCriteria wallTypeSearchCriteria = new WallTypeSearchCriteria();
			wallTypeSearchCriteria.setTenantId("default");
			wallTypeSearchCriteria.setIds(ids);
			wallTypeSearchCriteria.setName("Yoyo");
			wallTypeSearchCriteria.setCode("1234");
			wallTypeSearchCriteria.setNameLocal("test_namelocal");
			wallTypeSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));
			wallTypeSearchCriteria.setOffSet(Integer.valueOf(propertiesManager.getDefaultOffset()));
			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper.setRequestInfo(getRequestInfoObject());

			WallTypeResponse wallTypeResponse = masterService.getWallTypeMaster(getRequestInfoObject(),
					wallTypeSearchCriteria);
			if (wallTypeResponse.getWallTypes().size() == 0)
				assertTrue(false);

			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

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
		requestInfo.setAuthToken("a78edc28-1a5f-422e-91c6-19cae574c272");
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		requestInfo.setUserInfo(userInfo);
		return requestInfo;
	}

	@Test
	public void testCreatingProperty() throws Exception {
		try {
			PropertyRequest propertyRequest = new PropertyRequest();
			List<Property> properties = new ArrayList<Property>();

			Property property = new Property();

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("Anil");
			auditDetails.setLastModifiedBy("Anil");
			auditDetails.setCreatedTime((long) 564644560);
			auditDetails.setLastModifiedTime((long) 564644560);

			property.setTenantId("default");
			property.setUpicNumber("test123");
			property.setVltUpicNumber("vltUpicNumber1");
			property.setCreationReason(CreationReasonEnum.NEWPROPERTY.valueOf("NEWPROPERTY"));

			Address address = new Address();
			address.setTenantId("default");
			address.setLatitude((double) 11);
			address.setLongitude((double) 20);
			address.setAddressNumber("1-2-3");
			address.setAddressLine1("acgaurds");
			address.setAddressLine2("khirathabad");
			address.setLandmark("noori");
			address.setCity("hyderabad");
			address.setPincode("500004");
			address.setDetail("Testing");

			address.setAuditDetails(auditDetails);

			property.setAddress(address);

			List<User> owners = new ArrayList<User>();
			User owner = new User();
			owner.setTenantId("default");
			owner.setUserName("Anil");
			owner.setSalutation("testing");
			owner.setName("anil");
			owner.setGender("male");
			owner.setMobileNumber("9333555666");
			owner.setEmailId("anil@wtc.com");
			owner.setAadhaarNumber("123456789123");
			owner.setActive(true);
			owner.setLocale("no");
			owner.setType("house");
			owner.setAccountLocked(true);
			property.getOwners().add(owner);
			List<Role> roles = new ArrayList<Role>();

			Role role = new Role();

			role.setName("kumar");
			role.setDescription("Testing");
			owner.getRoles().add(role);
			owner.setRoles(roles);

			owner.setAuditDetails(auditDetails);

			owner.setDob("25/09/1989");
			owner.setAltContactNumber("9874562134");
			owner.setFatherOrHusbandName("not applicable");
			owner.setBloodGroup("O+");
			owner.setPan("stvt5854k");
			owner.setPermanentAddress("eluru");
			owner.setPermanentCity("eluru");
			owner.setPermanentPincode("534001");
			owner.setCorrespondenceCity("Hyderabad");
			owner.setCorrespondencePincode("500004");
			owner.setCorrespondenceAddress("Khirathabad");
			owner.setSignature("S Anilkumar");
			owner.setIdentificationMark("mole on right hand");
			owner.setPhoto("anil.png");

			owner.setIsPrimaryOwner(true);
			owner.setIsSecondaryOwner(true);
			owner.setOwnerShipPercentage((double) 10);
			owner.setOwnerType("Sandrapati Anilkumar");

			property.setOwners(owners);

			PropertyDetail propertyDetail = new PropertyDetail();
			propertyDetail.setSource(SourceEnum.MUNICIPAL_RECORDS.valueOf("MUNICIPAL_RECORDS"));
			propertyDetail.setRegdDocNo("regdoc1");
			propertyDetail.setRegdDocDate("25/05/2017");
			propertyDetail.setReason("testing");
			propertyDetail.setStatus(StatusEnum.ACTIVE.valueOf("ACTIVE"));
			propertyDetail.setIsVerified(true);
			propertyDetail.setVerificationDate("25/05/2017");
			propertyDetail.setIsExempted(true);
			propertyDetail.setExemptionReason("Testing");
			propertyDetail.setPropertyType("Land");
			propertyDetail.setCategory("Land");
			propertyDetail.setUsage("Anil");
			propertyDetail.setDepartment("Land department");
			propertyDetail.setApartment("apartment");
			propertyDetail.setSiteLength((double) 10);
			propertyDetail.setSiteBreadth((double) 15);
			propertyDetail.setSitalArea((double) 10);
			propertyDetail.setTotalBuiltupArea((double) 20);
			propertyDetail.setUndividedShare((double) 5);
			propertyDetail.setNoOfFloors((long) 1);
			propertyDetail.setIsSuperStructure(true);
			propertyDetail.setLandOwner("Anil");
			propertyDetail.setFloorType("normal");
			propertyDetail.setWoodType("normal");
			propertyDetail.setRoofType("normal");
			propertyDetail.setWallType("normal");
			propertyDetail.setAuditDetails(auditDetails);

			List<Floor> floors = propertyDetail.getFloors();

			Floor floor = new Floor();

			floor.setFloorNo("1");
			floor.setAuditDetails(auditDetails);

			List<Unit> units = new ArrayList<Unit>();

			Unit unit = new Unit();

			unit.setUnitNo("1");
			unit.setUnitType(UnitTypeEnum.FLAT.valueOf("FLAT"));
			unit.setLength((double) 10);
			unit.setWidth((double) 15);
			unit.setBuiltupArea((double) 15);
			unit.setAssessableArea((double) 25);
			unit.setBpaBuiltupArea((double) 35);
			unit.setBpaNo("bpa1");
			unit.setBpaDate("25/05/2017");
			unit.setUsage("construction");
			unit.setOccupancyType("business");
			unit.setOccupierName("Anil");
			unit.setFirmName("wtc");
			unit.setRentCollected((double) 12);
			unit.setStructure("rectangle");
			unit.setAge("27");
			unit.setExemptionReason("new property purchase");
			unit.setIsStructured(true);
			unit.setOccupancyDate("25/05/2017");
			unit.setConstCompletionDate("25/05/2017");
			unit.setManualArv((double) 5);
			unit.setArv((double) 10);
			unit.setElectricMeterNo("emno1");
			unit.setWaterMeterNo("waterno1");
			unit.setAuditDetails(auditDetails);
			floor.getUnits().add(unit);
			floor.setUnits(units);
			propertyDetail.getFloors().add(floor);

			propertyDetail.setFloors(floors);

			List<Document> documents = propertyDetail.getDocuments();
			Document document = new Document();
			document.setDocumentType("doctype");
			document.setFileStore("filestoredoc");
			document.setAuditDetails(auditDetails);
			propertyDetail.getDocuments().add(document);
			propertyDetail.setDocuments(documents);

			propertyDetail.setStateId("stateId1");
			propertyDetail.setApplicationNo("appno1");

			WorkFlowDetails workFlowDetails = new WorkFlowDetails();
			workFlowDetails.setDepartment("IT");
			workFlowDetails.setDesignation("se");
			workFlowDetails.setAssignee((long) 10);
			workFlowDetails.setAction("working");
			workFlowDetails.setStatus("processing");

			propertyDetail.setWorkFlowDetails(workFlowDetails);

			property.setPropertyDetail(propertyDetail);

			VacantLandDetail vacantLandDetails = new VacantLandDetail();
			vacantLandDetails.setSurveyNumber("sn1");
			vacantLandDetails.setPattaNumber("pt1");
			vacantLandDetails.setMarketValue((double) 150000);
			vacantLandDetails.setCapitalValue((double) 100000);
			vacantLandDetails.setLayoutApprovedAuth("approved");
			vacantLandDetails.setLayoutPermissionNo("pn1");
			vacantLandDetails.setLayoutPermissionDate("25/05/2017");
			vacantLandDetails.setResdPlotArea((double) 152);
			vacantLandDetails.setNonResdPlotArea((double) 154);
			vacantLandDetails.setAuditDetails(auditDetails);

			property.setVacantLand(vacantLandDetails);

			property.setAssessmentDate("25/05/2017");
			property.setOccupancyDate("25/05/2017");
			property.setGisRefNo("gf10");
			property.setIsAuthorised(true);
			property.setIsUnderWorkflow(true);

			PropertyLocation propertyLocation = new PropertyLocation();

			Boundary adminBoundary = new Boundary();
			Boundary locationBoundary = new Boundary();
			Boundary revenueBoundary = new Boundary();

			adminBoundary.setName("testing");
			locationBoundary.setName("testing");
			revenueBoundary.setName("testing");

			propertyLocation.setAdminBoundary(adminBoundary);
			propertyLocation.setLocationBoundary(locationBoundary);
			propertyLocation.setRevenueBoundary(revenueBoundary);

			propertyLocation.setNorthBoundedBy("north");
			propertyLocation.setSouthBoundedBy("south");
			propertyLocation.setWestBoundedBy("west");
			propertyLocation.setEastBoundedBy("east");
			propertyLocation.setAuditDetails(auditDetails);
			property.setBoundary(propertyLocation);

			property.setActive(true);
			property.setChannel(ChannelEnum.SYSTEM.valueOf("SYSTEM"));
			property.setAuditDetails(auditDetails);
			properties.add(property);

			propertyRequest.setProperties(properties);
			propertyRequest.setRequestInfo(getRequestInfoObject());

			kafkaTemplate.send(propertiesManager.getCreateWorkflow(), propertyRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateProperty() throws Exception {
		try {
			PropertyRequest propertyRequest = new PropertyRequest();
			List<Property> properties = new ArrayList<Property>();

			Property property = new Property();

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("Sandrapati");
			auditDetails.setLastModifiedBy("sandrapati");
			auditDetails.setCreatedTime((long) 123456);
			auditDetails.setLastModifiedTime((long) 987654);
			property.setId((long) 1);
			property.setTenantId("default");
			property.setUpicNumber("anil123");
			property.setVltUpicNumber("vltUpicNumberupdate2");
			property.setCreationReason(CreationReasonEnum.NEWPROPERTY.valueOf("NEWPROPERTY"));

			Address address = new Address();
			address.setId((long) 1);
			address.setTenantId("default");
			address.setLatitude((double) 12);
			address.setLongitude((double) 20);
			address.setAddressNumber("1-2-3");
			address.setAddressLine1("acgaurds");
			address.setAddressLine2("khirathabad");
			address.setLandmark("noori");
			address.setCity("hyderabad");
			address.setPincode("500004");
			address.setDetail("Testing");

			address.setAuditDetails(auditDetails);

			property.setAddress(address);

			List<User> owners = new ArrayList<User>();
			User owner = new User();
			owner.setId((long) 1);
			owner.setTenantId("default");
			owner.setUserName("Anilkumar");
			owner.setSalutation("testing");
			owner.setName("anil");
			owner.setGender("male");
			owner.setMobileNumber("9333555666");
			owner.setEmailId("anil@wtc.com");
			owner.setAadhaarNumber("123456789123");
			owner.setActive(true);
			owner.setLocale("no");
			owner.setType("house");
			owner.setAccountLocked(true);
			property.getOwners().add(owner);
			List<Role> roles = new ArrayList<Role>();

			Role role = new Role();
			role.setName("ANil");
			role.setDescription("Testing");
			owner.getRoles().add(role);
			owner.setRoles(roles);

			owner.setAuditDetails(auditDetails);

			owner.setDob("25/09/1989");
			owner.setAltContactNumber("9874562134");
			owner.setFatherOrHusbandName("svs");
			owner.setBloodGroup("O+");
			owner.setPan("stvt5854k");
			owner.setPermanentAddress("eluru");
			owner.setPermanentCity("eluru");
			owner.setPermanentPincode("534001");
			owner.setCorrespondenceCity("Hyderabad");
			owner.setCorrespondencePincode("500004");
			owner.setCorrespondenceAddress("Khirathabad");
			owner.setSignature("S Anilkumar");
			owner.setIdentificationMark("mole on right hand");
			owner.setPhoto("anil.png");

			owner.setIsPrimaryOwner(true);
			owner.setIsSecondaryOwner(true);
			owner.setOwnerShipPercentage((double) 10);
			owner.setOwnerType("Sandrapati Anilkumar");

			property.setOwners(owners);

			PropertyDetail propertyDetail = new PropertyDetail();
			propertyDetail.setId((long) 1);
			propertyDetail.setSource(SourceEnum.MUNICIPAL_RECORDS.valueOf("MUNICIPAL_RECORDS"));
			propertyDetail.setRegdDocNo("regdocupdate1");
			propertyDetail.setRegdDocDate("25/05/2017");
			propertyDetail.setReason("testing");
			propertyDetail.setStatus(StatusEnum.ACTIVE.valueOf("ACTIVE"));
			propertyDetail.setIsVerified(true);
			propertyDetail.setVerificationDate("25/05/2017");
			propertyDetail.setIsExempted(true);
			propertyDetail.setExemptionReason("Testing");
			propertyDetail.setPropertyType("Land");
			propertyDetail.setCategory("Land");
			propertyDetail.setUsage("Anil");
			propertyDetail.setDepartment("Land department");
			propertyDetail.setApartment("apartment");
			propertyDetail.setSiteLength((double) 10);
			propertyDetail.setSiteBreadth((double) 15);
			propertyDetail.setSitalArea((double) 10);
			propertyDetail.setTotalBuiltupArea((double) 20);
			propertyDetail.setUndividedShare((double) 5);
			propertyDetail.setNoOfFloors((long) 1);
			propertyDetail.setIsSuperStructure(true);
			propertyDetail.setLandOwner("Anil");
			propertyDetail.setFloorType("normal");
			propertyDetail.setWoodType("normal");
			propertyDetail.setRoofType("normal");
			propertyDetail.setWallType("normal");
			propertyDetail.setAuditDetails(auditDetails);

			List<Floor> floors = propertyDetail.getFloors();

			Floor floor = new Floor();
			floor.setId((long) 1);
			floor.setFloorNo("f1");
			floor.setAuditDetails(auditDetails);

			List<Unit> units = new ArrayList<Unit>();

			Unit unit = new Unit();
			unit.setId((long) 1);
			unit.setUnitNo("1");
			unit.setUnitType(UnitTypeEnum.FLAT.valueOf("FLAT"));
			unit.setLength((double) 15);
			unit.setWidth((double) 15);
			unit.setBuiltupArea((double) 15);
			unit.setAssessableArea((double) 25);
			unit.setBpaBuiltupArea((double) 35);
			unit.setBpaNo("bpa1");
			unit.setBpaDate("25/05/2017");
			unit.setUsage("construction");
			unit.setOccupancyType("business");
			unit.setOccupierName("Anil");
			unit.setFirmName("wtc");
			unit.setRentCollected((double) 12);
			unit.setStructure("rectangle");
			unit.setAge("27");
			unit.setExemptionReason("new property purchase");
			unit.setIsStructured(true);
			unit.setOccupancyDate("25/05/2017");
			unit.setConstCompletionDate("25/05/2017");
			unit.setManualArv((double) 5);
			unit.setArv((double) 10);
			unit.setElectricMeterNo("emno1");
			unit.setWaterMeterNo("waterno1");
			unit.setAuditDetails(auditDetails);
			floor.getUnits().add(unit);
			floor.setUnits(units);
			propertyDetail.getFloors().add(floor);

			propertyDetail.setFloors(floors);

			List<Document> documents = propertyDetail.getDocuments();
			Document document = new Document();
			document.setId((long) 1);
			document.setDocumentType("documenttype");
			document.setFileStore("filestoredoc1");
			document.setAuditDetails(auditDetails);
			propertyDetail.getDocuments().add(document);
			propertyDetail.setDocuments(documents);

			propertyDetail.setStateId("stateId1");
			propertyDetail.setApplicationNo("appno1");

			WorkFlowDetails workFlowDetails = new WorkFlowDetails();
			workFlowDetails.setDepartment("IT");
			workFlowDetails.setDesignation("se");
			workFlowDetails.setAssignee((long) 10);
			workFlowDetails.setAction("working");
			workFlowDetails.setStatus("processing");

			propertyDetail.setWorkFlowDetails(workFlowDetails);

			property.setPropertyDetail(propertyDetail);

			VacantLandDetail vacantLandDetails = new VacantLandDetail();
			vacantLandDetails.setId((long) 1);
			vacantLandDetails.setSurveyNumber("snupdate1");
			vacantLandDetails.setPattaNumber("pt1");
			vacantLandDetails.setMarketValue((double) 150000);
			vacantLandDetails.setCapitalValue((double) 100000);
			vacantLandDetails.setLayoutApprovedAuth("approved");
			vacantLandDetails.setLayoutPermissionNo("pn1");
			vacantLandDetails.setLayoutPermissionDate("25/05/2017");
			vacantLandDetails.setResdPlotArea((double) 152);
			vacantLandDetails.setNonResdPlotArea((double) 154);
			vacantLandDetails.setAuditDetails(auditDetails);

			property.setVacantLand(vacantLandDetails);

			property.setAssessmentDate("25/05/2017");
			property.setOccupancyDate("25/05/2017");
			property.setGisRefNo("gf10");
			property.setIsAuthorised(true);
			property.setIsUnderWorkflow(true);

			PropertyLocation propertyLocation = new PropertyLocation();

			Boundary adminBoundary = new Boundary();
			Boundary locationBoundary = new Boundary();
			Boundary revenueBoundary = new Boundary();

			adminBoundary.setName("test");
			locationBoundary.setName("testing");
			revenueBoundary.setName("testing");
			propertyLocation.setId((long) 5);
			propertyLocation.setAdminBoundary(adminBoundary);
			propertyLocation.setLocationBoundary(locationBoundary);
			propertyLocation.setRevenueBoundary(revenueBoundary);

			propertyLocation.setNorthBoundedBy("north");
			propertyLocation.setSouthBoundedBy("south");
			propertyLocation.setWestBoundedBy("west");
			propertyLocation.setEastBoundedBy("east");
			propertyLocation.setAuditDetails(auditDetails);
			property.setBoundary(propertyLocation);

			property.setActive(true);
			property.setChannel(ChannelEnum.SYSTEM.valueOf("SYSTEM"));
			property.setAuditDetails(auditDetails);
			properties.add(property);

			propertyRequest.setProperties(properties);
			propertyRequest.setRequestInfo(getRequestInfoObject());

			kafkaTemplate.send(propertiesManager.getUpdateWorkflow(), propertyRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createDepreciation() {

		DepreciationRequest depreciationRequest = new DepreciationRequest();
		depreciationRequest.setRequestInfo(getRequestInfoObject());

		String tenantId = "123";
		Depreciation depreciation = new Depreciation();
		List<Depreciation> depreciations = new ArrayList<>();
		depreciation.setTenantId("123");
		depreciation.setNameLocal("prasad");
		depreciation.setFromYear(2015);
		depreciation.setCode("456");
		depreciation.setToyear(2017);
		depreciation.setDescription("Intial loading");

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("prasad");
		auditDetails.setLastModifiedBy("pranav");
		auditDetails.setCreatedTime(new Long(23));
		auditDetails.setLastModifiedTime(new Long(256));

		depreciation.setAuditDetails(auditDetails);
		depreciations.add(depreciation);

		depreciationRequest.setDepreciations(depreciations);
		DepreciationResponse depreciationResponse = null;

		try {
			depreciationResponse = masterService.createDepreciation(tenantId, depreciationRequest);
		} catch (Exception e) {

			assertTrue(false);
		}

		if (depreciationResponse.getDepreciations().size() == 0) {
			assertTrue(false);
		}

		assertTrue(true);

	}

	@Test
	public void modifyDepreciation() {

		DepreciationRequest depreciationRequest = new DepreciationRequest();
		depreciationRequest.setRequestInfo(getRequestInfoObject());

		Depreciation depreciation = new Depreciation();
		List<Depreciation> depreciations = new ArrayList<>();
		depreciation.setId(1l);
		depreciation.setTenantId("12345");
		depreciation.setNameLocal("prasad");
		depreciation.setFromYear(2015);
		depreciation.setCode("4567");
		depreciation.setToyear(2017);
		depreciation.setDescription("Intial loading");

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pankaj");
		auditDetails.setLastModifiedBy("paku");
		auditDetails.setCreatedTime(new Long(23));
		auditDetails.setLastModifiedTime(new Long(256));

		depreciation.setAuditDetails(auditDetails);
		depreciations.add(depreciation);

		depreciationRequest.setDepreciations(depreciations);
		DepreciationResponse depreciationResponse = null;

		try {
			depreciationResponse = masterService.updateDepreciation(depreciationRequest);
		} catch (Exception e) {

			assertTrue(false);
		}

		if (depreciationResponse == null)
			assertTrue(false);

		if (depreciationResponse.getDepreciations().size() == 0)
			assertTrue(false);

		assertTrue(true);
	}

	@Test
	public void searchDepreciations() {

		Integer[] ids = new Integer[] { 1, 2, 7 };
		DepreciationSearchCriteria depreciationSearchCriteria = new DepreciationSearchCriteria();
		depreciationSearchCriteria.setTenantId("12345");
		depreciationSearchCriteria.setIds(ids);
		depreciationSearchCriteria.setFromYear(2015);
		depreciationSearchCriteria.setToYear(2017);
		depreciationSearchCriteria.setCode("4567");
		depreciationSearchCriteria.setNameLocal("prasad");
		depreciationSearchCriteria.setPageSize(20);
		depreciationSearchCriteria.setOffset(0);
		depreciationSearchCriteria.setYear(null);
		DepreciationResponse depreciationResponse = null;

		try {
			depreciationResponse = masterService.searchDepreciation(getRequestInfoObject(), depreciationSearchCriteria);
		} catch (Exception e) {
			assertTrue(false);
		}

		if (depreciationResponse == null)
			assertTrue(false);

		if (depreciationResponse.getDepreciations().size() == 0) {
			assertTrue(false);
		}

		assertTrue(true);

	}

	@Test
	public void createMutationTest() {

		MutationMasterRequest mutationMasterRequest = new MutationMasterRequest();
		MutationMaster mutationMaster = new MutationMaster();
		mutationMaster.setTenantId("123");
		mutationMaster.setCode("456");
		mutationMaster.setName("prasad");
		mutationMaster.setNameLocal("pranav");
		mutationMaster.setDescription("description");

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pankaj");
		auditDetails.setLastModifiedBy("paku");
		auditDetails.setCreatedTime(new Long(23));
		auditDetails.setLastModifiedTime(new Long(256));
		mutationMaster.setAuditDetails(auditDetails);

		List<MutationMaster> mutationMasters = new ArrayList<>();
		mutationMasters.add(mutationMaster);
		String tenantId = "123";

		mutationMasterRequest.setRequestInfo(getRequestInfoObject());
		mutationMasterRequest.setMutationMasters(mutationMasters);

		MutationMasterResponse mutationMasterResponse = null;

		try {
			mutationMasterResponse = masterService.createMutationMater(tenantId, mutationMasterRequest);
		} catch (Exception e) {
			assertTrue(false);
		}

		if (mutationMasterResponse != null && mutationMasterResponse.getMutationMasters().size() > 0)
			assertTrue(true);

		else
			assertTrue(false);

	}

	@Test
	public void modifyMutationMaster() {

		MutationMasterRequest mutationMasterRequest = new MutationMasterRequest();
		MutationMaster mutationMaster = new MutationMaster();
		mutationMaster.setTenantId("1234");
		mutationMaster.setCode("4567");
		mutationMaster.setName("pankaj");
		mutationMaster.setNameLocal("paku");
		mutationMaster.setDescription("description");
		mutationMaster.setId(1l);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pankaj");
		auditDetails.setLastModifiedBy("paku");
		auditDetails.setCreatedTime(new Long(23));
		auditDetails.setLastModifiedTime(new Long(256));
		mutationMaster.setAuditDetails(auditDetails);

		List<MutationMaster> mutationMasters = new ArrayList<>();
		mutationMasters.add(mutationMaster);

		mutationMasterRequest.setRequestInfo(getRequestInfoObject());
		mutationMasterRequest.setMutationMasters(mutationMasters);

		MutationMasterResponse mutationMasterResponse = null;

		try {
			mutationMasterResponse = masterService.updateMutationMaster(mutationMasterRequest);
		} catch (Exception e) {
			assertTrue(false);
		}

		if (mutationMasterResponse != null && mutationMasterResponse.getMutationMasters().size() > 0)
			assertTrue(true);

		else
			assertTrue(false);

	}

	@Test
	public void searchMutationMaster() {

		MutationMasterSearchCriteria mutationMasterSearchCriteria = new MutationMasterSearchCriteria();
		mutationMasterSearchCriteria.setTenantId("1234");
		mutationMasterSearchCriteria.setCode("4567");
		MutationMasterResponse mutationMasterResponse = null;

		try {
			mutationMasterResponse = masterService.searchMutationMaster(getRequestInfoObject(),
					mutationMasterSearchCriteria);
		} catch (Exception e) {
			assertTrue(false);
		}

		if (mutationMasterResponse != null && mutationMasterResponse.getMutationMasters().size() > 0) {
			assertTrue(true);
		} else
			assertTrue(false);
	}

	@Test
	public void createDocumentTypeTest() {

		DocumentTypeRequest documentTypeRequest = new DocumentTypeRequest();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("defaultt");
		documentType.setName("veswanthh");
		documentType.setCode("test code");
		documentType.setApplication(ApplicationEnum.BIFURCATION);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("veswanth");
		auditDetails.setLastModifiedBy("sai");
		auditDetails.setCreatedTime(new Long(23));
		auditDetails.setLastModifiedTime(new Long(256));
		documentType.setAuditDetails(auditDetails);

		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		documentTypes.add(documentType);
		String tenantId = "default";

		documentTypeRequest.setRequestInfo(getRequestInfoObject());
		documentTypeRequest.setDocumentType(documentTypes);

		DocumentTypeResponse documentTypeResponse = null;

		try {
			documentTypeResponse = masterService.createDocumentTypeMaster(tenantId, documentTypeRequest);
		} catch (Exception e) {
			assertTrue(false);
		}

		if (documentTypeResponse != null && documentTypeResponse.getDocumentType().size() > 0)
			assertTrue(true);
		else
			assertTrue(false);

	}

	@Test
	public void modifyDocumentTypeTest() {

		DocumentTypeRequest documentTypeRequest = new DocumentTypeRequest();
		DocumentType documentType = new DocumentType();
		documentType.setTenantId("default");
		documentType.setName("veswanth");
		documentType.setCode("codevalue");
		documentType.setApplication(ApplicationEnum.BIFURCATION);
		documentType.setId(1l);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("veswanth");
		auditDetails.setLastModifiedBy("sai");
		auditDetails.setCreatedTime(new Long(23));
		auditDetails.setLastModifiedTime(new Long(256));
		documentType.setAuditDetails(auditDetails);

		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		documentTypes.add(documentType);

		documentTypeRequest.setRequestInfo(getRequestInfoObject());
		documentTypeRequest.setDocumentType(documentTypes);

		DocumentTypeResponse documentTypeResponse = null;

		try {
			documentTypeResponse = masterService.updateDocumentTypeMaster(documentTypeRequest);
		} catch (Exception e) {
			assertTrue(false);
		}

		if (documentTypeResponse != null && documentTypeResponse.getDocumentType().size() > 0)
			assertTrue(true);
		else
			assertTrue(false);

	}

	@Test
	public void searchDocumentTypeTest() {
		DocumentTypeSearchCriteria documentTypeSearchCriteria = new DocumentTypeSearchCriteria();
		documentTypeSearchCriteria.setTenantId("default");
		documentTypeSearchCriteria.setName("veswanth");
		DocumentTypeResponse documentTypeResponse = null;

		try {
			documentTypeResponse = masterService.searchDocumentTypeMaster(getRequestInfoObject(),
					documentTypeSearchCriteria);

		} catch (Exception e) {
			assertTrue(false);
		}

		if (documentTypeResponse != null && documentTypeResponse.getDocumentType().size() > 0)
			assertTrue(true);
		else
			assertTrue(false);
	}

	@Test
	public void createGuidanceValueBoundaryTest() {
		try {
			String tenantId = "default";
			List<GuidanceValueBoundary> guidanceValueBoundaries = new ArrayList<>();
			GuidanceValueBoundary guidanceValueBoundary = new GuidanceValueBoundary();
			guidanceValueBoundary.setTenantId("default");
			guidanceValueBoundary.setGuidanceValueBoundary1("gvb1");
			guidanceValueBoundary.setGuidanceValueBoundary2("gvb2");
			AuditDetails auditDetails = new AuditDetails();
			guidanceValueBoundary.setAuditDetails(auditDetails);
			guidanceValueBoundaries.add(guidanceValueBoundary);
			GuidanceValueBoundaryRequest guidanceValueBoundaryRequest = new GuidanceValueBoundaryRequest();
			guidanceValueBoundaryRequest.setRequestInfo(getRequestInfoObject());
			guidanceValueBoundaryRequest.setGuidanceValueBoundaries(guidanceValueBoundaries);
			GuidanceValueBoundaryResponse guidanceValueBoundaryResponse;
			guidanceValueBoundaryResponse = masterService.createGuidanceValueBoundary(tenantId,
					guidanceValueBoundaryRequest);

			if (guidanceValueBoundaryResponse.getGuidanceValueBoundaries().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void updateGuidanceValueBoundaryTest() {
		try {
			List<GuidanceValueBoundary> guidanceValueBoundaries = new ArrayList<>();
			GuidanceValueBoundary guidanceValueBoundary = new GuidanceValueBoundary();
			guidanceValueBoundary.setId(1l);
			guidanceValueBoundary.setTenantId("default");
			guidanceValueBoundary.setGuidanceValueBoundary1("updated-gvb1");
			guidanceValueBoundary.setGuidanceValueBoundary2("updated-gvb2");
			AuditDetails auditDetails = new AuditDetails();
			guidanceValueBoundary.setAuditDetails(auditDetails);
			guidanceValueBoundaries.add(guidanceValueBoundary);
			GuidanceValueBoundaryRequest guidanceValueBoundaryRequest = new GuidanceValueBoundaryRequest();
			guidanceValueBoundaryRequest.setRequestInfo(getRequestInfoObject());
			guidanceValueBoundaryRequest.setGuidanceValueBoundaries(guidanceValueBoundaries);
			GuidanceValueBoundaryResponse guidanceValueBoundaryResponse;
			guidanceValueBoundaryResponse = masterService.updateGuidanceValueBoundary(guidanceValueBoundaryRequest);

			if (guidanceValueBoundaryResponse.getGuidanceValueBoundaries().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void searchGuidanceValueBoundaryTest() {
		GuidanceValueBoundarySearchCriteria guidanceValueBoundarySearchCriteria = new GuidanceValueBoundarySearchCriteria();
		guidanceValueBoundarySearchCriteria.setTenantId("default");
		guidanceValueBoundarySearchCriteria.setGuidanceValueBoundary1("updated-gvb1");
		RequestInfo requestInfo = getRequestInfoObject();
		GuidanceValueBoundaryResponse guidanceValueBoundaryResponse = null;
		try {
			guidanceValueBoundaryResponse = masterService.getGuidanceValueBoundary(requestInfo,
					guidanceValueBoundarySearchCriteria);
			if (guidanceValueBoundaryResponse != null
					&& guidanceValueBoundaryResponse.getGuidanceValueBoundaries().size() > 0)
				assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void createAppConfigurationTest() {
		try {
			String tenantId = "default";
			List<AppConfiguration> appConfigurations = new ArrayList<>();
			List<String> values = new ArrayList<>();
			values.add("appvalue1");
			values.add("appvalue2");
			values.add("appvalue3");
			AppConfiguration appConfiguration = new AppConfiguration();
			appConfiguration.setTenantId("default");
			appConfiguration.setKeyName("AppConfig");
			appConfiguration.setEffectiveFrom("01/04/2012");
			appConfiguration.setValues(values);
			AuditDetails auditDetails = new AuditDetails();
			appConfiguration.setAuditDetails(auditDetails);
			appConfigurations.add(appConfiguration);
			AppConfigurationRequest appConfigurationRequest = new AppConfigurationRequest();
			appConfigurationRequest.setRequestInfo(getRequestInfoObject());
			appConfigurationRequest.setAppConfigurations(appConfigurations);
			AppConfigurationResponse appConfigurationResponse;
			appConfigurationResponse = masterService.createAppConfiguration(tenantId, appConfigurationRequest);

			if (appConfigurationResponse.getAppConfigurations().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void updateAppConfigurationTest() {
		try {
			List<AppConfiguration> appConfigurations = new ArrayList<>();
			List<String> values = new ArrayList<>();
			values.add("appvalueupdate1");
			values.add("appvalueupdate2");
			values.add("appvalueupdate3");
			AppConfiguration appConfiguration = new AppConfiguration();
			appConfiguration.setId(1l);
			appConfiguration.setTenantId("default");
			appConfiguration.setKeyName("AppConfigUpdated");
			appConfiguration.setEffectiveFrom("05/09/2012");
			appConfiguration.setValues(values);
			AuditDetails auditDetails = new AuditDetails();
			appConfiguration.setAuditDetails(auditDetails);
			appConfigurations.add(appConfiguration);
			AppConfigurationRequest appConfigurationRequest = new AppConfigurationRequest();
			appConfigurationRequest.setRequestInfo(getRequestInfoObject());
			appConfigurationRequest.setAppConfigurations(appConfigurations);
			AppConfigurationResponse appConfigurationResponse;
			appConfigurationResponse = masterService.updateAppConfiguration(appConfigurationRequest);

			if (appConfigurationResponse.getAppConfigurations().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void searchAppConfigurationTest() {
		AppConfigurationSearchCriteria appConfigurationSearchCriteria = new AppConfigurationSearchCriteria();
		appConfigurationSearchCriteria.setTenantId("default");
		appConfigurationSearchCriteria.setKeyName("AppConfigUpdated");
		RequestInfo requestInfo = getRequestInfoObject();
		AppConfigurationResponse appConfigurationResponse;
		try {
			appConfigurationResponse = masterService.getAppConfiguration(requestInfo, appConfigurationSearchCriteria);
			if (appConfigurationResponse != null && appConfigurationResponse.getAppConfigurations().size() > 0)
				assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/*
	 * @Test public void createVacancyRemissionTest() {
	 * 
	 * VacancyRemissionRequest vacancyRemissionRequest = new
	 * VacancyRemissionRequest(); VacancyRemission vacancyRemission = new
	 * VacancyRemission(); vacancyRemission.setTenantId("default");
	 * vacancyRemission.setApplicationNo("1000123");
	 * vacancyRemission.setFromDate("12/12/2017");
	 * vacancyRemission.setToDate("13/05/2018");
	 * vacancyRemission.setPercentage(25.0);
	 * vacancyRemission.setReason(Reason.fromValue("NINTY_DAYS_INACTIVE"));
	 * vacancyRemission.setRequestDate("28/02/2018");
	 * vacancyRemission.setApprovedDate("30/08/2018");
	 * vacancyRemission.setIsApproved(true);
	 * vacancyRemission.setRemarks("Test Comment!");
	 * 
	 * List<Document> documents = new ArrayList<> (); Document document = new
	 * Document(); document.setDocumentType("DocumentType");
	 * document.setFileStore("TestFileStore"); documents.add(document);
	 * 
	 * WorkFlowDetails workFlowDetails = new WorkFlowDetails();
	 * workFlowDetails.setDepartment("Revenue");
	 * workFlowDetails.setDesignation("Junior Assistant");
	 * workFlowDetails.setAssignee(21l); workFlowDetails.setAction("forward");
	 * workFlowDetails.setStatus("processing");
	 * 
	 * vacancyRemission.setDocuments(documents);
	 * 
	 * 
	 * }
	 */
	/*
	 * @Test public void modifyVacancyRemissionTest() {
	 * 
	 * }
	 * 
	 * @Test public void searchVacancyRemissionTest() {
	 * 
	 * }
	 */

	@Test
	public void createDemolitionReason() {
		String tenantId = "default";
		RequestInfo requestInfo = getRequestInfoObject();

		List<DemolitionReason> demolitionReasons = new ArrayList<>();

		DemolitionReason demolitionReason = new DemolitionReason();
		demolitionReason.setTenantId("1234");
		demolitionReason.setName("vishal kumar");
		demolitionReason.setCode("111");

		demolitionReasons.add(demolitionReason);

		DemolitionReasonRequest demolitionReasonRequest = new DemolitionReasonRequest();
		demolitionReasonRequest.setDemolitionReasons(demolitionReasons);
		demolitionReasonRequest.setRequestInfo(requestInfo);

		try {
			DemolitionReasonResponse demolitionReasonResponse = masterService.createDemolitionReason(tenantId,
					demolitionReasonRequest);
			if (demolitionReasonResponse.getDemolitionReason().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	@Test
	public void updateDemolitionReason() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<DemolitionReason> demolitionReasons = new ArrayList<>();
		DemolitionReason demolitionReason = new DemolitionReason();
		demolitionReason.setId(demolitionId);
		demolitionReason.setTenantId("1234");
		demolitionReason.setName("vishal  kumar");
		demolitionReason.setCode("111");
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("vishal");
		auditDetails.setLastModifiedBy("vishal");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		demolitionReason.setAuditDetails(auditDetails);
		demolitionReasons.add(demolitionReason);

		DemolitionReasonRequest demolitionReasonRequest = new DemolitionReasonRequest();
		demolitionReasonRequest.setDemolitionReasons(demolitionReasons);
		demolitionReasonRequest.setRequestInfo(requestInfo);

		try {
			DemolitionReasonResponse demolitionReasonResponse = masterService
					.updateDemolitionReason(demolitionReasonRequest);

			if (demolitionReasonResponse.getDemolitionReason().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	@Test
	public void searchDemolitionReason() {

		DemolitionReasonSearchCriteria demolitionReasonSearchCriteria = new DemolitionReasonSearchCriteria();
		demolitionReasonSearchCriteria.setTenantId("1234");
		RequestInfo requestInfo = getRequestInfoObject();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			DemolitionReasonResponse demolitionReasonResponse = masterService.getDemolitionReason(requestInfo,
					demolitionReasonSearchCriteria);
			if (demolitionReasonResponse.getDemolitionReason().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	@Test
	public void creatTaxExemptionReasonTest() {

		TaxExemptionReasonRequest taxExemptionReasonRequest = new TaxExemptionReasonRequest();
		List<TaxExemptionReason> exemptionReasons = new ArrayList<>();
		List<String> taxHeads = new ArrayList<>();
		taxHeads.add("THU1");
		TaxExemptionReason taxExemptionReason = new TaxExemptionReason();
		taxExemptionReason.setTenantId("default");
		taxExemptionReason.setCode("ZIPCODE1");
		taxExemptionReason.setName("X-MAN1");
		taxExemptionReason.setPercentageRate(45d);
		taxExemptionReason.setDescription("Test Update Reason!");
		taxExemptionReason.setActive(true);
		taxExemptionReason.setTaxHeads(taxHeads);
		exemptionReasons.add(taxExemptionReason);
		taxExemptionReasonRequest.setRequestInfo(getRequestInfoObject());
		taxExemptionReasonRequest.setTaxExemptionReasons(exemptionReasons);
		TaxExemptionReasonResponse taxExemptionReasonResponse = null;
		try {
			taxExemptionReasonResponse = masterService.createTaxExemptionReason(taxExemptionReasonRequest);
			if (taxExemptionReasonResponse.getTaxExemptionReasons().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void updateTaxExemptionReasonTest() {

		TaxExemptionReasonRequest taxExemptionReasonRequest = new TaxExemptionReasonRequest();
		List<TaxExemptionReason> taxExemptionReasons = new ArrayList<>();
		List<String> taxHeads = new ArrayList<>();
		taxHeads.add("UTH1-U");
		TaxExemptionReason taxExemptionReason = new TaxExemptionReason();
		taxExemptionReason.setId(1l);
		taxExemptionReason.setTenantId("default");
		taxExemptionReason.setCode("UPDATE-CODE-U");
		taxExemptionReason.setName("UPDATEX-MAN-U");
		taxExemptionReason.setPercentageRate(50.0);
		taxExemptionReason.setDescription("Update Test reason!");
		taxExemptionReason.setActive(true);
		taxExemptionReason.setTaxHeads(taxHeads);
		taxExemptionReasons.add(taxExemptionReason);
		taxExemptionReasonRequest.setRequestInfo(getRequestInfoObject());
		taxExemptionReasonRequest.setTaxExemptionReasons(taxExemptionReasons);
		TaxExemptionReasonResponse taxExemptionReasonResponse = null;
		try {
			taxExemptionReasonResponse = masterService.updateTaxExemptionReason(taxExemptionReasonRequest);
			if (taxExemptionReasonResponse.getTaxExemptionReasons().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void searchExemptionReasonTest() {

		TaxExemptionReasonSearchCriteria exemptionReasonSearchCriteria = new TaxExemptionReasonSearchCriteria();
		exemptionReasonSearchCriteria.setTenantId("default");
		TaxExemptionReasonResponse exemptionReasonResponse = null;
		try {
			exemptionReasonResponse = masterService.getTaxExemptionReason(getRequestInfoObject(),
					exemptionReasonSearchCriteria);
			if (exemptionReasonResponse.getTaxExemptionReasons().size() == 0)
				assertTrue(false);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
}
