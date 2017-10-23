package org.egov.property.api;

import javax.validation.Valid;

import org.egov.models.ApartmentRequest;
import org.egov.models.ApartmentResponse;
import org.egov.models.ApartmentSearchCriteria;
import org.egov.models.AppConfigurationRequest;
import org.egov.models.AppConfigurationResponse;
import org.egov.models.AppConfigurationSearchCriteria;
import org.egov.models.DemolitionReasonRequest;
import org.egov.models.DemolitionReasonResponse;
import org.egov.models.DemolitionReasonSearchCriteria;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.DepartmentSearchCriteria;
import org.egov.models.DepreciationRequest;
import org.egov.models.DepreciationResponse;
import org.egov.models.DepreciationSearchCriteria;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.DocumentTypeSearchCriteria;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.FloorTypeSearchCriteria;
import org.egov.models.GuidanceValueBoundaryRequest;
import org.egov.models.GuidanceValueBoundaryResponse;
import org.egov.models.GuidanceValueBoundarySearchCriteria;
import org.egov.models.MutationMasterRequest;
import org.egov.models.MutationMasterResponse;
import org.egov.models.MutationMasterSearchCriteria;
import org.egov.models.OccuapancyMasterRequest;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.OccuapancyMasterSearchCriteria;
import org.egov.models.PropertyTypeRequest;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.PropertyTypeSearchCriteria;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.RoofTypeSearchCriteria;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.models.StructureClassSearchCriteria;
import org.egov.models.TaxExemptionReasonRequest;
import org.egov.models.TaxExemptionReasonResponse;
import org.egov.models.TaxExemptionReasonSearchCriteria;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.UsageMasterSearchCriteria;
import org.egov.models.WallTypeRequest;
import org.egov.models.WallTypeResponse;
import org.egov.models.WallTypeSearchCriteria;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;
import org.egov.models.WoodTypeSearchCriteria;
import org.egov.property.exception.InvalidSearchParameterException;
import org.egov.property.services.Masterservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	/**
	 * Description : This api for creating Department master
	 * 
	 * @param tenantId
	 * @param departmentRequest
	 * @return DepartmentResponseInfo
	 */
	@RequestMapping(path = "/departments/_create", method = RequestMethod.POST)
	public DepartmentResponseInfo createDepartmentMaster(@RequestParam String tenantId,
			@Valid @RequestBody DepartmentRequest departmentRequest) {

		return masterService.createDepartmentMaster(tenantId, departmentRequest);

	}

	/**
	 * Description : This api for updating Department master
	 * 
	 * @param departmentRequest
	 * @return DepartmentResponseInfo
	 */
	@RequestMapping(path = "/departments/_update", method = RequestMethod.POST)
	public DepartmentResponseInfo updateDepartmentMaster(@Valid @RequestBody DepartmentRequest departmentRequest) {

		return masterService.updateDepartmentMaster(departmentRequest);

	}

	/**
	 * Description : This api for fetching Departments based on search criteria
	 * 
	 * @param requestInfo
	 * @param departmentSearchCriteria
	 * @param bindingResult
	 * @return DepartmentResponseInfo
	 * @throws Exception
	 */
	@RequestMapping(path = "/departments/_search", method = RequestMethod.POST)
	public DepartmentResponseInfo getDeparmentMaster(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid DepartmentSearchCriteria departmentSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getDepartmentMaster(requestInfo.getRequestInfo(), departmentSearchCriteria);

	}

	/**
	 * Description : This api for getting floor master details
	 * 
	 * @param requestInfo
	 * @param floorTypeSearchCriteria
	 * @return masterResponseInfo
	 * @throws Exception
	 */
	@RequestMapping(path = "/floortypes/_search", method = RequestMethod.POST)
	public FloorTypeResponse getFloorTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid FloorTypeSearchCriteria floorTypeSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getFloorTypeMaster(requestInfo.getRequestInfo(), floorTypeSearchCriteria);

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
	 * @param woodTypeSearchCriteria
	 * @return WoodTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/woodtypes/_search", method = RequestMethod.POST)
	public WoodTypeResponse searchWoodType(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid WoodTypeSearchCriteria woodTypeSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getWoodTypes(requestInfo.getRequestInfo(), woodTypeSearchCriteria);

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

	/**
	 * <p>
	 * This Api
	 * 
	 * @param requestInfo
	 * @param roofTypeSearchCriteria
	 * @return RoofTypeResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/rooftypes/_search", method = RequestMethod.POST)
	public RoofTypeResponse searchRoofType(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid RoofTypeSearchCriteria roofTypeSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getRoofypes(requestInfo.getRequestInfo(), roofTypeSearchCriteria);
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
	 * @param structureClassSearchCriteria
	 * @return structureClassResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "structureclasses/_search", method = RequestMethod.POST)
	public StructureClassResponse getStructureClassMaster(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid StructureClassSearchCriteria structureClassSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getStructureClassMaster(requestInfo.getRequestInfo(), structureClassSearchCriteria);
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
	 * @param propertyTypeSearchCriteria
	 * @return PropertyTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/propertytypes/_search", method = RequestMethod.POST)
	public PropertyTypeResponse getPropertyTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid PropertyTypeSearchCriteria propertyTypeSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getPropertyTypeMaster(requestInfo.getRequestInfo(), propertyTypeSearchCriteria);

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
	 * @param OccuapancyMasterSearchCriteria
	 * @return OccuapancyMasterResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/occuapancies/_search", method = RequestMethod.POST)
	public OccuapancyMasterResponse getOccuapancyMaster(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid OccuapancyMasterSearchCriteria occuapancyMasterSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getOccuapancyMaster(requestInfo.getRequestInfo(), occuapancyMasterSearchCriteria);

	}

	/**
	 * Description : This api for getting wall type master details
	 * 
	 * @param requestInfo
	 * @param WallTypeSearchCriteria
	 * @return masterResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/walltypes/_search", method = RequestMethod.POST)
	public WallTypeResponse getWallTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid WallTypeSearchCriteria wallTypeSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getWallTypeMaster(requestInfo.getRequestInfo(), wallTypeSearchCriteria);
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
	 * @param requestInfo
	 * @param UsageMasterSearchCriteria
	 * @return UsageMasterResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/usages/_search", method = RequestMethod.POST)
	public UsageMasterResponse getUsageMaster(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid UsageMasterSearchCriteria usageMasterSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getUsageMaster(requestInfo.getRequestInfo(), usageMasterSearchCriteria);
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
	 * @param DepreciationSearchCriteria
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/depreciations/_search", method = RequestMethod.POST)
	public DepreciationResponse searchDepreciation(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DepreciationSearchCriteria depreciationSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfoWrapper.getRequestInfo());
		}
		return masterService.searchDepreciation(requestInfoWrapper.getRequestInfo(), depreciationSearchCriteria);
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
	 * @param MutationMasterSearchCriteria
	 * @return {@link MutationMasterResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/mutationmasters/_search", method = RequestMethod.POST)
	public MutationMasterResponse searchMutationMaster(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid MutationMasterSearchCriteria mutationMasterSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfoWrapper.getRequestInfo());
		}
		return masterService.searchMutationMaster(requestInfoWrapper.getRequestInfo(), mutationMasterSearchCriteria);

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
	 * @param DocumentTypeSearchCriteria
	 * @return DocumentTypeResponse
	 */
	@RequestMapping(path = "/documenttypes/_search", method = RequestMethod.POST)
	public DocumentTypeResponse searchDocumentTypeMaster(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DocumentTypeSearchCriteria documentTypeSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfoWrapper.getRequestInfo());
		}
		return masterService.searchDocumentTypeMaster(requestInfoWrapper.getRequestInfo(), documentTypeSearchCriteria);
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
	 * @param ApartmentSearchCriteria
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/apartment/_search", method = RequestMethod.POST)
	public ApartmentResponse searchApartment(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ApartmentSearchCriteria apartmentSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfoWrapper.getRequestInfo());
		}
		return masterService.searchApartment(requestInfoWrapper.getRequestInfo(), apartmentSearchCriteria);
	}

	/**
	 * This will create the GuidanceValueBoundary
	 * 
	 * @param tenantId
	 * @param AppConfigurationRequest
	 * @return {@link AppConfigurationResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/guidancevalueboundary/_create", method = RequestMethod.POST)
	public GuidanceValueBoundaryResponse createGuidanceValueBoundary(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody GuidanceValueBoundaryRequest guidanceValueBoundaryRequest) throws Exception {
		return masterService.createGuidanceValueBoundary(tenantId, guidanceValueBoundaryRequest);
	}

	/**
	 * This will update AppConfiguration
	 * 
	 * @param AppConfigurationRequest
	 * @return AppConfigurationResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/guidancevalueboundary/_update", method = RequestMethod.POST)
	public GuidanceValueBoundaryResponse updateGuidanceValueBoundary(
			@Valid @RequestBody GuidanceValueBoundaryRequest guidanceValueBoundaryRequest) throws Exception {

		return masterService.updateGuidanceValueBoundary(guidanceValueBoundaryRequest);
	}

	/**
	 * This will give GuidanceValueBoundary search
	 * 
	 * @param requestInfo
	 * @param GuidanceValueBoundarySearchCriteria
	 * @return GuidanceValueBoundaryResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/guidancevalueboundary/_search", method = RequestMethod.POST)
	public GuidanceValueBoundaryResponse getGuidanceValueBoundary(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid GuidanceValueBoundarySearchCriteria guidanceValueBoundarySearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getGuidanceValueBoundary(requestInfo.getRequestInfo(),
				guidanceValueBoundarySearchCriteria);

	}

	/**
	 * This will create the AppConfiguration
	 * 
	 * @param tenantId
	 * @param AppConfigurationRequest
	 * @return {@link AppConfigurationResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/appconfiguration/_create", method = RequestMethod.POST)
	public AppConfigurationResponse createAppConfiguration(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody AppConfigurationRequest appConfigurationRequest) throws Exception {
		return masterService.createAppConfiguration(tenantId, appConfigurationRequest);
	}

	/**
	 * This will update AppConfiguration
	 * 
	 * @param AppConfigurationRequest
	 * @return AppConfigurationResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/appconfiguration/_update", method = RequestMethod.POST)
	public AppConfigurationResponse updateAppConfiguration(
			@Valid @RequestBody AppConfigurationRequest appConfigurationRequest) throws Exception {

		return masterService.updateAppConfiguration(appConfigurationRequest);
	}

	/**
	 * This will fetch AppConfigurations based on search criteria params
	 * 
	 * @param requestInfo
	 * @param appConfigurationSearchCriteria
	 * @param bindingResult
	 * @return AppConfigurationResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/appconfiguration/_search", method = RequestMethod.POST)
	public AppConfigurationResponse getAppConfiguration(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid AppConfigurationSearchCriteria appConfigurationSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getAppConfiguration(requestInfo.getRequestInfo(), appConfigurationSearchCriteria);
	}

	/**
	 * This will create the demolition
	 * 
	 * @param tenantId
	 * @param demolitionReasonRequest
	 * @return {@link DemolitionReasonResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/demolitionreason/_create", method = RequestMethod.POST)
	public DemolitionReasonResponse createDemolitionReason(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody DemolitionReasonRequest demolitionReasonRequest) throws Exception {
		return masterService.createDemolitionReason(tenantId, demolitionReasonRequest);
	}

	/**
	 * 
	 * @param demolitiontReasonRequest
	 * @return
	 */
	@RequestMapping(path = "/demolitionreason/_update", method = RequestMethod.POST)
	public DemolitionReasonResponse updateDemolitionReason(
			@Valid @RequestBody DemolitionReasonRequest demolitiontReasonRequest) throws Exception{

		return masterService.updateDemolitionReason(demolitiontReasonRequest);

	}

	@RequestMapping(path = "/demolitionreason/_search", method = RequestMethod.POST)
	public DemolitionReasonResponse getDemolitionReason(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid DemolitionReasonSearchCriteria demolitionReasonSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return masterService.getDemolitionReason(requestInfo.getRequestInfo(), demolitionReasonSearchCriteria);
	}
	/**
	 * Description: This api create ExemptionReasonMaster
	 * 
	 * @param tenantId
	 * @param TaxExemptionReasonRequest
	 * @return {@link TaxExemptionReasonResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxexemptionreason/_create", method = RequestMethod.POST)
	public TaxExemptionReasonResponse createExemptionReason(
			@Valid @RequestBody TaxExemptionReasonRequest taxExemptionReasonRequest) throws Exception {
		return masterService.createTaxExemptionReason(taxExemptionReasonRequest);
	}

	/**
	 * Description: This api update ExemptionReasonMaster
	 * 
	 * @param TaxExemptionReasonRequest
	 * @return ExemptionReasonResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxexemptionreason/_update", method = RequestMethod.POST)
	public TaxExemptionReasonResponse updateExemptionReason(
			@Valid @RequestBody TaxExemptionReasonRequest taxExemptionReasonRequest) throws Exception {
		return masterService.updateTaxExemptionReason(taxExemptionReasonRequest);
	}

	/**
	 * Description: This api fetch ExemptionReasons based search criteria
	 * parameter
	 * 
	 * @param requestInfo
	 * @param TaxExemptionReasonSearchCriteria
	 * @return ExemptionReasonResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxexemptionreason/_search", method = RequestMethod.POST)
	public TaxExemptionReasonResponse getExemptionReason(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid TaxExemptionReasonSearchCriteria taxExemptionReasonSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}

		return masterService.getTaxExemptionReason(requestInfo.getRequestInfo(), taxExemptionReasonSearchCriteria);
	}

}
