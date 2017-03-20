package org.egov.demand.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgBill;
import org.egov.demand.persistence.entity.EgBillDetails;
import org.egov.demand.persistence.entity.EgDemand;
import org.egov.demand.persistence.entity.EgDemandDetails;
import org.springframework.stereotype.Service;

@Service
public class BillDetailsService {
	private static final Logger LOGGER = Logger.getLogger(BillDetailsService.class);

	public List<EgBillDetails> createBillDetails(EgDemand demand, EgBill bill) {
		List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
		EgBillDetails billDetail;
		BigDecimal balance = BigDecimal.ZERO;
		for (EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
			balance = demandDetail.getBalance();
			if (balance.compareTo(BigDecimal.ZERO) > 0) {
				billDetail = new EgBillDetails();
				billDetail.setCreateDate(new Date());
				billDetail.setModifiedDate(new Date());
				billDetail.setCrAmount(balance);
				billDetail.setDrAmount(BigDecimal.ZERO);
				billDetail.setAdditionalFlag(Integer.valueOf(1));
				billDetail.setDescription(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
						+ " - " + demandDetail.getEgDemandReason().getEgInstallmentMaster().getDescription());
				billDetail.setGlcode("1100101");
				billDetail.setFunctionCode("9100");
				billDetail.setEgBill(bill);

				billDetail.setOrderNo(1);
				// billDetail.setEgInstallmentMaster(currInstallment);
				// billDetail.setPurpose(PURPOSE.OTHERS.toString());
			}
		}
		return billDetails;
	}
}
