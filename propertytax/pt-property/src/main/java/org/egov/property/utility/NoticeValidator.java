package org.egov.property.utility;

import static org.springframework.util.StringUtils.isEmpty;

import org.egov.models.Notice;
import org.egov.property.exception.NoticeMandatoryFieldException;
import org.springframework.stereotype.Service;

@Service
public class NoticeValidator {

	public void validateNotice(Notice notice) {
		if (isEmpty(notice.getApplicationNo()) && isEmpty(notice.getUpicNumber()))
			throw new NoticeMandatoryFieldException();
	}
}
