package org.egov.lams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationCategory {
	
	private Long id;
	private String code;
	private String name;
	private Boolean isactive;
	private String tenantId;
	

}
