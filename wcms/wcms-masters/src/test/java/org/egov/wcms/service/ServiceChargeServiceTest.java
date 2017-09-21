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
package org.egov.wcms.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.ServiceCharge;
import org.egov.wcms.model.ServiceChargeDetails;
import org.egov.wcms.repository.ServiceChargeRepository;
import org.egov.wcms.web.contract.ServiceChargeGetRequest;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceChargeServiceTest {
    @Mock
    private ServiceChargeRepository serviceChargeRepository;

    @InjectMocks
    private ServiceChargeService serviceChargeService;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @Test
    public void test_should_push_create_ServiceCharge_to_Queue() {
        when(codeGeneratorService.generate("SEQ_EGWTR_SERVICECHARGE")).thenReturn("1", "2", "3", "4");
        when(serviceChargeRepository.pushCreateToQueue(getServiceChargeRequestAfterCodeAppend()))
                .thenReturn(getListOfServiceCharges());
        assertTrue(getListOfServiceCharges().equals(serviceChargeService
                .pushCreateToQueue(getServiceChargeRequest())));
    }

    private ServiceChargeReq getServiceChargeRequestAfterCodeAppend() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final ServiceChargeDetails details1 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(100000.0)
                .amountOrpercentage(200.0).tenantId("default").build();

        final ServiceChargeDetails details2 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(1000.0)
                .amountOrpercentage(100.0).tenantId("default").build();

        final ServiceChargeDetails details3 = ServiceChargeDetails.builder().uomFrom(1001.0).uomTo(2000.0)
                .amountOrpercentage(200.0).tenantId("default").build();

        final ServiceChargeDetails details4 = ServiceChargeDetails.builder().uomFrom(2001.0).uomTo(3000.0)
                .amountOrpercentage(300.0).tenantId("default").build();

        final ServiceChargeDetails details5 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(100000.0)
                .amountOrpercentage(12.5).tenantId("default").build();

        final ServiceChargeDetails details6 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(10000.0)
                .amountOrpercentage(2.5).tenantId("default").build();

        final ServiceChargeDetails details7 = ServiceChargeDetails.builder().uomFrom(10001.0).uomTo(20000.0)
                .amountOrpercentage(4.0).tenantId("default").build();

        final ServiceChargeDetails details8 = ServiceChargeDetails.builder().uomFrom(20001.0).uomTo(30000.0)
                .amountOrpercentage(6.5).tenantId("default").build();

        final ServiceCharge charge1 = ServiceCharge.builder().code("1").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("flat").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details1)).build();

        final ServiceCharge charge2 = ServiceCharge.builder().code("2").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("slab").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details2, details3, details4))
                .build();

        final ServiceCharge charge3 = ServiceCharge.builder().code("3").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("percentage flat").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details5)).build();

        final ServiceCharge charge4 = ServiceCharge.builder().code("4").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("percentage slab").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details6,
                        details7, details8))
                .build();

        return ServiceChargeReq.builder().requestInfo(requestInfo).serviceCharge(Arrays.asList(charge1, charge2,
                charge3, charge4)).build();
    }

    @Test
    public void test_should_push_update_serviceCharge_to_queue() {
        when(serviceChargeRepository.pushUpdateToQueue(getServiceChargeRequest()))
                .thenReturn(getListOfServiceCharges());
        assertTrue(getListOfServiceCharges()
                .equals(serviceChargeService.pushUpdateToQueue(getServiceChargeRequest())));
    }

    @Test
    public void test_should_create_serviceCharge() {
        when(serviceChargeRepository.create(getServiceChargeRequest()))
                .thenReturn(getServiceChargeRequest());
        assertTrue(getServiceChargeRequest().equals(serviceChargeService.create(getServiceChargeRequest())));
    }

    @Test
    public void test_should_update_serviceCharge() {
        when(serviceChargeRepository.update(getServiceChargeRequest()))
                .thenReturn(getServiceChargeRequest());
        assertTrue(getServiceChargeRequest().equals(serviceChargeService.update(getServiceChargeRequest())));
    }

    @Test
    public void test_should_search_serviceCharge_by_criteria() {
        when(serviceChargeRepository.searchServiceChargesByCriteria(getServiceChargeGetCriteria()))
                .thenReturn(getListOfServiceChargesForSearch());
        assertTrue(getListOfServiceChargesForSearch()
                .equals(serviceChargeService.getServiceChargesByCriteria(getServiceChargeGetCriteria())));
    }

    private List<ServiceCharge> getListOfServiceChargesForSearch() {
        final ServiceChargeDetails details1 = ServiceChargeDetails.builder().id(13L).code("13").uomFrom(0.0).uomTo(100000.0)
                .amountOrpercentage(12.5).serviceCharge(3L).tenantId("default").build();

        final ServiceChargeDetails details2 = ServiceChargeDetails.builder().id(10L).code("10").uomFrom(0.0).uomTo(1000.0)
                .amountOrpercentage(100.0).serviceCharge(2L).tenantId("default").build();

        final ServiceChargeDetails details3 = ServiceChargeDetails.builder().id(11L).code("11").uomFrom(1001.0).uomTo(2000.0)
                .amountOrpercentage(200.0).serviceCharge(2L).tenantId("default").build();

        final ServiceChargeDetails details4 = ServiceChargeDetails.builder().id(12L).code("12").uomFrom(2001.0).uomTo(3000.0)
                .amountOrpercentage(300.0).serviceCharge(2L).tenantId("default").build();
        final ServiceCharge charge1 = ServiceCharge.builder().id(2L).code("2").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("slab").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details2, details3, details4))
                .build();
        final ServiceCharge charge2 = ServiceCharge.builder().id(3L).code("3").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("percentage flat").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details1)).build();
        return Arrays.asList(charge1, charge2);
    }

    private ServiceChargeGetRequest getServiceChargeGetCriteria() {
        return ServiceChargeGetRequest.builder().ids(Arrays.asList(2L, 3L)).outsideUlb(false).serviceType("No due Certificate")
                .tenantId("default").sortBy("serviceChargeType").sortOrder("desc").build();
    }

    private List<ServiceCharge> getListOfServiceCharges() {
        final ServiceChargeDetails details1 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(100000.0)
                .amountOrpercentage(200.0).tenantId("default").build();

        final ServiceChargeDetails details2 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(1000.0)
                .amountOrpercentage(100.0).tenantId("default").build();

        final ServiceChargeDetails details3 = ServiceChargeDetails.builder().uomFrom(1001.0).uomTo(2000.0)
                .amountOrpercentage(200.0).tenantId("default").build();

        final ServiceChargeDetails details4 = ServiceChargeDetails.builder().uomFrom(2001.0).uomTo(3000.0)
                .amountOrpercentage(300.0).tenantId("default").build();

        final ServiceChargeDetails details5 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(100000.0)
                .amountOrpercentage(12.5).tenantId("default").build();

        final ServiceChargeDetails details6 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(10000.0)
                .amountOrpercentage(2.5).tenantId("default").build();

        final ServiceChargeDetails details7 = ServiceChargeDetails.builder().uomFrom(10001.0).uomTo(20000.0)
                .amountOrpercentage(4.0).tenantId("default").build();

        final ServiceChargeDetails details8 = ServiceChargeDetails.builder().uomFrom(20001.0).uomTo(30000.0)
                .amountOrpercentage(6.5).tenantId("default").build();

        final ServiceCharge charge1 = ServiceCharge.builder().code("1").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("flat").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details1)).build();

        final ServiceCharge charge2 = ServiceCharge.builder().code("2").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("slab").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details2, details3, details4))
                .build();

        final ServiceCharge charge3 = ServiceCharge.builder().code("3").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("percentage flat").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details5)).build();

        final ServiceCharge charge4 = ServiceCharge.builder().code("4").serviceType("No due Certificate")
                .serviceChargeApplicable(true)
                .serviceChargeType("percentage slab").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details6,
                        details7, details8))
                .build();
        return Arrays.asList(charge1, charge2, charge3, charge4);
    }

    public ServiceChargeReq getServiceChargeRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final ServiceChargeDetails details1 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(100000.0)
                .amountOrpercentage(200.0).tenantId("default").build();

        final ServiceChargeDetails details2 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(1000.0)
                .amountOrpercentage(100.0).tenantId("default").build();

        final ServiceChargeDetails details3 = ServiceChargeDetails.builder().uomFrom(1001.0).uomTo(2000.0)
                .amountOrpercentage(200.0).tenantId("default").build();

        final ServiceChargeDetails details4 = ServiceChargeDetails.builder().uomFrom(2001.0).uomTo(3000.0)
                .amountOrpercentage(300.0).tenantId("default").build();

        final ServiceChargeDetails details5 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(100000.0)
                .amountOrpercentage(12.5).tenantId("default").build();

        final ServiceChargeDetails details6 = ServiceChargeDetails.builder().uomFrom(0.0).uomTo(10000.0)
                .amountOrpercentage(2.5).tenantId("default").build();

        final ServiceChargeDetails details7 = ServiceChargeDetails.builder().uomFrom(10001.0).uomTo(20000.0)
                .amountOrpercentage(4.0).tenantId("default").build();

        final ServiceChargeDetails details8 = ServiceChargeDetails.builder().uomFrom(20001.0).uomTo(30000.0)
                .amountOrpercentage(6.5).tenantId("default").build();

        final ServiceCharge charge1 = ServiceCharge.builder().serviceType("No due Certificate").serviceChargeApplicable(true)
                .serviceChargeType("flat").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details1)).build();

        final ServiceCharge charge2 = ServiceCharge.builder().serviceType("No due Certificate").serviceChargeApplicable(true)
                .serviceChargeType("slab").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details2, details3, details4))
                .build();

        final ServiceCharge charge3 = ServiceCharge.builder().serviceType("No due Certificate").serviceChargeApplicable(true)
                .serviceChargeType("percentage flat").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details5)).build();

        final ServiceCharge charge4 = ServiceCharge.builder().serviceType("No due Certificate").serviceChargeApplicable(true)
                .serviceChargeType("percentage slab").description("no due certificate issued").effectiveFrom(15679009L)
                .effectiveTo(15679123L).active(true).outsideUlb(false).tenantId("default").createdBy(1L).createdDate(15679009L)
                .lastModifiedBy(1L).lastModifiedDate(15679009L).chargeDetails(Arrays.asList(details6,
                        details7, details8))
                .build();

        return ServiceChargeReq.builder().requestInfo(requestInfo).serviceCharge(Arrays.asList(charge1, charge2,
                charge3, charge4)).build();

    }

}
