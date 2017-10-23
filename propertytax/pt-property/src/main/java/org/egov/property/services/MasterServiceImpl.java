package org.egov.property.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import org.egov.models.TaxExemptionReason;
import org.egov.models.TaxExemptionReasonRequest;
import org.egov.models.TaxExemptionReasonResponse;
import org.egov.models.TaxExemptionReasonSearchCriteria;
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
import org.egov.models.PropertyType;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.PropertyTypeSearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.RoofType;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.RoofTypeSearchCriteria;
import org.egov.models.StructureClass;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.models.StructureClassSearchCriteria;
import org.egov.models.Unit;
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
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.DuplicateIdException;
import org.egov.property.exception.InvalidCodeException;
import org.egov.property.exception.InvalidInputException;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.model.ExcludeFileds;
import org.egov.property.repository.PropertyMasterRepository;
import org.egov.property.repository.builder.AppConfigurationBuilder;
import org.egov.property.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Description : MasterService interface implementation class
 * 
 * @author Narendra
 *
 */

@Service
public class MasterServiceImpl implements Masterservice {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private PropertyMasterRepository propertyMasterRepository;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public FloorTypeResponse getFloorTypeMaster(RequestInfo requestInfo,
			FloorTypeSearchCriteria floorTypeSearchCriteria) throws Exception {

		List<FloorType> floorTypes = null;
		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();

		try {
			floorTypes = propertyMasterRepository.searchFloorType(floorTypeSearchCriteria.getTenantId(),
					floorTypeSearchCriteria.getIds(), floorTypeSearchCriteria.getName(),
					floorTypeSearchCriteria.getCode(), floorTypeSearchCriteria.getNameLocal(),
					floorTypeSearchCriteria.getPageSize(), floorTypeSearchCriteria.getOffSet());
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			floorTypeResponse.setFloorTypes(floorTypes);
			floorTypeResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new PropertySearchException("invalid input", requestInfo);
		}
		return floorTypeResponse;

	}

	@Override
	@Transactional
	public FloorTypeResponse createFloorType(FloorTypeRequest floorTypeRequest, String tenantId) throws Exception {

		for (FloorType floorType : floorTypeRequest.getFloorTypes()) {

			AuditDetails auditDetails = getAuditDetail(floorTypeRequest.getRequestInfo());

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(floorType.getTenantId(),
					floorType.getCode(), ConstantUtility.FLOOR_TYPE_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(floorTypeRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(floorType);
				floorType.setAuditDetails(auditDetails);
				Long id = propertyMasterRepository.saveFloorType(floorType, data);
				floorType.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(floorTypeRequest.getRequestInfo());
			}
		}
		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(floorTypeRequest.getRequestInfo(), true);
		floorTypeResponse.setFloorTypes(floorTypeRequest.getFloorTypes());
		floorTypeResponse.setResponseInfo(responseInfo);
		return floorTypeResponse;
	}

	@Override
	@Transactional
	public FloorTypeResponse updateFloorType(FloorTypeRequest floorTypeRequest) throws Exception {

		for (FloorType floorType : floorTypeRequest.getFloorTypes()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(floorTypeRequest.getRequestInfo(),
					ConstantUtility.FLOOR_TYPE_TABLE_NAME, floorType.getId());
			floorType.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(floorType.getTenantId(),
					floorType.getCode(), ConstantUtility.FLOOR_TYPE_TABLE_NAME, floorType.getId());

			if (isExists)
				throw new DuplicateIdException(floorTypeRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(floorType);

			try {

				propertyMasterRepository.updateFloorType(floorType, data);
			} catch (Exception e) {

				throw new InvalidInputException(floorTypeRequest.getRequestInfo());
			}
		}
		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();
		floorTypeResponse.setFloorTypes(floorTypeRequest.getFloorTypes());
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(floorTypeRequest.getRequestInfo(), true);
		floorTypeResponse.setResponseInfo(responseInfo);

		return floorTypeResponse;
	}

	@Override
	public WoodTypeResponse getWoodTypes(RequestInfo requestInfo, WoodTypeSearchCriteria woodTypeSearchCriteria)
			throws Exception {

		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();

		try {

			List<WoodType> woodTypes = propertyMasterRepository.searchWoodType(woodTypeSearchCriteria.getTenantId(),
					woodTypeSearchCriteria.getIds(), woodTypeSearchCriteria.getName(), woodTypeSearchCriteria.getCode(),
					woodTypeSearchCriteria.getNameLocal(), woodTypeSearchCriteria.getPageSize(),
					woodTypeSearchCriteria.getOffSet());
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

			woodTypeResponse.setWoodTypes(woodTypes);
			woodTypeResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return woodTypeResponse;

	}

	@Override
	@Transactional
	public WoodTypeResponse createWoodType(WoodTypeRequest woodTypeRequest, String tenantId) throws Exception {

		for (WoodType woodType : woodTypeRequest.getWoodTypes()) {
			AuditDetails auditDetails = getAuditDetail(woodTypeRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(woodType.getTenantId(),
					woodType.getCode(), ConstantUtility.WOOD_TYPE_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(woodTypeRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(woodType);
				woodType.setAuditDetails(auditDetails);
				Long id = propertyMasterRepository.saveWoodType(woodType, data);
				woodType.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(woodTypeRequest.getRequestInfo());
			}
		}
		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(woodTypeRequest.getRequestInfo(), true);
		woodTypeResponse.setWoodTypes(woodTypeRequest.getWoodTypes());
		woodTypeResponse.setResponseInfo(responseInfo);
		return woodTypeResponse;

	}

	@Override
	@Transactional
	public WoodTypeResponse updateWoodType(WoodTypeRequest woodTypeRequest) throws Exception {

		for (WoodType woodType : woodTypeRequest.getWoodTypes()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(woodTypeRequest.getRequestInfo(),
					ConstantUtility.WOOD_TYPE_TABLE_NAME, woodType.getId());
			woodType.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(woodType.getTenantId(),
					woodType.getCode(), ConstantUtility.WOOD_TYPE_TABLE_NAME, woodType.getId());
			if (isExists)
				throw new DuplicateIdException(woodTypeRequest.getRequestInfo());
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(woodType);

			try {

				propertyMasterRepository.updateWoodType(woodType, data);
			} catch (Exception e) {

				throw new InvalidInputException(woodTypeRequest.getRequestInfo());
			}
		}
		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(woodTypeRequest.getRequestInfo(), true);
		woodTypeResponse.setWoodTypes(woodTypeRequest.getWoodTypes());
		woodTypeResponse.setResponseInfo(responseInfo);

		return woodTypeResponse;
	}

	@Override
	public RoofTypeResponse getRoofypes(RequestInfo requestInfo, RoofTypeSearchCriteria roofTypeSearchCriteria)
			throws Exception {

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();

		try {

			List<RoofType> roofTypes = propertyMasterRepository.searchRoofType(roofTypeSearchCriteria.getTenantId(),
					roofTypeSearchCriteria.getIds(), roofTypeSearchCriteria.getName(), roofTypeSearchCriteria.getCode(),
					roofTypeSearchCriteria.getNameLocal(), roofTypeSearchCriteria.getPageSize(),
					roofTypeSearchCriteria.getOffSet());
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			roofTypeResponse.setRoofTypes(roofTypes);
			roofTypeResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new PropertySearchException("invalid input", requestInfo);
		}
		return roofTypeResponse;

	}

	@Override
	@Transactional
	public RoofTypeResponse createRoofype(RoofTypeRequest roofTypeRequest, String tenantId) throws Exception {

		for (RoofType roofType : roofTypeRequest.getRoofTypes()) {
			AuditDetails auditDetails = getAuditDetail(roofTypeRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(roofType.getTenantId(),
					roofType.getCode(), ConstantUtility.ROOF_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(roofTypeRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(roofType);
				roofType.setAuditDetails(auditDetails);
				Long id = propertyMasterRepository.saveRoofType(roofType, data);
				roofType.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(roofTypeRequest.getRequestInfo());
			}
		}
		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(roofTypeRequest.getRequestInfo(), true);
		roofTypeResponse.setRoofTypes(roofTypeRequest.getRoofTypes());
		roofTypeResponse.setResponseInfo(responseInfo);

		return roofTypeResponse;
	}

	@Override
	@Transactional
	public RoofTypeResponse updateRoofType(RoofTypeRequest roofTypeRequest) throws Exception {

		for (RoofType roofType : roofTypeRequest.getRoofTypes()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(roofTypeRequest.getRequestInfo(),
					ConstantUtility.ROOF_TYPE_TABLE_NAME, roofType.getId());
			roofType.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(roofType.getTenantId(),
					roofType.getCode(), ConstantUtility.ROOF_TYPE_TABLE_NAME, roofType.getId());

			if (isExists)
				throw new DuplicateIdException(roofTypeRequest.getRequestInfo());
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(roofType);

			try {

				propertyMasterRepository.updateRoofType(roofType, data);
			} catch (Exception e) {

				throw new InvalidInputException(roofTypeRequest.getRequestInfo());
			}
		}
		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(roofTypeRequest.getRequestInfo(), true);
		roofTypeResponse.setRoofTypes(roofTypeRequest.getRoofTypes());
		roofTypeResponse.setResponseInfo(responseInfo);

		return roofTypeResponse;
	}

	@Override
	@Transactional
	public DepartmentResponseInfo createDepartmentMaster(String tenantId, DepartmentRequest departmentRequest) {
		// TODO Auto-generated method stub

		for (Department department : departmentRequest.getDepartments()) {
			AuditDetails auditDetails = getAuditDetail(departmentRequest.getRequestInfo());
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(department);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(department.getTenantId(),
					department.getCode(), ConstantUtility.DEPARTMENT_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(departmentRequest.getRequestInfo());
			department.setAuditDetails(auditDetails);
			Long id = propertyMasterRepository.saveDepartment(tenantId, department, data);
			department.setId(id);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(departmentRequest.getRequestInfo(), true);

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		departmentResponse.setDepartments(departmentRequest.getDepartments());
		departmentResponse.setResponseInfo(responseInfo);
		return departmentResponse;
	}

	/**
	 * Description: department update
	 * 
	 * @param tenantId
	 * @param id
	 * @param DepartmentRequest
	 */

	@Override
	@Transactional
	public DepartmentResponseInfo updateDepartmentMaster(DepartmentRequest departmentRequest) {

		for (Department department : departmentRequest.getDepartments()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(departmentRequest.getRequestInfo(),
					ConstantUtility.DEPARTMENT_TABLE_NAME, department.getId());
			department.setAuditDetails(auditDetails);
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(department);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(department.getTenantId(),
					department.getCode(), ConstantUtility.DEPARTMENT_TABLE_NAME, department.getId());

			if (isExists)
				throw new DuplicateIdException(departmentRequest.getRequestInfo());
			propertyMasterRepository.updateDepartment(department, data);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(departmentRequest.getRequestInfo(), true);
		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		departmentResponse.setDepartments(departmentRequest.getDepartments());
		departmentResponse.setResponseInfo(responseInfo);

		return departmentResponse;
	}

	@Override
	public DepartmentResponseInfo getDepartmentMaster(RequestInfo requestInfo,
			DepartmentSearchCriteria departmentSearchCriteria) {

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		List<Department> departments = null;
		try {

			departments = propertyMasterRepository.searchDepartment(departmentSearchCriteria.getTenantId(),
					departmentSearchCriteria.getIds(), departmentSearchCriteria.getName(),
					departmentSearchCriteria.getCode(), departmentSearchCriteria.getNameLocal(),
					departmentSearchCriteria.getPageSize(), departmentSearchCriteria.getOffSet());
		} catch (Exception e) {
			throw new PropertySearchException("invalid input", requestInfo);
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

		departmentResponse.setDepartments(departments);
		departmentResponse.setResponseInfo(responseInfo);

		return departmentResponse;
	}

	@Override
	@Transactional
	public OccuapancyMasterResponse createOccuapancyMaster(String tenantId,
			OccuapancyMasterRequest occuapancyMasterRequest) {
		// TODO Auto-generated method stub

		for (OccuapancyMaster occuapancy : occuapancyMasterRequest.getOccuapancyMasters()) {
			AuditDetails auditDetails = getAuditDetail(occuapancyMasterRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(occuapancy.getTenantId(),
					occuapancy.getCode(), ConstantUtility.OCCUPANCY_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(occuapancyMasterRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(occuapancy);
			occuapancy.setAuditDetails(auditDetails);
			Long id = propertyMasterRepository.saveOccuapancy(tenantId, occuapancy, data);

			occuapancy.setId(id);

		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(occuapancyMasterRequest.getRequestInfo(), true);

		OccuapancyMasterResponse occuapancyMasterResponse = new OccuapancyMasterResponse();

		occuapancyMasterResponse.setOccuapancyMasters(occuapancyMasterRequest.getOccuapancyMasters());

		occuapancyMasterResponse.setResponseInfo(responseInfo);

		return occuapancyMasterResponse;
	}

	@Override
	@Transactional
	public OccuapancyMasterResponse updateOccuapancyMaster(OccuapancyMasterRequest occuapancyRequest) {

		for (OccuapancyMaster occuapancyMaster : occuapancyRequest.getOccuapancyMasters()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(occuapancyRequest.getRequestInfo(),
					ConstantUtility.OCCUPANCY_TABLE_NAME, occuapancyMaster.getId());
			occuapancyMaster.setAuditDetails(auditDetails);

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(occuapancyMaster.getTenantId(),
					occuapancyMaster.getCode(), ConstantUtility.OCCUPANCY_TABLE_NAME, occuapancyMaster.getId());

			if (isExists)
				throw new DuplicateIdException(occuapancyRequest.getRequestInfo());
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(occuapancyMaster);
			propertyMasterRepository.updateOccuapancy(occuapancyMaster, data);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(occuapancyRequest.getRequestInfo(), true);
		OccuapancyMasterResponse occuapancyResponse = new OccuapancyMasterResponse();
		occuapancyResponse.setOccuapancyMasters(occuapancyRequest.getOccuapancyMasters());
		occuapancyResponse.setResponseInfo(responseInfo);

		return occuapancyResponse;
	}

	public OccuapancyMasterResponse getOccuapancyMaster(RequestInfo requestInfo,
			OccuapancyMasterSearchCriteria occuapancyMasterSearchCriteria) {

		OccuapancyMasterResponse occupapancyMasterResponse = new OccuapancyMasterResponse();

		List<OccuapancyMaster> occupapancyMasters = null;
		try {

			occupapancyMasters = propertyMasterRepository.searchOccupancy(requestInfo,
					occuapancyMasterSearchCriteria.getTenantId(), occuapancyMasterSearchCriteria.getIds(),
					occuapancyMasterSearchCriteria.getName(), occuapancyMasterSearchCriteria.getCode(),
					occuapancyMasterSearchCriteria.getNameLocal(), occuapancyMasterSearchCriteria.getActive(),
					occuapancyMasterSearchCriteria.getOrderNumber(), occuapancyMasterSearchCriteria.getPageSize(),
					occuapancyMasterSearchCriteria.getOffSet());
		} catch (Exception e) {
			throw new PropertySearchException("invalid input", requestInfo);
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

		occupapancyMasterResponse.setOccuapancyMasters(occupapancyMasters);
		occupapancyMasterResponse.setResponseInfo(responseInfo);

		return occupapancyMasterResponse;
	}

	@Override
	@Transactional
	public PropertyTypeResponse createPropertyTypeMaster(String tenantId, PropertyTypeRequest propertyTypeRequest) {
		// TODO Auto-generated method stub

		for (PropertyType propertyType : propertyTypeRequest.getPropertyTypes()) {
			AuditDetails auditDetails = getAuditDetail(propertyTypeRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(propertyType.getTenantId(),
					propertyType.getCode(), ConstantUtility.PROPERTY_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(propertyTypeRequest.getRequestInfo());

			if (propertyType.getParent() != null) {
				if (!propertyType.getParent().isEmpty()) {
					Boolean isParentCodeExists = propertyMasterRepository.checkWhetherRecordExits(
							propertyType.getTenantId(), propertyType.getParent(),
							ConstantUtility.PROPERTY_TYPE_TABLE_NAME, null);

					if (!isParentCodeExists)
						throw new InvalidCodeException(propertiesManager.getInvalidParentMsg(),
								propertyTypeRequest.getRequestInfo());
				}
			}

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(propertyType);
			propertyType.setAuditDetails(auditDetails);
			Long id = propertyMasterRepository.savePropertyType(tenantId, propertyType, data);
			propertyType.setId(id);

		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(propertyTypeRequest.getRequestInfo(), true);

		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();

		propertyTypeResponse.setPropertyTypes(propertyTypeRequest.getPropertyTypes());

		propertyTypeResponse.setResponseInfo(responseInfo);

		return propertyTypeResponse;
	}

	@Override
	@Transactional
	public PropertyTypeResponse updatePropertyTypeMaster(PropertyTypeRequest propertyTypeRequest) {

		for (PropertyType propertyType : propertyTypeRequest.getPropertyTypes()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(propertyTypeRequest.getRequestInfo(),
					ConstantUtility.PROPERTY_TYPE_TABLE_NAME, propertyType.getId());
			propertyType.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(propertyType.getTenantId(),
					propertyType.getCode(), ConstantUtility.PROPERTY_TYPE_TABLE_NAME, propertyType.getId());

			if (isExists)
				throw new DuplicateIdException(propertyTypeRequest.getRequestInfo());

			if (propertyType.getParent() != null) {
				if (!propertyType.getParent().isEmpty()) {
					Boolean isParentCodeExists = propertyMasterRepository.checkWhetherRecordExits(
							propertyType.getTenantId(), propertyType.getParent(),
							ConstantUtility.PROPERTY_TYPE_TABLE_NAME, null);

					if (!isParentCodeExists)
						throw new InvalidCodeException(propertiesManager.getInvalidParentMsg(),
								propertyTypeRequest.getRequestInfo());
				}
			}

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(propertyType);
			propertyMasterRepository.updatePropertyType(propertyType, data);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(propertyTypeRequest.getRequestInfo(), true);
		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();
		propertyTypeResponse.setPropertyTypes(propertyTypeRequest.getPropertyTypes());
		propertyTypeResponse.setResponseInfo(responseInfo);

		return propertyTypeResponse;
	}

	@Override
	public PropertyTypeResponse getPropertyTypeMaster(RequestInfo requestInfo,
			PropertyTypeSearchCriteria propertyTypeSearchCriteria) {
		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();
		List<PropertyType> propertyTypes = null;
		try {

			propertyTypes = propertyMasterRepository.searchPropertyType(requestInfo,
					propertyTypeSearchCriteria.getTenantId(), propertyTypeSearchCriteria.getIds(),
					propertyTypeSearchCriteria.getName(), propertyTypeSearchCriteria.getCode(),
					propertyTypeSearchCriteria.getNameLocal(), propertyTypeSearchCriteria.getActive(),
					propertyTypeSearchCriteria.getOrderNumber(), propertyTypeSearchCriteria.getPageSize(),
					propertyTypeSearchCriteria.getOffSet(), propertyTypeSearchCriteria.getParent());
		} catch (Exception e) {
			throw new PropertySearchException("invalid input", requestInfo);
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		propertyTypeResponse.setPropertyTypes(propertyTypes);
		propertyTypeResponse.setResponseInfo(responseInfo);
		return propertyTypeResponse;
	}

	@Override
	@Transactional
	public UsageMasterResponse createUsageMaster(String tenantId, UsageMasterRequest usageMasterRequest)
			throws Exception {

		for (UsageMaster usageMaster : usageMasterRequest.getUsageMasters()) {
			AuditDetails auditDetails = getAuditDetail(usageMasterRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(usageMaster.getTenantId(),
					usageMaster.getCode(), ConstantUtility.USAGE_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(usageMasterRequest.getRequestInfo());

			if (usageMaster.getParent() != null) {
				if (!usageMaster.getParent().isEmpty()) {
					Boolean isParentCodeExists = propertyMasterRepository.checkWhetherRecordExits(
							usageMaster.getTenantId(), usageMaster.getParent(), ConstantUtility.USAGE_TYPE_TABLE_NAME,
							null);

					if (!isParentCodeExists)
						throw new InvalidCodeException(propertiesManager.getInvalidParentMsg(),
								usageMasterRequest.getRequestInfo());
				}
			}

			if (usageMaster.getService() == null || usageMaster.getService().isEmpty()) {
				usageMaster.setService(propertiesManager.getUsageMasterDefaultService());
			}

			try {
				usageMaster.setAuditDetails(auditDetails);
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(usageMaster);
				Long id = propertyMasterRepository.saveUsageMaster(usageMaster, data);
				usageMaster.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(usageMasterRequest.getRequestInfo());
			}
		}
		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(usageMasterRequest.getRequestInfo(), true);

		usageMasterResponse.setUsageMasters(usageMasterRequest.getUsageMasters());
		usageMasterResponse.setResponseInfo(responseInfo);

		return usageMasterResponse;

	}

	@Override
	@Transactional
	public UsageMasterResponse updateUsageMaster(UsageMasterRequest usageMasterRequest) {

		for (UsageMaster usageMaster : usageMasterRequest.getUsageMasters()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(usageMasterRequest.getRequestInfo(),
					ConstantUtility.USAGE_TYPE_TABLE_NAME, usageMaster.getId());
			usageMaster.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(usageMaster.getTenantId(),
					usageMaster.getCode(), ConstantUtility.USAGE_TYPE_TABLE_NAME, usageMaster.getId());

			if (isExists)
				throw new DuplicateIdException(usageMasterRequest.getRequestInfo());

			if (usageMaster.getParent() != null) {
				if (!usageMaster.getParent().isEmpty()) {
					Boolean isParentCodeExists = propertyMasterRepository.checkWhetherRecordExits(
							usageMaster.getTenantId(), usageMaster.getParent(), ConstantUtility.USAGE_TYPE_TABLE_NAME,
							null);

					if (!isParentCodeExists)
						throw new InvalidCodeException(propertiesManager.getInvalidParentMsg(),
								usageMasterRequest.getRequestInfo());
				}
			}

			if (usageMaster.getService() == null || usageMaster.getService().isEmpty()) {
				usageMaster.setService(propertiesManager.getUsageMasterDefaultService());
			}

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(usageMaster);

				propertyMasterRepository.updateUsageMaster(usageMaster, data);

			} catch (Exception e) {
				throw new InvalidInputException(usageMasterRequest.getRequestInfo());
			}
		}
		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();
		usageMasterResponse.setUsageMasters(usageMasterRequest.getUsageMasters());
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(usageMasterRequest.getRequestInfo(), true);
		usageMasterResponse.setResponseInfo(responseInfo);

		return usageMasterResponse;
	}

	@Override
	public UsageMasterResponse getUsageMaster(RequestInfo requestInfo,
			UsageMasterSearchCriteria usageMasterSearchCriteria) throws Exception {

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();

		if (usageMasterSearchCriteria.getService() == null) {
			String[] defaultSerices = new String[1];
			defaultSerices[0] = propertiesManager.getUsageMasterDefaultService();

			// service = defaultSerices;
			usageMasterSearchCriteria.setService(defaultSerices);
		}
		try {
			List<UsageMaster> usageList = propertyMasterRepository.searchUsage(usageMasterSearchCriteria.getTenantId(),
					usageMasterSearchCriteria.getIds(), usageMasterSearchCriteria.getName(),
					usageMasterSearchCriteria.getCode(), usageMasterSearchCriteria.getNameLocal(),
					usageMasterSearchCriteria.getActive(), usageMasterSearchCriteria.getIsResidential(),
					usageMasterSearchCriteria.getOrderNumber(), usageMasterSearchCriteria.getPageSize(),
					usageMasterSearchCriteria.getOffSet(), usageMasterSearchCriteria.getParent(),
					usageMasterSearchCriteria.getService());
			usageMasterResponse.setUsageMasters(usageList);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			usageMasterResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			e.printStackTrace();
			throw new PropertySearchException("invalid input", requestInfo);
		}

		return usageMasterResponse;
	}

	@Override
	@Transactional
	public WallTypeResponse createWallTypeMaster(String tenantId, WallTypeRequest wallTypeRequest) throws Exception {

		for (WallType wallType : wallTypeRequest.getWallTypes()) {
			AuditDetails auditDetails = getAuditDetail(wallTypeRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(wallType.getTenantId(),
					wallType.getCode(), ConstantUtility.WALL_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(wallTypeRequest.getRequestInfo());

			try {
				wallType.setAuditDetails(auditDetails);
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(wallType);
				Long id = propertyMasterRepository.saveWallTypes(wallType, data);
				wallType.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(wallTypeRequest.getRequestInfo());
			}

		}
		WallTypeResponse wallTypeResponse = new WallTypeResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(wallTypeRequest.getRequestInfo(), true);
		wallTypeResponse.setWallTypes(wallTypeRequest.getWallTypes());
		wallTypeResponse.setResponseInfo(responseInfo);

		return wallTypeResponse;

	}

	@Override
	@Transactional
	public WallTypeResponse updateWallTypeMaster(WallTypeRequest wallTypeRequest) throws Exception {

		for (WallType wallType : wallTypeRequest.getWallTypes()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(wallTypeRequest.getRequestInfo(),
					ConstantUtility.WALL_TYPE_TABLE_NAME, wallType.getId());
			wallType.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(wallType.getTenantId(),
					wallType.getCode(), ConstantUtility.WALL_TYPE_TABLE_NAME, wallType.getId());

			if (isExists)
				throw new DuplicateIdException(wallTypeRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(wallType);
				propertyMasterRepository.updateWallTypes(wallType, data);

			} catch (Exception e) {
				throw new InvalidInputException(wallTypeRequest.getRequestInfo());
			}
		}
		WallTypeResponse wallTypeResponse = new WallTypeResponse();
		wallTypeResponse.setWallTypes(wallTypeRequest.getWallTypes());
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(wallTypeRequest.getRequestInfo(), true);
		wallTypeResponse.setResponseInfo(responseInfo);

		return wallTypeResponse;
	}

	@Override
	public WallTypeResponse getWallTypeMaster(RequestInfo requestInfo, WallTypeSearchCriteria wallTypeSearchCriteria)
			throws Exception {

		WallTypeResponse wallTypeResponse = new WallTypeResponse();

		try {

			List<WallType> wallTypes = propertyMasterRepository.searchWallType(wallTypeSearchCriteria.getTenantId(),
					wallTypeSearchCriteria.getIds(), wallTypeSearchCriteria.getName(), wallTypeSearchCriteria.getCode(),
					wallTypeSearchCriteria.getNameLocal(), wallTypeSearchCriteria.getPageSize(),
					wallTypeSearchCriteria.getOffSet());
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			wallTypeResponse.setResponseInfo(responseInfo);
			wallTypeResponse.setWallTypes(wallTypes);

		} catch (Exception e) {

			throw new PropertySearchException("invalid input", requestInfo);
		}

		return wallTypeResponse;
	}

	@Override
	@Transactional
	public StructureClassResponse craeateStructureClassMaster(String tenantId,
			StructureClassRequest structureClassRequest) {

		for (StructureClass structureClass : structureClassRequest.getStructureClasses()) {
			AuditDetails auditDetails = getAuditDetail(structureClassRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(structureClass.getTenantId(),
					structureClass.getCode(), ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(structureClassRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(structureClass);
				structureClass.setAuditDetails(auditDetails);
				Long id = propertyMasterRepository.saveStructureClsses(tenantId, structureClass, data);
				structureClass.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(structureClassRequest.getRequestInfo());
			}
		}

		StructureClassResponse structureClassResponse = new StructureClassResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(structureClassRequest.getRequestInfo(), true);

		structureClassResponse.setStructureClasses(structureClassRequest.getStructureClasses());
		structureClassResponse.setResponseInfo(responseInfo);

		return structureClassResponse;
	}

	@Override
	@Transactional
	public StructureClassResponse updateStructureClassMaster(StructureClassRequest structureClassRequest) {

		for (StructureClass structureClass : structureClassRequest.getStructureClasses()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(structureClassRequest.getRequestInfo(),
					ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, structureClass.getId());
			structureClass.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(structureClass.getTenantId(),
					structureClass.getCode(), ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, structureClass.getId());

			if (isExists)
				throw new DuplicateIdException(structureClassRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(structureClass);
				propertyMasterRepository.updateStructureClsses(structureClass, data);
			} catch (Exception e) {
				throw new InvalidInputException(structureClassRequest.getRequestInfo());
			}
		}

		StructureClassResponse structureClassResponse = new StructureClassResponse();
		structureClassResponse.setStructureClasses(structureClassRequest.getStructureClasses());
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(structureClassRequest.getRequestInfo(), true);
		structureClassResponse.setResponseInfo(responseInfo);

		return structureClassResponse;
	}

	@Override
	public StructureClassResponse getStructureClassMaster(RequestInfo requestInfo,
			StructureClassSearchCriteria structureClassSearchCriteria) {
		StructureClassResponse structureClassResponse = new StructureClassResponse();
		try {
			List<StructureClass> structureClasses = propertyMasterRepository.searchStructureClass(
					structureClassSearchCriteria.getTenantId(), structureClassSearchCriteria.getIds(),
					structureClassSearchCriteria.getName(), structureClassSearchCriteria.getCode(),
					structureClassSearchCriteria.getNameLocal(), structureClassSearchCriteria.getActive(),
					structureClassSearchCriteria.getOrderNumber(), structureClassSearchCriteria.getPageSize(),
					structureClassSearchCriteria.getOffSet());
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			structureClassResponse.setStructureClasses(structureClasses);
			structureClassResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new PropertySearchException("invalid input", requestInfo);
		}

		return structureClassResponse;

	}

	@Override
	@Transactional
	public DepreciationResponse createDepreciation(String tenantId, DepreciationRequest depreciationRequest)
			throws Exception {

		for (Depreciation depreciation : depreciationRequest.getDepreciations()) {
			AuditDetails auditDetails = getAuditDetail(depreciationRequest.getRequestInfo());
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(depreciation);

			Boolean isExists = checkCodeAndTenatIdExists(depreciation.getTenantId(), depreciation.getCode(),
					ConstantUtility.DEPRECIATION_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(depreciationRequest.getRequestInfo());
			depreciation.setAuditDetails(auditDetails);

			try {
				Long id = propertyMasterRepository.createDepreciation(depreciation, data);
				depreciation.setId(id);

			}

			catch (Exception e) {
				e.printStackTrace();
				throw new InvalidInputException(depreciationRequest.getRequestInfo());
			}
		}

		DepreciationResponse depreciationResponse = new DepreciationResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(depreciationRequest.getRequestInfo(), true);
		depreciationResponse.setResponseInfo(responseInfo);
		depreciationResponse.setDepreciations(depreciationRequest.getDepreciations());

		return depreciationResponse;
	}

	@Override
	@Transactional
	public DepreciationResponse updateDepreciation(DepreciationRequest depreciationRequest) throws Exception {

		for (Depreciation depreciation : depreciationRequest.getDepreciations()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(depreciationRequest.getRequestInfo(),
					ConstantUtility.DEPRECIATION_TABLE_NAME, depreciation.getId());
			depreciation.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(depreciation.getTenantId(),
					depreciation.getCode(), ConstantUtility.DEPRECIATION_TABLE_NAME, depreciation.getId());

			if (isExists)
				throw new DuplicateIdException(depreciationRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(depreciation);
				propertyMasterRepository.updateDepreciation(depreciation, data);

			} catch (Exception e) {
				throw new InvalidInputException(depreciationRequest.getRequestInfo());
			}
		}
		DepreciationResponse depreciationResponse = new DepreciationResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(depreciationRequest.getRequestInfo(), true);
		depreciationResponse.setResponseInfo(responseInfo);
		depreciationResponse.setDepreciations(depreciationRequest.getDepreciations());

		return depreciationResponse;
	}

	@Override
	public DepreciationResponse searchDepreciation(RequestInfo requestInfo,
			DepreciationSearchCriteria depreciationSearchCriteria) throws Exception {

		List<Depreciation> depreciations = propertyMasterRepository.searchDepreciations(
				depreciationSearchCriteria.getTenantId(), depreciationSearchCriteria.getIds(),
				depreciationSearchCriteria.getFromYear(), depreciationSearchCriteria.getToYear(),
				depreciationSearchCriteria.getCode(), depreciationSearchCriteria.getNameLocal(),
				depreciationSearchCriteria.getPageSize(), depreciationSearchCriteria.getOffset(),
				depreciationSearchCriteria.getYear());
		DepreciationResponse depreciationResponse = new DepreciationResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		depreciationResponse.setResponseInfo(responseInfo);
		depreciationResponse.setDepreciations(depreciations);
		return depreciationResponse;
	}

	public Boolean checkCodeAndTenatIdExists(String tenantId, String code, String tableName, Long id) {

		return propertyMasterRepository.checkWhetherRecordExits(tenantId, code, tableName, id);

	}

	@Override
	@Transactional
	public MutationMasterResponse createMutationMater(String tenantId, MutationMasterRequest mutationMasterRequest)
			throws Exception {
		mutationMasterRequest.getMutationMasters().forEach(muatation -> {
			AuditDetails auditDetails = getAuditDetail(mutationMasterRequest.getRequestInfo());
			Boolean isExists = checkCodeAndTenatIdExists(muatation.getTenantId(), muatation.getCode(),
					ConstantUtility.MUTATION_MASTER_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(mutationMasterRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(muatation);
			muatation.setAuditDetails(auditDetails);
			try {
				Long id = propertyMasterRepository.createMutationMaster(muatation, data);
				muatation.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(mutationMasterRequest.getRequestInfo());
			}

		});

		MutationMasterResponse mutationMasterResponse = new MutationMasterResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(mutationMasterRequest.getRequestInfo(), true);
		mutationMasterResponse.setResponseInfo(responseInfo);
		mutationMasterResponse.setMutationMasters(mutationMasterRequest.getMutationMasters());

		return mutationMasterResponse;
	}

	@Override
	@Transactional
	public MutationMasterResponse updateMutationMaster(MutationMasterRequest mutationMasterRequest) throws Exception {

		mutationMasterRequest.getMutationMasters().forEach(mutation -> {

			AuditDetails auditDetails = getUpdatedAuditDetails(mutationMasterRequest.getRequestInfo(),
					ConstantUtility.MUTATION_MASTER_TABLE_NAME, mutation.getId());
			mutation.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(mutation.getTenantId(),
					mutation.getCode(), ConstantUtility.MUTATION_MASTER_TABLE_NAME, mutation.getId());

			if (isExists)
				throw new DuplicateIdException(mutationMasterRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(mutation);

			try {
				propertyMasterRepository.updateMutationMaster(mutation, data);
			} catch (Exception e) {
				throw new InvalidInputException(mutationMasterRequest.getRequestInfo());
			}

		});

		MutationMasterResponse mutationMasterResponse = new MutationMasterResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(mutationMasterRequest.getRequestInfo(), true);
		mutationMasterResponse.setResponseInfo(responseInfo);
		mutationMasterResponse.setMutationMasters(mutationMasterRequest.getMutationMasters());

		return mutationMasterResponse;
	}

	@Override
	public MutationMasterResponse searchMutationMaster(RequestInfo requestInfo,
			MutationMasterSearchCriteria mutationMasterSearchCriteria) throws Exception {

		List<MutationMaster> mutationMasters = null;
		try {
			mutationMasters = propertyMasterRepository.searchMutation(mutationMasterSearchCriteria.getTenantId(),
					mutationMasterSearchCriteria.getIds(), mutationMasterSearchCriteria.getName(),
					mutationMasterSearchCriteria.getCode(), mutationMasterSearchCriteria.getNameLocal(),
					mutationMasterSearchCriteria.getPageSize(), mutationMasterSearchCriteria.getOffSet());
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		MutationMasterResponse mutationMasterResponse = new MutationMasterResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		mutationMasterResponse.setResponseInfo(responseInfo);
		mutationMasterResponse.setMutationMasters(mutationMasters);

		return mutationMasterResponse;
	}

	@Override
	@Transactional
	public DocumentTypeResponse createDocumentTypeMaster(String tenantId, DocumentTypeRequest documentTypeRequest)
			throws Exception {
		for (DocumentType documentType : documentTypeRequest.getDocumentType()) {
			AuditDetails auditDetails = getAuditDetail(documentTypeRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordWithTenantIdAndNameExits(
					documentType.getTenantId(), documentType.getCode(), documentType.getApplication().toString(),
					ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(documentTypeRequest.getRequestInfo());
			documentType.setAuditDetails(auditDetails);
			try {
				Long id = propertyMasterRepository.createDocumentTypeMaster(documentType);
				documentType.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(documentTypeRequest.getRequestInfo());
			}
		}

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(documentTypeRequest.getRequestInfo(), true);
		documentTypeResponse.setResponseInfo(responseInfo);
		documentTypeResponse.setDocumentType(documentTypeRequest.getDocumentType());

		return documentTypeResponse;
	}

	@Override
	@Transactional
	public DocumentTypeResponse updateDocumentTypeMaster(DocumentTypeRequest documentTypeRequest) throws Exception {

		for (DocumentType documentType : documentTypeRequest.getDocumentType()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(documentTypeRequest.getRequestInfo(),
					ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, documentType.getId());
			documentType.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordWithTenantIdAndNameExits(
					documentType.getTenantId(), documentType.getCode(), documentType.getApplication().toString(),
					ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, documentType.getId());

			if (isExists)
				throw new DuplicateIdException(documentTypeRequest.getRequestInfo());
			try {
				propertyMasterRepository.updateDocumentTypeMaster(documentType);
			} catch (Exception e) {
				throw new InvalidInputException(documentTypeRequest.getRequestInfo());
			}
		}

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(documentTypeRequest.getRequestInfo(), true);
		documentTypeResponse.setResponseInfo(responseInfo);
		documentTypeResponse.setDocumentType(documentTypeRequest.getDocumentType());

		return documentTypeResponse;
	}

	@Override
	public DocumentTypeResponse searchDocumentTypeMaster(RequestInfo requestInfo,
			DocumentTypeSearchCriteria documentTypeSearchCriteria) throws Exception {
		List<DocumentType> documentTypes = null;
		try {
			documentTypes = propertyMasterRepository.searchDocumentTypeMaster(documentTypeSearchCriteria.getTenantId(),
					documentTypeSearchCriteria.getName(), documentTypeSearchCriteria.getCode(),
					documentTypeSearchCriteria.getApplication(), documentTypeSearchCriteria.getPageSize(),
					documentTypeSearchCriteria.getOffSet());
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		documentTypeResponse.setResponseInfo(responseInfo);
		documentTypeResponse.setDocumentType(documentTypes);

		return documentTypeResponse;
	}

	@Override
	@Transactional
	public ApartmentResponse createApartment(String tenantId, ApartmentRequest apartmentRequest) throws Exception {

		for (Apartment apartment : apartmentRequest.getApartments()) {

			AuditDetails auditDetails = getAuditDetail(apartmentRequest.getRequestInfo());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(apartment.getTenantId(),
					apartment.getCode(), ConstantUtility.APARTMENT_TABLE_NAME, apartment.getId());
			validateApartmentData(apartment.getFloor(), apartment.getTenantId(), apartmentRequest.getRequestInfo());

			if (isExists)
				throw new DuplicateIdException(apartmentRequest.getRequestInfo());
			apartment.setAuditDetails(auditDetails);
			apartment.getFloor().setAuditDetails(auditDetails);

			for (Unit unit : apartment.getFloor().getUnits()) {

				if (unit.getUnits() != null) {
					if (unit.getUnits().size() > 0) {
						for (Unit room : unit.getUnits()) {
							room.setAuditDetails(auditDetails);
						}
					}
				}
				unit.setAuditDetails(auditDetails);
			}

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(apartment);
				Long id = propertyMasterRepository.createApartment(apartment, data);
				apartment.setId(id);
			} catch (Exception e) {

				throw new InvalidInputException(apartmentRequest.getRequestInfo());
			}
		}

		ApartmentResponse apartmentResponse = new ApartmentResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(apartmentRequest.getRequestInfo(), true);
		apartmentResponse.setResponseInfo(responseInfo);
		apartmentResponse.setApartments(apartmentRequest.getApartments());

		return apartmentResponse;
	}

	@Override
	@Transactional
	public ApartmentResponse updateApartment(ApartmentRequest apartmentRequest) throws Exception {

		for (Apartment apartment : apartmentRequest.getApartments()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(apartmentRequest.getRequestInfo(),
					ConstantUtility.APARTMENT_TABLE_NAME, apartment.getId());
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(apartment.getTenantId(),
					apartment.getCode(), ConstantUtility.APARTMENT_TABLE_NAME, apartment.getId());
			if (isExists)
				throw new DuplicateIdException(apartmentRequest.getRequestInfo());
			validateApartmentData(apartment.getFloor(), apartment.getTenantId(), apartmentRequest.getRequestInfo());
			apartment.setAuditDetails(auditDetails);
			apartment.getFloor().setAuditDetails(auditDetails);

			for (Unit unit : apartment.getFloor().getUnits()) {

				if (unit.getUnits() != null) {
					if (unit.getUnits().size() > 0) {
						for (Unit room : unit.getUnits()) {
							room.setAuditDetails(auditDetails);
						}
					}
				}
				unit.setAuditDetails(auditDetails);
			}

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(apartment);
				propertyMasterRepository.updateApartment(apartment, data);

			} catch (Exception e) {

				throw new InvalidInputException(apartmentRequest.getRequestInfo());
			}
		}

		ApartmentResponse apartmentResponse = new ApartmentResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(apartmentRequest.getRequestInfo(), true);
		apartmentResponse.setResponseInfo(responseInfo);
		apartmentResponse.setApartments(apartmentRequest.getApartments());

		return apartmentResponse;
	}

	@Override
	public ApartmentResponse searchApartment(RequestInfo requestInfo, ApartmentSearchCriteria apartmentSearchCriteria)
			throws Exception {

		List<Apartment> apartments = null;
		try {
			apartments = propertyMasterRepository.searchApartment(apartmentSearchCriteria.getTenantId(),
					apartmentSearchCriteria.getCode(), apartmentSearchCriteria.getName(),
					apartmentSearchCriteria.getIds(), apartmentSearchCriteria.getLiftFacility(),
					apartmentSearchCriteria.getPowerBackUp(), apartmentSearchCriteria.getParkingFacility(),
					apartmentSearchCriteria.getPageSize(), apartmentSearchCriteria.getOffSet());
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		ApartmentResponse apartmentComplexResponse = new ApartmentResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		apartmentComplexResponse.setResponseInfo(responseInfo);
		apartmentComplexResponse.setApartments(apartments);

		return apartmentComplexResponse;
	}

	private AuditDetails getAuditDetail(RequestInfo requestInfo) {

		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();

		AuditDetails auditDetail = new AuditDetails();
		auditDetail.setCreatedBy(userId);
		auditDetail.setCreatedTime(currEpochDate);
		auditDetail.setLastModifiedBy(userId);
		auditDetail.setLastModifiedTime(currEpochDate);
		return auditDetail;
	}

	private AuditDetails getUpdatedAuditDetails(RequestInfo requestInfo, String tableName, Long id) {

		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setLastModifiedBy(userId);
		auditDetails.setLastModifiedTime(currEpochDate);

		propertyMasterRepository.getCreatedAuditDetails(auditDetails, tableName, id);
		return auditDetails;
	}

	private void validateApartmentData(Floor floor, String tenantId, RequestInfo requestInfo) {

		if (floor.getFloorNo() != null) {

			Boolean floorNoExists = propertyMasterRepository.checkWhetherRecordExits(tenantId, floor.getFloorNo(),
					ConstantUtility.FLOOR_TYPE_TABLE_NAME, null);
			if (!floorNoExists) {
				throw new InvalidCodeException(propertiesManager.getInvalidPropertyFloor(), requestInfo);
			}

			for (Unit unit : floor.getUnits()) {

				if (unit.getUnits() != null || unit.getUnits().isEmpty()) {
					for (Unit unitData : unit.getUnits()) {
						validateUnitData(tenantId, unitData, requestInfo);
					}
				}
				validateUnitData(tenantId, unit, requestInfo);

			}
		} else {
			throw new InvalidCodeException(propertiesManager.getInvalidFloorNo(), requestInfo);
		}
	}

	private void validateUnitData(String tenantId, Unit unit, RequestInfo requestInfo) {

		if (unit.getUsage() != null) {
			Boolean usageExists = propertyMasterRepository.checkWhetherRecordExits(tenantId, unit.getUsage(),
					ConstantUtility.USAGE_TYPE_TABLE_NAME, null);
			if (!usageExists) {
				throw new InvalidCodeException(propertiesManager.getInvalidPropertyUsageCode(), requestInfo);
			}
		} else {
			throw new InvalidCodeException(propertiesManager.getInvalidUsage(), requestInfo);
		}

		if (unit.getOccupancyType() != null) {
			Boolean occupancyTypeExists = propertyMasterRepository.checkWhetherRecordExits(tenantId,
					unit.getOccupancyType(), ConstantUtility.OCCUPANCY_TABLE_NAME, null);

			if (!occupancyTypeExists) {
				throw new InvalidCodeException(propertiesManager.getInvalidPropertyOccupancyCode(), requestInfo);
			}
		} else {
			throw new InvalidCodeException(propertiesManager.getInvalidOccupancytype(), requestInfo);
		}

		if (unit.getStructure() != null) {
			Boolean structureExists = propertyMasterRepository.checkWhetherRecordExits(tenantId, unit.getStructure(),
					ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, null);

			if (!structureExists) {
				throw new InvalidCodeException(propertiesManager.getInvalidPropertyStructureCode(), requestInfo);
			}
		} else {
			throw new InvalidCodeException(propertiesManager.getInvalidStructure(), requestInfo);
		}

		if (unit.getAge() != null) {
			Boolean ageExists = propertyMasterRepository.checkWhetherRecordExits(tenantId, unit.getAge(),
					ConstantUtility.DEPRECIATION_TABLE_NAME, null);

			if (!ageExists) {
				throw new InvalidCodeException(propertiesManager.getInvalidPropertyAgeCode(), requestInfo);
			}
		} else {
			throw new InvalidCodeException(propertiesManager.getInvalidAge(), requestInfo);
		}
	}

	@Override
	@Transactional
	public GuidanceValueBoundaryResponse createGuidanceValueBoundary(String tenantId,
			GuidanceValueBoundaryRequest guidanceValueBoundaryRequest) throws Exception {
		// TODO Auto-generated method stub
		for (GuidanceValueBoundary guidanceValueBoundary : guidanceValueBoundaryRequest.getGuidanceValueBoundaries()) {
			AuditDetails auditDetails = getAuditDetail(guidanceValueBoundaryRequest.getRequestInfo());
			guidanceValueBoundary.setAuditDetails(auditDetails);
			Long id = propertyMasterRepository.saveGuidanceValueBoundary(tenantId, guidanceValueBoundary);
			guidanceValueBoundary.setId(id);
		}

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(guidanceValueBoundaryRequest.getRequestInfo(), true);
		GuidanceValueBoundaryResponse guidanceValueBoundaryResponse = new GuidanceValueBoundaryResponse();
		guidanceValueBoundaryResponse.setResponseInfo(responseInfo);
		guidanceValueBoundaryResponse
				.setGuidanceValueBoundaries(guidanceValueBoundaryRequest.getGuidanceValueBoundaries());
		return guidanceValueBoundaryResponse;
	}

	@Override
	@Transactional
	public GuidanceValueBoundaryResponse updateGuidanceValueBoundary(
			GuidanceValueBoundaryRequest guidanceValueBoundaryRequest) throws Exception {

		for (GuidanceValueBoundary guidanceValueBoundary : guidanceValueBoundaryRequest.getGuidanceValueBoundaries()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(guidanceValueBoundaryRequest.getRequestInfo(),
					ConstantUtility.GUIDANCEVALUEBOUNDARY_TABLE_NAME, guidanceValueBoundary.getId());
			guidanceValueBoundary.setAuditDetails(auditDetails);
			try {
				propertyMasterRepository.updateGuidanceValueBoundary(guidanceValueBoundary);
			} catch (Exception e) {
				throw new InvalidInputException(guidanceValueBoundaryRequest.getRequestInfo());
			}
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(guidanceValueBoundaryRequest.getRequestInfo(), true);
		GuidanceValueBoundaryResponse guidanceValueBoundaryResponse = new GuidanceValueBoundaryResponse();
		guidanceValueBoundaryResponse.setResponseInfo(responseInfo);
		guidanceValueBoundaryResponse
				.setGuidanceValueBoundaries(guidanceValueBoundaryRequest.getGuidanceValueBoundaries());
		return guidanceValueBoundaryResponse;
	}

	@Override
	public GuidanceValueBoundaryResponse getGuidanceValueBoundary(RequestInfo requestInfo,
			GuidanceValueBoundarySearchCriteria guidanceValueBoundarySearchCriteria) throws Exception {

		if (guidanceValueBoundarySearchCriteria.getPageSize() == null) {
			// pageSize =
			// Integer.parseInt(propertiesManager.getDefaultPageSize());
			guidanceValueBoundarySearchCriteria.setPageSize(Integer.parseInt(propertiesManager.getDefaultPageSize()));
		}
		if (guidanceValueBoundarySearchCriteria.getOffSet() == null) {
			// offset = Integer.parseInt(propertiesManager.getDefaultOffset());
			guidanceValueBoundarySearchCriteria.setOffSet(Integer.parseInt(propertiesManager.getDefaultOffset()));
		}

		if (guidanceValueBoundarySearchCriteria.getGuidanceValueBoundary1().isEmpty()) {
			throw new InvalidCodeException(propertiesManager.getInvalidGuidanceValueBoundary1(), requestInfo);
		}

		List<GuidanceValueBoundary> guidanceValueBoundaries = propertyMasterRepository.searchGuidanceValueBoundary(
				guidanceValueBoundarySearchCriteria.getTenantId(),
				guidanceValueBoundarySearchCriteria.getGuidanceValueBoundary1(),
				guidanceValueBoundarySearchCriteria.getGuidanceValueBoundary2(),
				guidanceValueBoundarySearchCriteria.getPageSize(), guidanceValueBoundarySearchCriteria.getOffSet());
		GuidanceValueBoundaryResponse guidanceValueBoundaryResponse = new GuidanceValueBoundaryResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		guidanceValueBoundaryResponse.setResponseInfo(responseInfo);
		guidanceValueBoundaryResponse.setGuidanceValueBoundaries(guidanceValueBoundaries);
		return guidanceValueBoundaryResponse;
	}

	@Override
	@Transactional
	public AppConfigurationResponse createAppConfiguration(String tenantId,
			AppConfigurationRequest appConfigurationRequest) throws Exception {
		// TODO Auto-generated method stub
		for (AppConfiguration appConfiguration : appConfigurationRequest.getAppConfigurations()) {
			AuditDetails auditDetails = getAuditDetail(appConfigurationRequest.getRequestInfo());
			appConfiguration.setAuditDetails(auditDetails);

			Boolean isExists = propertyMasterRepository.checkWhetherRecordWithTenantIdAndKeyName(
					appConfiguration.getTenantId(), appConfiguration.getKeyName(),
					ConstantUtility.CONFIGURATION_TABLE_NAME);

			if (isExists)
				throw new InvalidCodeException(propertiesManager.getInvalidAppConfigKey(),
						appConfigurationRequest.getRequestInfo());

			Long id = propertyMasterRepository.saveAppConfiguration(tenantId, appConfiguration);
			for (String value : appConfiguration.getValues()) {
				propertyMasterRepository.saveAppConfigurationValues(tenantId, appConfiguration, id, value);
			}
			appConfiguration.setId(id);
		}

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(appConfigurationRequest.getRequestInfo(), true);
		AppConfigurationResponse appConfigurationResponse = new AppConfigurationResponse();
		appConfigurationResponse.setResponseInfo(responseInfo);
		appConfigurationResponse.setAppConfigurations(appConfigurationRequest.getAppConfigurations());
		return appConfigurationResponse;
	}

	@Override
	@Transactional
	public AppConfigurationResponse updateAppConfiguration(AppConfigurationRequest appConfigurationRequest)
			throws Exception {

		for (AppConfiguration appConfiguration : appConfigurationRequest.getAppConfigurations()) {

			AuditDetails auditDetails = getUpdatedAuditDetails(appConfigurationRequest.getRequestInfo(),
					ConstantUtility.CONFIGURATION_TABLE_NAME, appConfiguration.getId());
			appConfiguration.setAuditDetails(auditDetails);

			try {

				propertyMasterRepository.updateAppConfiguration(appConfiguration);

				List<String> values = jdbcTemplate.queryForList(AppConfigurationBuilder.SELECT_BY_KEYID,
						new Object[] { appConfiguration.getId() }, String.class);

				List<String> deleteValues = new ArrayList<String>();

				List<String> tempValues = new ArrayList<String>();

				values.forEach(val -> {
					int count = 0;
					for (String x : appConfiguration.getValues()) {
						if (val.equalsIgnoreCase(x)) {
							count++;
							// matched values adding to tempArray
							tempValues.add(x);
							break;
						}
					}
					if (count == 0) {
						deleteValues.add(val);
					}
				});

				// adding new request value
				List<String> addValues = appConfiguration.getValues().stream().filter(x -> !tempValues.contains(x))
						.collect(Collectors.toList());

				deleteValues.forEach(value -> {
					jdbcTemplate.update(AppConfigurationBuilder.DELETE_BY_VALUE, new Object[] { value });

				});

				for (String value : addValues) {

					propertyMasterRepository.saveAppConfigurationValues(appConfiguration.getTenantId(),
							appConfiguration, appConfiguration.getId(), value);

				}

			} catch (Exception e) {
				throw new InvalidInputException(appConfigurationRequest.getRequestInfo());
			}
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(appConfigurationRequest.getRequestInfo(), true);
		AppConfigurationResponse appConfigurationResponse = new AppConfigurationResponse();
		appConfigurationResponse.setResponseInfo(responseInfo);
		appConfigurationResponse.setAppConfigurations(appConfigurationRequest.getAppConfigurations());
		return appConfigurationResponse;
	}

	@Override
	public AppConfigurationResponse getAppConfiguration(RequestInfo requestInfo,
			AppConfigurationSearchCriteria appConfigurationSearchCriteria) throws Exception {

		AppConfigurationResponse appConfigurationResponse = new AppConfigurationResponse();

		if (appConfigurationSearchCriteria.getPageSize() == null) {
			appConfigurationSearchCriteria.setPageSize(Integer.parseInt(propertiesManager.getDefaultPageSize()));
		}
		if (appConfigurationSearchCriteria.getOffSet() == null) {
			appConfigurationSearchCriteria.setOffSet(Integer.parseInt(propertiesManager.getDefaultOffset()));
		}

		List<AppConfiguration> appConfigurationList = propertyMasterRepository.searchAppConfiguration(
				appConfigurationSearchCriteria.getTenantId(), appConfigurationSearchCriteria.getIds(),
				appConfigurationSearchCriteria.getKeyName(), appConfigurationSearchCriteria.getEffectiveFrom(),
				appConfigurationSearchCriteria.getPageSize(), appConfigurationSearchCriteria.getOffSet());
		appConfigurationResponse.setAppConfigurations(appConfigurationList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		appConfigurationResponse.setResponseInfo(responseInfo);
		return appConfigurationResponse;
	}
	
	
	@Override
	@Transactional
	public DemolitionReasonResponse createDemolitionReason(String tenantId,
			DemolitionReasonRequest demolitionReasonRequest) throws Exception {
		for (DemolitionReason demolitionReason : demolitionReasonRequest.getDemolitionReasons()) {

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(demolitionReason.getTenantId(),
					demolitionReason.getCode(), ConstantUtility.DEMOLITIONREASON_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(demolitionReasonRequest.getRequestInfo());

			AuditDetails auditDetails = getAuditDetail(demolitionReasonRequest.getRequestInfo());
			demolitionReason.setAuditDetails(auditDetails);
			Long id = propertyMasterRepository.saveDemolitionReason(tenantId, demolitionReason);
			demolitionReason.setId(id);
		}

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(demolitionReasonRequest.getRequestInfo(), true);
		DemolitionReasonResponse demolitionReasonResponse = new DemolitionReasonResponse();
		demolitionReasonResponse.setResponseInfo(responseInfo);
		demolitionReasonResponse.setDemolitionReason(demolitionReasonRequest.getDemolitionReasons());
		return demolitionReasonResponse;
	}

	@Override
	@Transactional
	public DemolitionReasonResponse updateDemolitionReason(DemolitionReasonRequest demolitionReasonRequest)
			throws Exception {

		for (DemolitionReason demolitionReason : demolitionReasonRequest.getDemolitionReasons()) {

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(demolitionReason.getTenantId(),
					demolitionReason.getCode(), ConstantUtility.DEMOLITIONREASON_TABLE_NAME, demolitionReason.getId());

			if (isExists)
				throw new DuplicateIdException(demolitionReasonRequest.getRequestInfo());

			AuditDetails auditDetails = getUpdatedAuditDetails(demolitionReasonRequest.getRequestInfo(),
					ConstantUtility.DEMOLITIONREASON_TABLE_NAME, demolitionReason.getId());
			demolitionReason.setAuditDetails(auditDetails);

			propertyMasterRepository.updateDemolitionReason(demolitionReason);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(demolitionReasonRequest.getRequestInfo(), true);
		DemolitionReasonResponse demolitionReasonResponse = new DemolitionReasonResponse();
		demolitionReasonResponse.setDemolitionReason(demolitionReasonRequest.getDemolitionReasons());
		demolitionReasonResponse.setResponseInfo(responseInfo);

		return demolitionReasonResponse;
	}

	@Override
	public DemolitionReasonResponse getDemolitionReason(RequestInfo requestInfo,
			DemolitionReasonSearchCriteria demolitionReasonSearchCriteria) {

		DemolitionReasonResponse demolitionReasonResponse = new DemolitionReasonResponse();
		try {

			List<DemolitionReason> demolitionReasons = propertyMasterRepository
					.searchDemolitionReasonClass(demolitionReasonSearchCriteria);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			demolitionReasonResponse.setDemolitionReason(demolitionReasons);
			demolitionReasonResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new PropertySearchException("invalid input", requestInfo);
		}

		return demolitionReasonResponse;

	}

	@Override
	public TaxExemptionReasonResponse createTaxExemptionReason(TaxExemptionReasonRequest taxExemptionReasonRequest)
			throws Exception {

		taxExemptionReasonRequest.getTaxExemptionReasons().forEach(taxExemptionReason -> {

			AuditDetails auditDetails = getAuditDetail(taxExemptionReasonRequest.getRequestInfo());
			Boolean isExists = checkCodeAndTenatIdExists(taxExemptionReason.getTenantId(), taxExemptionReason.getCode(),
					ConstantUtility.TAXEXEMPTIONREASON_MASTER_TABLE_NAME, null);
			if (isExists) {
				throw new DuplicateIdException(taxExemptionReasonRequest.getRequestInfo());
			}
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(taxExemptionReason);
			taxExemptionReason.setAuditDetails(auditDetails);
			try {
				Long id = propertyMasterRepository.saveTaxExemptionReason(taxExemptionReason, data);
				taxExemptionReason.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(taxExemptionReasonRequest.getRequestInfo());
			}
		});
		
		TaxExemptionReasonResponse taxExemptionReasonResponse = new TaxExemptionReasonResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(taxExemptionReasonRequest.getRequestInfo(), true);
		taxExemptionReasonResponse.setResponseInfo(responseInfo);
		taxExemptionReasonResponse.setTaxExemptionReasons(taxExemptionReasonRequest.getTaxExemptionReasons());
		return taxExemptionReasonResponse;
	}

	@Override
	public TaxExemptionReasonResponse updateTaxExemptionReason(TaxExemptionReasonRequest taxExemptionReasonRequest)
			throws Exception {

		taxExemptionReasonRequest.getTaxExemptionReasons().forEach(taxExemptionReason -> {

			AuditDetails auditDetails = getUpdatedAuditDetails(taxExemptionReasonRequest.getRequestInfo(),
					ConstantUtility.TAXEXEMPTIONREASON_MASTER_TABLE_NAME, taxExemptionReason.getId());
			taxExemptionReason.setAuditDetails(auditDetails);
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(taxExemptionReason.getTenantId(),
					taxExemptionReason.getCode(), ConstantUtility.TAXEXEMPTIONREASON_MASTER_TABLE_NAME,
					taxExemptionReason.getId());
			if (isExists) {
				throw new DuplicateIdException(taxExemptionReasonRequest.getRequestInfo());
			}
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(taxExemptionReason);

			try {
				propertyMasterRepository.updateTaxExemptionReason(taxExemptionReason, data);
			} catch (Exception e) {
				throw new InvalidInputException(taxExemptionReasonRequest.getRequestInfo());
			}
		});

		TaxExemptionReasonResponse taxExemptionReasonResponse = new TaxExemptionReasonResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(taxExemptionReasonRequest.getRequestInfo(), true);
		taxExemptionReasonResponse.setResponseInfo(responseInfo);
		taxExemptionReasonResponse.setTaxExemptionReasons(taxExemptionReasonRequest.getTaxExemptionReasons());
		return taxExemptionReasonResponse;
	}

	@Override
	public TaxExemptionReasonResponse getTaxExemptionReason(RequestInfo requestInfo,
			TaxExemptionReasonSearchCriteria taxExemptionReasonSearchCriteria) throws Exception {

		List<TaxExemptionReason> taxExemptionReasons = null;
		try {
			taxExemptionReasons = propertyMasterRepository.searchTaxExemptionReason(taxExemptionReasonSearchCriteria);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		TaxExemptionReasonResponse taxExemptionReasonResponse = new TaxExemptionReasonResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		taxExemptionReasonResponse.setResponseInfo(responseInfo);
		taxExemptionReasonResponse.setTaxExemptionReasons(taxExemptionReasons);
		return taxExemptionReasonResponse;
	}
}