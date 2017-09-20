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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.UsageType;
import org.egov.wcms.repository.builder.UsageTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.UsageTypeRowMapperTest;
import org.egov.wcms.web.contract.UsageTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(UsageTypeRepository.class)
public class UsageTypeRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private UsageTypeQueryBuilder usageTypeQueryBuilder;

    @Mock
    private UsageTypeRowMapperTest usageTypeRowMapper;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private CodeSequenceNumberGenerator codeSequenceNumberGenerator;

    @InjectMocks
    private UsageTypeRepository usageTypeRepository;

    @Test
    public void test_should_push_create_usageType_to_queue() {
        when(codeSequenceNumberGenerator.getNextSequence("SEQ_EGWTR_USAGE_TYPE")).thenReturn("1", "2");
        when(applicationProperties.getCreateUsageTypeTopicName()).thenReturn("egov.wcms.usagetype-create");
        usageTypeRepository.pushCreateToQueue(getUsageTypeRequest());
        verify(kafkaTemplate).send("egov.wcms.usagetype-create", getUsageTypeRequest());
    }

    @Test
    public void test_should_push_update_usageType_to_queue() {
        when(codeSequenceNumberGenerator.getNextSequence("SEQ_EGWTR_USAGE_TYPE")).thenReturn("1", "2");
        when(applicationProperties.getUpdateUsageTypeTopicName()).thenReturn("egov.wcms.usagetype-update");
        usageTypeRepository.pushUpdateToQueue(getUsageTypeRequestForUpdate());
        verify(kafkaTemplate).send("egov.wcms.usagetype-update", getUsageTypeRequestForUpdate());
    }

    @Test
    public void test_should_persist_create_usageType_to_DB() {
        final UsageTypeReq usageTypeReq = usageTypeRepository.create(getUsageTypeRequest());
        assertThat(usageTypeReq.getUsageTypes().size()).isEqualTo(2);
    }

    @Test
    public void test_should_persist_update_usageType_to_DB() {
        final UsageTypeReq usageTypeReq = usageTypeRepository.update(getUsageTypeRequestForUpdate());
        assertThat(usageTypeReq.getUsageTypes().size()).isEqualTo(2);
    }
    /*
     * @Test public void test_should_verify_usageType_exists_in_DB_and_returns_false_if_it_exists_in_case_of_create() { final
     * Map<String, Object> preparedStatementValues = new HashMap<>(); preparedStatementValues.put("name", "School");
     * preparedStatementValues.put("tenantId", "default"); when(usageTypeQueryBuilder.getUsageTypeIdQuery()).thenReturn(
     * "select id FROM egwtr_usage_type" + "  where name= :name and tenantId = :tenantId ");
     * when(namedParameterJdbcTemplate.queryForList("select id FROM egwtr_usage_type" +
     * "  where name= :name and tenantId = :tenantId ", preparedStatementValues, Long.class)) .thenReturn(Arrays.asList(1L));
     * assertThat(usageTypeRepository.checkUsageTypeExists(getUsageTypeCreate())).isEqualTo(false); }
     */

    /*
     * @Test public void test_should_verify_usageType_exists_in_DB_and_returns_true_if_it_exists_in_case_of_update() { final
     * Map<String, Object> preparedStatementValues = new HashMap<>(); preparedStatementValues.put("name", "School");
     * preparedStatementValues.put("tenantId", "default"); preparedStatementValues.put("code", "2"); final List<Long> usageTypeids
     * = new ArrayList<>(); when(usageTypeQueryBuilder.getUsageTypeIdQueryWithCode()) .thenReturn(
     * "select id FROM egwtr_usage_type  where name= :name and tenantId = :tenantId" + " and code != :code");
     * when(namedParameterJdbcTemplate.queryForList("select id FROM egwtr_usage_type  where name= :name and tenantId = :tenantId"
     * + " and code != :code ", preparedStatementValues, Long.class)).thenReturn(usageTypeids);
     * assertThat(usageTypeRepository.checkUsageTypeExists(getUsageTypeUpdate())).isEqualTo(true); }
     */

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
        final UsageType usageType = UsageType.builder().code("1").name("Trust").active(true)
                .description("water required for trust")
                .tenantId("default").createdBy(1L).createdDate(12323321L)
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final UsageType subusageType = UsageType.builder().code("2").name("School").active(true)
                .description("water required for school")
                .tenantId("default").createdBy(1L).createdDate(12323321L).parent("1")
                .lastModifiedBy(1L).lastModifiedDate(12323321L).build();
        final List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(usageType);
        usageTypes.add(subusageType);
        return UsageTypeReq.builder().requestInfo(requestInfo).usageTypes(usageTypes).build();
    }

}
