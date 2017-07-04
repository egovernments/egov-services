package org.egov.lams.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class AgreementIndex {

	private AgreementDetailsEs agreementDetails;
		
	private List<DemandDetailsEs> demandDetailsEs = new ArrayList<>();
	
}
