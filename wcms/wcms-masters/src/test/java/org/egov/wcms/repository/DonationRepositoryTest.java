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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.AuditDetails;
import org.egov.wcms.model.Donation;
import org.egov.wcms.repository.builder.DonationQueryBuilder;
import org.egov.wcms.repository.rowmapper.DonationRowMapper;
import org.egov.wcms.service.RestWaterExternalMasterService;
import org.egov.wcms.web.contract.DonationGetRequest;
import org.egov.wcms.web.contract.DonationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DonationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DonationRowMapper donationRowMapper;

    @InjectMocks
    private DonationRepository donationRepository;

    @Mock
    private DonationQueryBuilder donationQueryBuilder;

    @Mock
    private RestWaterExternalMasterService restExternalMasterService;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void test_Should_Create_Donation_Valid() {
        final DonationRequest donationRequest = getDonationRequest();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(donationRequest.equals(donationRepository.create(donationRequest)));
    }

    @Test
    public void test_Should_Create_Donation_Invalid() {
        final DonationRequest donationRequest = getDonationRequest();
        final List<Donation> donation = donationRequest.getDonations();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(!donation.equals(donationRepository.create(donationRequest)));
    }

    @Test
    public void test_Should_Find_Donation_Valid() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final DonationGetRequest donationRequest = getDonationvalidaRequest();
        final String queryString = "MyQuery";
        final List<Donation> donations = new ArrayList<>();
        when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), donationRowMapper))
                .thenReturn(donations);

        assertTrue(donations.equals(donationRepository.findForCriteria(donationRequest)));
    }

    private DonationGetRequest getDonationvalidaRequest() {
        final DonationGetRequest donation = new DonationGetRequest();
        donation.setUsageTypeCode("abcd");
        donation.setSubUsageTypeCode("test");
        donation.setMaxPipeSize(4d);
        donation.setMinPipeSize(1d);
        donation.setFromDate(new Date().getTime());
        donation.setToDate(new Date().getTime());

        donation.setActive(true);

        return donation;
    }

    private DonationRequest getDonationRequest() {
        final DonationRequest donationReq = new DonationRequest();
        final List<Donation> donationList = new ArrayList<>();

        donationList.add(getDonation());

        final RequestInfo requestInfo = new RequestInfo();
        final User newUser = new User();
        newUser.setId(2L);
        requestInfo.setUserInfo(newUser);
        donationReq.setRequestInfo(requestInfo);
        donationReq.setDonations(donationList);
        return donationReq;
    }

    @Test
    public void test_Should_Modify_Donation() throws Exception {
        final DonationRequest donationRequest = new DonationRequest();

        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final List<Donation> donationList = new ArrayList<>();
        donationList.add(getDonation());
        donationRequest.setRequestInfo(requestInfo);
        donationRequest.setDonations(donationList);

        assertNotNull(donationRepository.update(donationRequest));

    }

    @Test(expected = Exception.class)
    public void test_throwException_Modify_Donation() throws Exception {
        final DonationRequest donationRequest = new DonationRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final List<Donation> donationList = new ArrayList<>();
        donationList.add(getDonation());
        donationRequest.setRequestInfo(requestInfo);
        donationRequest.setDonations(donationList);

        assertNotNull(donationRepository.update(donationRequest));

    }

    private Donation getDonation() {
        final Donation donation = new Donation();
        final AuditDetails auditDetails = new AuditDetails();
        donation.setAuditDetails(auditDetails);
        donation.getAuditDetails().setCreatedBy(1L);
        donation.setActive(true);
        donation.setCode("2");
        donation.getAuditDetails().setCreatedBy(1L);
        donation.setUsageTypeId(2l);
        donation.setSubUsageTypeId(3l);
        donation.setFromDate(new Date().getTime());
        donation.setToDate(new Date().getTime());
        donation.setMaxPipeSizeId(2L);
        donation.setMinPipeSizeId(2L);
        return donation;
    }

}