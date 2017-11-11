package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.Department;
import org.egov.lcms.models.DepartmentResponse;
import org.egov.lcms.models.Opinion;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.repository.builder.OpinionQueryBuilder;
import org.egov.lcms.repository.rowmapper.OpinionRowMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

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
	DepartmentRepository departmentRepository;

	@Autowired
	AdvocateRepository advocateRepository;

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

		for (Opinion opinion : opinions) {
			if (opinion.getDepartmentName() != null)
				opinion.setDepartmentName(searchDepartments(opinion, requestInfoWrapper));
		}
		if (opinions != null) {
			setAdvocates(opinions, requestInfoWrapper);
		}
		return opinions;
	}

	private Department searchDepartments(Opinion opinion, RequestInfoWrapper requestInfoWrapper) throws Exception {
		DepartmentResponse departmentResponse = departmentRepository.getDepartments(opinion.getTenantId(),
				opinion.getDepartmentName().getCode(), requestInfoWrapper);
		if (departmentResponse.getDepartment() != null && departmentResponse.getDepartment().size() > 0)
			return departmentResponse.getDepartment().get(0);

		return null;
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
