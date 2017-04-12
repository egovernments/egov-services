package org.egov.eis.indexer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class PropertiesManager {

	@Value("${egov.user.service.host}")
	private String egovUserServiceHost;

	@Value("${egov.user.service.user.basepath}")
	private String egovUserServiceUserBasepath;

	@Value("${egov.user.service.user.search.path}")
	private String egovUserServiceUserSearchPath;

	@Value("${egov.common.masters.service.host}")
	private String egovCommonMastersServiceHost;

	@Value("${egov.common.masters.service.basepath}")
	private String egovCommonMastersServiceBasepath;

	@Value("${egov.common.masters.service.language.search.path}")
	private String egovCommonMastersServiceLanguageSearchPath;

	@Value("${egov.common.masters.service.religion.search.path}")
	private String egovCommonMastersServiceReligionSearchPath;

	@Value("${egov.common.masters.service.category.search.path}")
	private String egovCommonMastersServiceCategorySearchPath;

	@Value("${egov.common.masters.service.community.search.path}")
	private String egovCommonMastersServiceCommunitySearchPath;

	@Value("${egov.common.masters.service.department.search.path}")
	private String egovCommonMastersServiceDepartmentSearchPath;

	@Value("${hr.masters.service.host}")
	private String hrMastersServiceHost;

	@Value("${hr.masters.service.basepath}")
	private String hrMastersServiceBasepath;

	@Value("${hr.masters.service.employeetype.search.path}")
	private String hrMastersServiceEmployeeTypeSearchPath;

	@Value("${hr.masters.service.recruitmentmode.search.path}")
	private String hrMastersServiceRecruitmentModeSearchPath;

	@Value("${hr.masters.service.recruitmentquota.search.path}")
	private String hrMastersServiceRecruitmentQuotaSearchPath;

	@Value("${hr.masters.service.recruitmenttype.search.path}")
	private String hrMastersServiceRecruitmentTypeSearchPath;

	@Value("${hr.masters.service.grade.search.path}")
	private String hrMastersServiceGradeSearchPath;

	@Value("${hr.masters.service.group.search.path}")
	private String hrMastersServiceGroupSearchPath;

	@Value("${hr.masters.service.position.search.path}")
	private String hrMastersServicePositionSearchPath;

	@Value("${hr.masters.service.designation.search.path}")
	private String hrMastersServiceDesignationSearchPath;

	@Value("${egov.location.service.host}")
	private String egovLocationServiceHost;

	@Value("${egov.location.service.basepath}")
	private String egovLocationServiceBasepath;

	@Value("${egov.location.service.boundary.search.path}")
	private String egovLocationServiceBoundarySearchPath;

	@Value("${egov.location.service.boundarytype.search.path}")
	private String egovLocationServiceBoundaryTypeSearchPath;

	@Value("${financials.egf.masters.service.host}")
	private String financialsEgfMastersServiceHost;

	@Value("${financials.egf.masters.service.basepath}")
	private String financialsEgfMastersServiceBasepath;

	@Value("${financials.egf.masters.service.bank.search.path}")
	private String financialsEgfMastersServiceBankSearchPath;

	@Value("${financials.egf.masters.service.bankbranch.search.path}")
	private String financialsEgfMastersServiceBankBranchSearchPath;

	@Value("${financials.egf.masters.service.fund.search.path}")
	private String financialsEgfMastersServiceFundSearchPath;

	@Value("${financials.egf.masters.service.function.search.path}")
	private String financialsEgfMastersServiceFunctionSearchPath;

	@Value("${financials.egf.masters.service.functionary.search.path}")
	private String financialsEgfMastersServiceFunctionarySearchPath;

	@Value("${kafka.topics.employee.esindex.savedb.name}")
	private String saveEmployeeIndexerTopic;

	@Value("${kafka.topics.employee.esindex.updatedb.name}")
	private String updateEmployeeIndexerTopic;

}
