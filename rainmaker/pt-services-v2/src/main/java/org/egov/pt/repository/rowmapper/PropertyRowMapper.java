package org.egov.pt.repository.rowmapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.pt.web.models.Address;
import org.egov.pt.web.models.Boundary;
import org.egov.pt.web.models.Document;
import org.egov.pt.web.models.OwnerInfo;
import org.egov.pt.web.models.Property;
import org.egov.pt.web.models.Property.CreationReasonEnum;
import org.egov.pt.web.models.Property.StatusEnum;
import org.egov.pt.web.models.PropertyDetail;
import org.egov.pt.web.models.PropertyDetail.ChannelEnum;
import org.egov.pt.web.models.PropertyDetail.SourceEnum;
import org.egov.pt.web.models.Unit;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

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

				PropertyDetail detail = PropertyDetail.builder().id(rs.getString("propertydetailid"))
						.additionalDetails(rs.getString("additionalDetails")).buildUpArea(rs.getFloat("buildUpArea"))
						.channel(ChannelEnum.fromValue(rs.getString("channel"))).landArea(rs.getFloat("landArea"))
						.noOfFloors(rs.getLong("noOfFloors")).source(SourceEnum.fromValue(rs.getString("source")))
						.usage(rs.getString("usage")).build();

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
						.street(rs.getString("street")).tenantId(tenanId).type(rs.getString("type")).build();

				currentProperty = Property.builder().propertyDetail(detail).address(address)
						.acknowldgementNumber(rs.getString("acknowldgementNumber"))
						.assessmentDate(rs.getLong("assessmentDate")).assessmentNumber(rs.getString("assessmentNumber"))
						.creationReason(CreationReasonEnum.fromValue(rs.getString("creationReason")))
						.financialYear(rs.getString("financialYear")).id(currentId)
						.occupancyDate(rs.getLong("occupancyDate"))
						.oldAssessmentNumber(rs.getString("oldAssessmentNumber"))
						.propertyType(rs.getString("propertyType")).status(StatusEnum.fromValue(rs.getString("status")))
						.propertySubType(rs.getString("propertySubType")).usageCategoryMajor(rs.getString("usageCategoryMajor"))
						.tenantId(tenanId).build();

				propertyMap.put(currentId, currentProperty);
			}

			addChildrenToProperty(rs, currentProperty);
		}
		return new ArrayList<>(propertyMap.values());
	}

	private void addChildrenToProperty(ResultSet rs, Property property) throws SQLException {

		PropertyDetail detail = property.getPropertyDetail();
		String tenantId = property.getTenantId();

		OwnerInfo owner = OwnerInfo.builder().id(rs.getString("userid")).build();
		Document document = Document.builder().id(rs.getString("documentid")).documentType(rs.getString("documentType"))
				.fileStore(rs.getString("fileStore")).build();
		Unit unit = Unit.builder().id(rs.getString("unitid")).floorNo(rs.getString("floorNo")).tenantId(tenantId)
				.unitArea(rs.getFloat("unitArea")).unitType(rs.getString("unitType")).usageCategoryMajor(rs.getString("usageCategoryMajor"))
				.usageCategoryMinor(rs.getString("usageCategoryMinor")).usageCategorySubMinor(rs.getString("usageCategorySubMinor"))
				.usageCategoryDetail(rs.getString("usageCategoryDetail"))
				.occupancyType(Unit.occupancyTypeEnum.fromValue(rs.getString("occupancyType")))
				.occupancyDate(rs.getLong("occupancyDate")).constructionType(rs.getString("constructionType"))
				.constructionSubType(rs.getString("constructionSubType")).arv(rs.getDouble("arv"))
				.build();


		/*
		 * add item methods of models are being used to avoid the null checks
		 */
		property.addOwnersItem(owner);
		detail.addDocumentsItem(document);
		detail.addUnitsItem(unit);
	}
}
