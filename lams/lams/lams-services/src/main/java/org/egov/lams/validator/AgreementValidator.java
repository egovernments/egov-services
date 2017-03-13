package org.egov.lams.validator;

import java.util.Date;
import org.egov.lams.exception.LamsException;
import org.egov.lams.model.Agreement;

public class AgreementValidator {

	public static void validateAgreement(Agreement agreement) {

		Double rent = agreement.getRent();
		Double securityDeposit = agreement.getSecurityDeposit();
		Date solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		Date bankGuaranteeDate = agreement.getBankGuaranteeDate();

		// TODO remove hard coded value of rent*3

		if (securityDeposit.equals(rent * 3))
			throw new LamsException("security deposit value should be thrice rent value");

		if (solvencyCertificateDate.compareTo(new Date()) >= 0)
			throw new LamsException("solvency certificate date should be lesser than current date");

		if (bankGuaranteeDate.compareTo(new Date()) >= 0)
			throw new LamsException("bank Guarantee Date date should be lesser than current date");
	}
}
