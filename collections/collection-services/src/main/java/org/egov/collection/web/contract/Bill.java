package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Bill   {
	//TODO some of the fields are mandatory in yml, lets discuss billdetail and billaccountdetail also for more clarity
	private String id;

	@NotNull
	@Size(min = 1)
	private String payeeName;

	private String payeeAddress;

	private String payeeEmail;
	
	private Boolean isActive;

	private Boolean isCancelled;

	@NotNull
	@Size(min = 1)
    private String paidBy;

	@JsonProperty("billDetails")
	@Size(min = 1)
	@Valid
	private List<BillDetail> billDetails = new ArrayList<>(); //for billing-service

	@NotNull
	@Size(min = 1)
	private String tenantId;

	@NotNull
	@Size(min = 1)
    private String mobileNumber;
	
}

