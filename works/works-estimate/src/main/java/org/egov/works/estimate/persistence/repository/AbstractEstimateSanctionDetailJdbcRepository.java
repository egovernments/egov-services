package org.egov.works.estimate.persistence.repository;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.AbstractEstimateSanctionDetailHelper;
import org.egov.works.estimate.web.contract.AbstractEstimateSanctionDetail;
import org.egov.works.estimate.web.contract.AbstractEstimateSanctionSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AbstractEstimateSanctionDetailJdbcRepository  extends JdbcRepository
{

	public static final String TABLE_NAME = "egw_abstractestimate_sanction_details";
	
    public List<AbstractEstimateSanctionDetail> search(
            AbstractEstimateSanctionSearchContract abstractEstimateSanctionSearchContract) {
    	String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (abstractEstimateSanctionSearchContract.getSortBy() != null
                && !abstractEstimateSanctionSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(abstractEstimateSanctionSearchContract.getSortBy());
            validateEntityFieldName(abstractEstimateSanctionSearchContract.getSortBy(), AbstractEstimateSanctionDetail.class);
        }

        StringBuilder orderBy = new StringBuilder("order by id");
        if (abstractEstimateSanctionSearchContract.getSortBy() != null
                && !abstractEstimateSanctionSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by ").append(abstractEstimateSanctionSearchContract.getSortBy());
        }
        
        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");


        if (abstractEstimateSanctionSearchContract.getTenantId() != null) {
        	addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", abstractEstimateSanctionSearchContract.getTenantId());
        }
        if (abstractEstimateSanctionSearchContract.getIds() != null) {
        	addAnd(params);
            params.append("id in(:ids) ");
            paramValues.put("ids", abstractEstimateSanctionSearchContract.getIds());
        }

        if (abstractEstimateSanctionSearchContract.getAbstractEstimateIds() != null) {
        	addAnd(params);
            params.append("abstractEstimate in(:abstractEstimateIds) ");
            paramValues.put("abstractEstimateIds", abstractEstimateSanctionSearchContract.getAbstractEstimateIds());
        }

        params.append(" and deleted = false");
        if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(AbstractEstimateSanctionDetailHelper.class);

        List<AbstractEstimateSanctionDetailHelper> abstractEstimateSanctionDetailHelpers = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<AbstractEstimateSanctionDetail> abstractEstimateSanctionDetails = new ArrayList<>();

        for (AbstractEstimateSanctionDetailHelper abstractEstimateSanctionDetailHelper : abstractEstimateSanctionDetailHelpers) {
        	abstractEstimateSanctionDetails.add(abstractEstimateSanctionDetailHelper.toDomain());
        }

        return abstractEstimateSanctionDetails;
    }
}
