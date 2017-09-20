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

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.UsageType;
import org.egov.wcms.repository.UsageTypeRepository;
import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.egov.wcms.web.contract.UsageTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(UsageTypeService.class)
public class UsageTypeServiceTest {
    @Mock
    private UsageTypeRepository usageTypeRepository;

    @InjectMocks
    private UsageTypeService usageTypeService;

    @Test
    public void test_should_push_usageType_create_request_to_queue() {
        when(usageTypeRepository.pushCreateToQueue(getUsageTypeRequest()))
                .thenReturn(getUsageTypeRequest().getUsageTypes());
        final List<UsageType> usageTypes = usageTypeService.pushCreateToQueue(getUsageTypeRequest());
        assertTrue(getUsageTypeRequest().getUsageTypes().equals(usageTypes));
    }

    @Test
    public void test_should_push_usageType_update_request_to_queue() {
        when(usageTypeRepository.pushUpdateToQueue(getUsageTypeRequestForUpdate()))
                .thenReturn(getUsageTypeRequestForUpdate().getUsageTypes());
        final List<UsageType> usageTypes = usageTypeService.pushUpdateToQueue(getUsageTypeRequestForUpdate());
        assertTrue(getUsageTypeRequestForUpdate().getUsageTypes().equals(usageTypes));
    }

    @Test
    public void test_should_persist_create_usageType_request_to_DB() {
        when(usageTypeRepository.create(getUsageTypeRequest())).thenReturn(getUsageTypeRequest());
        final UsageTypeReq usageTypeRequest = usageTypeService.create(getUsageTypeRequest());
        assertTrue(getUsageTypeRequest().equals(usageTypeRequest));
    }

    @Test
    public void test_should_update_usageType_request_in_DB() {
        when(usageTypeRepository.update(getUsageTypeRequestForUpdate()))
                .thenReturn(getUsageTypeRequestForUpdate());
        final UsageTypeReq usageTypeRequest = usageTypeService.update(getUsageTypeRequestForUpdate());
        assertTrue(getUsageTypeRequestForUpdate().equals(usageTypeRequest));
    }

    @Test
    public void test_should_search_usageType_from_DB() {
        when(usageTypeRepository.getUsageTypesByCriteria(getUsageTypeGetRequest())).thenReturn(getUsageType());
        final List<UsageType> usageTypes = usageTypeService.getUsageTypes(getUsageTypeGetRequest());
        assertTrue(getUsageType().equals(usageTypes));
    }

    /*
     * @Test public void test_should_verify_usageType_exists_in_DB_and_return_false_if_it_exists() {
     * when(usageTypeRepository.checkUsageTypeExists(getUsageType().get(0))).thenReturn(false); final Boolean value =
     * usageTypeService.checkUsageTypeExists(getUsageType().get(0)); assertTrue(value.equals(false)); }
     * @Test public void test_should_verify_usageType_exists_in_DB_and_return_true_if_it_doesnot_exists() {
     * when(usageTypeRepository.checkUsageTypeExists(getUsageType().get(0))).thenReturn(true); final Boolean value =
     * usageTypeService.checkUsageTypeExists(getUsageType().get(0)); assertTrue(value.equals(true)); }
     */

    private List<UsageType> getUsageType() {
        final List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(UsageType.builder().id(2L).code("2").name("School").active(true).description("water required for school")
                .tenantId("default").createdBy(1L).createdDate(12323321L).parent("1")
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build());

        return usageTypes;
    }

    private UsageTypeGetRequest getUsageTypeGetRequest() {
        return UsageTypeGetRequest.builder().parent("1").tenantId("default").name("School").build();
    }

    private UsageTypeReq getUsageTypeRequestForUpdate() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final UsageType usageType = UsageType.builder().name("Commercial").active(true)
                .description("water required for commercial purposes")
                .tenantId("default").createdBy(1L).createdDate(12323321L)
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final UsageType subusageType = UsageType.builder().name("Industrial").active(true)
                .description("water required for industrial purposes")
                .tenantId("default").createdBy(1L).createdDate(12323321L).parent("1")
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(usageType);
        usageTypes.add(subusageType);
        return UsageTypeReq.builder().requestInfo(requestInfo).usageTypes(usageTypes).build();
    }

    private UsageTypeReq getUsageTypeRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final UsageType usageType = UsageType.builder().name("Trust").active(true).description("water required for trust")
                .tenantId("default").createdBy(1L).createdDate(12323321L)
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final UsageType subusageType = UsageType.builder().name("School").active(true).description("water required for school")
                .tenantId("default").createdBy(1L).createdDate(12323321L).parent("1")
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(usageType);
        usageTypes.add(subusageType);
        return UsageTypeReq.builder().requestInfo(requestInfo).usageTypes(usageTypes).build();
    }

}
