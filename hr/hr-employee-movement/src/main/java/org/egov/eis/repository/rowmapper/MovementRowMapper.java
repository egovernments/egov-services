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
import java.util.Date;

import org.egov.eis.model.Movement;
import org.egov.eis.model.PromotionBasis;
import org.egov.eis.model.TransferReason;
import org.egov.eis.model.enums.TransferType;
import org.egov.eis.model.enums.TypeOfMovement;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MovementRowMapper implements RowMapper<Movement> {

    @Override
    public Movement mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        final Movement movement = new Movement();
        movement.setId(rs.getLong("m_id"));
        movement.setEmployeeId(rs.getLong("m_employee"));
        movement.setTypeOfMovement(TypeOfMovement.fromValue(rs.getString("m_typeOfMovement")));
        movement.setCurrentAssignment(rs.getLong("m_currentAssignment"));
        movement.setTransferType(TransferType.fromValue(rs.getString("m_transferType")));

        final PromotionBasis promotionBasis = new PromotionBasis();
        promotionBasis.setId(rs.getLong("m_promotionBasis"));
        movement.setPromotionBasis(promotionBasis);

        movement.setRemarks(rs.getString("m_remarks"));

        final TransferReason transferReason = new TransferReason();
        transferReason.setId(rs.getLong("m_reason"));
        movement.setReason(transferReason);

        try {
            Date date = isEmpty(rs.getDate("m_createdDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("m_createdDate")));
            movement.setCreatedDate(date);
            date = isEmpty(rs.getDate("m_lastModifiedDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("m_lastModifiedDate")));
            movement.setLastModifiedDate(date);
            date = isEmpty(rs.getDate("m_effectiveFrom")) ? null : sdf.parse(sdf.format(rs.getDate("m_effectiveFrom")));
            movement.setEffectiveFrom(date);
            date = isEmpty(rs.getDate("m_enquiryPassedDate")) ? null : sdf.parse(sdf.format(rs.getDate("m_enquiryPassedDate")));
            movement.setEnquiryPassedDate(date);
        } catch (final ParseException e) {
            e.printStackTrace();
            throw new SQLException("Parse exception while parsing Date");
        }

        movement.setTransferedLocation(rs.getString("m_transferedLocation"));
        movement.setDepartmentAssigned(rs.getLong("m_departmentAssigned"));
        movement.setDesignationAssigned(rs.getLong("m_designationAssigned"));
        movement.setPositionAssigned(rs.getLong("m_positionAssigned"));
        movement.setFundAssigned(rs.getLong("m_fundAssigned"));
        movement.setFunctionAssigned(rs.getLong("m_functionAssigned"));
        movement.setEmployeeAcceptance(rs.getBoolean("m_employeeAcceptance"));
        movement.setStatus(rs.getLong("m_status"));
        movement.setStateId(rs.getLong("m_stateId"));

        movement.setCreatedBy((Long) rs.getObject("m_createdBy"));
        movement.setLastModifiedBy((Long) rs.getObject("m_lastModifiedBy"));
        movement.setTenantId(rs.getString("m_tenantId"));

        return movement;
    }
}