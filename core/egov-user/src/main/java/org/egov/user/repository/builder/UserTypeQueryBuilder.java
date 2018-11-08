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

import lombok.extern.slf4j.Slf4j;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class UserTypeQueryBuilder {

    @Autowired
    private RoleRepository roleRepository;

    private static final String SELECT_USER_QUERY = "SELECT u.title, u.salutation, u.dob, u.locale, u.username, u" +
            ".password, u.pwdexpirydate,  u.mobilenumber, u.altcontactnumber, u.emailid, u.createddate, u" +
            ".lastmodifieddate,  u.createdby, u.lastmodifiedby, u.active, u.name, u.gender, u.pan, u.aadhaarnumber, u" +
            ".type,  u.version, u.guardian, u.guardianrelation, u.signature, u.accountlocked, u.bloodgroup, u.photo, " +
            "u.identificationmark,  u.tenantid, u.id, u.uuid, addr.id as addr_id, addr.type as addr_type, addr" +
            ".address as addr_address,  addr.city as addr_city, addr.pincode as addr_pincode, addr.tenantid as " +
            "addr_tenantid, addr.userid as addr_userid, r.code as role_code, r.name as role_name,  r.description as role_description " +
            ", r.id as role_id, r.tenantid as role_tenantid \n" +
            "\tFROM eg_user u LEFT OUTER JOIN eg_user_address addr ON u.id = addr.userid AND u.tenantid = addr" +
            ".tenantid LEFT OUTER JOIN eg_userrole ur ON u.id = ur.userid AND u.tenantid = ur.tenantid LEFT OUTER " +
            "JOIN eg_role r ON ur.roleid = r.id AND ur.roleidtenantid = r.tenantid ";

    private static final String PAGINATION_WRAPPER = "SELECT * FROM " +
            "(SELECT *, DENSE_RANK() OVER (ORDER BY id) offset_ FROM " +
            "({baseQuery})" +
            " result) result_offset " +
            "WHERE offset_ > ? AND offset_ <= ?";

    private static final String BASE_QUERY = "SELECT * from eg_user u ";

    public static final String SELECT_NEXT_SEQUENCE_USER = "select nextval('seq_eg_user')";

    @SuppressWarnings("rawtypes")
    public String getQuery(final UserSearchCriteria userSearchCriteria, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(SELECT_USER_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, userSearchCriteria);

//        selectQuery.append("And ur.roleid IN").append(getIdQuery(roleIds)).append(" And ur.userid = u.id");

        addOrderByClause(selectQuery, userSearchCriteria);
        return addPagingClause(selectQuery, preparedStatementValues, userSearchCriteria);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                final UserSearchCriteria userSearchCriteria) {

        if (userSearchCriteria.getId() == null && userSearchCriteria.getUserName() == null
                && userSearchCriteria.getName() == null && userSearchCriteria.getEmailId() == null
                && userSearchCriteria.getActive() == null && userSearchCriteria.getTenantId() == null
                && userSearchCriteria.getAadhaarNumber() == null && userSearchCriteria.getMobileNumber() == null
                && userSearchCriteria.getPan() == null && userSearchCriteria.getRoleCodes() == null
                && userSearchCriteria.getType() == null && userSearchCriteria.getUuid() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (userSearchCriteria.getId() != null && !userSearchCriteria.getId().isEmpty()) {
            isAppendAndClause = addAndClauseIfRequired(false, selectQuery);
            selectQuery.append(" u.id IN ( ").append(getQueryForCollection(userSearchCriteria.getId(),
                    preparedStatementValues)).append(" )");
        }

        if (userSearchCriteria.getTenantId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.tenantid = ?");
            preparedStatementValues.add(userSearchCriteria.getTenantId().trim());
        }

        if (userSearchCriteria.getUserName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.username = ?");
            preparedStatementValues.add(userSearchCriteria.getUserName().trim());
        }

        if (!userSearchCriteria.isFuzzyLogic() && userSearchCriteria.getName() != null) {
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
            preparedStatementValues.add(userSearchCriteria.getType().toString());
        }

        if (userSearchCriteria.isFuzzyLogic() && userSearchCriteria.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.name like " + "'%").append(userSearchCriteria.getName().trim()).append("%'");
        }

        if (!isEmpty(userSearchCriteria.getUuid())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.uuid IN (").append(getQueryForCollection(userSearchCriteria.getUuid(),
                    preparedStatementValues)).append(" )");
        }

        if(!isEmpty(userSearchCriteria.getRoleCodes())){
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" r.code IN (").append(getQueryForCollection(userSearchCriteria.getRoleCodes(),
                    preparedStatementValues)).append(" )");
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery, final UserSearchCriteria userSearchCriteria) {
        final String sortBy = userSearchCriteria.getSort() != null && !userSearchCriteria.getSort().isEmpty()
                ? " u." + userSearchCriteria.getSort().get(0) : "u.name";
        selectQuery.append(" ORDER BY ").append(sortBy);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                 final UserSearchCriteria criteria) {

        if(isNull(criteria.getOffset()))
            criteria.setOffset(0);

        if (criteria.getLimit()!=null && criteria.getLimit() != 0) {
            String finalQuery = PAGINATION_WRAPPER.replace("{baseQuery}", selectQuery);
            preparedStatementValues.add(criteria.getOffset());
            preparedStatementValues.add( criteria.getOffset() + criteria.getLimit());

            return finalQuery;
        } else
            return selectQuery.toString();

    }

//    private static String getIdQuery(final List<Long> idList) {
//        final StringBuilder query = new StringBuilder("(");
//        if (idList.size() >= 1) {
//            query.append(idList.get(0).toString());
//            for (int i = 1; i < idList.size(); i++)
//                query.append(", ").append(idList.get(i));
//        }
//        return query.append(")").toString();
//    }
//
//    private static String getUUIDQuery(final List<String> idList) {
//        final StringBuilder query = new StringBuilder("(");
//        if (idList.size() >= 1) {
//            query.append("'").append(idList.get(0)).append("'");
//            for (int i = 1; i < idList.size(); i++)
//                query.append(", '").append(idList.get(i)).append("'");
//        }
//        return query.append(")").toString();
//    }

    private String getQueryForCollection(List<?> ids, List<Object> preparedStmtList) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = ids.iterator();
        while (iterator.hasNext()){
            builder.append(" ?");
            preparedStmtList.add(iterator.next());

            if(iterator.hasNext())
                builder.append(",");
        }
        return builder.toString();
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
        return "insert into eg_user (id,uuid,tenantid,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,active,name,gender,pan,aadhaarnumber,"
                + "type,guardian,guardianrelation,signature,accountlocked,bloodgroup,photo,identificationmark,createddate,lastmodifieddate,createdby,lastmodifiedby) values (:id,:uuid,:tenantid,:salutation,"
                + ":dob,:locale,:username,:password,:pwdexpirydate,:mobilenumber,:altcontactnumber,:emailid,:active,:name,:gender,:pan,:aadhaarnumber,:type,:guardian,:guardianrelation,:signature,"
                + ":accountlocked,:bloodgroup,:photo,:identificationmark,:createddate,:lastmodifieddate,:createdby,:lastmodifiedby) ";
    }

    public String getUpdateUserQuery() {
        return "update eg_user set salutation=:Salutation,dob=:Dob,locale=:Locale,password=:Password,pwdexpirydate=:PasswordExpiryDate,mobilenumber=:MobileNumber,altcontactnumber=:AltContactNumber,emailid=:EmailId,active=:Active,name=:Name,gender=:Gender,pan=:Pan,aadhaarnumber=:AadhaarNumber,"
                + "type=:Type,guardian=:Guardian,guardianrelation=:GuardianRelation,signature=:Signature," +
                "accountlocked=:AccountLocked,bloodgroup=:BloodGroup,photo=:Photo," +
                "identificationmark=:IdentificationMark,lastmodifieddate=:LastModifiedDate," +
                "lastmodifiedby=:LastModifiedBy where username=:username and tenantid=:tenantid and type=:type";
    }


    public String getUserPresentByUserNameAndTenant() {
        return "select count(*) from eg_user where username =:userName and tenantId =:tenantId and type = :userType " ;
    }

}
