package org.egov.property.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.enums.ChannelEnum;
import org.egov.enums.CreationReasonEnum;
import org.egov.enums.Reason;
import org.egov.enums.SourceEnum;
import org.egov.enums.StatusEnum;
import org.egov.models.Address;
import org.egov.models.AssessmentDate;
import org.egov.models.AuditDetails;
import org.egov.models.BuilderDetail;
import org.egov.models.Demand;
import org.egov.models.DemandId;
import org.egov.models.Demolition;
import org.egov.models.DemolitionRequest;
import org.egov.models.Document;
import org.egov.models.Factors;
import org.egov.models.Floor;
import org.egov.models.FloorSpec;
import org.egov.models.HeadWiseTax;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyRequest;
import org.egov.models.RequestInfo;
import org.egov.models.SpecialNotice;
import org.egov.models.TitleTransfer;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.models.UserResponseInfo;
import org.egov.models.VacancyRemission;
import org.egov.models.VacancyRemissionRequest;
import org.egov.models.VacantLandDetail;
import org.egov.property.config.PropertiesManager;
import org.egov.property.model.PropertyLocationRowMapper;
import org.egov.property.repository.builder.AddressBuilder;
import org.egov.property.repository.builder.BoundaryBuilder;
import org.egov.property.repository.builder.DocumentBuilder;
import org.egov.property.repository.builder.FloorBuilder;
import org.egov.property.repository.builder.PropertyBuilder;
import org.egov.property.repository.builder.PropertyDetailBuilder;
import org.egov.property.repository.builder.PropertyHistoryBuilder;
import org.egov.property.repository.builder.SearchPropertyBuilder;
import org.egov.property.repository.builder.SpecialNoticeBuilder;
import org.egov.property.repository.builder.TitleTransferAddressBuilder;
import org.egov.property.repository.builder.TitleTransferBuilder;
import org.egov.property.repository.builder.TitleTransferDocumentBuilder;
import org.egov.property.repository.builder.TitleTransferUserBuilder;
import org.egov.property.repository.builder.UnitBuilder;
import org.egov.property.repository.builder.UserBuilder;
import org.egov.property.repository.builder.VacancyRemissionBuilder;
import org.egov.property.repository.builder.VacancyRemissionDocumentBuilder;
import org.egov.property.repository.builder.VacantLandDetailBuilder;
import org.egov.property.utility.TimeStampUtil;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * 
 * @author Prasad
 *
 */
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PropertyRepository {

	@Autowired
	SearchPropertyBuilder searchPropertyBuilder;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	TitleTransferBuilder titleTransferBuilder;
	
	@Autowired
	VacancyRemissionBuilder vacancyRemissionBuilder;

	@Autowired
	LogAwareRestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(PropertyRepository.class);

	/**
	 * property query formation
	 * 
	 * @param property
	 * @return holder key
	 * @throws JsonProcessingException
	 */

	public Long saveProperty(Property property) throws Exception {

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(PropertyBuilder.INSERT_PROPERTY_QUERY,
						new String[] { "id" });
				ps.setString(1, property.getTenantId());
				ps.setString(2, property.getUpicNumber());
				ps.setString(3, property.getOldUpicNumber());
				ps.setString(4, property.getVltUpicNumber());
				ps.setString(5, property.getCreationReason().toString());
				ps.setTimestamp(6, TimeStampUtil.getTimeStamp(property.getAssessmentDate()));
				ps.setObject(7, TimeStampUtil.getTimeStamp(property.getOccupancyDate()));
				ps.setString(8, property.getGisRefNo());
				ps.setBoolean(9, property.getIsAuthorised());
				ps.setBoolean(10, property.getIsUnderWorkflow());
				ps.setString(11, property.getChannel().toString());
				ps.setString(12, property.getAuditDetails().getCreatedBy());
				ps.setString(13, property.getAuditDetails().getLastModifiedBy());
				ps.setLong(14, getLong(property.getAuditDetails().getCreatedTime()));
				ps.setLong(15, getLong(property.getAuditDetails().getLastModifiedTime()));
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
				jsonObject.setValue(gson.toJson(demandIdList));
				ps.setObject(16, jsonObject);
				ps.setInt(17, getInteger(property.getSequenceNo()));
				ps.setString(18, property.getOldestUpicNumber());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

		Long propertyId = Long.parseLong(String.valueOf(holder.getKey().intValue()).trim());

		return propertyId;

	}

	/**
	 * Address creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveAddress(Property property, Long propertyId) {

		Address address = property.getAddress();

		Object[] addressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressNumber(), address.getAddressLine1(), address.getAddressLine2(), address.getLandmark(),
				address.getCity(), address.getPincode(), address.getDetail(), address.getAuditDetails().getCreatedBy(),
				address.getAuditDetails().getLastModifiedBy(), address.getAuditDetails().getCreatedTime(),
				address.getAuditDetails().getLastModifiedTime(), propertyId, address.getSurveyNo(),
				address.getPlotNo() };

		jdbcTemplate.update(AddressBuilder.INSERT_ADDRESS_QUERY, addressArgs);

	}

	/**
	 * Property details query
	 * 
	 * @param property
	 * @param propertyId
	 * @return key holder id
	 */
	public Long savePropertyDetails(Property property, Long propertyId) {
		PropertyDetail propertyDetails = property.getPropertyDetail();
		final PreparedStatementCreator pscPropertyDetails = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(PropertyDetailBuilder.INSERT_PROPERTYDETAILS_QUERY, new String[] { "id" });
				ps.setString(1, propertyDetails.getSource().toString());
				ps.setObject(2, propertyDetails.getRegdDocNo());
				ps.setObject(3, TimeStampUtil.getTimeStamp(propertyDetails.getRegdDocDate()));
				ps.setString(4, propertyDetails.getReason());
				ps.setString(5, propertyDetails.getStatus().toString());
				ps.setBoolean(6, propertyDetails.getIsVerified());
				ps.setObject(7, TimeStampUtil.getTimeStamp(propertyDetails.getVerificationDate()));
				ps.setBoolean(8, propertyDetails.getIsExempted());
				ps.setString(9, propertyDetails.getExemptionReason());
				ps.setString(10, propertyDetails.getPropertyType());
				ps.setString(11, propertyDetails.getCategory());
				ps.setString(12, propertyDetails.getUsage());
				ps.setString(13, propertyDetails.getDepartment());
				ps.setString(14, propertyDetails.getApartment());
				ps.setDouble(15, getDouble(propertyDetails.getSiteLength()));
				ps.setDouble(16, getDouble(propertyDetails.getSiteBreadth()));
				ps.setDouble(17, getDouble(propertyDetails.getSitalArea()));
				ps.setDouble(18, getDouble(propertyDetails.getTotalBuiltupArea()));
				ps.setDouble(19, getDouble(propertyDetails.getUndividedShare()));
				ps.setLong(20, getLong(propertyDetails.getNoOfFloors()));
				ps.setBoolean(21, propertyDetails.getIsSuperStructure());
				ps.setString(22, propertyDetails.getLandOwner());
				ps.setString(23, propertyDetails.getFloorType());
				ps.setString(24, propertyDetails.getWoodType());
				ps.setString(25, propertyDetails.getRoofType());
				ps.setString(26, propertyDetails.getWallType());
				ps.setString(27, propertyDetails.getStateId());
				ps.setString(28, propertyDetails.getApplicationNo());
				ps.setString(29, propertyDetails.getAuditDetails().getCreatedBy());
				ps.setString(30, propertyDetails.getAuditDetails().getLastModifiedBy());
				ps.setLong(31, getLong(propertyDetails.getAuditDetails().getCreatedTime()));
				ps.setLong(32, getLong(propertyDetails.getAuditDetails().getLastModifiedTime()));
				ps.setLong(33, getLong(propertyId));
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(propertyDetails.getTaxCalculations());
				ps.setObject(34, jsonObject);
				Gson gson = new Gson();
				PGobject factorsObject = new PGobject();
				factorsObject.setType("jsonb");
				factorsObject.setValue(gson.toJson(propertyDetails.getFactors()));
				ps.setObject(35, factorsObject);
				PGobject assessmentDatesObject = new PGobject();
				assessmentDatesObject.setType("jsonb");
				assessmentDatesObject.setValue(gson.toJson(propertyDetails.getAssessmentDates()));
				ps.setObject(36, assessmentDatesObject);
				PGobject builderDetailsObject = new PGobject();
				builderDetailsObject.setType("jsonb");
				builderDetailsObject.setValue(gson.toJson(propertyDetails.getBuilderDetails()));
				ps.setObject(37, builderDetailsObject);
				ps.setString(38, propertyDetails.getBpaNo());
				ps.setTimestamp(39, TimeStampUtil.getTimeStamp(propertyDetails.getBpaDate()));
				ps.setString(40, propertyDetails.getSubUsage());

				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holderPropertyDetails = new GeneratedKeyHolder();

		jdbcTemplate.update(pscPropertyDetails, holderPropertyDetails);

		Long propertyDetailsId = Long.parseLong(String.valueOf(holderPropertyDetails.getKey().intValue()).trim());

		return propertyDetailsId;

	}

	/**
	 * Floor creation query
	 * 
	 * @param floor
	 * @param propertyDetailsId
	 * @return key holder id
	 */
	public Long saveFloor(Floor floor, Long propertyDetailsId) {

		final PreparedStatementCreator pscFloor = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(FloorBuilder.INSERT_FLOOR_QUERY,
						new String[] { "id" });
				ps.setString(1, floor.getFloorNo());
				ps.setString(2, floor.getAuditDetails().getCreatedBy());
				ps.setString(3, floor.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(floor.getAuditDetails().getCreatedTime()));
				ps.setLong(5, getLong(floor.getAuditDetails().getLastModifiedTime()));
				ps.setLong(6, getLong(propertyDetailsId));
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holderFloor = new GeneratedKeyHolder();

		jdbcTemplate.update(pscFloor, holderFloor);

		Long floorId = Long.parseLong(String.valueOf(holderFloor.getKey().intValue()).trim());

		return floorId;

	}

	/**
	 * unit creation query
	 * 
	 * @param unit
	 * @param floorId
	 */
	public Long saveUnit(Unit unit, Long floorId) {

		if (unit.getIsAuthorised() == null) {
			unit.setIsAuthorised(true);
		}
		final PreparedStatementCreator pscUnit = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(UnitBuilder.INSERT_UNIT_QUERY,
						new String[] { "id" });
				ps.setString(1, unit.getUnitNo());
				ps.setString(2, unit.getUnitType().toString());
				ps.setDouble(3, getDouble(unit.getLength()));
				ps.setDouble(4, getDouble(unit.getWidth()));
				ps.setDouble(5, getDouble(unit.getBuiltupArea()));
				ps.setDouble(6, getDouble(unit.getAssessableArea()));
				ps.setDouble(7, getDouble(unit.getBpaBuiltupArea()));
				ps.setString(8, unit.getBpaNo());
				ps.setTimestamp(9, TimeStampUtil.getTimeStamp(unit.getBpaDate()));
				ps.setString(10, unit.getUsage());
				ps.setString(11, unit.getOccupancyType());
				ps.setString(12, unit.getOccupierName());
				ps.setString(13, unit.getFirmName());
				ps.setDouble(14, getDouble(unit.getRentCollected()));
				ps.setString(15, unit.getStructure());
				ps.setString(16, unit.getAge());
				ps.setString(17, unit.getExemptionReason());
				ps.setBoolean(18, unit.getIsStructured());
				ps.setTimestamp(19, TimeStampUtil.getTimeStamp(unit.getOccupancyDate()));
				ps.setTimestamp(20, TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()));
				ps.setDouble(21, getDouble(unit.getManualArv()));
				ps.setDouble(22, getDouble(unit.getArv()));
				ps.setString(23, unit.getElectricMeterNo());
				ps.setString(24, unit.getWaterMeterNo());
				ps.setString(25, unit.getAuditDetails().getCreatedBy());
				ps.setString(26, unit.getAuditDetails().getLastModifiedBy());
				ps.setLong(27, getLong(unit.getAuditDetails().getCreatedTime()));
				ps.setLong(28, getLong(unit.getAuditDetails().getLastModifiedTime()));
				ps.setLong(29, getLong(floorId));
				ps.setBoolean(30, unit.getIsAuthorised());
				ps.setTimestamp(31, TimeStampUtil.getTimeStamp(unit.getConstructionStartDate()));
				ps.setDouble(32, getDouble(unit.getLandCost()));
				ps.setDouble(33, getDouble(unit.getBuildingCost()));
				ps.setString(34, unit.getSubUsage());
				ps.setDouble(35, getDouble(unit.getCarpetArea()));
				ps.setDouble(36, getDouble(unit.getExemptionArea()));
				ps.setDouble(37, getDouble(unit.getRv()));
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holderUnit = new GeneratedKeyHolder();

		jdbcTemplate.update(pscUnit, holderUnit);

		Long unitId = Long.parseLong(String.valueOf(holderUnit.getKey().intValue()).trim());

		return unitId;

	}

	/**
	 * room creation query
	 * 
	 * @param unit
	 * @param floorId
	 */
	public void saveRoom(Unit unit, Long floorId, Long parent) {

		if (unit.getIsAuthorised() == null) {
			unit.setIsAuthorised(true);
		}

		Object[] roomArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getCreatedBy(),
				unit.getAuditDetails().getLastModifiedBy(), unit.getAuditDetails().getCreatedTime(),
				unit.getAuditDetails().getLastModifiedTime(), floorId, getLong(parent), unit.getIsAuthorised(),
				unit.getConstructionStartDate(), unit.getLandCost(), unit.getBuildingCost(), unit.getSubUsage(),
				unit.getCarpetArea(), unit.getExemptionArea(), getDouble(unit.getRv()) };

		jdbcTemplate.update(UnitBuilder.INSERT_ROOM_QUERY, roomArgs);

	}

	/**
	 * document creation query
	 * 
	 * @param document
	 * @param propertyDetailsId
	 * @return key holder id
	 */
	public Long saveDocument(Document document, Long propertyDetailsId) {

		final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(DocumentBuilder.INSERT_DOCUMENT_QUERY,
						new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getAuditDetails().getCreatedBy());
				ps.setString(3, document.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(document.getAuditDetails().getCreatedTime()));
				ps.setLong(5, getLong(document.getAuditDetails().getLastModifiedTime()));
				ps.setLong(6, getLong(propertyDetailsId));
				ps.setString(7, document.getDocumentType());
				return ps;
			}
		};

		final KeyHolder holderDocument = new GeneratedKeyHolder();

		jdbcTemplate.update(pscDocument, holderDocument);

		Long documentId = Long.parseLong(String.valueOf(holderDocument.getKey().intValue()).trim());

		return documentId;
	}

	public Long getLong(Long value) {

		if (value == null)
			return 0l;
		else
			return value;
	}

	public Double getDouble(Double value) {

		if (value == null)
			return 0.0;
		else
			return value;
	}

	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	public Integer getInteger(Integer value) {

		if (value == null)
			return 0;
		else
			return value;
	}

	public Integer getInteger(Object value) {

		if (value == null)
			return null;
		else
			return Integer.valueOf(value.toString());
	}

	/**
	 * VacantLandDetail creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveVacantLandDetail(Property property, Long propertyId) {

		VacantLandDetail vacantLand = property.getVacantLand();

		Object[] vaccantLandArgs = { vacantLand.getSurveyNumber(), vacantLand.getPattaNumber(),
				vacantLand.getMarketValue(), vacantLand.getCapitalValue(), vacantLand.getLayoutApprovedAuth(),
				vacantLand.getLayoutPermissionNo(), TimeStampUtil.getTimeStamp(vacantLand.getLayoutPermissionDate()),
				vacantLand.getResdPlotArea(), vacantLand.getNonResdPlotArea(),
				vacantLand.getAuditDetails().getCreatedBy(), vacantLand.getAuditDetails().getLastModifiedBy(),
				vacantLand.getAuditDetails().getCreatedTime(), vacantLand.getAuditDetails().getLastModifiedTime(),
				propertyId };

		jdbcTemplate.update(VacantLandDetailBuilder.INSERT_VACANTLANDDETAIL_QUERY, vaccantLandArgs);

	}

	/**
	 * Boundary creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveBoundary(Property property, Long propertyId) {

		PropertyLocation boundary = property.getBoundary();
		String revenueBoundaryCode = null;
		String locationBoundaryCode = null;
		String adminBoundaryCode = null;
		String guidanceBoundaryCode = null;
		if (!StringUtils.isEmpty(boundary.getRevenueBoundary())) {
			revenueBoundaryCode = boundary.getRevenueBoundary().getCode();
		}
		if (!StringUtils.isEmpty(boundary.getLocationBoundary())) {
			locationBoundaryCode = boundary.getLocationBoundary().getCode();
		}
		if (!StringUtils.isEmpty(boundary.getAdminBoundary())) {
			adminBoundaryCode = boundary.getAdminBoundary().getCode();
		}
		if (!StringUtils.isEmpty(boundary.getGuidanceValueBoundary())) {
			guidanceBoundaryCode = boundary.getGuidanceValueBoundary();
		}

		Object[] boundaryArgs = { revenueBoundaryCode, locationBoundaryCode, adminBoundaryCode,
				boundary.getNorthBoundedBy(), boundary.getEastBoundedBy(), boundary.getWestBoundedBy(),
				boundary.getSouthBoundedBy(), boundary.getAuditDetails().getCreatedBy(),
				boundary.getAuditDetails().getLastModifiedBy(), boundary.getAuditDetails().getCreatedTime(),
				boundary.getAuditDetails().getLastModifiedTime(), propertyId, guidanceBoundaryCode };

		jdbcTemplate.update(BoundaryBuilder.INSERT_BOUNDARY_QUERY, boundaryArgs);

	}

	/**
	 * User property query
	 * 
	 * @param owner
	 * @param propertyId
	 */
	public void saveUser(User owner, Long propertyId) {

		Object[] userPropertyArgs = { propertyId, owner.getId(), owner.getIsPrimaryOwner(), owner.getIsSecondaryOwner(),
				owner.getOwnerShipPercentage(), owner.getOwnerType(), owner.getAuditDetails().getCreatedBy(),
				owner.getAuditDetails().getLastModifiedBy(), owner.getAuditDetails().getCreatedTime(),
				owner.getAuditDetails().getLastModifiedTime() };

		jdbcTemplate.update(UserBuilder.INSERT_USER_QUERY, userPropertyArgs);
	}

	public Map<String, Object> searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			Integer pageSize, Integer pageNumber, String[] sort, String oldUpicNo, String mobileNumber,
			String aadhaarNumber, String houseNoBldgApt, String revenueZone, String revenueWard, String locality,
			String ownerName, Double demandFrom, Double demandTo, String propertyId, String applicationNo, String usage,
			String adminBoundary, String oldestUpicNo) throws Exception {

		Map<String, Object> searchPropertyMap = new HashMap<>();
		List<Object> preparedStatementValues = new ArrayList<Object>();

		if ((upicNo != null || oldUpicNo != null || houseNoBldgApt != null || propertyId != null
				|| applicationNo != null || demandFrom != null || demandTo != null || revenueZone != null
				|| locality != null || usage != null || adminBoundary != null || oldestUpicNo != null)) {

			List<Property> properties = getPropertyByUpic(upicNo, oldUpicNo, houseNoBldgApt, propertyId, tenantId,
					pageNumber, pageSize, requestInfo, applicationNo, demandFrom, demandTo, revenueZone, locality,
					usage, adminBoundary, oldestUpicNo);
			List<Property> nonMatchingProperties = new ArrayList<Property>();
			for (Property property : properties) {

				if (mobileNumber != null || aadhaarNumber != null || ownerName != null) {
					if (mobileNumber != null && aadhaarNumber != null && ownerName != null) {
						if (!mobileNumber.isEmpty() && !aadhaarNumber.isEmpty() && !ownerName.isEmpty()) {
							int count = property.getOwners().stream()
									.filter(User -> User.getMobileNumber().equalsIgnoreCase(mobileNumber)
											&& User.getAadhaarNumber().equalsIgnoreCase(aadhaarNumber)
											&& User.getName().contains(ownerName))
									.collect(Collectors.toList()).size();
							if (count == 0) {
								nonMatchingProperties.add(property);
							}
						}
					} else if (mobileNumber != null && aadhaarNumber != null && ownerName == null) {
						if (!mobileNumber.isEmpty() && !aadhaarNumber.isEmpty()) {
							int count = property.getOwners().stream()
									.filter(User -> User.getMobileNumber().equalsIgnoreCase(mobileNumber)
											&& User.getAadhaarNumber().equalsIgnoreCase(aadhaarNumber)
											&& User.getName().contains(ownerName))
									.collect(Collectors.toList()).size();
							if (count == 0) {
								nonMatchingProperties.add(property);
							}
						}
					} else if (mobileNumber != null && aadhaarNumber == null && ownerName != null) {
						if (!mobileNumber.isEmpty() && !ownerName.isEmpty()) {
							int count = property.getOwners().stream()
									.filter(User -> User.getMobileNumber().equalsIgnoreCase(mobileNumber)
											&& User.getAadhaarNumber().equalsIgnoreCase(aadhaarNumber)
											&& User.getName().contains(ownerName))
									.collect(Collectors.toList()).size();
							if (count == 0) {
								nonMatchingProperties.add(property);
							}
						}
					}

					else if (mobileNumber == null && aadhaarNumber != null && ownerName != null) {
						if (!aadhaarNumber.isEmpty() && !ownerName.isEmpty()) {
							int count = property.getOwners().stream()
									.filter(User -> User.getMobileNumber().equalsIgnoreCase(mobileNumber)
											&& User.getAadhaarNumber().equalsIgnoreCase(aadhaarNumber)
											&& User.getName().contains(ownerName))
									.collect(Collectors.toList()).size();
							if (count == 0) {
								nonMatchingProperties.add(property);
							}
						}
					} else if (mobileNumber != null) {
						if (!mobileNumber.isEmpty()) {
							int count = property.getOwners().stream()
									.filter(User -> User.getMobileNumber().equalsIgnoreCase(mobileNumber))
									.collect(Collectors.toList()).size();
							if (count == 0) {
								nonMatchingProperties.add(property);
							}
						}

					} else if (aadhaarNumber != null) {
						if (!aadhaarNumber.isEmpty()) {
							int count = property.getOwners().stream()
									.filter(User -> User.getAadhaarNumber().equalsIgnoreCase(aadhaarNumber))
									.collect(Collectors.toList()).size();
							if (count == 0) {
								nonMatchingProperties.add(property);
							}
						}

					} else if (ownerName != null) {
						if (!ownerName.isEmpty()) {
							int count = property.getOwners().stream().filter(User -> User.getName().contains(ownerName))
									.collect(Collectors.toList()).size();
							if (count == 0) {
								nonMatchingProperties.add(property);
							}
						}
					}
				}

			}

			if (nonMatchingProperties.size() > 0)

			{
				properties.removeAll(nonMatchingProperties);
			}
			searchPropertyMap.put("properties", properties);
			searchPropertyMap.put("users", null);

		} else {

			Map<String, Object> propertyMap = searchPropertyBuilder.createSearchPropertyQuery(requestInfo, tenantId,
					active, upicNo, pageSize, pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt,
					revenueZone, revenueWard, locality, ownerName, propertyId, applicationNo, preparedStatementValues);
			List<Property> properties = getProperty(propertyMap.get("Sql").toString(), preparedStatementValues);
			if (propertyMap.get("users") != null)

			{
				searchPropertyMap.put("properties", properties);
				searchPropertyMap.put("users", propertyMap.get("users"));
			} else

			{
				searchPropertyMap.put("properties", null);
				searchPropertyMap.put("users", null);
			}

		}

		return searchPropertyMap;

	}

	private List<Property> getPropertyByUpic(String upicNo, String oldUpicNo, String houseNoBldgApt, String propertyId,
			String tenantId, Integer pageSize, Integer pageNumber, RequestInfo requestInfo, String applicationNo,
			Double demandFrom, Double demandTo, String revenueZone, String locality, String usage, String adminBoundary,
			String oldestUpicNo) throws Exception {

		List<Object> preparedStatementvalues = new ArrayList<>();

		String query = searchPropertyBuilder.getPropertyByUpic(upicNo, oldUpicNo, houseNoBldgApt, propertyId, tenantId,
				preparedStatementvalues, pageSize, pageNumber, applicationNo, demandFrom, demandTo, requestInfo,
				revenueZone, locality, usage, adminBoundary, oldestUpicNo);

		List<Property> properties = getProperty(query, preparedStatementvalues);
		properties.forEach(property -> {

			Address address = getAddressByProperty(property.getId());
			property.setAddress(address);

			property.setOwners(getOwnersByproperty(property.getId(), property.getTenantId(), requestInfo));
		});

		return properties;

	}

	private List<User> getOwnersByproperty(Long propertyId, String tenantId, RequestInfo requestInfo) {

		List<Object> preparedStatementvalues = new ArrayList<>();
		String ownersQuery = SearchPropertyBuilder.getOwnersByproperty(propertyId, preparedStatementvalues);
		List<User> owners = null;

		owners = jdbcTemplate.query(ownersQuery, preparedStatementvalues.toArray(),
				new BeanPropertyRowMapper(User.class));

		List<User> users = new ArrayList<>();
		owners.forEach(owner -> {
			StringBuffer userSearchUrl = new StringBuffer();
			userSearchUrl.append(propertiesManager.getUserHostname());
			userSearchUrl.append(propertiesManager.getUserBasepath());
			userSearchUrl.append(propertiesManager.getUserSearchpath());
			Map<String, Object> userSearchRequestInfo = new HashMap<>();
			List<Long> userIds = new ArrayList<Long>();
			userIds.add(owner.getOwner());
			userSearchRequestInfo.put("tenantId", tenantId);
			userSearchRequestInfo.put("id", userIds);
			userSearchRequestInfo.put("RequestInfo", requestInfo);

			logger.info("PropertyRepository  userSearchRequestInfo :" + userSearchRequestInfo);
			logger.info("PropertyRepository  userSearchUrl :" + userSearchUrl);

			UserResponseInfo userResponse = restTemplate.postForObject(userSearchUrl.toString(), userSearchRequestInfo,
					UserResponseInfo.class);
			logger.info("PropertyRepository UserResponseInfo :" + userResponse);

			userResponse.getUser().forEach(user -> {
				user.setIsPrimaryOwner(owner.getIsPrimaryOwner());
				user.setIsSecondaryOwner(owner.getIsSecondaryOwner());
				user.setOwnerShipPercentage(owner.getOwnerShipPercentage());
				user.setOwnerType(owner.getOwnerType());
				user.setOwner(owner.getOwner());
				user.setAuditDetails(getAuditDetailsForOwner(owner.getId()));
				users.add(user);
			});

		});

		return users;

	}

	public Address getAddressByProperty(Long propertyId) {
		Address address = null;
		try {
			address = (Address) jdbcTemplate.queryForObject(AddressBuilder.ADDRES_BY_PROPERTY_ID_QUERY,
					new Object[] { propertyId }, new BeanPropertyRowMapper(Address.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (address != null && address.getId() != null && address.getId() > 0)
			address.setAuditDetails(getAuditDetailsForAddress(address.getId()));

		return address;
	}

	public List<User> getPropertyUserByProperty(Long propertyId) {
		List<User> propertyUsers = null;
		try {

			propertyUsers = jdbcTemplate.query(UserBuilder.PROPERTY_OWNER_BY_PROPERTY_ID_QUERY,
					new Object[] { propertyId }, new BeanPropertyRowMapper(User.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		return propertyUsers;

	}

	public User getPropertyUserByUser(Long userId) {
		User propertyUser = null;
		try {
			propertyUser = (User) jdbcTemplate.queryForObject(UserBuilder.PROPERTY_OWNER_BY_USER_ID_QUERY,
					new Object[] { userId }, new BeanPropertyRowMapper(User.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return propertyUser;

	}

	public PropertyDetail getPropertyDetailsByProperty(Long propertyId) {
		PropertyDetail propertyDetail = null;
		try {

			propertyDetail = getPropertyDetails(propertyId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		return propertyDetail;

	}

	private PropertyDetail getPropertyDetails(Long propertyId) {
		List<Map<String, Object>> rows = jdbcTemplate
				.queryForList(PropertyDetailBuilder.PROPERTY_DETAIL_BY_PROPERTY_QUERY, new Object[] { propertyId });

		PropertyDetail propertyDetail = new PropertyDetail();
		for (Map<String, Object> row : rows) {

			try {
				propertyDetail.setId(getLong(row.get("id")));
				if (row.get("source") != null)
					propertyDetail.setSource(SourceEnum.valueOf(getString(row.get("source"))));
				propertyDetail.setRegdDocNo(getString(row.get("regddocno")));
				String regdDocDate = getString(row.get("regddocdate"));
				if (regdDocDate != null) {
					propertyDetail.setRegdDocDate(TimeStampUtil.getDateFormat(regdDocDate));
				}
				propertyDetail.setReason(getString(row.get("reason")));
				if (row.get("status") != null)
					propertyDetail.setStatus(StatusEnum.valueOf(getString(row.get("status"))));
				propertyDetail.setIsVerified(getBoolean(row.get("isverified")));
				String verificationDate = getString(row.get("verificationdate"));
				if (verificationDate != null) {
					propertyDetail.setVerificationDate(TimeStampUtil.getDateFormat(verificationDate));
				}
				propertyDetail.setIsExempted(getBoolean(row.get("isexempted")));
				propertyDetail.setExemptionReason(getString(row.get("exemptionreason")));
				propertyDetail.setPropertyType(getString(row.get("propertytype")));
				propertyDetail.setCategory(getString(row.get("category")));
				propertyDetail.setUsage(getString(row.get("usage")));
				propertyDetail.setDepartment(getString(row.get("department")));
				propertyDetail.setApartment(getString(row.get("apartment")));
				propertyDetail.setSiteLength(getDouble(row.get("sitelength")));
				propertyDetail.setSiteBreadth(getDouble(row.get("sitebreadth")));
				propertyDetail.setSitalArea(getDouble(row.get("sitalarea")));
				propertyDetail.setTotalBuiltupArea(getDouble(row.get("totalbuiltuparea")));
				propertyDetail.setUndividedShare(getDouble(row.get("undividedshare")));
				propertyDetail.setNoOfFloors(getLong(row.get("nooffloors")));
				propertyDetail.setIsSuperStructure(getBoolean(row.get("issuperstructure")));
				propertyDetail.setLandOwner(getString(row.get("landowner")));
				propertyDetail.setFloorType(getString(row.get("floortype")));
				propertyDetail.setWoodType(getString(row.get("woodtype")));
				propertyDetail.setRoofType(getString(row.get("rooftype")));
				propertyDetail.setWallType(getString(row.get("walltype")));
				propertyDetail.setStateId(getString(row.get("stateid")));
				propertyDetail.setApplicationNo(getString(row.get("applicationno")));

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(getString(row.get("createdby")));
				auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
				auditDetails.setCreatedTime(getLong(row.get("createdtime")));
				auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));

				propertyDetail.setAuditDetails(auditDetails);

				propertyDetail.setTaxCalculations(getString(row.get("taxcalculations")));

				if (row.get("assessmentdates") != null) {
					List<AssessmentDate> assessmentDates = new ArrayList<>();
					TypeReference<List<AssessmentDate>> typeReference = new TypeReference<List<AssessmentDate>>() {
					};
					assessmentDates = new ObjectMapper().readValue(row.get("assessmentdates").toString(),
							typeReference);

					propertyDetail.setAssessmentDates(assessmentDates);
				} else {
					List<AssessmentDate> assessmentDates = new ArrayList<AssessmentDate>();
					propertyDetail.setAssessmentDates(assessmentDates);
				}

				if (row.get("factors") != null) {
					List<Factors> factors = new ArrayList<>();
					TypeReference<List<Factors>> typeReference = new TypeReference<List<Factors>>() {
					};
					factors = new ObjectMapper().readValue(row.get("factors").toString(), typeReference);

					propertyDetail.setFactors(factors);
				} else {
					List<Factors> factors = new ArrayList<>();
					propertyDetail.setFactors(factors);
				}

				if (row.get("builderdetails") != null) {
					BuilderDetail builderDetail = new ObjectMapper().readValue(row.get("builderdetails").toString(),
							BuilderDetail.class);
					if (builderDetail != null) {
						if (builderDetail.getCertificateCompletionDate() != null) {
							builderDetail.setCertificateCompletionDate(
									getString(builderDetail.getCertificateCompletionDate()));
						}
						if (builderDetail.getCertificateReceiveDate() != null) {
							builderDetail
									.setCertificateReceiveDate(getString(builderDetail.getCertificateReceiveDate()));
						}
						propertyDetail.setBuilderDetails(builderDetail);
					}

				} else {
					BuilderDetail builderDetail = new BuilderDetail();
					propertyDetail.setBuilderDetails(builderDetail);

				}

				propertyDetail.setBpaNo(getString(row.get("bpano")));
				String bpaDate = getString(row.get("bpadate"));
				if (bpaDate != null) {
					propertyDetail.setBpaDate(TimeStampUtil.getDateFormat(bpaDate));
				}

				propertyDetail.setSubUsage(getString(row.get("subUsage")));
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		return propertyDetail;

	}

	public VacantLandDetail getVacantLandByProperty(Long propertyId) {

		VacantLandDetail vacantLandDetail = null;
		try {
			vacantLandDetail = (VacantLandDetail) jdbcTemplate.queryForObject(
					VacantLandDetailBuilder.VACANT_LAND_BY_PROPERTY_QUERY, new Object[] { propertyId },
					new BeanPropertyRowMapper(VacantLandDetail.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (vacantLandDetail != null && vacantLandDetail.getId() != null && vacantLandDetail.getId() > 0)
			vacantLandDetail.setAuditDetails(getAuditDetailsForvacantLandDetail(vacantLandDetail.getId()));

		return vacantLandDetail;
	}

	public PropertyLocation getPropertyLocationByproperty(Long propertyId) {
		PropertyLocation propertyLocation = null;
		try {
			propertyLocation = (PropertyLocation) jdbcTemplate.queryForObject(
					BoundaryBuilder.PROPERTY_LOCATION_BY_PROPERTY_QUERY, new Object[] { propertyId },
					new PropertyLocationRowMapper());
		} catch (EmptyResultDataAccessException e1) {
			return null;
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (propertyLocation != null && propertyLocation.getId() != null && propertyLocation.getId() > 0)
			propertyLocation.setAuditDetails(getAuditDetailsForBoundary(propertyLocation.getId()));

		return propertyLocation;
	}

	public List<Floor> getFloorsByPropertyDetails(Long propertyDetailId) {
		List<Floor> floors = null;
		try {
			floors = jdbcTemplate.query(FloorBuilder.FLOORS_BY_PROPERTY_DETAILS_QUERY,
					new Object[] { propertyDetailId }, new BeanPropertyRowMapper(Floor.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		floors.forEach(floor -> {
			if (floor.getId() != null && floor.getId() > 0) {
				floor.setAuditDetails(getAuditDetailsForFloor(floor.getId()));
			}
		});

		return floors;

	}

	public List<Unit> getUnitsByFloor(Long floorId) {
		List<Unit> units = null;
		try {
			units = jdbcTemplate.query(UnitBuilder.UNITS_BY_FLOOR_QUERY, new Object[] { floorId },
					new BeanPropertyRowMapper(Unit.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		units.forEach(unit -> {
			if (unit.getId() != null && unit.getId() > 0)
				unit.setAuditDetails(getAuditDetailsForUnit(unit.getId()));
		});

		return units;

	}

	public List<Document> getDocumentByPropertyDetails(Long propertyDetailId) {
		List<Map<String, Object>> rows = null;
		try {
			rows = jdbcTemplate.queryForList(DocumentBuilder.DOCUMENT_BY_PROPERTY_DETAILS_QUERY,
					new Object[] { propertyDetailId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		List<Document> documents = getDocumentObject(rows);
		documents.forEach(document -> {
			if (document.getId() != null && document.getId() > 0)
				document.setAuditDetails(getAuditDetailsForDocument(document.getId()));
		});

		return documents;
	}

	public AuditDetails getAuditForPropertyDetails(Long propertyId) {
		AuditDetails auditDetails = null;
		try {
			auditDetails = (AuditDetails) jdbcTemplate.queryForObject(
					PropertyBuilder.AUDIT_DETAILS_FOR_PROPERTY_DETAILS, new Object[] { propertyId },
					new BeanPropertyRowMapper(AuditDetails.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		return auditDetails;
	}

	private List<Document> getDocumentObject(List<Map<String, Object>> documentList) {

		List<Document> documents = new ArrayList<>();

		for (Map<String, Object> documentdata : documentList) {
			Document document = new Document();
			if (documentdata.get("documenttype") != null)
				document.setDocumentType(documentdata.get("documenttype").toString());
			if (documentdata.get("filestore") != null)
				document.setFileStore(documentdata.get("filestore").toString());
			if (documentdata.get("id") != null)
				document.setId(Long.valueOf(documentdata.get("id").toString()));

			documents.add(document);

		}

		return documents;

	}

	public void updateProperty(Property property) throws Exception {

		String propertyUpdate = PropertyBuilder.updatePropertyQuery();

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
		jsonObject.setValue(gson.toJson(demandIdList));

		Object[] propertyArgs = { property.getTenantId(), property.getUpicNumber(), property.getOldUpicNumber(),
				property.getVltUpicNumber(), property.getCreationReason().name(),
				TimeStampUtil.getTimeStamp(property.getAssessmentDate()),
				TimeStampUtil.getTimeStamp(property.getOccupancyDate()), property.getGisRefNo(),
				property.getIsAuthorised(), property.getIsUnderWorkflow(), property.getChannel().name(),
				property.getAuditDetails().getLastModifiedBy(), property.getAuditDetails().getLastModifiedTime(),
				jsonObject, property.getSequenceNo(), property.getOldestUpicNumber(), property.getId() };

		jdbcTemplate.update(propertyUpdate, propertyArgs);

	}

	public void updateAddress(Address address, Long address_id, Long proertyId) {

		String addressUpdate = AddressBuilder.updatePropertyAddressQuery();

		Object[] addressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressNumber(), address.getAddressLine1(), address.getAddressLine2(), address.getLandmark(),
				address.getCity(), address.getPincode(), address.getDetail(),
				address.getAuditDetails().getLastModifiedBy(), address.getAuditDetails().getLastModifiedTime(),
				proertyId, address.getSurveyNo(), address.getPlotNo(), address_id };

		jdbcTemplate.update(addressUpdate, addressArgs);

	}

	public void updatePropertyDetail(PropertyDetail propertyDetails, Long proertyId) throws Exception {

		String propertyDetailsUpdate = PropertyDetailBuilder.updatePropertyDetailQuery();

		PGobject jsonObject = new PGobject();
		jsonObject.setType("jsonb");
		jsonObject.setValue(propertyDetails.getTaxCalculations());

		Gson gson = new Gson();
		PGobject factorsObject = null;
		PGobject assessmentDatesObject = null;
		PGobject builderDetailsObject = null;

		factorsObject = new PGobject();
		factorsObject.setType("jsonb");
		factorsObject.setValue(gson.toJson(propertyDetails.getFactors()));

		assessmentDatesObject = new PGobject();
		assessmentDatesObject.setType("jsonb");
		assessmentDatesObject.setValue(gson.toJson(propertyDetails.getAssessmentDates()));

		builderDetailsObject = new PGobject();
		builderDetailsObject.setType("jsonb");
		builderDetailsObject.setValue(gson.toJson(propertyDetails.getBuilderDetails()));

		Object[] propertyDetailsArgs = { propertyDetails.getSource().toString(), propertyDetails.getRegdDocNo(),
				TimeStampUtil.getTimeStamp(propertyDetails.getRegdDocDate()), propertyDetails.getReason(),
				propertyDetails.getStatus().toString(), propertyDetails.getIsVerified(),
				TimeStampUtil.getTimeStamp(propertyDetails.getVerificationDate()), propertyDetails.getIsExempted(),
				propertyDetails.getExemptionReason(), propertyDetails.getPropertyType(), propertyDetails.getCategory(),
				propertyDetails.getUsage(), propertyDetails.getDepartment(), propertyDetails.getApartment(),
				propertyDetails.getSiteLength(), propertyDetails.getSiteBreadth(), propertyDetails.getSitalArea(),
				propertyDetails.getTotalBuiltupArea(), propertyDetails.getUndividedShare(),
				propertyDetails.getNoOfFloors(), propertyDetails.getIsSuperStructure(), propertyDetails.getLandOwner(),
				propertyDetails.getFloorType(), propertyDetails.getWoodType(), propertyDetails.getRoofType(),
				propertyDetails.getWallType(), propertyDetails.getStateId(), propertyDetails.getApplicationNo(),
				propertyDetails.getAuditDetails().getLastModifiedBy(),
				propertyDetails.getAuditDetails().getLastModifiedTime(), proertyId, jsonObject, factorsObject,
				assessmentDatesObject, builderDetailsObject, propertyDetails.getBpaNo(),
				TimeStampUtil.getTimeStamp(propertyDetails.getBpaDate()), propertyDetails.getSubUsage(),
				propertyDetails.getId() };

		jdbcTemplate.update(propertyDetailsUpdate, propertyDetailsArgs);
	}

	public void updateVacantLandDetail(VacantLandDetail vacantLand, Long vacantland_id, Long proertyId) {

		String vacantlandUpdate = VacantLandDetailBuilder.updateVacantLandQuery();

		Object[] vaccantLandArgs = { vacantLand.getSurveyNumber(), vacantLand.getPattaNumber(),
				vacantLand.getMarketValue(), vacantLand.getCapitalValue(), vacantLand.getLayoutApprovedAuth(),
				vacantLand.getLayoutPermissionNo(), TimeStampUtil.getTimeStamp(vacantLand.getLayoutPermissionDate()),
				vacantLand.getResdPlotArea(), vacantLand.getNonResdPlotArea(),
				vacantLand.getAuditDetails().getLastModifiedBy(), vacantLand.getAuditDetails().getLastModifiedTime(),
				proertyId, vacantland_id };

		jdbcTemplate.update(vacantlandUpdate, vaccantLandArgs);
	}

	public void updateFloor(Floor floor, Long propertyDetail_id) {

		String floorUpdate = FloorBuilder.updateFloorQuery();

		Object[] floorArgs = { floor.getFloorNo(), floor.getAuditDetails().getLastModifiedBy(),
				floor.getAuditDetails().getLastModifiedTime(), propertyDetail_id, floor.getId() };

		jdbcTemplate.update(floorUpdate, floorArgs);

	}

	/**
	 * Description: updating unit
	 * 
	 * @param unit
	 */
	public void updateUnit(Unit unit) {

		String unitUpdate = UnitBuilder.updateUnitQuery();

		Object[] unitArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getLastModifiedBy(),
				unit.getAuditDetails().getLastModifiedTime(), unit.getParentId(), unit.getIsAuthorised(),
				TimeStampUtil.getTimeStamp(unit.getConstructionStartDate()), unit.getLandCost(), unit.getBuildingCost(),
				unit.getSubUsage(), unit.getCarpetArea(), unit.getExemptionArea(), getDouble(unit.getRv()),
				unit.getId() };

		jdbcTemplate.update(unitUpdate, unitArgs);

	}

	/**
	 * Description: updating room
	 * 
	 * @param unit
	 */
	public void updateRoom(Unit unit) {

		String roomUpdate = UnitBuilder.updateRoomQuery();

		Object[] roomArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getLastModifiedBy(),
				unit.getAuditDetails().getLastModifiedTime(), unit.getParentId(), unit.getIsAuthorised(),
				unit.getConstructionStartDate(), unit.getLandCost(), unit.getBuildingCost(), unit.getSubUsage(),
				unit.getCarpetArea(), unit.getExemptionArea(), getDouble(unit.getRv()), unit.getId() };

		jdbcTemplate.update(roomUpdate, roomArgs);

	}

	public void updateDocument(Document document, Long propertyDetailsId) {

		String documentUpdate = DocumentBuilder.updateDocumentQuery();

		Object[] documentArgs = { document.getFileStore(), document.getAuditDetails().getLastModifiedBy(),
				document.getAuditDetails().getLastModifiedTime(), propertyDetailsId, document.getDocumentType(),
				document.getId() };

		jdbcTemplate.update(documentUpdate, documentArgs);
	}

	public void updateUser(User owner, Long propertyId) {

		String userUpdate = UserBuilder.updateOwnerQuery();

		Object[] userPropertyArgs = { propertyId, owner.getId(), owner.getIsPrimaryOwner().booleanValue(),
				owner.getIsSecondaryOwner().booleanValue(), owner.getOwnerShipPercentage(), owner.getOwnerType(),
				owner.getAuditDetails().getLastModifiedBy(), owner.getAuditDetails().getLastModifiedTime(),
				owner.getId(), propertyId };

		jdbcTemplate.update(userUpdate, userPropertyArgs);

	}

	public void updateBoundary(PropertyLocation boundary, Long propertId) {

		String boundaryUpdate = BoundaryBuilder.updateBoundaryQuery();

		String revenueBoundaryCode = null;
		String locationBoundaryCode = null;
		String adminBoundaryCode = null;
		String guidanceBoundaryCode = null;
		if (!StringUtils.isEmpty(boundary.getRevenueBoundary())) {
			revenueBoundaryCode = boundary.getRevenueBoundary().getCode();
		}
		if (!StringUtils.isEmpty(boundary.getLocationBoundary())) {
			locationBoundaryCode = boundary.getLocationBoundary().getCode();
		}
		if (!StringUtils.isEmpty(boundary.getAdminBoundary())) {
			adminBoundaryCode = boundary.getAdminBoundary().getCode();
		}
		if (!StringUtils.isEmpty(boundary.getGuidanceValueBoundary())) {
			guidanceBoundaryCode = boundary.getGuidanceValueBoundary();
		}

		Object[] boundaryArgs = { revenueBoundaryCode, locationBoundaryCode, adminBoundaryCode,
				getString(boundary.getNorthBoundedBy()), getString(boundary.getEastBoundedBy()),
				getString(boundary.getWestBoundedBy()), getString(boundary.getSouthBoundedBy()),
				boundary.getAuditDetails().getLastModifiedBy(), boundary.getAuditDetails().getLastModifiedTime(),
				propertId, guidanceBoundaryCode, boundary.getId() };

		jdbcTemplate.update(boundaryUpdate, boundaryArgs);

	}

	public boolean isPropertyUnderWorkflow(String upicNo) {
		String query = PropertyBuilder.isPropertyUnderWorkflow;
		Object[] arguments = { upicNo };
		Boolean isPropertyUnderWorkflow = jdbcTemplate.queryForObject(query, arguments, Boolean.class);
		return isPropertyUnderWorkflow;
	}

	public void updateIsPropertyUnderWorkflow(String upicNo) {
		String query = PropertyBuilder.updatePropertyIsUnderWokflow;
		Object[] arguments = { true, upicNo };
		jdbcTemplate.update(query, arguments);
	}

	public void setIsPropertyUnderWorkFlowFalse(String upicNo) {
		String query = PropertyBuilder.updatePropertyIsUnderWokflow;
		Object[] arguments = { false, upicNo };
		jdbcTemplate.update(query, arguments);
	}

	/**
	 * This will give the audit details for the given addressId
	 * 
	 * @param addressId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForAddress(Long addressId) {
		String query = AddressBuilder.AUDIT_DETAILS_FOR_ADDRESS;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { addressId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This will give the audit details for the given property detail Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForPropertyDetail(Long propertyDetailId) {
		String query = PropertyDetailBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { propertyDetailId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This will give the audit details for the given vacant land detail Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForvacantLandDetail(Long vacantLandDetailId) {
		String query = VacantLandDetailBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query,
				new Object[] { vacantLandDetailId }, new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This will give the audit details for the given Boundary Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForBoundary(Long boundaryId) {
		String query = BoundaryBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { boundaryId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This will give the audit details for the given floor Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForFloor(Long floorId) {
		String query = FloorBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { floorId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This will give the audit details for the given document Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForDocument(Long documentId) {
		String query = DocumentBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { documentId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	public Long saveTitleTransfer(TitleTransfer titleTransfer) {

		final PreparedStatementCreator pscTitleTransfer = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				final PreparedStatement ps = connection
						.prepareStatement(TitleTransferBuilder.INSERT_TITLETRANSFER_QUERY, new String[] { "id" });

				ps.setString(1, titleTransfer.getTenantId());
				ps.setString(2, titleTransfer.getUpicNo());
				ps.setString(3, titleTransfer.getTransferReason());
				ps.setString(4, titleTransfer.getRegistrationDocNo());
				ps.setTimestamp(5, TimeStampUtil.getTimeStamp(titleTransfer.getRegistrationDocDate()));
				ps.setDouble(6, getDouble(titleTransfer.getDepartmentGuidelineValue()));
				ps.setDouble(7, getDouble(titleTransfer.getPartiesConsiderationValue()));
				ps.setLong(8, getLong(titleTransfer.getCourtOrderNumber()));
				ps.setString(9, titleTransfer.getSubRegOfficeName());
				ps.setDouble(10, getDouble(titleTransfer.getTitleTrasferFee()));
				ps.setString(11, titleTransfer.getStateId());
				ps.setString(12, titleTransfer.getReceiptnumber());
				ps.setTimestamp(13, TimeStampUtil.getTimeStamp(titleTransfer.getReceiptdate()));
				ps.setString(14, titleTransfer.getAuditDetails().getCreatedBy());
				ps.setString(15, titleTransfer.getAuditDetails().getLastModifiedBy());
				ps.setLong(16, getLong(titleTransfer.getAuditDetails().getCreatedTime()));
				ps.setLong(17, getLong(titleTransfer.getAuditDetails().getLastModifiedTime()));
				ps.setString(18, titleTransfer.getApplicationNo());
				ps.setString(19, titleTransfer.getDemandId());

				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder titleTransferKey = new GeneratedKeyHolder();

		jdbcTemplate.update(pscTitleTransfer, titleTransferKey);

		Long titleTransferId = Long.parseLong(String.valueOf(titleTransferKey.getKey().intValue()).trim());

		return titleTransferId;

	}

	public void saveTitleTransferUser(User owner, Long titleTransferId) {

		Object[] titleTransferUserArgs = { titleTransferId, owner.getId(), owner.getIsPrimaryOwner(),
				owner.getIsSecondaryOwner(), owner.getOwnerShipPercentage(), owner.getOwnerType(),
				owner.getAuditDetails().getCreatedBy(), owner.getAuditDetails().getLastModifiedBy(),
				owner.getAuditDetails().getCreatedTime(), owner.getAuditDetails().getLastModifiedTime() };

		jdbcTemplate.update(TitleTransferUserBuilder.INSERT_TITLETRANSFERUSER_QUERY, titleTransferUserArgs);
	}

	public Long saveTitleTransferDocument(Document document, Long titleTransferId) {

		final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(
						TitleTransferDocumentBuilder.INSERT_TITLETRANSFERDOCUMENT_QUERY, new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getAuditDetails().getCreatedBy());
				ps.setString(3, document.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(document.getAuditDetails().getCreatedTime()));
				ps.setLong(5, getLong(document.getAuditDetails().getLastModifiedTime()));
				ps.setLong(6, getLong(titleTransferId));
				ps.setString(7, document.getDocumentType());
				return ps;
			}
		};

		final KeyHolder holderTitleTransferDocument = new GeneratedKeyHolder();

		jdbcTemplate.update(pscDocument, holderTitleTransferDocument);

		Long titleTransferDocumentId = Long
				.parseLong(String.valueOf(holderTitleTransferDocument.getKey().intValue()).trim());

		return titleTransferDocumentId;
	}

	public void saveTitleTransferAddress(TitleTransfer titleTransfer, Long titleTransferId) {

		Address address = titleTransfer.getCorrespondenceAddress();

		Object[] titleTransferAddressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressNumber(), address.getAddressLine1(), address.getAddressLine2(), address.getLandmark(),
				address.getCity(), address.getPincode(), address.getDetail(), address.getAuditDetails().getCreatedBy(),
				address.getAuditDetails().getLastModifiedBy(), address.getAuditDetails().getCreatedTime(),
				address.getAuditDetails().getLastModifiedTime(), titleTransferId };

		jdbcTemplate.update(TitleTransferAddressBuilder.INSERT_TITLETRANSERADDRESS_QUERY, titleTransferAddressArgs);

	}

	public void updateTitleTransfer(TitleTransfer titleTransfer) {

		String titleTransferUpdate = TitleTransferBuilder.UPDATE_TITLETRANSFER_QUERY;

		Object[] titleTransferArgs = { titleTransfer.getStateId(), titleTransfer.getAuditDetails().getLastModifiedBy(),
				titleTransfer.getAuditDetails().getLastModifiedTime(), titleTransfer.getApplicationNo() };

		jdbcTemplate.update(titleTransferUpdate, titleTransferArgs);

	}

	/**
	 * updating isUnderWorkflow as false
	 * 
	 * @param property
	 * @throws Exception
	 */
	public void updateTitleTransferProperty(Property property) throws Exception {

		String propertyUpdate = PropertyBuilder.UPDATE_TITLETRANSFERPROPERTY_QUERY;

		Object[] propertyArgs = { false, property.getAuditDetails().getLastModifiedBy(),
				property.getAuditDetails().getLastModifiedTime(), property.getId() };

		jdbcTemplate.update(propertyUpdate, propertyArgs);

	}

	/**
	 * Title Transer PropertyDetails Udpate
	 * 
	 * @param propertyDetails
	 * @throws Exception
	 */
	@Transactional
	public void updateTitleTransferPropertyDetail(PropertyDetail propertyDetails) throws Exception {

		String propertyDetailsUpdate = PropertyDetailBuilder.UPDATE_TITLETRANSFERPROPERTYDETAIL_QUERY;

		Object[] propertyDetailsArgs = { propertyDetails.getStateId(),
				propertyDetails.getAuditDetails().getLastModifiedBy(),
				propertyDetails.getAuditDetails().getLastModifiedTime(), propertyDetails.getId() };

		jdbcTemplate.update(propertyDetailsUpdate, propertyDetailsArgs);
	}

	/**
	 * This will give the audit details for the given property Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForProperty(Long propertyId) {
		String query = PropertyBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { propertyId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This will give the audit details for the given unit Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForUnit(Long unitId) {
		String query = UnitBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { unitId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This will give the list of Properties
	 * 
	 * @param query
	 * @param preparedStatementValues
	 * @return {@link Property} List of property
	 * @throws ParseException
	 */
	private List<Property> getProperty(String query, List<Object> preparedStatementValues) throws Exception {

		List<Property> properties = new ArrayList<Property>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map<String, Object> row : rows) {
			Property property = new Property();

			property.setId(getLong(row.get("id")));
			property.setTenantId(getString(row.get("tenantid")));
			property.setSequenceNo(getInteger(row.get("sequenceNo")));
			property.setUpicNumber(getString(row.get("upicnumber")));
			property.setOldUpicNumber(getString(row.get("oldupicnumber")));
			property.setVltUpicNumber(getString(row.get("vltupicnumber")));
			if (row.get("creationreason") != null)
				property.setCreationReason(CreationReasonEnum.valueOf(row.get("creationreason").toString()));
			String assessmentDate = getString(row.get("assessmentdate"));
			if (assessmentDate != null) {
				property.setAssessmentDate(TimeStampUtil.getDateFormat(assessmentDate));
			}
			String occupancyDate = getString(row.get("occupancydate"));
			if (occupancyDate != null) {
				property.setOccupancyDate(TimeStampUtil.getDateFormat(occupancyDate));
			}
			property.setGisRefNo(getString(row.get("gisrefno")));
			property.setIsAuthorised(getBoolean(row.get("isauthorised")));
			property.setIsAuthorised(getBoolean(row.get("isunderworkflow")));
			property.setActive(getBoolean(row.get("active")));
			if (row.get("channel") != null)
				property.setChannel(ChannelEnum.valueOf(getString(row.get("channel"))));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			property.setAuditDetails(auditDetails);
			List<Demand> demands = new ArrayList<Demand>();

			if (row.get("demands") != null) {
				ObjectMapper mapper = new ObjectMapper();
				TypeReference<List<DemandId>> typeReference = new TypeReference<List<DemandId>>() {
				};
				try {
					List<DemandId> demandIds = mapper.readValue(row.get("demands").toString(), typeReference);

					for (DemandId demandId : demandIds) {
						Demand demand = new Demand();
						demand.setId(demandId.getId());
						demands.add(demand);
					}
				} catch (Exception e) {
					logger.error("error" + e);
				}
			}

			property.setDemands(demands);
			property.setOldestUpicNumber(getString(row.get("oldestupicnumber")));
			properties.add(property);

		}

		return properties;
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
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	/**
	 * * This method will cast the given object to Boolean
	 * 
	 * @param object
	 *            that need to be cast to Boolean
	 * @return {@link String}
	 */
	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : (Boolean) object;
	}

	/**
	 * This method will check whether the upic no exists in db
	 * 
	 * @param upicNo
	 * @return TRUE / FALSE if the document exists/ does not exists
	 */
	public Boolean checkUpicnoExixts(String upicNo) {

		Boolean isExists = Boolean.FALSE;
		int count = 0;

		String query = SpecialNoticeBuilder.UNIQUE_UPIC_NO;
		count = jdbcTemplate.queryForObject(query, new Object[] { upicNo }, Integer.class);
		if (count > 0)
			isExists = Boolean.TRUE;

		return isExists;
	}

	/**
	 * This will get the upic no
	 * 
	 * @param upicNo
	 * @return {@link String} Upic No
	 */
	public String getNoticeNumberForupic(String upicNo) {

		String noticeNumber = "";
		String query = SpecialNoticeBuilder.GET_NOTICE_NUMBER;
		noticeNumber = jdbcTemplate.queryForObject(query, new Object[] { upicNo }, String.class);

		return noticeNumber;
	}

	/**
	 * This will save the notice object in the repository
	 * 
	 * @param notice
	 * @param id
	 * @param totalTax
	 * @throws Exception
	 */
	@Transactional
	public void saveNotice(SpecialNotice notice, Double totalTax) throws Exception {

		int noticeId = 0;

		final PreparedStatementCreator pscDocumentType = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(SpecialNoticeBuilder.INSERT_SPECIALNOTICE_QUERY, new String[] { "id" });
				ps.setString(1, notice.getUpicNo());
				ps.setString(2, notice.getTenantId());
				ps.setString(3, notice.getUlbName());
				ps.setString(4, notice.getUlbLogo());
				try {
					ps.setTimestamp(5, conevrtFormat(notice.getNoticeDate()));
				} catch (ParseException e) {

					e.printStackTrace();
				}
				ps.setString(6, notice.getNoticeNumber());
				ps.setString(7, notice.getApplicationNo());
				if (notice.getApplicationDate() != null && !notice.getApplicationDate().isEmpty())
					ps.setTimestamp(8, getTimestamp(notice.getApplicationDate(), "yyyy-MM-dd hh:mm:ss.SSS"));
				else {
					ps.setTimestamp(8, null);
				}
				return ps;
			}

			private Timestamp getTimestamp(String date, String format) {
				Timestamp timestamp = null;
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date parsedDate = dateFormat.parse(date);
					timestamp = new java.sql.Timestamp(parsedDate.getTime());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return timestamp;
			}

			private Timestamp conevrtFormat(String date) throws ParseException {
				Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(notice.getNoticeDate());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String parsedDate1 = formatter.format(initDate);
				return getTimestamp(parsedDate1, "yyyy-MM-dd");

			}
		};

		final KeyHolder holderDocumentType = new GeneratedKeyHolder();

		jdbcTemplate.update(pscDocumentType, holderDocumentType);

		noticeId = holderDocumentType.getKey().intValue();

		saveNoticeOwners(notice, noticeId);
		saveNoticeAddress(notice, noticeId);
		saveNoticeFloorSpec(notice, noticeId);
		saveTotaltax(notice, noticeId, totalTax);

	}

	private void saveTotaltax(SpecialNotice notice, int noticeId, Double totalTax) {

		Long taxDetailsId = 0l;
		final PreparedStatementCreator totalTaxType = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(SpecialNoticeBuilder.INSERT_NOTICE_TAX_DETAILS,
						new String[] { "id" });
				ps.setLong(1, Long.valueOf(String.valueOf(noticeId)));
				ps.setDouble(2, totalTax);

				return ps;
			}
		};

		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(totalTaxType, holder);
		taxDetailsId = holder.getKey().longValue();

		saveNoticeTaxWiseDetails(notice, noticeId, taxDetailsId);

	}

	private void saveNoticeTaxWiseDetails(SpecialNotice notice, int noticeId, Long taxDetailId) {

		final int id = noticeId;

		for (HeadWiseTax headWiseTax : notice.getTaxDetails().getHeadWiseTaxes()) {

			final PreparedStatementCreator pscDocumentType = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(
							SpecialNoticeBuilder.INSERT_NOTICE_TAXWISE_DETAILS, new String[] { "id" });
					ps.setLong(1, Long.valueOf(String.valueOf(id)));
					ps.setLong(2, taxDetailId);
					ps.setString(3, headWiseTax.getTaxName());
					ps.setDouble(4, headWiseTax.getTaxDays());
					ps.setDouble(5, headWiseTax.getTaxValue());
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holderDocumentType = new GeneratedKeyHolder();

			jdbcTemplate.update(pscDocumentType, holderDocumentType);

		}

	}

	private void saveNoticeFloorSpec(SpecialNotice notice, int noticeId) {

		for (FloorSpec floorSpec : notice.getFloors()) {

			final PreparedStatementCreator pscDocumentType = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection
							.prepareStatement(SpecialNoticeBuilder.INSERT_NOTICE_FLOOR_SPEC, new String[] { "id" });
					ps.setLong(1, Long.valueOf(String.valueOf(noticeId)));
					ps.setString(2, floorSpec.getFloorNo());
					ps.setString(3, floorSpec.getUnitDetails());
					ps.setString(4, floorSpec.getUsage());
					ps.setString(5, floorSpec.getConstruction());
					ps.setString(6, floorSpec.getAssessableArea());
					ps.setString(7, floorSpec.getAlv());
					ps.setString(8, floorSpec.getRv());
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holderDocumentType = new GeneratedKeyHolder();

			jdbcTemplate.update(pscDocumentType, holderDocumentType);

		}

	}

	/**
	 * This will save the notice address
	 * 
	 * @param notice
	 * @param noticeId
	 */
	private void saveNoticeAddress(SpecialNotice notice, int noticeId) {

		final PreparedStatementCreator pscDocumentType = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(SpecialNoticeBuilder.INSERT_NOTICE_ADDRESS,
						new String[] { "id" });
				ps.setLong(1, Long.valueOf(String.valueOf(noticeId)));
				if (notice.getAddress() != null && notice.getAddress().getId() != null)
					ps.setLong(2, notice.getAddress().getId());
				else
					ps.setLong(2, 0l);
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holderDocumentType = new GeneratedKeyHolder();
		jdbcTemplate.update(pscDocumentType, holderDocumentType);

	}

	/**
	 * This will save the notice in the repository
	 * 
	 * @param notice
	 * @param noticeId
	 */
	private void saveNoticeOwners(SpecialNotice notice, int noticeId) {

		for (User owner : notice.getOwners()) {

			final PreparedStatementCreator noticeOwnersType = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(SpecialNoticeBuilder.INSERT_NOTICE_OWNERS,
							new String[] { "id" });
					ps.setLong(1, Long.valueOf(String.valueOf(noticeId)));
					ps.setLong(2, owner.getId());
					return ps;
				}
			};

			// The newly generated key will be saved in this object
			final KeyHolder holder = new GeneratedKeyHolder();

			jdbcTemplate.update(noticeOwnersType, holder);

		}
	}

	/**
	 * User property delete query
	 * 
	 * @param owner
	 * @param propertyId
	 */
	public void deleteUser(Long userId, Long propertyId) {
		jdbcTemplate.update(UserBuilder.DELETE_OWNER, new Object[] { propertyId, userId });
	}

	/**
	 * This will give the audit details for the given user Id
	 * 
	 * @param propertyDetailId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getAuditDetailsForOwner(Long ownerId) {
		String query = UserBuilder.AUDIT_DETAILS_QUERY;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { ownerId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	public String getDemandForProperty(String upicNumber) {

		String demands = jdbcTemplate.queryForObject(PropertyBuilder.getDemands, new Object[] { upicNumber },
				String.class);
		return demands;
	}

	/**
	 * Query for property history This will copy property to property history
	 * based on id
	 * 
	 * @param id
	 */
	public void getPropertyForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.PROPERTY_HISTORY, new Object[] { id });
	}

	/**
	 * Query for propertydetails history This will copy propertydetails to
	 * propertydetails history based on id
	 * 
	 * @param id
	 */
	public void getPropertyDetailsForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.PROPERTYDETAILS_HISTORY, new Object[] { id });
	}

	/**
	 * Query for address history This will copy address to address history based
	 * on id
	 * 
	 * @param id
	 */
	public void getAddressForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.ADDRESS_HISTORY, new Object[] { id });
	}

	/**
	 * Query for floor history This will copy floor to floor history based on id
	 * 
	 * @param id
	 */
	public void getFloorForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.FLOOR_HISTORY, new Object[] { id });
	}

	/**
	 * Query for unit history This will copy unit to unit history based on id
	 * 
	 * @param id
	 */
	public void getUnitForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.UNIT_HISTORY, new Object[] { id });
	}

	/**
	 * Query for document history This will copy document to document history
	 * based on id
	 * 
	 * @param id
	 */
	public void getDocumentForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.DOCUMENT_HISTORY, new Object[] { id });
	}

	/**
	 * Query for vacant land history This will copy vacant land to vacand land
	 * history based on id
	 * 
	 * @param id
	 */
	public void getVacantLandForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.VACANTLAND_HISTORY, new Object[] { id });
	}

	/**
	 * Query for property owner history This will copy property owner to
	 * property owner history based on id
	 * 
	 * @param id
	 */
	public void getOwnerForHistory(User owner, Long propertyId) {
		jdbcTemplate.update(PropertyHistoryBuilder.OWNER_HISTORY, new Object[] { owner.getId(), propertyId });
	}

	/**
	 * Query for property location history This will copy property location to
	 * property location history based on id
	 * 
	 * @param id
	 */
	public void getPropertyLocationForHistory(Long id) {
		jdbcTemplate.update(PropertyHistoryBuilder.BOUNDARY_HISTORY, new Object[] { id });
	}

	/**
	 * Query for update property details history status
	 * 
	 * @param id
	 */
	public void updatePropertyHistoryStatus(PropertyDetail propertyDetails) {
		jdbcTemplate.update(PropertyHistoryBuilder.PROPERTYDETAILS_HISTORY_STATUS_UPDATE,
				new Object[] { propertyDetails.getStatus().toString(), propertyDetails.getId() });
	}

	/**
	 * This Api will search the title transfer based on the given parameters
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param upicNo
	 * @param oldUpicNo
	 * @param applicationNo
	 * @return {@link TitleTransfer}
	 * @throws Exception
	 */
	public List<TitleTransfer> searchTitleTransfer(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String[] sort, String upicNo, String oldUpicNo, String applicationNo) throws Exception {

		List<Object> preparedStatementValues = new ArrayList<Object>();

		if (upicNo == null && oldUpicNo != null) {

			upicNo = jdbcTemplate.queryForObject(TitleTransferBuilder.GET_UPIC_NO_BY_OLD_UPIC,
					new Object[] { oldUpicNo }, String.class);
		}

		String searchQuery = titleTransferBuilder.getSearchQuery(tenantId, pageSize, pageNumber, sort, upicNo,
				oldUpicNo, applicationNo, preparedStatementValues);
		List<TitleTransfer> titleTransfers = getTitleTransfer(searchQuery, preparedStatementValues);

		for (TitleTransfer titleTransfer : titleTransfers) {
			Address address = getAddressByTitleTransfer(titleTransfer.getId());
			titleTransfer.setCorrespondenceAddress(address);
			titleTransfer.setDocuments(getDocumentsForTiltleTransfer(titleTransfer.getId()));

			titleTransfer.setNewOwners(
					getOwnersByTitleTransfer(titleTransfer.getId(), titleTransfer.getTenantId(), requestInfo));
		}

		return titleTransfers;
	}

	/**
	 * This API will return the list of Documents for the given title transfer
	 * Id
	 * 
	 * @param titleTransferId
	 * @return {@link Document}
	 */
	private List<Document> getDocumentsForTiltleTransfer(Long titleTransferId) {
		List<Object> preparedStatementvalues = new ArrayList<>();
		String documentsQuery = TitleTransferBuilder.getDocumentsByTitleTransfer(titleTransferId,
				preparedStatementvalues);
		List<Document> documents = null;

		documents = jdbcTemplate.query(documentsQuery, preparedStatementvalues.toArray(),
				new BeanPropertyRowMapper(Document.class));

		return documents;
	}

	/**
	 * This Api will give the list of title transfer records based on the given
	 * parameters
	 * 
	 * @param query
	 * @param preparedStatementValues
	 * @return TitleTransfer List
	 * @throws Exception
	 * @return {@link List} of {@link TitleTransfer}
	 */
	private List<TitleTransfer> getTitleTransfer(String query, List<Object> preparedStatementValues) throws Exception {

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		List<TitleTransfer> tiltleTransfers = new ArrayList<TitleTransfer>();
		TitleTransfer titleTransfer = new TitleTransfer();
		for (Map<String, Object> row : rows) {
			titleTransfer.setApplicationNo(getString(row.get("applicationno")));
			titleTransfer.setId(getLong(row.get("id")));
			titleTransfer.setTenantId(getString(row.get("tenantid")));
			titleTransfer.setTransferReason(getString(row.get("transferreason")));

			titleTransfer.setRegistrationDocNo(getString(row.get("registrationdocno")));
			String registrationDocDate = getString(row.get("registrationdocdate"));
			if (registrationDocDate != null) {
				titleTransfer.setRegistrationDocDate(TimeStampUtil.getDateFormat(registrationDocDate));
			}
			titleTransfer.setDepartmentGuidelineValue(getDouble(row.get("departmentguidelinevalue")));
			titleTransfer.setPartiesConsiderationValue(getDouble(row.get("partiesconsiderationvalue")));
			titleTransfer.setCourtOrderNumber(getLong(row.get("courtordernumber")));
			titleTransfer.setSubRegOfficeName(getString(row.get("subregofficename")));
			titleTransfer.setTitleTrasferFee(getDouble(row.get("titletrasferfee")));
			titleTransfer.setStateId(getString(row.get("stateid")));
			titleTransfer.setReceiptnumber(getString(row.get("receiptnumber")));
			titleTransfer.setDemandId(getString(row.get("demandid")));
			titleTransfer.setUpicNo(getString(row.get("upicno")));

			String receiptdate = getString(row.get("receiptdate"));
			if (receiptdate != null) {
				titleTransfer.setReceiptdate(TimeStampUtil.getDateFormat(receiptdate));
			}
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			titleTransfer.setAuditDetails(auditDetails);
			tiltleTransfers.add(titleTransfer);

		}
		return tiltleTransfers;
	}

	/**
	 * This API will get the Address by the given title transferId
	 * 
	 * @param titleTransferId
	 * @return {@link Address}
	 */
	public Address getAddressByTitleTransfer(Long titleTransferId) {
		Address address = null;
		try {
			address = (Address) jdbcTemplate.queryForObject(TitleTransferBuilder.ADDRES_BY_TITLE_TRANSFER_ID_QUERY,
					new Object[] { titleTransferId }, new BeanPropertyRowMapper(Address.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (address != null && address.getId() != null && address.getId() > 0)
			address.setAuditDetails(getAuditDetailsForAddress(address.getId()));

		return address;
	}

	/**
	 * This API will give the Audit details for the given Address Id
	 * 
	 * @param addressId
	 * @return {@link AuditDetails}
	 */
	public AuditDetails getTitleTransferAuditDetailsForAddress(Long addressId) {
		String query = TitleTransferBuilder.AUDIT_DETAILS_FOR_ADDRESS;
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(query, new Object[] { addressId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	/**
	 * This API will give the Owners for the given title TransferId & tenantId
	 * 
	 * @param titleTransferId
	 * @param tenantId
	 * @param requestInfo
	 * @return List<{@link User}>
	 */
	private List<User> getOwnersByTitleTransfer(Long titleTransferId, String tenantId, RequestInfo requestInfo) {

		List<Object> preparedStatementvalues = new ArrayList<>();
		String ownersQuery = TitleTransferBuilder.getOwnersByTitleTransfer(titleTransferId, preparedStatementvalues);
		List<User> owners = null;

		owners = jdbcTemplate.query(ownersQuery, preparedStatementvalues.toArray(),
				new BeanPropertyRowMapper(User.class));

		List<User> users = new ArrayList<>();
		owners.forEach(owner -> {
			StringBuffer userSearchUrl = new StringBuffer();
			userSearchUrl.append(propertiesManager.getUserHostname());
			userSearchUrl.append(propertiesManager.getUserBasepath());
			userSearchUrl.append(propertiesManager.getUserSearchpath());
			Map<String, Object> userSearchRequestInfo = new HashMap<>();
			List<Long> userIds = new ArrayList<Long>();
			userIds.add(owner.getOwner());
			userSearchRequestInfo.put("tenantId", tenantId);
			userSearchRequestInfo.put("id", userIds);
			userSearchRequestInfo.put("RequestInfo", requestInfo);

			logger.info("PropertyRepository  userSearchRequestInfo :" + userSearchRequestInfo);
			logger.info("PropertyRepository  userSearchUrl :" + userSearchUrl);

			UserResponseInfo userResponse = restTemplate.postForObject(userSearchUrl.toString(), userSearchRequestInfo,
					UserResponseInfo.class);
			logger.info("PropertyRepository UserResponseInfo :" + userResponse);

			userResponse.getUser().forEach(user -> {
				user.setIsPrimaryOwner(owner.getIsPrimaryOwner());
				user.setIsSecondaryOwner(owner.getIsSecondaryOwner());
				user.setOwnerShipPercentage(owner.getOwnerShipPercentage());
				user.setOwnerType(owner.getOwnerType());
				user.setOwner(owner.getOwner());
				user.setAuditDetails(getAuditDetailsForOwner(owner.getId()));
				users.add(user);
			});

		});

		return users;

	}

	/*
	 * 
	 * @param propertyRequest
	 * 
	 * @return true/false if the property has been moved/property failed to
	 * moved
	 */
	@Transactional
	public Boolean movePropertytoHistory(PropertyRequest propertyRequest) throws Exception {

		Boolean moved = Boolean.TRUE;
		// moving units by floor id

		Property property = propertyRequest.getProperties().get(0);

		Integer oldPropertyId = jdbcTemplate.queryForObject(PropertyBuilder.ACTIVE_PROPERTY_BY_UPIC_NUMBER,
				new Object[] { property.getUpicNumber(), Boolean.TRUE }, Integer.class);

		List<Floor> floors = new ArrayList<Floor>();
		Integer oldPropertyDetailId = jdbcTemplate.queryForObject(
				PropertyDetailBuilder.PROPERTY_DETAIL_ID_BY_PROPERTY_ID, new Object[] { oldPropertyId }, Integer.class);
		jdbcTemplate.update(PropertyDetailBuilder.UPDATE_PROPERTYDETAIL_STATUS,
				new Object[] { StatusEnum.HISTORY.toString(), oldPropertyDetailId });

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(FloorBuilder.FLOORS_BY_PROPERTY_DETAILS_QUERY,
				new Object[] { oldPropertyDetailId });

		for (Map<String, Object> row : rows) {
			Floor floor = new Floor();
			floor.setId(getLong(row.get("id")));
			floors.add(floor);
		}

		try {
			for (Floor floor : floors) {
				jdbcTemplate.update(UnitBuilder.MOVE_UNITS_TO_HISTORY, new Object[] { floor.getId() });
			}

			// moving floors by property detail id

			jdbcTemplate.update(FloorBuilder.MOVE_FLOORS_TO_HISTORY, new Object[] { oldPropertyDetailId });

			jdbcTemplate.update(PropertyDetailBuilder.MOVE_PROPERTY_DETAIL_TO_HISTORY, new Object[] { oldPropertyId });

			jdbcTemplate.update(UserBuilder.MOVE_OWNERS_TO_HISTORY, new Object[] { oldPropertyId });

			jdbcTemplate.update(BoundaryBuilder.MOVE_BOUNDARY_TO_HISTORY, new Object[] { oldPropertyId });

			jdbcTemplate.update(AddressBuilder.MOVE_ADDRESS_TO_HISTORY, new Object[] { oldPropertyId });

			jdbcTemplate.update(PropertyBuilder.MOVE_PROPERTY_TO_HISTORY, new Object[] { oldPropertyId });
		} catch (Exception e) {
			moved = Boolean.FALSE;
			jdbcTemplate.update(PropertyDetailBuilder.UPDATE_PROPERTYDETAIL_STATUS,
					new Object[] { StatusEnum.HISTORY.toString(), oldPropertyDetailId });
		}

		return moved;
	}

	public void deleteFloorById(Floor floor) {
		jdbcTemplate.update(FloorBuilder.DELETE_UNIT_BY_FLOORID, new Object[] { floor.getId() });
		jdbcTemplate.update(FloorBuilder.DELETE_FLOORS_BY_ID, new Object[] { floor.getId() });

	}

	public void deleteUnitByFloorId(Long floorId) {
		jdbcTemplate.update(FloorBuilder.DELETE_UNIT_BY_FLOORID, new Object[] { floorId });
	}

	public void deleteDocumentsById(Document document) {
		jdbcTemplate.update(DocumentBuilder.DELETE_DOCUMENT_BY_ID, new Object[] { document.getId() });
	}

	public void deleteOwnerById(User user, Long propertyId) {
		jdbcTemplate.update(UserBuilder.DELETE_USER_BY_ID, new Object[] { propertyId, user.getId() });

	}

	public void updateIsUnderWorkflowbyId(Long propertyId) {
		String query = PropertyBuilder.updateIsUnderWokflowById;
		Object[] arguments = { false, propertyId };
		jdbcTemplate.update(query, arguments);
	}

	/**
	 * This will Update the property object based on th demolition
	 * 
	 * @param property
	 * @param demolitionRequest
	 */
	@Transactional
	public void updateProperyAfterDemolition(Property property, DemolitionRequest demolitionRequest) {

		Demolition demolition = demolitionRequest.getDemolition();
		Long propertyId = property.getId();
		PropertyDetail propertyDetail = property.getPropertyDetail();

		Object[] propertsyDetailArgs = { propertiesManager.getVacantLand(), demolition.getPropertySubType(),
				demolition.getUsageType(), demolition.getUsageSubType(), demolition.getTotalArea(),
				propertyDetail.getId() };

		jdbcTemplate.update(PropertyDetailBuilder.UPDATE_PROPETY_DETAILS_AFTER_DEMOLITION, propertsyDetailArgs);

		jdbcTemplate.update(PropertyBuilder.UPDATE_PROPETY_AFTER_DEMOLITION,
				new Object[] { demolition.getSequenceNo(), Boolean.FALSE, demolition.getIsLegal(), propertyId });
		
		jdbcTemplate.update(PropertyBuilder.UPDATE_PROPETY_AFTER_DEMOLITION,new Object[] {demolition.getSequenceNo(),Boolean.FALSE,demolition.getIsLegal(),propertyId});
		
	}
}