package org.egov.property.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.AttributeNotFoundException;
import org.egov.models.Boundary;
import org.egov.models.Document;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyLocation;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.Unit;
import org.egov.models.WorkFlowDetails;
import org.egov.property.exception.InvalidCodeException;
import org.egov.property.exception.InvalidFloorException;
import org.egov.property.exception.InvalidPropertyBoundaryException;
import org.egov.property.exception.InvalidUpdatePropertyException;
import org.egov.property.exception.InvalidVacantLandException;
import org.egov.property.repository.BoundaryRepository;
import org.egov.property.repository.PropertyMasterRepository;
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

	@Autowired
	private PropertyMasterRepository propertyMasterRepository;

	/**
	 * Description : This validates the property boundary
	 * 
	 * @param property
	 * @throws InvalidPropertyBoundaryException
	 */
	public void validatePropertyBoundary(Property property, RequestInfo requestInfo)
			throws InvalidPropertyBoundaryException {

		List<String> fields = getAllBoundaries();
		// TODO location service gives provision to search by multiple ids, no
		// need to do multiple calls for each boundary id
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
		return boundaryRepository.isBoundaryExists(property, requestInfo, id);

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

	/**
	 * Description : Checks whether Property Type already exists or not
	 * 
	 * @param property
	 * @param requestInfo
	 */
	public void validatePropertyMasterData(Property property, RequestInfo requestInfo) {

		if (property.getPropertyDetail().getPropertyType() != null) {
			Boolean isExists = propertyMasterRepository.checkWhetherRecordExits(property.getTenantId(),
					property.getPropertyDetail().getPropertyType(), ConstantUtility.PROPERTY_TYPE_TABLE_NAME, null);

			if (!isExists) {
				throw new InvalidCodeException(env.getProperty("invalid.input.propertytype"), requestInfo);
			}
		}

		if (property.getPropertyDetail().getFloors() != null) {
			for (Floor floor : property.getPropertyDetail().getFloors()) {
				for (Unit unit : floor.getUnits()) {
					if (unit != null)
						validateUnitData(property.getTenantId(), unit, requestInfo);

					if (unit.getUnits() != null) {
						for (Unit units : unit.getUnits()) {
							validateUnitData(property.getTenantId(), units, requestInfo);
						}
					}
				}
			}
		}

		if (property.getPropertyDetail().getDocuments() != null) {
			for (Document document : property.getPropertyDetail().getDocuments()) {

				if (document.getDocumentType() != null) {
					Boolean isDocumentTypeRecordExists = propertyMasterRepository.checkWhetherRecordExits(
							property.getTenantId(), document.getDocumentType(),
							ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, null);

					if (!isDocumentTypeRecordExists) {
						throw new InvalidCodeException(env.getProperty("invalid.input.documenttype"), requestInfo);
					}
				}
			}
		}

		if (property.getPropertyDetail().getDepartment() != null) {
			Boolean isDepartmentRecordExists = propertyMasterRepository.checkWhetherRecordExits(property.getTenantId(),
					property.getPropertyDetail().getDepartment(), ConstantUtility.DEPARTMENT_TABLE_NAME, null);

			if (!isDepartmentRecordExists)
				throw new InvalidCodeException(env.getProperty("invalid.input.department"), requestInfo);
		}

		if (property.getPropertyDetail().getFloorType() != null) {
			Boolean isFloorTypeExists = propertyMasterRepository.checkWhetherRecordExits(property.getTenantId(),
					property.getPropertyDetail().getFloorType(), ConstantUtility.FLOOR_TYPE_TABLE_NAME, null);

			if (!isFloorTypeExists)
				throw new InvalidCodeException(env.getProperty("invalid.input.floortype"), requestInfo);
		}

		if (property.getPropertyDetail().getRoofType() != null) {
			Boolean isRoofTypeExists = propertyMasterRepository.checkWhetherRecordExits(property.getTenantId(),
					property.getPropertyDetail().getRoofType(), ConstantUtility.ROOF_TYPE_TABLE_NAME, null);

			if (!isRoofTypeExists)
				throw new InvalidCodeException(env.getProperty("invalid.input.rooftype"), requestInfo);
		}

		if (property.getPropertyDetail().getWoodType() != null) {
			Boolean isWoodTypeExists = propertyMasterRepository.checkWhetherRecordExits(property.getTenantId(),
					property.getPropertyDetail().getWoodType(), ConstantUtility.WOOD_TYPE_TABLE_NAME, null);

			if (!isWoodTypeExists)
				throw new InvalidCodeException(env.getProperty("invalid.input.woodtype"), requestInfo);
		}

		if (property.getPropertyDetail().getWallType() != null) {
			Boolean isWallTypeExists = propertyMasterRepository.checkWhetherRecordExits(property.getTenantId(),
					property.getPropertyDetail().getWallType(), ConstantUtility.WALL_TYPE_TABLE_NAME, null);

			if (!isWallTypeExists)
				throw new InvalidCodeException(env.getProperty("invalid.input.walltype"), requestInfo);
		}

		if (property.getPropertyDetail().getPropertyType()
				.equalsIgnoreCase(env.getProperty("egov.property.type.vacantLand"))) {
			if (property.getVacantLand() == null)
				throw new InvalidVacantLandException(env.getProperty("invalid.property.vacantland"), requestInfo);

		} else if (property.getPropertyDetail().getFloors() == null
				|| property.getPropertyDetail().getFloors().size() <= 0) {

			throw new InvalidFloorException(env.getProperty("invalid.property.floor"), requestInfo);
		}
	}

	/**
	 * Description : Method for validating the unit details
	 * 
	 * @param tenantId
	 * @param unit
	 * @param requestInfo
	 */
	private void validateUnitData(String tenantId, Unit unit, RequestInfo requestInfo) {

		if (unit.getUsage() != null) {
			Boolean usageExists = propertyMasterRepository.checkWhetherRecordExits(tenantId, unit.getUsage(),
					ConstantUtility.USAGE_TYPE_TABLE_NAME, null);
			if (!usageExists) {
				throw new InvalidCodeException(env.getProperty("invalid.input.usage"), requestInfo);
			}
		}

		if (unit.getOccupancyType() != null) {
			Boolean occupancyTypeExists = propertyMasterRepository.checkWhetherRecordExits(tenantId,
					unit.getOccupancyType(), ConstantUtility.OCCUPANCY_TABLE_NAME, null);

			if (!occupancyTypeExists) {
				throw new InvalidCodeException(env.getProperty("invalid.input.occupancy"), requestInfo);
			}
		}

		if (unit.getAge() != null) {
			Boolean ageExists = propertyMasterRepository.checkWhetherRecordExits(tenantId, unit.getAge(),
					ConstantUtility.DEPRECIATION_TABLE_NAME, null);

			if (!ageExists) {
				throw new InvalidCodeException(env.getProperty("invalid.input.age"), requestInfo);
			}
		}

		if (unit.getStructure() != null) {
			Boolean structureExists = propertyMasterRepository.checkWhetherRecordExits(tenantId, unit.getStructure(),
					ConstantUtility.STRUCTURE_CLASS_TABLE_NAME, null);

			if (!structureExists) {
				throw new InvalidCodeException(env.getProperty("invalid.input.structure"), requestInfo);
			}
		}
	}
}
