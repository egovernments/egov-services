package org.egov.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.model.AssetCategory;
import org.egov.model.AssetCategoryCriteria;
import org.egov.repository.querybuilder.AssetCategoryQueryBuilder;
import org.egov.repository.rowmapper.AssetCategoryRowMapper;
import org.egov.service.AssetCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AssetCategoryRowMapper assetCategoryRowMapper;

    @Autowired
    private AssetCategoryQueryBuilder assetCategoryQueryBuilder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AssetCommonService assetCommonService;

    public List<AssetCategory> search(final AssetCategoryCriteria assetCategoryCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = assetCategoryQueryBuilder.getQuery(assetCategoryCriteria, preparedStatementValues);

        List<AssetCategory> assetCategory = new ArrayList<AssetCategory>();
        try {
            assetCategory = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),assetCategoryRowMapper);
        } catch (final Exception exception) {
            log.info("the exception in assetcategory search :" + exception);
        }
        
        System.err.println("AssetCategoryRepository search"+assetCategory);
        return assetCategory;
    }
}