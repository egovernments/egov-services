package org.egov.tradelicense.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.models.AuditDetails;
import org.egov.models.PenaltyRate;
import org.egov.models.PenaltyRateRequest;
import org.egov.models.PenaltyRateResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.services.validator.PenaltyRateValidator;
import org.egov.tradelicense.persistence.repository.PenaltyRateRepository;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	PropertiesManager propertiesManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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

				throw new InvalidInputException(requestInfo);
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
	public void updatePenaltyRate(PenaltyRateRequest penaltyRateRequest) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();

		for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {

			try {

				penaltyRate = penaltyRateRepository.updatePenaltyRate(penaltyRate);

			} catch (Exception e) {

				throw new InvalidInputException(requestInfo);
			}
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
			throw new InvalidInputException(requestInfo);
		}

		return penaltyRateResponse;
	}

}