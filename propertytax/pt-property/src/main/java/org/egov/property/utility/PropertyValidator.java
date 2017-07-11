package org.egov.property.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.AttributeNotFoundException;
import org.egov.models.Boundary;
import org.egov.models.Property;
import org.egov.models.PropertyLocation;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.WorkFlowDetails;
import org.egov.property.exception.InvalidPropertyBoundaryException;
import org.egov.property.exception.InvalidUpdatePropertyException;
import org.egov.property.repository.BoundaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * This Service to validate the property attributes
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Service
public class PropertyValidator {

	@Autowired
	private Environment env;


	@Autowired
	ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	BoundaryRepository boundaryRepository;


	/**
	 * Description : This validates the property boundary
	 * 
	 * @param property
	 * @throws InvalidPropertyBoundaryException
	 */
	public void validatePropertyBoundary(Property property, RequestInfo requestInfo)
			throws InvalidPropertyBoundaryException {

		List<String> fields = getAllBoundaries();
		//TODO location service gives provision to search by multiple ids, no need to do multiple calls for each boundary id
		for (String field : fields) {
			validateBoundaryFields(property, field, requestInfo);
		}
	}

	/**
	 * Description : This validates the each boundary field of the property
	 * 
	 * @param property
	 * @param field
	 * @return true
	 * @throws InvalidPropertyBoundaryException
	 */
	public Boolean validateBoundaryFields(Property property, String field, RequestInfo requestInfo)
			throws InvalidPropertyBoundaryException {

		PropertyLocation propertyLocation = property.getBoundary();
		Long id;
        if (field.equalsIgnoreCase(env.getProperty("revenue.boundary"))) {
			id = propertyLocation.getRevenueBoundary().getId();
		} else if (field.equalsIgnoreCase(env.getProperty("location.boundary"))) {
			id = propertyLocation.getLocationBoundary().getId();
		} else {
			id = propertyLocation.getAdminBoundary().getId();
		}
      return  boundaryRepository.isBoundaryExists(property, requestInfo, id);
		
	}

	/**
	 * Description : This validates acknowledgement number and workflow details
	 * 
	 * @param property
	 * @param requestInfo
	 * @throws AttributeNotFoundException
	 * @author Yosadhara
	 */
	public void validateWorkflowDeatails(Property property, RequestInfo requestInfo) throws AttributeNotFoundException {

		String acknowledgementNo = property.getPropertyDetail().getApplicationNo();

		if (acknowledgementNo == null) {
			throw new AttributeNotFoundException(env.getProperty("acknowledgement.message"), requestInfo);

		} else {
			WorkFlowDetails workflowDetails = property.getPropertyDetail().getWorkFlowDetails();

			if (workflowDetails.getAction() == null) {
				throw new InvalidUpdatePropertyException(env.getProperty("workflow.action.message"), requestInfo);

			} else if (workflowDetails.getAssignee() == null) {
				throw new InvalidUpdatePropertyException(env.getProperty("workflow.assignee.message"), requestInfo);

			} else if (workflowDetails.getDepartment() == null) {
				throw new InvalidUpdatePropertyException(env.getProperty("workflow.department.message"), requestInfo);

			} else if (workflowDetails.getDesignation() == null) {
				throw new InvalidUpdatePropertyException(env.getProperty("workflow.designation.message"), requestInfo);

			} else if (workflowDetails.getStatus() == null) {
				throw new InvalidUpdatePropertyException(env.getProperty("workflow.status.message"), requestInfo);

			}
		}
	}

	/**
	 * Description : This is to get the searchType fields of the target class
	 * 
	 * @param target
	 * @param searchType
	 * @return result
	 */
	public List<String> getFieldsOfType(Class<?> target, Class<?> searchType) {
		Field[] fields = target.getDeclaredFields();
		List<String> result = new ArrayList<String>();
		for (Field field : fields) {
			if (field.getType().equals(searchType)) {
				result.add(field.getName());
			}
		}
		return result;
	}

	/**
	 * Description : This is to get the BoundaryType fields of the
	 * PropertyLocation class
	 * 
	 * @author Pavan Kumar Kamma
	 * @return List<String>
	 */
	public List<String> getAllBoundaries() {
		return getFieldsOfType(PropertyLocation.class, Boundary.class);
	}

}
