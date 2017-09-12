package org.egov.mr.repository.querybuilder;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.egov.mr.TestConfiguration;
import org.egov.mr.web.contract.FeeCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeeQueryBuilderTest {

	@InjectMocks
	private FeeQueryBuilder feeQueryBuilder;

	private final String query = "SELECT * FROM egmr_fee WHERE  tenantId = 'defualt' AND id IN ( '123') AND fromDate = 123456789 AND toDate = 23456789 AND feeCriteria = '2017-2018'";

	@Test
	public void testGetQuery() {
		Set<String> ids = new HashSet<>();
		ids.add("123");
		FeeCriteria feeCriteriaQuery = FeeCriteria.builder().tenantId("defualt").id(ids).feeCriteria("2017-2018")
				.fromDate(123456789l).toDate(23456789l).build();

		assertEquals(query, feeQueryBuilder.getQuery(feeCriteriaQuery));
	}

}
