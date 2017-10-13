package org.egov.property.utility;

import org.egov.models.Notice;
import org.egov.property.exception.NoticeMandatoryFieldException;
import org.springframework.stereotype.Service;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class NoticeValidator {

    public void validateNotice(Notice notice){
        if(isEmpty(notice.getApplicationNo()) && isEmpty(notice.getUpicNumber()) &&
                isEmpty(notice.getNoticeNumber()))
            throw new NoticeMandatoryFieldException();
    }
}
