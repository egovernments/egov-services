package org.egov.property.utility;

import org.egov.models.AttributeNotFoundException;
import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandResponse;
import org.egov.models.Property;
import org.egov.models.PropertyResponse;
import org.egov.models.PropertySearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.WorkFlowDetails;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.InvalidPropertyTypeException;
import org.egov.property.exception.InvalidUpdatePropertyException;
import org.egov.property.exception.InvalidVacancyRemissionPeriod;
import org.egov.property.exception.PropertyTaxPendingException;
import org.egov.property.exception.PropertyUnderWorkflowException;
import org.egov.property.repository.DemandRepository;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.services.PropertyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Prasad
 *
 */
@Service
public class ValidatorUtil {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	DemandRepository demandRepository;

	@Autowired
	PropertyRepository propertyRepository;
	
	@Autowired
	PropertyServiceImpl propertyServiceImpl;

	/**
	 * This API will validate the workflow details based on the given
	 * application No and Workflow details
	 * 
	 * @param acknowledgementNo
	 * @param workflowDetails
	 * @param requestInfo
	 * @throws AttributeNotFoundException
	 */
	public void validateWorkflowDeatails(String acknowledgementNo, WorkFlowDetails workflowDetails,
			RequestInfo requestInfo) throws AttributeNotFoundException {

		if (acknowledgementNo == null) {
			throw new AttributeNotFoundException(propertiesManager.getAcknowledgementNotfound(), requestInfo);

		} else {

			if (workflowDetails.getAction() == null) {
				throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowActionNotfound(), requestInfo);

			} else if (workflowDetails.getAssignee() == null
					&& (!workflowDetails.getAction().equalsIgnoreCase(propertiesManager.getSpecialNoticeAction())
							&& !workflowDetails.getAction().equalsIgnoreCase(propertiesManager.getCancelAction()))) {
				throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowAssigneeNotfound(), requestInfo);

			} else if (workflowDetails.getDepartment() == null) {
				throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowDepartmentNotfound(),
						requestInfo);

			} else if (workflowDetails.getDesignation() == null) {
				throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowDesignationNotfound(),
						requestInfo);

			} else if (workflowDetails.getStatus() == null) {
				throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowStatusNotfound(), requestInfo);

			}
		}
	}

	/**
	 * This API will check whether the any dues are there or not
	 * 
	 * @param upicNumber
	 * @param tenantId
	 * @param requestInfo
	 * @throws Exception
	 */
	public void isDemandCollected(String upicNumber, String tenantId, RequestInfo requestInfo) throws Exception {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		DemandResponse demandResponse = demandRepository.getDemands(upicNumber, tenantId, requestInfoWrapper);

		if (demandResponse != null) {
			for (Demand demand : demandResponse.getDemands()) {
				Double totalTax = 0.0;
				Double collectedAmount = 0.0;
				for (DemandDetail demandDetail : demand.getDemandDetails()) {
					totalTax += demandDetail.getTaxAmount().doubleValue();
					collectedAmount += demandDetail.getCollectionAmount().doubleValue();
				}
				if (totalTax != collectedAmount) {
					throw new PropertyTaxPendingException(propertiesManager.getInvalidTaxMessage(), requestInfo);
				}
			}
		}
	}

	/**
	 * This will Check whether the property with the given upic is under
	 * workflow, if yes then it will throw an Exception
	 * 
	 * @param upicNumber
	 * @param requestInfo
	 * @throws Exception
	 */
	public void isPropertyUnderWorkflow(String upicNumber, RequestInfo requestInfo) throws Exception {

		Boolean isPropertyUnderWorkflow = propertyRepository.isPropertyUnderWorkflow(upicNumber);

		if (isPropertyUnderWorkflow) {
			throw new PropertyUnderWorkflowException(propertiesManager.getInvalidPropertyStatus(), requestInfo);

	}
	
	}
	
	public void validateVacancyRemissionData(String upicNumber, String tenantId, String fromDate, String toDate,
			RequestInfoWrapper requestInfoWrapper) throws Exception {

		Boolean isValidPropertyType = checkValidPropertyType(upicNumber, tenantId, requestInfoWrapper);
		if (!isValidPropertyType) {
			throw new InvalidPropertyTypeException(propertiesManager.getInvalidPropertyTypeException(),
					requestInfoWrapper.getRequestInfo());
		}

		Boolean isValidFromAndToDateSequence = TimeStampUtil.compareDates(fromDate, toDate);
		if (!isValidFromAndToDateSequence) {
			throw new InvalidVacancyRemissionPeriod(propertiesManager.getInvalidTodateGreaterthanFromDate(),
					requestInfoWrapper.getRequestInfo());
		}

		Boolean isValidDate = TimeStampUtil.ValidDates(fromDate, toDate);
		if (!isValidDate) {
			throw new InvalidVacancyRemissionPeriod(propertiesManager.getFromOrToDateLessthanCurrentDate(),
					requestInfoWrapper.getRequestInfo());
		}

		Boolean isValidPeriod = TimeStampUtil.checkValidPeriod(fromDate, toDate);
		if (!isValidPeriod) {
			throw new InvalidVacancyRemissionPeriod(propertiesManager.getVacancyRemissionInvalidPeriodException(),
					requestInfoWrapper.getRequestInfo());
		}
	}
	/**
	 * This method check Property type
	 * 
	 * @param upicNumber
	 * @param tenantId
	 * @param requestInfoWrapper
	 * @return boolean value
	 * @throws Exception
	 */
	private Boolean checkValidPropertyType(String upicNumber, String tenantId, RequestInfoWrapper requestInfoWrapper)
			throws Exception {
		Boolean isValid = Boolean.TRUE;
		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setTenantId(tenantId);
		propertySearchCriteria.setUpicNumber(upicNumber);
		PropertyResponse propertyResponse = propertyServiceImpl.searchProperty(requestInfoWrapper.getRequestInfo(),
				propertySearchCriteria);
		Property property = propertyResponse.getProperties().get(0);
		if (property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {
			isValid = Boolean.FALSE;
		}
		if (property.getPropertyDetail().getIsExempted()) {
			isValid = Boolean.FALSE;
		}
		return isValid;
	}


}
