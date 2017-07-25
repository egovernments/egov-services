package org.egov.egf.master.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

import org.egov.egf.master.web.contract.FundSearchContract;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundQueryFactory {
	
	@Autowired
	private ElasticSearchUtils elasticSearchUtils;
	
	public BoolQueryBuilder create(FundSearchContract fundSearchContract) {
		BoolQueryBuilder boolQueryBuilder = boolQuery();
		if(!fundSearchContract.getIds().isEmpty())
			elasticSearchUtils.add(fundSearchContract.getIds(),"id",boolQueryBuilder);
		elasticSearchUtils.add(fundSearchContract.getName(),"name",boolQueryBuilder);
		elasticSearchUtils.add(fundSearchContract.getCode(),"code",boolQueryBuilder);
		elasticSearchUtils.add(fundSearchContract.getIdentifier(),"identifier",boolQueryBuilder);
		elasticSearchUtils.add(fundSearchContract.getLevel(),"level",boolQueryBuilder);
		elasticSearchUtils.add(fundSearchContract.getParent(),"parent",boolQueryBuilder);
		elasticSearchUtils.add(fundSearchContract.getIsParent(),"isParent",boolQueryBuilder);
		elasticSearchUtils.add(fundSearchContract.getActive(),"active",boolQueryBuilder);
		
		return boolQueryBuilder;
	}

}
