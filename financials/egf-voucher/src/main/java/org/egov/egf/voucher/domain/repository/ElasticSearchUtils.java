package org.egov.egf.voucher.domain.repository;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Service
public class ElasticSearchUtils {

    void add(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
        if (fieldValue != null) {
            boolQueryBuilder = boolQueryBuilder.filter(termsQuery(field, fieldValue));
        }

    }

    void gte(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
        if (fieldValue != null) {
            boolQueryBuilder = boolQueryBuilder
                    .filter(QueryBuilders.rangeQuery(field)
                            .from(fieldValue));
        }

    }

    void lte(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
        if (fieldValue != null) {
            boolQueryBuilder = boolQueryBuilder
                    .filter(QueryBuilders.rangeQuery(field)
                            .to(fieldValue));
        }

    }

    void in(Object fieldValue, String field, BoolQueryBuilder boolQueryBuilder) {
        if (fieldValue != null) {
            boolQueryBuilder = boolQueryBuilder
                    .should(QueryBuilders.multiMatchQuery(fieldValue, field));
        }

    }

}