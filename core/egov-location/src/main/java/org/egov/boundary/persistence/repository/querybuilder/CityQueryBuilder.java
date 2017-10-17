package org.egov.boundary.persistence.repository.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class CityQueryBuilder {
	
	public static String getCityByCodeAndTenantId(){
		return "select * from eg_city where code=:code and tenantid=:tenantId";
	}

	public static String getCityIdAndTenantId(){
		return "select * from eg_city where id=:id and tenantid=:tenantId";
	}
}
