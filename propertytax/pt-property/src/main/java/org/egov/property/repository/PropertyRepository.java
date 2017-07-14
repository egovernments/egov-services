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
import org.egov.models.Document;
import org.egov.models.DocumentType;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.RequestInfo;
import org.egov.models.Unit;
import org.egov.models.User;
import org.egov.models.VacantLandDetail;
import org.egov.property.model.PropertyLocationRowMapper;
import org.egov.property.model.PropertyUser;
import org.egov.property.repository.builder.AddressBuilder;
import org.egov.property.repository.builder.BoundaryBuilder;
import org.egov.property.repository.builder.DocumentBuilder;
import org.egov.property.repository.builder.DocumentTypeBuilder;
import org.egov.property.repository.builder.FloorBuilder;
import org.egov.property.repository.builder.PropertyBuilder;
import org.egov.property.repository.builder.PropertyDetailBuilder;
import org.egov.property.repository.builder.SearchPropertyBuilder;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	public Integer saveProperty(Property property) throws Exception {
		Long createdTime = new Date().getTime();

		ObjectMapper obj = new ObjectMapper();

		String demands = obj.writeValueAsString(property.getDemands());

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

				PGobject jsonObject = new PGobject();
				jsonObject.setType("jsonb");
				jsonObject.setValue(demands);

				ps.setObject(16, property.getDemands());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

		Integer propertyId = holder.getKey().intValue();

		return propertyId;

	}

	/**
	 * Address creation query
	 * 
	 * @param property
	 * @param propertyId
	 */
	public void saveAddress(Property property, Integer propertyId) {
		Long createdTime = new Date().getTime();

		Address address = property.getAddress();

		Object[] addressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
				address.getAddressId(), address.getAddressNumber(), address.getAddressLine1(),
				address.getAddressLine2(), address.getLandmark(), address.getCity(), address.getPincode(),
				address.getDetail(), address.getAuditDetails().getCreatedBy(),
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
	public Integer savePropertyDetails(Property property, Integer propertyId) {
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
				ps.setInt(33, getInteger(propertyId));
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

		Integer propertyDetailsId = holderPropertyDetails.getKey().intValue();

		return propertyDetailsId;

	}

	/**
	 * Floor creation query
	 * 
	 * @param floor
	 * @param propertyDetailsId
	 * @return key holder id
	 */
	public Integer saveFloor(Floor floor, Integer propertyDetailsId) {

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
				ps.setInt(6, getInteger(propertyDetailsId));
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holderFloor = new GeneratedKeyHolder();

		jdbcTemplate.update(pscFloor, holderFloor);

		Integer floorId = holderFloor.getKey().intValue();

		return floorId;

	}

	/**
	 * unit creation query
	 * 
	 * @param unit
	 * @param floorId
	 */
	public Integer saveUnit(Unit unit, Integer floorId) {

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
				ps.setInt(29, getInteger(floorId));
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holderUnit = new GeneratedKeyHolder();

		jdbcTemplate.update(pscUnit, holderUnit);

		Integer unitId = holderUnit.getKey().intValue();

		return unitId;

	}

	/**
	 * room creation query
	 * 
	 * @param unit
	 * @param floorId
	 */
	public void saveRoom(Unit unit, Integer floorId, Integer parent) {

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
	public Integer saveDocument(Document document, Integer propertyDetailsId) {
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
				ps.setInt(6, getInteger(propertyDetailsId));
				return ps;
			}
		};

		final KeyHolder holderDocument = new GeneratedKeyHolder();

		jdbcTemplate.update(pscDocument, holderDocument);

		Integer documentId = holderDocument.getKey().intValue();

		return documentId;
	}

	/**
	 * DocumentTYpe creation query
	 * 
	 * @param documentType
	 * @param documentId
	 */
	public void saveDocumentType(DocumentType documentType, Integer documentId) {
		Long createdTime = new Date().getTime();

		final PreparedStatementCreator pscDocumentType = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(DocumentTypeBuilder.INSERT_DOCUMENTTYPE_QUERY,
						new String[] { "id" });
				ps.setString(1, documentType.getName());
				ps.setString(2, documentType.getApplication().toString());
				ps.setString(3, documentType.getAuditDetails().getCreatedBy());
				ps.setLong(4, documentId);
				ps.setString(5, documentType.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, createdTime);
				ps.setLong(7, createdTime);
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holderDocumentType = new GeneratedKeyHolder();

		jdbcTemplate.update(pscDocumentType, holderDocumentType);

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
	public void saveVacantLandDetail(Property property, Integer propertyId) {
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
	public void saveBoundary(Property property, Integer propertyId) {
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
	public void saveUser(User owner, Integer propertyId) {
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

		Map<String, Object> propertyMap = searchPropertyBuilder.createSearchPropertyQuery(requestInfo, tenantId, active,
				upicNo, pageSize, pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone,
				revenueWard, locality, ownerName, demandFrom, demandTo);

		List<Property> properties = jdbcTemplate.query(propertyMap.get("Sql").toString(),
				new BeanPropertyRowMapper(Property.class));

		searchPropertyMap.put("properties", properties);
		searchPropertyMap.put("users", propertyMap.get("users"));

		return searchPropertyMap;

	}

	public Address getAddressByProperty(Long propertyId) {

		Address address = (Address) jdbcTemplate.queryForObject(AddressBuilder.ADDRES_BY_PROPERTY_ID_QUERY,
				new Object[] { propertyId }, new BeanPropertyRowMapper(Address.class));

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

		return propertyDetail;

	}

	public VacantLandDetail getVacantLandByProperty(Long propertyId) {
		VacantLandDetail vacantLandDetail = (VacantLandDetail) jdbcTemplate.queryForObject(
				VacantLandDetailBuilder.VACANT_LAND_BY_PROPERTY_QUERY, new Object[] { propertyId },
				new BeanPropertyRowMapper(VacantLandDetail.class));

		return vacantLandDetail;
	}

	public PropertyLocation getPropertyLocationByproperty(Long propertyId) {
		PropertyLocation propertyLocation = (PropertyLocation) jdbcTemplate.queryForObject(
				BoundaryBuilder.PROPERTY_LOCATION_BY_PROPERTY_QUERY, new Object[] { propertyId },
				new PropertyLocationRowMapper());

		return propertyLocation;
	}

	public List<Floor> getFloorsByPropertyDetails(Long propertyDetailId) {

		List<Floor> floors = jdbcTemplate.query(FloorBuilder.FLOORS_BY_PROPERTY_DETAILS_QUERY,
				new Object[] { propertyDetailId }, new BeanPropertyRowMapper(Floor.class));
		return floors;

	}

	public List<Unit> getUnitsByFloor(Long floorId) {

		List<Unit> units = jdbcTemplate.query(UnitBuilder.UNITS_BY_FLOOR_QUERY, new Object[] { floorId },
				new BeanPropertyRowMapper(Unit.class));

		return units;

	}

	public List<Document> getDocumentByPropertyDetails(Long propertyDetailId) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(DocumentBuilder.DOCUMENT_BY_PROPERTY_DETAILS_QUERY,
				new Object[] { propertyDetailId });
		List<Document> documents = getDocumentObject(rows);

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
			String documentTypeSQl = DocumentTypeBuilder.DOCUMENT_TYPE_BY_DOCUMENT;
			Integer documentId = Integer.valueOf(documentdata.get("id").toString());
			DocumentType documentType = (DocumentType) jdbcTemplate.queryForObject(documentTypeSQl,
					new Object[] { documentId }, new BeanPropertyRowMapper(DocumentType.class));
			document.setDocumentType(documentType);
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
				address.getAddressId(), address.getAddressNumber(), address.getAddressLine1(),
				address.getAddressLine2(), address.getLandmark(), address.getCity(), address.getPincode(),
				address.getDetail(), address.getAuditDetails().getLastModifiedBy(), updatedTime, proertyId,
				address_id };

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
				updatedTime, unit.getParentId(), unit.getId() };

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
				updatedTime, unit.getParentId(), unit.getId() };

		jdbcTemplate.update(roomUpdate, roomArgs);

	}

	public void updateDocument(Document document, Long propertyDetailsId) {

		Long updatedTime = new Date().getTime();

		String documentUpdate = DocumentBuilder.updateDocumentQuery();

		Object[] documentArgs = { document.getFileStore(), document.getAuditDetails().getLastModifiedBy(), updatedTime,
				propertyDetailsId, document.getId() };

		jdbcTemplate.update(documentUpdate, documentArgs);
	}

	public void updateDocumentType(DocumentType documentType, Long documentId) {

		Long updatedTime = new Date().getTime();

		String documentTypeUpdate = DocumentTypeBuilder.updateDocumentTypeQuery();

		Object[] documentTypeArgs = { documentType.getName(), documentType.getApplication().name(), documentId,
				documentType.getAuditDetails().getLastModifiedBy(), updatedTime, documentType.getId() };

		jdbcTemplate.update(documentTypeUpdate, documentTypeArgs);
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

}