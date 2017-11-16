package org.egov.works.services.domain.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.works.services.config.Constants;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.springframework.stereotype.Service;

@Service
public class EstimateAppropriationValidator {

	public void validateSearchContract(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
		Map<String, String> messages = new HashMap<>();
		if ((estimateAppropriationSearchContract.getObjectNumber() != null
				|| estimateAppropriationSearchContract.getAbstractEstimateNumbers() != null
				|| estimateAppropriationSearchContract.getDetailedEstimateNumber() != null)
				&& estimateAppropriationSearchContract.getObjectType() == null) {
			messages.put(Constants.KEY_OBJECTTYPE_INVALID,
					Constants.MESSAGE_OBJECTTYPE_INVALID);
			throw new CustomException(messages);

		}
	}

}
