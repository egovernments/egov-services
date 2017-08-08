package org.egov.egf.budget.domain.repository;

import org.egov.egf.budget.web.contract.BudgetDetailSearchContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Service
public class ElasticSearchQueryFactory {

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    public BoolQueryBuilder searchBudget(BudgetSearchContract budgetSearchContract) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        elasticSearchUtils.add(budgetSearchContract.getDescription(), "description", boolQueryBuilder);
        return boolQueryBuilder;
    }

    public BoolQueryBuilder searchBudgetDetail(BudgetDetailSearchContract budgetDetailSearchContract) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        elasticSearchUtils.add(budgetDetailSearchContract.getAnticipatoryAmount(), "description", boolQueryBuilder);
        return boolQueryBuilder;
    }

    public BoolQueryBuilder searchBudgetReAppropriation(BudgetReAppropriationSearchContract budgetReAppropriationSearchContract) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        elasticSearchUtils.add(budgetReAppropriationSearchContract.getAnticipatoryAmount(), "description", boolQueryBuilder);
        return boolQueryBuilder;
    }


    public List<String> prepareOrderBys(String sortBy) {
        List<String> orderByList = new ArrayList<String>();
        List<String> sortByList = new ArrayList<String>();
        if (sortBy.contains(",")) {
                sortByList = Arrays.asList(sortBy.split(","));
        } else {
                sortByList = Arrays.asList(sortBy);
        }
        for (String s : sortByList) {
                if (s.contains(" ") && (s.toLowerCase().trim().endsWith("asc") || s.toLowerCase().trim().endsWith("desc"))) {
                    orderByList.add(s.trim());
                }
                else {
                    
                    orderByList.add(s.trim() + " asc");
                }
        }
        
        return orderByList;
    }

}
