package org.egov.works.workorder.domain.service;

import org.egov.works.workorder.domain.repository.builder.IdGenerationRepository;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceRequest;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ramki on 11/11/17.
 */
@Service
@Transactional(readOnly = true)
public class LetterOfAcceptanceService {

	@Autowired
	private WorkOrderUtils workOrderUtils;

	@Autowired
	private EstimateService estimateService;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	public LetterOfAcceptanceResponse create(final LetterOfAcceptanceRequest letterOfAcceptanceRequest) {

		for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceRequest.getLetterOfAcceptances()) {

			// if (!estimate.getSpillOverFlag()) {
			String loaNumber = idGenerationRepository.generateLOANumber(letterOfAcceptance.getTenantId(),
					letterOfAcceptanceRequest.getRequestInfo());
			// TODO: check idgen to accept values to generate
			letterOfAcceptance.setLoaNumber(loaNumber);

		}
		// }
		workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), false);
		return new LetterOfAcceptanceResponse();
	}
}
