package org.egov.property.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.egov.enums.StatusEnum;
import org.egov.models.AuditDetails;
import org.egov.models.Document;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.PropertySearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.repository.PropertyMasterRepository;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * This class creates a property
 * 
 * @author S Anilkumar
 *
 */
@Service
@Slf4j
public class PersisterService {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	PropertyMasterRepository propertyMasterRepository;

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
	 * database database
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
			if (!property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {
				if (property.getPropertyDetail().getNoOfFloors() == null) {
					if (property.getPropertyDetail().getFloors() != null) {
						int floorCount = property.getPropertyDetail().getFloors().size();
						property.getPropertyDetail().setNoOfFloors((long) floorCount);
					}

				}
			}
			Long propertyDetailsId = propertyRepository.savePropertyDetails(property, propertyId);
			if (!property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {

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
						if (unit.getUnitType().toString().equalsIgnoreCase(propertiesManager.getUnitType())
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
		for (Property property : properties) {
			AuditDetails auditDetails = getUpdatedAuditDetails(propertyRequest.getRequestInfo(),
					ConstantUtility.PROPERTY_TABLE_NAME, property.getId());
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
			property.setAuditDetails(auditDetails);
			propertyRepository.updateProperty(property);
			if (property.getAddress() != null) {
				property.getAddress().setAuditDetails(auditDetails);
				propertyRepository.updateAddress(property.getAddress(), property.getAddress().getId(),
						property.getId());
			}
			propertyDetail.setAuditDetails(auditDetails);
			if (!property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {
				if (property.getPropertyDetail().getNoOfFloors() == null) {
					if (property.getPropertyDetail().getFloors() != null) {
						int floorCount = property.getPropertyDetail().getFloors().size();
						property.getPropertyDetail().setNoOfFloors((long) floorCount);
					}

				}
			}
			propertyRepository.updatePropertyDetail(property.getPropertyDetail(), property.getId());
			if (property.getVacantLand() != null) {
				property.getVacantLand().setAuditDetails(auditDetails);
				propertyRepository.updateVacantLandDetail(property.getVacantLand(), property.getVacantLand().getId(),
						property.getId());
			}
			if (!property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {
				for (Floor floor : property.getPropertyDetail().getFloors()) {
					floor.setAuditDetails(auditDetails);
					propertyRepository.updateFloor(floor, property.getPropertyDetail().getId());
					for (Unit unit : floor.getUnits()) {
						unit.setAuditDetails(auditDetails);
						if (unit.getIsAuthorised() == null) {
							unit.setIsAuthorised(true);
						}
						if (unit.getIsStructured() == null) {
							unit.setIsStructured(true);
						}
						propertyRepository.updateUnit(unit);
						if (unit.getUnitType().toString().equalsIgnoreCase(propertiesManager.getUnitType())
								&& unit.getUnits() != null) {
							for (Unit room : unit.getUnits()) {
								room.setAuditDetails(auditDetails);
								propertyRepository.updateRoom(room);
							}
						}
					}
				}
				if (property.getPropertyDetail().getDocuments() != null) {
					for (Document document : property.getPropertyDetail().getDocuments()) {
						document.setAuditDetails(auditDetails);
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
				owner.setAuditDetails(auditDetails);
				propertyRepository.updateUser(owner, property.getId());
			}
			property.getBoundary().setAuditDetails(auditDetails);
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

		AuditDetails auditDetails = getAuditDetail(titleTransferRequest.getRequestInfo());
		titleTransferRequest.getTitleTransfer().setAuditDetails(auditDetails);
		Long titleTransferId = propertyRepository.saveTitleTransfer(titleTransferRequest.getTitleTransfer());
		for (User owner : titleTransferRequest.getTitleTransfer().getNewOwners()) {
			owner.setAuditDetails(auditDetails);
			propertyRepository.saveTitleTransferUser(owner, titleTransferId);
		}
		if (titleTransferRequest.getTitleTransfer().getDocuments() != null) {
			for (Document document : titleTransferRequest.getTitleTransfer().getDocuments()) {
				document.setAuditDetails(auditDetails);
				propertyRepository.saveTitleTransferDocument(document, titleTransferId);
			}
		}
		if (titleTransferRequest.getTitleTransfer().getCorrespondenceAddress() != null) {
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
		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setTenantId(titleTransfer.getTenantId());
		propertySearchCriteria.setUpicNumber(titleTransfer.getUpicNo());

		try {
			PropertyResponse propertyResponse = propertyServiceImpl.searchProperty(requestInfo, propertySearchCriteria);
			if (propertyResponse != null && propertyResponse.getProperties().size() > 0) {
				property = propertyResponse.getProperties().get(0);
			}
		} catch (Exception e) {
			throw new PropertySearchException(propertiesManager.getInvalidInput(), requestInfo);
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

		AuditDetails auditDetails = getUpdatedAuditDetails(titleTransferRequest.getRequestInfo(),
				ConstantUtility.TITLETRANSFER_TABLE_NAME, titleTransferRequest.getTitleTransfer().getId());
		titleTransferRequest.getTitleTransfer().setAuditDetails(auditDetails);
		propertyRepository.updateTitleTransfer(titleTransferRequest.getTitleTransfer());
	}

	/**
	 * Description : This method will use for update main property stateId, user
	 * and address database and address database
	 * 
	 * @param Property
	 */

	public Property updateTitleTransferProperty(TitleTransferRequest titleTransferRequest, Property property)
			throws Exception {
		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		AuditDetails auditDetails = getUpdatedAuditDetails(titleTransferRequest.getRequestInfo(),
				ConstantUtility.PROPERTY_TABLE_NAME, property.getId());
		property.getPropertyDetail().setStateId(titleTransfer.getStateId());
		property.getPropertyDetail().setAuditDetails(auditDetails);

		Long propertyId = property.getId();
		updateTitleTransfer(titleTransferRequest);
		property.setAuditDetails(auditDetails);

		propertyRepository.updateTitleTransferProperty(property);
		if (titleTransfer.getCorrespondenceAddress() != null) {

			AuditDetails titletransferAuditDetails = getUpdatedAuditDetails(titleTransferRequest.getRequestInfo(),
					ConstantUtility.TITLETRANSFER_TABLE_NAME, titleTransferRequest.getTitleTransfer().getId());
			titleTransfer.getCorrespondenceAddress().setAuditDetails(titletransferAuditDetails);
			propertyRepository.updateAddress(titleTransfer.getCorrespondenceAddress(), property.getAddress().getId(),
					propertyId);
		}

		propertyRepository.updateTitleTransferPropertyDetail(property.getPropertyDetail());
		for (User owner : property.getOwners()) {
			propertyRepository.deleteUser(owner.getId(), propertyId);

		}

		for (User owner : titleTransfer.getNewOwners()) {
			AuditDetails ownerAuditDetails = getAuditDetail(titleTransferRequest.getRequestInfo());
			owner.setAuditDetails(ownerAuditDetails);
			propertyRepository.saveUser(owner, propertyId);

		}
		return property;
	}

	/**
	 * Description: save property history
	 * 
	 * @param properties
	 * @throws SQLException
	 */

	public void addPropertyHistory(TitleTransferRequest titleTransferRequest, Property property) throws Exception {
		savePropertyHistory(property);
	}

	/**
	 * Description : This method will use for insert property related data in
	 * database
	 * 
	 * @param properties
	 */
	private Property savePropertyHistory(Property property) throws Exception {
		propertyRepository.getPropertyForHistory(property.getId());
		if (property.getAddress() != null) {
			propertyRepository.getAddressForHistory(property.getAddress().getId());
		}
		if (property.getPropertyDetail() != null) {
			propertyRepository.getPropertyDetailsForHistory(property.getPropertyDetail().getId());
			property.getPropertyDetail().setStatus(StatusEnum.HISTORY);
			propertyRepository.updatePropertyHistoryStatus(property.getPropertyDetail());

		}

		if (!property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {
			for (Floor floor : property.getPropertyDetail().getFloors()) {
				propertyRepository.getFloorForHistory(floor.getId());
				for (Unit unit : floor.getUnits()) {
					propertyRepository.getUnitForHistory(unit.getId());
					if (unit.getUnitType().toString().equalsIgnoreCase(propertiesManager.getUnitType())
							&& unit.getUnits() != null) {
						for (Unit room : unit.getUnits()) {
							propertyRepository.getUnitForHistory(room.getId());
						}
					}
				}
				if (property.getPropertyDetail().getDocuments() != null) {
					for (Document document : property.getPropertyDetail().getDocuments()) {
						propertyRepository.getDocumentForHistory(document.getId());
					}
				}
			}
		}

		if (property.getVacantLand() != null) {
			propertyRepository.getVacantLandForHistory(property.getVacantLand().getId());
		}

		if (property.getBoundary() != null) {
			propertyRepository.getPropertyLocationForHistory(property.getBoundary().getId());
		}
		for (User owner : property.getOwners()) {
			propertyRepository.getOwnerForHistory(owner, property.getId());
		}
		return property;
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

	private AuditDetails getUpdatedAuditDetails(RequestInfo requestInfo, String tableName, Long id) {

		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setLastModifiedBy(userId);
		auditDetails.setLastModifiedTime(currEpochDate);

		propertyMasterRepository.getCreatedAuditDetails(auditDetails, tableName, id);
		return auditDetails;
	}

	public void movePropertyToHistory(PropertyRequest propertyRequest) throws Exception {
		Boolean moved = propertyRepository.movePropertytoHistory(propertyRequest);
		if (!moved) {
			log.error("Unable to move property to history");
		}

	}
}
