package org.egov.egf.voucher.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchUtils {

	void add(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
		if (fieldValue != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termsQuery(field, fieldValue));
		}

	}

	void gte(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
		if (fieldValue != null) {
			boolQueryBuilder = boolQueryBuilder.filter(QueryBuilders.rangeQuery(field).from(fieldValue));
		}

	}

	void lte(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
		if (fieldValue != null) {
			boolQueryBuilder = boolQueryBuilder.filter(QueryBuilders.rangeQuery(field).to(fieldValue));
		}

	}

	void in(List fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
		if (fieldValue != null) {
			boolQueryBuilder.filter(QueryBuilders.termsQuery(field, fieldValue));
		}

	}

}