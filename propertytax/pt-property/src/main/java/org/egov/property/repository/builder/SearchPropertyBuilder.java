package org.egov.property.repository.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.models.RequestInfo;
import org.egov.models.User;
import org.egov.models.UserResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchPropertyBuilder {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	Environment environment;

	@Autowired
	RestTemplate restTemplate;

	public Map<String, Object> createSearchPropertyQuery(RequestInfo requestInfo, String tenantId, Boolean active,
			String upicNo, int pageSize, int pageNumber, String[] sort, String oldUpicNo, String mobileNumber,
			String aadhaarNumber, String houseNoBldgApt, int revenueZone, int revenueWard, int locality,
			String ownerName, int demandFrom, int demandTo) {

		StringBuffer searchPropertySql = new StringBuffer();

		Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();

		if (ownerName != null && !ownerName.isEmpty())
			userSearchRequestInfo.put("user.name", ownerName);

		if (mobileNumber != null && !mobileNumber.isEmpty())

			userSearchRequestInfo.put("mobileNumber", mobileNumber);

		if (aadhaarNumber != null && !aadhaarNumber.isEmpty())
			userSearchRequestInfo.put("aadhaarNumber", aadhaarNumber);

		userSearchRequestInfo.put("tenantId", tenantId);
		userSearchRequestInfo.put("RequestInfo", requestInfo);

		UserResponseInfo userResponse = null;
		if (ownerName != null || mobileNumber != null || aadhaarNumber != null) {
			userResponse = restTemplate.postForObject(environment.getProperty("user.searchUrl"), userSearchRequestInfo,
					UserResponseInfo.class);
		}
		String Ids = "";

		List<User> users = null;

		if (userResponse != null && userResponse.getUser() != null) {
			users = userResponse.getUser();

			int count = 1;

			for (User user : users) {
				if (count < users.size())
					Ids = Ids + user.getId() + ",";
				else
					Ids = Ids + "" + user.getId() + "";

				count++;
			}

		}

		searchPropertySql.append("select * from egpt_property prop  JOIN egpt_address Addr"
				+ " on Addr.property =  prop.id JOIN egpt_property_owner puser on puser.property = prop.id where ");

		if (tenantId != null && !tenantId.isEmpty())
			searchPropertySql.append("prop.tenantid='" + tenantId.trim() + "'");

		if (active != null)
			searchPropertySql.append(" AND prop.active='" + active + "'");

		if (upicNo != null && !upicNo.isEmpty())
			searchPropertySql.append(" AND prop.upicnumber='" + upicNo.trim() + "'");

		if (oldUpicNo != null && !oldUpicNo.isEmpty())
			searchPropertySql.append(" AND prop.oldUpicNumber='" + oldUpicNo.trim() + "'");

		if (!Ids.isEmpty())
			searchPropertySql.append(" AND puser.user_id IN (" + Ids + ")");

		// TODO as of now we don't have the revenue Zone ,revenue
		// Ward,locality,houseNoBldgApt
		// TODO [Ramki] what do you mean by we do not have revenue Zone ,revenue
		// Ward,locality,houseNoBldgApt ?
		// So we are not putting in search

		if (houseNoBldgApt != null && !houseNoBldgApt.isEmpty())
			searchPropertySql.append(" AND Addr.housenobldgapt='" + houseNoBldgApt.trim() + "'");

		if (sort != null && sort.length > 0) {
			searchPropertySql.append(" ORDER BY ");

			// Count loop to add the coma ,

			int orderBycount = 1;

			StringBuffer orderByCondition = new StringBuffer();
			for (String order : sort) {
				if (orderBycount < sort.length)
					orderByCondition.append("prop." + order + ",");
				else
					orderByCondition.append("prop." + order);
				orderBycount++;
			}

			if (orderBycount > 1)
				orderByCondition.append(" asc");

			searchPropertySql.append(orderByCondition.toString());
		}

		//
		// Appending the pagination related logic,if the page size and page
		// number is -1
		// then we need to put the default page size and page number
		//

		if (pageNumber == -1)
			pageNumber = Integer.valueOf(environment.getProperty("default.page.number").trim());

		if (pageSize == -1)
			pageSize = Integer.valueOf(environment.getProperty("default.page.size").trim());

		int offset = 0;
		int limit = pageNumber * pageSize;

		if (pageNumber <= 1)
			offset = (limit - pageSize);
		else
			offset = (limit - pageSize) + 1;

		searchPropertySql.append(" offset " + offset + " limit " + limit);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("Sql", searchPropertySql.toString());
		resultMap.put("users", users);

		return resultMap;

	}

}
