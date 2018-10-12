package org.egov.tl.workflow;

import org.egov.tl.config.TLConfiguration;
import org.egov.tl.web.models.TradeLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.*;

@Component
public class WorkflowConfig {


    private TLConfiguration config;

    @Autowired
    public WorkflowConfig(TLConfiguration config) {
        this.config = config;
        setActionStatusMap();
        setRoleActionMap();
        setActionCurrentStatusMap();
    }

    private String ACTION_INITIATE = TradeLicense.ActionEnum.INITIATE.toString();
    private  String ACTION_APPLY = TradeLicense.ActionEnum.APPLY.toString();
    private  String ACTION_APPROVE = TradeLicense.ActionEnum.APPROVE.toString();
    private  String ACTION_REJECT = TradeLicense.ActionEnum.REJECT.toString();
    private  String ACTION_CANCEL = TradeLicense.ActionEnum.CANCEL.toString();

    private  String STATUS_INITIALIZED = TradeLicense.StatusEnum.INITIATED.toString();
    private  String STATUS_APPLIED = TradeLicense.StatusEnum.APPLIED.toString();
    private  String STATUS_PAID = TradeLicense.StatusEnum.PAID.toString();
    private  String STATUS_APPROVED = TradeLicense.StatusEnum.APPROVED.toString();
    private  String STATUS_REJECTED = TradeLicense.StatusEnum.REJECTED.toString();
    private  String STATUS_CANCELLED = TradeLicense.StatusEnum.CANCELLED.toString();



    private  Map<String, String> actionStatusMap;

    private  Map<String, List<String>> roleActionMap;

    private  Map<String,  List<String>> actionCurrentStatusMap;




    private  void setActionStatusMap(){

        Map<String, String> map = new HashMap<>();

        map.put(ACTION_INITIATE, STATUS_INITIALIZED);
        map.put(ACTION_APPLY, STATUS_APPLIED);
        map.put(ACTION_APPROVE, STATUS_APPROVED);
        map.put(ACTION_REJECT, STATUS_REJECTED);
        map.put(ACTION_CANCEL, STATUS_CANCELLED);

        actionStatusMap = Collections.unmodifiableMap(map);
    }

    private  void setRoleActionMap(){

        Map<String, List<String>> map = new HashMap<>();

        map.put(config.getROLE_CITIZEN(), Arrays.asList(ACTION_APPLY, ACTION_INITIATE));
        map.put(config.getROLE_EMPLOYEE(), Arrays.asList(ACTION_APPLY, ACTION_INITIATE,ACTION_APPROVE, ACTION_REJECT,ACTION_CANCEL));

        roleActionMap = Collections.unmodifiableMap(map);
    }

    private  void setActionCurrentStatusMap(){

        Map<String, List<String>> map = new HashMap<>();

        map.put(STATUS_INITIALIZED, Arrays.asList(ACTION_APPLY,ACTION_INITIATE));
        map.put(STATUS_APPLIED, Arrays.asList(ACTION_APPLY,ACTION_APPROVE, ACTION_REJECT)); // FIXME PUT THE ACTIONS IN PLACE
        map.put(STATUS_PAID, Arrays.asList(ACTION_APPROVE, ACTION_REJECT));
        map.put(STATUS_APPROVED, Arrays.asList(ACTION_CANCEL));
        map.put(STATUS_REJECTED, Arrays.asList()); // FIXME PUT THE ACTIONS IN PLACE
        map.put(STATUS_CANCELLED, Arrays.asList()); // FIXME PUT THE ACTIONS IN PLACE

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
