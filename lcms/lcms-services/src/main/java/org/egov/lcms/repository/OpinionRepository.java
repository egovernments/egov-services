package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.Department;
import org.egov.lcms.models.Opinion;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.repository.builder.OpinionQueryBuilder;
import org.egov.lcms.repository.rowmapper.OpinionRowMapper;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

/**
 * 
 * @author Veswanth
 *
 */
@Service
@Slf4j
public class OpinionRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private OpinionRowMapper opinionRowMapper;

	@Autowired
	OpinionQueryBuilder opinionBuilder;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	AdvocateRepository advocateRepository;

	@Autowired
	MdmsRepository mdmsRepository;

	@Autowired
	ObjectMapper objectMapper;

	public List<Opinion> search(OpinionSearchCriteria opinionSearch, RequestInfoWrapper requestInfoWrapper)
			throws Exception {

		final List<Object> preparedStatementValues = new ArrayList<Object>();

		String searchQuery = opinionBuilder.getOpinionSearchQuery(opinionSearch, preparedStatementValues);
		List<Opinion> opinions = new ArrayList<Opinion>();
		try {
			opinions = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(), opinionRowMapper);
			getCaseNoWithSummonReference(opinions);
		} catch (Exception ex) {
			log.info("the exception in opinion :" + ex.getMessage());
			throw new CustomException(propertiesManager.getOpinionSearchErrorCode(), ex.getMessage());
		}

		searchDepartments(opinions, opinions.get(0).getTenantId(), requestInfoWrapper);

		if (opinions != null && opinions.size() > 0) {
			setAdvocates(opinions, requestInfoWrapper);
		}
		return opinions;
	}

	private void searchDepartments(List<Opinion> opinions, String tenantId, RequestInfoWrapper requestInfoWrapper)
			throws Exception {

		List<String> opinionCodes = opinions.stream()
				.filter(data -> data.getDepartmentName() != null && data.getDepartmentName().getCode() != null)
				.map(opinionCode -> opinionCode.getDepartmentName().getCode()).collect(Collectors.toList());

		Map<String, String> masterCodeAndValue = new HashMap<String, String>();
		String departmentCode = mdmsRepository
				.getCommaSepratedValues(opinionCodes.toArray(new String[opinionCodes.size()]));

		if (departmentCode != null && !departmentCode.isEmpty()) {
			masterCodeAndValue.put("Department", departmentCode);
			MdmsResponse mdmsResponse = mdmsRepository.getMasterData(tenantId, masterCodeAndValue, requestInfoWrapper,
					propertiesManager.getCommonMasterModuleName());
			Map<String, Map<String, JSONArray>> response = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mastersmap = response.get("common-masters");

			for (Opinion opinion : opinions) {
				if (opinion.getDepartmentName() != null) {
					addParticularMastervalues("Department", opinion, mastersmap);
				}
			}
		}
	}

	private void addParticularMastervalues(String masterName, Opinion opinion, Map<String, JSONArray> mastersmap)
			throws Exception {
		if (mastersmap.get(masterName) != null) {
			List<Department> departments = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
					new TypeReference<List<Department>>() {
					});
			if (departments != null) {
				List<Department> departmentList = departments.stream().filter(
						department -> department.getCode().equalsIgnoreCase(opinion.getDepartmentName().getCode()))
						.collect(Collectors.toList());
				if (departmentList != null && departmentList.size() > 0)
					opinion.setDepartmentName((departmentList.get(0)));
			}
		}
	}

	private void setAdvocates(List<Opinion> opinions, RequestInfoWrapper requestInfoWrapper) {

		List<String> codes = opinions.stream()
				.filter(opinionData -> opinionData.getOpinionsBy() != null
						&& opinionData.getOpinionsBy().getCode() != null)
				.map(advocateCode -> advocateCode.getOpinionsBy().getCode()).collect(Collectors.toList());

		AdvocateSearchCriteria advocateSearch = new AdvocateSearchCriteria();
		advocateSearch.setTenantId(opinions.get(0).getTenantId());
		advocateSearch.setCode(codes.toArray(new String[codes.size()]));

		List<Advocate> advocates = advocateRepository.search(advocateSearch);

		for (Opinion opinion : opinions) {
			if (opinion.getOpinionsBy() != null) {
				Advocate advocate = advocates.stream()
						.filter(adv -> adv.getCode().equalsIgnoreCase(opinion.getOpinionsBy().getCode()))
						.collect(Collectors.toList()).get(0);
				opinion.setOpinionsBy(advocate);

			}
		}
	}

	private void getCaseNoWithSummonReference(List<Opinion> opinions) {

		for (Opinion opinion : opinions) {
			if (opinion.getCaseDetails() != null && opinion.getCaseDetails().getSummonReferenceNo() != null) {

				if (!opinion.getCaseDetails().getSummonReferenceNo().isEmpty()) {
					final List<Object> preparedStatementValues = new ArrayList<Object>();
					String searchQuery = opinionBuilder.getCaseNo(opinion.getCaseDetails().getSummonReferenceNo(),
							opinion.getTenantId(), preparedStatementValues);
					try {

						String caseNo = jdbcTemplate.queryForObject(searchQuery, preparedStatementValues.toArray(),
								String.class);
						opinion.getCaseDetails().setCaseNo(caseNo);
					} catch (Exception ex) {

						log.info("the exception in opinion :" + ex.getMessage());
						throw new CustomException(propertiesManager.getCaseNoErrorCode(),
								propertiesManager.getCaseNoErrorMsg());
					}
				}
			}
		}
	}
}
