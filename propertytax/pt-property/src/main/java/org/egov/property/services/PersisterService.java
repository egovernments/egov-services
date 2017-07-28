package org.egov.property.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.Document;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.property.consumer.Producer;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.repository.PropertyRepository;
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
	Producer producer;

	@Autowired
	PropertyServiceImpl propertyServiceImpl;

	/**
	 * Description: save property
	 * 
	 * @param properties
	 * @throws SQLException
	 */
	@Transactional
	public void addProperty(PropertyRequest propertyRequest) throws Exception {
		saveProperty(propertyRequest);

	}

	/**
	 * Description : This method will use for insert property related data in
	 * database
	 * 
	 * @param properties
	 */
	private List<Property> saveProperty(PropertyRequest propertyRequest) throws Exception {
		List<Property> properties = propertyRequest.getProperties();
		for (Property property : properties) {

			if (property.getIsUnderWorkflow() == null) {
				property.setIsUnderWorkflow(false);
			}

			if (property.getIsAuthorised() == null) {
				property.setIsAuthorised(true);
			}

			if (property.getActive() == null) {
				property.setActive(true);
			}

			PropertyDetail propertyDetail = property.getPropertyDetail();

			if (propertyDetail.getIsVerified() == null) {
				propertyDetail.setIsVerified(false);
			}

			if (propertyDetail.getIsExempted() == null) {
				propertyDetail.setIsExempted(false);
			}

			if (propertyDetail.getIsSuperStructure() == null) {
				propertyDetail.setIsSuperStructure(false);
			}

			AuditDetails auditDetails = getAuditDetail(propertyRequest.getRequestInfo());

			property.setAuditDetails(auditDetails);

			Long propertyId = propertyRepository.saveProperty(property);

			property.getAddress().setAuditDetails(auditDetails);
			propertyRepository.saveAddress(property, propertyId);

			propertyDetail.setAuditDetails(auditDetails);
			Long propertyDetailsId = propertyRepository.savePropertyDetails(property, propertyId);

			if (!property.getPropertyDetail().getPropertyType()
					.equalsIgnoreCase(environment.getProperty("vacantLand"))) {
				for (Floor floor : property.getPropertyDetail().getFloors()) {
					floor.setAuditDetails(auditDetails);
					Long floorId = propertyRepository.saveFloor(floor, propertyDetailsId);

					for (Unit unit : floor.getUnits()) {

						if (unit.getIsAuthorised() == null) {
							unit.setIsAuthorised(true);
						}
						if (unit.getIsStructured() == null) {
							unit.setIsStructured(true);
						}

						unit.setAuditDetails(auditDetails);
						Long unitId = propertyRepository.saveUnit(unit, floorId);

						if (unit.getUnitType().toString().equalsIgnoreCase(environment.getProperty("unit.type"))
								&& unit.getUnits() != null) {

							for (Unit room : unit.getUnits()) {
								propertyRepository.saveRoom(room, floorId, unitId);
							}
						}

					}
				}

				if (property.getPropertyDetail().getDocuments() != null) {
					for (Document document : property.getPropertyDetail().getDocuments()) {
						document.setAuditDetails(auditDetails);
						propertyRepository.saveDocument(document, propertyDetailsId);

					}
				}
			}

			if (property.getVacantLand() != null) {
				property.getVacantLand().setAuditDetails(auditDetails);
				propertyRepository.saveVacantLandDetail(property, propertyId);
			}

			property.getBoundary().setAuditDetails(auditDetails);
			propertyRepository.saveBoundary(property, propertyId);

			for (User owner : property.getOwners()) {

				if (owner.getIsPrimaryOwner() == null) {
					owner.setIsPrimaryOwner(false);
				}
				if (owner.getIsSecondaryOwner() == null) {
					owner.setIsSecondaryOwner(false);
				}
				if (owner.getAccountLocked() == null) {
					owner.setAccountLocked(false);
				}
				owner.setAuditDetails(auditDetails);
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
	public void updateProperty(PropertyRequest propertyRequest) throws Exception {
		List<Property> properties = propertyRequest.getProperties();
		AuditDetails auditDetails = getAuditDetail(propertyRequest.getRequestInfo());

		for (Property property : properties) {

			if (property.getIsUnderWorkflow() == null) {
				property.setIsUnderWorkflow(false);
			}

			if (property.getIsAuthorised() == null) {
				property.setIsAuthorised(true);
			}

			if (property.getActive() == null) {
				property.setActive(true);
			}

			PropertyDetail propertyDetail = property.getPropertyDetail();

			if (propertyDetail.getIsVerified() == null) {
				propertyDetail.setIsVerified(false);
			}

			if (propertyDetail.getIsExempted() == null) {
				propertyDetail.setIsExempted(false);
			}

			if (propertyDetail.getIsSuperStructure() == null) {
				propertyDetail.setIsSuperStructure(false);
			}

			property.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
			property.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
			propertyRepository.updateProperty(property);

			property.getAddress().getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
			property.getAddress().getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
			propertyRepository.updateAddress(property.getAddress(), property.getAddress().getId(), property.getId());

			propertyDetail.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
			propertyDetail.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
			propertyRepository.updatePropertyDetail(property.getPropertyDetail(), property.getId());

			if (property.getVacantLand() != null) {
				property.getVacantLand().getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
				property.getVacantLand().getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
				propertyRepository.updateVacantLandDetail(property.getVacantLand(), property.getVacantLand().getId(),
						property.getId());
			}

			if (!property.getPropertyDetail().getPropertyType()
					.equalsIgnoreCase(environment.getProperty("vacantLand"))) {

				for (Floor floor : property.getPropertyDetail().getFloors()) {
					floor.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
					floor.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
					propertyRepository.updateFloor(floor, property.getPropertyDetail().getId());

					for (Unit unit : floor.getUnits()) {

						unit.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
						unit.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());

						if (unit.getIsAuthorised() == null) {
							unit.setIsAuthorised(true);
						}
						if (unit.getIsStructured() == null) {
							unit.setIsStructured(true);
						}

						propertyRepository.updateUnit(unit);

						if (unit.getUnitType().toString().equalsIgnoreCase(environment.getProperty("unit.type"))
								&& unit.getUnits() != null) {
							for (Unit room : unit.getUnits()) {
								propertyRepository.updateRoom(room);
							}
						}
					}
				}
				if (property.getPropertyDetail().getDocuments() != null) {
					for (Document document : property.getPropertyDetail().getDocuments()) {
						document.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
						document.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
						propertyRepository.updateDocument(document, property.getPropertyDetail().getId());
					}
				}
			}

			for (User owner : property.getOwners()) {

				if (owner.getIsPrimaryOwner() == null) {
					owner.setIsPrimaryOwner(false);
				}
				if (owner.getIsSecondaryOwner() == null) {
					owner.setIsSecondaryOwner(false);
				}
				if (owner.getAccountLocked() == null) {
					owner.setAccountLocked(false);
				}

				owner.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
				owner.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());

				propertyRepository.updateUser(owner, property.getId());
			}

			propertyRepository.updateBoundary(property.getBoundary(), property.getId());
		}
	}

	/**
	 * Description: save title transfer
	 * 
	 * @param Title
	 *            Transfer
	 * @throws SQLException
	 */
	@Transactional
	public void addTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {

		saveTitleTransfer(titleTransferRequest);

	}

	/**
	 * Description : This method will use for insert property related data in
	 * database
	 * 
	 * @param properties
	 */
	private void saveTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {

		if (titleTransferRequest != null) {
			AuditDetails auditDetails = getAuditDetail(titleTransferRequest.getRequestInfo());

			titleTransferRequest.getTitleTransfer().setAuditDetails(auditDetails);
			Long titleTransferId = propertyRepository.saveTitleTransfer(titleTransferRequest.getTitleTransfer());

			for (User owner : titleTransferRequest.getTitleTransfer().getNewOwners()) {
				owner.setAuditDetails(auditDetails);
				propertyRepository.saveTitleTransferUser(owner, titleTransferId);

			}

			for (Document document : titleTransferRequest.getTitleTransfer().getDocuments()) {
				document.setAuditDetails(auditDetails);
				propertyRepository.saveTitleTransferDocument(document, titleTransferId);
			}

			titleTransferRequest.getTitleTransfer().getCorrespondenceAddress().setAuditDetails(auditDetails);
			propertyRepository.saveTitleTransferAddress(titleTransferRequest.getTitleTransfer(), titleTransferId);

		}
	}

	/**
	 * Search property based on upic no
	 * 
	 * @param titleTransferRequest
	 * @return
	 * @throws Exception
	 */
	public Property getPropertyUsingUpicNo(TitleTransferRequest titleTransferRequest) throws Exception {
		RequestInfo requestInfo = titleTransferRequest.getRequestInfo();
		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		Property property = null;
		String tenantId = titleTransfer.getTenantId();
		String upicNo = titleTransfer.getUpicNo();
		try {
			PropertyResponse propertyResponse = propertyServiceImpl.searchProperty(requestInfo, tenantId, null, upicNo,
					null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			if (propertyResponse != null && propertyResponse.getProperties().size() > 0) {
				property = propertyResponse.getProperties().get(0);
			}

		} catch (Exception e) {
			throw new PropertySearchException(environment.getProperty("invalid.input"), requestInfo);
		}
		return property;

	}

	/**
	 * Description : This method will use for insert property related data in
	 * database
	 * 
	 * @param TitleTransfer
	 */
	@Transactional
	public void updateTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {

		if (titleTransferRequest != null) {
			AuditDetails auditDetails = getAuditDetail(titleTransferRequest.getRequestInfo());
			titleTransferRequest.getTitleTransfer().setAuditDetails(auditDetails);
			propertyRepository.updateTitleTransfer(titleTransferRequest.getTitleTransfer());

		}
	}

	/**
	 * Description : This method will use for update main property stateId, user
	 * and address database
	 * 
	 * @param Property
	 */

	public void updateTitleTransferProperty(TitleTransferRequest titleTransferRequest) throws Exception {

		Property property = getPropertyUsingUpicNo(titleTransferRequest);

		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();

		AuditDetails auditDetails = getAuditDetail(titleTransferRequest.getRequestInfo());

		property.getPropertyDetail().setStateId(titleTransfer.getStateId());

		property.getPropertyDetail().getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
		property.getPropertyDetail().getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
		Long propertyId = property.getId();

		updateTitleTransfer(titleTransferRequest);

		property.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
		property.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
		propertyRepository.updateTitleTransferProperty(property);

		titleTransfer.getCorrespondenceAddress().getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
		titleTransfer.getCorrespondenceAddress().getAuditDetails()
				.setLastModifiedTime(auditDetails.getLastModifiedTime());
		propertyRepository.updateAddress(titleTransfer.getCorrespondenceAddress(), property.getAddress().getId(),
				propertyId);

		property.getPropertyDetail().getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
		property.getPropertyDetail().getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
		propertyRepository.updateTitleTransferPropertyDetail(property.getPropertyDetail());

		for (User owner : titleTransfer.getNewOwners()) {
			owner.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
			owner.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
			propertyRepository.updateUser(owner, propertyId);
		}

	}

	/**
	 * Description: save property history
	 * 
	 * @param properties
	 * @throws SQLException
	 */

	public void addPropertyHistory(TitleTransferRequest titleTransferRequest) throws Exception {
		Property property = getPropertyUsingUpicNo(titleTransferRequest);
		savePropertyHistory(property);
	}

	/**
	 * Description : This method will use for insert property related data in
	 * database
	 * 
	 * @param properties
	 */
	private Property savePropertyHistory(Property property) throws Exception {

		Long propertyId = property.getId();

		propertyRepository.savePropertyHistory(property);

		propertyRepository.saveAddressHistory(property, propertyId);

		propertyRepository.savePropertyDetailsHistory(property, propertyId);

		Long propertyDetailsId = property.getPropertyDetail().getId();

		for (Floor floor : property.getPropertyDetail().getFloors()) {

			propertyRepository.saveFloorHistory(floor, propertyDetailsId);

			Long floorId = floor.getId();

			for (Unit unit : floor.getUnits()) {

				propertyRepository.saveUnitHistory(unit, floorId);

				Long unitId = unit.getId();

				if (unit.getUnitType().toString().equalsIgnoreCase(environment.getProperty("unit.type"))
						&& unit.getUnits() != null) {
					for (Unit room : unit.getUnits()) {
						propertyRepository.saveRoomHistory(room, floorId, unitId);

					}
				}

			}
			for (Document document : property.getPropertyDetail().getDocuments()) {
				propertyRepository.saveDocumentHistory(document, propertyDetailsId);

			}

		}

		propertyRepository.saveVacantLandDetailHistory(property, propertyId);

		propertyRepository.saveBoundaryHistory(property, propertyId);

		for (User owner : property.getOwners()) {
			propertyRepository.saveUserHistory(owner, propertyId);
		}

		return property;
	}

	/**
	 * Save property history and update property
	 * 
	 * @param titleTransferRequest
	 * @throws Exception
	 */
	@Transactional
	public PropertyRequest savePropertyHistoryandUpdateProperty(TitleTransferRequest titleTransferRequest)
			throws Exception {
		addPropertyHistory(titleTransferRequest);
		updateTitleTransferProperty(titleTransferRequest);
		Property property = getPropertyUsingUpicNo(titleTransferRequest);
		PropertyRequest propertyRequest = new PropertyRequest();
		propertyRequest.setRequestInfo(titleTransferRequest.getRequestInfo());
		List<Property> properties = new ArrayList<Property>();
		properties.add(property);
		propertyRequest.setProperties(properties);
		return propertyRequest;
	}

	private AuditDetails getAuditDetail(RequestInfo requestInfo) {

		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();

		AuditDetails auditDetail = new AuditDetails();
		auditDetail.setCreatedBy(userId);
		auditDetail.setCreatedTime(currEpochDate);
		auditDetail.setLastModifiedBy(userId);
		auditDetail.setLastModifiedTime(currEpochDate);
		return auditDetail;
	}

}
