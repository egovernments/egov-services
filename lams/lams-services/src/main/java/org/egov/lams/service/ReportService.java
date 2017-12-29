package org.egov.lams.service;

import java.util.List;

import org.egov.lams.model.AgreementInfo;
import org.egov.lams.repository.ReportRepository;
import org.egov.lams.web.contract.BaseRegisterRequest;
import org.egov.lams.web.contract.RenewalPendingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service
public class ReportService {
	public static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	private ReportRepository reportRepository;

	public List<AgreementInfo> getAgreementDetails(BaseRegisterRequest baseRegisterRequest) {
		List<AgreementInfo> agreementList = reportRepository.getAgreementsForBaseRegister(baseRegisterRequest);

		log.info("Size of the agreements" + agreementList.size());
		return agreementList;
	}

	public List<AgreementInfo> getAgreementRenewalPendingDetails(RenewalPendingRequest renewalPendingRequest) {
		List<AgreementInfo> agreementList = reportRepository.getRenewalPendingAgreements(renewalPendingRequest);

		log.info("Size of the agreements" + agreementList.size());
		return agreementList;
	}

}
