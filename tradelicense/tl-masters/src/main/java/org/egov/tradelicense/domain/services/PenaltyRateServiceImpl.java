package org.egov.tradelicense.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.PenaltyRate;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.PenaltyRateRequest;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.PenaltyRateResponse;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.services.validator.PenaltyRateValidator;
import org.egov.tradelicense.persistence.repository.PenaltyRateRepository;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PenaltyRateService implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class PenaltyRateServiceImpl implements PenaltyRateService {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	PenaltyRateRepository penaltyRateRepository;

	@Autowired
	PenaltyRateValidator penaltyRateValidator;

	@Autowired
	Producer producer;
	
	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	@Transactional
	public PenaltyRateResponse createPenaltyRateMaster(String tenantId, PenaltyRateRequest penaltyRateRequest) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
		penaltyRateValidator.validatePenaltyRange(tenantId, penaltyRateRequest, true);
		producer.send(propertiesManager.getCreatePenaltyRateValidated(), penaltyRateRequest);
		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		penaltyRateResponse.setPenaltyRates(penaltyRateRequest.getPenaltyRates());
		penaltyRateResponse.setResponseInfo(responseInfo);

		return penaltyRateResponse;
	}

	@Override
	@Transactional
	public void createPenaltyRate(PenaltyRateRequest penaltyRateRequest) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();

		for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {

			try {

				Long id = penaltyRateRepository.craeatePenaltyRate(penaltyRate.getTenantId(), penaltyRate);
				penaltyRate.setId(id);

			} catch (Exception e) {

				throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
			}
		}
	}

	@Override
	@Transactional
	public PenaltyRateResponse updatePenaltyRateMaster(PenaltyRateRequest penaltyRateRequest) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
		penaltyRateValidator.validatePenaltyRange(null, penaltyRateRequest, false);
		producer.send(propertiesManager.getUpdatePenaltyRateValidated(), penaltyRateRequest);
		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		penaltyRateResponse.setPenaltyRates(penaltyRateRequest.getPenaltyRates());
		penaltyRateResponse.setResponseInfo(responseInfo);

		return penaltyRateResponse;
	}

	@Override
	@Transactional
	public void updatePenaltyRate(PenaltyRateRequest penaltyRateRequest) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
		String applicationType = null;
		if (penaltyRateRequest.getPenaltyRates().get(0).getApplicationType() != null) {
			applicationType = penaltyRateRequest.getPenaltyRates().get(0).getApplicationType().toString();
		}
		List<PenaltyRate> penaltyRates = new ArrayList<PenaltyRate>();
		String tenantId = penaltyRateRequest.getPenaltyRates().get(0).getTenantId();

		try {
			penaltyRates = penaltyRateRepository.searchPenaltyRate(tenantId, null, applicationType, null, null);
		} catch (Exception e) {
			throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
		}
		
		List<PenaltyRate> penaltyRatesToDelete = new ArrayList<PenaltyRate>();
		List<PenaltyRate> penaltyRatesToInsert = new ArrayList<PenaltyRate>();
		List<PenaltyRate> penaltyRatesToUpdate = new ArrayList<PenaltyRate>();

		if (penaltyRates != null && penaltyRateRequest.getPenaltyRates() != null) {

			if (penaltyRates.size() <= penaltyRateRequest.getPenaltyRates().size()) {

				for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
					
					
					AuditDetails auditDetails = penaltyRate.getAuditDetails();
					auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
					penaltyRate.setAuditDetails(auditDetails);

					if (penaltyRate.getId() != null) {

						Boolean isPenaltyExists = false;
						for (int i = 0; i < penaltyRates.size(); i++) {

							Long id = penaltyRates.get(i).getId();
							if (id != null && id.equals(penaltyRate.getId())) {
								isPenaltyExists = true;
								// getting the penalty records to update
								penaltyRatesToUpdate.add(penaltyRate);
								// remove matched record so that
								// remaining db records will be deleted
								penaltyRates.remove(i);
							}
						}

						if (!isPenaltyExists) {
							// getting the penalty records to insert
							penaltyRatesToInsert.add(penaltyRate);
						}

					} else {
						// getting the penalty records to insert
						penaltyRatesToInsert.add(penaltyRate);
					}
				}
				// get the penalty records to delete
				if (penaltyRates.size() > 0) {

					penaltyRatesToDelete.addAll(penaltyRates);
				}

			} else {
				
				List<PenaltyRate> requestPenaltyRates = penaltyRateRequest.getPenaltyRates();

				for (PenaltyRate penaltyRate : penaltyRates) {
				
					
					AuditDetails auditDetails = penaltyRate.getAuditDetails();
					auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);

					Boolean isPenaltyExists = false;

					for (int i = 0; i < requestPenaltyRates.size(); i++) {

						requestPenaltyRates.get(i).setAuditDetails(auditDetails);
						Long id = requestPenaltyRates.get(i).getId();
						if (id != null && id.equals(penaltyRate.getId())) {
							isPenaltyExists = true;
							// getting the penalty records to update
							penaltyRatesToUpdate.add(requestPenaltyRates.get(i));
							// remove matched record so that remaining
							// request records will be inserted
							requestPenaltyRates.remove(i);
						}
					}

					if (!isPenaltyExists) {
						// getting the penalty records to delete
						penaltyRatesToDelete.add(penaltyRate);
					}

				}
				// get the penalty records to insert
				if (requestPenaltyRates.size() > 0) {

					penaltyRatesToInsert.addAll(requestPenaltyRates);
				}
			}
		}
		
		for(PenaltyRate penaltyRate : penaltyRatesToDelete){
			penaltyRateRepository.deletePenaltyRates(penaltyRate.getId());
		}
		
		for(PenaltyRate penaltyRate : penaltyRatesToUpdate){
			penaltyRateRepository.updatePenaltyRate( penaltyRate );
		}

		for(PenaltyRate penaltyRate : penaltyRatesToInsert){
			penaltyRateRepository.craeatePenaltyRate(penaltyRate.getTenantId(), penaltyRate);
		}

	}

	@Override
	public PenaltyRateResponse getPenaltyRateMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String applicationType, Integer pageSize, Integer offSet) {

		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		try {
			List<PenaltyRate> penaltyRates = penaltyRateRepository.searchPenaltyRate(tenantId, ids, applicationType,
					pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			penaltyRateResponse.setPenaltyRates(penaltyRates);
			penaltyRateResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
		}

		return penaltyRateResponse;
	}

}