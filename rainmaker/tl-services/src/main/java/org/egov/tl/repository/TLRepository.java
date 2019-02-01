package org.egov.tl.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.producer.Producer;
import org.egov.tl.repository.builder.TLQueryBuilder;
import org.egov.tl.repository.rowmapper.TLRowMapper;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.egov.tl.web.models.TradeLicenseSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.egov.tl.util.TLConstants.*;


@Slf4j
@Repository
public class TLRepository {

    private JdbcTemplate jdbcTemplate;

    private TLQueryBuilder queryBuilder;

    private TLRowMapper rowMapper;

    private Producer producer;

    private TLConfiguration config;

    @Autowired
    public TLRepository(JdbcTemplate jdbcTemplate, TLQueryBuilder queryBuilder,
                        TLRowMapper rowMapper, Producer producer, TLConfiguration config) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
        this.producer = producer;
        this.config = config;
    }


    /**
     * Searhces license in databse
     * @param criteria The tradeLicense Search criteria
     * @return List of TradeLicense from seach
     */
    public List<TradeLicense> getLicenses(TradeLicenseSearchCriteria criteria){
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getTLSearchQuery(criteria, preparedStmtList);
        log.info("Query: "+query);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }

    /**
     * Pushes the request on save topic
     * @param tradeLicenseRequest The tradeLciense create request
     */
    public void save(TradeLicenseRequest tradeLicenseRequest){
        producer.push(config.getSaveTopic(),tradeLicenseRequest);
    }

    /**
     * Pushes the update request to update topic or on workflow topic depending on the status
     * @param tradeLicenseRequest The update requuest
     */
    public void update(TradeLicenseRequest tradeLicenseRequest){
        RequestInfo requestInfo = new RequestInfo();
        List<TradeLicense> licenses = tradeLicenseRequest.getLicenses();

        List<TradeLicense> licesnsesForStatusUpdate = new LinkedList<>();
        List<TradeLicense> licensesForUpdate = new LinkedList<>();

        licenses.forEach(license -> {
            if(license.getAction().equalsIgnoreCase(ACTION_APPLY)
                    || license.getAction().equalsIgnoreCase(ACTION_INITIATE)){
                licensesForUpdate.add(license);
            }
            if(license.getAction().equalsIgnoreCase(ACTION_APPROVE)
                    || license.getAction().equalsIgnoreCase(ACTION_REJECT)
                     || license.getAction().equalsIgnoreCase(ACTION_CANCEL)
                    || license.getAction().equalsIgnoreCase(ACTION_PAY)){
                licesnsesForStatusUpdate.add(license);
            }
        });

        if(!licensesForUpdate.isEmpty())
            producer.push(config.getUpdateTopic(),new TradeLicenseRequest(requestInfo,licensesForUpdate));

        if(!licesnsesForStatusUpdate.isEmpty())
            producer.push(config.getUpdateWorkflowTopic(),new TradeLicenseRequest(requestInfo,licesnsesForStatusUpdate));

    }












}
