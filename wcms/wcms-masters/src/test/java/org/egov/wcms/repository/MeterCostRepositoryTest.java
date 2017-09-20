/*

 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 
package org.egov.wcms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.MeterCost;
import org.egov.wcms.repository.builder.MeterCostQueryBuilder;
import org.egov.wcms.repository.rowmapper.MeterCostRowMapper;
import org.egov.wcms.service.CodeGeneratorService;
import org.egov.wcms.web.contract.MeterCostGetRequest;
import org.egov.wcms.web.contract.MeterCostReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MeterCostRepositoryTest {
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private MeterCostQueryBuilder meterCostQueryBuilder;

    @Mock
    private MeterCostRowMapper meterCostRowMapper;

    @Mock
    private CodeGeneratorService CodeGeneratorService;

    @InjectMocks
    private MeterCostRepository meterCostRepository;

    @Test
    public void test_should_push_create_meterCostRequest_to_Queue() {
        when(CodeGeneratorService.generate("SEQ_EGWTR_METER_COST")).thenReturn("1", "2");
        when(applicationProperties.getCreateMeterCostTopicName()).thenReturn("egov.wcms.metercost-create");
        meterCostRepository.pushCreateMeterCostReqToQueue(getMeterCostRequest());
        verify(kafkaTemplate).send("egov.wcms.metercost-create", getMeterCostRequest());
    }

    @Test
    public void test_should_push_update_meterCostRequest_to_Queue() {
        when(applicationProperties.getUpdateMeterCostTopicName()).thenReturn("egov.wcms.metercost-update");
        meterCostRepository.pushUpdateMeterCostReqToQueue(getMeterCostRequestForUpdate());
        verify(kafkaTemplate).send("egov.wcms.metercost-update", getMeterCostRequestForUpdate());
    }

    @Test
    public void test_should_persist_create_meterCost_to_DB() {
        final MeterCostReq meterCostRequest = meterCostRepository.persistCreateMeterCost(getMeterCostRequest());
        assertThat(meterCostRequest.getMeterCost().size()).isEqualTo(2);
    }

    @Test
    public void test_should_update_meterCost_to_DB() {
        final MeterCostReq meterCostRequest = meterCostRepository.persistUpdateMeterCost(getMeterCostRequestForUpdate());
        assertThat(meterCostRequest.getMeterCost().get(0).getMeterMake()).isEqualTo("meterMakeUpdated1");
        assertThat(meterCostRequest.getMeterCost().get(1).getMeterMake()).isEqualTo("meterMakeUpdated2");
        assertThat(meterCostRequest.getMeterCost().get(0).getAmount()).isEqualTo(3000);
        assertThat(meterCostRequest.getMeterCost().get(1).getAmount()).isEqualTo(4000);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_should_search_meterCost_from_DB() {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        when(meterCostQueryBuilder.getQuery(getMeterCostGetRequest(), preparedStatementValues))
                .thenReturn("Select wmc.id as wmc_id,wmc.code as wmc_code,"
                        + "wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
                        + "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
                        + "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
                        + "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc WHERE wmc.tenantId = ? AND "
                        + "wmc.active = ? AND  wmc.id IN (1, 2)  ORDER BY code desc");
        when(namedParameterJdbcTemplate.query(any(String.class), anyMap(), any(MeterCostRowMapper.class)))
                .thenReturn(getListOfMeterCosts());
        assertTrue(
                getListOfMeterCosts().equals(meterCostRepository.searchMeterCostByCriteria(getMeterCostGetRequest())));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_should_return_false_if__meter_make_exists_in_DB_and_code_is_null() {
        when(meterCostQueryBuilder.selectMeterCostByNameAndTenantIdQuery())
                .thenReturn("Select wmc.id as wmc_id,wmc.code as wmc_code,"
                        + "wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
                        + "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
                        + "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
                        + "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc where wmc.metermake = :name and wmc.tenantId = :tenantId");
        when(namedParameterJdbcTemplate.query(any(String.class), anyMap(), any(MeterCostRowMapper.class)))
                .thenReturn(getListOfMeterCostFromDB());
        assertTrue(meterCostRepository.checkMeterMakeAndAmountAlreadyExistsInDB(getMeterCost()).equals(false));
    }

    private List<MeterCost> getListOfMeterCostFromDB() {
        return Arrays.asList(MeterCost.builder().id(2L).meterMake("MeterMake").active(true).amount(3000.0).createdBy(2L)
                .lastModifiedBy(2L).tenantId("default").build());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_should_return_true_if__meter_make_doesnot_exists_in_DB_and_code_is_null() {
        final List<MeterCost> listOfMeterCosts = new ArrayList<>();
        when(meterCostQueryBuilder.selectMeterCostByNameAndTenantIdQuery())
                .thenReturn("Select wmc.id as wmc_id,wmc.code as wmc_code,"
                        + "wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
                        + "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
                        + "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
                        + "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc where wmc.metermake = :name and wmc.tenantId = :tenantId");
        when(namedParameterJdbcTemplate.query(any(String.class), anyMap(), any(MeterCostRowMapper.class)))
                .thenReturn(listOfMeterCosts);
        assertTrue(meterCostRepository.checkMeterMakeAndAmountAlreadyExistsInDB(getMeterCost()).equals(true));
    }

    @Test
    public void test_should_return_false_if__meter_make_exists_in_DB_and_code_is_notnull() {
        when(meterCostQueryBuilder.selectMeterCostByNameTenantIdAndCodeNotInQuery())
                .thenReturn("Select wmc.id as wmc_id,wmc.code as wmc_code,"
                        + "wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
                        + "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
                        + "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
                        + "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc"
                        + " where wmc.metermake = :name and wmc.tenantId = :tenantId and wmc.code != :code");
        when(namedParameterJdbcTemplate.query(any(String.class), anyMap(), any(MeterCostRowMapper.class)))
                .thenReturn(getListOfMeterCostsWhenCodeIsNotNull());
        assertTrue(meterCostRepository.checkMeterMakeAndAmountAlreadyExistsInDB(getMeterCostIfCodeIsNotNull()).equals(false));
    }

    private List<MeterCost> getListOfMeterCostsWhenCodeIsNotNull() {
        return Arrays.asList(MeterCost.builder().id(2L).code("MM").meterMake("MeterMake").active(true).amount(3000.0)
                .createdBy(2L).lastModifiedBy(2L).tenantId("default").build());
    }

    @Test
    public void test_should_return_true_if__meter_make_doesnot_exists_in_DB_and_code_is_notnull() {
        final List<MeterCost> listOfMeterCosts = new ArrayList<>();
        when(meterCostQueryBuilder.selectMeterCostByNameTenantIdAndCodeNotInQuery())
                .thenReturn("Select wmc.id as wmc_id,wmc.code as wmc_code,"
                        + "wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
                        + "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
                        + "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
                        + "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc"
                        + " where wmc.metermake = :name and wmc.tenantId = :tenantId and wmc.code != :code ");
        when(namedParameterJdbcTemplate.query(any(String.class), anyMap(), any(MeterCostRowMapper.class)))
                .thenReturn(listOfMeterCosts);
        assertTrue(meterCostRepository.checkMeterMakeAndAmountAlreadyExistsInDB(getMeterCostIfCodeIsNotNull()).equals(true));
    }

    private MeterCost getMeterCostIfCodeIsNotNull() {
        return MeterCost.builder().id(1L).meterMake("MeterMake").code("MMS").active(true).amount(1000.0).createdBy(1L)
                .lastModifiedBy(1L).tenantId("default").build();
    }

    private MeterCost getMeterCost() {
        return MeterCost.builder().id(1L).meterMake("MeterMake").active(true).amount(1000.0).createdBy(1L)
                .lastModifiedBy(1L).tenantId("default").build();
    }

    private List<MeterCost> getListOfMeterCosts() {
        final MeterCost meterCost1 = MeterCost.builder().id(1L).code("MeterCost123").meterMake("meterMake123")
                .amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().id(2L).code("MeterCost234").meterMake("meterMake234")
                .amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        return Arrays.asList(meterCost1, meterCost2);
    }

    private MeterCostGetRequest getMeterCostGetRequest() {
        return MeterCostGetRequest.builder().active(true).ids(Arrays.asList(1L, 2L)).tenantId("default").sortBy("code")
                .sortOrder("desc").build();
    }

    private MeterCostReq getMeterCostRequestForUpdate() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final MeterCost meterCost1 = MeterCost.builder().id(1L).code("MeterCost123")
                .meterMake("meterMakeUpdated1").amount(3000.0).active(true).createdBy(1L).lastModifiedBy(1L)
                .tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().id(2L).code("MeterCost234")
                .meterMake("meterMakeUpdated2").amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L)
                .tenantId("default").build();
        return MeterCostReq.builder().requestInfo(requestInfo).meterCost(Arrays.asList(meterCost1, meterCost2)).build();

    }

    private MeterCostReq getMeterCostRequest() {
        final User userInfo = User.builder().id(1L).build();
        final RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
                .did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
        final MeterCost meterCost1 = MeterCost.builder().id(1L).code("1").meterMake("meterMake123")
                .amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        final MeterCost meterCost2 = MeterCost.builder().id(2L).code("2").meterMake("meterMake234")
                .amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
        return MeterCostReq.builder().requestInfo(requestInfo).meterCost(Arrays.asList(meterCost1, meterCost2)).build();

    }

}*/