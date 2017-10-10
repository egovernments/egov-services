package org.egov.tl.indexer.service;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class SearchUtil {

	public static BoolQueryBuilder buildSearchQuery(String tenantId, String active, String tradeLicenseId,
			String applicationNumber, String licenseNumber, String oldLicenseNumber, String mobileNumber,
			String aadhaarNumber, String emailId, String propertyAssesmentNo, String adminWard, String locality,
			String ownerName, String tradeTitle, String tradeType, String tradeCategory, String tradeSubCategory,
			String legacy, String status) {

		BoolQueryBuilder bool = QueryBuilders.boolQuery();

		if (tenantId != null && !tenantId.isEmpty()) {
			bool.must(QueryBuilders.termQuery("tenantId", tenantId.trim()));
		}

		if (active != null && !active.isEmpty()) {
			Boolean isactive = Boolean.FALSE;
			if (active.equalsIgnoreCase("true"))
				isactive = Boolean.TRUE;

			else if (active.equalsIgnoreCase("false"))
				isactive = Boolean.FALSE;

			bool.must(QueryBuilders.termQuery("active", isactive));

		}

		if (tradeLicenseId != null && !tradeLicenseId.isEmpty())
			bool.must(QueryBuilders.termQuery("id", tradeLicenseId));

		if (applicationNumber != null && !applicationNumber.isEmpty())
			bool.must(QueryBuilders.termQuery("applicationNumber", applicationNumber));

		if (oldLicenseNumber != null && !oldLicenseNumber.isEmpty())
			bool.must(QueryBuilders.termQuery("oldLicenseNumber", oldLicenseNumber));

		if (licenseNumber != null && !licenseNumber.isEmpty())
			bool.must(QueryBuilders.termQuery("licenseNumber", licenseNumber));

		if (mobileNumber != null && !mobileNumber.isEmpty())
			bool.must(QueryBuilders.termQuery("mobileNumber", mobileNumber));

		if (aadhaarNumber != null && !aadhaarNumber.isEmpty())
			bool.must(QueryBuilders.termQuery("adhaarNumber", aadhaarNumber));

		if (emailId != null && !emailId.isEmpty())
			bool.must(QueryBuilders.termQuery("emailId", emailId.trim()));

		if (propertyAssesmentNo != null && !propertyAssesmentNo.isEmpty())
			bool.must(QueryBuilders.termQuery("propertyAssesmentNo", propertyAssesmentNo));

		if (adminWard != null && !adminWard.trim().isEmpty())
			bool.must(QueryBuilders.termQuery("adminWard", adminWard));

		if (locality != null && !locality.trim().isEmpty())
			bool.must(QueryBuilders.termQuery("locality", locality));

		if (ownerName != null && !ownerName.isEmpty())
			bool.must(QueryBuilders.termQuery("ownerName", ownerName));

		if (tradeTitle != null && !tradeTitle.isEmpty())
			bool.must(QueryBuilders.termQuery("tradeTitle", tradeTitle));

		if (tradeType != null && !tradeTitle.isEmpty())
			bool.must(QueryBuilders.termQuery("tradeType", tradeType));

		if (tradeCategory != null && !tradeCategory.trim().isEmpty())
			bool.must(QueryBuilders.termQuery("category", tradeCategory));

		if (tradeSubCategory != null && !tradeSubCategory.trim().isEmpty())
			bool.must(QueryBuilders.termQuery("subCategory", tradeSubCategory));

		if (legacy != null && !legacy.isEmpty()) {
			Boolean isLegacy = Boolean.FALSE;
			if (legacy.equalsIgnoreCase("true"))
				isLegacy = Boolean.TRUE;

			else if (legacy.equalsIgnoreCase("false"))
				isLegacy = Boolean.FALSE;

			bool.must(QueryBuilders.termQuery("isLegacy", isLegacy));

		}

		if (status != null && !status.trim().isEmpty())
			bool.must(QueryBuilders.termQuery("status", status));

		return bool;
	}

}
