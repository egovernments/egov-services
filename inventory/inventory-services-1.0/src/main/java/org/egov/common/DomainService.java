package org.egov.common;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.ResponseInfo;
import org.egov.inv.model.ResponseInfo.StatusEnum;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainService {
	@Autowired
	protected LogAwareKafkaTemplate<String, Object> kafkaQue;
	public AuditDetails mapAuditDetails(RequestInfo requestInfo
			) {

			return AuditDetails.builder()
					.createdBy(requestInfo.getUserInfo().getId().toString())
					.lastModifiedBy(requestInfo.getUserInfo().getId().toString())
					.createdTime(requestInfo.getTs())
					.lastModifiedTime(requestInfo.getTs()).build();

		}

		public AuditDetails mapAuditDetailsForUpdate(RequestInfo requestInfo
			) {

			return AuditDetails.builder()
					.lastModifiedBy(requestInfo.getUserInfo().getId().toString())
					.lastModifiedTime(requestInfo.getTs()).build();
		}
	public ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return new ResponseInfo().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status(StatusEnum.SUCCESSFUL);
	}

}
