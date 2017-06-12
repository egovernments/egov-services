package org.egov.property.services;

import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.OccuapancyMasterRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.WallTypeRequest;
import org.egov.models.WallTypeResponse;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;

public interface Masterservice {



	public DepartmentResponseInfo createDepartmentMaster(String tenantId, DepartmentRequest departmentRequest);

	public DepartmentResponseInfo updateDepartmentMaster(String tenantId, Long id, DepartmentRequest departmentRequest);

	public DepartmentResponseInfo getDepartmentMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String category, String name, String code, String nameLocal, Integer pageSize, Integer offSet);

	public FloorTypeResponse getFloorTypeMaster(RequestInfo requestInfo, String tenantId,Integer []ids, String name ,String code,String nameLocal, Integer pageSize, Integer offSet) throws Exception;

	public FloorTypeResponse createFloorType(FloorTypeRequest floorTypeRequest,String tenantId) throws Exception;

	public FloorTypeResponse updateFloorType(FloorTypeRequest floorTypeRequest,String tenantId,Integer id) throws Exception;

	// Wood Types

	public WoodTypeResponse getWoodTypes (RequestInfo requestInfo,String tenantId,Integer []ids, String name ,String code,String nameLocal, Integer pageSize, Integer offSet) throws Exception;

	public WoodTypeResponse createWoodType(WoodTypeRequest woodTypeRequest,String tenantId) throws Exception;

	public WoodTypeResponse updateWoodType(WoodTypeRequest woodTypeRequest,String tenantId,Integer id) throws Exception;


	// Roof Types

	public RoofTypeResponse getRoofypes (RequestInfo requestInfo,String tenantId,Integer []ids, String name ,String code,String nameLocal, Integer pageSize, Integer offSet) throws Exception;

	public RoofTypeResponse createRoofype(RoofTypeRequest roofTypeRequest,String tenantId) throws Exception;

	public RoofTypeResponse updateRoofType(RoofTypeRequest roofTypeRequest,String tenantId,Integer id) throws Exception;

	public StructureClassResponse craeateStructureClassMaster(String tenantId, StructureClassRequest structureClassRequest);

	public StructureClassResponse updateStructureClassMaster(String tenantId, Long id,
			StructureClassRequest structureClassRequest);

	public StructureClassResponse getStructureClassMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name , String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize, Integer offSet);

	public PropertyTypeResponse createPropertyTypeMaster(String tenantId, PropertyTypeRequest propertyTypeRequest);

	public PropertyTypeResponse updatePropertyTypeMaster(String tenantId, Long id, PropertyTypeRequest propertyTypeRequest);


	public PropertyTypeResponse getPropertyTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet);

	public OccuapancyMasterResponse createOccuapancyMaster(String tenantId,
			OccuapancyMasterRequest occuapancyMasterRequest);

	public OccuapancyMasterResponse updateOccuapancyMaster(String tenantId, Long id,
			OccuapancyMasterRequest occuapancyRequest);

	public OccuapancyMasterResponse getOccuapancyMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet);

	public WallTypeResponse getWallTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception;

	public WallTypeResponse createWallTypeMaster(String tenantId, WallTypeRequest wallTypeRequest) throws Exception;

	public WallTypeResponse updateWallTypeMaster(String tenantId, Long id, WallTypeRequest wallTypeRequest) throws Exception;


	public UsageMasterResponse getUsageMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name, String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception;

	public UsageMasterResponse createUsageMaster(String tenantId, UsageMasterRequest usageMasterRequest) throws Exception;

	public UsageMasterResponse updateUsageMaster(String tenantId, Long id, UsageMasterRequest usageMasterRequest) throws Exception;

}



