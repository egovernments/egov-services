package org.egov.property.utility;

import org.egov.models.AttributeNotFoundException;
import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.WorkFlowDetails;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.InvalidUpdatePropertyException;
import org.egov.property.exception.PropertyTaxPendingException;
import org.egov.property.exception.PropertyUnderWorkflowException;
import org.egov.property.repository.DemandRepository;
import org.egov.property.repository.PropertyRepository;
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

}
