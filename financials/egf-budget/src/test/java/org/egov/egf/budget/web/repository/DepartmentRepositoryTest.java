/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.web.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.egov.egf.budget.domain.service.DateFactory;
import org.egov.egf.budget.utils.RequestJsonReader;
import org.egov.egf.budget.web.contract.DepartmentRes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentRepositoryTest {

    private static final String HOST = "http://host";

    private static final String DEPT_BY_ID_URL = "/departments/findById";

    private MockRestServiceServer server;

    private DepartmentRepository departmentRepository;

    @Mock
    private DateFactory dateFactory;

    private final RequestJsonReader resources = new RequestJsonReader();

    @Before
    public void setup() {
        final RestTemplate restTemplate = new RestTemplate();
        departmentRepository = new DepartmentRepository(restTemplate, HOST, DEPT_BY_ID_URL, dateFactory);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_get_by_id() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

        final Date now = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2017 00:00:00");
        server.expect(once(), requestTo("http://host/departments/findById")).andExpect(method(HttpMethod.POST))
                .andExpect(content().string(resources.getFileContents("department/search_dept_by_id_request.json")))
                .andRespond(withSuccess(resources.getFileContents("department/search_dept_by_id.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        when(dateFactory.create()).thenReturn(now);

        final DepartmentRes response = departmentRepository.getDepartmentById("departmentId", "tenantId");
        server.verify();

        assertEquals(1, response.getDepartment().size());

    }
}
