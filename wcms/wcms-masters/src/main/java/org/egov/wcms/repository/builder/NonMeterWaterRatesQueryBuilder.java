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
import java.util.Map;

import org.egov.wcms.web.contract.NonMeterWaterRatesGetReq;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NonMeterWaterRatesQueryBuilder {

    private static final String BASE_QUERY = "SELECT nonmeterwater.id as nonmeterwater_id,nonmeterwater.code as nonmeterwater_code, nonmeterwater.billingtype as billingtype,"
            + " nonmeterwater.connectiontype as connectiontype,nonmeterwater.sourcetypeid "
            + "as nonmeterwater_sourcetypeid, nonmeterwater.usagetypeid as nonmeterwater_usagetypeid,"
            + "nonmeterwater.subusagetypeid as nonmeterwater_subusagetypeid,nonmeterwater.outsideulb as nonmeterwater_outsideulb,"
            + "nonmeterwater.pipesizeid as nonmeterwater_pipesizeId,pipesize.sizeinmilimeter as pipesize_sizeinmm,pipesize.sizeininch as pipeSizeInInch,"
            + " nonmeterwater.fromdate as nonmeterwater_fromdate,nonmeterwater.amount as nonmeterwater_amount ,"
            + " nonmeterwater.nooftaps as nonmeterwater_nooftaps,nonmeterwater.active as nonmeterwater_active, watersource.name as watersource_name,"
            + " usage.code as usage_code, subusage.code as subusage_code, usage.name as usage_name, subusage.name as subusage_name,nonmeterwater.tenantId as nonmeterwater_tenantId "
            + " FROM egwtr_non_meter_water_rates nonmeterwater INNER JOIN egwtr_pipesize pipesize ON nonmeterwater.pipesizeid = pipesize.id and nonmeterwater.tenantId=pipesize.tenantId "
            + " INNER JOIN egwtr_water_source_type watersource ON nonmeterwater.sourcetypeid = watersource.id and nonmeterwater.tenantId=watersource.tenantId "
            + " INNER JOIN egwtr_usage_type usage ON nonmeterwater.usagetypeid = usage.id and nonmeterwater.tenantId=usage.tenantId "
            + " INNER JOIN egwtr_usage_type subusage ON nonmeterwater.subusagetypeid = subusage.id and nonmeterwater.tenantId=subusage.tenantId ";

    public String getQuery(final NonMeterWaterRatesGetReq nonMeterWaterRatesGetRequest,
            @SuppressWarnings("rawtypes") final Map<String, Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, nonMeterWaterRatesGetRequest);
        addOrderByClause(selectQuery, nonMeterWaterRatesGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("unchecked")
    private void addWhereClause(final StringBuilder selectQuery, final Map<String, Object> preparedStatementValues,
            final NonMeterWaterRatesGetReq nonMeterWaterRatesGetRequest) {

        if (nonMeterWaterRatesGetRequest.getIds() == null && nonMeterWaterRatesGetRequest.getSourceTypeName() == null
                && nonMeterWaterRatesGetRequest.getUsageTypeCode() == null && nonMeterWaterRatesGetRequest.getSubUsageTypeCode()==null &&
                nonMeterWaterRatesGetRequest.getPipeSize() == null
                && nonMeterWaterRatesGetRequest.getTenantId() == null && nonMeterWaterRatesGetRequest.getConnectionType() == null
                &&
                nonMeterWaterRatesGetRequest.getAmount() == null
                && nonMeterWaterRatesGetRequest.getFromDate() == null && nonMeterWaterRatesGetRequest.getNoOfTaps() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (nonMeterWaterRatesGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" nonmeterwater.tenantId = :tenantId ");
            preparedStatementValues.put("tenantId", nonMeterWaterRatesGetRequest.getTenantId());
        }

        if (nonMeterWaterRatesGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.id IN " + getIdQuery(nonMeterWaterRatesGetRequest.getIds()));
        }

        if (nonMeterWaterRatesGetRequest.getUsageTypeCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.usagetypeid = :usagetypeid ");
            preparedStatementValues.put("usagetypeid", Long.valueOf(nonMeterWaterRatesGetRequest.getUsageTypeId()));
        }
        if (nonMeterWaterRatesGetRequest.getSubUsageTypeCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.subusagetypeid = :subusagetypeid");
            preparedStatementValues.put("subusagetypeid", Long.valueOf(nonMeterWaterRatesGetRequest.getSubUsageTypeId()));
        }
        if (nonMeterWaterRatesGetRequest.getOutsideUlb() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.outsideulb = :outsideulb");
            preparedStatementValues.put("outsideulb", nonMeterWaterRatesGetRequest.getOutsideUlb());
        }
        if (nonMeterWaterRatesGetRequest.getConnectionType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.connectiontype = :connectiontype ");
            preparedStatementValues.put("connectiontype", nonMeterWaterRatesGetRequest.getConnectionType());
        }

        if (nonMeterWaterRatesGetRequest.getSourceTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.sourcetypeid = :sourcetypeid ");
            preparedStatementValues.put("sourcetypeid", nonMeterWaterRatesGetRequest.getSourceTypeId());
        }

        if (nonMeterWaterRatesGetRequest.getPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.pipesizeid = :pipesizeid ");
            preparedStatementValues.put("pipesizeid", nonMeterWaterRatesGetRequest.getPipeSizeId());
        }

        if (nonMeterWaterRatesGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" nonmeterwater.active = :active ");
            preparedStatementValues.put("active", nonMeterWaterRatesGetRequest.getActive());
        }

    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final NonMeterWaterRatesGetReq nonMeterWaterRatesGetRequest) {
        final String sortBy = nonMeterWaterRatesGetRequest.getSortBy() == null ? "nonmeterwater.id"
                : "nonmeterwater." + nonMeterWaterRatesGetRequest.getSortBy();
        final String sortOrder = nonMeterWaterRatesGetRequest.getSortOrder() == null ? "DESC"
                : nonMeterWaterRatesGetRequest.getSortOrder();
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

    public static String insertNonMeterWaterRatesQuery() {
        return "INSERT INTO egwtr_non_meter_water_rates(id,code,billingtype,connectiontype,usagetypeid,subusagetypeid,outsideulb,sourcetypeid,pipesizeid,fromdate,amount,nooftaps,active,"
                + "createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:billingtype,:connectiontype,:usagetypeid,:subusagetypeid,:outsideulb,:sourcetypeid,:pipesizeid,:fromdate,:amount,:nooftaps,:active,"
                + ":createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updateNonMeterWaterRatesQuery() {
        return "UPDATE egwtr_non_meter_water_rates SET billingtype = :billingtype,connectiontype = :connectiontype ,usagetypeid = :usagetypeid,subusagetypeid = :subusagetypeid,outsideulb = :outsideulb,sourcetypeid = :sourcetypeid,pipesizeid = :pipesizeid ,fromdate = :fromdate "
                + " , amount = :amount ,nooftaps= :nooftaps,active = :active,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code  and tenantid = :tenantid ";
    }

    public static String selectNonMeterWaterRatesByCodeQuery() {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        selectQuery.append("where nonmeterwater.usagetypeid= :usagetypeid and nonmeterwater.subusagetypeid= :subusagetypeid and nonmeterwater.connectiontype =:connectiontype "
                + " and nonmeterwater.sourcetypeid =:sourcetypeid and nonmeterwater.pipesizeid=:pipesizeid and nonmeterwater.fromdate=:fromdate and nonmeterwater.tenantId = :tenantId");
        return selectQuery.toString();
    }

    public static String selectNonMeterWaterRatesByCodeNotInQuery() {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        selectQuery.append(" where nonmeterwater.usagetypeid= :usagetypeid and nonmeterwater.subusagetypeid= :subusagetypeid and nonmeterwater.connectiontype =:connectiontype and "
                + " nonmeterwater.sourcetypeid =:sourcetypeid and nonmeterwater.pipesizeid=:pipesizeid and nonmeterwater.fromdate=:fromdate and nonmeterwater.tenantId = :tenantId and nonmeterwater.code !=:code ");
        return selectQuery.toString();
    }

    public static String getSourceTypeIdQueryForSearch() {
        return " select id FROM egwtr_water_source_type  where name= :name and tenantId = :tenantId ";
    }

    public static String getSourceTypeNameQuery() {
        return "SELECT name FROM egwtr_water_source_type WHERE id = ? and tenantId = ? ";
    }

    public static String getPipeSizeIdQueryForSearch() {
        return " select id FROM egwtr_pipesize where sizeinmilimeter= :sizeinmilimeter and tenantId = :tenantId ";
    }

    public static String getUsageTypeIdQuery() {
        return " select id FROM egwtr_usage_type  where code= ? and tenantId = ? ";
    }

    public static String getUsageTypeIdQueryForSearch() {
        return " select id FROM egwtr_usage_type  where code= :code and tenantId = :tenantId ";
    }
}
