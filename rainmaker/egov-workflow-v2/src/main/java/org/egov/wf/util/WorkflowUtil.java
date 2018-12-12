package org.egov.wf.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.egov.wf.web.models.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.egov.wf.util.WorkflowConstants.*;


@Component
public class WorkflowUtil {

    private ObjectMapper mapper;


    @Autowired
    public WorkflowUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Method to return auditDetails for create/update flows
     *
     * @param by The uuid of the user sending the request
     * @param isCreate Flag to determine if the call is for create or update
     * @return AuditDetails The auditdetails of the request
     */
    public AuditDetails getAuditDetails(String by, Boolean isCreate) {
        Long time = System.currentTimeMillis();
        if(isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }


    /**
     * Checks if the user has role allowed for the action
     * @param userRoles The roles available with the user
     * @param actionRoles The roles for which action is allowed
     * @return True if user can perform the action else false
     */
    public Boolean isRoleAvailable(List<Role> userRoles, List<String> actionRoles){
        Boolean flag = false;
        List<String> allowedRoles = Arrays.asList(actionRoles.get(0).split(","));
        for(Role role : userRoles) {
            if (allowedRoles.contains(role.getCode())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     *  Fetches roles of all the actions in the businessService
     * @param mdmsData The mdms data from MDMS search
     * @return All roles in the business service
     */
    public List<String> rolesAllowedInService(Object mdmsData,String jsonPath){
        List<String> roles;
        try {
          roles = JsonPath.read(mdmsData,jsonPath);
        }
        catch (Exception e){
            throw new CustomException("PARSING ERROR","Failed to fetch allowed roles of the service");
        }
        return roles;
    }



    /**
     * Fetches the appropriate BusinessService object from MDMS data
     * @param mdmsData MDMS data from MDMS search
     * @param businessServiceName The businessService of the request
     * @return BusinessService object for the given business service
     */
    public BusinessService getBusinessService(Object mdmsData,String businessServiceName){
        BusinessService businessService;
        try {
            String jsonpath = WF_JSONPATH_CODE.replace("{name}",businessServiceName);
            List<Object> objects  = JsonPath.read(mdmsData,jsonpath);
            String jsonString = new JSONArray(objects).toString();
            List<BusinessService> businessServices =
                    mapper.readValue(jsonString, new TypeReference<List<BusinessService>>(){});
            businessService = businessServices.get(0);
        }
        catch (Exception e){
            throw new CustomException("BUSINESSSERVICE ERROR","Failed to get applicable Business Service object");
        }
        return businessService;
    }


    /**
     * Fetches all BusinessService object from MDMS data
     * @param mdmsData MDMS data from MDMS search
     * @return BusinessService object for the given business service
     */
    public List<BusinessService> getAllBusinessServices(Object mdmsData){
        List<BusinessService> businessServices;
        try {
            List<Object> objects  = JsonPath.read(mdmsData,ALL_WF_JSONPATH_CODE);
            String jsonString = new JSONArray(objects).toString();
            businessServices =
                    mapper.readValue(jsonString, new TypeReference<List<BusinessService>>(){});
        }
        catch (Exception e){
            throw new CustomException("BUSINESSSERVICE ERROR","Failed to get applicable Business Service object");
        }
        return businessServices;
    }


    /**
     * Creates a map of status to roles who can take actions on it for particular businessService
     * @param businessService The businessService for which map is to be created
     * @return Map of status to roles which can take action on it
     */
    public Map<String,Set<String>> getStateToRoleMap(BusinessService businessService){
        Map<String,Set<String>> stateToRolesMap = new HashMap<>();
        for(State state : businessService.getStates()){
            HashSet<String> roles = new HashSet<>();
            state.getActions().forEach(action -> {
                roles.addAll(Arrays.asList(action.getRoles().get(0).split(",")));
            });
            stateToRolesMap.put(state.getUuid(),roles);
        }
        return stateToRolesMap;
    }


    /**
     * Creates a map of status to roles who can take actions on it for all businessService
     * @param businessServices The list of businessServices
     * @return Map of status to roles which can take action on it for all businessService
     */
    public Map<String,Set<String>> getStateToRoleMap(List<BusinessService> businessServices){
        Map<String,Set<String>> stateToRolesMap = new HashMap<>();
        businessServices.forEach(businessService -> {
            for(State state : businessService.getStates()){
                HashSet<String> roles = new HashSet<>();
                state.getActions().forEach(action -> {
                    roles.addAll(Arrays.asList(action.getRoles().get(0).split(",")));
                });
                stateToRolesMap.put(state.getUuid(),roles);
            }
        });
        return stateToRolesMap;
    }


    /**
     * Gets the roles the user is assigned
     * @param requestInfo RequestInfo of the request
     * @return List of roles for user in the requestInfo
     */
    public List<String> getUserRoles(RequestInfo requestInfo){
        List<String> roleCodes = new LinkedList<>();
        requestInfo.getUserInfo().getRoles().forEach(role -> {
            roleCodes.add(role.getCode());
        });
        return roleCodes;
    }


    /**
     * Gets the list of status on which user from requestInfo can take action upon
     * @param requestInfo The RequestInfo Object of the request
     * @param businessServices List of all businessServices
     * @return List of status on which user from requestInfo can take action upon
     */
    public List<String> getActionableStatusesForRole(RequestInfo requestInfo, List<BusinessService> businessServices){
        Map<String,Set<String>> stateToRoleMap = getStateToRoleMap(businessServices);
        List<String> userRoleCodes = getUserRoles(requestInfo);
        List<String> actionableStatuses = new LinkedList<>();
        for(Map.Entry<String,Set<String>> entry : stateToRoleMap.entrySet()){
            if(!Collections.disjoint(userRoleCodes,entry.getValue())){
                actionableStatuses.add(entry.getKey());
            }

        }
        return actionableStatuses;
    }









}
