package org.egov.tenant.persistence.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class CityQueryBuilder {

    public String getInsertQuery() {
        return "INSERT INTO city (id, name, localname, districtcode, districtname, regionname, longitude, latitude, tenantcode, createdby, createddate, lastmodifiedby, lastmodifieddate) " +
                "values ( nextval('seq_city'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }
}