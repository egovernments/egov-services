package org.egov.property.services;

import org.egov.models.ApartmentRequest;
import org.egov.models.ApartmentResponse;
import org.egov.models.AppConfigurationRequest;
import org.egov.models.AppConfigurationResponse;
import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.DepreciationRequest;
import org.egov.models.DepreciationResponse;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.FloorTypeRequest;
import org.egov.models.FloorTypeResponse;
import org.egov.models.GuidanceValueBoundaryRequest;
import org.egov.models.GuidanceValueBoundaryResponse;
import org.egov.models.MutationMasterRequest;
import org.egov.models.MutationMasterResponse;
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
	 * @param tenantId
	 * @param ids
	 * @param category
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return
	 */
	public DepartmentResponseInfo getDepartmentMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String category, String name, String code, String nameLocal, Integer pageSize, Integer offSet);

	/**
	 * Description : This method for getting floor master details
	 * 
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */
	public FloorTypeResponse getFloorTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception;

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
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return {@link WoodTypeResponse}
	 */
	public WoodTypeResponse getWoodTypes(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception;

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
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return {@link RoofTypeResponse}
	 */
	public RoofTypeResponse getRoofypes(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception;

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
	public StructureClassResponse getStructureClassMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet);

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
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param active
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @param String parent
	 * @return
	 */
	public PropertyTypeResponse getPropertyTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet, String parent);

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
	 */
	public OccuapancyMasterResponse getOccuapancyMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String nameLocal, Boolean active, Integer orderNumber, Integer pageSize,
			Integer offSet);

	/**
	 * Description : This method for getting wall type master details
	 * 
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */
	public WallTypeResponse getWallTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception;

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
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */
	public UsageMasterResponse getUsageMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, String nameLocal, Boolean active, Boolean isResidential, Integer orderNumber, Integer pageSize,
			Integer offSet, String parent, String[] service) throws Exception;

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
	public DepreciationResponse searchDepreciation(RequestInfo requestInfo, String tenantId, Integer[] ids,
			Integer fromYear, Integer toYear, String code, String nameLocal, Integer pageSize, Integer offset,
			Integer year) throws Exception;

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
	 * @param tenatId
	 * @param ids
	 * @param name
	 * @param code
	 * @param nameLocal
	 * @param pageSize
	 * @param offSet
	 * @return {@link MutationMasterResponse}
	 */
	public MutationMasterResponse searchMutationMaster(RequestInfo requestInfo, String tenatId, Integer[] ids,
			String name, String code, String nameLocal, Integer pageSize, Integer offSet) throws Exception;

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
	 * @param tenantId
	 * @param name
	 * @param code
	 * @param application
	 * @param pageSize
	 * @param OffSet
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	public DocumentTypeResponse searchDocumentTypeMaster(RequestInfo requestInfo, String tenantId, String name,
			String code, String application, Integer pageSize, Integer offSet) throws Exception;

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
	 * @param tenantId
	 * @param apartmentCode
	 * @param apartmentName
	 * @param pageSize
	 * @param offSet
	 * @return ApartmentResponse
	 * @throws Exception
	 */
	public ApartmentResponse searchApartment(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			String code, Boolean liftFacility, Boolean powerBackUp, Boolean parkingFacility, Integer pageSize,
			Integer offSet) throws Exception;
	
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
	 * @param tenantId
	 * @param guidancevalueboundary1
	 * @param guidancevalueboundary2
	 * @param orderNumber
	 * @param pageSize
	 * @param offSet
	 * @return
	 * @throws Exception
	 */
	public GuidanceValueBoundaryResponse getGuidanceValueBoundary(RequestInfo requestInfo, String tenantId,
			String guidanceValueBoundary1, String guidanceValueBoundary2, Integer pageSize, Integer offSet)
			throws Exception;

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
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param keyName
	 * @param effectiveFrom
	 * @param pageSize
	 * @param offSet
	 * @return
	 */
	public AppConfigurationResponse getAppConfiguration(RequestInfo requestInfo, String tenantId, Long[] ids,
			String keyName, String effectiveFrom, Integer pageSize, Integer offSet) throws Exception;
	 

}
