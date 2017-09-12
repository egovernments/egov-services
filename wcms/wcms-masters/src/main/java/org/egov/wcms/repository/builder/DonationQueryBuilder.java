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

package org.egov.wcms.repository.builder;

import java.util.List;

import org.egov.wcms.web.contract.DonationGetRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DonationQueryBuilder {

    private static final String BASE_QUERY = "SELECT donation.id as donation_id, donation.code as donation_code,donation.propertytypeid as donation_propertytypeId,"
            + "donation.usagetypeid as donation_usagetypeId,donation.subusagetypeid as donation_subusagetypeId,donation.outsideulb as donation_outsideulb,donation.categorytypeid as donation_categorytypeId,donation.maxpipesizeid"
            + " as donation_maxpipesizId,donation.minpipesizeid as donation_minpipesizeId,donation.fromdate as donation_fromDate,"
            + "donation.todate as donation_toDate,donation.donationamount as donation_amount, donation.active as donation_active, "
            + "donation.tenantId as donation_tenantId FROM egwtr_donation donation ";

    public String getQuery(final DonationGetRequest donation, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, donation);
        addOrderByClause(selectQuery, donation);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("unchecked")
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final DonationGetRequest donation) {

        if (donation.getId() == null && donation.getPropertyType() == null && donation.getUsageType() == null
                && donation.getCategoryType() == null && donation.getMaxPipeSize() == null
                && donation.getMinPipeSize() == null && donation.getDonationAmount() == 0
                && donation.getActive() == null && donation.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (donation.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" donation.tenantId = ?");
            preparedStatementValues.add(donation.getTenantId());
        }

        if (donation.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.id IN " + getIdQuery(donation.getId()));
        }

        if (donation.getPropertyType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.propertytypeid = ?");
            preparedStatementValues.add(donation.getPropertyTypeId());
        }

        if (donation.getUsageType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.usagetypeid = ?");
            preparedStatementValues.add(donation.getUsageTypeId());
        }
        
        if (donation.getSubUsageType()!= null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.subusagetypeid = ?");
            preparedStatementValues.add(donation.getSubUsageTypeId());
        }
        
        if(donation.getOutSideUlb()!= null){
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.outsideulb = ?");
            preparedStatementValues.add(donation.getOutSideUlb());
        }

        if (donation.getCategoryType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.categorytypeid = ?");
            preparedStatementValues.add(donation.getCategoryTypeId());
        }

        if (donation.getMaxPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.maxpipesizeid = ?");
            preparedStatementValues.add(donation.getMaxPipeSizeId());
        }

        if (donation.getMinPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.minpipesizeid = ?");
            preparedStatementValues.add(donation.getMinPipeSizeId());
        }

        if (donation.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.active = ?");
            preparedStatementValues.add(donation.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery, final DonationGetRequest donation) {
        final String sortBy = donation.getSortBy() == null ? "donation.id" : "donation." + donation.getSortBy();
        final String sortOrder = donation.getSortOrder() == null ? "DESC" : donation.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
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

    public static String donationInsertQuery() {
        return "INSERT INTO egwtr_donation "
                + "(id,code, propertytypeid, usagetypeid,subusagetypeid,outsideulb, categorytypeid, maxpipesizeid, minpipesizeid, fromdate, todate, donationamount, "
                + "active, tenantid, createdby,lastmodifiedby,createddate,lastmodifieddate) VALUES (:id,:code,:propertytypeid, :usagetypeid,:subusagetypeid,:outsideulb,:categorytypeid, "
                + ":maxpipesizeid, :minpipesizeid, :fromdate, :todate, :donationamount, "
                + " :active, :tenantid, :createdby,:lastmodifiedby,:createddate,:lastmodifieddate)";
    }

    public static String donationUpdateQuery() {
        return "UPDATE egwtr_donation set propertytypeid= :propertytypeid,usagetypeid= :usagetypeid,subusagetypeid= :subusagetypeid,outsideulb= :outsideulb,categorytypeid= :categorytypeid, maxpipesizeid= :maxpipesizeid, minpipesizeid= :minpipesizeid ,"
                + " fromdate= :fromdate, todate= :todate, donationamount= :donationamount, active=:active,lastmodifiedby= :lastmodifiedby, lastmodifieddate= :lastmodifieddate where code= :code ";
    }

    public static String getCategoryId() {
        return "SELECT id FROM egwtr_category WHERE name = ? and tenantId = ? ";
    }

    public static String getCategoryTypeName() {
        return "SELECT name FROM egwtr_category WHERE id = ? and tenantId = ? ";
    }

    public static String getPipeSizeIdQuery() {
        return " select id FROM egwtr_pipesize where sizeinmilimeter= ? and tenantId = ? ";
    }

    public static String getPipeSizeInmm() {
        return "SELECT sizeinmilimeter FROM egwtr_pipesize WHERE id = ? and tenantId = ? ";
    }

    
    public static String selectDonationByCodeQuery() {
        return " select code FROM egwtr_donation where propertytypeid = ? and usagetypeid = ? and subusagetypeid = ? "
                + " and categorytypeid = ? and maxpipesizeid = ? and minpipesizeid = ? and tenantId = ?";
    }

    public static String selectDonationByCodeNotInQuery() {
        return " select code from egwtr_donation where propertytypeid = ? and usagetypeid = ? and "
                + " subusagetypeid = ? and categorytypeid = ? and maxpipesizeid = ? and minpipesizeid = ? and tenantId = ? and code != ? ";
    }
}
