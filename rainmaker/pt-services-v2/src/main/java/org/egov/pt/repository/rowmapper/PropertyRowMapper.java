package org.egov.pt.repository.rowmapper;


import org.egov.pt.web.models.*;
import org.egov.pt.web.models.Property.CreationReasonEnum;
import org.egov.pt.web.models.PropertyDetail.ChannelEnum;
import org.egov.pt.web.models.PropertyDetail.SourceEnum;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PropertyRowMapper implements ResultSetExtractor<List<Property>> {

	@Override
	public List<Property> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, Property> propertyMap = new HashMap<>();

		while (rs.next()) {

			String currentId = rs.getString("propertyid");
			Property currentProperty = propertyMap.get(currentId);
			String tenanId = rs.getString("tenantId");

			if (null == currentProperty) {

				Boundary locality = Boundary.builder().code(rs.getString("locality")).build();

				/*
				 * id of the address table is being fetched as address key to avoid confusion
				 * with addressId field
				 */
				Address address = Address.builder().addressId(rs.getString("addressId"))
						.addressLine1(rs.getString("addressLine1")).addressLine2(rs.getString("addressLine2"))
						.addressNumber(rs.getString("addressNumber")).buildingName(rs.getString("buildingName"))
						.city(rs.getString("city")).detail(rs.getString("detail")).id(rs.getString("addresskeyid"))
						.landmark(rs.getString("landmark")).latitude(rs.getDouble("latitude")).locality(locality)
						.longitude(rs.getDouble("longitude")).pincode(rs.getString("pincode"))
						.doorNo(rs.getString("doorno"))
						.street(rs.getString("street")).tenantId(tenanId).type(rs.getString("type")).build();

				AuditDetails auditdetails = AuditDetails.builder().createdBy(rs.getString("createdBy"))
						.createdTime(rs.getLong("createdTime")).lastModifiedBy(rs.getString("lastModifiedBy"))
						.lastModifiedTime(rs.getLong("lastModifiedTime"))
						.build();

				currentProperty = Property.builder().address(address)
						.acknowldgementNumber(rs.getString("acknowldgementNumber"))
						.creationReason(CreationReasonEnum.fromValue(rs.getString("creationReason")))
						.occupancyDate(rs.getLong("occupancyDate")).propertyId(currentId)
						.oldPropertyId(rs.getString("oldPropertyId"))
						.status(PropertyInfo.StatusEnum.fromValue(rs.getString("status")))
						.accountId(rs.getString("accountId"))
						.tenantId(tenanId).auditDetails(auditdetails).build();

				propertyMap.put(currentId, currentProperty);
			}

			addChildrenToProperty(rs, currentProperty);
		}
		return new ArrayList<>(propertyMap.values());
	}

	private void addChildrenToProperty(ResultSet rs, Property property) throws SQLException {

		PropertyDetail detail = null;

		String assessmentNumber = rs.getString("assessmentNumber");
		if(!CollectionUtils.isEmpty(property.getPropertyDetails())) {
			for(PropertyDetail propertyDetail:property.getPropertyDetails()){
				if(propertyDetail.getAssessmentNumber().equals(assessmentNumber)){
					detail = propertyDetail;
					break;
				}
			}
		}
		if(detail==null) {
			detail = PropertyDetail.builder()
					.additionalDetails(rs.getObject("additionalDetails")).buildUpArea(rs.getFloat("buildUpArea"))
					.channel(ChannelEnum.fromValue(rs.getString("channel"))).landArea(rs.getFloat("landArea"))
					.noOfFloors(rs.getLong("noOfFloors")).source(SourceEnum.fromValue(rs.getString("source")))
					.usage(rs.getString("usage")).assessmentDate(rs.getLong("assessmentDate"))
					.assessmentNumber(rs.getString("assessmentNumber")).financialYear(rs.getString("financialYear"))
					.propertyType(rs.getString("propertyType")).propertySubType(rs.getString("propertySubType"))
					.ownershipCategory(rs.getString("ownershipCategory"))
					.subOwnershipCategory(rs.getString("subOwnershipCategory"))
					.usageCategoryMajor(rs.getString("usageCategoryMajor"))
					.build();
			property.addpropertyDetailsItem(detail);
		}

		String tenantId = property.getTenantId();

		OwnerInfo owner = OwnerInfo.builder().uuid(rs.getString("userid"))
				          .isPrimaryOwner(rs.getBoolean("isPrimaryOwner"))
				          .ownerType(rs.getString("ownerType"))
				          .ownerShipPercentage(rs.getDouble("ownerShipPercentage"))
				          .build();
		Document document = Document.builder().id(rs.getString("documentid")).documentType(rs.getString("documentType"))
				.fileStore(rs.getString("fileStore")).build();
		Unit unit = Unit.builder().id(rs.getString("unitid")).floorNo(rs.getString("floorNo")).tenantId(tenantId)
				.unitArea(rs.getFloat("unitArea")).unitType(rs.getString("unitType")).usageCategoryMajor(rs.getString("usageCategoryMajor"))
				.usageCategoryMinor(rs.getString("usageCategoryMinor")).usageCategorySubMinor(rs.getString("usageCategorySubMinor"))
				.usageCategoryDetail(rs.getString("usageCategoryDetail"))
				.occupancyType(rs.getString("occupancyType"))
				.occupancyDate(rs.getLong("occupancyDate")).constructionType(rs.getString("constructionType"))
				.constructionSubType(rs.getString("constructionSubType")).arv(rs.getBigDecimal("arv"))
				.build();


		/*
		 * add item methods of models are being used to avoid the null checks
		 */
		detail.addOwnersItem(owner);
		detail.addDocumentsItem(document);
		detail.addUnitsItem(unit);

	}
}
