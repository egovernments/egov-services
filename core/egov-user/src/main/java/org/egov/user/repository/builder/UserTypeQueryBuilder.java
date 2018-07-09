/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.user.repository.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.model.Role;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserTypeQueryBuilder {

	@Autowired
	private RoleRepository roleRepositiry;

	private static final String BASE_QUERY = "SELECT * from eg_user u ";

	@SuppressWarnings("rawtypes")
	public String getQuery(final UserSearchCriteria userSearchCriteria, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		List<Long> roleIds = getRoleIds(userSearchCriteria);
		if (userSearchCriteria.getRoleCodes() != null && userSearchCriteria.getRoleCodes().size() > 0
				&& !roleIds.isEmpty()) {
			selectQuery.append(",eg_userrole ur");
		}
		addWhereClause(selectQuery, preparedStatementValues, userSearchCriteria);
		if (!roleIds.isEmpty())
			selectQuery.append("And ur.roleid IN" + getIdQuery(roleIds) + " And ur.userid = u.id");
		addOrderByClause(selectQuery, userSearchCriteria);
		addPagingClause(selectQuery, preparedStatementValues, userSearchCriteria);
		log.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	private List<Long> getRoleIds(final UserSearchCriteria userSearchCriteria) {

		List<Long> roleIdList = new ArrayList<Long>();
		if (userSearchCriteria.getRoleCodes() != null && userSearchCriteria.getRoleCodes().size() > 0) {
			for (String roleCode : userSearchCriteria.getRoleCodes()) {
				Role role = roleRepositiry.findByTenantIdAndCode(userSearchCriteria.getTenantId(), roleCode);
				if (role != null) {
					roleIdList.add(role.getId());
				}
			}
		}
		return roleIdList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final UserSearchCriteria userSearchCriteria) {

		if (userSearchCriteria.getId() == null && userSearchCriteria.getUserName() == null
				&& userSearchCriteria.getName() == null && userSearchCriteria.getEmailId() == null
				&& userSearchCriteria.getActive() == null && userSearchCriteria.getTenantId() == null
				&& userSearchCriteria.getAadhaarNumber() == null && userSearchCriteria.getMobileNumber() == null
				&& userSearchCriteria.getPan() == null && userSearchCriteria.getRoleCodes() == null
				&& userSearchCriteria.getType() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (userSearchCriteria.getId() != null && !userSearchCriteria.getId().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.id IN " + getIdQuery(userSearchCriteria.getId()));
		}

		if (userSearchCriteria.getTenantId() != null) {
			if(!StringUtils.isEmpty(userSearchCriteria.getType())) {
				if(userSearchCriteria.getType().equalsIgnoreCase("CITIZEN")) {
					isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
					String tenantId = userSearchCriteria.getTenantId().split("[.]")[0];
					selectQuery.append(" u.tenantid LIKE ").append("'%").append(tenantId).append("%'");
				}
			}else {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" u.tenantid = ?");
				preparedStatementValues.add(userSearchCriteria.getTenantId().trim());
			}
		}

		if (userSearchCriteria.getUserName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.username = ?");
			preparedStatementValues.add(userSearchCriteria.getUserName().trim());
		}

		if (userSearchCriteria.isFuzzyLogic() == false && userSearchCriteria.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.name = ?");
			preparedStatementValues.add(userSearchCriteria.getName().trim());
		}

		if (userSearchCriteria.getActive() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.active = ?");
			preparedStatementValues.add(userSearchCriteria.getActive());
		}

		if (userSearchCriteria.getEmailId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.emailid = ?");
			preparedStatementValues.add(userSearchCriteria.getEmailId().trim());
		}

		if (userSearchCriteria.getAadhaarNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.aadhaarnumber = ?");
			preparedStatementValues.add(userSearchCriteria.getAadhaarNumber().trim());
		}

		if (userSearchCriteria.getMobileNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.mobilenumber = ?");
			preparedStatementValues.add(userSearchCriteria.getMobileNumber().trim());
		}

		if (userSearchCriteria.getPan() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.pan = ?");
			preparedStatementValues.add(userSearchCriteria.getPan().trim());
		}

		if (userSearchCriteria.getType() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.type = ?");
			preparedStatementValues.add(userSearchCriteria.getType().trim());
		}

		if (userSearchCriteria.isFuzzyLogic() == true && userSearchCriteria.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.name like " + "'%" + userSearchCriteria.getName().trim() + "%'");
		}
		
		if (!CollectionUtils.isEmpty(userSearchCriteria.getUuid())) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" u.uuid IN " + getUUIDQuery(userSearchCriteria.getUuid()));
		}		
	}

	private void addOrderByClause(final StringBuilder selectQuery, final UserSearchCriteria userSearchCriteria) {
		final String sortBy = userSearchCriteria.getSort() != null && !userSearchCriteria.getSort().isEmpty()
				? " u." + userSearchCriteria.getSort().get(0) : "u.name";
		selectQuery.append(" ORDER BY " + sortBy);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final UserSearchCriteria userSearchCriteria) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		int pageSize = userSearchCriteria.getPageSize();
		if (pageSize != 0) {
			pageSize = userSearchCriteria.getPageSize();
		} else {
			pageSize = 500;
		}
		preparedStatementValues.add(pageSize); // Set limit to pageSize
	}

	private static String getIdQuery(final List<Long> idList) {
		final StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++)
				query.append(", " + idList.get(i));
		}
		return query.append(")").toString();
	}
	
	private static String getUUIDQuery(final List<String> idList) {
		final StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append("'").append(idList.get(0).toString()).append("'");
			for (int i = 1; i < idList.size(); i++)
				query.append(", '" + idList.get(i)).append("'");
		}
		return query.append(")").toString();
	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 *
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}

	public String getInsertUserQuery() {
		String insertQuery = "insert into eg_user (id,uuid,tenantid,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,active,name,gender,pan,aadhaarnumber,"
				+ "type,guardian,guardianrelation,signature,accountlocked,bloodgroup,photo,identificationmark,createddate,lastmodifieddate,createdby,lastmodifiedby) values (:id,:uuid,:tenantid,:salutation,"
				+ ":dob,:locale,:username,:password,:pwdexpirydate,:mobilenumber,:altcontactnumber,:emailid,:active,:name,:gender,:pan,:aadhaarnumber,:type,:guardian,:guardianrelation,:signature,"
				+ ":accountlocked,:bloodgroup,:photo,:identificationmark,:createddate,:lastmodifieddate,:createdby,:lastmodifiedby) ";
		return insertQuery;
	}

	public String getUpdateUserQuery() {
		return "update eg_user set salutation=:Salutation,dob=:Dob,locale=:Locale,password=:Password,pwdexpirydate=:PasswordExpiryDate,mobilenumber=:MobileNumber,altcontactnumber=:AltContactNumber,emailid=:EmailId,active=:Active,name=:Name,gender=:Gender,pan=:Pan,aadhaarnumber=:AadhaarNumber,"
				+ "type=:Type,guardian=:Guardian,guardianrelation=:GuardianRelation,signature=:Signature,accountlocked=:AccountLocked,bloodgroup=:BloodGroup,photo=:Photo,identificationmark=:IdentificationMark,lastmodifieddate=:LastModifiedDate,lastmodifiedby=:LastModifiedBy where id=:id and tenantid=:tenantid";
	}

	public String getUserRoleInsertQuery() {
		return "insert into eg_userrole(roleid,roleidtenantid,userid,tenantid,lastmodifieddate) values (:roleid,:roleidtenantid,:userid,:tenantid,:lastmodifieddate)";
	}

	public String getFindUserByIdAndTenantId() {
		return "select * from eg_user where id=:id and tenantid =:tenantId ";
	}

	public String getFindUserByUserNameAndTenantId() {
		return "select * from eg_user where username=:userName and tenantid =:tenantId ";
	}
	
	public String getUserByUserNameAndTenantId(String tenantId) {
		return "select * from eg_user where username=:userName and tenantid like"  + "'" + tenantId + "%'";
	}

	public String getUserPresentByIdAndUserNameAndTenant() {
		return "select id from eg_user where username =:userName and id !=:id and tenantid =:tenantId";
	}

	public String getUserPresentByUserNameAndTenant() {
		return "select id from eg_user where username =:userName and tenantId =:tenantId";
	}

	public String getUserByEmailAntTenant() {
		return "select * from eg_user where emailid =:emailId and tenantId =:tenantId";
	}
}
