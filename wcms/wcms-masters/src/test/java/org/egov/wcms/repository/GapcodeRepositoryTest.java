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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.repository.builder.GapcodeQueryBuilder;
import org.egov.wcms.repository.rowmapper.GapcodeRowMapper;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(GapcodeRepository.class)
public class GapcodeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private GapcodeQueryBuilder gapcodeQueryBuilder;

    @Mock
    private GapcodeRowMapper gapcodeRowMapper;

    @InjectMocks
    private GapcodeRepository gapcodeRepository;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void test_Should_Create_Gapcode() {
        final GapcodeRequest gapcodeRequest = new GapcodeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        gapcodeRequest.setRequestInfo(requestInfo);
        final List<Gapcode> gapcodeList = new ArrayList<>();
        final Gapcode gapcode = getCategory();
        gapcodeList.add(gapcode);
        when(jdbcTemplate.update(any(String.class), any(Object[].class)))
                .thenReturn(1);
        assertTrue(gapcodeRequest.equals(gapcodeRepository
                .create(gapcodeRequest)));
    }

    @Test
    public void test_Should_Update_Gapcode() {
        final GapcodeRequest gapcodeRequest = new GapcodeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        gapcodeRequest.setRequestInfo(requestInfo);
        final List<Gapcode> gapcodeList = new ArrayList<>();
        final Gapcode gapcode = getCategory();
        gapcodeList.add(gapcode);
        when(jdbcTemplate.update(any(String.class), any(Object[].class)))
                .thenReturn(1);
        assertTrue(gapcodeRequest.equals(gapcodeRepository
                .update(gapcodeRequest)));
    }

    @Test
    public void test_Should_Find_CategoryType_Valid() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final GapcodeGetRequest gapcodeGetRequest = Mockito
                .mock(GapcodeGetRequest.class);
        final String queryString = "MyQuery";
        when(
                gapcodeQueryBuilder.getQuery(gapcodeGetRequest,
                        preparedStatementValues)).thenReturn(queryString);
        final List<Gapcode> connectionCategories = new ArrayList<>();
        when(
                jdbcTemplate.query(queryString,
                        preparedStatementValues.toArray(), gapcodeRowMapper))
                                .thenReturn(connectionCategories);

        assertTrue(connectionCategories.equals(gapcodeRepository
                .findForCriteria(gapcodeGetRequest)));
    }

    private Gapcode getCategory() {
        final Gapcode gapcode = new Gapcode();
        gapcode.setCode("23");
        gapcode.setName("New Gapcode");
        gapcode.setActive(true);
        gapcode.setOutSideUlb(true);
        gapcode.setLogic("Average");
        gapcode.setNoOfMonths("Last 3 months Average");
        gapcode.setDescription("New Gaocode of Connection");
        return gapcode;
    }
}
