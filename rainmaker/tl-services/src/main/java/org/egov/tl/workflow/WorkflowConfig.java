package org.egov.tl.workflow;

import org.egov.tl.config.TLConfiguration;
import org.egov.tl.web.models.TradeLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.*;

import static org.egov.tl.web.models.TradeLicense.ActionEnum.*;
import static org.egov.tl.web.models.TradeLicense.StatusEnum.*;


@Configuration
@PropertySource("classpath:workflow.properties")
public class WorkflowConfig {


    private Environment env;

    @Autowired
    public WorkflowConfig(Environment env) {
        this.env = env;
        setActionStatusMap();
        setRoleActionMap();
        setActionCurrentStatusMap();
    }


    private String CONFIG_ROLES ="egov.workflow.tl.roles";



    private  Map<String, String> actionStatusMap;

    private  Map<String, List<String>> roleActionMap;

    private  Map<String,  List<String>> actionCurrentStatusMap;




    private  void setActionStatusMap(){

        Map<String, String> map = new HashMap<>();

        map.put(INITIATE.toString(), INITIATED.toString());
        map.put(APPLY.toString(), APPLIED.toString());
        map.put(APPROVE.toString(), APPROVED.toString());
        map.put(REJECT.toString(), REJECTED.toString());
        map.put(CANCEL.toString(), CANCELLED.toString());

        actionStatusMap = Collections.unmodifiableMap(map);
    }

/*    private  void setRoleActionMap(){

        Map<String, List<String>> map = new HashMap<>();

        map.put(config.getROLE_CITIZEN(), Arrays.asList(ACTION_APPLY, ACTION_INITIATE));
        map.put(config.getROLE_EMPLOYEE(), Arrays.asList(ACTION_APPLY, ACTION_INITIATE,ACTION_APPROVE, ACTION_REJECT,ACTION_CANCEL));

        roleActionMap = Collections.unmodifiableMap(map);
    }*/


    private  void setRoleActionMap(){

        Map<String, List<String>> map = new HashMap<>();

        String[] keys = env.getProperty(CONFIG_ROLES).split(",");

        for(String key : keys){
            map.put(env.getProperty(key),Arrays.asList(env.getProperty(key.replace("role","action")).split(",")));
        }

        roleActionMap = Collections.unmodifiableMap(map);
    }



    private  void setActionCurrentStatusMap(){

        Map<String, List<String>> map = new HashMap<>();

        map.put(null, Arrays.asList(APPLY.toString(),INITIATE.toString()));
        map.put(INITIATED.toString(), Arrays.asList(APPLY.toString(),INITIATE.toString()));
        map.put(APPLIED.toString(), Arrays.asList(APPLY.toString())); // FIXME PUT THE ACTIONS IN PLACE
        map.put(PAID.toString(), Arrays.asList(APPROVE.toString(), REJECT.toString()));
        map.put(APPROVED.toString(), Arrays.asList(CANCEL.toString()));
        map.put(REJECTED.toString(), Arrays.asList()); // FIXME PUT THE ACTIONS IN PLACE
        map.put(CANCELLED.toString(), Arrays.asList()); // FIXME PUT THE ACTIONS IN PLACE

        actionCurrentStatusMap = Collections.unmodifiableMap(map);
    }

    public  Map<String, String> getActionStatusMap(){
        return actionStatusMap;
    }

    public  Map<String, List<String>> getActionCurrentStatusMap(){
        return actionCurrentStatusMap;
    }

    public  Map<String, List<String>> getRoleActionMap(){
        return roleActionMap;
    }



}
