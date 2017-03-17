package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bill {
	private Long id = null;
	private Long demandId = null;
	private String citizenName = null;
	private String citizenAddress = null;
	private String billNumber = null;
	private String billType = null;
	private Date issuedDate = null;
	private Date lastDate = null;
	private String moduleName = null;
	private String createdBy = null;
	private String history = null;
	private String cancelled = null;
	private String fundCode = null;
	private Long functionaryCode = null;
	private String fundSourceCode = null;
	private String departmentCode = null;
	private String collModesNotAllowed = null;
	private Integer boundaryNumber = null;
	private String boundaryType = null;
	private Double billAmount = null;
	private Double billAmountCollected = null;
	private String serviceCode = null;
	private Boolean partPaymentAllowed = null;
	private Boolean overrideAccHeadAllowed = null;
	private String description = null;
	private Double minAmountPayable = null;
	private String consumerCode = null;
	private String displayMessage = null;
	private Boolean callbackForApportion = null;
	private String emailId = null;
	private String consumerType = null;
	private List<BillDetail> billDetails = new ArrayList<BillDetail>();

}
