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

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeePayscale;
import org.egov.eis.model.PayscaleHeader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class EmployeePayscaleRowMapper implements RowMapper<EmployeePayscale> {

    @Override
    public EmployeePayscale mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        EmployeePayscale employeePayscale = new EmployeePayscale();
        employeePayscale.setId((Long) rs.getObject("ep_id"));
        Employee employee = new Employee();
        employee.setId((Long) rs.getObject("emp_id"));
        employee.setCode(rs.getString("emp_code"));
        PayscaleHeader payscaleHeader = new PayscaleHeader();
        payscaleHeader.setId((Long) rs.getObject("ph_id"));
        payscaleHeader.setPaycommission(rs.getString("ph_paycommission"));
        payscaleHeader.setPayscale(rs.getString("ph_payscale"));
        payscaleHeader.setAmountFrom((Long) rs.getObject("ph_amountFrom"));
        payscaleHeader.setAmountTo((Long) rs.getObject("ph_amountTo"));
        employeePayscale.setEmployee(employee);
        employeePayscale.setPayscaleHeader(payscaleHeader);
        employeePayscale.setBasicAmount((Long) rs.getObject("ep_basicAmount"));
        employeePayscale.setIncrementMonth(rs.getString("ep_incrementMonth"));
        employeePayscale.setReason(rs.getString("ep_reason"));
        employeePayscale.setTenantId(rs.getString("ep_tenantId"));
        employeePayscale.setCreatedBy((Long) rs.getObject("ep_createdBy"));
        employeePayscale.setLastModifiedBy((Long) rs.getObject("ep_lastmodifiedBy"));
        try {
            Date date = isEmpty(rs.getDate("ep_effectiveFrom")) ? null
                    : sdf.parse(sdf.format(rs.getDate("ep_effectiveFrom")));
            employeePayscale.setEffectiveFrom(date);
            date = isEmpty(rs.getDate("ep_createdDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("ep_createdDate")));
            employeePayscale.setCreatedDate(date);
            date = isEmpty(rs.getDate("ep_lastmodifiedDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("ep_lastmodifiedDate")));
            employeePayscale.setLastModifiedDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SQLException("Parse exception while parsing date");
        }
        return employeePayscale;
    }
}