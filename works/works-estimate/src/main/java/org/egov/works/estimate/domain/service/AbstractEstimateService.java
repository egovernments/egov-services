package org.egov.works.estimate.domain.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.domain.model.AuditDetails;
import org.egov.works.commons.web.contract.RequestInfo;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.config.WorksEstimateServiceConstants;
import org.egov.works.estimate.domain.exception.ErrorCode;
import org.egov.works.estimate.domain.exception.InvalidDataException;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.egov.works.estimate.web.contract.DetailedEstimateRequest;
import org.egov.works.estimate.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class AbstractEstimateService {

	@Autowired
	private AbstractEstimateRepository abstractEstimateRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	@Autowired
	private EstimateUtils estimateUtils;

	@Transactional
	public List<AbstractEstimate> create(AbstractEstimateRequest abstractEstimateRequest) {
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			estimate.setId(UUID.randomUUID().toString().replace("-", ""));
			estimate.setAuditDetails(
					setAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUsername(), false));
			String abstractEstimateNumber = idGenerationRepository
					.generateAbstractEstimateNumber(estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
			estimate.setAbstractEstimateNumber("AE/" + estimate.getDepartment().getCode() + abstractEstimateNumber);
			for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails()) {
				details.setId(UUID.randomUUID().toString().replace("-", ""));
				details.setAuditDetails(
						setAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUsername(), false));
			}
		}
		kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateCreateTopic(), abstractEstimateRequest);
		return abstractEstimateRequest.getAbstractEstimates();
	}

	public List<AbstractEstimate> update(AbstractEstimateRequest abstractEstimateRequest) {
		kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateUpdateTopic(), abstractEstimateRequest);
		return abstractEstimateRequest.getAbstractEstimates();
	}

	public List<AbstractEstimate> search(AbstractEstimateSearchContract abstractEstimateSearchContract) {
		return abstractEstimateRepository.search(abstractEstimateSearchContract);
	}

	public void validateEstimates(AbstractEstimateRequest abstractEstimateRequest, BindingResult errors, Boolean isNew) {
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			if (estimate.getDateOfProposal() == null)
				throw new InvalidDataException("dateOfProposal", ErrorCode.NOT_NULL.getCode(), null);
			if (!estimate.getSpillOverFlag() && estimate.getDateOfProposal() != null
					&& new Date(estimate.getDateOfProposal()).after(new Date()))
				throw new InvalidDataException("dateOfProposal", "dateofproposal.future.date",
						estimate.getDateOfProposal().toString());
			if (estimate.getTenantId() == null)
				throw new InvalidDataException("tenantId", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),
						estimate.getTenantId());
			validateMasterData(estimate, errors, abstractEstimateRequest.getRequestInfo());
			
			AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
			searchContract.setIds(Arrays.asList(estimate.getId()));
			searchContract.setAbstractEstimateNumbers(Arrays.asList(estimate.getAbstractEstimateNumber()));
			searchContract.setTenantId(estimate.getTenantId());
			List<AbstractEstimate> oldEstimates = search(searchContract);
			if (isNew && !oldEstimates.isEmpty())
				throw new InvalidDataException("abstractEstimateNumber", ErrorCode.NON_UNIQUE_VALUE.getCode(),
						estimate.getAbstractEstimateNumber());
			searchContract.setAbstractEstimateNumbers(null);
			if (!isNew && estimate.getWorkFlowDetails() != null
					&& "Approve".equalsIgnoreCase(estimate.getWorkFlowDetails().getAction())) {
				if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
					throw new InvalidDataException("adminSanctionNumber", ErrorCode.NON_UNIQUE_VALUE.getCode(),
							estimate.getAdminSanctionNumber());
				if (StringUtils.isBlank(estimate.getCouncilResolutionNumber()))
					throw new InvalidDataException("councilResolutionNumber", ErrorCode.NON_UNIQUE_VALUE.getCode(),
							estimate.getCouncilResolutionNumber());
				if (estimate.getCouncilResolutionDate() == null)
					throw new InvalidDataException("councilResolutionDate", ErrorCode.NON_UNIQUE_VALUE.getCode(),
							estimate.getCouncilResolutionDate().toString());
				searchContract.setAdminSanctionNumbers(Arrays.asList(estimate.getAdminSanctionNumber()));
				oldEstimates = search(searchContract);
				if (!oldEstimates.isEmpty() && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId()))
					throw new InvalidDataException("adminSanctionNumber", ErrorCode.NOT_NULL.getCode(),
							estimate.getAdminSanctionNumber());
			}
			validateEstimateDetails(estimate, errors);
		}
	}
	
	private void validateEstimateDetails(AbstractEstimate estimate, BindingResult errors) {
		BigDecimal estimateAmount = BigDecimal.ZERO;
        for (final AbstractEstimateDetails aed : estimate.getAbstractEstimateDetails())
            estimateAmount = estimateAmount.add(aed.getEstimateAmount());
        if (Double.parseDouble(estimateAmount.toString()) <= 0)
        	throw new InvalidDataException("estimateAmount", "estimateamount.notvalid",
					estimateAmount.toString());
	}

	public void validateMasterData(AbstractEstimate abstractEstimate, BindingResult errors, RequestInfo requestInfo) {

		JSONArray responseJSONArray = null;

     if(abstractEstimate.getFund() != null && StringUtils.isNotBlank(abstractEstimate.getFund().getCode())) {
      responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUND_OBJECT, abstractEstimate.getFund().getCode(), abstractEstimate.
              getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
         if(responseJSONArray != null && responseJSONArray.isEmpty()) {
             throw new InvalidDataException("Fund","Invalid data for fund code", abstractEstimate.getFund().getCode());
      }  }
      if(abstractEstimate.getFunction() != null && StringUtils.isNotBlank(abstractEstimate.getFunction().getCode())) {
        responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUNCTION_OBJECT,abstractEstimate.getFunction().getCode(),
       abstractEstimate.getTenantId(),requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
          if(responseJSONArray != null && responseJSONArray.isEmpty()) {
              throw new InvalidDataException("Function", "Invalid data for function code", abstractEstimate.getFunction().getCode());
      } }

		if (abstractEstimate.getTypeOfWork() != null
				&& StringUtils.isNotBlank(abstractEstimate.getTypeOfWork().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.TYPEOFWORK_OBJECT,
                    abstractEstimate.getTypeOfWork().getCode(), abstractEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				throw new InvalidDataException("TypeOfWork", "Invalid data for estimate type of work",
						abstractEstimate.getTypeOfWork().getCode());
			}
		}
		if (abstractEstimate.getSubTypeOfWork() != null
				&& StringUtils.isNotBlank(abstractEstimate.getSubTypeOfWork().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SUBTYPEOFWORK_OBJECT,
                    abstractEstimate.getSubTypeOfWork().getCode(), abstractEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				throw new InvalidDataException("SubTypeOfWork", "Invalid data for estimate subtype of work",
						abstractEstimate.getSubTypeOfWork().getCode());
			}
		}
		if (abstractEstimate.getDepartment() != null
				& StringUtils.isNotBlank(abstractEstimate.getDepartment().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.DEPARTMENT_OBJECT,
                    abstractEstimate.getDepartment().getCode(), abstractEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				throw new InvalidDataException("Department", "Invalid data for estimate Department",
						abstractEstimate.getDepartment().getCode());
			}
		}


		if(abstractEstimate.getScheme() != null & StringUtils.isNotBlank(abstractEstimate.getScheme().getCode())) {
		   responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.
                 SCHEME_OBJECT, abstractEstimate.getScheme().getCode(), abstractEstimate
                 .getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if(responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Scheme", "Invalid data for estimate scheme", abstractEstimate.getScheme().getCode());
         } }

		 if(abstractEstimate.getSubScheme() != null & StringUtils.isNotBlank(abstractEstimate.getSubScheme().getCode())) {
		 responseJSONArray =  estimateUtils.getMDMSData(WorksEstimateServiceConstants.
                         SUBSCHEME_OBJECT, abstractEstimate.getSubScheme().getCode(), abstractEstimate.getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
         if(responseJSONArray != null && responseJSONArray.isEmpty()) {
                 throw new InvalidDataException("SubScheme",  "Invalid data for estimate SubScheme",
		  abstractEstimate.getSubScheme().getCode());
         } }

        if(abstractEstimate.getBudgetGroup() != null & StringUtils.isNotBlank(abstractEstimate.getBudgetGroup().getCode())) {
          responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.BUDGETGROUP_OBJECT, abstractEstimate.getBudgetGroup().getCode(),
                  abstractEstimate.getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
          if(responseJSONArray != null && responseJSONArray.isEmpty()) {
              throw new InvalidDataException("BudgetGroup", "Invalid data for estimate Budget Group",abstractEstimate.getBudgetGroup().getCode());
          } }

	/*	 * if(abstractEstimate.getWard() != null &
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
		 * abstractEstimate.getLocality().getCode()); } }*/

	}

	public AuditDetails setAuditDetails(final String userName, final Boolean isUpdate) {
		AuditDetails auditDetails = new AuditDetails();
		if (isUpdate) {
			auditDetails.setLastModifiedBy(userName);
			auditDetails.setLastModifiedTime(new Date().getTime());
		} else {
			auditDetails.setCreatedBy(userName);
			auditDetails.setCreatedTime(new Date().getTime());
			auditDetails.setLastModifiedBy(userName);
			auditDetails.setLastModifiedTime(new Date().getTime());
		}

		return auditDetails;
	}


}
