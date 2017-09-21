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
package org.egov.wcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.NonMeterWaterRates;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class NonMeterWaterRatesRowMapper implements RowMapper<NonMeterWaterRates> {

    @Override
    public NonMeterWaterRates mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final NonMeterWaterRates nonMeterWaterRates = new NonMeterWaterRates();
        nonMeterWaterRates.setId(rs.getLong("nonmeterwater_id"));
        nonMeterWaterRates.setCode(rs.getString("nonmeterwater_code"));
        nonMeterWaterRates.setBillingType(rs.getString("billingtype"));
        nonMeterWaterRates.setUsageTypeId(rs.getLong("nonmeterwater_usagetypeid"));
        nonMeterWaterRates.setSourceTypeId(rs.getLong("nonmeterwater_sourcetypeid"));
        nonMeterWaterRates.setPipeSizeId(rs.getLong("nonmeterwater_pipesizeId"));
        nonMeterWaterRates.setActive(rs.getBoolean("nonmeterwater_active"));
        nonMeterWaterRates.setFromDate(rs.getLong("nonmeterwater_fromdate"));
        nonMeterWaterRates.setAmount(rs.getDouble("nonmeterwater_amount"));
        nonMeterWaterRates.setActive(rs.getBoolean("nonmeterwater_active"));
        nonMeterWaterRates.setTenantId(rs.getString("nonmeterwater_tenantId"));
        nonMeterWaterRates.setOutsideUlb((Boolean) rs.getObject("nonmeterwater_outsideulb"));
        nonMeterWaterRates.setSubUsageTypeId(rs.getLong("nonmeterwater_subusagetypeid"));
        nonMeterWaterRates.setPipeSize(rs.getDouble("pipesize_sizeinmm"));
        nonMeterWaterRates.setPipeSizeInInch(rs.getDouble("pipeSizeInInch"));
        nonMeterWaterRates.setSourceTypeName(rs.getString("watersource_name"));
        nonMeterWaterRates.setConnectionType(rs.getString("connectiontype"));
        nonMeterWaterRates.setNoOfTaps(rs.getLong("nonmeterwater_nooftaps"));
        nonMeterWaterRates.setUsageTypeCode(rs.getString("usage_code"));
        nonMeterWaterRates.setSubUsageTypeCode(rs.getString("subusage_code"));
        nonMeterWaterRates.setUsageTypeName(rs.getString("usage_name"));
        nonMeterWaterRates.setSubUsageTypeName(rs.getString("subusage_name"));
        return nonMeterWaterRates;
    }
}
