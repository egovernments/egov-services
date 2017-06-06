package org.egov.wcms.web.contract;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DonationGetRequest {
	
	private List<Long> id;
	
	private String propertyType;
	private String categoryType;
    private String usageType;
	private double minHSCPipeSize;
	private double maxHSCPipeSize;
	private Date fromDate;
	private Date toDate;
	private boolean active;
    private String tenantId;

}
