package org.egov.works.estimate.domain.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.estimate.config.WorksEstimateServiceConstants;
import org.egov.works.estimate.domain.service.AbstractEstimateService;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.AbstractEstimate;
import org.egov.works.estimate.web.contract.AbstractEstimateDetails;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.egov.works.estimate.web.contract.BudgetGroup;
import org.egov.works.estimate.web.contract.Department;
import org.egov.works.estimate.web.contract.Function;
import org.egov.works.estimate.web.contract.Fund;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.Scheme;
import org.egov.works.estimate.web.contract.SubScheme;
import org.egov.works.estimate.web.contract.TypeOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

@Service
public class EstimateValidator {

	@Autowired
	private EstimateUtils estimateUtils;

	@Autowired
	private AbstractEstimateService abstractEstimateService;

	public void validateEstimates(AbstractEstimateRequest abstractEstimateRequest, Boolean isNew) {
		Map<String, String> messages = new HashMap<>();
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			validateSpillOverData(estimate, messages);
			validateMasterData(estimate, abstractEstimateRequest.getRequestInfo(), messages);

			AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
			if (estimate.getId() != null)
				searchContract.setIds(Arrays.asList(estimate.getId()));
			searchContract.setAbstractEstimateNumbers(Arrays.asList(estimate.getAbstractEstimateNumber()));
			searchContract.setTenantId(estimate.getTenantId());
			List<AbstractEstimate> oldEstimates = abstractEstimateService
					.search(searchContract, abstractEstimateRequest.getRequestInfo()).getAbstractEstimates();
			if (isNew && !oldEstimates.isEmpty())
				messages.put(WorksEstimateServiceConstants.KEY_UNIQUE_ABSTRACTESTIMATENUMBER,
						WorksEstimateServiceConstants.MESSAGE_UNIQUE_ABSTRACTESTIMATENUMBER);
			searchContract.setAbstractEstimateNumbers(null);
			if (!isNew && estimate.getWorkFlowDetails() != null && WorksEstimateServiceConstants.APPROVE
					.equalsIgnoreCase(estimate.getWorkFlowDetails().getAction())) {
				if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
					messages.put(WorksEstimateServiceConstants.KEY_NULL_ADMINSANCTIONNUMBER,
							WorksEstimateServiceConstants.MESSAGE_NULL_ADMINSANCTIONNUMBER);
				searchContract.setAdminSanctionNumbers(Arrays.asList(estimate.getAdminSanctionNumber()));
				oldEstimates = abstractEstimateService.search(searchContract, abstractEstimateRequest.getRequestInfo())
						.getAbstractEstimates();
				if (!oldEstimates.isEmpty() && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId()))
					messages.put(WorksEstimateServiceConstants.KEY_UNIQUE_ADMINSANCTIONNUMBER,
							WorksEstimateServiceConstants.MESSAGE_UNIQUE_ADMINSANCTIONNUMBER);
			}
			validateEstimateDetails(estimate, messages);

			if (messages != null && !messages.isEmpty())
				throw new CustomException(messages);
		}
	}

	private void validateSpillOverData(AbstractEstimate estimate, Map<String, String> messages) {
		if (estimate.getSpillOverFlag() && estimate.getAbstractEstimateNumber() == null)
			messages.put(WorksEstimateServiceConstants.KEY_NULL_ABSTRACTESTIMATE_NUMBER,
					WorksEstimateServiceConstants.MESSAGE_NULL_ABSTRACTESTIMATE_NUMBER);
		if (!estimate.getSpillOverFlag() && estimate.getDateOfProposal() != null
				&& new Date(estimate.getDateOfProposal()).after(new Date()))
			messages.put(WorksEstimateServiceConstants.KEY_FUTUREDATE_DATEOFPROPOSAL,
					WorksEstimateServiceConstants.MESSAGE_FUTUREDATE_DATEOFPROPOSAL);
	}

	private void validateEstimateDetails(AbstractEstimate estimate, Map<String, String> messages) {
		BigDecimal estimateAmount = BigDecimal.ZERO;
		for (final AbstractEstimateDetails aed : estimate.getAbstractEstimateDetails())
			estimateAmount = estimateAmount.add(aed.getEstimateAmount());
		if (estimateAmount.compareTo(BigDecimal.ZERO) == -1)
			messages.put(WorksEstimateServiceConstants.KEY_INVALID_ESTIMATEAMOUNT,
					WorksEstimateServiceConstants.MESSAGE_INVALID_ESTIMATEAMOUNT);
	}

	public void validateMasterData(AbstractEstimate abstractEstimate, RequestInfo requestInfo,
			Map<String, String> messages) {

		Boolean isFinIntReq = false;
		JSONArray responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.APPCONFIGURATION_OBJECT,
				CommonConstants.CODE, WorksEstimateServiceConstants.FINANCIAL_INTEGRATION_KEY,
				abstractEstimate.getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
		if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
			Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
			if (jsonMap.get("value").equals("Yes"))
				isFinIntReq = true;
		}

		if (isFinIntReq)
			validateFinancialDetails(abstractEstimate, requestInfo, messages);

		validateTypeOfWork(abstractEstimate.getTypeOfWork(), abstractEstimate.getTenantId(), requestInfo, messages);
		validateSubTypeOfWork(abstractEstimate.getSubTypeOfWork(), abstractEstimate.getTenantId(), requestInfo,
				messages);
		validateDepartment(abstractEstimate.getDepartment(), abstractEstimate.getTenantId(), requestInfo, messages);

		/*
		 * * if(abstractEstimate.getWard() != null &
		 * StringUtils.isNotBlank(abstractEstimate.getWard().getCode())) {
		 * responseJSONArray =
		 * estimateUtils.getAppConfigurationData(WorksEstimateServiceConstants.
		 * DEPARTMENT_OBJECT,abstractEstimate.getWard().getCode(),
		 * abstractEstimate.getTenantId(),requestInfo); if(responseJSONArray !=
		 * null && responseJSONArray.isEmpty()) { throw new
		 * InvalidDataException("Boundary",
		 * "Invalid data for estimate Ward boundary",
		 * abstractEstimate.getWard().getCode()); } }
		 * if(abstractEstimate.getLocality() != null &
		 * StringUtils.isNotBlank(abstractEstimate.getLocality().getCode())) {
		 * responseJSONArray =
		 * estimateUtils.getAppConfigurationData(WorksEstimateServiceConstants.
		 * DEPARTMENT_OBJECT,abstractEstimate.getLocality().getCode(),
		 * abstractEstimate.getTenantId(),requestInfo); if(responseJSONArray !=
		 * null && responseJSONArray.isEmpty()) { throw new
		 * InvalidDataException("Boundary",
		 * "Invalid data for estimate locality boundary",
		 * abstractEstimate.getLocality().getCode()); } }
		 */

	}

	private void validateFinancialDetails(AbstractEstimate abstractEstimate, RequestInfo requestInfo,
			Map<String, String> messages) {
		validateFund(abstractEstimate.getFund(), abstractEstimate.getTenantId(), requestInfo, messages);
		validateFunction(abstractEstimate.getFunction(), abstractEstimate.getTenantId(), requestInfo, messages);
		validateScheme(abstractEstimate.getScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
		validateSubScheme(abstractEstimate.getSubScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
		validateBudgetGroup(abstractEstimate.getBudgetGroup(), abstractEstimate.getTenantId(), requestInfo, messages);
	}

	public void validateFund(Fund fund, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (fund != null && fund.getCode() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUND_OBJECT,
					CommonConstants.CODE, fund.getCode(), tenantId, requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_FUND_INVALID,
						WorksEstimateServiceConstants.MESSAGE_FUND_INVALID);
			}
		}
	}

	public void validateFunction(Function function, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (function != null && function.getCode() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUNCTION_OBJECT,
					CommonConstants.CODE, function.getCode(), tenantId, requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_FUNCTION_INVALID,
						WorksEstimateServiceConstants.MESSAGE_FUNCTION_INVALID);
			}
		}
	}

	public void validateScheme(Scheme scheme, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (scheme != null && scheme.getCode() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SCHEME_OBJECT,
					CommonConstants.CODE, scheme.getCode(), tenantId, requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_SCHEME_INVALID,
						WorksEstimateServiceConstants.MESSAGE_SCHEME_INVALID);
			}
		}
	}

	public void validateSubScheme(SubScheme subScheme, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (subScheme != null && subScheme.getCode() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SUBSCHEME_OBJECT,
					CommonConstants.CODE, subScheme.getCode(), tenantId, requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_SUBSCHEME_INVALID,
						WorksEstimateServiceConstants.MESSAGE_SUBSCHEME_INVALID);
			}
		}
	}

	public void validateBudgetGroup(BudgetGroup budgetGroup, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (budgetGroup != null && budgetGroup.getName() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.BUDGETGROUP_OBJECT,
					CommonConstants.NAME, budgetGroup.getName(), tenantId, requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_BUDGETGROUP_INVALID,
						WorksEstimateServiceConstants.MESSAGE_BUDGETGROUP_INVALID);
			}
		}
	}

	public void validateTypeOfWork(TypeOfWork typeOfWork, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (typeOfWork != null && typeOfWork.getName() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.TYPEOFWORK_OBJECT,
					CommonConstants.CODE, typeOfWork.getCode(), tenantId, requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_TYPEOFWORK_INVALID,
						WorksEstimateServiceConstants.MESSAGE_TYPEOFWORK_INVALID);
			}
		}
	}

	public void validateSubTypeOfWork(TypeOfWork subTypeOfWork, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (subTypeOfWork != null && subTypeOfWork.getName() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.TYPEOFWORK_OBJECT,
					CommonConstants.CODE, subTypeOfWork.getCode(), tenantId, requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_SUBTYPEOFWORK_INVALID,
						WorksEstimateServiceConstants.MESSAGE_SUBTYPEOFWORK_INVALID);
			}
		}
	}

	public void validateDepartment(Department department, String tenantId, RequestInfo requestInfo,
			Map<String, String> messages) {
		JSONArray responseJSONArray;
		if (department != null && department.getName() != null) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.DEPARTMENT_OBJECT,
					CommonConstants.CODE, department.getCode(), tenantId, requestInfo,
					WorksEstimateServiceConstants.COMMON_MASTERS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_DEPARTMENT_INVALID,
						WorksEstimateServiceConstants.MESSAGE_DEPARTMENT_INVALID);
			}
		}
	}
}
