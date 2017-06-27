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
import org.egov.property.util.TimeStampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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

	/**
	 * property query formation
	 * 
	 * @param property
	 * @return holder key
	 */

	public Integer saveProperty(Property property) {
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
				ps.setLong(14, createdTime);
				ps.setLong(15, createdTime);
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
				ps.setString(2, propertyDetails.getRegdDocNo());
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
				ps.setDouble(15, propertyDetails.getSiteLength());
				ps.setDouble(16, propertyDetails.getSiteBreadth());
				ps.setDouble(17, propertyDetails.getSitalArea());
				ps.setDouble(18, propertyDetails.getTotalBuiltupArea());
				ps.setDouble(19, propertyDetails.getUndividedShare());
				ps.setLong(20, propertyDetails.getNoOfFloors());
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
				ps.setLong(31, createdTime);
				ps.setLong(32, createdTime);
				ps.setInt(33, propertyId);
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
				ps.setLong(4, createdTime);
				ps.setLong(5, createdTime);
				ps.setInt(6, propertyDetailsId);
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
	public void saveUnit(Unit unit, Integer floorId) {

		Long createdTime = new Date().getTime();

		Object[] unitArgs = { unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
				unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
				TimeStampUtil.getTimeStamp(unit.getBpaDate()), unit.getUsage(), unit.getOccupancyType(),
				unit.getOccupierName(), unit.getFirmName(), unit.getRentCollected(), unit.getStructure(), unit.getAge(),
				unit.getExemptionReason(), unit.getIsStructured(), TimeStampUtil.getTimeStamp(unit.getOccupancyDate()),
				TimeStampUtil.getTimeStamp(unit.getConstCompletionDate()), unit.getManualArv(), unit.getArv(),
				unit.getElectricMeterNo(), unit.getWaterMeterNo(), unit.getAuditDetails().getCreatedBy(),
				unit.getAuditDetails().getLastModifiedBy(), createdTime, createdTime, floorId };

		jdbcTemplate.update(UnitBuilder.INSERT_UNIT_QUERY, unitArgs);

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
				ps.setLong(4, createdTime);
				ps.setLong(5, createdTime);
				ps.setLong(6, propertyDetailsId);
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

	public void updateProperty(Property property) {

		Long updatedTime = new Date().getTime();

		String propertyUpdate = PropertyBuilder.updatePropertyQuery();

		Object[] propertyArgs = { property.getTenantId(), property.getUpicNumber(), property.getOldUpicNumber(),
				property.getVltUpicNumber(), property.getCreationReason().name(),
				TimeStampUtil.getTimeStamp(property.getAssessmentDate()),
				TimeStampUtil.getTimeStamp(property.getOccupancyDate()), property.getGisRefNo(),
				property.getIsAuthorised(), property.getIsUnderWorkflow(), property.getChannel().name(),
				property.getAuditDetails().getLastModifiedBy(), updatedTime, property.getId() };

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

	public void updatePropertyDetail(PropertyDetail propertyDetails, Long proertyId) {

		Long updatedTime = new Date().getTime();

		String propertyDetailsUpdate = PropertyDetailBuilder.updatePropertyDetailQuery();

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
				propertyDetails.getAuditDetails().getLastModifiedBy(), updatedTime, proertyId,
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
				updatedTime, unit.getId() };

		jdbcTemplate.update(unitUpdate, unitArgs);

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

}
