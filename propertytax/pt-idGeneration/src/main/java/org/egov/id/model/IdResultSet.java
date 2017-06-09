package org.egov.id.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Model IdResultSet Model
 * @author Pavan Kumar Kamma
 */
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IdResultSet {

	private String id;

	private String idtype;

	private String entity;

	private String tenentid;

	private String regex;

	private String currentseqnum;

}
