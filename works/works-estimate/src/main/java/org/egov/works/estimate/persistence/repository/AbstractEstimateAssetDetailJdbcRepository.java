package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.AbstractEstimateAssetDetailHelper;
import org.egov.works.estimate.web.contract.AbstractEstimateAssetDetail;
import org.egov.works.estimate.web.contract.AbstractEstimateAssetDetailSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class AbstractEstimateAssetDetailJdbcRepository  extends JdbcRepository
{

	public static final String TABLE_NAME = "egw_abstractestimate_asset_details";
	
    public List<AbstractEstimateAssetDetail> search(
    		AbstractEstimateAssetDetailSearchContract abstractEstimateAssetDetailSearchContract) {
    	String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (abstractEstimateAssetDetailSearchContract.getSortBy() != null
                && !abstractEstimateAssetDetailSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(abstractEstimateAssetDetailSearchContract.getSortBy());
            validateEntityFieldName(abstractEstimateAssetDetailSearchContract.getSortBy(), AbstractEstimateAssetDetailHelper.class);
        }

        String orderBy = "order by id";
        if (abstractEstimateAssetDetailSearchContract.getSortBy() != null
                && !abstractEstimateAssetDetailSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by " + abstractEstimateAssetDetailSearchContract.getSortBy();
        }
        
        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");


        if (abstractEstimateAssetDetailSearchContract.getTenantId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", abstractEstimateAssetDetailSearchContract.getTenantId());
        }
        if (abstractEstimateAssetDetailSearchContract.getIds() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("id in(:ids) ");
            paramValues.put("ids", abstractEstimateAssetDetailSearchContract.getIds());
        }

        if (abstractEstimateAssetDetailSearchContract.getAbstractEstimateIds() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("abstractEstimate in(:abstractEstimateIds) ");
            paramValues.put("abstractEstimateIds", abstractEstimateAssetDetailSearchContract.getAbstractEstimateIds());
        }
        
        if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(AbstractEstimateAssetDetailHelper.class);

        List<AbstractEstimateAssetDetailHelper> abstractEstimateAssetDetailHelpers = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<AbstractEstimateAssetDetail> abstractEstimateAssetDetails = new ArrayList<>();

        for (AbstractEstimateAssetDetailHelper abstractEstimateAssetDetailHelper : abstractEstimateAssetDetailHelpers) {
        	abstractEstimateAssetDetails.add(abstractEstimateAssetDetailHelper.toDomain());
        }

        return abstractEstimateAssetDetails;
    }
}
