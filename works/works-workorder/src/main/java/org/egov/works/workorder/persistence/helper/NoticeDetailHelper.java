package org.egov.works.workorder.persistence.helper;

import org.egov.works.workorder.web.contract.NoticeDetail;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NoticeDetailHelper {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("notice")
	private String notice = null;

	@JsonProperty("remarks")
	private String remarks = null;

	public NoticeDetail toDomain() {

		NoticeDetail noticeDetail = new NoticeDetail();
		noticeDetail.setId(this.id);
		noticeDetail.setTenantId(this.tenantId);
		noticeDetail.setRemarks(this.remarks);
		noticeDetail.setNotice(this.notice);

		return noticeDetail;
	}

}
