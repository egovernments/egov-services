/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.*;
import org.egov.eis.model.enums.MaritalStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class PayscaleHeaderRowMapper implements ResultSetExtractor<List<PayscaleHeader>> {

    @Override
    public List<PayscaleHeader> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Long, PayscaleHeaderInfo> payscaleHeaderInfoMap = getPayscaleHeaderInfoMap(rs);
        List<PayscaleHeader> payscaleHeaderList = getPayscaleHeaderList(payscaleHeaderInfoMap);

        return payscaleHeaderList;
    }

    /**
     * Convert flat Result set data into hierarchical/map structure.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Map<Long, PayscaleHeaderInfo> getPayscaleHeaderInfoMap(ResultSet rs) throws SQLException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Map<Long, PayscaleHeaderInfo> payscaleHeaderInfoMap = new LinkedHashMap<>();

        while (rs.next()) {
            Long payscaleHeaderId = (Long) rs.getObject("ph_id");

            PayscaleHeaderInfo payscaleHeaderInfo = payscaleHeaderInfoMap.get(payscaleHeaderId);

            // populate payscaleHeaderInfo fields from result set
            if (payscaleHeaderInfo == null) {
                payscaleHeaderInfo = new PayscaleHeaderInfo();
                payscaleHeaderInfo.setId((Long) rs.getObject("ph_id"));
                payscaleHeaderInfo.setPaycommission(rs.getString("ph_paycommission"));
                payscaleHeaderInfo.setPayscale(rs.getString("ph_payscale"));
                payscaleHeaderInfo.setAmountFrom((Long) rs.getObject("ph_amountFrom"));
                payscaleHeaderInfo.setAmountTo((Long) rs.getObject("ph_amountTo"));
                payscaleHeaderInfo.setTenantId(rs.getString("ph_tenantId"));
                payscaleHeaderInfo.setCreatedBy((Long) rs.getObject("ph_createdBy"));
                payscaleHeaderInfo.setLastmodifiedBy((Long) rs.getObject("ph_lastmodifiedBy"));
                try {
                    Date date = isEmpty(rs.getDate("ph_createdDate")) ? null
                            : sdf.parse(sdf.format(rs.getDate("ph_createdDate")));
                    payscaleHeaderInfo.setCreatedDate(date);
                    date = isEmpty(rs.getDate("ph_lastmodifiedDate")) ? null
                            : sdf.parse(sdf.format(rs.getDate("ph_lastmodifiedDate")));
                    payscaleHeaderInfo.setLastmodifiedDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new SQLException("Parse exception while parsing date");
                }

                payscaleHeaderInfoMap.put(payscaleHeaderId, payscaleHeaderInfo);
            }

            Map<Long, PayscaleDetailsInfo> payscaleDetailsInfoMap = payscaleHeaderInfo.getPayscaleDetails();

            Long payscaleDetailsId = (Long) rs.getObject("pd_id");
            PayscaleDetailsInfo payscaleDetailsInfo = payscaleDetailsInfoMap.get(payscaleDetailsId);

            if (payscaleDetailsInfo == null) {
                payscaleDetailsInfo = new PayscaleDetailsInfo();
                payscaleDetailsInfo.setId((Long) rs.getObject("pd_id"));
                payscaleDetailsInfo.setBasicFrom((Long) rs.getObject("pd_basicFrom"));
                payscaleDetailsInfo.setBasicTo((Long) rs.getObject("pd_basicTo"));
                payscaleDetailsInfo.setIncrement((Long) rs.getObject("pd_increment"));
                payscaleDetailsInfoMap.put(payscaleDetailsId, payscaleDetailsInfo);
            }


        }


        return payscaleHeaderInfoMap;
    }

    /**
     * Convert intermediate Map into List of EmployeeInfo
     *
     * @param payscaleHeaderInfoMap
     * @return
     */
    private List<PayscaleHeader> getPayscaleHeaderList(Map<Long, PayscaleHeaderInfo> payscaleHeaderInfoMap) {
        List<PayscaleHeader> payscaleHeaders = new ArrayList<>();
        for (Map.Entry<Long, PayscaleHeaderInfo> payscaleHeaderInfoEntry : payscaleHeaderInfoMap.entrySet()) {
            PayscaleHeaderInfo payscaleHeaderInfo = payscaleHeaderInfoEntry.getValue();

            PayscaleHeader payscaleHeader = PayscaleHeader.builder().id(payscaleHeaderInfo.getId())
                    .paycommission(payscaleHeaderInfo.getPaycommission()).payscale(payscaleHeaderInfo.getPayscale())
                    .amountFrom(payscaleHeaderInfo.getAmountFrom()).amountTo(payscaleHeaderInfo.getAmountTo())
                    .createdBy(payscaleHeaderInfo.getCreatedBy()).createdDate(payscaleHeaderInfo.getCreatedDate())
                    .lastModifiedBy(payscaleHeaderInfo.getLastmodifiedBy()).lastModifiedDate(payscaleHeaderInfo.getLastmodifiedDate())
                    .tenantId(payscaleHeaderInfo.getTenantId()).build();

            List<PayscaleDetails> payscaleDetails = new ArrayList<>();
            for (Map.Entry<Long, PayscaleDetailsInfo> payscaleDetailsInfoEntry : payscaleHeaderInfo.getPayscaleDetails().entrySet()) {
                PayscaleDetailsInfo payscaleDetailsInfo = payscaleDetailsInfoEntry.getValue();

                PayscaleDetails payscaleDet = PayscaleDetails.builder().id(payscaleDetailsInfo.getId())
                        .basicFrom(payscaleDetailsInfo.getBasicFrom()).basicTo(payscaleDetailsInfo.getBasicTo()).build();

                payscaleDetails.add(payscaleDet);
            }

            payscaleHeader.setPayscaleDetails(payscaleDetails);

            payscaleHeaders.add(payscaleHeader);
        }
        return payscaleHeaders;
    }


    @Getter
    @Setter
    private class PayscaleHeaderInfo {
        private Long id;
        private String paycommission;
        private String payscale;
        private Long amountFrom;
        private Long amountTo;
        private Map<Long, PayscaleDetailsInfo> payscaleDetails = new LinkedHashMap<>();
        private Long createdBy;
        private Date createdDate;
        private Long lastmodifiedBy;
        private Date lastmodifiedDate;
        private String tenantId;
    }

    @Getter
    @Setter
    private class PayscaleDetailsInfo {
        private Long id;
        private Long basicFrom;
        private Long basicTo;
        private Long increment;
    }
}