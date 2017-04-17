package org.egov.tenant.persistence.rowmapper;

import org.egov.tenant.persistence.entity.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityRowMapperTest {

    @Mock
    ResultSet resultSet;

    @Test
    public void test_should_map_result_set_to_entity() throws Exception {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        when(resultSet.getLong(City.Fields.ID.getValue())).thenReturn(1L);
        when(resultSet.getString(City.Fields.NAME.getValue())).thenReturn("Bengaluru");
        when(resultSet.getString(City.Fields.LOCAL_NAME.getValue())).thenReturn("local name");
        when(resultSet.getString(City.Fields.DISTRICT_CODE.getValue())).thenReturn("district code");
        when(resultSet.getString(City.Fields.DISTRICT_NAME.getValue())).thenReturn("district name");
        when(resultSet.getString(City.Fields.REGION_NAME.getValue())).thenReturn("region name");
        when(resultSet.getDouble(City.Fields.LONGITUDE.getValue())).thenReturn(35.234);
        when(resultSet.getDouble(City.Fields.LATITUDE.getValue())).thenReturn(75.234);
        when(resultSet.getString(City.Fields.TENANT_CODE.getValue())).thenReturn("AP.GUNTOOR");
        when(resultSet.getTimestamp(City.Fields.CREATED_DATE.getValue())).thenReturn(timestamp);
        when(resultSet.getLong(City.Fields.CREATED_BY.getValue())).thenReturn(1L);
        when(resultSet.getTimestamp(City.Fields.LAST_MODIFIED_DATE.getValue())).thenReturn(timestamp);
        when(resultSet.getLong(City.Fields.LAST_MODIFIED_BY.getValue())).thenReturn(1L);

        CityRowMapper cityRowMapper = new CityRowMapper();

        City city = cityRowMapper.mapRow(resultSet, 1);

        assertThat(city.getId()).isEqualTo(1);
        assertThat(city.getName()).isEqualTo("Bengaluru");
        assertThat(city.getLocalName()).isEqualTo("local name");
        assertThat(city.getDistrictCode()).isEqualTo("district code");
        assertThat(city.getDistrictName()).isEqualTo("district name");
        assertThat(city.getRegionName()).isEqualTo("region name");
        assertThat(city.getLongitude()).isEqualTo(35.234);
        assertThat(city.getLatitude()).isEqualTo(75.234);
        assertThat(city.getTenantCode()).isEqualTo("AP.GUNTOOR");
        assertThat(city.getCreatedDate()).isInSameSecondAs(date);
        assertThat(city.getCreatedBy()).isEqualTo(1L);
        assertThat(city.getLastModifiedDate()).isInSameSecondAs(date);
        assertThat(city.getLastModifiedBy()).isEqualTo(1L);
    }
}