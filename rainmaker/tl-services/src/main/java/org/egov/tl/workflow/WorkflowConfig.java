package org.egov.tl.workflow;

import org.egov.tl.web.models.TradeLicense;

import java.util.Collections;
import java.util.*;

public class WorkflowConfig {


    public static String ACTION_INITIATE = TradeLicense.ActionEnum.INITIATE.toString();
    public static String ACTION_APPLY = TradeLicense.ActionEnum.APPLY.toString();
    public static String ACTION_APPROVE = TradeLicense.ActionEnum.APPROVE.toString();
    public static String ACTION_REJECT = TradeLicense.ActionEnum.REJECT.toString();
    public static String ACTION_CANCEL = TradeLicense.ActionEnum.CANCEL.toString();

    public static String STATUS_INITIALIZED = TradeLicense.StatusEnum.INITIATED.toString();
    public static String STATUS_APPLIED = TradeLicense.StatusEnum.APPLIED.toString();
    public static String STATUS_PAID = TradeLicense.StatusEnum.PAID.toString();
    public static String STATUS_APPROVED = TradeLicense.StatusEnum.APPROVED.toString();
    public static String STATUS_REJECTED = TradeLicense.StatusEnum.REJECTED.toString();
    public static String STATUS_CANCELLED = TradeLicense.StatusEnum.CANCELLED.toString();

    public static String ROLE_CITIZEN = "CITIZEN";
    public static String ROLE_EMPLOYEE = "EMPLOYEE";

    private WorkflowConfig(){}

    private static Map<String, String> actionStatusMap;

    private static Map<String, List<String>> roleActionMap;

    private static Map<String,  List<String>> actionCurrentStatusMap;

    static {
        setActionStatusMap();
        setRoleActionMap();
        setActionCurrentStatusMap();
    }


    private static void setActionStatusMap(){

        Map<String, String> map = new HashMap<>();

        map.put(ACTION_INITIATE, STATUS_INITIALIZED);
        map.put(ACTION_APPLY, STATUS_APPLIED);
        map.put(ACTION_APPROVE, STATUS_APPROVED);
        map.put(ACTION_REJECT, STATUS_REJECTED);
        map.put(ACTION_CANCEL, STATUS_CANCELLED);

        actionStatusMap = Collections.unmodifiableMap(map);
    }

    private static void setRoleActionMap(){

        Map<String, List<String>> map = new HashMap<>();

        map.put(ROLE_CITIZEN, Arrays.asList(ACTION_APPLY, ACTION_INITIATE));
        map.put(ROLE_EMPLOYEE, Arrays.asList(ACTION_APPLY, ACTION_INITIATE,ACTION_APPROVE, ACTION_REJECT,ACTION_CANCEL));

        roleActionMap = Collections.unmodifiableMap(map);
    }

    private static void setActionCurrentStatusMap(){

        Map<String, List<String>> map = new HashMap<>();

        map.put(STATUS_INITIALIZED, Arrays.asList(ACTION_APPLY,ACTION_INITIATE));
        map.put(STATUS_APPLIED, Arrays.asList(ACTION_APPLY,ACTION_APPROVE, ACTION_REJECT)); // FIXME PUT THE ACTIONS IN PLACE
        map.put(STATUS_PAID, Arrays.asList(ACTION_APPROVE, ACTION_REJECT));
        map.put(STATUS_APPROVED, Arrays.asList(ACTION_CANCEL));
        map.put(STATUS_REJECTED, Arrays.asList()); // FIXME PUT THE ACTIONS IN PLACE
        map.put(STATUS_CANCELLED, Arrays.asList()); // FIXME PUT THE ACTIONS IN PLACE

        actionCurrentStatusMap = Collections.unmodifiableMap(map);
    }

    public static Map<String, String> getActionStatusMap(){
        return actionStatusMap;
    }

    public static Map<String, List<String>> getActionCurrentStatusMap(){
        return actionCurrentStatusMap;
    }

    public static Map<String, List<String>> getRoleActionMap(){
        return roleActionMap;
    }



}
