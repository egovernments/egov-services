package org.egov.property.services;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.enums.StatusEnum;
import org.egov.models.Address;
import org.egov.models.AppConfigurationResponse;
import org.egov.models.AppConfigurationSearchCriteria;
import org.egov.models.AttributeNotFoundException;
import org.egov.models.AuditDetails;
import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandId;
import org.egov.models.DemandRequest;
import org.egov.models.DemandResponse;
import org.egov.models.Demolition;
import org.egov.models.DemolitionRequest;
import org.egov.models.DemolitionResponse;
import org.egov.models.DemolitionSearchCriteria;
import org.egov.models.DemolitionSearchResponse;
import org.egov.models.Document;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.Floor;
import org.egov.models.FloorSpec;
import org.egov.models.HeadWiseTax;
import org.egov.models.IdGenerationResponse;
import org.egov.models.Owner;
import org.egov.models.Property;
import org.egov.models.PropertyDCB;
import org.egov.models.PropertyDCBRequest;
import org.egov.models.PropertyDCBResponse;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.PropertySearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.SearchTenantResponse;
import org.egov.models.SpecialNotice;
import org.egov.models.SpecialNoticeRequest;
import org.egov.models.SpecialNoticeResponse;
import org.egov.models.TaxCalculation;
import org.egov.models.TaxExemption;
import org.egov.models.TaxExemptionRequest;
import org.egov.models.TaxExemptionResponse;
import org.egov.models.TaxExemptionSearchCriteria;
import org.egov.models.TaxExemptionSearchResponse;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.egov.models.TitleTransferSearchCriteria;
import org.egov.models.TotalTax;
import org.egov.models.TransferFeeCal;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.models.VacancyRemission;
import org.egov.models.VacancyRemissionRequest;
import org.egov.models.VacancyRemissionResponse;
import org.egov.models.VacancyRemissionSearchCriteria;
import org.egov.models.VacancyRemissionSearchResponse;
import org.egov.models.VacantLandDetail;
import org.egov.models.WorkFlowDetails;
import org.egov.models.demand.TaxHeadMaster;
import org.egov.models.demand.TaxHeadMasterResponse;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.IdGenerationException;
import org.egov.property.exception.InvalidFloorException;
import org.egov.property.exception.InvalidUpdatePropertyException;
import org.egov.property.exception.InvalidVacantLandException;
import org.egov.property.exception.PropertyTaxPendingException;
import org.egov.property.exception.PropertyUnderWorkflowException;
import org.egov.property.model.TitleTransferSearchResponse;
import org.egov.property.model.UserCreateRequest;
import org.egov.property.model.UserCreateResponse;
import org.egov.property.repository.CalculatorRepository;
import org.egov.property.repository.DemandRepository;
import org.egov.property.repository.DemandRestRepository;
import org.egov.property.repository.IdgenRepository;
import org.egov.property.repository.PropertyMasterRepository;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.repository.TaxExemptionRepository;
import org.egov.property.repository.TenantRepository;
import org.egov.property.repository.UserRestRepository;
import org.egov.property.repository.VacancyRemissionRepository;
import org.egov.property.repository.WorkFlowRepository;
import org.egov.property.utility.PropertyValidator;
import org.egov.property.utility.TimeStampUtil;
import org.egov.property.utility.UpicNoGeneration;
import org.egov.property.utility.UserUtil;
import org.egov.property.utility.ValidatorUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	PropertyValidator propertyValidator;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	PropertyMasterRepository propertyMasterRepository;

	@Autowired
	PersisterService persisterService;

	@Autowired
	UpicNoGeneration upicNoGeneration;

	@Autowired
	MasterServiceImpl masterServiceImpl;

	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	UserUtil userUtil;

	@Autowired
	TaxExemptionRepository taxExemptionRepository;

	private static final Logger logger = LoggerFactory.getLogger(PropertyServiceImpl.class);

	@Autowired
	DemandRepository demandRepository;

	@Autowired
	WorkFlowRepository workFlowRepository;

	@Autowired
	CalculatorRepository calculatorRepository;

	@Autowired
	DemandRestRepository demandRestRepository;

	@Autowired
	UserRestRepository userRestRepository;

	@Autowired
	IdgenRepository idgenRepository;

	@Autowired
	ValidatorUtil validatorUtil;

	@Autowired
	VacancyRemissionRepository vacancyRemissionRepository;

	@Override
	public PropertyResponse createProperty(PropertyRequest propertyRequest) throws Exception {

		for (Property property : propertyRequest.getProperties()) {

			propertyValidator.validatePropertyMasterData(property, propertyRequest.getRequestInfo());
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());

			String acknowldgementNumber = generateAcknowledegeMentNumber(property.getTenantId(),
					propertyRequest.getRequestInfo());
			property.getPropertyDetail().setApplicationNo(acknowldgementNumber);
			property.getPropertyDetail().setStatus(StatusEnum.WORKFLOW);

			if (property.getChannel().toString().equalsIgnoreCase(propertiesManager.getChannelType())) {

				String upicNumber = upicNoGeneration.generateUpicNo(property, propertyRequest.getRequestInfo());
				property.setUpicNumber(upicNumber);

			} else {
				property.setIsUnderWorkflow(Boolean.TRUE);
			}

			List<User> users = property.getOwners();
			createUsers(users, propertyRequest.getRequestInfo());

			prepareToSave(property, propertyRequest.getRequestInfo());

			kafkaTemplate.send(propertiesManager.getCreateValidatedProperty(), propertyRequest);
		}
		return getResponseInfo(propertyRequest);
	}

	private void prepareToSave(Property property, RequestInfo requestInfo) {

		property.setId(new Random().nextLong());
		property.getAddress().setId(new Random().nextLong());
		property.getPropertyDetail().setId(new Random().nextLong());

		if (property.getIsUnderWorkflow() == null) {
			property.setIsUnderWorkflow(false);
		}
		if (property.getIsAuthorised() == null) {
			property.setIsAuthorised(true);
		}
		if (property.getActive() == null) {
			property.setActive(true);
		}
		if (property.getPropertyDetail().getIsVerified() == null) {
			property.getPropertyDetail().setIsVerified(false);
		}
		if (property.getPropertyDetail().getIsExempted() == null) {
			property.getPropertyDetail().setIsExempted(false);
		}
		if (property.getPropertyDetail().getIsSuperStructure() == null) {
			property.getPropertyDetail().setIsSuperStructure(false);
		}

		AuditDetails auditDetails = getAuditDetail(requestInfo);

		property.setAuditDetails(auditDetails);
		property.getAddress().setAuditDetails(auditDetails);
		property.getPropertyDetail().setAuditDetails(auditDetails);
		property.getBoundary().setId(new Random().nextLong());
		property.getBoundary().setAuditDetails(auditDetails);

		if (property.getVacantLand() != null) {
			property.getVacantLand().setId(new Random().nextLong());
			property.getVacantLand().setAuditDetails(auditDetails);
		}

		if (!property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {

			if (property.getPropertyDetail().getNoOfFloors() == null) {
				if (property.getPropertyDetail().getFloors() != null) {
					int floorCount = property.getPropertyDetail().getFloors().size();
					property.getPropertyDetail().setNoOfFloors((long) floorCount);
				}

			}

			for (Floor floor : property.getPropertyDetail().getFloors()) {
				floor.setId(new Random().nextLong());
				floor.setAuditDetails(auditDetails);
				for (Unit unit : floor.getUnits()) {
					if (unit.getIsAuthorised() == null) {
						unit.setIsAuthorised(true);
					}
					if (unit.getIsStructured() == null) {
						unit.setIsStructured(true);
					}
					unit.setId(new Random().nextLong());
					unit.setAuditDetails(auditDetails);
					if (unit.getUnitType().toString().equalsIgnoreCase(propertiesManager.getUnitType())
							&& unit.getUnits() != null) {

						for (Unit room : unit.getUnits()) {
							room.setId(new Random().nextLong());
							room.setAuditDetails(auditDetails);
						}
					}
				}
			}

			if (property.getPropertyDetail().getDocuments() != null) {
				for (Document document : property.getPropertyDetail().getDocuments()) {
					document.setId(new Random().nextLong());
					document.setAuditDetails(auditDetails);
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
			owner.setId(new Random().nextLong());
			owner.setAuditDetails(auditDetails);
		}

		List<DemandId> demandIdList = new ArrayList<DemandId>();

		if (property.getDemands() != null) {
			for (Demand demand : property.getDemands()) {
				DemandId id = new DemandId();
				id.setId(demand.getId());
				demandIdList.add(id);
			}
		}

		Gson gson = new Gson();
		PGobject jsonObject = new PGobject();
		jsonObject.setType("jsonb");
		try {
			jsonObject.setValue(gson.toJson(demandIdList));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		property.setDemandsJsonObject(jsonObject);

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

	private PropertyResponse getResponseInfo(PropertyRequest propertyRequest) {
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(), true);
		PropertyResponse propertyResponse = new PropertyResponse();
		propertyResponse.setResponseInfo(responseInfo);
		propertyResponse.setProperties(propertyRequest.getProperties());
		return propertyResponse;
	}

	/**
	 * This API will validate users and add the username and password if its
	 * null
	 * 
	 * @param users
	 * @param requestInfo
	 * @throws Exception
	 */
	private void createUsers(List<User> users, RequestInfo requestInfo) throws Exception {
		for (User user : users) {
			if (user.getUserName() == null) {
				user.setUserName(getUserName(user));
			}
			if (user.getPassword() == null) {
				user.setPassword(propertiesManager.getDefaultUserPassword());
			}
			if (user.getActive() == null) {
				user.setActive(true);
			}
			UserCreateRequest userCreateRequest = new UserCreateRequest();

			userCreateRequest.setRequestInfo(requestInfo);
			userCreateRequest.setUser(user);

			UserCreateResponse userCreateResponse = userRestRepository.createUser(userCreateRequest);
			user.setId(userCreateResponse.getUser().get(0).getId());

		}
	}

	/**
	 * This API will add the random username if the user name is null
	 * 
	 * @param user
	 * @return {@link String} UserName
	 */
	private String getUserName(User user) {

		String userName = user.getUserName();

		if (userName == null) {
			userName = RandomStringUtils.randomAlphanumeric(32);
		}
		return userName;

	}

	@Override
	public PropertyResponse updateProperty(PropertyRequest propertyRequest) throws Exception {
		for (Property property : propertyRequest.getProperties()) {
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());
			if (property.getPropertyDetail().getPropertyType().equalsIgnoreCase(propertiesManager.getVacantLand())) {
				if (property.getVacantLand() == null) {
					throw new InvalidVacantLandException(propertiesManager.getInvalidPropertyVacantland(),
							propertyRequest.getRequestInfo());
				}

				if (property.getPropertyDetail().getFloors() != null) {

					throw new InvalidFloorException(propertiesManager.getInvalidPropertyFloor(),
							propertyRequest.getRequestInfo());
				}
			}

			propertyValidator.validateWorkflowDeatails(property, propertyRequest.getRequestInfo());
			String action = property.getPropertyDetail().getWorkFlowDetails().getAction();
			if (action.equalsIgnoreCase(propertiesManager.getApproveProperty())
					&& (property.getUpicNumber() == null || property.getUpicNumber().isEmpty())) {
				String upicNumber = upicNoGeneration.generateUpicNo(property, propertyRequest.getRequestInfo());
				property.setUpicNumber(upicNumber);
			}
			property.getPropertyDetail().setStatus(StatusEnum.WORKFLOW);
			WorkFlowDetails workFlowDetails = propertyRequest.getProperties().get(0).getPropertyDetail()
					.getWorkFlowDetails();

			for (User user : property.getOwners()) {
				if (user.getActive() == null) {
					user.setActive(true);
				}
				if (user.getId() == null) {
					user = userUtil.createNewUser(user, propertyRequest.getRequestInfo());
				} else {
					userUtil.updateUser(user, propertyRequest.getRequestInfo());
				}
			}

			PropertyRequest updatedPropertyRequest = new PropertyRequest(propertyRequest.getRequestInfo(),
					Collections.singletonList(property));

			kafkaTemplate.send(propertiesManager.getUpdateValidatedUser(), updatedPropertyRequest);

			if (workFlowDetails.getAction().equalsIgnoreCase(propertiesManager.getCancelAction())) {
				propertyRepository.updateIsUnderWorkflowbyId(property.getId());
			}
		}

		return getResponseInfo(propertyRequest);
	}

	/**
	 * Description: Generating acknowledge number for property
	 * 
	 * @param property
	 * @param requestInfo
	 * @return
	 */
	public String generateAcknowledegeMentNumber(String tenantId, RequestInfo requestInfo) {

		// generating acknowledgement number for all properties
		String acknowledegeMentNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, propertiesManager.getIdFormat(),
				propertiesManager.getIdName());
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new IdGenerationException(error.getMessage(), error.getDescription(), requestInfo);
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase(propertiesManager.getSuccess())) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					acknowledegeMentNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return acknowledegeMentNumber;
	}

	/**
	 * <p>
	 * This method will search the documents in Database(Postgres) with the
	 * given parameters
	 * </p>
	 * 
	 * @author Prasad
	 * @param requestInfo
	 * @param propertySearchCriteria
	 * @return Property Object if search is successful or Error Object if search
	 *         will fail
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */

	@SuppressWarnings("unchecked")
	public PropertyResponse searchProperty(RequestInfo requestInfo, PropertySearchCriteria propertySearchCriteria)
			throws Exception {

		List<Property> updatedPropety = null;

		Map<String, Object> map = propertyRepository.searchProperty(requestInfo, propertySearchCriteria.getTenantId(),
				propertySearchCriteria.getActive(), propertySearchCriteria.getUpicNumber(),
				propertySearchCriteria.getPageSize(), propertySearchCriteria.getPageNumber(),
				propertySearchCriteria.getSort(), propertySearchCriteria.getOldUpicNo(),
				propertySearchCriteria.getMobileNumber(), propertySearchCriteria.getAadhaarNumber(),
				propertySearchCriteria.getHouseNoBldgApt(), propertySearchCriteria.getRevenueZone(),
				propertySearchCriteria.getRevenueWard(), propertySearchCriteria.getLocality(),
				propertySearchCriteria.getOwnerName(), propertySearchCriteria.getDemandFrom(),
				propertySearchCriteria.getDemandTo(), propertySearchCriteria.getPropertyId(),
				propertySearchCriteria.getApplicationNo(), propertySearchCriteria.getUsage(),
				propertySearchCriteria.getAdminBoundary(), propertySearchCriteria.getOldestUpicNo());

		List<Property> property = (List<Property>) map.get("properties");
		List<User> users = (List<User>) map.get("users");
		if (users != null && users.size() > 0) {
			updatedPropety = addAllPropertyDetails(property, requestInfo, users);
		} else {
			if (property.size() > 0) {
				if (property.get(0).getOwners().size() > 0) {
					updatedPropety = addAllPropertyDetails(property, requestInfo, users);
				}
			} else
				updatedPropety = new ArrayList<Property>();
		}
		PropertyResponse propertyResponse = new PropertyResponse();
		propertyResponse.setProperties(updatedPropety);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		propertyResponse.setResponseInfo(responseInfo);

		return propertyResponse;

	}

	/**
	 * <p>
	 * This method will add the property details to the given list of property
	 * objects ,such as floors,owners etc
	 * </p>
	 * 
	 * @author Prasad
	 * @param properties
	 * @return List of property Object's
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws ParseException
	 * @see ArrayList
	 * 
	 */

	private List<Property> addAllPropertyDetails(List<Property> properties, RequestInfo requestInfo, List<User> users)
			throws JsonParseException, JsonMappingException, IOException, Exception {

		List<Property> updatedPropertyDetails = new ArrayList<>();

		for (Property property : properties) {

			Long propertyId = property.getId();

			Address address = propertyRepository.getAddressByProperty(propertyId);
			property.setAddress(address);

			List<User> ownerInfos = new ArrayList<>();

			if (!(property.getOwners().size() > 0)) {

				List<User> propertyUsers = propertyRepository.getPropertyUserByProperty(propertyId);
				List<Integer> userIds = new ArrayList<>();

				for (User propertyUser : propertyUsers) {

					userIds.add(Integer.valueOf(propertyUser.getOwner().toString()));
				}

				List<User> userOfProperty = getUserObjectForUserIds(userIds, users);

				// get owner info for property

				for (User propertyUser : userOfProperty) {
					ownerInfos.add(propertyUser);
				}

				property.setOwners(ownerInfos);
			}
			PropertyDetail propertyDetail = propertyRepository.getPropertyDetailsByProperty(propertyId);

			property.setPropertyDetail(propertyDetail);

			VacantLandDetail vacantLandDetail = propertyRepository.getVacantLandByProperty(propertyId);
			if (vacantLandDetail != null) {
				if (vacantLandDetail.getLayoutPermissionDate() != null) {
					vacantLandDetail.setLayoutPermissionDate(
							TimeStampUtil.getDateFormat(vacantLandDetail.getLayoutPermissionDate()));
				}
			}
			property.setVacantLand(vacantLandDetail);

			PropertyLocation propertyLocation = propertyRepository.getPropertyLocationByproperty(propertyId);
			property.setBoundary(propertyLocation);

			Long propertyDetailId = property.getPropertyDetail().getId();

			List<Floor> floors = propertyRepository.getFloorsByPropertyDetails(propertyDetailId);

			property.getPropertyDetail().setFloors(floors);

			for (Floor floor : floors) {
				List<Unit> units = propertyRepository.getUnitsByFloor(floor.getId());
				for (Unit unit : units) {
					if (unit.getBpaDate() != null) {
						unit.setBpaNo(TimeStampUtil.getDateFormat(unit.getBpaDate()));
					}
					if (unit.getOccupancyDate() != null) {
						unit.setOccupancyDate(TimeStampUtil.getDateFormat(unit.getOccupancyDate()));
					}
					if (unit.getConstCompletionDate() != null) {
						unit.setConstCompletionDate(TimeStampUtil.getDateFormat(unit.getConstCompletionDate()));
					}
					if (unit.getConstructionStartDate() != null) {
						unit.setConstructionStartDate(TimeStampUtil.getDateFormat(unit.getConstructionStartDate()));
					}
				}
				floor.setUnits(units);

			}

			int i = 0;

			for (Floor floor : floors) {

				List<Unit> flats = new ArrayList<>();
				List<Unit> rooms = new ArrayList<>();

				List<Unit> units = floors.get(i).getUnits();

				for (Unit unit : units) {
					if (unit.getParentId() != 0)
						rooms.add(unit);
					flats.add(unit);

				}

				if (flats.size() > 0) {
					for (Unit flat : flats) {
						List<Unit> newUnits = new ArrayList<>();
						for (Unit room : rooms) {

							if (room.getParentId() == flat.getId()) {
								newUnits.add(room);

							}
							if (newUnits.size() > 0)
								flat.setUnits(newUnits);

						}
						floor.setUnits(flats);
					}

					i++;
				}
			}

			List<Document> documents = propertyRepository.getDocumentByPropertyDetails(propertyDetailId);

			property.getPropertyDetail().setDocuments(documents);

			property.setAuditDetails(propertyRepository.getAuditDetailsForProperty(propertyId));

			updatedPropertyDetails.add(property);

		}
		return updatedPropertyDetails;

	}

	/**
	 * <p>
	 * This method will give you the user Objects which has the given userIds
	 * <p>
	 * 
	 * @author Prasad
	 * @param userIds
	 * @param users
	 * @return List Of user Object
	 */

	private List<User> getUserObjectForUserIds(List<Integer> userIds, List<User> users) {

		List<User> userList = new ArrayList<User>();
		if (users != null) {
			for (User user : users) {
				Long userId = user.getId();
				if (userIds.contains(userId.intValue())) {
					userList.add(user);
				}
			}

		}
		return userList;
	}

	/**
	 * calculating transfer fee for title transfer create and update
	 * 
	 * @param titleTransferRequest
	 * @param propertyTax
	 * @return double
	 * @throws Exception
	 */
	public Double titleTransferFee(TitleTransferRequest titleTransferRequest, Double propertyTax) throws Exception {

		if (propertyTax == 0.0) {
			throw new PropertyUnderWorkflowException(propertiesManager.getInvalidTaxValueTransferFee(),
					titleTransferRequest.getRequestInfo());
		}

		AppConfigurationSearchCriteria appConfigurationSearchCriteria = new AppConfigurationSearchCriteria();
		appConfigurationSearchCriteria.setTenantId(titleTransferRequest.getTitleTransfer().getTenantId());
		appConfigurationSearchCriteria.setKeyName(propertiesManager.getTitleTransferFeeFactorKeyName());

		AppConfigurationResponse appConfiguration = masterServiceImpl
				.getAppConfiguration(titleTransferRequest.getRequestInfo(), appConfigurationSearchCriteria);
		String feeFactor = null;
		if (appConfiguration != null) {
			if (appConfiguration.getAppConfigurations().size() > 0) {
				feeFactor = appConfiguration.getAppConfigurations().get(0).getValues().get(0);
			} else {
				throw new PropertyUnderWorkflowException(propertiesManager.getFeeFactorFromAppConfiguration(),
						titleTransferRequest.getRequestInfo());
			}
		} else {
			throw new PropertyUnderWorkflowException(propertiesManager.getFeeFactorFromAppConfiguration(),
					titleTransferRequest.getRequestInfo());
		}

		Double tax = propertyTax;
		Double transferFee = null;
		TransferFeeCal transferFeeCal = new TransferFeeCal();
		String currentDate = new SimpleDateFormat(propertiesManager.getDate()).format(new Date());
		if (feeFactor != null) {
			if (!(feeFactor.equalsIgnoreCase(propertiesManager.getPropertyTax())
					|| feeFactor.equalsIgnoreCase(propertiesManager.getDocumentValue()))) {
				throw new PropertyUnderWorkflowException(propertiesManager.getInvalidTitleTransferFeeFactor(),
						titleTransferRequest.getRequestInfo());
			}
			// only calculting tax for propertytax and documentvalue
			transferFeeCal.setFeeFactor(feeFactor);
			transferFeeCal.setTenantId(titleTransferRequest.getTitleTransfer().getTenantId());
			transferFeeCal.setValidDate(currentDate);
			if (feeFactor.equalsIgnoreCase(propertiesManager.getPropertyTax())) {
				if (tax != null) {
					transferFeeCal.setValidValue(tax);
				} else {
					throw new PropertyUnderWorkflowException(propertiesManager.getInvalidPropertyTax(),
							titleTransferRequest.getRequestInfo());
				}

			}
			if (feeFactor.equalsIgnoreCase(propertiesManager.getDocumentValue())) {

				transferFeeCal.setValidValue(titleTransferRequest.getTitleTransfer().getDepartmentGuidelineValue());
			}
		}

		transferFee = calculatorRepository.getFeeFactorRates(transferFeeCal, titleTransferRequest.getRequestInfo());

		return transferFee;
	}

	/**
	 * getting property tax from demands in update titletransfer case
	 * 
	 * @param titleTransferRequest
	 * @return double
	 * @throws Exception
	 */
	public Double getPropertyTaxForUpdateWorkflow(TitleTransferRequest titleTransferRequest) throws Exception {
		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(titleTransferRequest.getRequestInfo());
		DemandResponse demandResponse = demandRepository.getDemands(titleTransfer.getUpicNo(),
				titleTransfer.getTenantId(), requestInfoWrapper);
		Double totalPropertyTax = 0.0;
		if (demandResponse != null) {
			for (Demand demand : demandResponse.getDemands()) {
				Double totalTax = 0.0;
				Double collectedAmount = 0.0;
				for (DemandDetail demandDetail : demand.getDemandDetails()) {
					if (!demandDetail.getTaxHeadMasterCode().contains(propertiesManager.getTitleTransferPenalty())) {
						totalTax += demandDetail.getTaxAmount().doubleValue();
						collectedAmount += demandDetail.getCollectionAmount().doubleValue();
						totalPropertyTax += totalTax;
					}
				}
				if (totalTax > collectedAmount) {
					throw new PropertyTaxPendingException(propertiesManager.getInvalidTaxMessage(),
							titleTransferRequest.getRequestInfo());
				}
			}
		}
		return totalPropertyTax;
	}

	@Override
	public TitleTransferResponse createTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {
		Boolean isPropertyUnderWorkflow = false;
		isPropertyUnderWorkflow = propertyRepository
				.isPropertyUnderWorkflow(titleTransferRequest.getTitleTransfer().getUpicNo());
		TitleTransferResponse titleTransferResponse = null;

		if (isPropertyUnderWorkflow) {
			throw new PropertyUnderWorkflowException(propertiesManager.getInvalidPropertyStatus(),
					titleTransferRequest.getRequestInfo());

		} else {
			Double propertyTax = validateTitleTransfer(titleTransferRequest);
			Double transferFee = titleTransferFee(titleTransferRequest, propertyTax);
			titleTransferRequest.getTitleTransfer().setTitleTrasferFee(transferFee);

			String acknowldgeMentNumber = generateAcknowledegeMentNumber(
					titleTransferRequest.getTitleTransfer().getTenantId(), titleTransferRequest.getRequestInfo());

			titleTransferRequest.getTitleTransfer().setApplicationNo(acknowldgeMentNumber);

			kafkaTemplate.send(propertiesManager.getCreateTitleTransferValidated(), titleTransferRequest);
			propertyRepository.updateIsPropertyUnderWorkflow(titleTransferRequest.getTitleTransfer().getUpicNo());

			titleTransferResponse = new TitleTransferResponse();
			titleTransferResponse.setResponseInfo(
					responseInfoFactory.createResponseInfoFromRequestInfo(titleTransferRequest.getRequestInfo(), true));
			titleTransferResponse.setTitleTransfer(titleTransferRequest.getTitleTransfer());

		}
		return titleTransferResponse;
	}

	@Override
	public TitleTransferResponse updateTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {

		validateTitleTransferWorkflowDeatails(titleTransferRequest, titleTransferRequest.getRequestInfo());
		Double propertyTax = getPropertyTaxForUpdateWorkflow(titleTransferRequest);
		Double transferFee = titleTransferFee(titleTransferRequest, propertyTax);
		titleTransferRequest.getTitleTransfer().setTitleTrasferFee(transferFee);
		kafkaTemplate.send(propertiesManager.getUpdateTitleTransferValidated(), titleTransferRequest);
		TitleTransferResponse titleTransferResponse = null;
		titleTransferResponse = new TitleTransferResponse();
		titleTransferResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(titleTransferRequest.getRequestInfo(), true));
		titleTransferResponse.setTitleTransfer(titleTransferRequest.getTitleTransfer());

		return titleTransferResponse;
	}

	/**
	 * This method validate title transfer worflow details
	 * 
	 * @param titleTransferRequest
	 * @param requestInfo
	 * @throws AttributeNotFoundException
	 */
	public void validateTitleTransferWorkflowDeatails(TitleTransferRequest titleTransferRequest,
			RequestInfo requestInfo) throws AttributeNotFoundException {

		WorkFlowDetails workflowDetails = titleTransferRequest.getTitleTransfer().getWorkFlowDetails();
		if (workflowDetails.getAction() == null) {
			throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowActionNotfound(), requestInfo);

		} else if (workflowDetails.getAssignee() == null) {
			throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowAssigneeNotfound(), requestInfo);

		} else if (workflowDetails.getDepartment() == null) {
			throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowDepartmentNotfound(), requestInfo);

		} else if (workflowDetails.getDesignation() == null) {
			throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowDesignationNotfound(), requestInfo);

		} else if (workflowDetails.getStatus() == null) {
			throw new InvalidUpdatePropertyException(propertiesManager.getWorkflowStatusNotfound(), requestInfo);

		}
	}

	/**
	 * This method will validate title transfer object(Currently not yet have
	 * any validations)
	 * 
	 * @param titleTransferRequest
	 * @return boolean
	 */
	private Double validateTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {
		Boolean isExist = false;
		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(titleTransferRequest.getRequestInfo());
		DemandResponse demandResponse = demandRepository.getDemands(titleTransfer.getUpicNo(),
				titleTransfer.getTenantId(), requestInfoWrapper);
		Double totalPropertyTax = 0.0;
		if (demandResponse != null) {
			for (Demand demand : demandResponse.getDemands()) {
				Double totalTax = 0.0;
				Double collectedAmount = 0.0;
				for (DemandDetail demandDetail : demand.getDemandDetails()) {
					if (!demandDetail.getTaxHeadMasterCode().contains(propertiesManager.getTitleTransferPenalty())) {
						totalTax += demandDetail.getTaxAmount().doubleValue();
						collectedAmount += demandDetail.getCollectionAmount().doubleValue();
						totalPropertyTax += totalTax;
					}
				}
				if (totalTax > collectedAmount) {
					throw new PropertyTaxPendingException(propertiesManager.getInvalidTaxMessage(),
							titleTransferRequest.getRequestInfo());
				}
			}
		}
		isExist = propertyMasterRepository
				.checkUniqueCodeForMutation(titleTransferRequest.getTitleTransfer().getTransferReason());
		if (isExist) {
			return totalPropertyTax;
		} else {
			throw new PropertyUnderWorkflowException(propertiesManager.getInvalidTitleTransfer(),
					titleTransferRequest.getRequestInfo());
		}
	}

	@Override
	public SpecialNoticeResponse generateSpecialNotice(SpecialNoticeRequest specialNoticeRequest) throws Exception {

		String noticeNumber = "";
		if (!checkWhetherUpicNoExixts(specialNoticeRequest.getUpicNo())) {
			String ulbCode = getUlbCodeForTenant(specialNoticeRequest.getTenantId(),
					specialNoticeRequest.getRequestInfo());
			String noticeSequence = generateNoticeNumber(specialNoticeRequest.getRequestInfo(),
					specialNoticeRequest.getTenantId());
			noticeNumber = ulbCode + noticeSequence;
		}

		else {
			noticeNumber = getNoticeNumberForUpic(specialNoticeRequest.getUpicNo());
		}
		SpecialNotice notice = new SpecialNotice();

		SpecialNoticeResponse specialNoticeResponse = new SpecialNoticeResponse();
		String upicNo = specialNoticeRequest.getUpicNo();
		String tenantId = specialNoticeRequest.getTenantId();

		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setTenantId(tenantId);
		propertySearchCriteria.setUpicNumber(upicNo);

		PropertyResponse propertyRespone = searchProperty(specialNoticeRequest.getRequestInfo(),
				propertySearchCriteria);
		Property property = propertyRespone.getProperties().get(0);
		notice.setUpicNo(specialNoticeRequest.getUpicNo());
		notice.setTenantId(specialNoticeRequest.getTenantId());
		notice.setAddress(property.getAddress());
		notice.setOwners(property.getOwners());
		List<FloorSpec> floorSpecs = new ArrayList<FloorSpec>();
		List<Floor> floors = property.getPropertyDetail().getFloors();

		Double grossAmt = getGrossAmount(floors);

		for (Floor floor : floors) {
			for (Unit unit : floor.getUnits()) {
				if (unit.getUnits() != null) {
					for (Unit room : unit.getUnits()) {
						getFloorSpec(floor, room, floorSpecs, grossAmt);
					}
				} else {
					getFloorSpec(floor, unit, floorSpecs, grossAmt);

				}
			}

		}

		notice.setFloors(floorSpecs);
		notice.setApplicationNo(property.getPropertyDetail().getApplicationNo());
		notice.setApplicationDate(property.getAssessmentDate());
		notice.setNoticeNumber(noticeNumber);
		notice.setNoticeDate(new SimpleDateFormat(propertiesManager.getSimpleDateFormat()).format(new Date()));

		TaxPeriodResponse taxPeriodResponse = calculatorRepository.getTaxPeriods(tenantId, notice.getNoticeDate(),
				specialNoticeRequest.getRequestInfo());

		logger.info("PropertyServiceImpl taxPeriodResponse : " + taxPeriodResponse);
		ObjectMapper mapper = new ObjectMapper();

		List<TaxCalculation> calculationList = new ArrayList<>();
		TypeReference<List<TaxCalculation>> typeReference = new TypeReference<List<TaxCalculation>>() {
		};
		calculationList = mapper.readValue(
				propertyRespone.getProperties().get(0).getPropertyDetail().getTaxCalculations(), typeReference);

		SimpleDateFormat sdf = new SimpleDateFormat(propertiesManager.getDate());

		SimpleDateFormat sdff = new SimpleDateFormat(propertiesManager.getDateAndTimeFormat());
		List<TaxCalculation> currentYearTax = new ArrayList<>();
		Double totalTax = 0.0d;

		for (TaxPeriod taxPeriod : taxPeriodResponse.getTaxPeriods()) {

			for (TaxCalculation taxCalculation : calculationList) {
				if ((sdff.parse(taxPeriod.getFromDate()).getTime() == sdf.parse(taxCalculation.getFromDate()).getTime())
						&& (sdff.parse(taxPeriod.getToDate()).getTime() == sdf.parse(taxCalculation.getToDate())
								.getTime())) {

					currentYearTax.add(taxCalculation);

				}
			}
		}
		List<HeadWiseTax> headWiseTaxes = new ArrayList<>();

		for (TaxCalculation taxCalc : currentYearTax) {
			Integer totalDays = taxCalc.getPropertyTaxes().getHeadWiseTaxes().get(0).getTaxDays();
			totalTax = totalTax + taxCalc.getPropertyTaxes().getTotalTax();
			Map<String, Double> headWiseGroupBy = taxCalc.getPropertyTaxes().getHeadWiseTaxes().stream().collect(
					Collectors.groupingBy(HeadWiseTax::getTaxName, Collectors.summingDouble(HeadWiseTax::getTaxValue)));
			headWiseGroupBy.forEach((headwise, totalHeadWiseTax) -> {
				HeadWiseTax headWise = new HeadWiseTax();
				headWise.setTaxName(headwise);
				headWise.setTaxValue(totalHeadWiseTax);
				headWise.setTaxDays(totalDays);
				headWiseTaxes.add(headWise);
			});
		}
		TotalTax totalTaxes = new TotalTax();
		totalTaxes.setHeadWiseTaxes(headWiseTaxes);
		totalTaxes.setTotalTax(totalTax);
		notice.setTaxDetails(totalTaxes);

		addNotice(notice, totalTax);

		specialNoticeResponse.setNotice(notice);

		WorkFlowDetails workFlowDetails = specialNoticeRequest.getWorkFlowDetails();
		if (workFlowDetails != null) {
			workFlowRepository.updateWorkFlowDetails(workFlowDetails, specialNoticeRequest.getRequestInfo(), tenantId,
					property.getPropertyDetail().getStateId());
		}
		specialNoticeResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(specialNoticeRequest.getRequestInfo(), true));

		return specialNoticeResponse;

	}

	private String getNoticeNumberForUpic(String upicNo) {

		return propertyRepository.getNoticeNumberForupic(upicNo);

	}

	/**
	 * This method will check whether the document exists with given upic no
	 * 
	 * @param upicNo
	 * @return TRUE / False if the document exists/ does not exists
	 */
	private boolean checkWhetherUpicNoExixts(String upicNo) {

		return propertyRepository.checkUpicnoExixts(upicNo);

	}

	/**
	 * This will calculate the gross Amount of the total units of floors
	 * 
	 * @param floors
	 * @return Double Amount
	 */
	private Double getGrossAmount(List<Floor> floors) {
		Double amount = 0.0d;
		for (Floor floor : floors) {
			for (Unit unit : floor.getUnits()) {
				if (unit.getUnits() != null) {
					for (Unit room : unit.getUnits()) {
						if (room.getArv() != null)
							amount = amount + room.getArv();
					}
				} else {
					if (unit.getArv() != null)
						amount = amount + unit.getArv();

				}
			}

		}

		return amount;
	}

	/**
	 * This will add the notice object in the database
	 * 
	 * @param notice
	 * @param totalTax
	 * @throws Exception
	 */
	private void addNotice(SpecialNotice notice, Double totalTax) throws Exception {

		propertyRepository.saveNotice(notice, totalTax);

	}

	/**
	 * This method will return the Ulb cod
	 * 
	 * @param tenantId
	 * @param requestInfo
	 * @return {@link String} ULB CODE
	 * @throws Exception
	 */
	private String getUlbCodeForTenant(String tenantId, RequestInfo requestInfo) throws Exception {

		String ulbCode = "";
		SearchTenantResponse searchTenantResponse = null;
		String response = null;
		response = tenantRepository.getTenantRepository(tenantId, requestInfo);
		if (response != null) {
			ObjectMapper mapper = new ObjectMapper();
			searchTenantResponse = mapper.readValue(response, SearchTenantResponse.class);
		}

		ulbCode = searchTenantResponse.getTenant().get(0).getCode();
		logger.info("PropertyServiceImpl ulbocode = " + ulbCode);

		return ulbCode;

	}

	/**
	 * This Api will return the Notice number
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @return {@link String} NoticeNumber
	 * @throws Exception
	 */
	private String generateNoticeNumber(RequestInfo requestInfo, String tenantId) throws Exception {

		String noticeNumber = "";
		String response = null;

		response = idgenRepository.getIdGeneration(tenantId, requestInfo, propertiesManager.getUlbFormat(),
				propertiesManager.getUlbName());

		logger.info("PropertyServiceImpl After the calling  idgenearion  response = " + response);
		if (response != null && response.length() > 0) {

			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);
			noticeNumber = idResponse.getIdResponses().get(0).getId();
			logger.info("PropertyServiceImpl After the calling  idgenearion  noticeNumber = " + noticeNumber);

		}

		return noticeNumber;
	}

	/**
	 * This method will get the floor test
	 * 
	 * @param floor
	 * @param unit
	 * @param floorSpecs
	 * @param amount
	 */
	private void getFloorSpec(Floor floor, Unit unit, List<FloorSpec> floorSpecs, Double amount) {
		FloorSpec floorSpecUnit = new FloorSpec();
		floorSpecUnit.setUnitDetails(getString(unit.getId()));
		floorSpecUnit.setUsage(getString(unit.getUsage()));
		floorSpecUnit.setConstruction(getString(unit.getStructure()));
		floorSpecUnit.setAssessableArea(getString(unit.getAssessableArea()));
		floorSpecUnit.setRv(getString(unit.getArv()));
		floorSpecUnit.setAlv(getString(amount.toString().trim()));
		floorSpecUnit.setFloorNo(getString(floor.getFloorNo()));
		floorSpecs.add(floorSpecUnit);
	}

	/**
	 * * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
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
		Property property = persisterService.getPropertyUsingUpicNo(titleTransferRequest);
		persisterService.addPropertyHistory(titleTransferRequest, property);
		Property updatedProperty = persisterService.updateTitleTransferProperty(titleTransferRequest, property);
		PropertyRequest propertyRequest = new PropertyRequest();
		propertyRequest.setRequestInfo(titleTransferRequest.getRequestInfo());
		List<Property> properties = new ArrayList<Property>();
		properties.add(updatedProperty);
		propertyRequest.setProperties(properties);
		return propertyRequest;
	}

	/**
	 * API prepares DCB data for Add/Edit DCB feature
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param upicNumber
	 * @throws Exception
	 */
	public DemandResponse getDemandsForProperty(RequestInfoWrapper requestInfoWrapper, String tenantId,
			String upicNumber) throws Exception {
		DemandResponse demandResponse = null;
		int noOfPeriods = 0;
		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setTenantId(tenantId);
		propertySearchCriteria.setUpicNumber(upicNumber);

		PropertyResponse propertyResponse = searchProperty(requestInfoWrapper.getRequestInfo(), propertySearchCriteria);

		if (propertyResponse != null) {
			Property property = propertyResponse.getProperties().get(0);

			SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			dbDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date occupancyDate = dbDateFormat.parse(property.getOccupancyDate());
			String occupancyDateStr = new SimpleDateFormat(propertiesManager.getSimpleDateFormat())
					.format(occupancyDate);

			// Fetch TaxPeriods
			TaxPeriodResponse taxPeriodResponse = getTaxPeriodsForOccupancyDate(requestInfoWrapper.getRequestInfo(),
					tenantId, occupancyDateStr, dbDateFormat);
			logger.info("PropertyServiceImpl getDemandsForProperty() taxPeriodResponse : " + taxPeriodResponse);

			// Fetch TaxHeads
			TaxHeadMasterResponse taxHeadResponse = getTaxHeadMasters(requestInfoWrapper.getRequestInfo(), tenantId,
					occupancyDate);
			logger.info("PropertyServiceImpl getDemandsForProperty() taxHeadResponse : " + taxHeadResponse);
			// Fetch Demands for property
			DemandResponse demandRespForSavedDemands = demandRepository.getDemands(upicNumber, tenantId,
					requestInfoWrapper);
			if (!taxPeriodResponse.getTaxPeriods().isEmpty())
				noOfPeriods = taxPeriodResponse.getTaxPeriods().size();

			List<Demand> newDemandList = new ArrayList<>();
			List<Demand> finalDemandList = new ArrayList<>();
			// If demands are present, load them in the response, else prepare
			// new demands and set in response
			logger.info("----------- demands size:" + demandRespForSavedDemands.getDemands().size() + " no of periods"
					+ noOfPeriods);
			Date taxPeriodFromDate = null;
			if (!demandRespForSavedDemands.getDemands().isEmpty()) {

				// If number of demands and tax periods are same, set the
				// demands to the list,
				// else prepare demands for the remaining taxperiods and add the
				// existing demands along with the new demands to the response
				if (demandRespForSavedDemands.getDemands().size() < noOfPeriods) {
					for (TaxPeriod taxPeriod : taxPeriodResponse.getTaxPeriods()) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

						sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

						taxPeriodFromDate = sdf.parse(taxPeriod.getFromDate());
						long time = taxPeriodFromDate.getTime();

						List<Demand> matchedDemands = demandRespForSavedDemands.getDemands().stream()
								.filter(demand -> demand.getTaxPeriodFrom() == time).collect(Collectors.toList());
						if (matchedDemands == null) {
							newDemandList = prepareDemands(tenantId, upicNumber, property, taxHeadResponse, taxPeriod);
							finalDemandList.addAll(newDemandList);
						} else {
							if (matchedDemands.size() > 0) {
								finalDemandList.add(matchedDemands.get(0));
							} else {
								newDemandList = prepareDemands(tenantId, upicNumber, property, taxHeadResponse,
										taxPeriod);
								finalDemandList.addAll(newDemandList);
							}
						}
					}
					demandResponse = new DemandResponse();
					demandResponse.setResponseInfo(responseInfoFactory
							.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true));
					demandResponse.setDemands(finalDemandList);
				} else {
					// set the existing demands to the response
					demandResponse = demandRespForSavedDemands;
				}

			} else {
				demandResponse = new DemandResponse();
				demandResponse.setResponseInfo(responseInfoFactory
						.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true));
				for (TaxPeriod taxPeriod : taxPeriodResponse.getTaxPeriods()) {
					newDemandList = prepareDemands(tenantId, upicNumber, property, taxHeadResponse, taxPeriod);
					finalDemandList.addAll(newDemandList);
				}
				demandResponse.setDemands(finalDemandList);
			}
			if (!demandResponse.getDemands().isEmpty()) {
				DemandDetail newDemandDetail;
				List<DemandDetail> demandDetailList;
				List<String> demandDetailCodes;
				List<String> taxHeadCodes = taxHeadResponse.getTaxHeadMasters().stream().map(TaxHeadMaster::getCode)
						.collect(Collectors.toList());
				for (Demand demand : demandResponse.getDemands()) {
					demandDetailList = demand.getDemandDetails();
					demandDetailCodes = demandDetailList.stream().map(DemandDetail::getTaxHeadMasterCode)
							.collect(Collectors.toList());
					logger.info("-------- demanddetail codes --------- " + demandDetailCodes);
					logger.info("-------- tax heads --------- " + taxHeadCodes);
					for (String taxHead : taxHeadCodes) {
						if (!"ADVANCE".equalsIgnoreCase(taxHead) && !demandDetailCodes.contains(taxHead)) {
							newDemandDetail = new DemandDetail();
							newDemandDetail.setTenantId(tenantId);
							newDemandDetail.setTaxHeadMasterCode(taxHead);
							demandDetailList.add(newDemandDetail);
						}
					}
					Collections.sort(demandDetailList, Comparator.comparing(DemandDetail::getTaxHeadMasterCode));
					logger.info("----------- final demanddetails list ------------ " + demandDetailList);
					demand.setDemandDetails(demandDetailList);
				}
			}
		}
		return demandResponse;
	}

	/**
	 * Fetches the TaxPeriods from pt-calculator service for the given occupancy
	 * date, till the current date
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param occupancyDateStr
	 * @return TaxPeriodResponse
	 */
	private TaxPeriodResponse getTaxPeriodsForOccupancyDate(RequestInfo requestInfo, String tenantId,
			String occupancyDateStr, SimpleDateFormat dateFormat) {
		return calculatorRepository.getTaxPeriodsForOccupancyDateRepository(requestInfo, tenantId, occupancyDateStr,
				dateFormat);
	}

	/**
	 * API fetches TaxHeadMasters from the Billing Service for the occupancy
	 * date
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param occupancyDate
	 * @return TaxHeadMasterResponse
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private TaxHeadMasterResponse getTaxHeadMasters(RequestInfo requestInfo, String tenantId, Date occupancyDate)
			throws JsonParseException, JsonMappingException, IOException {

		return calculatorRepository.getTaxHeadMastersRepository(requestInfo, tenantId, occupancyDate);
	}

	/**
	 * API prepares the new demands
	 * 
	 * @param tenantId
	 * @param upicNumber
	 * @param property
	 * @param taxHeadResponse
	 * @param taxPeriod
	 * @param dateFormat
	 * @return List of demands
	 */
	private List<Demand> prepareDemands(String tenantId, String upicNumber, Property property,
			TaxHeadMasterResponse taxHeadResponse, TaxPeriod taxPeriod) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		List<Demand> newDemandList = new ArrayList<>();
		Demand newDemand;
		List<DemandDetail> demandDetailsList;
		DemandDetail demandDetail;
		newDemand = new Demand();
		newDemand.setTenantId(tenantId);
		newDemand.setBusinessService(propertiesManager.getBusinessService());
		newDemand.setConsumerType(property.getPropertyDetail().getPropertyType());
		newDemand.setConsumerCode(upicNumber);
		newDemand.setMinimumAmountPayable(BigDecimal.ONE);
		demandDetailsList = new ArrayList<>();
		for (TaxHeadMaster taxHeadMaster : taxHeadResponse.getTaxHeadMasters()) {
			if (!"ADVANCE".equalsIgnoreCase(taxHeadMaster.getCode())) {
				demandDetail = new DemandDetail();
				demandDetail.setTenantId(tenantId);
				demandDetail.setTaxHeadMasterCode(taxHeadMaster.getCode());
				demandDetailsList.add(demandDetail);
			}
		}
		newDemand.setDemandDetails(demandDetailsList);
		logger.info("Demand fromDate = " + taxPeriod.getFromDate() + " \n toDate = " + taxPeriod.getToDate());
		try {
			Date fromDate = dateFormat.parse(taxPeriod.getFromDate());

			Date toDate = dateFormat.parse(taxPeriod.getToDate());
			logger.info(" Dates, fromDate = " + fromDate + ", toDate = " + toDate + " \n Epoch values, fromDate = "
					+ fromDate.getTime() + " \n toDate = " + toDate.getTime());
			newDemand.setTaxPeriodFrom(fromDate.getTime());
			newDemand.setTaxPeriodTo(toDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Owner owner = new Owner();
		owner.setId(property.getOwners().get(0).getId());
		newDemand.setOwner(owner);
		newDemandList.add(newDemand);
		return newDemandList;
	}

	@Override
	public TitleTransferSearchResponse searchTitleTransfer(RequestInfoWrapper requestInfo,
			TitleTransferSearchCriteria titleTransferSearchCriteria) throws Exception {
		List<TitleTransfer> titleTransfers = propertyRepository.searchTitleTransfer(requestInfo.getRequestInfo(),
				titleTransferSearchCriteria.getTenantId(), titleTransferSearchCriteria.getPageSize(),
				titleTransferSearchCriteria.getPageNumber(), titleTransferSearchCriteria.getSort(),
				titleTransferSearchCriteria.getUpicNo(), titleTransferSearchCriteria.getOldUpicNo(),
				titleTransferSearchCriteria.getApplicationNo());
		TitleTransferSearchResponse titleTransferSearchResponse = new TitleTransferSearchResponse();
		titleTransferSearchResponse.setTitleTransfers(titleTransfers);
		titleTransferSearchResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(), true));
		return titleTransferSearchResponse;
	}

	@Override
	public PropertyDCBResponse updateDcbDemand(PropertyDCBRequest propertyDCBRequest, String tenantId)
			throws Exception {

		PropertyDCB propertyDCB = propertyDCBRequest.getPropertyDCB();
		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setTenantId(tenantId);
		propertySearchCriteria.setActive(true);
		propertySearchCriteria.setPageSize(10);
		propertySearchCriteria.setPageNumber(1);
		propertySearchCriteria.setOldUpicNo(propertyDCB.getOldUpicNumber());

		if (isEmpty(propertyDCB.getUpicNumber()) && !isEmpty(propertyDCB.getOldUpicNumber())) {
			propertySearchCriteria.setOldUpicNo(propertyDCB.getOldUpicNumber());
			PropertyResponse propertyResponse = searchProperty(propertyDCBRequest.getRequestInfo(),
					propertySearchCriteria);

			updateUpicNumberAndOwnerInDemands(propertyDCB, propertyResponse);
		}

		DemandResponse demandResponse = demandRestRepository
				.updateDemand(new DemandRequest(propertyDCBRequest.getRequestInfo(), propertyDCB.getDemands()));

		propertyDCB.setDemands(demandResponse.getDemands());

		return new PropertyDCBResponse(demandResponse.getResponseInfo(), propertyDCB);
	}

	private void updateUpicNumberAndOwnerInDemands(PropertyDCB propertyDCB, PropertyResponse propertyResponse) {
		if (!isEmpty(propertyResponse.getProperties())) {
			Property property = propertyResponse.getProperties().get(0);

			User user = property.getOwners().stream().filter(User::getIsPrimaryOwner).findFirst()
					.orElse(property.getOwners().get(0));

			propertyDCB.getDemands().stream().map(demand -> {
				demand.setConsumerCode(property.getUpicNumber());
				demand.setConsumerType(property.getPropertyDetail().getPropertyType());
				demand.getOwner().setId(user.getId());
				return demand;
			}).collect(Collectors.toList());
		}
	}

	@Override
	public PropertyResponse modifyProperty(PropertyRequest propertyRequest) throws Exception {

		for (Property property : propertyRequest.getProperties()) {
			property.getPropertyDetail().setStatus(StatusEnum.valueOf("WORKFLOW"));
		}

		for (Property property : propertyRequest.getProperties()) {

			propertyValidator.validatePropertyMasterData(property, propertyRequest.getRequestInfo());
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());
			String acknowldgementNumber = generateAcknowledegeMentNumber(property.getTenantId(),
					propertyRequest.getRequestInfo());
			property.getPropertyDetail().setApplicationNo(acknowldgementNumber);

			validatorUtil.isDemandCollected(property.getUpicNumber(), property.getTenantId(),
					propertyRequest.getRequestInfo());

			PropertyRequest updatedPropertyRequest = new PropertyRequest();
			updatedPropertyRequest.setRequestInfo(propertyRequest.getRequestInfo());
			List<Property> updatedPropertyList = new ArrayList<Property>();
			updatedPropertyList.add(property);
			updatedPropertyRequest.setProperties(updatedPropertyList);

			kafkaTemplate.send(propertiesManager.getModifyValidatedProperty(), updatedPropertyRequest);

		}

		return getResponseInfo(propertyRequest);

	}

	@Override
	public DemolitionResponse createDemolition(DemolitionRequest demolitionRequest) throws Exception {

		validatorUtil.isPropertyUnderWorkflow(demolitionRequest.getDemolition().getUpicNumber(),
				demolitionRequest.getRequestInfo());
		String acknowldgementNumber = generateAcknowledegeMentNumber(demolitionRequest.getDemolition().getTenantId(),
				demolitionRequest.getRequestInfo());
		demolitionRequest.getDemolition().setApplicationNo(acknowldgementNumber);

		propertyRepository.updateIsPropertyUnderWorkflow(demolitionRequest.getDemolition().getUpicNumber());

		kafkaTemplate.send(propertiesManager.getCreateValidatedDemolition(), demolitionRequest);
		return getDemolitionResposne(demolitionRequest);
	}

	public DemolitionResponse updateDemolition(DemolitionRequest demolitionRequest) throws Exception {

		validatorUtil.validateWorkflowDeatails(demolitionRequest.getDemolition().getApplicationNo(),
				demolitionRequest.getDemolition().getWorkFlowDetails(), demolitionRequest.getRequestInfo());
		validatorUtil.isDemandCollected(demolitionRequest.getDemolition().getUpicNumber(),
				demolitionRequest.getDemolition().getTenantId(), demolitionRequest.getRequestInfo());

		if (demolitionRequest.getDemolition().getWorkFlowDetails().getAction()
				.equalsIgnoreCase(propertiesManager.getCancelAction())) {
			propertyRepository.setIsPropertyUnderWorkFlowFalse(demolitionRequest.getDemolition().getUpicNumber());

		}

		kafkaTemplate.send(propertiesManager.getUpdateValidatedDemolition(), demolitionRequest);

		return getDemolitionResposne(demolitionRequest);

	}

	@Override
	public DemolitionSearchResponse searchDemolition(RequestInfo requestInfo,
			DemolitionSearchCriteria demolitionSearchCriteria) throws Exception {
		List<Demolition> demolitions = persisterService.searchDemolitions(requestInfo, demolitionSearchCriteria);

		return getDemolitionSearchResposne(demolitions, requestInfo);
	}

	/**
	 * This API will give you the Demolition Response
	 * 
	 * @param demolitionRequest
	 * @return {@link DemolitionResponse}
	 */
	private DemolitionResponse getDemolitionResposne(DemolitionRequest demolitionRequest) {

		DemolitionResponse demolitionResponse = new DemolitionResponse();
		demolitionResponse.setDemolition(demolitionRequest.getDemolition());
		demolitionResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(demolitionRequest.getRequestInfo(), true));

		return demolitionResponse;
	}

	private DemolitionSearchResponse getDemolitionSearchResposne(List<Demolition> demolitions,
			RequestInfo requestInfo) {
		DemolitionSearchResponse demolitionSearchResponse = new DemolitionSearchResponse();
		demolitionSearchResponse.setDemolitions(demolitions);
		demolitionSearchResponse
				.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
		return demolitionSearchResponse;
	}

	@Override
	public TaxExemptionResponse createTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception {

		validatorUtil.isPropertyUnderWorkflow(taxExemptionRequest.getTaxExemption().getUpicNumber(),
				taxExemptionRequest.getRequestInfo());

		validatorUtil.isDemandCollected(taxExemptionRequest.getTaxExemption().getUpicNumber(),
				taxExemptionRequest.getTaxExemption().getTenantId(), taxExemptionRequest.getRequestInfo());

		TaxExemptionResponse taxExemptionResponse = null;

		String acknowldgeMentNumber = generateAcknowledegeMentNumber(
				taxExemptionRequest.getTaxExemption().getTenantId(), taxExemptionRequest.getRequestInfo());

		taxExemptionRequest.getTaxExemption().setApplicationNo(acknowldgeMentNumber);

		kafkaTemplate.send(propertiesManager.getCreateValidatedTaxExemption(), taxExemptionRequest);
		propertyRepository.updateIsPropertyUnderWorkflow(taxExemptionRequest.getTaxExemption().getUpicNumber());

		taxExemptionResponse = new TaxExemptionResponse();
		taxExemptionResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(taxExemptionRequest.getRequestInfo(), true));
		taxExemptionResponse.setTaxExemption(taxExemptionRequest.getTaxExemption());

		return taxExemptionResponse;
	}

	@Override
	@Transactional
	public TaxExemptionResponse updateTaxExemption(TaxExemptionRequest taxExemptionRequest) throws Exception {

		validatorUtil.validateWorkflowDeatails(taxExemptionRequest.getTaxExemption().getApplicationNo(),
				taxExemptionRequest.getTaxExemption().getWorkFlowDetails(), taxExemptionRequest.getRequestInfo());

		kafkaTemplate.send(propertiesManager.getUpdateValidatedTaxExemption(), taxExemptionRequest);

		if (taxExemptionRequest.getTaxExemption().getWorkFlowDetails().getAction()
				.equalsIgnoreCase(propertiesManager.getCancelAction())) {
			propertyRepository.updateIsPropertyUnderWorkflow(taxExemptionRequest.getTaxExemption().getUpicNumber());
		}
		TaxExemptionResponse taxExemptionResponse = null;
		taxExemptionResponse = new TaxExemptionResponse();
		taxExemptionResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(taxExemptionRequest.getRequestInfo(), true));
		taxExemptionResponse.setTaxExemption(taxExemptionRequest.getTaxExemption());

		return taxExemptionResponse;
	}

	/**
	 * Save property history and update property for Tax Exemption
	 * 
	 * @param taxExemptionRequest
	 * @throws Exception
	 */
	@Transactional
	public PropertyRequest savePropertyHistoryandUpdateProperty(TaxExemptionRequest taxExemptionRequest)
			throws Exception {
		Property property = persisterService.getPropertyUsingUpicNo(taxExemptionRequest);
		persisterService.addPropertyHistoryForTaxExemption(taxExemptionRequest, property);
		Property updatedProperty = persisterService.updateTaxExemptionProperty(taxExemptionRequest, property);
		PropertyRequest propertyRequest = new PropertyRequest();
		propertyRequest.setRequestInfo(taxExemptionRequest.getRequestInfo());
		List<Property> properties = new ArrayList<Property>();
		properties.add(updatedProperty);
		propertyRequest.setProperties(properties);
		return propertyRequest;
	}

	@Override
	public TaxExemptionSearchResponse searchTaxExemption(RequestInfoWrapper requestInfo,
			TaxExemptionSearchCriteria taxExemptionSearchCriteria) throws Exception {
		List<TaxExemption> taxExemptions = taxExemptionRepository.searchTaxExemption(requestInfo.getRequestInfo(),
				taxExemptionSearchCriteria);
		TaxExemptionSearchResponse taxExemptionSearchResponse = new TaxExemptionSearchResponse();
		taxExemptionSearchResponse.setTaxExemptions(taxExemptions);
		taxExemptionSearchResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(), true));
		return taxExemptionSearchResponse;
	}

	@Override
	public VacancyRemissionResponse createVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest)
			throws Exception {

		RequestInfo requestInfo = vacancyRemissionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		validatorUtil.validateVacancyRemissionData(vacancyRemissionRequest.getVacancyRemission().getUpicNo(),
				vacancyRemissionRequest.getVacancyRemission().getTenantId(),
				vacancyRemissionRequest.getVacancyRemission().getFromDate(),
				vacancyRemissionRequest.getVacancyRemission().getToDate(), requestInfoWrapper);

		validatorUtil.isPropertyUnderWorkflow(vacancyRemissionRequest.getVacancyRemission().getUpicNo(), requestInfo);

		validatorUtil.isDemandCollected(vacancyRemissionRequest.getVacancyRemission().getUpicNo(),
				vacancyRemissionRequest.getVacancyRemission().getTenantId(), requestInfo);

		propertyRepository.updateIsPropertyUnderWorkflow(vacancyRemissionRequest.getVacancyRemission().getUpicNo());

		kafkaTemplate.send(propertiesManager.getCreateValidatedVacancyRemission(), vacancyRemissionRequest);

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(vacancyRemissionRequest.getRequestInfo(), true);
		VacancyRemissionResponse vacancyRemissionResponse = new VacancyRemissionResponse();
		vacancyRemissionResponse.setResponseInfo(responseInfo);
		vacancyRemissionResponse.setVacancyRemission(vacancyRemissionRequest.getVacancyRemission());

		return vacancyRemissionResponse;
	}

	@Override
	public VacancyRemissionResponse updateVacancyRemission(VacancyRemissionRequest vacancyRemissionRequest)
			throws Exception {

		RequestInfo requestInfo = vacancyRemissionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		validatorUtil.validateWorkflowDeatails(vacancyRemissionRequest.getVacancyRemission().getApplicationNo(),
				vacancyRemissionRequest.getVacancyRemission().getWorkFlowDetails(), requestInfo);

		validatorUtil.validateVacancyRemissionData(vacancyRemissionRequest.getVacancyRemission().getUpicNo(),
				vacancyRemissionRequest.getVacancyRemission().getTenantId(),
				vacancyRemissionRequest.getVacancyRemission().getFromDate(),
				vacancyRemissionRequest.getVacancyRemission().getToDate(), requestInfoWrapper);

		validatorUtil.isDemandCollected(vacancyRemissionRequest.getVacancyRemission().getUpicNo(),
				vacancyRemissionRequest.getVacancyRemission().getTenantId(), requestInfo);

		if (vacancyRemissionRequest.getVacancyRemission().getWorkFlowDetails().getAction()
				.equalsIgnoreCase(propertiesManager.getCancelAction())) {
			propertyRepository
					.setIsPropertyUnderWorkFlowFalse(vacancyRemissionRequest.getVacancyRemission().getUpicNo());
		}

		kafkaTemplate.send(propertiesManager.getUpdateValidatedVacancyRemission(), vacancyRemissionRequest);

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(vacancyRemissionRequest.getRequestInfo(), true);
		VacancyRemissionResponse vacancyRemissionResponse = new VacancyRemissionResponse();
		vacancyRemissionResponse.setResponseInfo(responseInfo);
		vacancyRemissionResponse.setVacancyRemission(vacancyRemissionRequest.getVacancyRemission());

		return vacancyRemissionResponse;
	}

	@Override
	public VacancyRemissionSearchResponse searchVacancyRemission(RequestInfoWrapper requestInfo,
			VacancyRemissionSearchCriteria vacancyRemissionSearchCriteria) throws Exception {

		List<VacancyRemission> vacancyRemissions = vacancyRemissionRepository.searchVacancyRemission(
				requestInfo.getRequestInfo(), vacancyRemissionSearchCriteria.getTenantId(),
				vacancyRemissionSearchCriteria.getPageSize(), vacancyRemissionSearchCriteria.getPageNumber(),
				vacancyRemissionSearchCriteria.getUpicNumber(), vacancyRemissionSearchCriteria.getApplicationNo(),
				vacancyRemissionSearchCriteria.getApprovalDate(), vacancyRemissionSearchCriteria.getIsApproved());
		VacancyRemissionSearchResponse vacancyRemissionSearchResponse = new VacancyRemissionSearchResponse();
		vacancyRemissionSearchResponse.setVacancyRemission(vacancyRemissions);
		vacancyRemissionSearchResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(), true));

		return vacancyRemissionSearchResponse;
	}
}
