package org.egov.egf.persistence.repository;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import org.egov.egf.persistence.entity.FundSearchCriteria;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class FundQueryFactory {
	public BoolQueryBuilder create(FundSearchCriteria criteria) {
		BoolQueryBuilder boolQueryBuilder = boolQuery();
		boolQueryBuilder = addIdsFilter(criteria, boolQueryBuilder);
		boolQueryBuilder = addNameFilter(criteria, boolQueryBuilder);
		boolQueryBuilder = addCodeFilter(criteria, boolQueryBuilder);
		boolQueryBuilder = addIdentifierFilter(criteria, boolQueryBuilder);
		boolQueryBuilder = addLevelFilter(criteria, boolQueryBuilder);
		boolQueryBuilder = addParentIdFilter(criteria, boolQueryBuilder);
		boolQueryBuilder = addIsParentFilter(criteria, boolQueryBuilder);
		boolQueryBuilder = addActiveFilter(criteria, boolQueryBuilder);
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addIdsFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (criteria.getIds() != null && !criteria.getIds().isEmpty()) {
			boolQueryBuilder = boolQueryBuilder.filter(termsQuery("id", criteria.getIds()));
		}
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addNameFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (isNotEmpty(criteria.getName())) {
			boolQueryBuilder = boolQueryBuilder.filter(termQuery("name", criteria.getName()));
		}
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addCodeFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (isNotEmpty(criteria.getCode())) {
			boolQueryBuilder = boolQueryBuilder.filter(termQuery("code", criteria.getCode()));
		}
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addIdentifierFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (criteria.getIdentifier() != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termQuery("identifier", criteria.getIdentifier().toString()));
		}
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addLevelFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (criteria.getLevel() != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termQuery("level", criteria.getLevel()));
		}
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addParentIdFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (criteria.getParentId() != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termQuery("parentId", criteria.getParentId()));
		}
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addIsParentFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (criteria.getIsParent() != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termsQuery("isParent", criteria.getIsParent()));
		}
		return boolQueryBuilder;
	}

	private BoolQueryBuilder addActiveFilter(FundSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
		if (criteria.getActive() != null) {
			boolQueryBuilder = boolQueryBuilder.filter(termsQuery("active", criteria.getActive()));
		}
		return boolQueryBuilder;
	}
}
