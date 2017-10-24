package org.egov.property.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.enums.StatusEnum;
import org.egov.models.AuditDetails;
import org.egov.models.Demolition;
import org.egov.models.DemolitionRequest;
import org.egov.models.DemolitionSearchCriteria;
import org.egov.models.Document;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.PropertySearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TaxExemption;
import org.egov.models.TaxExemptionRequest;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.models.WorkFlowDetails;
import org.egov.models.VacancyRemissionRequest;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.repository.DemolitionRepository;
import org.egov.property.repository.PropertyMasterRepository;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.repository.TaxExemptionRepository;
import org.egov.property.repository.VacancyRemissionRepository;
import org.egov.property.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

	@Autowired
	DemolitionRepository demolitionRepository;
	
	@Autowired
	VacancyRemissionRepository vacancyRemissionRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	TaxExemptionRepository taxExemptionRepository;

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
								room.setAuditDetails(auditDetails);
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
			WorkFlowDetails workflowDetails = property.getPropertyDetail().getWorkFlowDetails();
			if (property.getIsUnderWorkflow() == null) {
				if (workflowDetails.getAction().equalsIgnoreCase(propertiesManager.getSpecialNoticeAction())) {
					property.setIsUnderWorkflow(false);
				} else {
					property.setIsUnderWorkflow(true);
				}
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

				List<Floor> existingFloorList = property.getPropertyDetail().getFloors().stream()
						.filter(floor -> floor.getId() != null).collect(Collectors.toList());
				deleteFloors(existingFloorList, property.getPropertyDetail().getId());

				for (Floor floor : property.getPropertyDetail().getFloors()) {
					floor.setAuditDetails(auditDetails);

					Long id = floor.getId();

					if (id != null) {
						propertyRepository.updateFloor(floor, property.getPropertyDetail().getId());
						List<Unit> existingUnitList = floor.getUnits().stream().filter(unit -> unit.getId() != null)
								.collect(Collectors.toList());
						deleteUnits(existingUnitList, id);
					} else {
						Long floorId = propertyRepository.saveFloor(floor, property.getPropertyDetail().getId());
						floor.setId(floorId);
					}

					for (Unit unit : floor.getUnits()) {
						unit.setAuditDetails(auditDetails);
						if (unit.getIsAuthorised() == null) {
							unit.setIsAuthorised(true);
						}
						if (unit.getIsStructured() == null) {
							unit.setIsStructured(true);
						}
						if (unit.getId() != null) {
							propertyRepository.updateUnit(unit);
						} else {
							Long unitId = propertyRepository.saveUnit(unit, floor.getId());
							unit.setId(unitId);
						}
						if (unit.getUnitType().toString().equalsIgnoreCase(propertiesManager.getUnitType())
								&& unit.getUnits() != null) {
							for (Unit room : unit.getUnits()) {
								room.setAuditDetails(auditDetails);
								if (room.getId() != null) {
									propertyRepository.saveRoom(unit, floor.getId(), unit.getId());
								} else {
									propertyRepository.updateRoom(room);
								}
							}
						}
					}
				}
				if (property.getPropertyDetail().getDocuments() != null) {
					List<Document> documents = property.getPropertyDetail().getDocuments().stream()
							.filter(document -> document.getId() != null).collect(Collectors.toList());
					deleteDocuments(documents, property.getPropertyDetail().getId());
					for (Document document : property.getPropertyDetail().getDocuments()) {
						document.setAuditDetails(auditDetails);
						if (document.getId() != null) {
							propertyRepository.updateDocument(document, property.getPropertyDetail().getId());
						} else {
							propertyRepository.saveDocument(document, property.getPropertyDetail().getId());
						}
					}
				}
			}

			List<User> dbOwners = propertyRepository.getPropertyUserByProperty(property.getId());

			deleteOwners(property.getOwners(), dbOwners, property.getId());
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

				int count = dbOwners.stream().filter(dbOwner -> dbOwner.getOwner().equals(owner.getId()))
						.collect(Collectors.toList()).size();
				if (count > 0) {
					propertyRepository.updateUser(owner, property.getId());
				} else {
					propertyRepository.saveUser(owner, property.getId());
				}

			}
			property.getBoundary().setAuditDetails(auditDetails);
			propertyRepository.updateBoundary(property.getBoundary(), property.getId());
		}

	}

	private void deleteUnits(List<Unit> units, Long floorId) {
		List<Unit> dbUnits = propertyRepository.getUnitsByFloor(floorId);
		dbUnits.forEach(dbUnit -> {
			int count = units.stream().filter(unit -> unit.getId().equals(dbUnit.getId())).collect(Collectors.toList())
					.size();
			if (count == 0) {
				propertyRepository.deleteUnitByFloorId(floorId);
			}
		});
	}

	private void deleteOwners(List<User> owners, List<User> dbOwners, Long propertyId) {
		dbOwners.forEach(user -> {
			int count = owners.stream().filter(owner -> owner.getId().equals(user.getOwner()))
					.collect(Collectors.toList()).size();
			if (count == 0) {
				propertyRepository.deleteOwnerById(user, propertyId);
			}
		});
	}

	private void deleteDocuments(List<Document> documents, Long propertyDetailId) {
		List<Document> dbDocuments = propertyRepository.getDocumentByPropertyDetails(propertyDetailId);
		dbDocuments.forEach(dbDocument -> {
			int count = documents.stream().filter(document -> document.getId().equals(dbDocument.getId()))
					.collect(Collectors.toList()).size();
			if (count == 0) {
				propertyRepository.deleteDocumentsById(dbDocument);
			}
		});
	}

	private void deleteFloors(List<Floor> floors, Long propertyDetail) {
		List<Floor> dbFloors = propertyRepository.getFloorsByPropertyDetails(propertyDetail);
		for (Floor dbfloor : dbFloors) {
			int count = floors.stream().filter(floor -> floor.getId().equals(dbfloor.getId()))
					.collect(Collectors.toList()).size();
			if (count == 0) {
				propertyRepository.deleteFloorById(dbfloor);
			}
		}
		/*
		 * dbFloors.forEach(Dbfloor -> { int count =
		 * floors.stream().filter(floor -> floor.getId() ==
		 * Dbfloor.getId()).collect(Collectors.toList()) .size(); if (count ==
		 * 0) { propertyRepository.deleteFloorById(Dbfloor); } });
		 */
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
	
	/**
	 * Description: save VacancyRemission
	 * 
	 * @param VacancyRemission
	 * @throws SQLException
	 */
	@Transactional
	public void addVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) throws Exception {
		saveVacancyRemission(vacancyRemissionRequest);

	}

	/**
	 * Description : This method will use for insert VacancyRemission related
	 * data in database
	 * 
	 * @param VacancyRemission
	 */
	private void saveVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) throws Exception {

		if (vacancyRemissionRequest.getVacancyRemission().getIsApproved() == null) {
			vacancyRemissionRequest.getVacancyRemission().setIsApproved(false);
		}

		AuditDetails auditDetails = getAuditDetail(vacancyRemissionRequest.getRequestInfo());
		vacancyRemissionRequest.getVacancyRemission().setAuditDetails(auditDetails);
		Long vacancyRemissionId = vacancyRemissionRepository
				.saveVacancyRemission(vacancyRemissionRequest.getVacancyRemission());

		if (vacancyRemissionRequest.getVacancyRemission().getDocuments() != null) {
			for (Document document : vacancyRemissionRequest.getVacancyRemission().getDocuments()) {
				document.setAuditDetails(auditDetails);
				vacancyRemissionRepository.saveVacancyRemissionDocument(document, vacancyRemissionId);
			}
		}
	}

	/**
	 * Description : This method will use for update VacancyRemission related
	 * data in database
	 * 
	 * @param VacancyRemission
	 */
	@Transactional
	public void updateVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest) throws Exception {

		if (vacancyRemissionRequest.getVacancyRemission().getIsApproved() == null) {
			vacancyRemissionRequest.getVacancyRemission().setIsApproved(false);
		}
		AuditDetails auditDetails = getUpdatedAuditDetails(vacancyRemissionRequest.getRequestInfo(),
				ConstantUtility.VACANCYREMISSION_TABLE_NAME, vacancyRemissionRequest.getVacancyRemission().getId());
		vacancyRemissionRequest.getVacancyRemission().setAuditDetails(auditDetails);

		for (Document document : vacancyRemissionRequest.getVacancyRemission().getDocuments()) {
			AuditDetails docAuditDetails = getUpdatedAuditDetails(vacancyRemissionRequest.getRequestInfo(),
					"egpt_vacancyremission_document", document.getId());
			document.setAuditDetails(docAuditDetails);
		}
		vacancyRemissionRepository.updateVacancyRemission(vacancyRemissionRequest);
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

	/**
	 * This API will persist the Demolition Object in Database
	 * 
	 * @param demolitionRequest
	 * @throws Exception
	 */
	public void saveDemolition(DemolitionRequest demolitionRequest) throws Exception {
		Long demolitionId = demolitionRepository.saveDemolition(demolitionRequest.getDemolition());
		AuditDetails auditDetails = getAuditDetail(demolitionRequest.getRequestInfo());
		demolitionRepository.saveDemolitionDocuments(demolitionRequest.getDemolition(), demolitionId, auditDetails);

	}

	public void updateDemolition(DemolitionRequest demolitionRequest) throws Exception {

		Demolition demolition = demolitionRequest.getDemolition();

		for (Document document : demolition.getDocuments()) {
			AuditDetails auditDetails = getUpdatedAuditDetails(demolitionRequest.getRequestInfo(),
					"egpt_demolition_document", document.getId());
			document.setAuditDetails(auditDetails);
		}

		demolitionRepository.updateDemolition(demolitionRequest);

	}

	public List<Demolition> searchDemolitions(RequestInfo requestInfo,
			DemolitionSearchCriteria demolitionSearchCriteria) throws Exception {
		return demolitionRepository.searchDemolitions(demolitionSearchCriteria);

	}

	/**
	 * Search property based on upic no
	 * 
	 * @param titleTransferRequest
	 * @return
	 * @throws Exception
	 */
	public Property getPropertyUsingUpicNo(DemolitionRequest demolitionRequest) throws Exception {
		RequestInfo requestInfo = demolitionRequest.getRequestInfo();
		Demolition demolition = demolitionRequest.getDemolition();
		Property property = null;
		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setTenantId(demolition.getTenantId());
		propertySearchCriteria.setUpicNumber(demolition.getUpicNumber());

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
	 * This API will copy the Image of the property object to history table and
	 * will update the object based on the demolition object
	 * 
	 * @param demolitionRequest
	 * @throws Exception
	 */
	public void savePropertyTohistoryAndUpdateProperty(DemolitionRequest demolitionRequest) throws Exception {

		Property property = getPropertyUsingUpicNo(demolitionRequest);
		savePropertyHistory(property);
		propertyRepository.updateIsUnderWorkflowbyId(property.getId());
		propertyRepository.updateProperyAfterDemolition(property, demolitionRequest);

	}

	public void updateIsUnderWorkflow(Long propertyId) {
		propertyRepository.updateIsUnderWorkflowbyId(propertyId);
	}

	/**
	 * Description: save TaxExemption
	 * 
	 * @param TaxExemptionRequest
	 * @throws SQLException
	 */
	@Transactional
	public void addTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception {

		saveTaxExemption(taxExemptionRequest);

	}

	/**
	 * Description : This method will use for insert property related data in
	 * database
	 * 
	 * @param properties
	 */
	private void saveTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception {

		AuditDetails auditDetails = getAuditDetail(taxExemptionRequest.getRequestInfo());
		taxExemptionRequest.getTaxExemption().setAuditDetails(auditDetails);
		Long taxExemptionId = taxExemptionRepository.saveTaxExemption(taxExemptionRequest.getTaxExemption());

		if (taxExemptionRequest.getTaxExemption().getDocuments() != null) {
			for (Document document : taxExemptionRequest.getTaxExemption().getDocuments()) {
				document.setAuditDetails(auditDetails);
				taxExemptionRepository.saveTaxExemptionDocument(document, taxExemptionId);
			}
		}

	}

	/**
	 * Description : This method will use for update tax exemption related data
	 * in database
	 * 
	 * @param TaxExemption
	 */
	@Transactional
	public void updateTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception {

		AuditDetails auditDetails = getUpdatedAuditDetails(taxExemptionRequest.getRequestInfo(),
				ConstantUtility.TAXEXEMPTION_TABLE_NAME, taxExemptionRequest.getTaxExemption().getId());
		taxExemptionRequest.getTaxExemption().setAuditDetails(auditDetails);

		if (taxExemptionRequest.getTaxExemption().getDocuments() != null) {
			for (Document document : taxExemptionRequest.getTaxExemption().getDocuments()) {
				AuditDetails docAuditDetails = getUpdatedAuditDetails(taxExemptionRequest.getRequestInfo(),
						ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME, document.getId());
				document.setAuditDetails(docAuditDetails);
			}
			taxExemptionRepository.updateTaxExemption(taxExemptionRequest);
		}
	}

	/**
	 * Description: save property history for Tax Exemption
	 * 
	 * @param properties
	 * @throws SQLException
	 */

	public void addPropertyHistoryForTaxExemption(TaxExemptionRequest taxExemptionRequest, Property property)
			throws Exception {
		savePropertyHistory(property);
	}

	/**
	 * Description : This method will use for update main property (property
	 * details) isexemption and exemption reason in database
	 * 
	 * @param Property
	 */

	public Property updateTaxExemptionProperty(TaxExemptionRequest taxExemptionRequest, Property property)
			throws Exception {
		TaxExemption taxExemption = taxExemptionRequest.getTaxExemption();
		AuditDetails auditDetails = getUpdatedAuditDetails(taxExemptionRequest.getRequestInfo(),
				ConstantUtility.PROPERTY_TABLE_NAME, property.getId());
		property.getPropertyDetail().setIsExempted(true);
		property.getPropertyDetail().setExemptionReason(taxExemption.getExemptionReason());
		property.getPropertyDetail().setAuditDetails(auditDetails);
		updateTaxExemption(taxExemptionRequest);
		property.setAuditDetails(auditDetails);

		taxExemptionRepository.updateTaxExemptionPropertyDetail(property.getPropertyDetail());

		return property;
	}

	/**
	 * Search property based on upic no
	 * 
	 * @param Tax
	 *            Exemption
	 * 
	 * @return
	 * @throws Exception
	 */
	public Property getPropertyUsingUpicNo(TaxExemptionRequest taxExemptionRequest) throws Exception {
		RequestInfo requestInfo = taxExemptionRequest.getRequestInfo();
		TaxExemption taxExemption = taxExemptionRequest.getTaxExemption();
		Property property = null;
		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setTenantId(taxExemption.getTenantId());
		propertySearchCriteria.setUpicNumber(taxExemption.getUpicNumber());

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

}
