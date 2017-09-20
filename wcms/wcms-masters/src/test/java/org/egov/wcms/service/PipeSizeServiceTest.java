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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.repository.PipeSizeRepository;
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PipeSizeService.class)
@WebAppConfiguration
public class PipeSizeServiceTest {

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private PipeSizeRepository pipeSizeRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @InjectMocks
    private PipeSizeService pipeSizeService;

    @Test
    public void test_Should_Create_PipeSize() throws Exception {
        final PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final List<PipeSize> pipeSizeList = new ArrayList<>();
        pipeSizeList.add(getPipeSize());
        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSizes(pipeSizeList);

        final List<PipeSize> pipeSizeResult = pipeSizeService.pushCreateToQueue("topic", "key", pipeSizeRequest);

        assertNotNull(pipeSizeResult);
    }

    /*
     * @Test(expected = Exception.class) public void test_throwException_Create_PipeSize() throws Exception{ PipeSizeRequest
     * pipeSizeRequest = new PipeSizeRequest(); RequestInfo requestInfo = new RequestInfo(); PipeSize pipeSize = new PipeSize();
     * pipeSize.setCode("10"); pipeSize.setActive(true); pipeSize.setCreatedBy(1L); pipeSizeRequest.setRequestInfo(requestInfo);
     * pipeSizeRequest.setPipeSize(pipeSize); when(pipeSizeRequest.getPipeSize()).thenThrow(Exception.class); PipeSize
     * pipeSizeResult = pipeSizeService.createPipeSize("topic", "key", pipeSizeRequest); assertNotNull(pipeSizeResult); }
     */

    @Test
    public void test_Should_Update_PipeSize() throws Exception {
        final PipeSizeRequest pipeSizeRequest = new PipeSizeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final List<PipeSize> pipeSizeList = new ArrayList<>();
        pipeSizeList.add(getPipeSize());

        pipeSizeRequest.setRequestInfo(requestInfo);
        pipeSizeRequest.setPipeSizes(pipeSizeList);

        final List<PipeSize> pipeSizeResult = pipeSizeService.pushUpdateToQueue("topic", "key", pipeSizeRequest);

        assertNotNull(pipeSizeResult);
    }

    @Test
    public void test_Should_Get_PipeSize() throws Exception {

        when(pipeSizeRepository.checkPipeSizeInmmAndCode("code", 1.2, "tenantId")).thenReturn(false);

        final boolean result = pipeSizeService.getPipeSizeInmmAndCode("code", 1.2, "tenantId");

        assertTrue(false == result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Get_PipeSize() throws Exception {

        when(pipeSizeRepository.checkPipeSizeInmmAndCode("code", 1.2, "tenantId")).thenThrow(Exception.class);

        final boolean result = pipeSizeService.getPipeSizeInmmAndCode("code", 1.2, "tenantId");

        assertTrue(false == result);
    }

    @Test
    public void test_Should_Get_PipeSizes() throws Exception {

        final PipeSizeGetRequest pipeSizeGetRequest = new PipeSizeGetRequest();
        final PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);

        final List<PipeSize> pipeSizes = new ArrayList<>();
        List<PipeSize> pipeSizesResult = new ArrayList<>();
        pipeSizes.add(pipeSize);
        when(pipeSizeRepository.findForCriteria(pipeSizeGetRequest)).thenReturn(pipeSizes);
        pipeSizesResult = pipeSizeService.getPipeSizes(pipeSizeGetRequest);
        assertTrue(null != pipeSizesResult);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Get_PipeSizes() throws Exception {

        final PipeSizeGetRequest pipeSizeGetRequest = new PipeSizeGetRequest();
        final PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);

        final List<PipeSize> pipeSizes = new ArrayList<>();
        List<PipeSize> pipeSizesResult = new ArrayList<>();
        pipeSizes.add(pipeSize);
        when(pipeSizeRepository.findForCriteria(pipeSizeGetRequest)).thenThrow(Exception.class);
        pipeSizesResult = pipeSizeService.getPipeSizes(pipeSizeGetRequest);
        assertTrue(null != pipeSizesResult);
    }

    private PipeSize getPipeSize() {
        final PipeSize pipeSize = new PipeSize();
        pipeSize.setCode("10");
        pipeSize.setActive(true);
        pipeSize.setSizeInInch(1.2);
        pipeSize.setSizeInMilimeter(10.1);
        return pipeSize;
    }

    /*
     * @Test(expected = Exception.class) public void test_throwException_Update_PipeSize() throws Exception{ PipeSizeRequest
     * pipeSizeRequest = new PipeSizeRequest(); RequestInfo requestInfo = new RequestInfo(); PipeSize pipeSize = new PipeSize();
     * pipeSize.setCode("10"); pipeSize.setActive(true); pipeSize.setCreatedBy(1L); pipeSizeRequest.setRequestInfo(requestInfo);
     * pipeSizeRequest.setPipeSize(pipeSize); when(pipeSizeRequest.getPipeSize()).thenThrow(Exception.class); PipeSize
     * pipeSizeResult = pipeSizeService.updatePipeSize("topic", "key", pipeSizeRequest); assertNotNull(pipeSizeResult); }
     */

}
