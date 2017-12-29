package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.AgreementInfo;
import org.egov.lams.repository.builder.ReportQueryBuilder;
import org.egov.lams.repository.rowmapper.AgreementInfoRowMapper;
import org.egov.lams.web.contract.BaseRegisterRequest;
import org.egov.lams.web.contract.RenewalPendingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ReportRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ReportQueryBuilder reportQueryBuilder;

	public List<AgreementInfo> getAgreementsForBaseRegister(BaseRegisterRequest baseRegisterCriteria) {
		Map<String, Object> namedParameters = new HashMap<>();
		List<AgreementInfo> agreementInfo = new ArrayList<>();
		String queryStr = reportQueryBuilder.getQueryForBaseRegisterReport(baseRegisterCriteria, namedParameters);
		try {
			agreementInfo = namedParameterJdbcTemplate.query(queryStr, namedParameters, new AgreementInfoRowMapper());
		} catch (EmptyResultDataAccessException e) {
			log.info("exception occured in base resister report" + e);
		}
		return agreementInfo;
	}

	public List<AgreementInfo> getRenewalPendingAgreements(RenewalPendingRequest renewalPendingCriteria) {
		Map<String, Object> namedParameters = new HashMap<>();
		List<AgreementInfo> agreementInfo = new ArrayList<>();
		String queryStr = reportQueryBuilder.getQueryForRenewalPendingReport(renewalPendingCriteria, namedParameters);
		try {
			agreementInfo = namedParameterJdbcTemplate.query(queryStr, namedParameters, new AgreementInfoRowMapper());
		} catch (EmptyResultDataAccessException e) {
			log.info("exception occured in renewal pending resister report" + e);
		}
		return agreementInfo;
	}

}
