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

import java.util.Map;

import org.egov.wcms.web.contract.MeterWaterRatesGetRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MeterWaterRatesQueryBuilder {

    private static final String BASE_QUERY = "SELECT meterwater.id as meterwater_id,meterwater.code as meterwater_code, meterwater.billingtype as billingtype,meterwater.sourcetypeid "
            + "as meterwater_sourcetypeid, meterwater.usagetypeid as meterwater_usagetypeid,"
            + " meterwater.subusagetypeid as meterwater_subusagetypeid,meterwater.outsideulb as meterwater_outsideulb,"
            + "meterwater.pipesizeid as meterwater_pipesizeId,pipesize.sizeinmilimeter as pipesize_sizeinmm,pipesize.sizeininch as pipeSizeInInch,meterwater.fromdate as meterwater_fromdate,meterwater.todate as meterwater_todate,"
            + "meterwater.active as meterwater_active, watersource.name as watersource_name,"
            + "usage.code as usage_code, subusage.code as subusage_code,usage.name as usage_name, subusage.name as subusage_name,"
            + " meterwater.tenantId as meterwater_tenantId"
            + " FROM egwtr_meter_water_rates meterwater INNER JOIN egwtr_pipesize pipesize ON meterwater.pipesizeid = pipesize.id and meterwater.tenantId= pipesize.tenantId "
            + " INNER JOIN egwtr_water_source_type watersource ON meterwater.sourcetypeid = watersource.id and meterwater.tenantId=watersource.tenantId "
            + " INNER JOIN egwtr_usage_type usage ON meterwater.usagetypeid = usage.id and meterwater.tenantId=usage.tenantId "
            + " INNER JOIN egwtr_usage_type subusage ON meterwater.subusagetypeid = subusage.id and meterwater.tenantId=subusage.tenantId ";

    public String getQuery(final MeterWaterRatesGetRequest meterWaterRatesGetRequest,
            @SuppressWarnings("rawtypes") final Map<String, Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, meterWaterRatesGetRequest);
        addOrderByClause(selectQuery, meterWaterRatesGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("unchecked")
    private void addWhereClause(final StringBuilder selectQuery, final Map<String, Object> preparedStatementValues,
            final MeterWaterRatesGetRequest meterWaterRatesGetRequest) {

        if (meterWaterRatesGetRequest.getIds() == null && meterWaterRatesGetRequest.getSourceTypeName() == null
                && meterWaterRatesGetRequest.getUsageTypeName() == null &&
                meterWaterRatesGetRequest.getPipeSize() == null
                && meterWaterRatesGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (meterWaterRatesGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" meterwater.tenantId = :tenantId");
            preparedStatementValues.put("tenantId", meterWaterRatesGetRequest.getTenantId());
        }

        if (meterWaterRatesGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.id IN (:ids)");
            preparedStatementValues.put("ids", meterWaterRatesGetRequest.getIds());
        }

        if (meterWaterRatesGetRequest.getUsageTypeCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.usagetypeid = :usagetypeid");
            preparedStatementValues.put("usagetypeid", Long.valueOf(meterWaterRatesGetRequest.getUsageTypeId()));
        }

        if (meterWaterRatesGetRequest.getSubUsageTypeCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.subusagetypeid = :subusagetypeid");
            preparedStatementValues.put("subusagetypeid", Long.valueOf(meterWaterRatesGetRequest.getSubUsageTypeId()));
        }
        if (meterWaterRatesGetRequest.getOutsideUlb() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.outsideulb = :outsideulb");
            preparedStatementValues.put("outsideulb", meterWaterRatesGetRequest.getOutsideUlb());
        }

        if (meterWaterRatesGetRequest.getSourceTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.sourcetypeid = :sourcetypeid");
            preparedStatementValues.put("sourcetypeid", meterWaterRatesGetRequest.getSourceTypeId());
        }

        if (meterWaterRatesGetRequest.getPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.pipesizeid = :pipesizeid");
            preparedStatementValues.put("pipesizeid", Long.valueOf(meterWaterRatesGetRequest.getPipeSizeId()));
        }

        if (meterWaterRatesGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.active = :active");
            preparedStatementValues.put("active", meterWaterRatesGetRequest.getActive());
        }

    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final MeterWaterRatesGetRequest meterWaterRatesGetRequest) {
        final String sortBy = meterWaterRatesGetRequest.getSortBy() == null ? "meterwater.id"
                : "meterwater." + meterWaterRatesGetRequest.getSortBy();
        final String sortOrder = meterWaterRatesGetRequest.getSortOrder() == null ? "DESC"
                : meterWaterRatesGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    public static String insertMeterWaterRatesQuery() {
        return "INSERT INTO egwtr_meter_water_rates(id,code,billingtype,usagetypeid,subusagetypeid,outsideulb,sourcetypeid,pipesizeid,fromdate,todate,active,"
                + "createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:billingtype,:usagetypeid,:subusagetypeid,:outsideulb,:sourcetypeid,:pipesizeid,:fromdate,:todate,:active,"
                + ":createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String insertSlabQuery() {
        return "INSERT INTO egwtr_slab(id,meterwaterratesid,fromunit,tounit,unitrate,tenantid) values "
                + "(nextval('seq_egwtr_slab'),:meterwaterratesid,:fromunit,:tounit,:unitrate,:tenantid)";
    }

    public static String updateMeterWaterRatesQuery() {
        return "UPDATE egwtr_meter_water_rates SET billingtype = :billingtype,usagetypeid = :usagetypeid, subusagetypeid = :subusagetypeid,outsideulb = :outsideulb,sourcetypeid = :sourcetypeid,pipesizeid = :pipesizeid ,fromdate = :fromdate "
                + " , todate = :todate ,active = :active,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code  and tenantid = :tenantid ";
    }

    public static String selectMeterWaterRatesByCodeQuery() {
        return " select code FROM egwtr_meter_water_rates where usagetypeid = ? and subusagetypeid = ? and "
                + "sourcetypeid = ? and pipesizeid = ? and tenantId = ?";
    }

    public static String selectMeterWaterRatesByCodeNotInQuery() {
        return " select code from egwtr_meter_water_rates where  usagetypeid = ? and  subusagetypeid = ? and "
                + " sourcetypeid = ? and pipesizeid = ? and tenantId = ? and code != ? ";
    }

    public static String getSourceTypeIdQuery() {
        return " select id FROM egwtr_water_source_type  where name= ? and tenantId = ? ";
    }

    public static String getSourceTypeIdQueryForSearch() {
        return " select id FROM egwtr_water_source_type  where name= :name and tenantId = :tenantId ";
    }

    public static String getSourceTypeNameQuery() {
        return "SELECT name FROM egwtr_water_source_type WHERE id = ? and tenantId = ? ";
    }

    public static String getUsageTypeIdQueryForSearch() {
        return " select id FROM egwtr_usage_type  where code= :code and tenantId = :tenantId ";
    }

    public static String getUsageTypeIdQuery() {
        return " select id FROM egwtr_usage_type  where code= ? and tenantId = ? ";
    }

    public static String getPipeSizeIdQuery() {
        return " select id FROM egwtr_pipesize where sizeinmilimeter= ? and tenantId = ?";
    }

    public static String getPipeSizeIdQueryForSearch() {
        return " select id FROM egwtr_pipesize where sizeinmilimeter= :sizeinmilimeter and tenantId = :tenantId ";
    }

    public static String getPipeSizeInmm() {
        return "SELECT sizeinmilimeter FROM egwtr_pipesize WHERE id = ? and tenantId = ? ";
    }

    public String deleteSlabValuesQuery() {
        return "Delete from egwtr_slab where meterwaterratesid =:meterwaterratesid and tenantId =:tenantId";
    }

    public String getSlabDetailsQuery() {
        return "Select slab.id as slab_id,slab.meterwaterratesid as slab_meterwaterratesid,slab.fromunit as slab_fromunit,"
                + "slab.tounit as slab_tounit,slab.unitrate as slab_unitrate,slab.tenantid as slab_tenantid"
                + " from egwtr_slab slab where slab.meterwaterratesid =:meterwaterratesid and slab.tenantid =:tenantid";
    }

}
