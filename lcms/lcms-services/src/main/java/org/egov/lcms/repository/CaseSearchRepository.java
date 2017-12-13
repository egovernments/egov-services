package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateDetails;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseDetails;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.CaseStatus;
import org.egov.lcms.models.CaseVoucher;
import org.egov.lcms.models.Event;
import org.egov.lcms.models.EventSearchCriteria;
import org.egov.lcms.models.HearingDetails;
import org.egov.lcms.models.ParaWiseComment;
import org.egov.lcms.models.ReferenceEvidence;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.repository.builder.CaseBuilder;
import org.egov.lcms.repository.rowmapper.AdvocateDetailsRowMapper;
import org.egov.lcms.repository.rowmapper.CaseDetailsRowMapper;
import org.egov.lcms.repository.rowmapper.CaseRowMapper;
import org.egov.lcms.repository.rowmapper.CaseVoucherRowMapper;
import org.egov.lcms.repository.rowmapper.EventRowMapper;
import org.egov.lcms.repository.rowmapper.EvidenceRowMapper;
import org.egov.lcms.repository.rowmapper.HearingDetailsRowMapper;
import org.egov.lcms.repository.rowmapper.ParaWiseRowMapper;
import org.egov.lcms.util.ConstantUtility;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

/**
 * 
 * @author Prasad
 *
 */
@Repository
@Slf4j
public class CaseSearchRepository {

	@Autowired
	CaseBuilder caseBuilder;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CaseRowMapper caseRowMapper;

	@Autowired
	ParaWiseRowMapper paraWiseRowMapper;

	@Autowired
	HearingDetailsRowMapper hearingDetailsRowMapper;

	@Autowired
	AdvocateDetailsRowMapper advocateDetailsRowMapper;

	@Autowired
	CaseVoucherRowMapper caseVoucherRowMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	AdvocateRepository advocateRepository;

	@Autowired
	EvidenceRowMapper evidenceRowMapper;

	@Autowired
	CaseDetailsRowMapper caseDetailsRowMapper;

	@Autowired
	EventRowMapper eventRowMapper;

	@Autowired
	MdmsRepository mdmsRepository;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * This will search the cases based on the given casesearchCriteria
	 * 
	 * @param caseSearchCriteria
	 * @param requestInfo
	 * @return case list
	 * @throws Exception
	 */
	public List<Case> searchCases(CaseSearchCriteria caseSearchCriteria, RequestInfo requestInfo) throws Exception {

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.getQuery(caseSearchCriteria, preparedStatementValues);

		List<Case> cases = new ArrayList<Case>();
		try {
			cases = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), caseRowMapper);
		} catch (final Exception exception) {
			log.info("the exception in case search :" + exception);
			throw new CustomException(propertiesManager.getCaseResponseErrorCode(), exception.getMessage());
		}

		for (Case casee : cases) {
			if (caseSearchCriteria.getSearchResultLevel() != null) {
				casee.setParawiseComments(searchParaWiseComments(casee));
				casee.setHearingDetails(searchHearingDetails(casee));
				casee.setReferenceEvidences(searchRefernceEvidence(casee));
			} else {

				Map<String, String> masterMap = new HashMap<>();
				RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
				requestInfoWrapper.setRequestInfo(requestInfo);

				List<CaseStatus> caseStatusList = new ArrayList<CaseStatus>();

				if (casee.getCaseStatus() != null && casee.getCaseStatus().getCode() != null) {

					masterMap.put("caseStatus", casee.getCaseStatus().getCode());

					MdmsResponse mdmsResponse = mdmsRepository.getMasterData(casee.getTenantId(), masterMap,
							requestInfoWrapper, propertiesManager.getLcmsModuleName());

					Map<String, Map<String, JSONArray>> response = mdmsResponse.getMdmsRes();

					Map<String, JSONArray> mastersmap = response.get(propertiesManager.getLcmsModuleName());

					for (String key : mastersmap.keySet()) {

						String masterName = key;

						if (masterName == "caseStatus") {
							caseStatusList = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
									new TypeReference<List<CaseStatus>>() {
									});
						}
					}

					if (caseStatusList != null && caseStatusList.size() > 0)
						casee.setCaseStatus(caseStatusList.get(0));
				}
			}
			casee.setAdvocateDetails(searchAdvocateDetails(casee));

			// casee.setCaseVoucher(searchCaseVoucher(casee));

		}

		return cases;
	}

	private List<ParaWiseComment> searchParaWiseComments(Case casee) {
		List<ParaWiseComment> paraWiseComments = new ArrayList<ParaWiseComment>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchByCaseCodeQuery(casee, ConstantUtility.PARAWISE_COMMENTS_TABLE_NAME,
				preparedStatementValues);

		try {
			paraWiseComments = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), paraWiseRowMapper);
		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getParaWiseResponseErrorCode(), ex.getMessage());
		}
		return paraWiseComments;
	}

	private List<HearingDetails> searchHearingDetails(Case caseObj) {
		List<HearingDetails> hearingDetails = new ArrayList<HearingDetails>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchByCaseCodeQuery(caseObj, ConstantUtility.HEARING_DETAILS_TABLE_NAME,
				preparedStatementValues);

		try {
			hearingDetails = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), hearingDetailsRowMapper);
		} catch (Exception ex) {
			log.info("Error occured in getting hearing details " + ex.getMessage());
			throw new CustomException(propertiesManager.getHearingDetailsResponseErrorCode(), ex.getMessage());
		}
		return hearingDetails;
	}

	/**
	 * 
	 * @param casee
	 * @return
	 */
	private List<AdvocateDetails> searchAdvocateDetails(Case casee) {
		List<AdvocateDetails> advocateDetails = new ArrayList<>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchByCaseCodeQuery(casee, ConstantUtility.ADVOCATE_DETAILS_TABLE_NAME,
				preparedStatementValues);

		try {
			advocateDetails = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), advocateDetailsRowMapper);
		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getAdvocateDetailsResponseErrorCode(), ex.getMessage());
		}
		setAdvocate(advocateDetails);

		return advocateDetails;
	}

	/**
	 * This Api will set the advocate details based on the given advocateDetails
	 * 
	 * @param advocateDetails
	 */
	private void setAdvocate(List<AdvocateDetails> advocateDetails) {

		for (AdvocateDetails advocateDetail : advocateDetails) {
			AdvocateSearchCriteria advocateSearchCriteria = new AdvocateSearchCriteria();
			advocateSearchCriteria.setTenantId(advocateDetail.getTenantId());
			if (advocateDetail.getAdvocate().getCode() != null && !advocateDetail.getAdvocate().getCode().isEmpty()) {
				advocateSearchCriteria.setCode(new String[] { advocateDetail.getAdvocate().getCode() });
				List<Advocate> advocates = advocateRepository.search(advocateSearchCriteria);
				if (advocates != null && advocates.size() > 0)
					advocateDetail.setAdvocate(advocates.get(0));
			}
		}
	}

	private CaseVoucher searchCaseVoucher(Case casee) {
		List<CaseVoucher> caseVouchers = new ArrayList<>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchByCaseCodeQuery(casee, ConstantUtility.CASE_VOUCHER_TABLE_NAME,
				preparedStatementValues);

		try {
			caseVouchers = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), caseVoucherRowMapper);
		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getCaseVoucherResponseErrorCode(), ex.getMessage());
		}
		if (caseVouchers != null && caseVouchers.size() > 0)
			return caseVouchers.get(0);

		return null;
	}

	private List<ReferenceEvidence> searchRefernceEvidence(Case casee) {
		List<ReferenceEvidence> referenceEvidences = new ArrayList<>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchByCaseCodeQuery(casee, ConstantUtility.REFERENCE_EVIDENCE_TABLE_NAME,
				preparedStatementValues);

		try {
			referenceEvidences = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), evidenceRowMapper);
		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getEvidenceResponseErrorCode(),
					propertiesManager.getEvidenceResponseErrorMsg());
		}
		if (referenceEvidences != null && referenceEvidences.size() > 0)
			return referenceEvidences;

		return null;
	}

	public List<CaseDetails> getCaseDetails(String tenantId, String advocateName) {
		List<CaseDetails> caseDetails = new ArrayList<>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchCaseDetailsByTenantId(tenantId, advocateName,
				ConstantUtility.CASE_TABLE_NAME, preparedStatementValues);

		try {
			caseDetails = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), caseDetailsRowMapper);
		} catch (Exception ex) {
			log.info("the exception is :" + ex.getMessage());
			throw new CustomException(propertiesManager.getCaseDetailsResponseErrorCode(),
					propertiesManager.getCaseDetailsResponseErrorMsg());
		}

		if (caseDetails != null && caseDetails.size() > 0)
			return caseDetails;

		return null;
	}

	public List<Event> getEvent(EventSearchCriteria eventSearchCriteria) {
		List<Event> events = new ArrayList<Event>();
		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String eventSearchQuery = caseBuilder.searchEvent(eventSearchCriteria, preparedStatementValues);

		try {
			events = jdbcTemplate.query(eventSearchQuery, preparedStatementValues.toArray(), eventRowMapper);
		} catch (Exception ex) {
			log.info("the exception is :" + ex.getMessage());
			throw new CustomException(propertiesManager.getEventResponseErrorCode(),
					propertiesManager.getEventResponseErrorMsg());
		}

		if (events != null && events.size() > 0)
			return events;

		return null;
	}

	public List<Map<String, Object>> searchHearingDetailsQuery(String code, String tenantId) {

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String queryStr = caseBuilder.searchHearingDetails(code, tenantId,
				ConstantUtility.HEARING_DETAILS_TABLE_NAME, preparedStatementValues);

		List<Map<String, Object>> hearingDateValues = new ArrayList<>();

		try {

			hearingDateValues = jdbcTemplate.queryForList(queryStr, preparedStatementValues.toArray());
		} catch (Exception ex) {
			log.info("the exception is :" + ex.getMessage());
			throw new CustomException(propertiesManager.getCaseDetailsResponseErrorCode(),
					propertiesManager.getCaseDetailsResponseErrorMsg());
		}
		return hearingDateValues != null ? hearingDateValues : null;
	}
}
