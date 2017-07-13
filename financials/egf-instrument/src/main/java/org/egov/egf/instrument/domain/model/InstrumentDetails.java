package org.egov.egf.instrument.domain.model;

import java.math.BigDecimal;
import java.util.Date;

public class InstrumentDetails {
	
	
	 	private InstrumentHeader instrumentHeader;
	    private String payinSlipId;
	    private Date instrumentStatusDate;
	    private BigDecimal reconciledAmount;
	    private Date reconciledOn;
	    private String dishonorBankRefNo;

}
