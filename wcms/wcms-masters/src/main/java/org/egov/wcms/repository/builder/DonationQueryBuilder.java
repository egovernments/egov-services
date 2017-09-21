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

    private static final String BASE_QUERY = "SELECT donation.id as donation_id, donation.code as donation_code ,"
            + "donation.usagetypeid as donation_usagetypeId,donation.subusagetypeid as donation_subusagetypeId,"
            + " donation.outsideulb as donation_outsideulb ,usage.code as usagecode ,usage.name as usageName ,"
            + "subusage.code as subusagecode ,subusage.name as subUsageName,donation.maxpipesizeid "
            + " as donation_maxpipesizId ,maxpipesize.sizeinmilimeter as maxpipesize,maxpipesize.sizeininch as maxpipesizeininch,donation.minpipesizeid as donation_minpipesizeId,"
            + " minpipesize.sizeinmilimeter as minpipesize,minpipesize.sizeininch as minpipesizeininch,donation.fromdate as donation_fromDate,"
            + "donation.todate as donation_toDate,donation.donationamount as donation_amount, donation.active as donation_active, "
            + "donation.tenantId as donation_tenantId FROM egwtr_donation donation ,egwtr_usage_type usage , egwtr_usage_type subusage, "
            + " egwtr_pipesize maxpipesize,egwtr_pipesize minpipesize where usage.id=donation.usagetypeid and usage.tenantId=donation.tenantId "
            + " and subusage.id=donation.subusagetypeid  and subusage.tenantId=donation.tenantId "
            + " and maxpipesize.id=donation.maxpipesizeid and maxpipesize.tenantId=donation.tenantId and "
            + " minpipesize.id=donation.minpipesizeid and minpipesize.tenantId=donation.tenantId";

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

        if (donation.getIds() == null && donation.getUsageTypeCode() == null && donation.getSubUsageTypeCode() == null
                 && donation.getMaxPipeSize() == null
                && donation.getMinPipeSize() == null && donation.getDonationAmount() == 0
                && donation.getActive() == null && donation.getTenantId() == null)
            return;

        // selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (donation.getTenantId() != null) {
            isAppendAndClause = true;
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.tenantId = ?");
            preparedStatementValues.add(donation.getTenantId());
        }

        if (donation.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.id IN " + getIdQuery(donation.getIds()));
        }

        if (donation.getUsageTypeCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" usage.code = ?");
            preparedStatementValues.add(donation.getUsageTypeCode());
        }

        if (donation.getSubUsageTypeCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" subusage.code = ?");
            preparedStatementValues.add(donation.getSubUsageTypeCode());
        }
        
        if (donation.getUsageTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" usage.name = ?");
            preparedStatementValues.add(donation.getUsageTypeCode());
        }

        if (donation.getSubUsageTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" subusage.name = ?");
            preparedStatementValues.add(donation.getSubUsageTypeCode());
        }
        

        if (donation.getOutSideUlb() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.outsideulb = ?");
            preparedStatementValues.add(donation.getOutSideUlb());
        }


        if (donation.getMaxPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" maxpipesize.sizeinmilimeter = ?");
            preparedStatementValues.add(donation.getMaxPipeSize());
        }
        
        if (donation.getMaxPipeSizeId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" donation.maxpipesizeid = ?");
            preparedStatementValues.add(donation.getMaxPipeSizeId());
        }

        if (donation.getMinPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" minpipesize.sizeinmilimeter = ?");
            preparedStatementValues.add(donation.getMinPipeSize());
        }
        
        if (donation.getMinPipeSizeId() != null) {
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
                + "(id,code, usagetypeid,subusagetypeid,outsideulb, maxpipesizeid, minpipesizeid, fromdate, todate, donationamount, "
                + "active, tenantid, createdby,lastmodifiedby,createddate,lastmodifieddate) VALUES "
                + "(:id,:code,:usagetypeid,:subusagetypeid,:outsideulb, "
                + " :maxpipesizeid, :minpipesizeid, :fromdate, :todate, :donationamount, "
                + " :active, :tenantid, :createdby,:lastmodifiedby,:createddate,:lastmodifieddate)";
    }

    public static String donationUpdateQuery() {
        return "UPDATE egwtr_donation set usagetypeid= :usagetypeid,subusagetypeid= :subusagetypeid,outsideulb= :outsideulb, maxpipesizeid= :maxpipesizeid, minpipesizeid= :minpipesizeid ,"
                + " fromdate= :fromdate, todate= :todate, donationamount= :donationamount, active=:active,lastmodifiedby= :lastmodifiedby, lastmodifieddate= :lastmodifieddate where code= :code and tenantid = :tenantid ";
    }

    public static String getPipeSizeIdQuery() {
        return " select id FROM egwtr_pipesize where sizeinmilimeter= ? and tenantId = ? ";
    }

    public static String getPipeSizeInmm() {
        return "SELECT sizeinmilimeter FROM egwtr_pipesize WHERE id = ? and tenantId = ? ";
    }

    public static String selectDonationByCodeQuery() {
        return " select code FROM egwtr_donation where usagetypeid = ? and subusagetypeid = ? "
                + " and maxpipesizeid = ? and minpipesizeid = ? and tenantId = ?";
    }

    public static String selectDonationByCodeNotInQuery() {
        return " select code from egwtr_donation where usagetypeid = ? and "
                + " subusagetypeid = ? and maxpipesizeid = ? and minpipesizeid = ? and tenantId = ? and code != ? ";
    }

    public static String getUsageTypeId() {
        return "SELECT id FROM egwtr_usage_type WHERE code = ? and tenantId = ? ";
    }
}