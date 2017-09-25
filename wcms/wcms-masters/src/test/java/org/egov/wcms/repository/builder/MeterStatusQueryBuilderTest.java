package org.egov.wcms.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.junit.Test;

public class MeterStatusQueryBuilderTest {
    @Test
    public void no_input_test() {
        final MeterStatusQueryBuilder meterStatusQueryBuilder = new MeterStatusQueryBuilder();
        final MeterStatusGetRequest meterStatusGetRequest = new MeterStatusGetRequest();
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        assertEquals("Select ms.id as ms_id,ms.code as ms_code,"
                + "ms.status as ms_status,ms.active as ms_active,ms.description as ms_description,ms.createdby as"
                + " ms_createdby,ms.createddate as ms_createddate,ms.lastmodifiedby as ms_"
                + "lastmodifiedby,ms.lastmodifieddate as ms_lastmodifieddate,ms.tenantid as"
                + " ms_tenantid FROM egwtr_meterstatus ms ORDER BY ms.id DESC",
                meterStatusQueryBuilder.getQuery(meterStatusGetRequest, preparedStatementValues));
    }

    @Test
    public void all_input_test() {
        final MeterStatusQueryBuilder meterStatusQueryBuilder = new MeterStatusQueryBuilder();
        final MeterStatusGetRequest meterStatusGetRequest = new MeterStatusGetRequest();

        meterStatusGetRequest.setCode("1");
        meterStatusGetRequest.setIds(Arrays.asList(1L));
        meterStatusGetRequest.setMeterStatus("Meter is faulty");
        meterStatusGetRequest.setSortBy("status");
        meterStatusGetRequest.setSortOrder("desc");
        meterStatusGetRequest.setTenantId("default");

        assertEquals("Select ms.id as ms_id,ms.code as ms_code,"
                + "ms.status as ms_status,ms.active as ms_active,ms.description as ms_description,ms.createdby as"
                + " ms_createdby,ms.createddate as ms_createddate,ms.lastmodifiedby as ms_"
                + "lastmodifiedby,ms.lastmodifieddate as ms_lastmodifieddate,ms.tenantid as"
                + " ms_tenantid FROM egwtr_meterstatus ms WHERE ms.tenantid = :tenantId AND "
                + "ms.code = :code AND ms.status = :status AND"
                + " ms.id IN (:ids) ORDER BY ms.status desc",
                meterStatusQueryBuilder.getQuery(meterStatusGetRequest, new HashMap<>()));

    }
}
