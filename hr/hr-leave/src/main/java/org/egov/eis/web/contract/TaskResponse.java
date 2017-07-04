package org.egov.eis.web.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@JsonInclude(value = Include.NON_NULL)
public @Data class TaskResponse {
    private ResponseInfo responseInfo;
    private List<Task> tasks;
    private Task task;
    private Pagination page;
}