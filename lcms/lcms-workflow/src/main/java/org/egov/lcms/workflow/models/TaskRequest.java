package org.egov.lcms.workflow.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * TaskRequest class
 * 
 * @author Yosadhara
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskRequest {

	private RequestInfo requestInfo;

	private Task task;

	private List<Task> tasks = new ArrayList<Task>();
}