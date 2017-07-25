package org.egov.property.services;

import java.util.Date;
import java.util.List;

import org.egov.models.Department;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.Depreciation;
import org.egov.models.DepreciationRequest;
import org.egov.models.DepreciationResponse;
import org.egov.models.DocumentType;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.FloorType;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.MutationMaster;
import org.egov.models.MutationMasterRequest;
import org.egov.models.MutationMasterResponse;
import org.egov.models.OccuapancyMaster;
import org.egov.models.OccuapancyMasterRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyType;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
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
import org.egov.property.exception.DuplicateIdException;
import org.egov.property.exception.InvalidInputException;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.model.ExcludeFileds;
import org.egov.property.repository.PropertyMasterRepository;
import org.egov.property.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	public FloorTypeResponse getFloorTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		List<FloorType> floorTypes = null;
		FloorTypeResponse floorTypeResponse = new FloorTypeResponse();

		try {
			floorTypes = propertyMasterRepository.searchFloorType(tenantId, ids, name, code, nameLocal, pageSize,
					offSet);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(floorType.getTenantId(),
					floorType.getCode(), ConstantUtility.FLOOR_TYPE_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(floorTypeRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(floorType);
				Long createdTime = new Date().getTime();

				Long id = propertyMasterRepository.saveFloorType(floorType, data);
				floorType.setId(id);
				floorType.getAuditDetails().setCreatedTime(createdTime);
				floorType.getAuditDetails().setLastModifiedTime(createdTime);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(floorType.getTenantId(),
					floorType.getCode(), ConstantUtility.FLOOR_TYPE_TABLE_NAME, floorType.getId());

			if (isExists)
				throw new DuplicateIdException(floorTypeRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(floorType);

				Long updatedTime = new Date().getTime();
				floorType = propertyMasterRepository.updateFloorType(floorType, data);
				floorType.getAuditDetails().setLastModifiedTime(updatedTime);
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
	public WoodTypeResponse getWoodTypes(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		WoodTypeResponse woodTypeResponse = new WoodTypeResponse();

		try {

			List<WoodType> woodTypes = propertyMasterRepository.searchWoodType(tenantId, ids, name, code, nameLocal,
					pageSize, offSet);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(woodType.getTenantId(),
					woodType.getCode(), ConstantUtility.WOOD_TYPE_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(woodTypeRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(woodType);

				Long createdTime = new Date().getTime();
				Long id = propertyMasterRepository.saveWoodType(woodType, data);
				woodType.setId(id);
				woodType.getAuditDetails().setCreatedTime(createdTime);
				woodType.getAuditDetails().setLastModifiedTime(createdTime);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(woodType.getTenantId(),
					woodType.getCode(), ConstantUtility.WOOD_TYPE_TABLE_NAME, woodType.getId());
			if (isExists)
				throw new DuplicateIdException(woodTypeRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(woodType);

				Long updatedTime = new Date().getTime();
				woodType = propertyMasterRepository.updateWoodType(woodType, data);
				woodType.getAuditDetails().setLastModifiedTime(updatedTime);
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
	public RoofTypeResponse getRoofypes(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		RoofTypeResponse roofTypeResponse = new RoofTypeResponse();

		try {

			List<RoofType> roofTypes = propertyMasterRepository.searchRoofType(tenantId, ids, name, code, nameLocal,
					pageSize, offSet);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(roofType.getTenantId(),
					roofType.getCode(), ConstantUtility.ROOF_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(roofTypeRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(roofType);

				Long createdTime = new Date().getTime();
				Long id = propertyMasterRepository.saveRoofType(roofType, data);
				roofType.setId(id);
				roofType.getAuditDetails().setCreatedTime(createdTime);
				roofType.getAuditDetails().setLastModifiedTime(createdTime);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(roofType.getTenantId(),
					roofType.getCode(), ConstantUtility.ROOF_TYPE_TABLE_NAME, roofType.getId());

			if (isExists)
				throw new DuplicateIdException(roofTypeRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(roofType);

				Long updatedTime = new Date().getTime();
				roofType = propertyMasterRepository.updateRoofType(roofType, data);
				roofType.getAuditDetails().setLastModifiedTime(updatedTime);
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

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(department);		
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(department.getTenantId(),
					department.getCode(), ConstantUtility.DEPARTMENT_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(departmentRequest.getRequestInfo());

			Long createdTime = new Date().getTime();
			Long id = propertyMasterRepository.saveDepartment(tenantId, department, data);

			department.setId(id);
			department.getAuditDetails().setCreatedTime(createdTime);
			department.getAuditDetails().setLastModifiedTime(createdTime);

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
		Long modifiedTime = new Date().getTime();

		for (Department department : departmentRequest.getDepartments()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(department);

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(department.getTenantId(),
					department.getCode(), ConstantUtility.DEPARTMENT_TABLE_NAME, department.getId());

			if (isExists)
				throw new DuplicateIdException(departmentRequest.getRequestInfo());

			department = propertyMasterRepository.updateDepartment(department, data, department.getId());
			department.getAuditDetails().setLastModifiedTime(modifiedTime);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(departmentRequest.getRequestInfo(), true);

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		departmentResponse.setDepartments(departmentRequest.getDepartments());
		departmentResponse.setResponseInfo(responseInfo);
		return departmentResponse;
	}

	@Override
	public DepartmentResponseInfo getDepartmentMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String category, String name, String code, String nameLocal, Integer pageSize, Integer offSet) {

		DepartmentResponseInfo departmentResponse = new DepartmentResponseInfo();
		List<Department> departments = null;
		try {

			departments = propertyMasterRepository.searchDepartment(tenantId, ids, name, code, nameLocal, pageSize,
					offSet);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(occuapancy.getTenantId(),
					occuapancy.getCode(), ConstantUtility.OCCUPANCY_TABLE_NAME, null);
			if (isExists)
				throw new DuplicateIdException(occuapancyMasterRequest.getRequestInfo());

			Long createdTime = new Date().getTime();

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(occuapancy);

			Long id = propertyMasterRepository.saveOccuapancy(tenantId, occuapancy, data);

			occuapancy.setId(id);
			occuapancy.getAuditDetails().setCreatedTime(createdTime);
			occuapancy.getAuditDetails().setLastModifiedTime(createdTime);

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

		Long modifiedTime = new Date().getTime();

		for (OccuapancyMaster occuapancyMaster : occuapancyRequest.getOccuapancyMasters()) {

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(occuapancyMaster.getTenantId(),
					occuapancyMaster.getCode(), ConstantUtility.OCCUPANCY_TABLE_NAME, occuapancyMaster.getId());
			if (isExists)
				throw new DuplicateIdException(occuapancyRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(occuapancyMaster);

			occuapancyMaster = propertyMasterRepository.updateOccuapancy(occuapancyMaster, data);

			occuapancyMaster.getAuditDetails().setLastModifiedTime(modifiedTime);

		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(occuapancyRequest.getRequestInfo(), true);

		OccuapancyMasterResponse occuapancyResponse = new OccuapancyMasterResponse();
		occuapancyResponse.setOccuapancyMasters(occuapancyRequest.getOccuapancyMasters());
		occuapancyResponse.setResponseInfo(responseInfo);
		return occuapancyResponse;
	}

	public OccuapancyMasterResponse getOccuapancyMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet) {

		OccuapancyMasterResponse occupapancyMasterResponse = new OccuapancyMasterResponse();

		List<OccuapancyMaster> occupapancyMasters = null;
		try {

			occupapancyMasters = propertyMasterRepository.searchOccupancy(requestInfo, tenantId, ids, name, code,
					nameLocal, active, orderNumber, pageSize, offSet);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(propertyType.getTenantId(),
					propertyType.getCode(), ConstantUtility.PROPERTY_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(propertyTypeRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(propertyType);
			Long createdTime = new Date().getTime();
			Long id = propertyMasterRepository.savePropertyType(tenantId, propertyType, data);
			propertyType.setId(id);
			propertyType.getAuditDetails().setCreatedTime(createdTime);
			propertyType.getAuditDetails().setLastModifiedTime(createdTime);

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

		Long modifiedTime = new Date().getTime();

		for (PropertyType propertyType : propertyTypeRequest.getPropertyTypes()) {

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(propertyType.getTenantId(),
					propertyType.getCode(), ConstantUtility.PROPERTY_TYPE_TABLE_NAME, propertyType.getId());

			if (isExists)
				throw new DuplicateIdException(propertyTypeRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(propertyType);

			propertyType = propertyMasterRepository.updatePropertyType(propertyType, data);

			propertyType.getAuditDetails().setLastModifiedTime(modifiedTime);

		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(propertyTypeRequest.getRequestInfo(), true);

		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();
		propertyTypeResponse.setPropertyTypes(propertyTypeRequest.getPropertyTypes());
		propertyTypeResponse.setResponseInfo(responseInfo);
		return propertyTypeResponse;
	}

	@Override
	public PropertyTypeResponse getPropertyTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet) {
		PropertyTypeResponse propertyTypeResponse = new PropertyTypeResponse();
		List<PropertyType> propertyTypes = null;
		try {

			propertyTypes = propertyMasterRepository.searchPropertyType(requestInfo, tenantId, ids, name, code,
					nameLocal, active, orderNumber, pageSize, offSet);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(usageMaster.getTenantId(),
					usageMaster.getCode(), ConstantUtility.USAGE_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(usageMasterRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(usageMaster);

				Long createdTime = new Date().getTime();
				Long id = propertyMasterRepository.saveUsageMaster(usageMaster, data);
				usageMaster.setId(id);

				usageMaster.getAuditDetails().setCreatedTime(createdTime);
				usageMaster.getAuditDetails().setLastModifiedTime(createdTime);

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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(usageMaster.getTenantId(),
					usageMaster.getCode(), ConstantUtility.USAGE_TYPE_TABLE_NAME, usageMaster.getId());

			if (isExists)
				throw new DuplicateIdException(usageMasterRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(usageMaster);

				Long updatedTime = new Date().getTime();

				usageMaster = propertyMasterRepository.updateUsageMaster(usageMaster, data);

				usageMaster.getAuditDetails().setLastModifiedTime(updatedTime);

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
	public UsageMasterResponse getUsageMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Boolean active, Boolean isResidential, Integer orderNumber, Integer pageSize,
			Integer offSet) throws Exception {

		UsageMasterResponse usageMasterResponse = new UsageMasterResponse();

		try {
			List<UsageMaster> usageList = propertyMasterRepository.searchUsage(tenantId, ids, name, code, nameLocal,
					active, isResidential, orderNumber, pageSize, offSet);
			usageMasterResponse.setUsageMasters(usageList);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			usageMasterResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {

			throw new PropertySearchException("invalid input", requestInfo);
		}

		return usageMasterResponse;
	}

	@Override
	@Transactional
	public WallTypeResponse createWallTypeMaster(String tenantId, WallTypeRequest wallTypeRequest) throws Exception {

		for (WallType wallType : wallTypeRequest.getWallTypes()) {

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(wallType.getTenantId(),
					wallType.getCode(), ConstantUtility.WALL_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(wallTypeRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(wallType);

				Long createTime = new Date().getTime();
				Long id = propertyMasterRepository.saveWallTypes(wallType, data);
				wallType.setId(id);
				wallType.getAuditDetails().setCreatedTime(createTime);
				wallType.getAuditDetails().setLastModifiedTime(createTime);

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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(wallType.getTenantId(),
					wallType.getCode(), ConstantUtility.WALL_TYPE_TABLE_NAME, wallType.getId());

			if (isExists)
				throw new DuplicateIdException(wallTypeRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(wallType);

				Long updatedTime = new Date().getTime();

				wallType = propertyMasterRepository.updateWallTypes(wallType, data);

				wallType.getAuditDetails().setLastModifiedTime(updatedTime);

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
	public WallTypeResponse getWallTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		WallTypeResponse wallTypeResponse = new WallTypeResponse();

		try {

			List<WallType> wallTypes = propertyMasterRepository.searchWallType(tenantId, ids, name, code, nameLocal,
					pageSize, offSet);
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(structureClass.getTenantId(),
					structureClass.getCode(), ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(structureClassRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(structureClass);

				Long createdTime = new Date().getTime();
				Long id = propertyMasterRepository.saveStructureClsses(tenantId, structureClass, data);
				structureClass.setId(id);
				structureClass.getAuditDetails().setCreatedTime(createdTime);
				structureClass.getAuditDetails().setLastModifiedTime(createdTime);

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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(structureClass.getTenantId(),
					structureClass.getCode(), ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, structureClass.getId());

			if (isExists)
				throw new DuplicateIdException(structureClassRequest.getRequestInfo());

			try {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(structureClass);

				Long updatedTime = new Date().getTime();

				structureClass = propertyMasterRepository.updateStructureClsses(structureClass, data);

				structureClass.getAuditDetails().setLastModifiedTime(updatedTime);

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
	public StructureClassResponse getStructureClassMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet) {
		StructureClassResponse structureClassResponse = new StructureClassResponse();
		try {
			List<StructureClass> structureClasses = propertyMasterRepository.searchStructureClass(tenantId, ids, name,
					code, nameLocal, active, orderNumber, pageSize, offSet);
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

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(depreciation);

			Boolean isExists = checkCodeAndTenatIdExists(depreciation.getTenantId(), depreciation.getCode(),
					ConstantUtility.DEPRECIATION_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(depreciationRequest.getRequestInfo());

			try {
				Long id = propertyMasterRepository.createDepreciation(depreciation, data);
				depreciation.setId(id);

			}

			catch (Exception e) {
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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(depreciation.getTenantId(),
					depreciation.getCode(), ConstantUtility.DEPRECIATION_TABLE_NAME, depreciation.getId());

			if (isExists)
				throw new DuplicateIdException(depreciationRequest.getRequestInfo());

			try {

				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
				String data = gson.toJson(depreciation);

				Long updatedTime = new Date().getTime();

				propertyMasterRepository.updateDepreciation(depreciation, data);
				depreciation.getAuditDetails().setLastModifiedTime(updatedTime);

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
	public DepreciationResponse searchDepreciation(RequestInfo requestInfo, String tenantId, Integer[] ids,
			Integer fromYear, Integer toYear, String code, String nameLocal, Integer pageSize, Integer offset)
			throws Exception {

		List<Depreciation> depreciations = propertyMasterRepository.searchDepreciations(tenantId, ids, fromYear, toYear,
				code, nameLocal, pageSize, offset);
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

			Boolean isExists = checkCodeAndTenatIdExists(muatation.getTenantId(), muatation.getCode(),
					ConstantUtility.MUTATION_MASTER_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(mutationMasterRequest.getRequestInfo());

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(muatation);

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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(mutation.getTenantId(),
					mutation.getCode(), ConstantUtility.DEPRECIATION_TABLE_NAME, mutation.getId());

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
	public MutationMasterResponse searchMutationMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception {

		List<MutationMaster> mutationMasters = null;
		try {
			mutationMasters = propertyMasterRepository.searchMutation(tenantId, ids, name, code, nameLocal, pageSize,
					offSet);
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
	public DocumentTypeResponse createDocumentTypeMaster(String tenantId, DocumentTypeRequest documentTypeRequest) throws Exception {
		for (DocumentType documentType : documentTypeRequest.getDocumentType()) {
			
			Boolean isExists = propertyMasterRepository.checkWhetherRecordWithTenantIdAndNameExits(documentType.getTenantId(),
					documentType.getCode(), documentType.getApplication().toString(),
					ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, null);

			if (isExists)
				throw new DuplicateIdException(documentTypeRequest.getRequestInfo());

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

			Boolean isExists = propertyMasterRepository.checkWhetherRecordWithTenantIdAndNameExits(documentType.getTenantId(),
					documentType.getCode(), documentType.getApplication().toString(),
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
	public DocumentTypeResponse searchDocumentTypeMaster(RequestInfo requestInfo, String tenantId, String name, String code,
			String application, Integer pageSize, Integer OffSet) throws Exception {
		List<DocumentType> documentTypes = null;
		try {
			documentTypes = propertyMasterRepository.searchDocumentTypeMaster(tenantId, name, code, application, pageSize, OffSet);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		documentTypeResponse.setResponseInfo(responseInfo);
		documentTypeResponse.setDocumentType(documentTypes);

		return documentTypeResponse;
	}
}