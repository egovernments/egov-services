package org.egov.property.services;

import java.util.Date;
import java.util.List;

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
import org.egov.property.exception.InvalidInputException;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.model.ExcludeFileds;
import org.egov.property.repository.PropertyMasterRepository;
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
	public FloorTypeResponse createFloorType(FloorTypeRequest floorTypeRequest, String tenantId) throws Exception {

		for (FloorType floorType : floorTypeRequest.getFloorTypes()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(floorType);
			try {
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
	public FloorTypeResponse updateFloorType(FloorTypeRequest floorTypeRequest) throws Exception {

		for (FloorType floorType : floorTypeRequest.getFloorTypes()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(floorType);
			try {
				long updatedTime = new Date().getTime();
				propertyMasterRepository.updateFloorType(floorType, data);
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
	public WoodTypeResponse createWoodType(WoodTypeRequest woodTypeRequest, String tenantId) throws Exception {

		for (WoodType woodType : woodTypeRequest.getWoodTypes()) {
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(woodType);

			try {
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
	public WoodTypeResponse updateWoodType(WoodTypeRequest woodTypeRequest) throws Exception {

		for (WoodType woodType : woodTypeRequest.getWoodTypes()) {
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
	public RoofTypeResponse createRoofype(RoofTypeRequest roofTypeRequest, String tenantId) throws Exception {

		for (RoofType roofType : roofTypeRequest.getRoofTypes()) {
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(roofType);
			try {
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
	public RoofTypeResponse updateRoofType(RoofTypeRequest roofTypeRequest) throws Exception {
		for (RoofType roofType : roofTypeRequest.getRoofTypes()) {
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

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(department);

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

			propertyMasterRepository.updateDepartment(department, data, department.getId());
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
	public OccuapancyMasterResponse createOccuapancyMaster(String tenantId,
			OccuapancyMasterRequest occuapancyMasterRequest) {
		// TODO Auto-generated method stub

		for (OccuapancyMaster occuapancy : occuapancyMasterRequest.getOccuapancyMasters()) {

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
	public OccuapancyMasterResponse updateOccuapancyMaster(OccuapancyMasterRequest occuapancyRequest) {

		Long modifiedTime = new Date().getTime();

		for (OccuapancyMaster occuapancyMaster : occuapancyRequest.getOccuapancyMasters()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(occuapancyMaster);

			propertyMasterRepository.updateOccuapancy(occuapancyMaster, data);

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
	public PropertyTypeResponse createPropertyTypeMaster(String tenantId, PropertyTypeRequest propertyTypeRequest) {
		// TODO Auto-generated method stub

		for (PropertyType propertyType : propertyTypeRequest.getPropertyTypes()) {

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
	public PropertyTypeResponse updatePropertyTypeMaster(PropertyTypeRequest propertyTypeRequest) {

		Long modifiedTime = new Date().getTime();

		for (PropertyType propertyType : propertyTypeRequest.getPropertyTypes()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(propertyType);

			propertyMasterRepository.updatePropertyType(propertyType, data);

			propertyType.getAuditDetails().setCreatedTime(modifiedTime);

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
	public UsageMasterResponse createUsageMaster(String tenantId, UsageMasterRequest usageMasterRequest)
			throws Exception {

		for (UsageMaster usageMaster : usageMasterRequest.getUsageMasters()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(usageMaster);

			try {

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
	public UsageMasterResponse updateUsageMaster(UsageMasterRequest usageMasterRequest) {

		for (UsageMaster usageMaster : usageMasterRequest.getUsageMasters()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(usageMaster);

			try {

				long updatedTime = new Date().getTime();

				propertyMasterRepository.updateUsageMaster(usageMaster, data);

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
	public WallTypeResponse createWallTypeMaster(String tenantId, WallTypeRequest wallTypeRequest) throws Exception {

		for (WallType wallType : wallTypeRequest.getWallTypes()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(wallType);

			try {

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
	public WallTypeResponse updateWallTypeMaster(WallTypeRequest wallTypeRequest) throws Exception {

		for (WallType wallType : wallTypeRequest.getWallTypes()) {

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(wallType);

			try {

				long updatedTime = new Date().getTime();

				propertyMasterRepository.updateWallTypes(wallType, data);

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

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();

			String data = gson.toJson(structureClass);

			try {

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

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcludeFileds()).serializeNulls().create();
			String data = gson.toJson(structureClass);

			try {

				long updatedTime = new Date().getTime();

				propertyMasterRepository.updateStructureClsses(structureClass, data);

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
}