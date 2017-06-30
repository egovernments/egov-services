package org.egov.collection.persistence.repository.builder;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.egov.collection.domain.model.StatusCriteria;
import org.junit.Test;

public class StatusQueryBuilderTest {

	@Test
	public void no_input_test() {
	StatusCriteria statusCriteria = new StatusCriteria();

		StatusQueryBuilder builder = new StatusQueryBuilder();
		assertEquals(
				"select id,code,objecttype,description,tenantId,createdBy,"
						+ "createdDate,lastModifiedBy,lastModifiedDate FROM egcl_status",
					builder.getQuery(statusCriteria, new ArrayList<>()));
		
		
	}
	
	@Test
	public void all_input_test(){
		StatusCriteria statusCriteria = new StatusCriteria();

		StatusQueryBuilder builder = new StatusQueryBuilder();	
		
		statusCriteria.setCode("Submitted");
		statusCriteria.setObjectType("ReceiptHeader");
		statusCriteria.setTenantId("default");
		assertEquals(
				"select id,code,objecttype,description,tenantId,createdBy,"
						+ "createdDate,lastModifiedBy,lastModifiedDate FROM egcl_status"
						+" WHERE tenantId = ? AND code = ? AND objecttype = ?",
					builder.getQuery(statusCriteria, new ArrayList<>()));
		
		
		
		
		
	}
}
