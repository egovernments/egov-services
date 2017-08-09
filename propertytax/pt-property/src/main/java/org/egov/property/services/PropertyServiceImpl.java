package org.egov.property.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.models.Address;
import org.egov.models.AttributeNotFoundException;
import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandResponse;
import org.egov.models.Document;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.Floor;
import org.egov.models.FloorSpec;
import org.egov.models.HeadWiseTax;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Notice;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.SearchTenantResponse;
import org.egov.models.SpecialNoticeRequest;
import org.egov.models.SpecialNoticeResponse;
import org.egov.models.TaxCalculation;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.egov.models.TotalTax;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.models.VacantLandDetail;
import org.egov.models.WorkFlowDetails;
import org.egov.property.consumer.Producer;
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
import org.egov.property.utility.UpicNoGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
	Producer producer;

	@Autowired
	Environment environment;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	PropertyMasterRepository propertyMasterRepository;

	@Autowired
	PersisterService persisterService;

	private static final Logger logger = LoggerFactory.getLogger(UpicNoGeneration.class);

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
			String acknowldgementNumber = generateAcknowledegeMentNumber(property.getTenantId(),
					propertyRequest.getRequestInfo());
			property.getPropertyDetail().setApplicationNo(acknowldgementNumber);
			producer.send(environment.getProperty("egov.propertytax.property.create.validate.user"), propertyRequest);
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
			producer.send(environment.getProperty("egov.propertytax.property.update.validate.user"), propertyRequest);
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
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.hostname"));
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.createpath"));

		// generating acknowledgement number for all properties
		String acknowledegeMentNumber = null;
		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(environment.getProperty("id.format"));
		idrequest.setIdName(environment.getProperty("id.idName"));
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
			throw new ValidationUrlNotFoundException(environment.getProperty("invalid.id.service.url"),
					idGenerationUrl.toString(), requestInfo);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new IdGenerationException(error.getMessage(), error.getDescription(), requestInfo);
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString()
					.equalsIgnoreCase(environment.getProperty("success"))) {
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
	 * @see ArrayList
	 * 
	 */

	private List<Property> addAllPropertyDetails(List<Property> properties, RequestInfo requestInfo, List<User> users)
			throws JsonParseException, JsonMappingException, IOException {

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

			propertyDetail.setFloors(propertyDetail.getFloors());
			property.setPropertyDetail(propertyDetail);

			VacantLandDetail vacantLandDetail = propertyRepository.getVacantLandByProperty(propertyId);
			property.setVacantLand(vacantLandDetail);

			PropertyLocation propertyLocation = propertyRepository.getPropertyLocationByproperty(propertyId);
			property.setBoundary(propertyLocation);

			Long propertyDetailId = property.getPropertyDetail().getId();

			List<Floor> floors = propertyRepository.getFloorsByPropertyDetails(propertyDetailId);
			property.getPropertyDetail().setFloors(floors);

			for (Floor floor : floors) {
				List<Unit> units = propertyRepository.getUnitsByFloor(floor.getId());
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
			throw new PropertyUnderWorkflowException(environment.getProperty("invalid.property.status"),
					titleTransferRequest.getRequestInfo());

		} else {
			Boolean isValidated = validateTitleTransfer(titleTransferRequest);

			if (!isValidated) {
				throw new PropertyUnderWorkflowException(environment.getProperty("invalid.title.transfer"),
						titleTransferRequest.getRequestInfo());

			} else {

				String acknowldgeMentNumber = generateAcknowledegeMentNumber(
						titleTransferRequest.getTitleTransfer().getTenantId(), titleTransferRequest.getRequestInfo());

				titleTransferRequest.getTitleTransfer().setApplicationNo(acknowldgeMentNumber);

				producer.send(environment.getProperty("egov.propertytax.property.titletransfer.create"),
						titleTransferRequest);
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
		producer.send(environment.getProperty("egov.propertytax.property.titletransfer.update"), titleTransferRequest);
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
			throw new InvalidUpdatePropertyException(environment.getProperty("workflow.action.message"), requestInfo);

		} else if (workflowDetails.getAssignee() == null) {
			throw new InvalidUpdatePropertyException(environment.getProperty("workflow.assignee.message"), requestInfo);

		} else if (workflowDetails.getDepartment() == null) {
			throw new InvalidUpdatePropertyException(environment.getProperty("workflow.department.message"),
					requestInfo);

		} else if (workflowDetails.getDesignation() == null) {
			throw new InvalidUpdatePropertyException(environment.getProperty("workflow.designation.message"),
					requestInfo);

		} else if (workflowDetails.getStatus() == null) {
			throw new InvalidUpdatePropertyException(environment.getProperty("workflow.status.message"), requestInfo);

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
					throw new PropertyTaxPendingException(environment.getProperty("invalid.titletransfer.tax.message"),
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
				10000, -1, null, null, null, null, null, 0, 0, 0, null, 0, 0, null, null);

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
		notice.setNoticeDate(
				new SimpleDateFormat(environment.getProperty("egov.property.simple.dateformat")).format(new Date()));

		StringBuffer taxPeriodSearchUrl = new StringBuffer();
		taxPeriodSearchUrl.append(environment.getProperty("egov.services.pt_calculator.hostname"));
		taxPeriodSearchUrl.append(environment.getProperty("egov.services.pt_calculator.taxperiods.search"));
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

		SimpleDateFormat sdf = new SimpleDateFormat(environment.getProperty("egov.property.simple.dateformat"));

		SimpleDateFormat sdff = new SimpleDateFormat(environment.getProperty("egov.property.date.and.timeformat"));
		List<TaxCalculation> currentYearTax = new ArrayList<>();
		Double totalTax = 0.0d;

		for (TaxPeriod taxPeriod : taxPeriodResponse.getTaxPeriods()) {

			for (TaxCalculation taxCalculation : calculationList) {

				if (sdff.parse(taxPeriod.getFromDate()).compareTo(sdf.parse(taxCalculation.getFromDate())) >= 0
						&& sdff.parse(taxPeriod.getToDate()).compareTo(sdf.parse(taxCalculation.getToDate())) >= 0) {

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

		workFlowRepository.updateWorkFlowDetails(workFlowDetails, specialNoticeRequest.getRequestInfo(), tenantId,
				specialNoticeRequest.getStateId());
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
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.hostname"));
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.basepath"));
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.searchpath"));

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
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.hostname"));
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.createpath"));

		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(environment.getProperty("ulb.format"));
		idrequest.setIdName(environment.getProperty("ulb.name"));
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
		return object == null ? "" : object.toString();
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
		persisterService.addPropertyHistory(titleTransferRequest,property);
		Property updatedProperty = persisterService.updateTitleTransferProperty(titleTransferRequest, property);
		PropertyRequest propertyRequest = new PropertyRequest();
		propertyRequest.setRequestInfo(titleTransferRequest.getRequestInfo());
		List<Property> properties = new ArrayList<Property>();
		properties.add(updatedProperty);
		propertyRequest.setProperties(properties);
		return propertyRequest;
	}

}
