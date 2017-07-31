package org.egov.tradelicense.services;

import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.PenaltyRate;
import org.egov.models.PenaltyRateRequest;
import org.egov.models.PenaltyRateResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.tradelicense.exception.InvalidInputException;
import org.egov.tradelicense.repository.PenaltyRateRepository;
import org.egov.tradelicense.repository.helper.PenaltyRateHelper;
import org.egov.tradelicense.repository.helper.UtilityHelper;
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
	private PenaltyRateHelper penaltyRateHelper;

	@Autowired
	private UtilityHelper utilityHelper;

	@Autowired
	PenaltyRateRepository penaltyRateRepository;

	@Override
	@Transactional
	public PenaltyRateResponse createPenaltyRateMaster(String tenantId, PenaltyRateRequest penaltyRateRequest) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
		penaltyRateHelper.validatePenaltyRange(tenantId, penaltyRateRequest, true);
		AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetals(requestInfo);
		for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
			try {
				penaltyRate.setAuditDetails(auditDetails);
				Long id = penaltyRateRepository.craeatePenaltyRate(tenantId, penaltyRate);
				penaltyRate.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(requestInfo);
			}
		}

		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		penaltyRateResponse.setPenaltyRates(penaltyRateRequest.getPenaltyRates());
		penaltyRateResponse.setResponseInfo(responseInfo);

		return penaltyRateResponse;
	}

	@Override
	@Transactional
	public PenaltyRateResponse updatePenaltyRateMaster(PenaltyRateRequest penaltyRateRequest) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
		penaltyRateHelper.validatePenaltyRange(null,penaltyRateRequest,false);
		for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
			try {
				Long updatedTime = new Date().getTime();
				penaltyRate.getAuditDetails().setLastModifiedTime(updatedTime);
				penaltyRate.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUsername());
				penaltyRate = penaltyRateRepository.updatePenaltyRate(penaltyRate);
			} catch (Exception e) {
				throw new InvalidInputException(requestInfo);
			}
		}

		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		penaltyRateResponse.setPenaltyRates(penaltyRateRequest.getPenaltyRates());
		penaltyRateResponse.setResponseInfo(responseInfo);

		return penaltyRateResponse;
	}

	@Override
	public PenaltyRateResponse getPenaltyRateMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String applicationTypeId, Integer pageSize, Integer offSet) {

		PenaltyRateResponse penaltyRateResponse = new PenaltyRateResponse();
		try {
			List<PenaltyRate> penaltyRates = penaltyRateRepository.searchPenaltyRate(tenantId, ids, applicationTypeId,
					pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			penaltyRateResponse.setPenaltyRates(penaltyRates);
			penaltyRateResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		return penaltyRateResponse;
	}
}