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
package org.egov.wcms.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.wcms.model.Donation;
import org.egov.wcms.repository.rowmapper.DonationRowMapper;
import org.egov.wcms.web.contract.DonationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DonationRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(DonationRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private DonationRowMapper donationRowMapper;
    
	public DonationRequest persistDonationDetails(final DonationRequest donationRequest) {
		LOGGER.info("Donation Request::" + donationRequest);
		final String donationInsert = "INSERT INTO egwtr_donation "
				+ "(id, property_type, usage_type, category, hsc_pipesize_max, hsc_pipesize_min, from_date, to_date, donation_amount, active, tenantid, createddate, createdby) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] obj = new Object[] { donationRequest.getDonation().getId(),
				donationRequest.getDonation().getPropertyTypeId(), donationRequest.getDonation().getUsageTypeId(),
				donationRequest.getDonation().getCategoryTypeId(), donationRequest.getDonation().getMaxHSCPipeSizeId(),
				donationRequest.getDonation().getMinHSCPipeSizeId(), donationRequest.getDonation().getFromDate(), donationRequest.getDonation().getToDate(),
				donationRequest.getDonation().getDonationAmount(), donationRequest.getDonation().isActive(),
				donationRequest.getDonation().getTenantId(), new Date(new java.util.Date().getTime()), donationRequest.getRequestInfo().getUserInfo().getId() };
		jdbcTemplate.update(donationInsert, obj);
		return donationRequest;
	}
	
	public DonationRequest persistModifyDonationDetails(final DonationRequest donationRequest) {
		LOGGER.info("Donation update Request::" + donationRequest);
		final String donationInsert = "update egwtr_donation set property_type=?,usage_type=?,category=?, hsc_pipesize_max=?, hsc_pipesize_min=?,"
				+ " from_date=?, to_date=?, donation_amount=?, active=?,lastmodifiedby=?, lastmodifieddate=? where id=?) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] obj = new Object[] {
				donationRequest.getDonation().getPropertyTypeId(), donationRequest.getDonation().getUsageTypeId(),
				donationRequest.getDonation().getCategoryTypeId(), donationRequest.getDonation().getMaxHSCPipeSizeId(),
				donationRequest.getDonation().getMinHSCPipeSizeId(), donationRequest.getDonation().getFromDate(), donationRequest.getDonation().getToDate(),
				donationRequest.getDonation().getDonationAmount(), donationRequest.getDonation().isActive(),
				donationRequest.getRequestInfo().getUserInfo().getId(),new Date(new java.util.Date().getTime()),donationRequest.getDonation().getId() };
		jdbcTemplate.update(donationInsert, obj);
		return donationRequest;
	}

	
	public List<Donation> getDonationList(Donation donation) {
        List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(8L);
        preparedStatementValues.add(1L);
        preparedStatementValues.add(7L);
        preparedStatementValues.add(7L);
        preparedStatementValues.add(1L);
        preparedStatementValues.add(new Date());
        preparedStatementValues.add(new Date());
        String queryStr = "SELECT * FROM egwtr_donation WHERE property_type = ? AND usage_type = ? "
        		+ " AND category = ? AND hsc_pipesize_max = ? AND hsc_pipesize_min = ? AND from_date <= ? "
        		+ " AND to_date >= ? AND active IS TRUE" ; 
        List<Donation> donationList = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), donationRowMapper);
        return donationList;
    }
}


