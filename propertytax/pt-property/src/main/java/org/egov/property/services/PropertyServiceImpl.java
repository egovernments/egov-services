package org.egov.property.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.egov.models.*;
import org.egov.enums.StatusEnum;
import org.egov.models.Address;
import org.egov.models.AttributeNotFoundException;
import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandResponse;
import org.egov.models.Document;
import org.egov.models.Error;
import org.egov.models.demand.*;
import org.egov.models.demand.TaxHeadMaster;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.IdGenerationException;
import org.egov.property.exception.InvalidUpdatePropertyException;
import org.egov.property.exception.PropertyTaxPendingException;
import org.egov.property.exception.PropertyUnderWorkflowException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.repository.DemandRepository;
import org.egov.property.repository.PropertyMasterRepository;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.repository.WorkFlowRepository;
import org.egov.property.utility.PropertyValidator;
import org.egov.property.utility.TimeStampUtil;
import org.egov.property.utility.UpicNoGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

	private static final Logger logger = LoggerFactory.getLogger(PropertyServiceImpl.class);

	@Autowired
	DemandRepository demandRepository;

	@Autowired
	WorkFlowRepository workFlowRepository;

	@Override
	public PropertyResponse createProperty(PropertyRequest propertyRequest) {
		// TODO Auto-generated method stub

		for (Property property : propertyRequest.getProperties()) {
			propertyValidator.validatePropertyMasterData(property, propertyRequest.getRequestInfo());
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());
			if (property.getOldUpicNumber() != null)
				propertyValidator.validateUpicNo(property, propertyRequest.getRequestInfo());
			String acknowldgementNumber = generateAcknowledegeMentNumber(property.getTenantId(),
					propertyRequest.getRequestInfo());
			property.getPropertyDetail().setApplicationNo(acknowldgementNumber);
			property.getPropertyDetail().setStatus(StatusEnum.WORKFLOW);
			PropertyRequest updatedPropertyRequest = new PropertyRequest();
			updatedPropertyRequest.setRequestInfo(propertyRequest.getRequestInfo());
			List<Property> updatedPropertyList = new ArrayList<Property>();
			if (property.getChannel().toString().equalsIgnoreCase(propertiesManager.getChannelType())) {

				String upicNumber = upicNoGeneration.generateUpicNo(property, propertyRequest.getRequestInfo());
				property.setUpicNumber(upicNumber);

			}
			updatedPropertyList.add(property);
			updatedPropertyRequest.setProperties(updatedPropertyList);
			kafkaTemplate.send(propertiesManager.getCreateValidatedProperty(), updatedPropertyRequest);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(), true);
		PropertyResponse propertyResponse = new PropertyResponse();
		propertyResponse.setResponseInfo(responseInfo);
		propertyResponse.setProperties(propertyRequest.getProperties());
		return propertyResponse;
	}

	@Override
	public PropertyResponse updateProperty(PropertyRequest propertyRequest) {
		for (Property property : propertyRequest.getProperties()) {
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());
			propertyValidator.validateWorkflowDeatails(property, propertyRequest.getRequestInfo());
			property.getPropertyDetail().setStatus(StatusEnum.WORKFLOW);
			PropertyRequest updatedPropertyRequest = new PropertyRequest();
			updatedPropertyRequest.setRequestInfo(propertyRequest.getRequestInfo());
			List<Property> updatedPropertyList = new ArrayList<Property>();
			updatedPropertyList.add(property);
			updatedPropertyRequest.setProperties(updatedPropertyList);
			kafkaTemplate.send(propertiesManager.getUpdateValidatedProperty(), updatedPropertyRequest);
		}

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(), true);
		PropertyResponse propertyResponse = new PropertyResponse();
		propertyResponse.setResponseInfo(responseInfo);
		propertyResponse.setProperties(propertyRequest.getProperties());
		return propertyResponse;
	}

	/**
	 * Description: Generating acknowledge number for property
	 * 
	 * @param property
	 * @param requestInfo
	 * @return
	 */
	public String generateAcknowledegeMentNumber(String tenantId, RequestInfo requestInfo) {

		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(propertiesManager.getIdHostName());
		idGenerationUrl.append(propertiesManager.getIdCreatepath());

		// generating acknowledgement number for all properties
		String acknowledegeMentNumber = null;
		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(propertiesManager.getIdFormat());
		idrequest.setIdName(propertiesManager.getIdName());
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(requestInfo);
		String response = null;

		RestTemplate restTemplate = new RestTemplate();
		try {
			logger.info("PropertyServiceImpl idGenerationUrl : " + idGenerationUrl.toString()
					+ " \n IdGenerationRequest  : " + idGeneration);
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
			logger.info("PropertyServiceImpl IdGenerationResponse : " + response);
		} catch (Exception ex) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidIdServiceUrl(),
					idGenerationUrl.toString(), requestInfo);
		}
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
	 * @param tenantId
	 * @param active
	 * @param upicNo
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param oldUpicNo
	 * @param mobileNumber
	 * @param aadhaarNumber
	 * @param houseNoBldgApt
	 * @param revenueZone
	 * @param revenueWard
	 * @param locality
	 * @param ownerName
	 * @param demandFrom
	 * @param demandTo
	 * @param propertyId
	 * @return Property Object if search is successful or Error Object if search
	 *         will fail
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */

	@SuppressWarnings("unchecked")
	public PropertyResponse searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			Integer pageSize, Integer pageNumber, String[] sort, String oldUpicNo, String mobileNumber,
			String aadhaarNumber, String houseNoBldgApt, Integer revenueZone, Integer revenueWard, Integer locality,
			String ownerName, Integer demandFrom, Integer demandTo, String propertyId, String applicationNo)
					throws Exception {

		List<Property> updatedPropety = null;

		Map<String, Object> map = propertyRepository.searchProperty(requestInfo, tenantId, active, upicNo, pageSize,
				pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone, revenueWard,
				locality, ownerName, demandFrom, demandTo, propertyId, applicationNo);

		List<Property> property = (List<Property>) map.get("properties");
		List<User> users = (List<User>) map.get("users");
		updatedPropety = addAllPropertyDetails(property, requestInfo, users);

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
			List<Unit> flats = new ArrayList<>();
			List<Unit> rooms = new ArrayList<>();
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

	@Override
	public TitleTransferResponse createTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {
		Boolean isPropertyUnderWorkflow = propertyRepository
				.isPropertyUnderWorkflow(titleTransferRequest.getTitleTransfer().getUpicNo());
		TitleTransferResponse titleTransferResponse = null;

		if (isPropertyUnderWorkflow) {
			throw new PropertyUnderWorkflowException(propertiesManager.getInvalidPropertyStatus(),
					titleTransferRequest.getRequestInfo());

		} else {
			Boolean isValidated = validateTitleTransfer(titleTransferRequest);

			if (!isValidated) {
				throw new PropertyUnderWorkflowException(propertiesManager.getInvalidTitleTransfer(),
						titleTransferRequest.getRequestInfo());

			} else {

				String acknowldgeMentNumber = generateAcknowledegeMentNumber(
						titleTransferRequest.getTitleTransfer().getTenantId(), titleTransferRequest.getRequestInfo());

				titleTransferRequest.getTitleTransfer().setApplicationNo(acknowldgeMentNumber);

				kafkaTemplate.send(propertiesManager.getCreateTitleTransferUserValidator(), titleTransferRequest);
				propertyRepository.updateIsPropertyUnderWorkflow(titleTransferRequest.getTitleTransfer().getUpicNo());

				titleTransferResponse = new TitleTransferResponse();
				titleTransferResponse.setResponseInfo(responseInfoFactory
						.createResponseInfoFromRequestInfo(titleTransferRequest.getRequestInfo(), true));
				titleTransferResponse.setTitleTransfer(titleTransferRequest.getTitleTransfer());
			}
		}
		return titleTransferResponse;
	}

	@Override
	public TitleTransferResponse updateTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {

		validateTitleTransferWorkflowDeatails(titleTransferRequest, titleTransferRequest.getRequestInfo());
		kafkaTemplate.send(propertiesManager.getUpdateTitleTransferUserValidator(), titleTransferRequest);
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
	private Boolean validateTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {

		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(titleTransferRequest.getRequestInfo());
		DemandResponse demandResponse = demandRepository.getDemands(titleTransfer.getUpicNo(),
				titleTransfer.getTenantId(), requestInfoWrapper);

		if (demandResponse != null) {
			for (Demand demand : demandResponse.getDemands()) {
				Double totalTax = 0.0;
				Double collectedAmount = 0.0;
				for (DemandDetail demandDetail : demand.getDemandDetails()) {
					totalTax += demandDetail.getTaxAmount().doubleValue();
					collectedAmount += demandDetail.getCollectionAmount().doubleValue();
				}
				if (totalTax != collectedAmount) {
					throw new PropertyTaxPendingException(propertiesManager.getInvalidTaxMessage(),
							titleTransferRequest.getRequestInfo());
				}
			}
		}
		return propertyMasterRepository
				.checkUniqueCodeForMutation(titleTransferRequest.getTitleTransfer().getTransferReason());
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
		Notice notice = new Notice();

		SpecialNoticeResponse specialNoticeResponse = new SpecialNoticeResponse();
		String upicNo = specialNoticeRequest.getUpicNo();
		String tenantId = specialNoticeRequest.getTenantId();

		PropertyResponse propertyRespone = searchProperty(specialNoticeRequest.getRequestInfo(), tenantId, null, upicNo,
				null, null, null, null, null, null, null, 0, 0, 0, null, 0, 0, null, null);

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

		StringBuffer taxPeriodSearchUrl = new StringBuffer();
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorHostName());
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorTaxperiodsSearch());
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(taxPeriodSearchUrl.toString())
				.queryParam("tenantId", tenantId).queryParam("validDate", notice.getNoticeDate());

		TaxPeriodResponse taxPeriodResponse = null;

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(specialNoticeRequest.getRequestInfo());
		logger.info("PropertyServiceImpl BuilderUri : " + builder.buildAndExpand().toUri()
				+ " \n RequestInfoWrapper  : " + requestInfoWrapper);
		RestTemplate restTemplate = new RestTemplate();
		taxPeriodResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(), requestInfoWrapper,
				TaxPeriodResponse.class);
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
	private void addNotice(Notice notice, Double totalTax) throws Exception {

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

		StringBuilder tenantCodeUrl = new StringBuilder();
		tenantCodeUrl.append(propertiesManager.getTenantHostName());
		tenantCodeUrl.append(propertiesManager.getTenantBasepath());
		tenantCodeUrl.append(propertiesManager.getTenantSearchpath());

		String url = tenantCodeUrl.toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("code", tenantId);
		SearchTenantResponse searchTenantResponse = null;
		RestTemplate restTemplate = new RestTemplate();
		logger.info("PropertyServiceImpl Calling tenant service url = " + tenantCodeUrl.toString() + " request = "
				+ requestInfo + " tenantId =" + tenantId);
		String response = restTemplate.postForObject(builder.buildAndExpand().toUri(), requestInfo, String.class);
		logger.info("PropertyServiceImpl after calling the tenant service  response = " + response);

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
		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(propertiesManager.getIdHostName());
		idGenerationUrl.append(propertiesManager.getIdCreatepath());

		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(propertiesManager.getUlbFormat());
		idrequest.setIdName(propertiesManager.getUlbName());
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(requestInfo);
		RestTemplate restTemplate = new RestTemplate();
		logger.info("PropertyServiceImpl calling the idgenearion service url = " + idGenerationUrl.toString()
				+ " Request = " + idGeneration.toString());
		String response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);

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
		// RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		PropertyResponse propertyResponse = searchProperty(requestInfoWrapper.getRequestInfo(), tenantId, null,
				upicNumber, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

		if (propertyResponse != null) {
			Property property = propertyResponse.getProperties().get(0);

			SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date occupancyDate = dbDateFormat.parse(property.getOccupancyDate());
			String occupancyDateStr = new SimpleDateFormat(propertiesManager.getSimpleDateFormat())
					.format(occupancyDate);

			// Fetch TaxPeriods
			TaxPeriodResponse taxPeriodResponse = getTaxPeriodsForOccupancyDate(requestInfoWrapper.getRequestInfo(),
					tenantId, occupancyDateStr);
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
			String occupancyDateStr) {
		StringBuffer taxPeriodSearchUrl = new StringBuffer();
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorHostName());
		taxPeriodSearchUrl.append(propertiesManager.getCalculatorTaxperiodsSearch());
		String todate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(taxPeriodSearchUrl.toString())
				.queryParam("tenantId", tenantId).queryParam("fromDate", occupancyDateStr).queryParam("toDate", todate);

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		logger.info("PropertyServiceImpl BuilderUri : " + builder.buildAndExpand().toUri()
				+ " \n RequestInfoWrapper  : " + requestInfoWrapper);
		RestTemplate restTemplate = new RestTemplate();
		TaxPeriodResponse taxPeriodResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(),
				requestInfoWrapper, TaxPeriodResponse.class);
		return taxPeriodResponse;
	}

	/**
	 * API fetches TaxHeadMasters from the Billing Service for the occupancy
	 * date
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param occupancyDate
	 * @return TaxHeadMasterResponse
	 */
	private TaxHeadMasterResponse getTaxHeadMasters(RequestInfo requestInfo, String tenantId, Date occupancyDate) {
		TaxHeadMasterResponse taxHeadResponse = null;
		RestTemplate restTemplate = new RestTemplate();
		StringBuffer taxHeadsUrl = new StringBuffer();
		taxHeadsUrl.append(propertiesManager.getBillingServiceHostname());
		taxHeadsUrl.append(propertiesManager.getBillingServiceSearchTaxHeads());

		URI uri = UriComponentsBuilder.fromUriString(taxHeadsUrl.toString()).queryParam("tenantId", tenantId)
				.queryParam("service", "PT").queryParam("validFrom", occupancyDate.getTime())
				.queryParam("validTill", new Date().getTime()).build(true).encode().toUri();

		logger.info("getTaxHeadMasters taxheads url --> " + uri + " taxheads request --> " + requestInfo);

		String taxHeadsResponseStr = restTemplate.postForObject(uri, requestInfo, String.class);
		logger.info("getTaxHeadMasters taxheads response string is --> " + taxHeadsResponseStr);
		if (!taxHeadsResponseStr.isEmpty()) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				taxHeadResponse = objectMapper.readValue(taxHeadsResponseStr, TaxHeadMasterResponse.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return taxHeadResponse;
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
}
