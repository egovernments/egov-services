package org.egov.property.api;

import java.util.List;

import javax.validation.Valid;

import org.egov.models.ApartmentRequest;
import org.egov.models.ApartmentResponse;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.DepreciationRequest;
import org.egov.models.DepreciationResponse;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.MutationMasterRequest;
import org.egov.models.MutationMasterResponse;
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
			@Valid @RequestBody DepartmentRequest departmentRequest) {

		return masterService.createDepartmentMaster(tenantId, departmentRequest);

	}

	@RequestMapping(path = "/departments/_update", method = RequestMethod.POST)
	public DepartmentResponseInfo updateDepartmentMaster(@Valid @RequestBody DepartmentRequest departmentRequest) {

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
	@RequestMapping(path = "floortypes/_create", method = RequestMethod.POST)
	public FloorTypeResponse createFloorType(@Valid @RequestBody FloorTypeRequest floorTypeRequest,
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
	@RequestMapping(path = "/floortypes/_update")
	public FloorTypeResponse updateFloorType(@Valid @RequestBody FloorTypeRequest floorTypeRequest) throws Exception {

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
	public WoodTypeResponse createWoodType(@Valid @RequestBody WoodTypeRequest woodTypeRequest,
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
	public WoodTypeResponse updateWoodType(@Valid @RequestBody WoodTypeRequest woodTypeRequest) throws Exception {

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
	public RoofTypeResponse createRoofType(@Valid @RequestBody RoofTypeRequest roofTypeRequest,
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
	public RoofTypeResponse updateRoofType(@Valid @RequestBody RoofTypeRequest roofTypeRequest) throws Exception {

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
			@Valid @RequestBody StructureClassRequest structureClassRequest) {

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
	public StructureClassResponse updateStructureClassMaster(
			@Valid @RequestBody StructureClassRequest structureClassRequest) {

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
			@Valid @RequestBody PropertyTypeRequest propertyTypeRequest) {

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
	public PropertyTypeResponse updatePropertyTypeMaster(@Valid @RequestBody PropertyTypeRequest propertyTypeRequest) {

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
			@RequestParam(required = false) Integer offSet, @RequestParam(required = false) String parent) throws Exception {
		return masterService.getPropertyTypeMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal,
				active, orderNumber, pageSize, offSet, parent);

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
			@Valid @RequestBody OccuapancyMasterRequest occuapancyMasterRequest) {

		return masterService.createOccuapancyMaster(tenantId, occuapancyMasterRequest);

	}

	/**
	 * Description : This api for updating occupancyType master
	 * 
	 * @param occuapancyRequest
	 * @return
	 */

	@RequestMapping(path = "/occuapancies/_update", method = RequestMethod.POST)
	public OccuapancyMasterResponse updateOccuapancyMaster(
			@Valid @RequestBody OccuapancyMasterRequest occuapancyRequest) {

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
			@Valid @RequestBody WallTypeRequest wallTypeRequest) throws Exception {

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
	public WallTypeResponse updateWallTypeMaster(@Valid @RequestBody WallTypeRequest wallTypeRequest) throws Exception {

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
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offSet,
			@RequestParam(required = false) String parent,@RequestParam(required = false) List<String> service)
			throws Exception {

		return masterService.getUsageMaster(requestInfo.getRequestInfo(), tenantId, ids, name, code, nameLocal, active,
				isResidential, orderNumber, pageSize, offSet, parent,service);

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
			@Valid @RequestBody UsageMasterRequest usageMasterRequest) throws Exception {

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
	public UsageMasterResponse updateUsageMaster(@Valid @RequestBody UsageMasterRequest usageMasterRequest)
			throws Exception {

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
			@Valid @RequestBody DepreciationRequest depreciationRequest) throws Exception {
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
	public DepreciationResponse updateDepreciation(@Valid @RequestBody DepreciationRequest depreciationRequest)
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
	 * @param year
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/depreciations/_search", method = RequestMethod.POST)
	public DepreciationResponse searchDepreciation(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) Integer fromYear, @RequestParam(required = false) Integer toYear,
			@RequestParam(required = false) String code, @RequestParam(required = false) String nameLocal,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offset,
			@RequestParam(required = false) Integer year) throws Exception {
		return masterService.searchDepreciation(requestInfoWrapper.getRequestInfo(), tenantId, ids, fromYear, toYear,
				code, nameLocal, pageSize, offset, year);
	}

	/**
	 * This will create the mutation master based on the given mutation master
	 * request
	 * 
	 * @param tenantId
	 * @param mutationMasterRequest
	 * @return {@link MutationMasterResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/mutationmasters/_create", method = RequestMethod.POST)
	public MutationMasterResponse createMutationMater(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody MutationMasterRequest mutationMasterRequest) throws Exception {

		return masterService.createMutationMater(tenantId, mutationMasterRequest);

	}

	/**
	 * This will update the mutation master based on the given mutation master
	 * request
	 * 
	 * @param mutationMasterRequest
	 * @return {@link MutationMasterResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/mutationmasters/_update", method = RequestMethod.POST)
	public MutationMasterResponse updateMutationMaster(@Valid @RequestBody MutationMasterRequest mutationMasterRequest)
			throws Exception {
		return masterService.updateMutationMaster(mutationMasterRequest);
	}

	/**
	 * This will search the mutation master based on the given parameters
	 * 
	 * @param requestInfoWrapper
	 * @param tenatId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return {@link MutationMasterResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/mutationmasters/_search", method = RequestMethod.POST)
	public MutationMasterResponse searchMutationMaster(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) String nameLocal, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {
		return masterService.searchMutationMaster(requestInfoWrapper.getRequestInfo(), tenantId, ids, name, code,
				nameLocal, pageSize, offSet);

	}

	/**
	 * This will create the documentType master
	 * 
	 * @param documentTypeRequest
	 * @return DocumentTypeResponse
	 */
	@RequestMapping(path = "/documenttypes/_create", method = RequestMethod.POST)
	public DocumentTypeResponse createDocumentTypeMaster(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody DocumentTypeRequest documentTypeRequest) throws Exception {
		return masterService.createDocumentTypeMaster(tenantId, documentTypeRequest);
	}

	/**
	 * This will update the Document type master
	 * 
	 * @param documentTypeRequest
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/documenttypes/_update", method = RequestMethod.POST)
	public DocumentTypeResponse updateDocumentTypeMaster(@Valid @RequestBody DocumentTypeRequest documentTypeRequest)
			throws Exception {
		return masterService.updateDocumentTypeMaster(documentTypeRequest);
	}

	/**
	 * This will search for the Document type master
	 * 
	 * @param requestInfoWrapper
	 * @param tenantId
	 * @param name
	 * @param code
	 * @param application
	 * @param pageSize
	 * @param offSet
	 * @return DocumentTypeResponse
	 */
	@RequestMapping(path = "/documenttypes/_search", method = RequestMethod.POST)
	public DocumentTypeResponse searchDocumentTypeMaster(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) String name,
			@RequestParam(required = false) String code, @RequestParam(required = false) String application,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offSet)
			throws Exception {

		return masterService.searchDocumentTypeMaster(requestInfoWrapper.getRequestInfo(), tenantId, name, code,
				application, pageSize, offSet);
	}

	/**
	 * This will create Apartment
	 * 
	 * @param tenantId
	 * @param apartmentRequest
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/apartment/_create", method = RequestMethod.POST)
	public ApartmentResponse createApartment(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody ApartmentRequest apartmentRequest) throws Exception {

		return masterService.createApartment(tenantId, apartmentRequest);
	}

	/**
	 * This will update Apartment
	 * 
	 * @param apartmentRequest
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/apartment/_update", method = RequestMethod.POST)
	public ApartmentResponse updateApartment(@Valid @RequestBody ApartmentRequest apartmentRequest) throws Exception {

		return masterService.updateApartment(apartmentRequest);
	}

	/**
	 * This will search for Apartment
	 * 
	 * @param requestInfoWrapper
	 * @param tenantId
	 * @param apartmentCode
	 * @param apartmentName
	 * @param liftFacility
	 * @param powerBackUp
	 * @param parkingFacility
	 * @param pageSize
	 * @param offSet
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/apartment/_search", method = RequestMethod.POST)
	public ApartmentResponse searchApartment(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code,
			@RequestParam(required = false) Boolean liftFacility, @RequestParam(required = false) Boolean powerBackUp,
			@RequestParam(required = false) Boolean parkingFacility, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return masterService.searchApartment(requestInfoWrapper.getRequestInfo(), tenantId, ids, name, code,
				liftFacility, powerBackUp, parkingFacility, pageSize, offSet);
	}
}
