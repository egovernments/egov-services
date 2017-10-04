package org.egov.egf.bill.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchUtils {

	void add(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
		if (fieldValue != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termsQuery(field,
					fieldValue));
		}

	}

	void in(List fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
		if (fieldValue != null) {
			boolQueryBuilder
					.filter(QueryBuilders.termsQuery(field, fieldValue));
		}

	}

}