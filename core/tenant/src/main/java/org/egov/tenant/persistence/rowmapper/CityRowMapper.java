package org.egov.tenant.persistence.rowmapper;

import org.egov.tenant.persistence.entity.City;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityRowMapper implements RowMapper<City> {

    @Override
    public City mapRow(ResultSet resultSet, int i) throws SQLException {
        return City.builder()
                .id(resultSet.getLong(City.Fields.ID.getValue()))
                .name(resultSet.getString(City.Fields.NAME.getValue()))
                .localName(resultSet.getString(City.Fields.LOCAL_NAME.getValue()))
                .districtCode(resultSet.getString(City.Fields.DISTRICT_CODE.getValue()))
                .districtName(resultSet.getString(City.Fields.DISTRICT_NAME.getValue()))
                .latitude(resultSet.getDouble(City.Fields.LATITUDE.getValue()))
                .longitude(resultSet.getDouble(City.Fields.LONGITUDE.getValue()))
                .tenantCode(resultSet.getString(City.Fields.TENANT_CODE.getValue()))
                .regionName(resultSet.getString(City.Fields.REGION_NAME.getValue()))
                .createdBy(resultSet.getLong(City.Fields.CREATED_BY.getValue()))
                .createdDate(resultSet.getTimestamp(City.Fields.CREATED_DATE.getValue()))
                .lastModifiedBy(resultSet.getLong(City.Fields.LAST_MODIFIED_BY.getValue()))
                .lastModifiedDate(resultSet.getTimestamp(City.Fields.LAST_MODIFIED_DATE.getValue()))
                .build();
    }
}
