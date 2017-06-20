package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import org.egov.demand.model.enums.Purpose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillAccountDetail {

	private Long id;

	private String glcode;

	private Integer order;

	private String accountDescription;

	private Double creditAmount;

	private Double debitAmount;

	private Boolean isActualDemand;

	private List<Purpose> purpose = new ArrayList<>();
}