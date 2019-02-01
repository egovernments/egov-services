package org.egov.tl.workflow;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.*;

import static org.egov.tl.util.TLConstants.*;
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

        map.put(ACTION_INITIATE, INITIATED.toString());
        map.put(ACTION_APPLY, APPLIED.toString());
        map.put(ACTION_APPROVE, APPROVED.toString());
        map.put(ACTION_REJECT, REJECTED.toString());
        map.put(ACTION_CANCEL, CANCELLED.toString());

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

        map.put(null, Arrays.asList(ACTION_APPLY,ACTION_INITIATE));
        map.put(INITIATED.toString(), Arrays.asList(ACTION_APPLY,ACTION_INITIATE));
        map.put(APPLIED.toString(), Arrays.asList(ACTION_APPLY)); // FIXME PUT THE ACTIONS IN PLACE
        map.put(PAID.toString(), Arrays.asList(ACTION_APPROVE, ACTION_REJECT));
        map.put(APPROVED.toString(), Arrays.asList(ACTION_CANCEL));
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
