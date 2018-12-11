package org.egov.wf.web.models;

import lombok.Data;

@Data
public class ProcessStateAndAction {

    private ProcessInstance processInstance = null;

    private Action action = null;

    private State currentState = null;

    private State postActionState = null;

}
