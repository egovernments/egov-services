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

import org.egov.wcms.web.contract.MeterWaterRatesGetRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MeterWaterRatesQueryBuilder {

    private static final String BASE_QUERY = "SELECT meterwater.id as meterwater_id,meterwater.code as meterwater_code, meterwater.billingtype as billingtype,meterwater.sourcetypeid "
            + "as meterwater_sourcetypeid, meterwater.usagetypeid as meterwater_usagetypeid,"
            + " meterwater.subusagetypeid as meterwater_subusagetypeid,meterwater.outsideulb as meterwater_outsideulb,"
            + "meterwater.pipesizeid as meterwater_pipesizeId,pipesize.sizeinmilimeter as pipesize_sizeinmm,meterwater.fromdate as meterwater_fromdate,meterwater.todate as meterwater_todate,"
            + "meterwater.active as meterwater_active, watersource.name as watersource_name,"
            + "usage.name as usage_name, subusage.name as subusage_name,"
            + "meterwater.tenantId as meterwater_tenantId ,slab.id as slab_id,slab.meterwaterratesid as slab_meterwaterratesid,"
            + "slab.fromunit as slab_fromunit,slab.tounit as slab_tounit,slab.unitrate as slab_unitrate,slab.tenantId as slab_tenantId"
            + " FROM egwtr_meter_water_rates meterwater LEFT JOIN egwtr_slab slab ON slab.meterwaterratesid=meterwater.id LEFT JOIN egwtr_pipesize pipesize ON meterwater.pipesizeid = pipesize.id "
            + " LEFT JOIN egwtr_water_source_type watersource ON meterwater.sourcetypeid = watersource.id"
    		+ " LEFT JOIN egwtr_usage_type usage ON meterwater.usagetypeid = usage.id"
    		+ " LEFT JOIN egwtr_usage_type subusage ON meterwater.subusagetypeid = subusage.id";

    public String getQuery(final MeterWaterRatesGetRequest meterWaterRatesGetRequest,
            @SuppressWarnings("rawtypes") final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, meterWaterRatesGetRequest);
        addOrderByClause(selectQuery, meterWaterRatesGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("unchecked")
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final MeterWaterRatesGetRequest meterWaterRatesGetRequest) {

        if (meterWaterRatesGetRequest.getId() == null && meterWaterRatesGetRequest.getSourceTypeName() == null
                && meterWaterRatesGetRequest.getUsageTypeName() == null &&
                meterWaterRatesGetRequest.getPipeSize() == null
                && meterWaterRatesGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (meterWaterRatesGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" meterwater.tenantId = ?");
            preparedStatementValues.add(meterWaterRatesGetRequest.getTenantId());
        }

        if (meterWaterRatesGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.id IN " + getIdQuery(meterWaterRatesGetRequest.getId()));
        }

        if (meterWaterRatesGetRequest.getUsageTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.usagetypeid = ?");
            preparedStatementValues.add(meterWaterRatesGetRequest.getUsageTypeId());
        }

        if (meterWaterRatesGetRequest.getSubUsageType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.subusagetypeid = ?");
            preparedStatementValues.add(meterWaterRatesGetRequest.getSubUsageTypeId());
        }
        if (meterWaterRatesGetRequest.getOutsideUlb() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.outsideulb = ?");
            preparedStatementValues.add(meterWaterRatesGetRequest.getOutsideUlb());
        }

        if (meterWaterRatesGetRequest.getSourceTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.sourcetypeid = ?");
            preparedStatementValues.add(meterWaterRatesGetRequest.getSourceTypeId());
        }

        if (meterWaterRatesGetRequest.getPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.pipesizeid = ?");
            preparedStatementValues.add(meterWaterRatesGetRequest.getPipeSizeId());
        }

        if (meterWaterRatesGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" meterwater.active = ?");
            preparedStatementValues.add(meterWaterRatesGetRequest.getActive());
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

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
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

    public static String getSourceTypeNameQuery() {
        return "SELECT name FROM egwtr_water_source_type WHERE id = ? and tenantId = ? ";
    }
    
    public static String getUsageTypeIdQuery() {
        return " select id FROM egwtr_usage_type  where name= ? and tenantId = ? ";
    }

}
