package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TaskResponse class
 * 
 * @author veswanth
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskResponse {

	private ResponseInfo responseInfo;

	private Task task;

	private List<Task> tasks = new ArrayList<Task>();

	private Page page;
}