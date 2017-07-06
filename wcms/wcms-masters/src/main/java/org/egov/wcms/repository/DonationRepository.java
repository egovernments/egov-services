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
import org.egov.wcms.repository.builder.DonationQueryBuilder;
import org.egov.wcms.repository.builder.PropertyPipeSizeQueryBuilder;
import org.egov.wcms.repository.builder.PropertyTypeCategoryTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.DonationRowMapper;
import org.egov.wcms.web.contract.DonationGetRequest;
import org.egov.wcms.web.contract.DonationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DonationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DonationRowMapper donationRowMapper;

    @Autowired
    private DonationQueryBuilder donationQueryBuilder;

    public DonationRequest persistDonationDetails(final DonationRequest donationRequest) {
        log.info("Donation Request::" + donationRequest);

        final String donationInsert = DonationQueryBuilder.donationInsertQuery();
        final Donation donation = donationRequest.getDonation();

        final String categoryQuery = DonationQueryBuilder.getCategoryId();
        Long categoryId = 0L;
        try {
            categoryId = jdbcTemplate.queryForObject(categoryQuery,
                    new Object[] { donation.getCategory(), donation.getTenantId() }, Long.class);
            log.info("Category Id: " + categoryId);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (categoryId == null)
            log.info("Invalid input.");

        final String pipesizeQuery = DonationQueryBuilder.getPipeSizeIdQuery();
        Long maxPipeSizeId = 0L;
        try {
            maxPipeSizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                    new Object[] { donation.getMaxPipeSize(), donation.getTenantId() }, Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (maxPipeSizeId == null)
            log.info("Invalid input for MaxPipeSize.");

        Long minPipeSizeId = 0L;
        try {
            minPipeSizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                    new Object[] { donation.getMinPipeSize(), donation.getTenantId() }, Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (minPipeSizeId == null)
            log.info("Invalid input for MinPipeSize");

        final Object[] obj = new Object[] { donation.getPropertyTypeId(), donation.getUsageTypeId(), categoryId,
                maxPipeSizeId, minPipeSizeId, donation.getFromDate(), donation.getToDate(),
                donation.getDonationAmount(), donation.getActive(), donation.getTenantId(),
                Long.valueOf(donationRequest.getRequestInfo().getUserInfo().getId()),
                Long.valueOf(donationRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), };
        jdbcTemplate.update(donationInsert, obj);
        return donationRequest;
    }

    public DonationRequest persistModifyDonationDetails(final DonationRequest donationRequest) {
        log.info("Donation update Request::" + donationRequest);
        final String donationInsert = DonationQueryBuilder.donationUpdateQuery();
        final Donation donation = donationRequest.getDonation();
        final String categoryQuery = DonationQueryBuilder.getCategoryId();
        Long categoryId = 0L;
        try {
            categoryId = jdbcTemplate.queryForObject(categoryQuery,
                    new Object[] { donation.getCategory(), donation.getTenantId() }, Long.class);
            log.info("Category Id: " + categoryId);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (categoryId == null)
            log.info("Invalid input.");

        final String pipesizeQuery = DonationQueryBuilder.getPipeSizeIdQuery();
        Long maxPipeSizeId = 0L;
        try {
            maxPipeSizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                    new Object[] { donation.getMaxPipeSize(), donation.getTenantId() }, Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set for max pipesize");
        }
        if (maxPipeSizeId == null)
            log.info("Invalid input for MaxPipeSize.");

        Long minPipeSizeId = 0L;
        try {
            minPipeSizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                    new Object[] { donation.getMinPipeSize(), donation.getTenantId() }, Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set for min pipesize");
        }
        if (minPipeSizeId == null)
            log.info("Invalid input for MinPipeSize");

        final Object[] obj = new Object[] { donation.getPropertyTypeId(), donation.getUsageTypeId(), categoryId,
                maxPipeSizeId, minPipeSizeId, donation.getFromDate(), donation.getToDate(),
                donation.getDonationAmount(), donation.getActive(),
                Long.valueOf(donationRequest.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), donation.getId() };
        jdbcTemplate.update(donationInsert, obj);
        return donationRequest;
    }

    public List<Donation> findForCriteria(final DonationGetRequest donation) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        try {
            if (donation.getCategoryType() != null)
                donation.setCategoryTypeId(jdbcTemplate.queryForObject(DonationQueryBuilder.getCategoryId(),
                        new Object[] { donation.getCategoryType(),donation.getTenantId() }, Long.class));
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty set for category type.");
        }

            try {
                if (donation.getMaxPipeSize() != null)
                    donation.setMaxPipeSizeId(jdbcTemplate.queryForObject(DonationQueryBuilder.getPipeSizeIdQuery(),
                                    new Object[] { donation.getMaxPipeSize(),
                                            donation.getTenantId() },
                                    Long.class));
            } catch (final EmptyResultDataAccessException e) {
                log.error("EmptyResultDataAccessException: Query returned empty RS.");

            }
            try {
                if (donation.getMinPipeSize() != null)
                    donation.setMinPipeSizeId(jdbcTemplate.queryForObject(DonationQueryBuilder.getPipeSizeIdQuery(),
                                    new Object[] { donation.getMinPipeSize(),
                                            donation.getTenantId() },
                                    Long.class));
            } catch (final EmptyResultDataAccessException e) {
                log.error("EmptyResultDataAccessException: Query returned empty RS.");

            }

        final String queryStr = donationQueryBuilder.getQuery(donation, preparedStatementValues);
        final String categoryNameQuery = DonationQueryBuilder.getCategoryTypeName();
        final String pipeSizeInmmQuery = DonationQueryBuilder.getPipeSizeInmm();
        final List<Donation> donationList = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                donationRowMapper);
        for (final Donation donations : donationList) {
            donations.setCategory(jdbcTemplate.queryForObject(categoryNameQuery,
                    new Object[] { donations.getCategoryTypeId(),donations.getTenantId() }, String.class));
            donations.setMaxPipeSize(jdbcTemplate.queryForObject(pipeSizeInmmQuery,
                    new Object[] { donations.getMaxPipeSizeId(),donations.getTenantId() }, Double.class));
            donations.setMinPipeSize(jdbcTemplate.queryForObject(pipeSizeInmmQuery,
                    new Object[] { donations.getMinPipeSizeId(),donations.getTenantId() }, Double.class));
        }
        return donationList;

    }
}
