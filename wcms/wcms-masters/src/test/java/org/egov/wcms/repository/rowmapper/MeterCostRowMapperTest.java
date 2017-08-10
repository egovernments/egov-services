package org.egov.wcms.repository.rowmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;

import org.egov.wcms.model.MeterCost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MeterCostRowMapperTest {

	@Mock
	private ResultSet rs;

	@InjectMocks
	private MeterCostRowMapper meterCostRowMapper;

	@Test
	public void test_should_map_result_set_to_entity() throws Exception {
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		when(rs.getLong("wmc_id")).thenReturn(1L);
		when(rs.getString("wmc_code")).thenReturn("WMC");
		when(rs.getString("wmc_metermake")).thenReturn("meterMake");
		when((Boolean) rs.getObject("wmc_active")).thenReturn(true);
		when((Double) rs.getObject("wmc_amount")).thenReturn(4000.0);
		when((Long) rs.getObject("wmc_pipesizeid")).thenReturn(2L);
		when((Long) rs.getObject("wmc_createdby")).thenReturn(1L);
		when((Long) rs.getObject("wmc_lastmodifiedby")).thenReturn(1L);
		when(rs.getString("wmc_tenantid")).thenReturn("default");
		when((Long) rs.getObject("wmc_createddate")).thenReturn(1543678989L);
		when((Long) rs.getObject("wmc_lastmodifieddate")).thenReturn(1543678989L);
		MeterCost actualMeterCost = meterCostRowMapper.mapRow(rs, 1);
		MeterCost expectedMeterCost = getMeterCost();
		assertThat(expectedMeterCost.equals(actualMeterCost));
	}

	private MeterCost getMeterCost() {
		return MeterCost.builder().id(1L).code("WMC").meterMake("meterMake").active(true).amount(4000.0).pipeSizeId(2L)
				.createdBy(1L).lastModifiedBy(1L).tenantId("default").createdDate(1543678989L)
				.lastModifiedDate(1543678989L).build();

	}

}
