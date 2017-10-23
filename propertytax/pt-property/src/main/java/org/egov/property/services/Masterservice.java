package org.egov.property.services;

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
import org.egov.models.TaxExemptionReasonRequest;
import org.egov.models.TaxExemptionReasonResponse;
import org.egov.models.TaxExemptionReasonSearchCriteria;
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
import org.egov.models.RequestInfo;
import org.egov.models.RoofTypeRequest;
import org.egov.models.RoofTypeResponse;
import org.egov.models.RoofTypeSearchCriteria;
import org.egov.models.StructureClassRequest;
import org.egov.models.StructureClassResponse;
import org.egov.models.StructureClassSearchCriteria;
import org.egov.models.UsageMasterRequest;
import org.egov.models.UsageMasterResponse;
import org.egov.models.UsageMasterSearchCriteria;
import org.egov.models.WallTypeRequest;
import org.egov.models.WallTypeResponse;
import org.egov.models.WallTypeSearchCriteria;
import org.egov.models.WoodTypeRequest;
import org.egov.models.WoodTypeResponse;
import org.egov.models.WoodTypeSearchCriteria;

public interface Masterservice {

	/**
	 * Description : This method will use for creating department type
	 * 
	 * @param tenantId
	 * @param departmentRequest
	 * @return departmentResponseInfo
	 */
	public DepartmentResponseInfo createDepartmentMaster(String tenantId, DepartmentRequest departmentRequest);

	/**
	 * Description: department update
	 * 
	 * @param tenantId
	 * @param id
	 * @param DepartmentRequest
	 */
	public DepartmentResponseInfo updateDepartmentMaster(DepartmentRequest departmentRequest);

	/**
	 * Description: search for deparment
	 * 
	 * @param requestInfo
	 * @param departmentSearchCriteria
	 * @return DepartmentResponseInfo
	 */
	public DepartmentResponseInfo getDepartmentMaster(RequestInfo requestInfo,
			DepartmentSearchCriteria departmentSearchCriteria);

	/**
	 * Description : This method for getting floor master details
	 * 
	 * @param requestInfo
	 * @param floorTypeSearchCriteria
	 * @return masterModel
	 * @throws Exception
	 */
	public FloorTypeResponse getFloorTypeMaster(RequestInfo requestInfo,
			FloorTypeSearchCriteria floorTypeSearchCriteria) throws Exception;

	/**
	 * <P>
	 * This method will presist the given floor type in the database
	 * </p>
	 * 
	 * @param floorTypeRequest
	 * @param tenantId
	 * @return {@link FloorTypeResponse}
	 */
	public FloorTypeResponse createFloorType(FloorTypeRequest floorTypeRequest, String tenantId) throws Exception;

	/**
	 * <p>
	 * This method will update floor type object with the given details
	 * <p>
	 * 
	 * @param floorTypeRequest
	 * @param tenantId
	 * @param id
	 * @return {@link FloorTypeRequest}
	 */
	public FloorTypeResponse updateFloorType(FloorTypeRequest floorTypeRequest) throws Exception;

	/**
	 * <P>
	 * This method will search the Wood type records based on the given
	 * parameters
	 * <p>
	 * 
	 * @param requestInfo
	 * @param woodTypeSearchCriteria
	 * @return {@link WoodTypeResponse}
	 */
	public WoodTypeResponse getWoodTypes(RequestInfo requestInfo, WoodTypeSearchCriteria woodTypeSearchCriteria)
			throws Exception;

	/**
	 * <p>
	 * Thos method will insert the wood type record in the database
	 * <p>
	 * 
	 * @param woodTypeRequest
	 * @param tenantId
	 * @return {@link WoodTypeResponse}
	 */
	public WoodTypeResponse createWoodType(WoodTypeRequest woodTypeRequest, String tenantId) throws Exception;

	/**
	 * <p>
	 * This method will update the wood type object for the given Id for given
	 * wood type object
	 * <p>
	 * 
	 * @param woodTypeRequest
	 * @param tenantId
	 * @param id
	 * @return {@link WoodTypeResponse}
	 */
	public WoodTypeResponse updateWoodType(WoodTypeRequest woodTypeRequest) throws Exception;

	/**
	 * 
	 * <P>
	 * This method will search for the roof type objects for the given
	 * parameters and returns the matched roof type Objects
	 * <p>
	 * 
	 * @param requestInfo
	 * @param roofTypeSearchCriteria
	 * @return {@link RoofTypeResponse}
	 */
	public RoofTypeResponse getRoofypes(RequestInfo requestInfo, RoofTypeSearchCriteria roofTypeSearchCriteria)
			throws Exception;

	/**
	 * <p>
	 * This methhod will insert the given roof object in the database
	 * </p>
	 * 
	 * @param roofTypeRequest
	 * @param tenantId
	 * @return {@link RoofTypeResponse}
	 */
	public RoofTypeResponse createRoofype(RoofTypeRequest roofTypeRequest, String tenantId) throws Exception;

	/**
	 * <p>
	 * This method will update the given rooftype object for the given rooftype
	 * Id
	 * </p>
	 * 
	 * @param roofTypeRequest
	 * @param tenantId
	 * @param id
	 * @return {@link RoofTypeResponse}
	 */
	public RoofTypeResponse updateRoofType(RoofTypeRequest roofTypeRequest) throws Exception;

	/**
	 * Description : This api for creating strctureClass master
	 * 
	 * @param tenantId
	 * @param StructureClassRequest
	 * @return structureClassResponse
	 * @throws Exception
	 */
	public StructureClassResponse craeateStructureClassMaster(String tenantId,
			StructureClassRequest structureClassRequest);

	/**
	 * Description : This api for updating strctureClass master
	 * 
	 * @param tenantId
	 * @param StructureClassRequest
	 * @return structureClassResponse
	 * @throws Exception
	 */
	public StructureClassResponse updateStructureClassMaster(StructureClassRequest structureClassRequest);

	/**
	 * Description : This api for searching strctureClass master
	 * 
	 * @param requestInfo
	 * @param structureClassSearchCriteria
	 * @return structureClassResponse
	 * @throws Exception
	 */
	public StructureClassResponse getStructureClassMaster(RequestInfo requestInfo,
			StructureClassSearchCriteria structureClassSearchCriteria);

	/**
	 * Description : This method will use for creating property type
	 * 
	 * @param tenantId
	 * @param propertyTypeRequest
	 * @return
	 */
	public PropertyTypeResponse createPropertyTypeMaster(String tenantId, PropertyTypeRequest propertyTypeRequest);

	/**
	 * Description : This method will use for update property type
	 * 
	 * @param tenantId
	 * @param propertyTypeResponse
	 * @return
	 */
	public PropertyTypeResponse updatePropertyTypeMaster(PropertyTypeRequest propertyTypeRequest);

	/**
	 * Description : This api for searching propertyType master
	 * 
	 * @param requestInfo
	 * @param propertyTypeSearchCriteria
	 * @return PropertyTypeResponse
	 */
	public PropertyTypeResponse getPropertyTypeMaster(RequestInfo requestInfo,
			PropertyTypeSearchCriteria propertyTypeSearchCriteria);

	/**
	 * Description : This method will use for creating Occuapancy
	 * 
	 * @param tenantId
	 * @param occuapancyRequest
	 * @return
	 */
	public OccuapancyMasterResponse createOccuapancyMaster(String tenantId,
			OccuapancyMasterRequest occuapancyMasterRequest);

	/**
	 * Description : This api for updating occupancyType master
	 * 
	 * @param tenantId
	 * @param id
	 * @param occuapancyRequest
	 * @return
	 */
	public OccuapancyMasterResponse updateOccuapancyMaster(OccuapancyMasterRequest occuapancyRequest);

	/**
	 * Description: search occupancy query formation and used in
	 * getOccupancyMaster method
	 * 
	 * @param requestInfo
	 * @param OccuapancyMasterSearchCriteria
	 * @return OccuapancyMasterResponse
	 */
	public OccuapancyMasterResponse getOccuapancyMaster(RequestInfo requestInfo,
			OccuapancyMasterSearchCriteria occuapancyMasterSearchCriteria);

	/**
	 * Description : This method for getting wall type master details
	 * 
	 * @param requestInfo
	 * @param wallTypeSearchCriteria
	 * @return masterModel
	 * @throws Exception
	 */
	public WallTypeResponse getWallTypeMaster(RequestInfo requestInfo, WallTypeSearchCriteria wallTypeSearchCriteria)
			throws Exception;

	/**
	 * Description : This method for getting wall type master details
	 * 
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */
	public WallTypeResponse createWallTypeMaster(String tenantId, WallTypeRequest wallTypeRequest) throws Exception;

	/**
	 * Description : this method for updating wall type
	 * 
	 * @param wallTypeRequest
	 * @param id
	 * @return
	 */
	public WallTypeResponse updateWallTypeMaster(WallTypeRequest wallTypeRequest) throws Exception;

	/**
	 * Description : This method for getting usage master details
	 * 
	 * @param requestInfo
	 * @param UsageMasterSearchCriteria
	 * @return masterModel
	 * @throws Exception
	 */
	public UsageMasterResponse getUsageMaster(RequestInfo requestInfo,
			UsageMasterSearchCriteria usageMasterSearchCriteria) throws Exception;

	/**
	 * Description : This method for creating usageMaster
	 * 
	 * @param tenantId
	 * @param usageMasters
	 * @return masterModel
	 * @throws Exception
	 */
	public UsageMasterResponse createUsageMaster(String tenantId, UsageMasterRequest usageMasterRequest)
			throws Exception;

	/**
	 * Description : This method for updating usageMaster
	 * 
	 * @param tenantId
	 * @param usageMasters
	 * @return masterModel
	 * @throws Exception
	 */
	public UsageMasterResponse updateUsageMaster(UsageMasterRequest usageMasterRequest) throws Exception;

	/**
	 * This will create the depreciations based on the given object
	 * 
	 * @param tenantId
	 * @param depreciationRequest
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	public DepreciationResponse createDepreciation(String tenantId, DepreciationRequest depreciationRequest)
			throws Exception;

	/**
	 * This will update the depreciations based on the given depreciation object
	 * 
	 * @param depreciationRequest
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	public DepreciationResponse updateDepreciation(DepreciationRequest depreciationRequest) throws Exception;

	/**
	 * 
	 * This will search the depreciations based on the given input parameters
	 * 
	 * @param requestInfo
	 * @param DepreciationSearchCriteria
	 * @return {@link DepreciationResponse}
	 * @throws Exception
	 */
	public DepreciationResponse searchDepreciation(RequestInfo requestInfo,
			DepreciationSearchCriteria depreciationSearchCriteria) throws Exception;

	/**
	 * This will create the mutation master based on the given mutation master
	 * request
	 * 
	 * @param tenantId
	 * @param mutationMasterRequest
	 * @return {@link MutationMasterResponse}
	 */
	public MutationMasterResponse createMutationMater(String tenantId, MutationMasterRequest mutationMasterRequest)
			throws Exception;

	/**
	 * This will update the mutation master based on the given mutation master
	 * request
	 * 
	 * @param mutationMasterRequest
	 * @return {@link MutationMasterResponse}
	 */

	public MutationMasterResponse updateMutationMaster(MutationMasterRequest mutationMasterRequest) throws Exception;

	/**
	 * This will search the mutation master based on the given parameters
	 * 
	 * @param requestInfoWrapper
	 * @param MutationMasterSearchCriteria
	 * @return {@link MutationMasterResponse}
	 */
	public MutationMasterResponse searchMutationMaster(RequestInfo requestInfo,
			MutationMasterSearchCriteria mutationMasterSearchCriteria) throws Exception;

	/**
	 * public DocumentTypeResponse updateDocumentTypeMaster(DocumentTypeRequest
	 * documentTypeRequest); This will create the Document type master
	 * 
	 * @param documentTypeRequest
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	public DocumentTypeResponse createDocumentTypeMaster(String tenantId, DocumentTypeRequest documentTypeRequest)
			throws Exception;

	/**
	 * This will update the Document type master
	 * 
	 * @param documentTypeRequest
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	public DocumentTypeResponse updateDocumentTypeMaster(DocumentTypeRequest documentTypeRequest) throws Exception;

	/**
	 * This will search the Document Type masters
	 * 
	 * @param requestInfo
	 * @param DocumentTypeSearchCriteria
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	public DocumentTypeResponse searchDocumentTypeMaster(RequestInfo requestInfo,
			DocumentTypeSearchCriteria documentTypeSearchCriteria) throws Exception;

	/**
	 * This will create Apartment Master
	 * 
	 * @param tenantId
	 * @param apartmentRequest
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	public ApartmentResponse createApartment(String tenantId, ApartmentRequest apartmentRequest) throws Exception;

	/**
	 * This will update Apartment Master
	 * 
	 * @param apartmentRequest
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	public ApartmentResponse updateApartment(ApartmentRequest apartmentRequest) throws Exception;

	/**
	 * This will search for Apartment Master
	 * 
	 * @param requestInfo
	 * @param ApartmentSearchCriteria
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	public ApartmentResponse searchApartment(RequestInfo requestInfo, ApartmentSearchCriteria apartmentSearchCriteria)
			throws Exception;

	/**
	 * This will create the GuidanceValueBoundary
	 * 
	 * @param tenantId
	 * @param GuidanceValueBouondaryRequest
	 * @return GuidanceValueBouondaryResponse
	 */
	public GuidanceValueBoundaryResponse createGuidanceValueBoundary(String tenantId,
			GuidanceValueBoundaryRequest guidanceValueBoundaryRequest) throws Exception;

	/**
	 * This will update the GuidanceValueBoundary
	 * 
	 * @param tenantId
	 * @param GuidanceValueBouondaryRequest
	 * @return GuidanceValueBouondaryResponse
	 */
	public GuidanceValueBoundaryResponse updateGuidanceValueBoundary(
			GuidanceValueBoundaryRequest guidanceValueBoundaryRequest) throws Exception;

	/**
	 * This will give search for guidance value boundary
	 * 
	 * @param requestInfo
	 * @param GuidanceValueBoundarySearchCriteria
	 *            guidanceValueBoundarySearchCriteria
	 * @return GuidanceValueBoundaryResponse
	 * @throws Exception
	 */
	public GuidanceValueBoundaryResponse getGuidanceValueBoundary(RequestInfo requestInfo,
			GuidanceValueBoundarySearchCriteria guidanceValueBoundarySearchCriteria) throws Exception;

	/**
	 * This will create the Appconfiguration
	 * 
	 * @param tenantId
	 * @param ConfigurationRequest
	 * @return ConfigurationResponse
	 */
	public AppConfigurationResponse createAppConfiguration(String tenantId,
			AppConfigurationRequest appConfigurationRequest) throws Exception;

	/**
	 * This will update the AppConfiguration
	 * 
	 * @param ConfigurationRequest
	 * @return ConfigurationResponse
	 * @throws Exception
	 */
	public AppConfigurationResponse updateAppConfiguration(AppConfigurationRequest appConfigurationRequest)
			throws Exception;

	/**
	 * This will search app confiuration and return list of configurations
	 * 
	 * @param requestInfo
	 * @param AppConfigurationSearchCriteria
	 * @return AppConfigurationResponse
	 */
	public AppConfigurationResponse getAppConfiguration(RequestInfo requestInfo,
			AppConfigurationSearchCriteria appConfigurationSearchCriteria) throws Exception;

	/**
	 * This API will create the demolition reason master
	 * 
	 * @param tenantId
	 * @param demolitionReasonRequest
	 * @return {@link DemolitionReasonResponse}
	 */
	public DemolitionReasonResponse createDemolitionReason(String tenantId,
			DemolitionReasonRequest demolitionReasonRequest) throws Exception;

	/**
	 * This API wil update the demolition object
	 * 
	 * @param demolitionMasterRequest
	 * @return {@link DemolitionReasonResponse}
	 */
	public DemolitionReasonResponse updateDemolitionReason(DemolitionReasonRequest demolitionMasterRequest)
			 throws Exception;

	/**
	 * This will search the demolitions based on the given paramerters
	 * 
	 * @param requestInfo
	 * @param demolitionReasonSearchCriteria
	 * @return {@link DemolitionReasonResponse}
	 */
	public DemolitionReasonResponse getDemolitionReason(RequestInfo requestInfo,
			DemolitionReasonSearchCriteria demolitionReasonSearchCriteria)  throws Exception;

	
	/**
	 * Description: This api create ExemptionReasonMaster
	 * 
	 * @param exemptionReasonRequest
	 * @return ExemptionReasonResponse
	 * @throws Exception
	 */
	public TaxExemptionReasonResponse createTaxExemptionReason(
			TaxExemptionReasonRequest taxExemptionReasonRequest) throws Exception;
	
	/**
	 * Description: This api update ExemptionReasonMaster
	 * 
	 * @param TaxExemptionReasonRequest
	 * @return ExemptionReasonResponse
	 * @throws Exception
	 */
	public TaxExemptionReasonResponse updateTaxExemptionReason(
			TaxExemptionReasonRequest taxExemptionReasonRequest) throws Exception;
	
	/**
	 * Description: This api fetch ExemptionReasons based search criteria
	 * parameter
	 * 
	 * @param requestInfo
	 * @param TaxExemptionReasonSearchCriteria
	 * @return ExemptionReasonResponse
	 * @throws Exception
	 */
	public TaxExemptionReasonResponse getTaxExemptionReason(RequestInfo requestInfo,
			TaxExemptionReasonSearchCriteria taxExemptionReasonSearchCriteria) throws Exception;
}
