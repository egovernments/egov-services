package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * TaskResponse class
 * 
 * @author veswanth
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskResponse {

	private ResponseInfo responseInfo;

	private Task task;

	private List<Task> tasks = new ArrayList<Task>();

	private Page page;
}