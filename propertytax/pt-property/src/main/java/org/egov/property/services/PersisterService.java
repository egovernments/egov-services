package org.egov.property.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.egov.models.Address;
import org.egov.models.Document;
import org.egov.models.DocumentType;
import org.egov.models.Floor;
import org.egov.models.OwnerInfo;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.Unit;
import org.egov.models.VacantLandDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class creates a property 
 * @author S Anilkumar
 *
 */
@Service
public class PersisterService {


	@Autowired
	private JdbcTemplate jdbcTemplate;


	/**
	 * 
	 * @param properties
	 * @throws SQLException
	 */
	@Transactional
	public void addProperty(List<Property> properties) throws SQLException {

		createProperty(properties);

	}

	/**
	 * Description : This method will use for insert property related data in database
	 * @param properties
	 */
	public void createProperty(List<Property> properties) {

		//iterating property from properties
		for (Property property : properties) {

			Long createdTime = new Date().getTime();


			// property insertion
			StringBuffer propertySql=new StringBuffer();

			propertySql.append("INSERT INTO egpt_Property(tenantId, upicNumber, oldUpicNumber, vltUpicNumber,")
			.append("creationReason, assessmentDate, occupancyDate, gisRefNo,")
			.append("isAuthorised, isUnderWorkflow, channel, createdBy,lastModifiedBy, createdTime, ")
			.append("lastModifiedTime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");


			final PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(propertySql.toString(), new String[] { "id" });
					ps.setString(1, property.getTenantId());
					ps.setString(2, property.getUpicNumber());
					ps.setString(3, property.getOldUpicNumber());
					ps.setString(4, property.getVltUpicNumber());
					ps.setString(5, property.getCreationReason().toString());
					ps.setString(6, property.getAssessmentDate());
					ps.setString(7, property.getOccupancyDate());
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

			//address insertion
			Address address = property.getAddress();

			StringBuffer addressSql=new StringBuffer();

			addressSql.append("INSERT INTO egpt_Address(tenantId, latitude, longitude, addressId, ")
			.append( "addressNumber, addressLine1, addressLine2, landmark, city, pincode, detail,")
			.append(" createdBy, lastModifiedBy, createdTime, lastModifiedTime, property_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


			Object[] addressArgs = { address.getTenantId(), address.getLatitude(), address.getLongitude(),
					address.getAddressId(),
					address.getAddressNumber(),
					address.getAddressLine1(),
					address.getAddressLine2(),
					address.getLandmark(),
					address.getCity(),
					address.getPincode(),
					address.getDetail(),
					address.getAuditDetails().getCreatedBy(),
					address.getAuditDetails().getLastModifiedBy(),
					createdTime,
					createdTime,
					propertyId
			};

			jdbcTemplate.update(addressSql.toString(), addressArgs);

			//property detail insertion
			PropertyDetail propertyDetails = property.getPropertyDetail();

			StringBuffer propertyDetailsSql=new StringBuffer();

			propertyDetailsSql.append("INSERT INTO egpt_propertydetails(source, regdDocNo, regdDocDate,")
			.append("reason, status, isVerified, verificationDate, isExempted, exemptionReason,")
			.append("propertyType, category, usage, department, apartment, siteLength, siteBreadth, sitalArea,")
			.append(" totalBuiltupArea, undividedShare, noOfFloors, isSuperStructure, landOwner, floorType,")
			.append(" woodType, roofType, wallType, stateId, applicationNo, createdBy, lastModifiedBy, ")
			.append("createdTime, lastModifiedTime, property_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");



			final PreparedStatementCreator pscPropertyDetails = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(propertyDetailsSql.toString(), new String[] { "id" });
					ps.setString(1, propertyDetails.getSource().toString());
					ps.setString(2, propertyDetails.getRegdDocNo());
					ps.setString(3, propertyDetails.getRegdDocDate());
					ps.setString(4, propertyDetails.getReason());
					ps.setString(5, propertyDetails.getStatus().toString());
					ps.setBoolean(6, propertyDetails.getIsVerified());
					ps.setString(7, propertyDetails.getVerificationDate());
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

			//iterating floor from property
			for (Floor floor : property.getPropertyDetail().getFloors()) {

				//floor insertion
				String floorSql="INSERT INTO egpt_floors(floorNo,createdBy, lastModifiedBy, createdTime, lastModifiedTime, property_details_id) values (?,?,?,?,?,?)";

				final PreparedStatementCreator pscFloor = new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
						final PreparedStatement ps = connection.prepareStatement(floorSql, new String[] { "id" });
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

				Integer floorId = holderPropertyDetails.getKey().intValue();

				//unit insertion
				for(Unit unit : floor.getUnits()){

					StringBuffer unitSql=new StringBuffer();

					unitSql.append("INSERT INTO egpt_unit(unitNo, unitType, length,width,builtupArea,assessableArea,")
					.append("bpaBuiltupArea,bpaNo,bpaDate,usage,occupancy,occupierName,firmName,rentCollected, structure, age,")
					.append("exemptionReason, isStructured, occupancyDate, constCompletionDate, manualArv, arv,")
					.append(" electricMeterNo, waterMeterNo, createdBy, lastModifiedBy, createdTime, lastModifiedTime,")
					.append("floor_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");


					Object[] unitArgs = {unit.getUnitNo(), unit.getUnitType().toString(), unit.getLength(), unit.getWidth(),
							unit.getBuiltupArea(), unit.getAssessableArea(), unit.getBpaBuiltupArea(), unit.getBpaNo(),
							unit.getBpaDate(),unit.getUsage(),unit.getOccupancy(),unit.getOccupierName(), unit.getFirmName(),
							unit.getRentCollected(),unit.getStructure(), unit.getAge(), unit.getExemptionReason(),
							unit.getIsStructured(), unit.getOccupancyDate(), unit.getConstCompletionDate(), unit.getManualArv(),
							unit.getArv(), unit.getElectricMeterNo(), unit.getWaterMeterNo(),
							unit.getAuditDetails().getCreatedBy(), unit.getAuditDetails().getLastModifiedBy(),
							createdTime, createdTime,
							floorId};

					jdbcTemplate.update(unitSql.toString(), unitArgs);

				}


			}

			//iterating document from property
			for (Document document : property.getPropertyDetail().getDocuments()) {

				// document insertion

				String documentSql = "INSERT INTO  egpt_document(fileStore, createdBy, lastModifiedBy, createdTime, lastModifiedTime, property_details_id) values (?,?,?,?,?,?)";

				final PreparedStatementCreator pscDocument = new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
						final PreparedStatement ps = connection.prepareStatement(documentSql,
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


				//document type insertion

				DocumentType documentType = document.getDocumentType();

				String documentTypeSql = "INSERT INTO egpt_documenttype(name,application,createdBy,document_id,lastModifiedBy, createdTime, lastModifiedTime) values (?,?,?,?,?,?,?)";

				final PreparedStatementCreator pscDocumentType = new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
						final PreparedStatement ps = connection.prepareStatement(documentTypeSql,
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

			//vacantland property insertion
			VacantLandDetail vacantLand = property.getVacantLand();
			StringBuffer vaccantLandSql=new StringBuffer();
			vaccantLandSql.append("insert into egpt_vacantland(surveyNumber,pattaNumber,")
			.append("marketValue,capitalValue,layoutApprovedAuth,layoutPermissionNo,layoutPermissionDate,")
			.append("resdPlotArea,nonResdPlotArea,createdBy, lastModifiedBy, createdTime,lastModifiedTime,property_id)")
			.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			Object[] vaccantLandArgs = {vacantLand.getSurveyNumber(),
					vacantLand.getPattaNumber(), vacantLand.getMarketValue(), vacantLand.getCapitalValue(),
					vacantLand.getLayoutApprovedAuth(), vacantLand.getLayoutPermissionNo(),
					vacantLand.getLayoutPermissionDate(), vacantLand.getResdPlotArea(), vacantLand.getNonResdPlotArea(),
					vacantLand.getAuditDetails().getCreatedBy(), 
					vacantLand.getAuditDetails().getLastModifiedBy(),
					createdTime,createdTime, propertyId };

			jdbcTemplate.update(vaccantLandSql.toString(), vaccantLandArgs);

			//boundary insertion
			PropertyLocation boundary = property.getBoundary();
			StringBuffer boundarySql=new StringBuffer();
			boundarySql.append("insert into egpt_propertylocation(revenueBoundary, locationBoundary,")
			.append(" adminBoundary, northBoundedBy,eastBoundedBy, westBoundedBy,")
			.append("southBoundedBy,createdBy, lastModifiedBy, createdTime,lastModifiedTime, property_id) values (?,?,?,?,?,?,?,?,?,?,?,?)");

			Object[] boundaryArgs = { boundary.getRevenueBoundary().getId(),boundary.getLocationBoundary().getId(),boundary.getAdminBoundary().getId(),
					boundary.getNorthBoundedBy(),boundary.getEastBoundedBy(),boundary.getWestBoundedBy(),
					boundary.getSouthBoundedBy(),
					boundary.getAuditDetails().getCreatedBy(), 
					boundary.getAuditDetails().getLastModifiedBy(),
					createdTime,
					createdTime,
					propertyId};

			jdbcTemplate.update(boundarySql.toString(), boundaryArgs);


			//property and user relation table insertion
			for (OwnerInfo owner : property.getOwners()) {
				StringBuffer userPropertySql = new StringBuffer();
				userPropertySql.append("insert into egpt_Property_user(property_Id, user_Id,isPrimaryOwner,")
				.append("isSecondaryOwner,ownerShipPercentage, ownerType, createdBy, lastModifiedBy, createdTime,lastModifiedTime)")
				.append("values (?,?,?,?,?,?,?,?,?,?);");
				Object[] userPropertyArgs = { propertyId, owner.getId(),
						owner.getIsPrimaryOwner(),						
						owner.getIsSecondaryOwner(),
						owner.getOwnerShipPercentage(),
						owner.getOwnerType(),
						owner.getAuditDetails().getCreatedBy(),
						owner.getAuditDetails().getLastModifiedBy(),
						createdTime,
						createdTime,
				};

				jdbcTemplate.update(userPropertySql.toString(), userPropertyArgs);

			}

		}
	}

	/**
	 * update method
	 * @param: List of properties
	 * This method updates:
	 * 1. Property
	 * 2. Address
	 * 3. Property Details
	 * 4. Vacant Land
	 * 5. Floor
	 * 6. Document and Document type
	 * 7. User
	 * 8. Boundary
	 **/

	@Transactional
	public void updateProperty(List<Property> properties) throws SQLException {

		for (Property property : properties) {

			//Property updated time
			Long updatedTime = new Date().getTime();

			//Update Property Query
			StringBuffer propertyUpdateSQL=new StringBuffer();

			propertyUpdateSQL.append("UPDATE egpt_Property")
			.append(" SET tenantId = ? , upicNumber = ?, oldUpicNumber = ?, vltUpicNumber = ?,")
			.append("creationReason = ?, assessmentDate = ?, occupancyDate = ?, gisRefNo = ?,")
			.append(" isAuthorised = ?, isUnderWorkflow = ?, channel = ?,")
			.append(" lastModifiedBy = ?, lastModifiedTime = ?")
			.append(" WHERE id = " + property.getId());

			final PreparedStatementCreator property_psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(propertyUpdateSQL.toString());
					ps.setString(1, property.getTenantId());
					ps.setString(2, property.getUpicNumber());
					ps.setString(3, property.getOldUpicNumber());
					ps.setString(4, property.getVltUpicNumber());
					ps.setString(5, property.getCreationReason().name());
					ps.setString(6, property.getAssessmentDate());
					ps.setString(7, property.getOccupancyDate());
					ps.setString(8, property.getGisRefNo());
					ps.setBoolean(9, property.getIsAuthorised());
					ps.setBoolean(10, property.getIsUnderWorkflow());
					ps.setString(11, property.getChannel().name());
					ps.setString(12, property.getAuditDetails().getLastModifiedBy());
					ps.setLong(13, updatedTime);
					return ps;
				}
			};

			jdbcTemplate.update(property_psc);

			//2.Address address
			Address address = property.getAddress();
			Long address_id = property.getAddress().getId();

			//Update Address Query
			StringBuffer addressUpdateSQL=new StringBuffer();

			addressUpdateSQL.append("UPDATE egpt_Address")
			.append(" SET tenantId = ?, latitude = ?, longitude = ?, addressId = ?,")
			.append(" addressNumber = ?, addressLine1 = ?, addressLine2 = ?,")
			.append(" landmark = ?, city = ?, pincode = ?, detail = ?, lastModifiedBy = ?,")
			.append(" lastModifiedTime = ?, property_id = ?" )
			.append(" WHERE id = " + address_id);

			Object[] addressArgs = {
					address.getTenantId(), address.getLatitude(), 
					address.getLongitude(),address.getAddressId(), 
					address.getAddressNumber(), address.getAddressLine1(),
					address.getAddressLine2(), address.getLandmark(), 
					address.getCity(), address.getPincode(),
					address.getDetail(), address.getAuditDetails().getLastModifiedBy(), 
					updatedTime,property.getId()
			};

			jdbcTemplate.update(addressUpdateSQL.toString(), addressArgs);


			//3.Property Details
			PropertyDetail propertyDetails = property.getPropertyDetail();
			//Update Address Query and Conditions
			StringBuffer propertyDetailUpdateSQL=new StringBuffer();

			propertyDetailUpdateSQL.append("UPDATE egpt_propertydetails")
			.append(" SET source = ?, regdDocNo = ?, regdDocDate = ?, reason = ?,")
			.append(" status = ?, isVerified = ?, verificationDate = ?, isExempted = ?,")
			.append(" exemptionReason = ?, propertyType = ?, category = ?, usage = ?, department = ?,")
			.append(" apartment = ?, siteLength = ?, siteBreadth = ?, sitalArea = ?, totalBuiltupArea = ?,")
			.append(" undividedShare = ?, noOfFloors = ?, isSuperStructure = ?, landOwner = ?,")
			.append(" floorType = ?, woodType = ?, roofType = ?, wallType = ?, stateId = ?,")
			.append(" applicationNo = ?, lastModifiedBy = ?, lastmodifiedtime = ?, property_id = ?")
			.append(" WHERE id = " + propertyDetails.getId());

			final PreparedStatementCreator details_psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
					final PreparedStatement ps = connection.prepareStatement(propertyDetailUpdateSQL.toString());
					ps.setString(1, propertyDetails.getSource().toString());
					ps.setString(2, propertyDetails.getRegdDocNo());
					ps.setString(3, propertyDetails.getRegdDocDate());
					ps.setString(4, propertyDetails.getReason());
					ps.setString(5, propertyDetails.getStatus().toString());
					ps.setBoolean(6, propertyDetails.getIsVerified());
					ps.setString(7, propertyDetails.getVerificationDate());
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
					ps.setString(29, propertyDetails.getAuditDetails().getLastModifiedBy());
					ps.setLong(30, updatedTime);
					ps.setLong(31, property.getId());
					return ps;
				}
			};

			jdbcTemplate.update(details_psc);

			//4.Vacant Land
			VacantLandDetail vacantLand = property.getVacantLand();

			Long vacantland_id = vacantLand.getId();

			//Update Vacant land Query
			StringBuffer vacantlandUpdateSQL=new StringBuffer();

			vacantlandUpdateSQL.append("UPDATE egpt_vacantland")
			.append(" SET surveyNumber = ?, pattaNumber = ?, marketValue = ?, capitalValue = ?, layoutApprovedAuth = ?,")
			.append(" layoutPermissionNo = ?, layoutPermissionDate = ?, resdPlotArea = ?, ")
			.append(" nonResdPlotArea = ?, lastModifiedBy = ?, lastModifiedTime = ?, property_id = ?")
			.append(" WHERE id = " + vacantland_id);

			Object[] vaccantLandArgs = {
					vacantLand.getSurveyNumber(), vacantLand.getPattaNumber(), vacantLand.getMarketValue(), 
					vacantLand.getCapitalValue(),vacantLand.getLayoutApprovedAuth(), vacantLand.getLayoutPermissionNo(),
					vacantLand.getLayoutPermissionDate(), vacantLand.getResdPlotArea(), vacantLand.getNonResdPlotArea(),
					vacantLand.getAuditDetails().getLastModifiedBy(),updatedTime,property.getId() 
			};

			jdbcTemplate.update(vacantlandUpdateSQL.toString(), vaccantLandArgs);


			//5.Floor
			for (Floor floor : property.getPropertyDetail().getFloors()) {

				Long floor_id = floor.getId();
				//Floor Query
				StringBuffer floorUpdateSQL=new StringBuffer();

				//details_id
				floorUpdateSQL.append("UPDATE egpt_floors")
				.append(" SET floorNo = ?, lastModifiedBy = ?, lastModifiedTime = ?, property_details_id = ? ")
				.append(" WHERE id = " + floor_id);

				Object[] floorArgs = { 
						floor.getFloorNo(), floor.getAuditDetails().getLastModifiedBy(), 
						updatedTime, propertyDetails.getId()
				};

				jdbcTemplate.update(floorUpdateSQL.toString(), floorArgs);

				//unit insertion
				for(Unit unit : floor.getUnits()){

					StringBuffer unitSql=new StringBuffer();
					Long unit_id = unit.getId();

					unitSql.append("UPDATE egpt_unit")
					.append(" SET unitNo = ?, unitType = ?, length = ?, width = ?,")
					.append(" builtupArea = ?, assessableArea = ?, bpaBuiltupArea = ?,")
					.append(" bpaNo = ?, bpaDate = ?, usage = ?, occupancy = ?, occupierName = ?,")
					.append(" firmName = ?, rentCollected = ?, structure = ?, age = ?,")
					.append(" exemptionReason = ?, isStructured = ?, occupancyDate = ?,")
					.append(" constCompletionDate = ?, manualArv = ?, arv = ?,")
					.append(" electricMeterNo = ?, waterMeterNo = ?, lastModifiedBy = ?, lastModifiedTime = ?")
					.append(" WHERE id = " + unit_id);


					Object[] unitArgs = {
							unit.getUnitNo(), unit.getUnitType().toString(), 
							unit.getLength(), unit.getWidth(),
							unit.getBuiltupArea(), unit.getAssessableArea(),
							unit.getBpaBuiltupArea(), unit.getBpaNo(),
							unit.getBpaDate(),unit.getUsage(),
							unit.getOccupancy(),unit.getOccupierName(), 
							unit.getFirmName(),unit.getRentCollected(),
							unit.getStructure(), unit.getAge(), unit.getExemptionReason(),
							unit.getIsStructured(), unit.getOccupancyDate(), unit.getConstCompletionDate(), unit.getManualArv(),
							unit.getArv(), unit.getElectricMeterNo(), unit.getWaterMeterNo(),
							unit.getAuditDetails().getLastModifiedBy(),
							updatedTime
					};

					jdbcTemplate.update(unitSql.toString(), unitArgs);

				}
			}
			//6.Document
			for (Document document : property.getPropertyDetail().getDocuments()) {


				//Document Query
				Long document_id = document.getId();
				StringBuffer documentUpdateSQL=new StringBuffer();

				documentUpdateSQL.append("UPDATE egpt_document")
				.append(" SET  fileStore = ?, lastModifiedBy = ?, lastModifiedTime = ?, property_details_id = ? ")
				.append(" WHERE id = "+ document_id);

				Object[] documentArgs = { 
						document.getFileStore(),
						document.getAuditDetails().getLastModifiedBy(),updatedTime, propertyDetails.getId()
				};

				jdbcTemplate.update(documentUpdateSQL.toString(), documentArgs);

				DocumentType documentType = document.getDocumentType();

				//Document Type Query
				Long documenttype_id = document.getDocumentType().getId();
				StringBuffer documentTypeUpdateSQL=new StringBuffer();

				documentTypeUpdateSQL.append("UPDATE egpt_documenttype")
				.append(" SET name = ?, application = ?, document_id = ?, lastModifiedBy = ?, lastModifiedTime = ?")
				.append(" WHERE id = " + documenttype_id);

				Object[] documentTypeArgs = { 
						documentType.getName(), documentType.getApplication().name(),
						document_id,
						documentType.getAuditDetails().getLastModifiedBy(), updatedTime
				};

				jdbcTemplate.update(documentTypeUpdateSQL.toString(),documentTypeArgs);

			}

			//7.User
			for (OwnerInfo owner : property.getOwners()) {

				Long user_id = owner.getId();

				//User Query
				StringBuffer userUpdateSQL=new StringBuffer();

				userUpdateSQL.append("UPDATE egpt_Property_user")
				.append(" SET property_Id = ?, user_Id = ?, isPrimaryOwner = ?,")
				.append(" isSecondaryOwner = ?, ownerShipPercentage = ?, ownerType = ?,")
				.append(" lastModifiedBy = ?, lastModifiedTime = ?")
				.append(" WHERE id = " + user_id);

				Object[] userPropertyArgs = { 
						property.getId(), user_id, owner.getIsPrimaryOwner().booleanValue(),
						owner.getIsSecondaryOwner().booleanValue(), owner.getOwnerShipPercentage(), 
						owner.getOwnerType(),owner.getAuditDetails().getLastModifiedBy(), 
						updatedTime
				}; 

				jdbcTemplate.update(userUpdateSQL.toString(), userPropertyArgs);

			}

			//8.Boundary
			PropertyLocation boundary = property.getBoundary();

			Long boundary_id = boundary.getId();

			StringBuffer boundaryUpdateSQL = new StringBuffer();

			//Boundary query
			boundaryUpdateSQL.append("UPDATE egpt_propertylocation")
			.append(" SET revenueBoundary = ?, locationBoundary = ?, adminBoundary = ?,")
			.append(" northBoundedBy = ?, eastBoundedBy = ?, westBoundedBy = ?, southBoundedBy = ?,")
			.append(" lastModifiedBy = ?, lastModifiedTime = ?, property_id = ?")
			.append(" WHERE id = " + boundary_id);

			Object[] boundaryArgs = { 
					boundary.getRevenueBoundary().getId(),
					boundary.getLocationBoundary().getId(),
					boundary.getAdminBoundary().getId(),
					boundary.getNorthBoundedBy().toString(),
					boundary.getEastBoundedBy().toString(),
					boundary.getWestBoundedBy().toString(),
					boundary.getSouthBoundedBy().toString(),
					boundary.getAuditDetails().getLastModifiedBy(),
					updatedTime,property.getId()
			};

			jdbcTemplate.update(boundaryUpdateSQL.toString(), boundaryArgs);

		}
	}

}
