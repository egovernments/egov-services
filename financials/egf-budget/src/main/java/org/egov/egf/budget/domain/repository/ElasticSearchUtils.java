package org.egov.egf.budget.domain.repository;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Service
public class ElasticSearchUtils {

	void add(Object fieldValue, String field,BoolQueryBuilder boolQueryBuilder ) {
		if (fieldValue != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termsQuery(field, fieldValue));
		}
		
	}

}