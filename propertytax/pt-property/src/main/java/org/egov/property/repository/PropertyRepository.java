package org.egov.property.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.models.Address;
import org.egov.models.AuditDetails;
import org.egov.models.Demand;
import org.egov.models.DemandId;
import org.egov.models.Document;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.RequestInfo;
import org.egov.models.TitleTransfer;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.models.VacantLandDetail;
import org.egov.property.model.PropertyLocationRowMapper;
import org.egov.property.model.PropertyUser;
import org.egov.property.repository.builder.AddressBuilder;
import org.egov.property.repository.builder.BoundaryBuilder;
import org.egov.property.repository.builder.DocumentBuilder;
import org.egov.property.repository.builder.FloorBuilder;
import org.egov.property.repository.builder.PropertyBuilder;
import org.egov.property.repository.builder.PropertyDetailBuilder;
import org.egov.property.repository.builder.SearchPropertyBuilder;
import org.egov.property.repository.builder.TitleTransferAddressBuilder;
import org.egov.property.repository.builder.TitleTransferBuilder;
import org.egov.property.repository.builder.TitleTransferDocumentBuilder;
import org.egov.property.repository.builder.TitleTransferUserBuilder;
import org.egov.property.repository.builder.UnitBuilder;
import org.egov.property.repository.builder.UserBuilder;
import org.egov.property.repository.builder.VacantLandDetailBuilder;
import org.egov.property.utility.TimeStampUtil;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	Environment environment;

	/**
	 * property query formation
	 * 
	 * @param property
	 * @return holder key
	 * @throws JsonProcessingException
	 */

	public Long saveProperty(Property property) throws Exception {
		Long createdTime = new Date().getTime();

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
				ps.setLong(14, getLong(createdTime));
				ps.setLong(15, getLong(createdTime));
				List<DemandId> demandIdList = new ArrayList<DemandId>();

				for (Demand demand : property.getDemands()) {
					DemandId id = new DemandId();
					id.setId(demand.getId());
					demandIdList.add(id);
				}

				Gson gson = new Gson();
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(gson.toJson(demandIdList));
				ps.setObject(16, jsonObject);
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
		Long createdTime = new Date().getTime();

		Address address = property.getAddress();

		Object[] addressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressNumber(), address.getAddressLine1(), address.getAddressLine2(), address.getLandmark(),
				address.getCity(), address.getPincode(), address.getDetail(), address.getAuditDetails().getCreatedBy(),
				address.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, propertyId };

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
		Long createdTime = new Date().getTime();
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
				ps.setLong(31, getLong(createdTime));
				ps.setLong(32, getLong(createdTime));
				ps.setLong(33, getLong(propertyId));
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(propertyDetails.getTaxCalculations());
				ps.setObject(34, jsonObject);
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

		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscFloor = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(FloorBuilder.INSERT_FLOOR_QUERY,
						new String[] { "id" });
				ps.setString(1, floor.getFloorNo());
				ps.setString(2, floor.getAuditDetails().getCreatedBy());
				ps.setString(3, floor.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(createdTime));
				ps.setLong(5, getLong(createdTime));
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

		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscUnit = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(UnitBuilder.INSERT_UNIT_QUERY,
						new String[] { "id" });
				ps.setInt(1, getInteger(unit.getUnitNo()));
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
				ps.setLong(27, getLong(createdTime));
				ps.setLong(28, getLong(createdTime));
				ps.setLong(29, getLong(floorId));
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

		Long createdTime = new Date().getTime();

		Object[] roomArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getCreatedBy(),
				unit.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, floorId, parent };

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
		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(DocumentBuilder.INSERT_DOCUMENT_QUERY,
						new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getAuditDetails().getCreatedBy());
				ps.setString(3, document.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(createdTime));
				ps.setLong(5, getLong(createdTime));
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

	public Integer getInteger(Integer value) {

		if (value == null)
			return 0;
		else
			return value;
	}

	/**
	 * VacantLandDetail creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveVacantLandDetail(Property property, Long propertyId) {
		Long createdTime = new Date().getTime();
		VacantLandDetail vacantLand = property.getVacantLand();

		Object[] vaccantLandArgs = { vacantLand.getSurveyNumber(), vacantLand.getPattaNumber(),
				vacantLand.getMarketValue(), vacantLand.getCapitalValue(), vacantLand.getLayoutApprovedAuth(),
				vacantLand.getLayoutPermissionNo(), TimeStampUtil.getTimeStamp(vacantLand.getLayoutPermissionDate()),
				vacantLand.getResdPlotArea(), vacantLand.getNonResdPlotArea(),
				vacantLand.getAuditDetails().getCreatedBy(), vacantLand.getAuditDetails().getLastModifiedBy(),
				createdTime, createdTime, propertyId };

		jdbcTemplate.update(VacantLandDetailBuilder.INSERT_VACANTLANDDETAIL_QUERY, vaccantLandArgs);

	}

	/**
	 * Boundary creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveBoundary(Property property, Long propertyId) {
		Long createdTime = new Date().getTime();
		PropertyLocation boundary = property.getBoundary();

		Object[] boundaryArgs = { boundary.getRevenueBoundary().getId(), boundary.getLocationBoundary().getId(),
				boundary.getAdminBoundary().getId(), boundary.getNorthBoundedBy(), boundary.getEastBoundedBy(),
				boundary.getWestBoundedBy(), boundary.getSouthBoundedBy(), boundary.getAuditDetails().getCreatedBy(),
				boundary.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, propertyId };

		jdbcTemplate.update(BoundaryBuilder.INSERT_BOUNDARY_QUERY, boundaryArgs);

	}

	/**
	 * User property query
	 * 
	 * @param owner
	 * @param propertyId
	 */
	public void saveUser(User owner, Long propertyId) {
		Long createdTime = new Date().getTime();

		Object[] userPropertyArgs = { propertyId, owner.getId(), owner.getIsPrimaryOwner(), owner.getIsSecondaryOwner(),
				owner.getOwnerShipPercentage(), owner.getOwnerType(), owner.getAuditDetails().getCreatedBy(),
				owner.getAuditDetails().getLastModifiedBy(), createdTime, createdTime };

		jdbcTemplate.update(UserBuilder.INSERT_USER_QUERY, userPropertyArgs);
	}

	public Map<String, Object> searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			int pageSize, int pageNumber, String[] sort, String oldUpicNo, String mobileNumber, String aadhaarNumber,
			String houseNoBldgApt, int revenueZone, int revenueWard, int locality, String ownerName, int demandFrom,
			int demandTo) {

		Map<String, Object> searchPropertyMap = new HashMap<>();
		List<Object> preparedStatementValues = new ArrayList<Object>();

		Map<String, Object> propertyMap = searchPropertyBuilder.createSearchPropertyQuery(requestInfo, tenantId, active,
				upicNo, pageSize, pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone,
				revenueWard, locality, ownerName, demandFrom, demandTo, preparedStatementValues);
		List<Property> properties = jdbcTemplate.query(propertyMap.get("Sql").toString(),
				preparedStatementValues.toArray(), new BeanPropertyRowMapper(Property.class));

		searchPropertyMap.put("properties", properties);
		searchPropertyMap.put("users", propertyMap.get("users"));

		return searchPropertyMap;

	}

	public Address getAddressByProperty(Long propertyId) {

		Address address = (Address) jdbcTemplate.queryForObject(AddressBuilder.ADDRES_BY_PROPERTY_ID_QUERY,
				new Object[] { propertyId }, new BeanPropertyRowMapper(Address.class));
		if (address != null && address.getId() != null && address.getId() > 0)
			address.setAuditDetails(getAuditDetailsForAddress(address.getId()));

		return address;
	}

	public List<PropertyUser> getPropertyUserByProperty(Long propertyId) {

		List<PropertyUser> propertyUsers = jdbcTemplate.query(UserBuilder.PROPERTY_OWNER_BY_PROPERTY_ID_QUERY,
				new Object[] { propertyId }, new BeanPropertyRowMapper(PropertyUser.class));
		return propertyUsers;

	}

	public PropertyUser getPropertyUserByUser(Long userId) {

		PropertyUser propertyUser = (PropertyUser) jdbcTemplate.queryForObject(
				UserBuilder.PROPERTY_OWNER_BY_USER_ID_QUERY, new Object[] { userId },
				new BeanPropertyRowMapper(PropertyUser.class));
		return propertyUser;

	}

	public PropertyDetail getPropertyDetailsByProperty(Long propertyId) {
		PropertyDetail propertyDetail = (PropertyDetail) jdbcTemplate.queryForObject(
				PropertyDetailBuilder.PROPERTY_DETAIL_BY_PROPERTY_QUERY, new Object[] { propertyId },
				new BeanPropertyRowMapper(PropertyDetail.class));
		if (propertyDetail != null && propertyDetail.getId() != null && propertyDetail.getId() > 0)
			propertyDetail.setAuditDetails(getAuditDetailsForPropertyDetail(propertyDetail.getId()));

		return propertyDetail;

	}

	public VacantLandDetail getVacantLandByProperty(Long propertyId) {
		VacantLandDetail vacantLandDetail = (VacantLandDetail) jdbcTemplate.queryForObject(
				VacantLandDetailBuilder.VACANT_LAND_BY_PROPERTY_QUERY, new Object[] { propertyId },
				new BeanPropertyRowMapper(VacantLandDetail.class));
		if (vacantLandDetail != null && vacantLandDetail.getId() != null && vacantLandDetail.getId() > 0)
			vacantLandDetail.setAuditDetails(getAuditDetailsForvacantLandDetail(vacantLandDetail.getId()));

		return vacantLandDetail;
	}

	public PropertyLocation getPropertyLocationByproperty(Long propertyId) {
		PropertyLocation propertyLocation = (PropertyLocation) jdbcTemplate.queryForObject(
				BoundaryBuilder.PROPERTY_LOCATION_BY_PROPERTY_QUERY, new Object[] { propertyId },
				new PropertyLocationRowMapper());
		if (propertyLocation != null && propertyLocation.getId() != null && propertyLocation.getId() > 0)
			propertyLocation.setAuditDetails(getAuditDetailsForBoundary(propertyLocation.getId()));

		return propertyLocation;
	}

	public List<Floor> getFloorsByPropertyDetails(Long propertyDetailId) {

		List<Floor> floors = jdbcTemplate.query(FloorBuilder.FLOORS_BY_PROPERTY_DETAILS_QUERY,
				new Object[] { propertyDetailId }, new BeanPropertyRowMapper(Floor.class));
		floors.forEach(floor -> {
			if (floor.getId() != null && floor.getId() > 0) {
				floor.setAuditDetails(getAuditDetailsForFloor(floor.getId()));
			}
		});

		return floors;

	}

	public List<Unit> getUnitsByFloor(Long floorId) {

		List<Unit> units = jdbcTemplate.query(UnitBuilder.UNITS_BY_FLOOR_QUERY, new Object[] { floorId },
				new BeanPropertyRowMapper(Unit.class));
		units.forEach(unit -> {
			if (unit.getId() != null && unit.getId() > 0)
				unit.setAuditDetails(getAuditDetailsForUnit(unit.getId()));
		});

		return units;

	}

	public List<Document> getDocumentByPropertyDetails(Long propertyDetailId) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(DocumentBuilder.DOCUMENT_BY_PROPERTY_DETAILS_QUERY,
				new Object[] { propertyDetailId });
		List<Document> documents = getDocumentObject(rows);
		documents.forEach(document -> {
			if (document.getId() != null && document.getId() > 0)
				document.setAuditDetails(getAuditDetailsForDocument(document.getId()));
		});

		return documents;
	}

	public AuditDetails getAuditForPropertyDetails(Long propertyId) {
		AuditDetails auditDetails = (AuditDetails) jdbcTemplate.queryForObject(
				PropertyBuilder.AUDIT_DETAILS_FOR_PROPERTY_DETAILS, new Object[] { propertyId },
				new BeanPropertyRowMapper(AuditDetails.class));
		return auditDetails;
	}

	private List<Document> getDocumentObject(List<Map<String, Object>> documentList) {

		List<Document> documents = new ArrayList<>();

		for (Map<String, Object> documentdata : documentList) {
			Document document = new Document();
			document.setDocumentType(documentdata.get("documenttype").toString());
			document.setFileStore(documentdata.get("filestore").toString());
			document.setId(Long.valueOf(documentdata.get("id").toString()));
			documents.add(document);

		}

		return documents;

	}

	public void updateProperty(Property property) throws Exception {

		Long updatedTime = new Date().getTime();

		String propertyUpdate = PropertyBuilder.updatePropertyQuery();

		ObjectMapper obj = new ObjectMapper();

		String demands = obj.writeValueAsString(property.getDemands());
		PGobject jsonObject = new PGobject();
		jsonObject.setType("jsonb");
		jsonObject.setValue(demands);

		Object[] propertyArgs = { property.getTenantId(), property.getUpicNumber(), property.getOldUpicNumber(),
				property.getVltUpicNumber(), property.getCreationReason().name(),
				TimeStampUtil.getTimeStamp(property.getAssessmentDate()),
				TimeStampUtil.getTimeStamp(property.getOccupancyDate()), property.getGisRefNo(),
				property.getIsAuthorised(), property.getIsUnderWorkflow(), property.getChannel().name(),
				property.getAuditDetails().getLastModifiedBy(), updatedTime, jsonObject, property.getId() };

		jdbcTemplate.update(propertyUpdate, propertyArgs);

	}

	public void updateAddress(Address address, Long address_id, Long proertyId) {

		Long updatedTime = new Date().getTime();

		String addressUpdate = AddressBuilder.updatePropertyAddressQuery();

		Object[] addressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressNumber(), address.getAddressLine1(), address.getAddressLine2(), address.getLandmark(),
				address.getCity(), address.getPincode(), address.getDetail(),
				address.getAuditDetails().getLastModifiedBy(), updatedTime, proertyId, address_id };

		jdbcTemplate.update(addressUpdate, addressArgs);

	}

	public void updatePropertyDetail(PropertyDetail propertyDetails, Long proertyId) throws Exception {

		Long updatedTime = new Date().getTime();

		String propertyDetailsUpdate = PropertyDetailBuilder.updatePropertyDetailQuery();

		PGobject jsonObject = new PGobject();
		jsonObject.setType("jsonb");
		jsonObject.setValue(propertyDetails.getTaxCalculations());

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
				propertyDetails.getAuditDetails().getLastModifiedBy(), updatedTime, proertyId, jsonObject,
				propertyDetails.getId() };

		jdbcTemplate.update(propertyDetailsUpdate, propertyDetailsArgs);
	}

	public void updateVacantLandDetail(VacantLandDetail vacantLand, Long vacantland_id, Long proertyId) {

		Long updatedTime = new Date().getTime();

		String vacantlandUpdate = VacantLandDetailBuilder.updateVacantLandQuery();

		Object[] vaccantLandArgs = { vacantLand.getSurveyNumber(), vacantLand.getPattaNumber(),
				vacantLand.getMarketValue(), vacantLand.getCapitalValue(), vacantLand.getLayoutApprovedAuth(),
				vacantLand.getLayoutPermissionNo(), TimeStampUtil.getTimeStamp(vacantLand.getLayoutPermissionDate()),
				vacantLand.getResdPlotArea(), vacantLand.getNonResdPlotArea(),
				vacantLand.getAuditDetails().getLastModifiedBy(), updatedTime, proertyId, vacantland_id };

		jdbcTemplate.update(vacantlandUpdate, vaccantLandArgs);
	}

	public void updateFloor(Floor floor, Long propertyDetail_id) {

		Long updatedTime = new Date().getTime();

		String floorUpdate = FloorBuilder.updateFloorQuery();

		Object[] floorArgs = { floor.getFloorNo(), floor.getAuditDetails().getLastModifiedBy(), updatedTime,
				propertyDetail_id, floor.getId() };

		jdbcTemplate.update(floorUpdate, floorArgs);

	}

	/**
	 * Description: updating unit
	 * 
	 * @param unit
	 */
	public void updateUnit(Unit unit) {

		Long updatedTime = new Date().getTime();

		String unitUpdate = UnitBuilder.updateUnitQuery();

		Object[] unitArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getLastModifiedBy(),
				updatedTime, unit.getParentId(), unit.getId(), unit.getIsAuthorised() };

		jdbcTemplate.update(unitUpdate, unitArgs);

	}

	/**
	 * Description: updating room
	 * 
	 * @param unit
	 */
	public void updateRoom(Unit unit) {

		Long updatedTime = new Date().getTime();

		String roomUpdate = UnitBuilder.updateRoomQuery();

		Object[] roomArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getLastModifiedBy(),
				updatedTime, unit.getParentId(), unit.getId(), unit.getIsAuthorised() };

		jdbcTemplate.update(roomUpdate, roomArgs);

	}

	public void updateDocument(Document document, Long propertyDetailsId) {

		Long updatedTime = new Date().getTime();

		String documentUpdate = DocumentBuilder.updateDocumentQuery();

		Object[] documentArgs = { document.getFileStore(), document.getAuditDetails().getLastModifiedBy(), updatedTime,
				propertyDetailsId, document.getDocumentType(), document.getId() };

		jdbcTemplate.update(documentUpdate, documentArgs);
	}

	public void updateUser(User owner, Long propertyId) {

		Long updatedTime = new Date().getTime();

		String userUpdate = UserBuilder.updateOwnerQuery();

		Object[] userPropertyArgs = { propertyId, owner.getId(), owner.getIsPrimaryOwner().booleanValue(),
				owner.getIsSecondaryOwner().booleanValue(), owner.getOwnerShipPercentage(), owner.getOwnerType(),
				owner.getAuditDetails().getLastModifiedBy(), updatedTime, owner.getId() };

		jdbcTemplate.update(userUpdate, userPropertyArgs);

	}

	public void updateBoundary(PropertyLocation boundary, Long propertId) {

		Long updatedTime = new Date().getTime();

		String boundaryUpdate = BoundaryBuilder.updateBoundaryQuery();

		Object[] boundaryArgs = { boundary.getRevenueBoundary().getId(), boundary.getLocationBoundary().getId(),
				boundary.getAdminBoundary().getId(), boundary.getNorthBoundedBy().toString(),
				boundary.getEastBoundedBy().toString(), boundary.getWestBoundedBy().toString(),
				boundary.getSouthBoundedBy().toString(), boundary.getAuditDetails().getLastModifiedBy(), updatedTime,
				propertId, boundary.getId() };

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
		Long createdTime = new Date().getTime();

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
				ps.setLong(16, getLong(createdTime));
				ps.setLong(17, getLong(createdTime));
				ps.setString(18, titleTransfer.getApplicationNo());

				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder titleTransferKey = new GeneratedKeyHolder();

		jdbcTemplate.update(pscTitleTransfer, titleTransferKey);

		Long title_Transfer_id = Long.parseLong(String.valueOf(titleTransferKey.getKey().intValue()).trim());

		return title_Transfer_id;

	}

	public void saveTitleTransferUser(User owner, Long titleTransferId) {
		Long createdTime = new Date().getTime();

		Object[] titleTransferUserArgs = { titleTransferId, owner.getId(), owner.getIsPrimaryOwner(),
				owner.getIsSecondaryOwner(), owner.getOwnerShipPercentage(), owner.getOwnerType(),
				owner.getAuditDetails().getCreatedBy(), owner.getAuditDetails().getLastModifiedBy(), createdTime,
				createdTime };

		jdbcTemplate.update(TitleTransferUserBuilder.INSERT_TITLETRANSFERUSER_QUERY, titleTransferUserArgs);
	}

	public Long saveTitleTransferDocument(Document document, Long titleTransferId) {
		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(
						TitleTransferDocumentBuilder.INSERT_TITLETRANSFERDOCUMENT_QUERY, new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getAuditDetails().getCreatedBy());
				ps.setString(3, document.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(createdTime));
				ps.setLong(5, getLong(createdTime));
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
		Long createdTime = new Date().getTime();

		Address address = titleTransfer.getCorrespondenceAddress();

		Object[] titleTransferAddressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressNumber(), address.getAddressLine1(), address.getAddressLine2(), address.getLandmark(),
				address.getCity(), address.getPincode(), address.getDetail(), address.getAuditDetails().getCreatedBy(),
				address.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, titleTransferId };

		jdbcTemplate.update(TitleTransferAddressBuilder.INSERT_TITLETRANSERADDRESS_QUERY, titleTransferAddressArgs);

	}

	public void updateTitleTransfer(TitleTransfer titleTransfer) {

		Long updatedTime = new Date().getTime();

		String titleTransferUpdate = TitleTransferBuilder.UPDATE_TITLETRANSFER_QUERY;

		Object[] titleTransferArgs = { titleTransfer.getStateId(), titleTransfer.getAuditDetails().getLastModifiedBy(),
				updatedTime, titleTransfer.getApplicationNo() };

		jdbcTemplate.update(titleTransferUpdate, titleTransferArgs);

	}

	/**
	 * updating isUnderWorkflow as false
	 * 
	 * @param property
	 * @throws Exception
	 */
	public void updateTitleTransferProperty(Property property) throws Exception {

		Long updatedTime = new Date().getTime();

		String propertyUpdate = PropertyBuilder.UPDATE_TITLETRANSFERPROPERTY_QUERY;

		Object[] propertyArgs = { false, property.getAuditDetails().getLastModifiedBy(), updatedTime,
				property.getId() };

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

		Long updatedTime = new Date().getTime();

		String propertyDetailsUpdate = PropertyDetailBuilder.UPDATE_TITLETRANSFERPROPERTYDETAIL_QUERY;

		Object[] propertyDetailsArgs = { propertyDetails.getStateId(),
				propertyDetails.getAuditDetails().getLastModifiedBy(), updatedTime, propertyDetails.getId() };

		jdbcTemplate.update(propertyDetailsUpdate, propertyDetailsArgs);
	}

	/**
	 * property history query formation
	 * 
	 * @param property
	 * @throws JsonProcessingException
	 */

	public void savePropertyHistory(Property property) throws Exception {
		Long createdTime = new Date().getTime();

		ObjectMapper obj = new ObjectMapper();

		String demands = obj.writeValueAsString(property.getDemands());

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(PropertyBuilder.INSERT_PROPERTYHISTORY_QUERY,
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
				ps.setLong(14, getLong(createdTime));
				ps.setLong(15, getLong(createdTime));

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(demands);

				ps.setObject(16, property.getDemands());
				ps.setLong(17, getLong(property.getId()));
				return ps;
			}
		};

		jdbcTemplate.update(psc);

	}

	/**
	 * Address history creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveAddressHistory(Property property, Long propertyId) {
		Long createdTime = new Date().getTime();

		Address address = property.getAddress();

		Object[] addressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressNumber(), address.getAddressLine1(), address.getAddressLine2(), address.getLandmark(),
				address.getCity(), address.getPincode(), address.getDetail(), address.getAuditDetails().getCreatedBy(),
				address.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, propertyId, address.getId() };

		jdbcTemplate.update(AddressBuilder.INSERT_ADDRESSHISTORY_QUERY, addressArgs);

	}

	/**
	 * Property details history query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void savePropertyDetailsHistory(Property property, Long propertyId) {
		Long createdTime = new Date().getTime();
		PropertyDetail propertyDetails = property.getPropertyDetail();
		final PreparedStatementCreator pscPropertyDetails = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(
						PropertyDetailBuilder.INSERT_PROPERTYDETAILSHISTORY_QUERY, new String[] { "id" });
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
				ps.setLong(31, getLong(createdTime));
				ps.setLong(32, getLong(createdTime));
				ps.setLong(33, getLong(propertyId));
				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(propertyDetails.getTaxCalculations());
				ps.setObject(34, jsonObject);
				ps.setLong(35, getLong(propertyDetails.getId()));
				return ps;
			}
		};

		jdbcTemplate.update(pscPropertyDetails);

	}

	/**
	 * Floor history creation query
	 * 
	 * @param floor
	 * @param propertyDetailsId
	 */
	public void saveFloorHistory(Floor floor, Long propertyDetailsId) {

		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscFloor = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(FloorBuilder.INSERT_FLOORHISTORY_QUERY,
						new String[] { "id" });
				ps.setString(1, floor.getFloorNo());
				ps.setString(2, floor.getAuditDetails().getCreatedBy());
				ps.setString(3, floor.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(createdTime));
				ps.setLong(5, getLong(createdTime));
				ps.setLong(6, getLong(propertyDetailsId));
				ps.setLong(7, getLong(floor.getId()));
				return ps;
			}
		};

		jdbcTemplate.update(pscFloor);
	}

	/**
	 * unit history creation query
	 * 
	 * @param unit
	 * @param floorId
	 */
	public void saveUnitHistory(Unit unit, Long floorId) {

		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscUnit = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(UnitBuilder.INSERT_UNITHISTORY_QUERY,
						new String[] { "id" });
				ps.setInt(1, getInteger(unit.getUnitNo()));
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
				ps.setLong(27, getLong(createdTime));
				ps.setLong(28, getLong(createdTime));
				ps.setLong(29, getLong(floorId));
				ps.setLong(30, getLong(unit.getId()));
				return ps;
			}
		};

		jdbcTemplate.update(pscUnit);

	}

	/**
	 * room history creation query
	 * 
	 * @param unit
	 * @param floorId
	 */
	public void saveRoomHistory(Unit unit, Long floorId, Long parent) {

		Long createdTime = new Date().getTime();

		Object[] roomArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getCreatedBy(),
				unit.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, floorId, parent, unit.getId() };

		jdbcTemplate.update(UnitBuilder.INSERT_ROOMHISTORY_QUERY, roomArgs);

	}

	/**
	 * document history creation query
	 * 
	 * @param document
	 * @param propertyDetailsId
	 */
	public void saveDocumentHistory(Document document, Long propertyDetailsId) {
		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(DocumentBuilder.INSERT_DOCUMENTHISTORY_QUERY,
						new String[] { "id" });
				ps.setString(1, document.getFileStore());
				ps.setString(2, document.getAuditDetails().getCreatedBy());
				ps.setString(3, document.getAuditDetails().getLastModifiedBy());
				ps.setLong(4, getLong(createdTime));
				ps.setLong(5, getLong(createdTime));
				ps.setLong(6, getLong(propertyDetailsId));
				ps.setLong(7, getLong(document.getId()));
				ps.setString(8, document.getDocumentType());
				return ps;
			}
		};

		jdbcTemplate.update(pscDocument);

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
	 * VacantLandDetail history creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveVacantLandDetailHistory(Property property, Long propertyId) {
		Long createdTime = new Date().getTime();
		VacantLandDetail vacantLand = property.getVacantLand();

		Object[] vaccantLandArgs = { vacantLand.getSurveyNumber(), vacantLand.getPattaNumber(),
				vacantLand.getMarketValue(), vacantLand.getCapitalValue(), vacantLand.getLayoutApprovedAuth(),
				vacantLand.getLayoutPermissionNo(), TimeStampUtil.getTimeStamp(vacantLand.getLayoutPermissionDate()),
				vacantLand.getResdPlotArea(), vacantLand.getNonResdPlotArea(),
				vacantLand.getAuditDetails().getCreatedBy(), vacantLand.getAuditDetails().getLastModifiedBy(),
				createdTime, createdTime, propertyId, vacantLand.getId() };

		jdbcTemplate.update(VacantLandDetailBuilder.INSERT_VACANTLANDDETAILHISTORY_QUERY, vaccantLandArgs);

	}

	/**
	 * Boundary history creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveBoundaryHistory(Property property, Long propertyId) {
		Long createdTime = new Date().getTime();
		PropertyLocation boundary = property.getBoundary();

		Object[] boundaryArgs = { boundary.getRevenueBoundary().getId(), boundary.getLocationBoundary().getId(),
				boundary.getAdminBoundary().getId(), boundary.getNorthBoundedBy(), boundary.getEastBoundedBy(),
				boundary.getWestBoundedBy(), boundary.getSouthBoundedBy(), boundary.getAuditDetails().getCreatedBy(),
				boundary.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, propertyId,
				boundary.getId() };

		jdbcTemplate.update(BoundaryBuilder.INSERT_BOUNDARYHISTORY_QUERY, boundaryArgs);

	}

	/**
	 * User property history query
	 * 
	 * @param owner
	 * @param propertyId
	 */
	public void saveUserHistory(User owner, Long propertyId) {
		Long createdTime = new Date().getTime();

		Long id = jdbcTemplate.queryForObject(UserBuilder.GET_OWNERTABLE_ID, new Object[] { owner.getId(), propertyId },
				Long.class);
		Object[] userPropertyArgs = { propertyId, owner.getId(), owner.getIsPrimaryOwner(), owner.getIsSecondaryOwner(),
				owner.getOwnerShipPercentage(), owner.getOwnerType(), owner.getAuditDetails().getCreatedBy(),
				owner.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, id };

		jdbcTemplate.update(UserBuilder.INSERT_USERHISTORY_QUERY, userPropertyArgs);
	}

}
