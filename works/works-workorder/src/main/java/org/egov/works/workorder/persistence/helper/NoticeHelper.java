package org.egov.works.workorder.persistence.helper;

import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.Notice;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NoticeHelper {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("letterOfAcceptance")
	private String letterOfAcceptance = null;

	@JsonProperty("noticeNumber")
	private String noticeNumber = null;

	@JsonProperty("closingLine")
	private String closingLine = null;

	@JsonProperty("daysOfReply")
	private Integer daysOfReply = null;

	public Notice toDomain() {
		Notice notice = new Notice();
		notice.setId(this.id);
		notice.setTenantId(this.tenantId);
		LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
		letterOfAcceptance.setId(this.letterOfAcceptance);
		notice.setLetterOfAcceptance(letterOfAcceptance);
		notice.setNoticeNumber(this.noticeNumber);
		notice.setClosingLine(this.closingLine);
		notice.setDaysOfReply(this.daysOfReply);
		return notice;
	}

}
