package org.egov.tl.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.producer.Producer;
import org.egov.tl.repository.builder.TLQueryBuilder;
import org.egov.tl.repository.rowmapper.TLRowMapper;
import org.egov.tl.web.models.*;
import org.egov.tl.web.models.workflow.BusinessService;
import org.egov.tl.web.models.workflow.State;
import org.egov.tl.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.egov.tl.util.TLConstants.ACTION_ADHOC;


@Slf4j
@Repository
public class TLRepository {

    private JdbcTemplate jdbcTemplate;

    private TLQueryBuilder queryBuilder;

    private TLRowMapper rowMapper;

    private Producer producer;

    private TLConfiguration config;

    private WorkflowService workflowService;


    @Autowired
    public TLRepository(JdbcTemplate jdbcTemplate, TLQueryBuilder queryBuilder, TLRowMapper rowMapper,
                        Producer producer, TLConfiguration config, WorkflowService workflowService) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
        this.producer = producer;
        this.config = config;
        this.workflowService = workflowService;
    }


    /**
     * Searhces license in databse
     *
     * @param criteria The tradeLicense Search criteria
     * @return List of TradeLicense from seach
     */
    public List<TradeLicense> getLicenses(TradeLicenseSearchCriteria criteria) {
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getTLSearchQuery(criteria, preparedStmtList);
        log.info("Query: " + query);
        List<TradeLicense> licenses =  jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
        sortChildObjectsById(licenses);
        return licenses;
    }

    /**
     * Pushes the request on save topic
     *
     * @param tradeLicenseRequest The tradeLciense create request
     */
    public void save(TradeLicenseRequest tradeLicenseRequest) {
        producer.push(config.getSaveTopic(), tradeLicenseRequest);
    }

    /**
     * Pushes the update request to update topic or on workflow topic depending on the status
     *
     * @param tradeLicenseRequest The update requuest
     */
    public void update(TradeLicenseRequest tradeLicenseRequest,Map<String,Boolean> idToIsStateUpdatableMap) {
        RequestInfo requestInfo = new RequestInfo();
        List<TradeLicense> licenses = tradeLicenseRequest.getLicenses();

        List<TradeLicense> licesnsesForStatusUpdate = new LinkedList<>();
        List<TradeLicense> licensesForUpdate = new LinkedList<>();
        List<TradeLicense> licensesForAdhocChargeUpdate = new LinkedList<>();


        for (TradeLicense license : licenses) {
            if (idToIsStateUpdatableMap.get(license.getId())) {
                licensesForUpdate.add(license);
            }
            else if(license.getAction().equalsIgnoreCase(ACTION_ADHOC))
                licensesForAdhocChargeUpdate.add(license);
            else {
                licesnsesForStatusUpdate.add(license);
            }
        }

        if (!CollectionUtils.isEmpty(licensesForUpdate))
            producer.push(config.getUpdateTopic(), new TradeLicenseRequest(requestInfo, licensesForUpdate));

        if (!CollectionUtils.isEmpty(licesnsesForStatusUpdate))
            producer.push(config.getUpdateWorkflowTopic(), new TradeLicenseRequest(requestInfo, licesnsesForStatusUpdate));

        if(!licensesForAdhocChargeUpdate.isEmpty())
            producer.push(config.getUpdateAdhocTopic(),new TradeLicenseRequest(requestInfo,licensesForAdhocChargeUpdate));

    }


    /**
     * Searches and returns the tradelicense from db corresponding to tradelicenses in the request
     *
     * @param request The update request
     * @return The list of TradeLicense from db
     */

    public List<TradeLicense> searchTradeLicenseFromDB(TradeLicenseRequest request) {

        Map<String, List<String>> tenantIdToIds = new HashMap<>();

        // Map of tenantId to tradeLicenses created
        request.getLicenses().forEach(license -> {
            if (tenantIdToIds.containsKey(license.getTenantId()))
                tenantIdToIds.get(license.getTenantId()).add(license.getId());
            else {
                List<String> perTenantIds = new LinkedList<>();
                perTenantIds.add(license.getId());
                tenantIdToIds.put(license.getTenantId(), perTenantIds);
            }
        });

        List<TradeLicense> searchResult = new LinkedList<>();
        tenantIdToIds.keySet().forEach(key -> {
            addTradeLicenseFromSearch(key, tenantIdToIds.get(key), searchResult);
        });

        if(searchResult.size()!=request.getLicenses().size())
            throw new CustomException("INVALID UPDATE","License to be updated not present in DB");

        return searchResult;
    }


    /**
     * Adds tradeLicense for the particular tenantId in the list
     *
     * @param tenantId     tenantId of the license
     * @param ids          ids of licenses with the given tenantId
     * @param searchResult The list containing multitenant licenses
     */
    private void addTradeLicenseFromSearch(String tenantId, List<String> ids,
                                           List<TradeLicense> searchResult) {
        TradeLicenseSearchCriteria criteria = new TradeLicenseSearchCriteria();
        criteria.setTenantId(tenantId);
        criteria.setIds(ids);
        searchResult.addAll(getLicenses(criteria));
    }


    /**
     * Sorts the child objects by  there ids
     * @param tradeLicenses The list of tradeLicense
     */
    private void sortChildObjectsById(List<TradeLicense> tradeLicenses){
        if(CollectionUtils.isEmpty(tradeLicenses))
            return;
        tradeLicenses.forEach(license -> {
            license.getTradeLicenseDetail().getOwners().sort(Comparator.comparing(User::getUuid));
            license.getTradeLicenseDetail().getTradeUnits().sort(Comparator.comparing(TradeUnit::getId));
            if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getAccessories()))
                license.getTradeLicenseDetail().getAccessories().sort(Comparator.comparing(Accessory::getId));
            if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getApplicationDocuments()))
                license.getTradeLicenseDetail().getApplicationDocuments().sort(Comparator.comparing(Document::getId));
            if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getVerificationDocuments()))
                license.getTradeLicenseDetail().getVerificationDocuments().sort(Comparator.comparing(Document::getId));
        });
    }


}
