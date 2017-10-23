package org.egov.property.services;

import org.egov.models.DemandResponse;
import org.egov.models.DemolitionRequest;
import org.egov.models.DemolitionResponse;
import org.egov.models.DemolitionSearchCriteria;
import org.egov.models.DemolitionSearchResponse;
import org.egov.models.PropertyDCBRequest;
import org.egov.models.PropertyDCBResponse;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.PropertySearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.SpecialNoticeRequest;
import org.egov.models.SpecialNoticeResponse;
import org.egov.models.TaxExemptionRequest;
import org.egov.models.TaxExemptionResponse;
import org.egov.models.TaxExemptionSearchCriteria;
import org.egov.models.TaxExemptionSearchResponse;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.egov.models.TitleTransferSearchCriteria;
import org.egov.models.VacancyRemissionRequest;
import org.egov.models.VacancyRemissionResponse;
import org.egov.models.VacancyRemissionSearchCriteria;
import org.egov.models.VacancyRemissionSearchResponse;
import org.egov.property.model.TitleTransferSearchResponse;

public interface PropertyService {

	/**
	 * This method for validating request and generating id,send property object
	 * to kafka
	 * 
	 * @param propertyRequest
	 * @return propertyResponse
	 */
	public PropertyResponse createProperty(PropertyRequest propertyRequest) throws Exception;

	/**
	 * This method for updating property
	 * 
	 * @param propertyRequest
	 * @return propertyResponse
	 */

	public PropertyResponse updateProperty(PropertyRequest propertyRequest) throws Exception;

	/**
	 * This method for search properties based on input parameters
	 * 
	 * @param requestInfo
	 * @param PropertySearchCriteria
	 * @return {@link PropertyResponse}
	 */
	public PropertyResponse searchProperty(RequestInfo requestInfo, PropertySearchCriteria propertySearchCriteria)
			throws Exception;

	/**
	 * This api for creating title transfer request for property
	 * 
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */

	public TitleTransferResponse createTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception;

	/**
	 * This api for updating title transfer request for property
	 * 
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */
	public TitleTransferResponse updateTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception;

	/**
	 * Save property history and update property
	 * 
	 * @param titleTransferRequest
	 * @throws Exception
	 */
	public PropertyRequest savePropertyHistoryandUpdateProperty(TitleTransferRequest titleTransferRequest)
			throws Exception;

	/**
	 * This Api will generate the special based on the given special notice
	 * request
	 * 
	 * @param specialNoticeRequest
	 * @return {@link SpecialNoticeResponse}
	 * @throws Exception
	 */
	public SpecialNoticeResponse generateSpecialNotice(SpecialNoticeRequest specialNoticeRequest) throws Exception;

	/**
	 * API prepares DCB data for Add/Edit DCB feature
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param upicNumber
	 * @throws Exception
	 */
	public DemandResponse getDemandsForProperty(RequestInfoWrapper requestInfo, String tenantId, String upicNumber)
			throws Exception;

	/**
	 * This API will search the title transfer based on the given parameters
	 * 
	 * @param requestInfo
	 * @param titleTransferSearchCriteria
	 * @return {@link TitleTransferResponse}
	 * @throws Exception
	 */
	public TitleTransferSearchResponse searchTitleTransfer(RequestInfoWrapper requestInfo,
			TitleTransferSearchCriteria titleTransferSearchCriteria) throws Exception;

	/**
	 * This is api to update dcb demands
	 * 
	 * @param propertyDCBRequest
	 * @param tenantId
	 * @return
	 */
	public PropertyDCBResponse updateDcbDemand(PropertyDCBRequest propertyDCBRequest, String tenantId) throws Exception;

	/**
	 * 
	 * This will create a new property with the status as workflow
	 * 
	 * @param propertyRequest
	 */
	public PropertyResponse modifyProperty(PropertyRequest propertyRequest) throws Exception;

	/**
	 * This API will create the demolition
	 * 
	 * @param demolitionRequest
	 * @return {@link DemolitionResponse}
	 * @throws Exception
	 */
	public DemolitionResponse createDemolition(DemolitionRequest demolitionRequest) throws Exception;

	/**
	 * This API will update the demolition
	 * 
	 * @param demolitionRequest
	 * @return {@link DemolitionResponse}
	 * @throws Exception
	 */
	public DemolitionResponse updateDemolition(DemolitionRequest demolitionRequest) throws Exception;

	/**
	 * This API will search the demolitions
	 * 
	 * @param demolitionRequest
	 * @return {@link DemolitionResponse}
	 * @throws Exception
	 */
	public DemolitionSearchResponse searchDemolition(RequestInfo requestInfo,
			DemolitionSearchCriteria demolitionSearchCriteria) throws Exception;

	/**
	 * This api for creating tax exemption request for property
	 * 
	 * @param taxExemptionRequest
	 * @return taxExemptionResponse
	 * @throws Exception
	 */

	public TaxExemptionResponse createTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception;

	/**
	 * This api for updating tax exemption request for property
	 * 
	 * @param taxExemptionRequest
	 * @return taxExemptionResponse
	 * @throws Exception
	 */

	public TaxExemptionResponse updateTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception;

	/**
	 * Save property history and update property
	 * 
	 * @param taxExemptionRequest
	 * @throws Exception
	 */
	public PropertyRequest savePropertyHistoryandUpdateProperty(TaxExemptionRequest taxExemptionRequest)
			throws Exception;

	/**
	 * Tax Exemption Search
	 * 
	 * @param requestInfo
	 * @param taxExemptionSearchCriteria
	 * @return
	 * @throws Exception
	 */
	public TaxExemptionSearchResponse searchTaxExemption(RequestInfoWrapper requestInfo,
			TaxExemptionSearchCriteria taxExemptionSearchCriteria) throws Exception;

	/**
	 * Description : This will create Vacancy Remission
	 * 
	 * @param vacancyRemissionRequest
	 * @return VacancyRemissionResponse
	 * @throws Exception
	 */
	public VacancyRemissionResponse createVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) throws Exception;
	
	/**
	 * Description : This will update Vacancy Remission
	 * 
	 * @param vacancyRemissionRequest
	 * @return VacancyRemissionResponse
	 * @throws Exception
	 */
	public VacancyRemissionResponse updateVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) throws Exception;
	
	/**
	 * This API will search the vacancy remission based on the given parameters
	 * 
	 * @param requestInfo
	 * @param vacancyRemissionSearchCriteria
	 * @return VacancyRemissionSearchResponse
	 * @throws Exception
	 */
	public VacancyRemissionSearchResponse searchVacancyRemission(RequestInfoWrapper requestInfo,
			VacancyRemissionSearchCriteria vacancyRemissionSearchCriteria) throws Exception;
}
