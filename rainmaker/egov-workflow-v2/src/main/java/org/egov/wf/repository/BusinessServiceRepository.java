package org.egov.wf.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.wf.repository.querybuilder.BusinessServiceQueryBuilder;
import org.egov.wf.repository.rowmapper.BusinessServiceRowMapper;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class BusinessServiceRepository {


    private BusinessServiceQueryBuilder queryBuilder;

    private JdbcTemplate jdbcTemplate;

    private BusinessServiceRowMapper rowMapper;


    @Autowired
    public BusinessServiceRepository(BusinessServiceQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate,
                                     BusinessServiceRowMapper rowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    /**
     * Queries the db to fetch the current currentState and action for the given processInstance
     * @param processInstance The ProcessInstance whose current currentState and action has to be fetched
     * @return List of BusinessService object containing the current currentState and action
     */
    public List<BusinessService> getCurrentStateAndAction(ProcessInstance processInstance,Boolean isTransition){
        List<Object> preparedStmtList = new ArrayList<>();
        BusinessServiceSearchCriteria criteria = getCriteria(processInstance,isTransition);
        String query = queryBuilder.getStateAndActionSearchQuery(criteria, preparedStmtList);
        log.info("Query CurrentStateAndAction: "+query);
        log.info("preparedStmtList : "+preparedStmtList);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }


    /**
     * Queries to get the currentState after the action is applied
     * @param action The action object whose nextState has to be fetched
     * @return List of BusinessService containing the nextState
     */
    public List<BusinessService> getResultantState(Action action){
        List<Object> preparedStmtList = new ArrayList<>();
        BusinessServiceSearchCriteria criteria = getCriteria(action);
        String query = queryBuilder.getResultantState(criteria, preparedStmtList);
        log.info("Query ResultantState : "+query);
        log.info("preparedStmtList: : "+preparedStmtList);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }


    /**
     * Creates BusinessSearchCriteria from processInstance
     * @param processInstance The processInstance Object whose currentState and action is to be searched
     * @return BusinessSearchCriteria for the processInstance
     */
    private BusinessServiceSearchCriteria getCriteria(ProcessInstance processInstance,Boolean isTransition){
        BusinessServiceSearchCriteria criteria = new BusinessServiceSearchCriteria();
        criteria.setTenantId(processInstance.getTenantId());
        criteria.setBusinessService(processInstance.getBusinessService());
        if(isTransition)
            criteria.setAction(processInstance.getAction());
        criteria.setStateUuid(processInstance.getStatus());
        return criteria;
    }

    /**
     * Creates BusinessSearchCriteria from action object
     * @param action The action Object whose nextState has to be searched
     * @return BusinessSearchCriteria for the action
     */
    private BusinessServiceSearchCriteria getCriteria(Action action){
        BusinessServiceSearchCriteria criteria = new BusinessServiceSearchCriteria();
        criteria.setStateUuid(action.getNextState());
        return criteria;
    }



    public List<BusinessService> getBusinessServices(BusinessServiceSearchCriteria criteria){
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getBusinessServices(criteria, preparedStmtList);
        log.info("Query BusinessServices: "+query);
        log.info("preparedStmtList: : "+preparedStmtList);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }


    /**
     * Replaces * in roles with all available roles in the particular businessService
     * @param businessServices The list of businessServices to be enriched
     */
    private void setAllRoles(List<BusinessService> businessServices){
        Map<String,List<String>> idToRoles = new HashMap<>();

        if(CollectionUtils.isEmpty(businessServices))
            return;

        // Collecting all the available roles for the businessService
        businessServices.forEach(businessService -> {
            businessService.getStates().forEach(state -> {
                if(!CollectionUtils.isEmpty(state.getActions()))
                    state.getActions().forEach(action -> {
                        if(!action.getRoles().contains("*"))
                            idToRoles.put(businessService.getUuid(),action.getRoles());
                    });
            });
        });
        // Replacing * with all roles available
        businessServices.forEach(businessService -> {
            businessService.getStates().forEach(state -> {
                if(!CollectionUtils.isEmpty(state.getActions()))
                    state.getActions().forEach(action -> {
                        if(action.getRoles().contains("*"))
                            action.setRoles(idToRoles.get(businessService.getUuid()));
                    });
            });
        });
    }



}
