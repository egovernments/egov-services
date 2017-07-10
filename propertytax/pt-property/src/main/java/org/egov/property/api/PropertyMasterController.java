package org.egov.property.api;

import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.DepreciationRequest;
import org.egov.models.DepreciationResponse;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.OccuapancyMasterRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfoWrapper;
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
import org.egov.property.services.Masterservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to master
 * 
 * @author Narendra
 *
 */

@RestController
@RequestMapping(path = "/property")
public class PropertyMasterController {

	@Autowired
	Masterservice masterService;

	@RequestMapping(path = "/departments/_create", method = RequestMethod.POST)
	public DepartmentResponseInfo createDepartmentMaster(@RequestParam String tenantId,
			@RequestBody DepartmentRequest departmentRequest) {

		return masterService.createDepartmentMaster(tenantId, departmentRequest);

	}

	@RequestMapping(path = "/departments/_update", method = RequestMethod.POST)
	public DepartmentResponseInfo updateDepartmentMaster(@RequestBody DepartmentRequest departmentRequest) {

		return masterService.updateDepartmentMaster(departmentRequest);

	}

	@RequestMapping(path = "/departments/_search", method = RequestMethod.POST)
	public DepartmentResponseInfo getDeparmentMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String category, @RequestParam(required = false) String name,
			@RequestParam(required = false) String code, @RequestParam(required = false) String nameLocal,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offSet)
			throws Exception {
		return masterService.getDepartmentMaster(requestInfo.getRequestInfo(), tenantId, ids, category, name, code,
				nameLocal, pageSize, offSet);

	}

	/**
	 * Description : This api for getting floor master details
	 * 
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponseInfo
	 * @throws Exception
	 */

	@RequestMapping(path = "/floortypes/_search", method = RequestMethod.POST)
	public FloorTypeResponse getFloorTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {
		return masterService.getFloorTypeMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal,
				pageSize, offSet);

	}

	/**
	 * <p>
	 * This API will create the floorType
	 * <p>
	 * 
	 * @param floorTypeRequest
	 * @param tenantId
	 * @return {@link FloorTypeResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/floortypes/_create", method = RequestMethod.POST)
	public FloorTypeResponse createFloorType(@RequestBody FloorTypeRequest floorTypeRequest,
			@RequestParam(required = true) String tenantId) throws Exception {
		return masterService.createFloorType(floorTypeRequest, tenantId);
	}

	/**
	 * <p>
	 * This API will update the floor Type
	 * <p>
	 * 
	 * @param floorTypeRequest
	 * @return {@link FloorTypeResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "floortypes/_update")
	public FloorTypeResponse updateFloorType(@RequestBody FloorTypeRequest floorTypeRequest) throws Exception {

		return masterService.updateFloorType(floorTypeRequest);
	}

	/**
	 * <p>
	 * This API will search the wood types
	 * </p>
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/woodtypes/_search", method = RequestMethod.POST)
	public WoodTypeResponse searchWoodType(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return masterService.getWoodTypes(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal, pageSize,
				offSet);

	}

	/**
	 * <p>
	 * This API will create the wood type response
	 * <p>
	 * 
	 * @param woodTypeRequest
	 * @param tenantId
	 * @return {@link WoodTypeResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/woodtypes/_create", method = RequestMethod.POST)
	public WoodTypeResponse createWoodType(@RequestBody WoodTypeRequest woodTypeRequest,
			@RequestParam(required = true) String tenantId) throws Exception {

		return masterService.createWoodType(woodTypeRequest, tenantId);
	}

	/**
	 * <p>
	 * This API will update the woodType
	 * <p>
	 * 
	 * @param woodTypeRequest
	 * @return {@link WoodTypeResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/woodtypes/_update")
	public WoodTypeResponse updateWoodType(@RequestBody WoodTypeRequest woodTypeRequest) throws Exception {

		return masterService.updateWoodType(woodTypeRequest);
	}

	// Roof types
	/**
	 * <p>
	 * This Api
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(path = "/rooftypes/_search", method = RequestMethod.POST)
	public RoofTypeResponse searchRoofType(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return masterService.getRoofypes(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal, pageSize,
				offSet);

	}

	/**
	 * This Api will create the roof type
	 * 
	 * @param roofTypeRequest
	 * @param tenantId
	 * @return {@link RoofTypeResponse}
	 * @throws Exception
	 */

	@RequestMapping(path = "/rooftypes/_create", method = RequestMethod.POST)
	public RoofTypeResponse createRoofType(@RequestBody RoofTypeRequest roofTypeRequest,
			@RequestParam(required = true) String tenantId) throws Exception {

		return masterService.createRoofype(roofTypeRequest, tenantId);
	}

	/**
	 * This API will update the roof type
	 * 
	 * @param roofTypeRequest
	 * @return {@link RoofTypeResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/rooftypes/_update", method = RequestMethod.POST)
	public RoofTypeResponse updateRoofType(@RequestBody RoofTypeRequest roofTypeRequest) throws Exception {

		return masterService.updateRoofType(roofTypeRequest);
	}

	/**
	 * Description : This api for creating strctureClass master
	 * 
	 * @param tenantId
	 * @param StructureClassRequest
	 * @return structureClassResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "structureclasses/_create", method = RequestMethod.POST)
	public StructureClassResponse craeateStructureClassMaster(@RequestParam(required = true) String tenantId,
			@RequestBody StructureClassRequest structureClassRequest) {

		return masterService.craeateStructureClassMaster(tenantId, structureClassRequest);

	}

	/**
	 * Description : This api for updating strctureClass master
	 * 
	 * 
	 * @param StructureClassRequest
	 * @return structureClassResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "structureclasses/_update", method = RequestMethod.POST)
	public StructureClassResponse updateStructureClassMaster(@RequestBody StructureClassRequest structureClassRequest) {

		return masterService.updateStructureClassMaster(structureClassRequest);

	}

	/**
	 * Description : This api for searching strctureClass master
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
	 * @return structureClassResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "structureclasses/_search", method = RequestMethod.POST)
	public StructureClassResponse getStructureClassMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Boolean active,
			@RequestParam(required = false) Integer orderNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {
		return masterService.getStructureClassMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal,
				active, orderNumber, pageSize, offSet);

	}

	/**
	 * Description : This api for creating propertyType master
	 * 
	 * @param tenantId
	 * @param propertyTypeRequest
	 * @return
	 */

	@RequestMapping(path = "/propertytypes/_create", method = RequestMethod.POST)
	public PropertyTypeResponse createPropertyTypeMaster(@RequestParam(required = true) String tenantId,
			@RequestBody PropertyTypeRequest propertyTypeRequest) {

		return masterService.createPropertyTypeMaster(tenantId, propertyTypeRequest);

	}

	/**
	 * Description : This api for updating propertyType master
	 * 
	 * 
	 * 
	 * @param propertyTypeRequest
	 * @return
	 */

	@RequestMapping(path = "/propertytypes/_update", method = RequestMethod.POST)
	public PropertyTypeResponse updatePropertyTypeMaster(@RequestBody PropertyTypeRequest propertyTypeRequest) {

		return masterService.updatePropertyTypeMaster(propertyTypeRequest);

	}

	/**
	 * Description : This api for searching propertyType master
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
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(path = "/propertytypes/_search", method = RequestMethod.POST)
	public PropertyTypeResponse getPropertyTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Boolean active,
			@RequestParam(required = false) Integer orderNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {
		return masterService.getPropertyTypeMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal,
				active, orderNumber, pageSize, offSet);

	}

	/**
	 * Description : This api for creating occupancy Type master
	 * 
	 * @param tenantId
	 * @param occuapancyMasterRequest
	 * @return
	 */

	@RequestMapping(path = "/occuapancies/_create", method = RequestMethod.POST)
	public OccuapancyMasterResponse createOccuapancyMaster(@RequestParam(required = true) String tenantId,
			@RequestBody OccuapancyMasterRequest occuapancyMasterRequest) {

		return masterService.createOccuapancyMaster(tenantId, occuapancyMasterRequest);

	}

	/**
	 * Description : This api for updating occupancyType master
	 * 
	 * @param occuapancyRequest
	 * @return
	 */

	@RequestMapping(path = "/occuapancies/_update", method = RequestMethod.POST)
	public OccuapancyMasterResponse updateOccuapancyMaster(@RequestBody OccuapancyMasterRequest occuapancyRequest) {

		return masterService.updateOccuapancyMaster(occuapancyRequest);

	}

	/**
	 * Description : This api for searching occupancy type master
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
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(path = "/occuapancies/_search", method = RequestMethod.POST)
	public OccuapancyMasterResponse getOccuapancyMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Boolean active,
			@RequestParam(required = false) Integer orderNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {
		return masterService.getOccuapancyMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal,
				active, orderNumber, pageSize, offSet);

	}

	/**
	 * Description : This api for getting wall type master details
	 * 
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/walltypes/_search", method = RequestMethod.POST)
	public WallTypeResponse getWallTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String category, @RequestParam(required = false) String name,
			@RequestParam(required = false) String code, @RequestParam(required = false) String nameLocal,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offSet)
			throws Exception {

		return masterService.getWallTypeMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal,
				pageSize, offSet);

	}

	/**
	 * Description : Create new walltype(s)
	 * 
	 * @param tenantId
	 * @param wallTypeRequest
	 * @return WallTypeResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/walltypes/_create", method = RequestMethod.POST)
	public WallTypeResponse createWallTypeMaster(@RequestParam(required = true) String tenantId,
			@RequestBody WallTypeRequest wallTypeRequest) throws Exception {

		return masterService.createWallTypeMaster(tenantId, wallTypeRequest);
	}

	/**
	 * Description : Update any of the walltypes
	 * 
	 * @param wallTypeRequest
	 * @return WallTypeResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/walltypes/_update", method = RequestMethod.POST)
	public WallTypeResponse updateWallTypeMaster(@RequestBody WallTypeRequest wallTypeRequest) throws Exception {

		return masterService.updateWallTypeMaster(wallTypeRequest);

	}

	/**
	 * Description : This api for getting usage type master details
	 * 
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/usages/_search", method = RequestMethod.POST)
	public UsageMasterResponse getUsageMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Boolean active,
			@RequestParam(required = false) Boolean isResidential, @RequestParam(required = false) Integer orderNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offSet)
			throws Exception {

		return masterService.getUsageMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal, active,
				isResidential, orderNumber, pageSize, offSet);

	}

	/**
	 * Description : This api for creating new usagemaster(s)
	 * 
	 * @param tenantId
	 * @param usageMasterRequest
	 * @return masterResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/usages/_create", method = RequestMethod.POST)
	public UsageMasterResponse createUsageMaster(@RequestParam(required = true) String tenantId,
			@RequestBody UsageMasterRequest usageMasterRequest) throws Exception {

		return masterService.createUsageMaster(tenantId, usageMasterRequest);
	}

	/**
	 * Description : This api to Update any of the usagemasters
	 * 
	 * @param usageMasterRequest
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/usages/_update", method = RequestMethod.POST)
	public UsageMasterResponse updateUsageMaster(@RequestBody UsageMasterRequest usageMasterRequest) throws Exception {

		return masterService.updateUsageMaster(usageMasterRequest);
	}

	/**
	 * This will create the depreciation
	 * 
	 * @param tenantId
	 * @param depreciationRequest
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/depreciations/_create", method = RequestMethod.POST)
	public DepreciationResponse createDepreciation(@RequestParam(required = true) String tenantId,
			@RequestBody DepreciationRequest depreciationRequest) throws Exception {
		return masterService.createDepreciation(tenantId, depreciationRequest);
	}

	/**
	 * This will update the depreciation based on the given object
	 * 
	 * @param depreciationRequest
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/depreciations/_update", method = RequestMethod.POST)
	public DepreciationResponse updateDepreciation(@RequestBody DepreciationRequest depreciationRequest)
			throws Exception {
		return masterService.updateDepreciation(depreciationRequest);

	}

	/**
	 * This will search the depreciations based on the given inputs.
	 * 
	 * @param requestInfoWrapper
	 * @param tenantId
	 * @param ids
	 * @param fromYear
	 * @param toYear
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offset
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/depreciations/_search", method = RequestMethod.POST)
	public DepreciationResponse searchDepreciation(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) Integer fromYear, @RequestParam(required = false) Integer toYear,
			@RequestParam(required = false) String code, @RequestParam(required = false) String nameLocal,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offset)
			throws Exception {
		return masterService.searchDepreciation(requestInfoWrapper.getRequestInfo(), tenantId, ids, fromYear, toYear,
				code, nameLocal, pageSize, offset);
	}

}
