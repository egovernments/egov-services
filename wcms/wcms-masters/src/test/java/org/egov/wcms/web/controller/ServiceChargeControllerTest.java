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
package org.egov.wcms.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.model.ServiceCharge;
import org.egov.wcms.model.ServiceChargeDetails;
import org.egov.wcms.service.ServiceChargeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.ServiceChargeGetRequest;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceChargeController.class)
@Import(TestConfiguration.class)
public class ServiceChargeControllerTest {
    @MockBean
    private ValidatorUtils validatorUtils;

    @MockBean
    private ServiceChargeService serviceChargeService;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ErrorHandler errHandler;

    @InjectMocks
    private ServiceChargeController serviceChargeController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_should_create_ServiceCharge() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(validatorUtils.validateServiceChargeRequest(getServiceChargeRequest(), false)).thenReturn(errorResponses);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(serviceChargeService.pushCreateToQueue(getServiceChargeRequest()))
                .thenReturn(getListOfServiceCharges());
        mockMvc.perform(post("/serviceCharges/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("ServiceChargeRequestCreate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("ServiceChargeResponseCreate.json")));
    }

    @Test
    public void test_should_update_ServiceCharge() throws Exception {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        when(validatorUtils.validateServiceChargeRequest(getServiceChargeRequest(), true)).thenReturn(errorResponses);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(serviceChargeService.pushUpdateToQueue(getServiceChargeRequest()))
                .thenReturn(getListOfServiceCharges());
        mockMvc.perform(post("/serviceCharges/_update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("ServiceChargeRequestUpdate.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("ServiceChargeResponseUpdate.json")));
    }

    @Test
    public void test_should_search_serviceCharge() throws Exception {
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getSuccessRequestInfo());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
                .thenReturn(getFailureRequestInfo());
        when(serviceChargeService.getServiceChargesByCriteria(getServiceChargeGetCriteria()))
                .thenReturn(getListOfServiceChargesForSearch());
        mockMvc.perform(post("/serviceCharges/_search?ids=2,3&"
                + "serviceType=No due Certificate&outsideUlb=false&"
                + "tenantId=default&sortBy=serviceChargeType&sortOrder=desc")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(getFileContents("ServiceChargeRequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("ServiceChargeResponse.json")));
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
        return Arrays.asList(charge1, charge2, charge3, charge4);
    }

    private ResponseInfo getFailureRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("failed").build();
    }

    private ResponseInfo getSuccessRequestInfo() {
        return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("successful").build();
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

    private String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
