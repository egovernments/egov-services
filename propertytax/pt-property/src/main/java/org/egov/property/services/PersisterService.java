package org.egov.property.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.egov.models.AttributeNotFoundException;
import org.egov.models.Document;
import org.egov.models.DocumentType;
import org.egov.models.Floor;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.property.consumer.PropertyProducer;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.util.PropertyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * This class creates a property
 * 
 * @author S Anilkumar
 *
 */
@Service
public class PersisterService {

	@Autowired
	Environment environment;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertyProducer producer;

	@Autowired
	PropertyValidator propertyValidator;

	/**
	 * Description: save property
	 * 
	 * @param properties
	 * @throws SQLException
	 */
	@Transactional
	public void addProperty(List<Property> properties) throws Exception {

		saveProperty(properties);

	}

	/**
	 * Description : This method will use for insert property related data in
	 * database
	 * 
	 * @param properties
	 */
	private List<Property> saveProperty(List<Property> properties) throws Exception {

		for (Property property : properties) {

			Integer propertyId = propertyRepository.saveProperty(property);

			propertyRepository.saveAddress(property, propertyId);

			Integer propertyDetailsId = propertyRepository.savePropertyDetails(property, propertyId);

			for (Floor floor : property.getPropertyDetail().getFloors()) {

				Integer floorId = propertyRepository.saveFloor(floor, propertyDetailsId);

				for (Unit unit : floor.getUnits()) {

					Integer unitId = propertyRepository.saveUnit(unit, floorId);

					if (unit.getUnitType().toString().equalsIgnoreCase(environment.getProperty("unit.type"))
							&& unit.getUnits() != null) {
						for (Unit room : unit.getUnits()) {
							propertyRepository.saveRoom(room, floorId, unitId);
						}
					}

				}
				for (Document document : property.getPropertyDetail().getDocuments()) {

					Integer documentId = propertyRepository.saveDocument(document, propertyDetailsId);

					DocumentType documentType = document.getDocumentType();

					propertyRepository.saveDocumentType(documentType, documentId);
				}

			}

			propertyRepository.saveVacantLandDetail(property, propertyId);

			propertyRepository.saveBoundary(property, propertyId);

			for (User owner : property.getOwners()) {

				propertyRepository.saveUser(owner, propertyId);

			}

		}
		return properties;
	}

	/**
	 * update method
	 * 
	 * @param: List
	 *             of properties This method updates: 1. Property 2. Address 3.
	 *             Property Details 4. Vacant Land 5. Floor 6. Document and
	 *             Document type 7. User 8. Boundary
	 **/

	@Transactional
	public void updateProperty(List<Property> properties) throws Exception {

		for (Property property : properties) {

			propertyRepository.updateProperty(property);
			propertyRepository.updateAddress(property.getAddress(), property.getAddress().getId(), property.getId());

			propertyRepository.updatePropertyDetail(property.getPropertyDetail(), property.getId());
			propertyRepository.updateVacantLandDetail(property.getVacantLand(), property.getVacantLand().getId(),
					property.getId());
			for (Floor floor : property.getPropertyDetail().getFloors()) {
				propertyRepository.updateFloor(floor, property.getPropertyDetail().getId());

				for (Unit unit : floor.getUnits()) {

					propertyRepository.updateUnit(unit);
					if (unit.getUnitType().toString().equalsIgnoreCase(environment.getProperty("unit.type"))
							&& unit.getUnits() != null) {
						for (Unit room : unit.getUnits()) {
							propertyRepository.updateRoom(room);
						}
					}
				}
			}

			for (Document document : property.getPropertyDetail().getDocuments()) {

				propertyRepository.updateDocument(document, property.getPropertyDetail().getId());
				propertyRepository.updateDocumentType(document.getDocumentType(), document.getId());
			}

			for (User owner : property.getOwners()) {
				propertyRepository.updateUser(owner, property.getId());
			}

			propertyRepository.updateBoundary(property.getBoundary(), property.getId());
		}
	}
}
