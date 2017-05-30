package org.egov.wcms.repository;

import java.util.Date;

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
    
	public DonationRequest persistDonationDetails(final DonationRequest donationRequest) {
		LOGGER.info("Donation Request::" + donationRequest);
		final String donationInsert = "INSERT INTO egwtr_donation "
				+ "(id, property_type, usage_type, category, hsc_pipesize_max, hsc_pipesize_min, from_date, to_date, donation_amount, active, tenantid, createddate, createdby) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] obj = new Object[] { donationRequest.getDonation().getId(),
				donationRequest.getDonation().getPropertyType(), donationRequest.getDonation().getUsageType(),
				donationRequest.getDonation().getCategory(), donationRequest.getDonation().getMaxHSCPipeSize(),
				donationRequest.getDonation().getMinHSCPipeSize(), donationRequest.getDonation().getFromDate(), donationRequest.getDonation().getToDate(),
				donationRequest.getDonation().getDonationAmount(), donationRequest.getDonation().isActive(),
				donationRequest.getDonation().getTenantId(), new Date(new java.util.Date().getTime()), 1L };
		jdbcTemplate.update(donationInsert, obj);
		return donationRequest;
	}
}


