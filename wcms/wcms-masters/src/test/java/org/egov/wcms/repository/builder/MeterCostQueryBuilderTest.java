package org.egov.wcms.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.egov.wcms.model.MeterCostCriteria;
import org.junit.Test;

public class MeterCostQueryBuilderTest {

	@Test
	public void no_input_test() {
		MeterCostQueryBuilder meterCostQueryBuilder = new MeterCostQueryBuilder();
		MeterCostCriteria meterCostCriteria = new MeterCostCriteria();
		assertEquals(
				"Select wmc.id as wmc_id,wmc.code as wmc_code,"
						+ "wmc.pipesizeid as wmc_pipesizeid,wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
						+ "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
						+ "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
						+ "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc ORDER BY wmc.metermake ASC",
				meterCostQueryBuilder.getQuery(meterCostCriteria, new ArrayList<>()));
	}

	@Test
	public void all_input_test() {
		MeterCostQueryBuilder meterCostQueryBuilder = new MeterCostQueryBuilder();

		MeterCostCriteria meterCostCriteria = new MeterCostCriteria();
		meterCostCriteria.setActive(true);
		meterCostCriteria.setCode("MC");
		meterCostCriteria.setIds(Arrays.asList(1L, 2L));
		meterCostCriteria.setName("MeterMake");
		meterCostCriteria.setPipeSizeId(1L);
		meterCostCriteria.setSortby("code");
		meterCostCriteria.setSortOrder("desc");
		meterCostCriteria.setTenantId("default");

		assertEquals(
				"Select wmc.id as wmc_id,wmc.code as wmc_code,"
						+ "wmc.pipesizeid as wmc_pipesizeid,wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
						+ "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
						+ "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
						+ "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc WHERE wmc.tenantId = ? AND "
						+ "wmc.code = ? AND wmc.metermake = ? AND wmc.active = ? AND wmc.pipesizeid = ? AND"
						+ " wmc.id IN (1, 2) ORDER BY wmc.code desc",
				meterCostQueryBuilder.getQuery(meterCostCriteria, new ArrayList<>()));

	}
}
