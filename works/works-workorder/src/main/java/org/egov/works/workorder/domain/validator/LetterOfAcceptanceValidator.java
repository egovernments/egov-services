package org.egov.works.workorder.domain.validator;

import java.util.Date;
import java.util.HashMap;

import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.springframework.stereotype.Service;
import org.egov.tracer.model.CustomException;
import org.egov.works.workorder.config.Constants;

/**
 * Created by ramki on 11/11/17.
 */
@Service
public class LetterOfAcceptanceValidator {
	
	public void validateLetterOfAcceptance(final LetterOfAcceptance letterOfAcceptance) {
		
		HashMap<String, String> messages= new HashMap<>();
		if(letterOfAcceptance.getLoaDate() > new Date().getTime()) {
			messages.put(Constants.KEY_FUTUREDATE_LOADATE, Constants.MESSAGE_FUTUREDATE_LOADATE);
		}
		if(letterOfAcceptance.getFileDate() > new Date().getTime()) {
			messages.put(Constants.KEY_FUTUREDATE_FILEDATE, Constants.MESSAGE_FUTUREDATE_FILEDATE);
		}
		
//		if(letterOfAcceptance.getLoaDate() > ) {
//			messages.put(Constants.KEY_FUTUREDATE_L1DATE, Constants.MESSAGE_FUTUREDATE_L1DATE);
//		}
		
		if(messages != null && !messages.isEmpty())
			throw new CustomException(messages);
	}
}
