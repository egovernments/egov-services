package org.egov.egf.bill.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchUtils {

	void add(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
		if (fieldValue != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termsQuery(field, fieldValue));
		}

	}

}