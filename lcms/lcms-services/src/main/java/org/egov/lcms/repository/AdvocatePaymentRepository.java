package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocatePayment;
import org.egov.lcms.models.AdvocatePaymentSearchCriteria;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseStatus;
import org.egov.lcms.models.CaseType;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.repository.builder.AdvocatePaymentQueryBuilder;
import org.egov.lcms.repository.rowmapper.AdvocatePaymentRowMapper;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Shubham Pratap
 *
 */

@Repository
@Slf4j
public class AdvocatePaymentRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AdvocatePaymentRowMapper advocatePaymentRowMapper;

	@Autowired
	private AdvocatePaymentQueryBuilder advocatePaymentQueryBuilder;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	MdmsRepository mdmsRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private AdvocateRepository advocateRepository;

	public List<AdvocatePayment> search(final AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria,
			RequestInfo requestInfo) throws Exception {

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = advocatePaymentQueryBuilder.getQuery(advocatePaymentSearchCriteria,
				preparedStatementValues);

		List<AdvocatePayment> advocatePayments = new ArrayList<AdvocatePayment>();
		try {
			advocatePayments = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
					advocatePaymentRowMapper);
		} catch (final Exception exception) {
			log.info("Exception in advocate payment :" + exception.getMessage());
			throw new CustomException(propertiesManager.getPaymentSearchErrorCode(), exception.getMessage());
		}

		setAdvocate(advocatePayments, advocatePaymentSearchCriteria.getTenantId());
		setCaseDetails(advocatePayments, advocatePaymentSearchCriteria.getTenantId(), requestInfo);

		return advocatePayments;
	}

	private void setCaseDetails(List<AdvocatePayment> advocatePayments, String tenantId, RequestInfo requestInfo) throws Exception {

		RequestInfoWrapper requestWrapper = new RequestInfoWrapper();
		requestWrapper.setRequestInfo(requestInfo);

		

		List<String> caseTypeCodes = advocatePayments.stream()
				.filter(advocateData -> advocateData.getCaseType() != null
						&& advocateData.getCaseType().getCode() != null)
				.map(advocateCode -> advocateCode.getCaseType().getCode()).collect(Collectors.toList());
		
		List<String> caseStatusCodes = advocatePayments.stream()
				.filter(caseStatusData -> caseStatusData.getCaseStatus() != null
						&& caseStatusData.getCaseStatus().getCode() != null)
				.map(advocateCode -> advocateCode.getCaseStatus().getCode()).collect(Collectors.toList());
		
		

		Map<String, String> masterCodeAndValue = new HashMap<String, String>();
		String caseTypesCode = getCommaSepratedValues(caseTypeCodes.toArray(new String[caseTypeCodes.size()]));
		String caseStatusesCode = getCommaSepratedValues(caseStatusCodes.toArray(new String[caseStatusCodes.size()]));

		if (!caseTypesCode.isEmpty()) {
			masterCodeAndValue.put("caseType", caseTypesCode);
		}

		if (!caseStatusesCode.isEmpty()) {
			masterCodeAndValue.put("caseStatus", caseStatusesCode);
		}
		
		MdmsResponse mdmsResponse = mdmsRepository.getMasterData(tenantId, masterCodeAndValue, requestWrapper);
		Map<String, Map<String, JSONArray>> response = mdmsResponse.getMdmsRes();
		Map<String, JSONArray> mastersmap = response.get("lcms");
		
		for(AdvocatePayment advocatePayment : advocatePayments){
			addParticularMastervalues("caseType", advocatePayment, mastersmap);
			addParticularMastervalues("caseStatus", advocatePayment, mastersmap);
		}
		
		

	}

	private void setAdvocate(List<AdvocatePayment> advocatePayments, String tenantId) {

		List<String> codes = advocatePayments.stream()
				.filter(advocateData -> advocateData.getAdvocate() != null
						&& advocateData.getAdvocate().getCode() != null)
				.map(advocateCode -> advocateCode.getAdvocate().getCode()).collect(Collectors.toList());

		AdvocateSearchCriteria advocateSearch = new AdvocateSearchCriteria();
		advocateSearch.setTenantId(tenantId);
		advocateSearch.setCode(codes.toArray(new String[codes.size()]));

		List<Advocate> advocates = advocateRepository.search(advocateSearch);

		for (AdvocatePayment advocatePayment : advocatePayments) {
			if (advocatePayment.getAdvocate() != null && advocatePayment.getAdvocate().getCode() != null) {
				Advocate advocate = advocates.stream()
						.filter(adv -> adv.getCode().equalsIgnoreCase(advocatePayment.getAdvocate().getCode()))
						.collect(Collectors.toList()).get(0);
				advocatePayment.setAdvocate(advocate);
			}
		}
	}

	private static  String getCommaSepratedValues(String[] code) {

		if (code.length > 0) {
			StringBuilder nameBuilder = new StringBuilder();

			for (String n : code) {
				nameBuilder.append(n).append(",");
			}

			nameBuilder.deleteCharAt(nameBuilder.length() - 1);

			return nameBuilder.toString();
		} else {
			return "";
		}
	}

	private void addParticularMastervalues(String masterName, AdvocatePayment advocatePayment,
			Map<String, JSONArray> mastersmap) throws Exception {

		switch (masterName) {

		case "caseStatus": {
			List<CaseStatus> caseStatuses = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
					new TypeReference<List<CaseStatus>>() {
					});
			List<CaseStatus> caseStatusList = caseStatuses.stream().filter(
					CaseStatus -> CaseStatus.getCode().equalsIgnoreCase(advocatePayment.getCaseStatus().getCode()))
					.collect(Collectors.toList());
			if (caseStatusList != null && caseStatusList.size() > 0)
				advocatePayment.setCaseStatus((caseStatusList.get(0)));
			break;
		}

		case "caseType": {
			List<CaseType> caseTypes = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
					new TypeReference<List<CaseType>>() {
					});
	
		List<CaseType> caseTypesList = caseTypes.stream().filter(
				CaseType -> CaseType.getCode().equalsIgnoreCase(advocatePayment.getCaseType().getCode()))
				.collect(Collectors.toList());
		if (caseTypesList != null && caseTypesList.size() > 0)
			advocatePayment.setCaseType(caseTypesList.get(0));
			break;
		}
		default:
			break;
		}

	}

}