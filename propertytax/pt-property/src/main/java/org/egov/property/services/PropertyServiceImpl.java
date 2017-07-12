package org.egov.property.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.models.Address;
import org.egov.models.AuditDetails;
import org.egov.models.Document;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.Floor;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.models.VacantLandDetail;
import org.egov.property.consumer.Producer;
import org.egov.property.exception.IdGenerationException;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.exception.PropertyUnderWorkflowException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.model.PropertyUser;
import org.egov.property.repository.PropertyMasterRepository;
import org.egov.property.repository.PropertyRepository;
import org.egov.property.utility.PropertyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	RestTemplate restTemplate;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	PropertyMasterRepository propertyMasterRepository;

	@Override
	public PropertyResponse createProperty(PropertyRequest propertyRequest) {
		// TODO Auto-generated method stub
		for (Property property : propertyRequest.getProperties()) {
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());
			String acknowldgementNumber = generateAcknowledegeMentNumber(property.getTenantId(),
					propertyRequest.getRequestInfo());
			property.getPropertyDetail().setApplicationNo(acknowldgementNumber);
			producer.send(environment.getProperty("egov.propertytax.property.userenhanced"), propertyRequest);
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
			producer.send(environment.getProperty("egov.propertytax.property.update.userenhanced"), propertyRequest);
		}
		return null;
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
		try {
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
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
	 * @return Property Object if search is successful or Error Object if search
	 *         will fail
	 */

	@SuppressWarnings("unchecked")
	public PropertyResponse searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			Integer pageSize, Integer pageNumber, String[] sort, String oldUpicNo, String mobileNumber,
			String aadhaarNumber, String houseNoBldgApt, Integer revenueZone, Integer revenueWard, Integer locality,
			String ownerName, Integer demandFrom, Integer demandTo) {

		List<Property> updatedPropety = null;

		try {

			Map<String, Object> map = propertyRepository.searchProperty(requestInfo, tenantId, active, upicNo, pageSize,
					pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone, revenueWard,
					locality, ownerName, demandFrom, demandTo);

			List<Property> property = (List<Property>) map.get("properties");
			List<User> users = (List<User>) map.get("users");
			updatedPropety = addAllPropertyDetails(property, requestInfo, users);
		} catch (Exception e) {
			throw new PropertySearchException(environment.getProperty("invalid.input"), requestInfo);
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

			List<PropertyUser> propertyUsers = propertyRepository.getPropertyUserByProperty(propertyId);
			List<Integer> userIds = new ArrayList<>();

			for (PropertyUser propertyUser : propertyUsers) {

				userIds.add(propertyUser.getOwner());
			}

			List<User> userOfProperty = getUserObjectForUserIds(userIds, users);

			// get owner info for property

			for (User propertyUser : userOfProperty) {
				ownerInfos.add(propertyUser);
			}

			property.setOwners(ownerInfos);
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

			for (Floor floor : property.getPropertyDetail().getFloors()) {

				List<Unit> units = propertyRepository.getUnitsByFloor(floor.getId());
				floor.setUnits(units);

			}

			for (Floor floor : propertyDetail.getFloors()) {
				int i = 0;
				List<Unit> units = propertyDetail.getFloors().get(i).getUnits();

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

			AuditDetails auditDetails = propertyRepository.getAuditForPropertyDetails(propertyId);
			property.getPropertyDetail().setAuditDetails(auditDetails);

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
				propertyRepository.updateIsPropertyUnderWorkflow(titleTransferRequest.getTitleTransfer().getUpicNo());
				String acknowldgeMentNumber = generateAcknowledegeMentNumber(
						titleTransferRequest.getTitleTransfer().getTenantId(), titleTransferRequest.getRequestInfo());

				titleTransferRequest.getTitleTransfer().setApplicationNo(acknowldgeMentNumber);

				producer.send(environment.getProperty("egov.propertytax.property.titletransfer.create"),
						titleTransferRequest);

				titleTransferResponse = new TitleTransferResponse();
				titleTransferResponse.setResponseInfo(responseInfoFactory
						.createResponseInfoFromRequestInfo(titleTransferRequest.getRequestInfo(), true));
				titleTransferResponse.setTitleTransfer(titleTransferRequest.getTitleTransfer());
			}
		}
		return titleTransferResponse;
	}

	/**
	 * This method will validate title transfer object(Currently not yet have
	 * any validations)
	 * 
	 * @param titleTransferRequest
	 * @return boolean
	 */
	private Boolean validateTitleTransfer(TitleTransferRequest titleTransferRequest) {

		return propertyMasterRepository
				.checkUniqueCodeForMutation(titleTransferRequest.getTitleTransfer().getTrasferReason());
	}
}
